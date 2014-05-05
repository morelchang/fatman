package tw.com.mds.fet.femtocellportal.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.core.ApZone;
import tw.com.mds.fet.femtocellportal.core.ConnectionMode;
import tw.com.mds.fet.femtocellportal.core.FemtoProfile;
import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.core.LocationDetectMode;
import tw.com.mds.fet.femtocellportal.core.LoginUser;
import tw.com.mds.fet.femtocellportal.core.MaskedFemtoProfileWrapper;
import tw.com.mds.fet.femtocellportal.core.MaskedFemtoUserWrapper;
import tw.com.mds.fet.femtocellportal.core.Position;
import tw.com.mds.fet.femtocellportal.core.ProvisionService;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.core.UePermissionMode;
import tw.com.mds.fet.femtocellportal.core.UserEquipment;
import tw.com.mds.fet.femtocellportal.core.UserLogType;
import tw.com.mds.fet.femtocellportal.gis.AddressFormatException;
import tw.com.mds.fet.femtocellportal.gis.NoCellFoundException;
import tw.com.mds.fet.femtocellportal.util.Utils;

public class ModifyProfileAction extends IdentityAction {
	

	public static enum Mode {
		CREATE_USER_PROFILE,
		CREATE_PROFILE,
		DISPLAY,
		CHANGE_PERMISSIONLIST,
		CHANGE_PERMISSIONLIST_BY_FEMTOUSER,
		CHANGE_UEPERMISSIONMODE,
		CHANGE_PROFILE,
		CHANGE_LOCATIONDETECTMODE
	}

	private static final long serialVersionUID = 1L;

	private static final String RESULT_SEARCH_USER = "searchUser";
	private static final String RESULT_SEARCH_PROFILE = "searchProfile";

	private ProvisionService provisionService;
	
	// available list
	private List<ApZone> availableApZones = new ArrayList<ApZone>();
	private List<UePermissionMode> availableUePermissionModes = Arrays.asList(UePermissionMode.CLOSE, UePermissionMode.OPEN);
	private List<ConnectionMode> availableConnectionModes = Arrays.asList(ConnectionMode.PPPOE, ConnectionMode.DHCP, ConnectionMode.STATIC_IP);
	private List<LocationDetectMode> availableLocationDetectModes = Arrays.asList(LocationDetectMode.SECOND_AND_THIRG_G, LocationDetectMode.NOT_PERFORMED);

	// current selected user status
	private FemtoUser current;
	private FemtoProfile currentProfile;
	private Long selectedApZoneOid;

	// config
	private int initPermissionListSize;
	private int maxPermissionListSize;
	private int maxCellSize = 6;
	
	// properties
	private Mode mode;
	private boolean requiredLonLat;
	private BigDecimal manualLon;
	private BigDecimal manualLat;
	private boolean enableLocalAuthentication;

	public void prepare() throws Exception {
		availableApZones = administrationService.listAllApZones();
		initPermissionListSize = provisionService.getMaxPermissionListSize();
		if (currentProfile != null) {
			arrangeProfile(currentProfile);
			complementPermissionListSize(currentProfile.getPermissionList());
		}
		maxPermissionListSize = provisionService.getMaxPermissionListSize();
		
		requiredLonLat = false;
		manualLon = null;
		manualLat = null;
		enableLocalAuthentication = provisionService.isEnableLocalAuthentication();
		super.prepare();
	}

	private void complementPermissionListSize(List<UserEquipment> permissionList) {
		if (permissionList.size() >= initPermissionListSize) {
			return;
		}
		
		for (int i = permissionList.size(); i < initPermissionListSize; i++) {
			permissionList.add(new UserEquipment());
		}
	}

	private void arrangeProfile(FemtoProfile profile) {
		// compact permissionlist
		Iterator<UserEquipment> it = profile.getPermissionList().iterator();
		while (it.hasNext()) {
			if (StringUtils.isEmpty(it.next().getMsisdn())) {
				it.remove();
			}
		}
		
		// replace manual position
		if (manualLon != null && manualLat != null) {
			Position manualPos = new Position(manualLon, manualLat);
			profile.setPosition(manualPos);
			profile.setLocatingState(null);
		}
	}

