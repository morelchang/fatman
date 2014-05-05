package tw.com.mds.fet.femtocellportal.core.impl;

import tw.com.mds.fet.femtocellportal.dao.impl.JpaGenericDao;

public class JpaProvisionConfigDao extends JpaGenericDao<ProvisionConfig>
		implements ProvisionConfigDao {

	public ProvisionConfig load() {
		return queryForFirst("from ProvisionConfig e order by oid");
	}

	public ProvisionConfig save(ProvisionConfig config) {
		return persist(config);
	}

}
