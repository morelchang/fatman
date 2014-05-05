package tw.com.mds.fet.femtocellportal.core.dao;

import java.util.Date;
import java.util.List;

import tw.com.mds.fet.femtocellportal.core.Announcement;
import tw.com.mds.fet.femtocellportal.core.UserType;
import tw.com.mds.fet.femtocellportal.dao.GenericDao;

public interface AnnouncementDao extends GenericDao<Announcement> {

	public List<Announcement> findPublished(UserType type, Date atTime);

	public List<Announcement> findByKeyword(UserType userType,
			String titleKeyword, String contentKeyword);

}