	private FemtoProfile createDefaultProfile() throws ServiceException {
		FemtoProfile p = new FemtoProfile();
		p.setMaxUserCount(provisionService.getDefaultMaxUserEquipmentSize());
		p.setMaxPermissionListSize(provisionService.getMaxPermissionListSize());
		p.setLocationDetectMode(LocationDetectMode.SECOND_AND_THIRG_G);
		complementPermissionListSize(p.getPermissionList());
		return p;
	}

	private void clearCurrent() {
		current = null;
		selectProfile(null, current);
	}

	public String displayAddUserProfile() throws ServiceException {
		selectProfile(createDefaultProfile(), new FemtoUser());
		mode = Mode.CREATE_USER_PROFILE;
		return SUCCESS;
	}

	public String displayDetail() throws ServiceException {
		mode = Mode.DISPLAY;
		return refreshProfileByOid();
	}
	
	public String displayCreateProfile() throws ServiceException {
		mode = Mode.CREATE_PROFILE;
		current = provisionService.findUserByOid(current.getOid());
		if (current == null) {
			addActionError("用戶資訊已不存在，請重新操作");
			return RESULT_SEARCH_USER;
		}
		selectProfile(createDefaultProfile(), current);
		return SUCCESS;
	}
	
	public String displayChangeProfile() throws ServiceException {
		mode = Mode.CHANGE_PROFILE;;
		return refreshProfileByOid();
	}

	public String displayChangePermissionList() throws ServiceException {
		mode = Mode.CHANGE_PERMISSIONLIST;
		return refreshProfileByOid();
	}
	
	public String displayChangePermissionListByFemtoUser() throws ServiceException {
		mode = Mode.CHANGE_PERMISSIONLIST_BY_FEMTOUSER;
		this.current = getLoginFemtoUser();
		return refreshProfileByOid();
	}

	public String displayChangeUePermissionMode() throws ServiceException {
		mode = Mode.CHANGE_UEPERMISSIONMODE;
		return refreshProfileByOid();
	}
	
	public String displayChangeLocationDetectMode() throws ServiceException {
		mode = Mode.CHANGE_LOCATIONDETECTMODE;
		return refreshProfileByOid();
	}
	
	private String refreshProfileByOid() throws ServiceException {
		currentProfile = provisionService.findProfileByOid(currentProfile.getOid());
		if (currentProfile == null) {
			addActionError("該申裝資訊已經不存在，請重新操作");
			return ERROR;
		}
		currentProfile = provisionService.syncPermissionList(currentProfile);
		return selectProfile(currentProfile, currentProfile.getUser());
	}

	private String selectProfile(FemtoProfile profile, FemtoUser user) {
		if (profile == null) {
			currentProfile = null;
			selectedApZoneOid = 0L;
			return SUCCESS;
		}
		
		// femto user can only select his own profiles
		if (mode == Mode.CHANGE_PERMISSIONLIST_BY_FEMTOUSER && !profile.getUser().equals(getLoginFemtoUser())) {
			return LOGIN;
		}
		
		complementPermissionListSize(profile.getPermissionList());
		currentProfile = profile;
		
		current = user;

		if (profile.getApZone() == null) {
			selectedApZoneOid = 0L;
		} else {
			selectedApZoneOid = profile.getApZone().getOid();
		}
		return SUCCESS;
	}
	
	public String save() throws ServiceException {
		switch (mode) {
		case CREATE_USER_PROFILE:
			return saveUserProfile();
		case CREATE_PROFILE:
			return createProfile();
		case CHANGE_PROFILE:
			return changeProfile();
		case CHANGE_PERMISSIONLIST:
			return savePermissionList();
		case CHANGE_UEPERMISSIONMODE:
			return changeUePermissionMode();
		case CHANGE_PERMISSIONLIST_BY_FEMTOUSER:
			return savePermissionList();
		case CHANGE_LOCATIONDETECTMODE:
			return changeLocationDetectMode();
		case DISPLAY:
			return displayDetail();
		}
		
		addActionMessage("系統錯誤,原因: incorrect mode value:{0}, 請重新操作", mode);
		return ERROR;
	}
	
