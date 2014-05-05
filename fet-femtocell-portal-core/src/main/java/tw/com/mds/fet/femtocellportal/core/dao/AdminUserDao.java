package tw.com.mds.fet.femtocellportal.core.dao;

import java.util.List;

import tw.com.mds.fet.femtocellportal.core.AdminUser;
import tw.com.mds.fet.femtocellportal.dao.GenericDao;

public interface AdminUserDao extends GenericDao<AdminUser> {

	public AdminUser findById(String id);
	
	public List<AdminUser> findByName(String name);

	public AdminUser findByAccount(String account);

	public List<AdminUser> findByCriteria(AdminUser criteria);

}
