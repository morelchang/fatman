package tw.com.mds.fet.femtocellportal.web;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.core.Announcement;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.core.UserType;
import tw.com.mds.fet.femtocellportal.util.Utils;

public class AnnouncementAction extends CrudAction<Announcement> {

	private static final long serialVersionUID = 1L;
	
	private List<UserType> availableUserTypes = Arrays.asList(UserType.ADMINUSER, UserType.FEMTOUSER);
	
	protected Announcement createDefaultNew() {
		Announcement announcement = new Announcement();
		Date startTime = new Date();
		announcement.setStartTime(startTime);
		announcement.setEndTime(Utils.addDays(startTime, 30));
		return announcement;
	}

	protected boolean validateSearch(Announcement criteria) {
		if (criteria.getUserType() == null
				&& StringUtils.isEmpty(criteria.getTitle())
				&& StringUtils.isEmpty(criteria.getContent())) {
			return false;
		}
		return true;
	}

	protected List<Announcement> search(Announcement criteria) throws ServiceException {
		return administrationService.findAnnouncements(criteria.getUserType(),
				criteria.getTitle(), criteria.getContent());
	}

	protected boolean validateSave(Announcement saving) {
		if (saving.getUserType() == null) {
			addActionError("公告對象未填寫");
			return false;
		}
		if (saving.getStartTime() == null) {
			addActionError("公告起始時間:未填或格式錯誤", saving.getStartTime());
			return false;
		}
		if (saving.getEndTime() == null) {
			addActionError("公告結束時間:未填或格式錯誤", saving.getEndTime());
			return false;
		}
		
		if (saving.getStartTime().after(saving.getEndTime())) {
			addActionError("公告起始時間:{0}不可在公告結束時間:{1}之後", saving.getStartTime(), saving.getEndTime());
			return false;
		}
		
		if (saving.getEndTime().before(new Date())) {
			addActionError("無法發佈一個公告結束時間:{0} 已過的公告", saving.getEndTime());
			return false;
		}
		return true;
	}

	protected void afterSaving(Announcement saved) throws ServiceException {
		reloadAnnouncement();
	}

	@Override
	protected void afterDeleting(Announcement deleted) throws ServiceException {
		reloadAnnouncement();
	}
	
	public List<UserType> getAvailableUserTypes() {
		return availableUserTypes;
	}

	public void setAvailableUserTypes(List<UserType> availableUserTypes) {
		this.availableUserTypes = availableUserTypes;
	}

}
