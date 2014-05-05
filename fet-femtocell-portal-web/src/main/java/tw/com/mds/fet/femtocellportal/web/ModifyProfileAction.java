package tw.com.mds.fet.femtocellportal.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.core.ApZone;
import tw.com.mds.fet.femtocellportal.core.ConnectionMode;
import tw.com.mds.fet.femtocellportal.core.FemtoProfile;
import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.core.LocationDetectMode;
import tw.com.mds.fet.femtocellportal.core.LoginUser;
import tw.com.mds.fet.femtocellportal.core.MaskedFemtoProfileWrapper;
import tw.com.mds.fet.femtocellportal.core.MaskedFemtoUserWrapper;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.core.UePermissionMode;
import tw.com.mds.fet.femtocellportal.core.UserEquipment;
import tw.com.mds.fet.femtocellportal.core.UserLogType;
import tw.com.mds.fet.femtocellportal.gis.AddressFormatException;
import tw.com.mds.fet.femtocellportal.gis.NoCellFoundException;
import tw.com.mds.fet.femtocellportal.test.TestData;
import tw.com.mds.fet.femtocellportal.util.Utils;

public class ModifyProfileAction extends IdentityAction {
	

	public static enum Mode {
		CREATE_USER_PROFILE,
		CREATE_PROFILE,
		DISPLAY,
		CHANGE_PERMISSIONLIST,
		CHANGE_PERMISSIONLIST_BY_FEMTOUSER,
		CHANGE_UEPERMISSIONMODE,
		CHANGE_PROFILE
	}

	private static final long serialVersionUID = 1L;

	private static final String RESULT_SEARCH_USER = "searchUser";
	private static final String RESULT_SEARCH_PROFILE = "searchProfile";

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
	private Mode mode = Mode.CHANGE_PERMISSIONLIST_BY_FEMTOUSER;

