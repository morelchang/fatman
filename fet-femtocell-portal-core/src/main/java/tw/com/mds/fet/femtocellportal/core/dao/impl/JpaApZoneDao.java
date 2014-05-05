package tw.com.mds.fet.femtocellportal.core.dao.impl;

import java.util.ArrayList;
import java.util.List;

import tw.com.mds.fet.femtocellportal.core.ApZone;
import tw.com.mds.fet.femtocellportal.core.dao.ApZoneDao;
import tw.com.mds.fet.femtocellportal.dao.impl.JpaGenericDao;

public class JpaApZoneDao extends JpaGenericDao<ApZone> implements ApZoneDao {

	public List<ApZone> findByFuzzyName(String name) {
		if (name == null) {
			return new ArrayList<ApZone>();
		}
		return query("from ApZone e where e.name like ? order by e.name", quoteLike(name));
	}

	public ApZone findExactlyByName(String name) {
		if (name == null) {
			return null;
		}
		return queryForFirst("from ApZone e where e.name = ?", name);
	}

}
