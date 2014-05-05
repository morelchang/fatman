package tw.com.mds.fet.femtocellportal.web;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import tw.com.mds.fet.femtocellportal.core.UserLog;
import tw.com.mds.fet.femtocellportal.core.UserLogType;
import tw.com.mds.fet.femtocellportal.util.Utils;

public class UserLogAction extends CrudAction<UserLog> {

	private static final long serialVersionUID = 1L;
	
	private List<UserLogType> availableUserLogTypes = Arrays.asList(UserLogType.values());
	private Date startCreateTime;
	private Date endCreateTime;

	@Override
	public String search() {
		if(!validateSearch(getSearchCriteria())) {
			getSearchResult().clear();
			return ERROR;
		}
		
		try {
			setSearchResult(search(getSearchCriteria()));
			logEvent(UserLogType.SYSTEM_MANAGEMENT, Utils.format(
					"查詢條件:{0}, startCreateTime:{1}, endCreateTime:{2}, 結果筆數:{3}", 
					getSearchCriteria(), startCreateTime, endCreateTime, getSearchResult().size()));
			addActionMessage("查詢結果共:{0}筆", getSearchResult().size());
			
		} catch (Exception e) {
			addActionError("系統錯誤, 原因:{0}", e.getMessage());
			logEvent(UserLogType.SYSTEM_MANAGEMENT, Utils.format(
					"錯誤:{0}, 查詢條件:{0}, startCreateTime:{1}, endCreateTime:{2}", 
					e.getMessage(), getSearchCriteria(), startCreateTime, endCreateTime));
			return ERROR;
		}
		setMode(Mode.SEARCH);
		return SUCCESS;
	}

	@Override
	protected List<UserLog> search(UserLog criteria) throws Exception {
		return administrationService.findUserLogByCriteria(criteria, startCreateTime, endCreateTime);
	}

	public Date getStartCreateTime() {
		return startCreateTime;
	}

	public void setStartCreateTime(Date startCreateTime) {
		this.startCreateTime = startCreateTime;
	}

	public Date getEndCreateTime() {
		return endCreateTime;
	}

	public void setEndCreateTime(Date endCreateTime) {
		this.endCreateTime = endCreateTime;
	}

	public List<UserLogType> getAvailableUserLogTypes() {
		return availableUserLogTypes;
	}

	public void setAvailableUserLogTypes(List<UserLogType> availableUserLogTypes) {
		this.availableUserLogTypes = availableUserLogTypes;
	}

}
