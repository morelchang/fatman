package tw.com.mds.fet.femtocellportal.core.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.core.dao.FemtoUserDao;
import tw.com.mds.fet.femtocellportal.dao.impl.JpaGenericDao;

public class JpaFemtoUserDao extends JpaGenericDao<FemtoUser> implements FemtoUserDao {

	public List<FemtoUser> findByUserName(String userName) {
		if (userName == null) {
			return new ArrayList<FemtoUser>();
		}
		return query("from FemtoUser e where e.userName = ? order by e.createTime", Base64.encodeBase64String(userName.getBytes()));
	}

	public List<FemtoUser> findByMobile(String mobile) {
		if (StringUtils.isEmpty(mobile)) {
			return new ArrayList<FemtoUser>();
		}
		return query("from FemtoUser e where e.mobile = ? ", mobile);
	}

	public FemtoUser findByAccount(String account) {
		if (StringUtils.isEmpty(account)) {
			return null;
		}
		return queryForFirst("from FemtoUser e where e.account = ? ", account);
	}

}
