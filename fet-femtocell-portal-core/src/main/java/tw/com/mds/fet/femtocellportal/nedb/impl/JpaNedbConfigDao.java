package tw.com.mds.fet.femtocellportal.nedb.impl;

import tw.com.mds.fet.femtocellportal.dao.impl.JpaGenericDao;

public class JpaNedbConfigDao extends JpaGenericDao<NedbConfig> implements NedbConfigDao {

	public NedbConfig load() {
		return queryForFirst("from NedbConfig e order by oid");
	}

	public NedbConfig save(NedbConfig config) {
		return persist(config);
	}

}