	public void prepare() throws Exception {
		availableApZones = administrationService.listAllApZones();
		initPermissionListSize = provisionService.getMaxPermissionListSize();
		if (currentProfile != null) {
			compactPermissionList(currentProfile.getPermissionList());
			complementPermissionListSize(currentProfile.getPermissionList());
		}
		maxPermissionListSize = provisionService.getMaxPermissionListSize();
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

	private void compactPermissionList(List<UserEquipment> permissionList) {
		Iterator<UserEquipment> it = permissionList.iterator();
		while (it.hasNext()) {
			if (StringUtils.isEmpty(it.next().getMsisdn())) {
				it.remove();
			}
		}
	}

	private FemtoProfile createDefaultProfile() {
		FemtoProfile p = new FemtoProfile();
		try {
			p.setMaxUserCount(provisionService.getDefaultMaxUserEquipmentSize());
			p.setMaxPermissionListSize(provisionService.getMaxPermissionListSize());
			p.setLocationDetectMode(LocationDetectMode.THIRD_G);
		} catch (ServiceException e) {
			addActionError("系統錯誤,原因:{0}, 請重新操作", e.getMessage());
		}
		complementPermissionListSize(p.getPermissionList());
		return p;
	}

	private void clearCurrent() {
		current = null;
		currentProfile = null;
		selectedApZoneOid = 0L;
	}

	public String displayAddUserProfile() {
		current = new FemtoUser();
		currentProfile = createDefaultProfile();
		selectedApZoneOid = 0L;
		
		mode = Mode.CREATE_USER_PROFILE;
		return SUCCESS;
	}

	public String displayDetail() throws ServiceException {
		mode = Mode.DISPLAY;
		return refreshProfileByOid();
	}
	
	public String displayCreateProfile() {
		current = provisionService.findUserByOid(current.getOid());
		if (current == null) {
			addActionError("用戶資訊已不存在，請重新操作");
			return ERROR;
		}
		currentProfile = createDefaultProfile();
		
		mode = Mode.CREATE_PROFILE;
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
	
	private String refreshProfileByOid() throws ServiceException {
		currentProfile = provisionService.findProfileByOid(currentProfile.getOid());
		if (currentProfile == null) {
			addActionError("該申裝資訊已經不存在，請重新操作");
			return ERROR;
		}
		currentProfile = provisionService.syncPermissionList(currentProfile);
		return selectProfile(currentProfile);
	}

	private String selectProfile(FemtoProfile profile) {
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
		current = profile.getUser();
		currentProfile = profile;
		if (profile.getApZone() == null) {
			selectedApZoneOid = 0L;
		} else {
			selectedApZoneOid = profile.getApZone().getOid();
		}
		return SUCCESS;
	}
	
	public String save() {
		switch (mode) {
		case CREATE_USER_PROFILE:
			return saveUserProfile();
		case CREATE_PROFILE:
			if (createProfile() == ERROR) {
				return ERROR;
			}
			return RESULT_SEARCH_USER;
		case CHANGE_PROFILE:
			if (changeProfile() == ERROR) {
				return ERROR;
			}
			return SUCCESS;
		case CHANGE_PERMISSIONLIST:
			if (savePermissionList() == ERROR) {
				return ERROR;
			}
			return SUCCESS;
		case CHANGE_UEPERMISSIONMODE:
			if (changeUePermissionMode() == ERROR) {
				return ERROR;
			}
			return SUCCESS;
		case CHANGE_PERMISSIONLIST_BY_FEMTOUSER:
			if (savePermissionList() == ERROR) {
				return ERROR;
			}
			return SUCCESS;
		}
		
		addActionMessage("系統錯誤,原因: incorrect mode value:{0}, 請重新操作", mode);
		return ERROR;
	}
	
	private String changeUePermissionMode() {
		// validate permission list
		compactPermissionList(currentProfile.getPermissionList());
		if (!validateProfile(currentProfile)) {
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
		} catch (ServiceException e) {
			addActionError("用戶  {0} 儲存失敗，原因：{1}", current.getUserName(), e.getMessage());
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}
		
		addActionMessage("用戶 {0} 儲存成功", current.getUserName());
		complementPermissionListSize(currentProfile.getPermissionList());
		return SUCCESS;
	}

	public String saveUserProfile() {
		compactPermissionList(currentProfile.getPermissionList());
		if (!validateUser(current)) {
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}
		
		if (!validateProfile(currentProfile)) {
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}
		
		try {
			current = provisionService.createUser(current, currentProfile, getLoginAdminUser());
		} catch (AddressFormatException e) {
			addActionError("用戶  {0} 儲存失敗，原因：地址格式錯誤, 帶入的地址字串:{1}，【段】可為中文或阿拉伯數字，但X巷、X號、X樓皆需為阿拉伯數字", 
					current.getUserName(), currentProfile.getAddress());
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		} catch (ServiceException e) {
			addActionError("用戶  {0} 儲存失敗，原因：{1}", current.getUserName(), e.getMessage());
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}
		
		addActionMessage("用戶 {0} 儲存成功", current.getUserName());
		complementPermissionListSize(currentProfile.getPermissionList());
		return SUCCESS;
	}

	private boolean validateUser(FemtoUser user) {
		boolean validated = true;
		validated = validated && validateStringEmpty(user.getUserName(), "用戶名稱為必填");
		validated = validated && validateStringEmpty(user.getUserName(), "用戶門號為必填");
		validated = validated && validateStringEmpty(user.getUserName(), "用戶帳號為必填");
		validated = validated && validateStringLengthIsOver(user.getPassword(), 0, "用戶密碼為必填");
		return validated;
	}

	private boolean validateProfile(FemtoProfile profile) {
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
		validated = validated
				&& validatePermissionList(profile.getPermissionList(),
						profile.getUePermissionMode(),
						profile.getMaxPermissionListSize());
		// validate ap zone
		ApZone selectedApZone = fetchSelectedApZone();
		profile.setApZone(selectedApZone);
		validated = validated && validateEmpty(profile.getApZone(), "AP Zone為必填");
		return validated;
	}

	private boolean validatePermissionList(List<UserEquipment> permissionList,
			UePermissionMode uePermissionMode, int maxPermissionSize) {
		boolean validated = true;
		if (uePermissionMode == UePermissionMode.CLOSE
				&& permissionList.isEmpty()) {
			addActionError("至少需要輸入一組用戶白名單門號");
			validated = false;
		}
		if (uePermissionMode == UePermissionMode.CLOSE
				&& permissionList.size() > maxPermissionSize) {
			addActionError("用戶白名單數目:{0}超過最大限制數量:{1}", permissionList.size(),
					maxPermissionSize);
			validated = false;
		}
		return validated;
	}

	private String changeProfile() {
		// validate permission list
		compactPermissionList(currentProfile.getPermissionList());
		if (!validateProfile(currentProfile)) {
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}
		
		try {
			FemtoProfile oldProfile = provisionService.findProfileByOid(currentProfile.getOid());
			currentProfile = provisionService.changeProfile(oldProfile, currentProfile, getLoginAdminUser());
		
		} catch (NoCellFoundException e) {
			addActionError("該地址查無基地台訊號，請把網路鎖定模式改成不鎖定，並重新儲存", getCurrent().getUserName(), e.getMessage());
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
			
		} catch (ServiceException e) {
			addActionError("用戶  {0} 儲存失敗，原因：{1}", current.getUserName(), e.getMessage());
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}
		
		addActionMessage("用戶 {0} 儲存成功", current.getUserName());
		complementPermissionListSize(currentProfile.getPermissionList());
		return SUCCESS;
	}

	private String createProfile() {
		compactPermissionList(currentProfile.getPermissionList());
		if (!validateProfile(currentProfile)) {
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}
		
		try {
			current = provisionService.findUserByOid(current.getOid());
			currentProfile = provisionService.createProfile(current, currentProfile, getLoginAdminUser());
		
		} catch (NoCellFoundException e) {
			addActionError("該地址查無基地台訊號，請把網路鎖定模式改成不鎖定，並重新儲存", getCurrent().getUserName(), e.getMessage());
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
			
		} catch (AddressFormatException e) {
			addActionError("用戶  {0} 儲存失敗，原因：地址格式錯誤, 帶入的地址字串:{1}，【段】可為中文或阿拉伯數字，但X巷、X號、X樓皆需為阿拉伯數字", 
					current.getUserName(), currentProfile.getAddress());
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
			
		} catch (ServiceException e) {
			addActionError("用戶  {0} 儲存失敗，原因：{1}", current.getUserName(), e.getMessage());
			complementPermissionListSize(currentProfile.getPermissionList());
			return ERROR;
		}
		
		addActionMessage("用戶 {0} 儲存成功", current.getUserName());
		clearCurrent();
		return SUCCESS;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(BeanUtils.describe(new TestData().createProfile("a", "b")));
	}
	
	public String savePermissionList() {
		List<UserEquipment> newPermissionList = currentProfile.getPermissionList();
		FemtoProfile oldProfile = null;
		try {
			oldProfile = provisionService.findProfileByOid(currentProfile.getOid());
			compactPermissionList(newPermissionList);
			if (!validatePermissionList(currentProfile.getPermissionList(),
					oldProfile.getUePermissionMode(),
					oldProfile.getMaxPermissionListSize())) {
				complementPermissionListSize(newPermissionList);
				currentProfile = oldProfile;
				currentProfile.setPermissionList(newPermissionList);
				selectedApZoneOid = currentProfile.getApZone().getOid();
				current = getLoginFemtoUser();
				return ERROR;
			}
		
			List<UserEquipment> oldPermissionList = oldProfile.getPermissionList();
			oldProfile.setPermissionList(newPermissionList);
			provisionService.savePermissionList(oldProfile, fetchLoginUser());
			
			current = getLoginFemtoUser();
			logEvent(UserLogType.CHANGE_PERMISSION_LIST, oldProfile, Utils.format("用戶:{0}, 原名單:{1}, 變更後名單:{2}",
					describe(current), describe(oldPermissionList), describe(newPermissionList)));
			selectProfile(oldProfile);
			
		} catch (ServiceException e) {
			currentProfile = oldProfile;
			currentProfile.setPermissionList(newPermissionList);
			selectedApZoneOid = currentProfile.getApZone().getOid();
			current = getLoginFemtoUser();
			addActionError("用戶  {0} 儲存失敗，原因：{1}", getCurrent().getUserName(), e.getMessage());
			logEvent(UserLogType.CHANGE_PERMISSION_LIST, currentProfile, Utils.format(
					"錯誤, {0}, 用戶:{1}, 申裝資訊:{2}", 
					e.getMessage(), describe(current), describe(currentProfile)));
			complementPermissionListSize(newPermissionList);
			return ERROR;
		}

		addActionMessage("用戶 {0} 儲存成功", getCurrent().getUserName());
		complementPermissionListSize(newPermissionList);
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

	public List<LocationDetectMode> getAvailableLocationDetectModes() {
		return availableLocationDetectModes;
	}

}
