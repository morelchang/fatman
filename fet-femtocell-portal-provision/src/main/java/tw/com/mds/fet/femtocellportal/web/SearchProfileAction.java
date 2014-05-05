package tw.com.mds.fet.femtocellportal.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.core.FemtoProfile;
import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.core.MaskedFemtoUserWrapper;
import tw.com.mds.fet.femtocellportal.core.ProvisionService;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.core.UserLogType;
import tw.com.mds.fet.femtocellportal.util.Utils;


public class SearchProfileAction extends IdentityAction {
	
	public static enum Mode {
		SEARCH,
		CHANGE_PERMISSIONLIST,
		CHANGE_PERMISSIONLIST_BY_FEMTOUSER,
		CHANGE_UEPERMISSIONMODE,
		CHANGE_PROFILE,
		SUSPEND,
		RESUME,
		DELETE,
		CHANGE_LOCATIONDETECTMODE
	}

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_CHANGE_PERMISSIONLIST = "displayChangePermissionList";

	private ProvisionService provisionService;
	
	// search criteria
	private Long searchUserOid;
	private String searchUserName;
	private String searchUserMobile;
	private String searchApei;
	private String searchImsi;

	// search result
	private List<FemtoUser> searchResult = new ArrayList<FemtoUser>();
	private FemtoUser current;
	private FemtoProfile currentProfile;
	
	private String gisLocationLink;
	
	private Mode mode = Mode.SEARCH;
	private boolean defaultListAll;

	@Override
	public void prepare() throws Exception {
		gisLocationLink = provisionService.getGisLocationLink();
		defaultListAll = provisionService.isDefaultListAll();
		super.prepare();
	}
	
	public String displayUserProfiles() {
		searchResult = Arrays.asList(getLoginFemtoUser());
		mode = Mode.CHANGE_PERMISSIONLIST_BY_FEMTOUSER;
		return SUCCESS;
	}

	public String searchProfile() {
		// find by apei or imsi
		if (!StringUtils.isBlank(searchApei) || !StringUtils.isBlank(searchImsi)) {
			FemtoUser user = provisionService.findUserByProfile(searchApei, searchImsi);
			if (user != null) {
				searchResult = Arrays.asList(user);
			} else {
				searchResult = Collections.emptyList();
			}
		} else if (!StringUtils.isBlank(searchUserName)) {
			// find by userName or userMobile
			searchResult = provisionService.findUsers(searchUserName);
		} else if (!StringUtils.isBlank(searchUserMobile)) {
			searchResult = provisionService.findUsersByMobile(searchUserMobile);
		} else if (defaultListAll) {
			searchResult = provisionService.listAllUsers();
		} else {
			searchResult = Collections.emptyList();
		}
		logEvent(UserLogType.SEARCH_USERPROFILES, Utils.format(
				"條件:[用戶名稱={0}, 用戶門號={1}, APEI:{2}, IMSI:{3}], 結果筆數:{4}", 
				searchUserName, searchUserMobile, searchApei, searchImsi, searchResult.size()));
		return addResultMessageAndGotoNextPage(searchResult);
	}

	private String addResultMessageAndGotoNextPage(List<? extends FemtoUser> searchResult) {
		if (mode == Mode.CHANGE_PERMISSIONLIST && selectMatchedProfile(searchResult) != null) {
			return DISPLAY_CHANGE_PERMISSIONLIST;
		}
		
		addActionMessage("查詢結果共 {0} 筆資料", sizeOfProfiles(searchResult));
		return SUCCESS;
	}

	private FemtoProfile selectMatchedProfile(List<? extends FemtoUser> searchResult) {
		if (StringUtils.isEmpty(searchApei)
				&& StringUtils.isEmpty(searchImsi)) {
			return null;
		}
			
		for (FemtoUser femtoUser : searchResult) {
			List<FemtoProfile> profiles = femtoUser.getProfiles();
			for (FemtoProfile p : profiles) {
				if (searchApei.equals(p.getApei()) || searchImsi.equals(p.getImsi())) {
					currentProfile = p;
					return currentProfile;
				}
			}
		}
		return null;
	}

	private int sizeOfProfiles(List<? extends FemtoUser> searchResult) {
		int result = 0;
		for (FemtoUser u : searchResult) {
			result += u.getProfiles().size();
		}
		return result;
	}
	
	public String removeProfile() {
		Long oid = currentProfile.getOid();
		FemtoProfile removed = null;
		try {
			removed = provisionService.removeProfileByOid(oid, getLoginAdminUser());
			if (removed == null) {
				addActionError("申裝資訊已不存在，請重新操作");
				return SUCCESS;
			}
			
			logEvent(UserLogType.DELETE_PROFILE, removed, Utils.format("用戶:{0}, 申裝明細:{1}",
					describe(removed.getUser()), describe(removed)));
		} catch (ServiceException e) {
			addActionError("申裝資訊刪除失敗，原因：{0}", e.getMessage());
			logEvent(UserLogType.DELETE_PROFILE, currentProfile, Utils.format(
					"錯誤, {0}, 用戶:{1}, 申裝資訊:{2}", 
					e.getMessage(), describe(currentProfile.getUser()), describe(currentProfile)));
			return SUCCESS;
		}
		
		
		addActionMessage("用戶所申裝的 APEI:{0},IMSI:{1}資訊 刪除成功", removed.getApei(), removed.getImsi());
		clearCurrent();
		return searchProfile();
	}

