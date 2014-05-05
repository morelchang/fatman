package tw.com.mds.fet.femtocellportal.core.dao;

import java.util.Date;
import java.util.List;

import tw.com.mds.fet.femtocellportal.core.UserLog;
import tw.com.mds.fet.femtocellportal.dao.GenericDao;

public interface UserLogDao extends GenericDao<UserLog> {

	List<UserLog> findByCriteria(UserLog criteria, Date startCreateTime,
			Date endCreateTime);

}
