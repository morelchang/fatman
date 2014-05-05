package tw.com.mds.fet.femtocellportal.translator.impl;

import tw.com.mds.fet.femtocellportal.dao.impl.JpaGenericDao;

public class JpaFlowControlConfigDao extends JpaGenericDao<FlowControlConfig> implements FlowControlConfigDao {

	public FlowControlConfig load() {
		return queryForFirst("from FlowControlConfig e order by oid");
	}

	public FlowControlConfig save(FlowControlConfig config) {
		return persist(config);
	}

}