	private String changeLocationDetectMode() {
		// validate permission list
		arrangeProfile(currentProfile);
		if (!validateProfile(currentProfile, false)) {
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}
		
		try {
			FemtoProfile oldProfile = provisionService.findProfileByOid(currentProfile.getOid());
			if (oldProfile == null) {
				addActionError("資料已不存在，請重新操作");
				complementPermissionListSize(currentProfile.getPermissionList());
				return ERROR;	
			}
			
//			currentProfile.setOid(null);
			currentProfile = provisionService.changeProfile(oldProfile, currentProfile, getLoginAdminUser());
			logEvent(UserLogType.CHANGE_LOCATIONDETECTMODE, oldProfile, Utils.format("用戶:{0}, 原鎖定模式:{1}, 變更後鎖定模式:{2}",
					describe(current), oldProfile.getLocationDetectMode(), currentProfile.getLocationDetectMode()));

		} catch (NoCellFoundException e) {
			addActionError("該地址查無基地台訊號，請把網路鎖定模式改成不鎖定，並重新儲存", getCurrent().getUserName(), e.getMessage());
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
			
		} catch (ServiceException e) {
			addActionError("用戶  {0} 儲存失敗，原因：{1}", getCurrent().getUserName(), e.getMessage());
			logEvent(UserLogType.CHANGE_LOCATIONDETECTMODE, currentProfile, Utils.format(
					"錯誤, {0}, 用戶:{1}, 申裝資訊:{2}", 
					e.getMessage(), describe(current), describe(currentProfile)));
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}
		
		addActionMessage("用戶 {0} 儲存成功", getCurrent().getUserName());
		complementPermissionListSize(currentProfile.getPermissionList());
		selectProfile(currentProfile, current);
		return SUCCESS;
	}

	private String changeUePermissionMode() {
		// validate permission list
		arrangeProfile(currentProfile);
		if (!validateProfile(currentProfile, false)) {
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}
		
		try {
			FemtoProfile oldProfile = provisionService.findProfileByOid(currentProfile.getOid());
			if (oldProfile == null) {
				addActionError("資料已不存在，請重新操作");
				complementPermissionListSize(currentProfile.getPermissionList());
				return ERROR;	
			}
			
			currentProfile = provisionService.changeProfile(oldProfile, currentProfile, getLoginAdminUser());
			logEvent(UserLogType.CHANGE_UEPERMISSIONMODE, oldProfile, Utils.format("用戶:{0}, 原網路模式:{1}, 變更後網路模式:{2}",
					describe(current), oldProfile.getUePermissionMode(), currentProfile.getUePermissionMode()));
		} catch (ServiceException e) {
			addActionError("用戶  {0} 儲存失敗，原因：{1}", getCurrent().getUserName(), e.getMessage());
			logEvent(UserLogType.CHANGE_UEPERMISSIONMODE, currentProfile, Utils.format(
					"錯誤, {0}, 用戶:{1}, 申裝資訊:{2}", 
					e.getMessage(), describe(current), describe(currentProfile)));
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}
		
		addActionMessage("用戶 {0} 儲存成功", getCurrent().getUserName());
		complementPermissionListSize(currentProfile.getPermissionList());
		selectProfile(currentProfile, current);
		return SUCCESS;
	}

	public String saveUserProfile() throws ServiceException {
		arrangeProfile(currentProfile);
		if (!validateUser(current)) {
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}
		
		if (!validateProfile(currentProfile, true)) {
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}
		
		try {
			current = provisionService.createUser(current, currentProfile, getLoginAdminUser());
			currentProfile = current.getProfiles().get(0);
			logEvent(UserLogType.CREATE_USER_AND_PROFILE, currentProfile, Utils.format("用戶:{0}, 申裝明細:{1}",
					describe(current), describe(currentProfile)));

		} catch (NoCellFoundException e) {
			addActionError("該地址查無基地台訊號，請把網路鎖定模式改成不鎖定，並重新儲存", getCurrent().getUserName(), e.getMessage());
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
			
		} catch (AddressFormatException e) {
			complementPermissionListSize(currentProfile.getPermissionList());
			requiredLonLat = true;
			return INPUT;
			
		} catch (ServiceException e) {
			addActionError("用戶  {0} 儲存失敗，原因：{1}", getCurrent().getUserName(), e.getMessage());
			logEvent(UserLogType.CREATE_USER_AND_PROFILE, currentProfile, Utils.format(
					"錯誤, {0}, 用戶:{1}, 申裝資訊:{2}", 
					e.getMessage(), describe(current), describe(currentProfile)));
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}
		
		addActionMessage("用戶 {0} 儲存成功", getCurrent().getUserName());
		complementPermissionListSize(currentProfile.getPermissionList());
		
		clearCurrent();
		return displayAddUserProfile();
	}

