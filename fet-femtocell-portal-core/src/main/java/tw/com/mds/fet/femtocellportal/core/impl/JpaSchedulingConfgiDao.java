package tw.com.mds.fet.femtocellportal.core.impl;

import tw.com.mds.fet.femtocellportal.dao.impl.JpaGenericDao;


public class JpaSchedulingConfgiDao extends JpaGenericDao<SchedulingConfig>
		implements SchedulingConfigDao {

	public SchedulingConfig load() {
		return queryForFirst("from SchedulingConfig e order by oid");
	}

	public SchedulingConfig save(SchedulingConfig config) {
		return persist(config);
	}

}
