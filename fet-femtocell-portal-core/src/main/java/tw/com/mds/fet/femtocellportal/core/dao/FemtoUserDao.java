package tw.com.mds.fet.femtocellportal.core.dao;

import java.util.List;

import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.dao.GenericDao;

/**
 * femtocell user dao
 * 
 * @author morel
 *
 */
public interface FemtoUserDao extends GenericDao<FemtoUser> {
	
	/**
	 * find femto user by user name
	 * <p>
	 * order by userId
	 * </p>
	 * 
	 * @param userName
	 * @return
	 */
	public List<FemtoUser> findByUserName(String userName);

	/**
	 * find femto users by there mobile numbe
	 * 
	 * @param mobile
	 * @return
	 */
	public List<FemtoUser> findByMobile(String mobile);

	public FemtoUser findByAccount(String account);
	
}