	private boolean validateUser(FemtoUser user) {
		boolean validated = true;
		validated = validated && validateStringEmpty(user.getUserName(), "用戶名稱為必填");
		validated = validated && validateStringEmpty(user.getMobile(), "用戶門號為必填");
		if (enableLocalAuthentication) {
			validated = validated && validateStringEmpty(user.getAccount(), "用戶帳號為必填");
			validated = validated && validateStringLengthIsOver(user.getPassword(), 0, "用戶密碼為必填");
		}
		List<FemtoUser> existed = provisionService.findUsersByMobile(user.getMobile());
		if (!existed.isEmpty()) {
			addActionError("用戶門號:{0}已存在, 不可重複", user.getMobile());
			return false;
		}
		if (!StringUtils.isBlank(user.getAccount()) && 
				provisionService.findUserByAccount(user.getAccount()) != null) {
			addActionError("用戶帳號:{0}已存在, 不可重複", user.getAccount());
			return false;
		}
		return validated;
	}

	private boolean validateProfile(FemtoProfile profile, boolean validateDuplicated) {
		// validate properties
		boolean validated = true;
		validated = validated && validateStringEmpty(profile.getAddress(), "裝機地址為必填");
		validated = validated && validateStringEmpty(profile.getApei(), "APEI為必填");
		validated = validated && validateStringEmpty(profile.getImsi(), "IMSI為必填");
		validated = validated && validateEmpty(profile.getConnectionMode(), "連線方式為必填");
		validated = validated && validateEmpty(profile.getMaxPermissionListSize(), "使用者白名單數限制為必填");
		validated = validated && validateEmpty(profile.getMaxUserCount(), "同時最大用戶數為必填");
		validated = validated && validateEmpty(profile.getUePermissionMode(), "網路模式為必填");
		// validate static ip
		if (profile.getConnectionMode() == ConnectionMode.STATIC_IP) {
			validated = validated && validateStringEmpty(profile.getStaticIp(), "STATIC IP為必填");
		}
		// validate permission list
		validated = validated && validatePermissionList(profile);
		// validate ap zone
		ApZone selectedApZone = fetchSelectedApZone();
		profile.setApZone(selectedApZone);
		validated = validated && validateEmpty(profile.getApZone(), "AP Zone為必填");
		// validate apei 
		if (validateDuplicated) {
			FemtoProfile exists = provisionService.findProfileByApei(profile.getApei());
			if (exists != null && !exists.equals(profile)) {
				addActionError("APEI:{0} 已存在系統，不可重複", exists.getApei());
				validated = false;
			}
		}
		// validate imsi 
		if (validateDuplicated) {
			FemtoProfile exists = provisionService.findProfileByImsi(profile.getImsi());
			if (exists != null && !exists.equals(profile)) {
				addActionError("IMSI:{0} 已存在系統，不可重複", exists.getImsi());
				validated = false;
			}
		}
		return validated;
	}

	private boolean validatePermissionList(FemtoProfile profile) {
		boolean validated = true;
		if (profile.isLackingPermissionList()) {
			addActionError("至少需要輸入一組用戶白名單門號");
			validated = false;
		}
		if (profile.isOverMaxPermissionListSize()) {
			addActionError("用戶白名單數目:{0}超過最大限制數量:{1}", 
					profile.getPermissionList().size(), profile.getMaxPermissionListSize());
			validated = false;
		}
		return validated;
	}

