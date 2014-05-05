package tw.com.mds.fet.femtocellportal.core.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.core.AdminUser;
import tw.com.mds.fet.femtocellportal.core.dao.AdminUserDao;
import tw.com.mds.fet.femtocellportal.dao.impl.JpaGenericDao;

public class JpaAdminUserDao extends JpaGenericDao<AdminUser> implements AdminUserDao {

	public AdminUser findById(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		return queryForFirst("from AdminUser e where e.userId = ?", id);
	}

	public List<AdminUser> findByName(String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		return query("from AdminUser e where e.userName like ?", quoteLike(name));
	}

	public AdminUser findByAccount(String account) {
		if (StringUtils.isEmpty(account)) {
			return null;
		}
		return queryForFirst("from AdminUser e where e.account = ?", account);
	}

	public List<AdminUser> findByCriteria(AdminUser c) {
		StringBuilder jpql = new StringBuilder("from AdminUser e where 1 = 1 ");
		List<Object> params = new ArrayList<Object>();
		List<AdminUser> empty = new ArrayList<AdminUser>();
		
		if (!StringUtils.isEmpty(c.getUserId())) {
			jpql.append(" and e.userId = ?");
			params.add(c.getUserId());
		}
		if (!StringUtils.isEmpty(c.getUserName())) {
			jpql.append(" and e.userName like ?");
			params.add(quoteLike(c.getUserName()));
		}
		if (!StringUtils.isEmpty(c.getAccount())) {
			jpql.append(" and e.account = ?");
			params.add(c.getAccount());
		}
		if (!StringUtils.isEmpty(c.getEmail())) {
			jpql.append(" and e.email like ?");
			params.add(quoteLike(c.getEmail()));
		}
		
		if (params.isEmpty()) {
			return empty;
		}
		
		jpql.append(" order by e.userId");
		return query(jpql.toString(), params.toArray());
	}

}
