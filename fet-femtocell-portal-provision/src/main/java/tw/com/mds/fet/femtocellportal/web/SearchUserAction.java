package tw.com.mds.fet.femtocellportal.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.core.MaskedFemtoUserWrapper;
import tw.com.mds.fet.femtocellportal.core.ProvisionService;
import tw.com.mds.fet.femtocellportal.core.UserLogType;
import tw.com.mds.fet.femtocellportal.util.Utils;

public class SearchUserAction extends IdentityAction {


	private static final long serialVersionUID = 1L;
	
	public static enum Mode {
		CREATE_PROFILE,
	}

//	private static final String DISPLAY_CREATE_PROFILE = "displayCreateProfile";
	
	private ProvisionService provisionService;

	private String searchUserName;
	private String searchUserMobile;
	private List<FemtoUser> searchResult = new ArrayList<FemtoUser>();
	private FemtoUser current;
	private Mode mode = Mode.CREATE_PROFILE;
	private boolean defaultListAll;
	
	@Override
	public void prepare() throws Exception {
		clearSearch();
		defaultListAll = provisionService.isDefaultListAll();
		super.prepare();
	}

	private void clearSearch() {
		searchUserName = "";
		searchUserMobile = "";
	}

	public String searchUser() {
		if (!StringUtils.isEmpty(searchUserName)) {
			searchResult = provisionService.findUsers(searchUserName);

		} else if (!StringUtils.isEmpty(searchUserMobile)) {
			searchResult = provisionService.findUsersByMobile(searchUserMobile);
			
		} else if (defaultListAll) {
			searchResult = provisionService.listAllUsers();
		}
		logEvent(UserLogType.SEARCH_USERPROFILES, Utils.format(
				"條件:[用戶名稱={0}, 用戶門號={1}], 結果筆數:{2}", searchUserName, searchUserMobile, searchResult.size()));

//		if (searchResult.size() == 1) {
//			current = searchResult.get(0);
//
//			switch (mode) {
//			case CREATE_PROFILE:
//				return DISPLAY_CREATE_PROFILE;
//			}
//		}
		
		addActionMessage("查詢結果共 {0} 筆資料", searchResult.size());
		return SUCCESS;
	}

	public String getSearchUserName() {
		return searchUserName;
	}

	public void setSearchUserName(String searchUserName) {
		this.searchUserName = searchUserName;
	}

	public String getSearchUserMobile() {
		return searchUserMobile;
	}

	public void setSearchUserMobile(String searchUserMobile) {
		this.searchUserMobile = searchUserMobile;
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

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
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

	public boolean isDefaultListAll() {
		return defaultListAll;
	}

	public void setDefaultListAll(boolean defaultListAll) {
		this.defaultListAll = defaultListAll;
	}

}