	private String changeProfile() {
		// validate permission list
		currentProfile.setPosition(null);
		arrangeProfile(currentProfile);
		if (!validateProfile(currentProfile, true)) {
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}
		
		try {
			FemtoProfile oldProfile = provisionService.findProfileByOid(currentProfile.getOid());
			if (oldProfile == null) {
				addActionError("資料已不存在，請重新操作");
				complementPermissionListSize(currentProfile.getPermissionList());
				return ERROR;	
			}
			
			currentProfile = provisionService.changeProfile(oldProfile, currentProfile, getLoginAdminUser());
			logEvent(UserLogType.CHANGE_PROFILE, oldProfile, Utils.format("用戶:{0}, 原申裝明細:{1}, 變更後申裝明細:{2}",
					describe(current), describe(oldProfile), describe(currentProfile)));

		} catch (NoCellFoundException e) {
			addActionError("該地址查無基地台訊號，請把網路鎖定模式改成不鎖定，並重新儲存", getCurrent().getUserName(), e.getMessage());
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
			
		} catch (AddressFormatException e) {
			complementPermissionListSize(currentProfile.getPermissionList());
			requiredLonLat = true;
			return INPUT;
			
		} catch (ServiceException e) {
			addActionError("用戶  {0} 儲存失敗，原因：{1}", getCurrent().getUserName(), e.getMessage());
			logEvent(UserLogType.CHANGE_PROFILE, currentProfile, Utils.format(
					"錯誤, {0}, 用戶:{1}, 申裝資訊:{2}", 
					e.getMessage(), describe(current), describe(currentProfile)));
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}
		
		addActionMessage("用戶 {0} 儲存成功", getCurrent().getUserName());
		complementPermissionListSize(currentProfile.getPermissionList());
		selectProfile(currentProfile, current);
		return SUCCESS;
	}

	private String createProfile() {
		arrangeProfile(currentProfile);
		if (!validateProfile(currentProfile, true)) {
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}
		
		try {
			current = provisionService.findUserByOid(current.getOid());
			currentProfile = provisionService.createProfile(current, currentProfile, getLoginAdminUser());
			logEvent(UserLogType.CREATE_PROFILE, currentProfile, Utils.format("用戶:{0}, 申裝明細:{1}",
					describe(current), describe(currentProfile)));
			
		} catch (AddressFormatException e) {
			complementPermissionListSize(currentProfile.getPermissionList());
			requiredLonLat = true;
			return INPUT;
		
		} catch (NoCellFoundException e) {
			addActionError("該地址查無基地台訊號，請把網路鎖定模式改成不鎖定，並重新儲存", getCurrent().getUserName(), e.getMessage());
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
			
		} catch (ServiceException e) {
			addActionError("用戶  {0} 儲存失敗，原因：{1}", getCurrent().getUserName(), e.getMessage());
			logEvent(UserLogType.CREATE_PROFILE, currentProfile, Utils.format(
					"錯誤, {0}, 用戶:{1}, 申裝資訊:{2}", 
					e.getMessage(), describe(current), describe(currentProfile)));
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}
		
		addActionMessage("用戶 {0} 儲存成功", getCurrent().getUserName());
		complementPermissionListSize(currentProfile.getPermissionList());
		selectProfile(currentProfile, current);
		mode = Mode.DISPLAY;
		return SUCCESS;
	}

	public String savePermissionList() {
		arrangeProfile(currentProfile);
		if (!validatePermissionList(currentProfile)) {
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}
		
		try {
			FemtoProfile oldProfile = provisionService.findProfileByOid(currentProfile.getOid());
			if (oldProfile == null) {
				addActionError("資料已不存在，請重新操作");
				complementPermissionListSize(currentProfile.getPermissionList());
				return ERROR;	
			}
			
			provisionService.savePermissionList(currentProfile, fetchLoginUser());
			logEvent(UserLogType.CHANGE_PERMISSION_LIST, oldProfile, Utils.format("用戶:{0}, 原名單:{1}, 變更後名單:{2}",
					describe(current), describe(oldProfile.getPermissionList()), describe(currentProfile.getPermissionList())));
			
		} catch (ServiceException e) {
			addActionError("用戶  {0} 儲存失敗，原因：{1}", getCurrent().getUserName(), e.getMessage());
			logEvent(UserLogType.CHANGE_PERMISSION_LIST, currentProfile, Utils.format(
					"錯誤, {0}, 用戶:{1}, 申裝資訊:{2}", 
					e.getMessage(), describe(current), describe(currentProfile)));
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}

		addActionMessage("用戶 {0} 儲存成功", getCurrent().getUserName());
		complementPermissionListSize(currentProfile.getPermissionList());
		return SUCCESS;
	}

