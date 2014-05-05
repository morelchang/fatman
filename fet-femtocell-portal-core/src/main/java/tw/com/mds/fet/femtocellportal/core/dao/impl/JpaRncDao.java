package tw.com.mds.fet.femtocellportal.core.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.core.Rnc;
import tw.com.mds.fet.femtocellportal.core.dao.RncDao;
import tw.com.mds.fet.femtocellportal.dao.impl.JpaGenericDao;

public class JpaRncDao extends JpaGenericDao<Rnc> implements RncDao {

	public Rnc findByRncId(String rncId) {
		if (StringUtils.isEmpty(rncId)) {
			return null;
		}
		return queryForFirst("from Rnc e where e.rncId = ?", rncId);
	}

	public List<Rnc> findByFuzzyIdAndName(Rnc criteria) {
		List<Rnc> result = new ArrayList<Rnc>();
		if (criteria == null) {
			return result; 
		}

		String jpql = "from Rnc e where 1 = 1 ";
		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isBlank(criteria.getRncId())) {
			jpql += "and e.rncId like ? ";
			params.add(quoteLike(criteria.getRncId()));
		}
		if (!StringUtils.isBlank(criteria.getRncName())) {
			jpql += "and e.rncName like ? ";
			params.add(quoteLike(criteria.getRncName()));
		}
		
		return query(jpql + "order by e.rncId", params.toArray());
	}

	public List<Rnc> findbyRncName(String rncName) {
		if (StringUtils.isEmpty(rncName)) {
			return null;
		}
		return query("from Rnc e where e.rncName = ?", rncName);
	}

}
