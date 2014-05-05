package tw.com.mds.fet.femtocellportal.core.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.core.UserLog;
import tw.com.mds.fet.femtocellportal.core.dao.UserLogDao;
import tw.com.mds.fet.femtocellportal.dao.impl.JpaGenericDao;

public class JpaUserLogDao extends JpaGenericDao<UserLog>
		implements UserLogDao {

	public List<UserLog> findByCriteria(UserLog criteria, Date startCreateTime, Date endCreateTime) {
		if (criteria == null && startCreateTime == null && endCreateTime == null) {
			return new ArrayList<UserLog>();
		}
		
		StringBuilder jpql = new StringBuilder("from UserLog e where 1 = 1 ");
		List<Object> params = new ArrayList<Object>();
		if (startCreateTime != null) {
			jpql.append("and e.createTime >= ? ");
			params.add(startCreateTime);
		}
		if (endCreateTime != null) {
			jpql.append("and e.createTime <= ? ");
			params.add(endCreateTime);
		}
		if (criteria.getType() != null) {
			jpql.append("and e.type = ? ");
			params.add(criteria.getType());
		}
		if (!StringUtils.isEmpty(criteria.getOperatorId())) {
			jpql.append("and e.operatorId = ? ");
			params.add(criteria.getOperatorId());
		}
		if (!StringUtils.isEmpty(criteria.getOperatorName())) {
			jpql.append("and e.operatorName like ? ");
			params.add(quoteLike(criteria.getOperatorName()));
		}
		if (!StringUtils.isEmpty(criteria.getOperatorSourceIp())) {
			jpql.append("and e.operatorSourceIp like ? ");
			params.add(quoteLike(criteria.getOperatorSourceIp()));
		}
		if (!StringUtils.isEmpty(criteria.getUserName())) {
			jpql.append("and e.userName like ? ");
			params.add(quoteLike(criteria.getUserName()));
		}
		if (!StringUtils.isEmpty(criteria.getUserMobile())) {
			jpql.append("and e.userMobile = ? ");
			params.add(criteria.getUserMobile());
		}
		if (!StringUtils.isEmpty(criteria.getProfileApei())) {
			jpql.append("and e.profileApei = ? ");
			params.add(criteria.getProfileApei());
		}
		if (!StringUtils.isEmpty(criteria.getProfileImsi())) {
			jpql.append("and e.profileImsi = ? ");
			params.add(criteria.getProfileImsi());
		}
		if (!StringUtils.isEmpty(criteria.getContent())) {
			jpql.append("and e.content like ? ");
			params.add(quoteLike(criteria.getContent()));
		}
		
		if (params.isEmpty()) {
			return new ArrayList<UserLog>();
		}
		return query(jpql.append("order by createTime desc").toString(), params.toArray());
	}

}