	public String suspendProfile() {
		Long oid = currentProfile.getOid();
		FemtoProfile removed = null;
		try {
			currentProfile = provisionService.findProfileByOid(oid);
			if (currentProfile == null) {
				addActionError("申裝資訊已不存在，請重新操作");
				return ERROR;
			}
			removed = provisionService.suspendFemtoProfile(currentProfile, getLoginAdminUser());
			logEvent(UserLogType.SUSPEND_PROFILE, currentProfile, Utils.format("用戶:{0}, 停用申裝:{1}",
					describe(currentProfile.getUser()), describe(currentProfile)));
		} catch (ServiceException e) {
			addActionError("用戶停機失敗，原因：{0}", e.getMessage());
			logEvent(UserLogType.SUSPEND_PROFILE, currentProfile, Utils.format(
					"錯誤, {0}, 用戶:{1}, 申裝資訊:{2}", 
					e.getMessage(), describe(currentProfile.getUser()), describe(currentProfile)));
			return SUCCESS;
		}
		
		if (removed == null) {
			addActionError("申裝資訊已不存在，請重新操作");
			return SUCCESS;
		}
		
		addActionMessage("用戶所申裝的 APEI:{0},IMSI:{1} 停機成功", removed.getApei(), removed.getImsi());
		clearCurrent();
		return searchProfile();
	}

	public String resumeProfile() {
		Long oid = currentProfile.getOid();
		FemtoProfile removed = null;
		try {
			currentProfile = provisionService.findProfileByOid(oid);
			if (currentProfile == null) {
				addActionError("申裝資訊已不存在，請重新操作");
				return ERROR;
			}
			removed = provisionService.resumeFemtoProfile(currentProfile, getLoginAdminUser());
			logEvent(UserLogType.RESUME_PROFILE, currentProfile, Utils.format("用戶:{0}, 啟用申裝:{1}",
					describe(currentProfile.getUser()), describe(currentProfile)));
		} catch (ServiceException e) {
			addActionError("用戶重啟失敗，原因：{0}", e.getMessage());
			logEvent(UserLogType.RESUME_PROFILE, currentProfile, Utils.format(
					"錯誤, {0}, 用戶:{1}, 申裝資訊:{2}", 
					e.getMessage(), describe(currentProfile.getUser()), describe(currentProfile)));
			return SUCCESS;
		}
		
		if (removed == null) {
			addActionError("申裝資訊已不存在，請重新操作");
			return SUCCESS;
		}
		
		addActionMessage("用戶所申裝的 APEI:{0},IMSI:{1} 重啟成功", removed.getApei(), removed.getImsi());
		clearCurrent();
		return searchProfile();
	}

	private void clearCurrent() {
		current = null;
	}

	public List<MaskedFemtoUserWrapper> getSearchResult() {
		return MaskedFemtoUserWrapper.wrapList(searchResult);
	}

	public void setSearchResult(List<MaskedFemtoUserWrapper> searchResult) {
		this.searchResult = MaskedFemtoUserWrapper.unwrap(searchResult);
	}

	public ProvisionService getProvisionService() {
		return provisionService;
	}

	public void setProvisionService(ProvisionService provisionService) {
		this.provisionService = provisionService;
	}

	public String getSearchUserName() {
		return searchUserName;
	}

	public void setSearchUserName(String searchUserName) {
		this.searchUserName = searchUserName;
	}

	public String getSearchApei() {
		return searchApei;
	}

	public void setSearchApei(String searchApei) {
		this.searchApei = searchApei;
	}

	public String getSearchImsi() {
		return searchImsi;
	}

	public void setSearchImsi(String searchImsi) {
		this.searchImsi = searchImsi;
	}

	public FemtoUser getCurrent() {
		return current;
	}

	public void setCurrent(FemtoUser current) {
		this.current = current;
	}

	public FemtoProfile getCurrentProfile() {
		return currentProfile;
	}

	public void setCurrentProfile(FemtoProfile currentProfile) {
		this.currentProfile = currentProfile;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public Long getSearchUserOid() {
		return searchUserOid;
	}

	public void setSearchUserOid(Long searchUserOid) {
		this.searchUserOid = searchUserOid;
	}

	public String getGisLocationLink() {
		return gisLocationLink;
	}

	public boolean isDefaultListAll() {
		return defaultListAll;
	}

	public void setDefaultListAll(boolean defaultListAll) {
		this.defaultListAll = defaultListAll;
	}

	public String getSearchUserMobile() {
		return searchUserMobile;
	}

	public void setSearchUserMobile(String searchUserMobile) {
		this.searchUserMobile = searchUserMobile;
	}

}