	private LoginUser fetchLoginUser() {
		switch (mode) {
		case CHANGE_PERMISSIONLIST_BY_FEMTOUSER:
			return getLoginFemtoUser();
		default:
			return getLoginAdminUser();
		}
	}

	private ApZone fetchSelectedApZone() {
		for (ApZone zone : availableApZones) {
			if (zone.getOid().equals(selectedApZoneOid)) {
				return zone;
			}
		}
		return null;
	}

	public String leaveModifyProfile() {
		switch (mode) {
		case CREATE_PROFILE:
			return RESULT_SEARCH_USER;
		case CHANGE_PROFILE:
		case CHANGE_PERMISSIONLIST:
		case CHANGE_UEPERMISSIONMODE:
		case CHANGE_PERMISSIONLIST_BY_FEMTOUSER:
		case CHANGE_LOCATIONDETECTMODE:
		case DISPLAY:
			return RESULT_SEARCH_PROFILE;
		}
		return SUCCESS;
	}
	
	public MaskedFemtoUserWrapper getCurrent() {
		if (current == null) {
			current = new FemtoUser();
		}
		return MaskedFemtoUserWrapper.wrap(current);
	}

	public void setCurrent(MaskedFemtoUserWrapper current) {
		this.current = current.getDelegatee();
	}

	public int getInitPermissionListSize() {
		return initPermissionListSize;
	}

	public void setInitPermissionListSize(int initPermissionListSize) {
		this.initPermissionListSize = initPermissionListSize;
	}

	public MaskedFemtoProfileWrapper getCurrentProfile() {
		if (currentProfile == null) {
			currentProfile = new FemtoProfile();
		}
		return MaskedFemtoProfileWrapper.wrap(currentProfile);
	}

	public void setCurrentProfile(MaskedFemtoProfileWrapper currentProfile) {
		this.currentProfile = currentProfile.getDelegatee();
	}

	public List<ApZone> getAvailableApZones() {
		return availableApZones;
	}

	public Long getSelectedApZoneOid() {
		return selectedApZoneOid;
	}

	public void setSelectedApZoneOid(Long selectedApZoneOid) {
		this.selectedApZoneOid = selectedApZoneOid;
	}

	public List<UePermissionMode> getAvailableUePermissionModes() {
		return availableUePermissionModes;
	}

	public int getMaxCellSize() {
		return maxCellSize;
	}

	public void setMaxCellSize(int maxCellSize) {
		this.maxCellSize = maxCellSize;
	}

	public int getMaxPermissionListSize() {
		return maxPermissionListSize;
	}

	public void setMaxPermissionListSize(int maxPermissionListSize) {
		this.maxPermissionListSize = maxPermissionListSize;
	}

	public List<ConnectionMode> getAvailableConnectionModes() {
		return availableConnectionModes;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public ProvisionService getProvisionService() {
		return provisionService;
	}

	public void setProvisionService(ProvisionService provisionService) {
		this.provisionService = provisionService;
	}

	public List<LocationDetectMode> getAvailableLocationDetectModes() {
		return availableLocationDetectModes;
	}

	public boolean isRequiredLonLat() {
		return requiredLonLat;
	}

	public void setRequiredLonLat(boolean requiredLonLat) {
		this.requiredLonLat = requiredLonLat;
	}

	public BigDecimal getManualLon() {
		return manualLon;
	}

	public void setManualLon(BigDecimal manualLon) {
		this.manualLon = manualLon;
	}

	public BigDecimal getManualLat() {
		return manualLat;
	}

	public void setManualLat(BigDecimal manualLat) {
		this.manualLat = manualLat;
	}

	public boolean isEnableLocalAuthentication() {
		return enableLocalAuthentication;
	}

}
