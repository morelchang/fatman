package tw.com.mds.fet.femtocellportal.core.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.core.Announcement;
import tw.com.mds.fet.femtocellportal.core.UserType;
import tw.com.mds.fet.femtocellportal.core.dao.AnnouncementDao;
import tw.com.mds.fet.femtocellportal.dao.impl.JpaGenericDao;

public class JpaAnnouncementDao extends JpaGenericDao<Announcement> implements
		AnnouncementDao {

	public List<Announcement> findPublished(UserType type, Date atTime) {
		if (type == null || atTime == null) {
			return new ArrayList<Announcement>();
		}
		return query(
				"from Announcement e where e.userType = ? and ? between e.startTime and e.endTime order by e.startTime desc",
				type, atTime);
	}

	public List<Announcement> findByKeyword(UserType userType,
			String titleKeyword, String contentKeyword) {
		List<Object> params = new ArrayList<Object>();
		
		String userTypeClause = "";
		if (userType != null) {
			userTypeClause = " and e.userType = ? ";
			params.add(userType);
		}
		String titleClause = "";
		if (!StringUtils.isEmpty(titleKeyword)) {
			titleClause = " and e.title like ? ";
			params.add(quoteLike(titleKeyword));
		}
		String contentClause = "";
		if (!StringUtils.isEmpty(contentKeyword)) {
			contentClause = " and e.content like ? ";
			params.add(quoteLike(contentKeyword));
		}
		
		String jpql = "from Announcement e where 1 = 1 " + userTypeClause + titleClause
				+ contentClause + " order by e.startTime desc";
		return query(jpql, params.toArray());
	}

}
