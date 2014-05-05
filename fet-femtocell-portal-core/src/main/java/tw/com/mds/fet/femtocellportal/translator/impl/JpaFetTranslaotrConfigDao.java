package tw.com.mds.fet.femtocellportal.translator.impl;

import tw.com.mds.fet.femtocellportal.dao.impl.JpaGenericDao;

public class JpaFetTranslaotrConfigDao extends
		JpaGenericDao<FetTranslatorConfig> implements FetTranslatorConfigDao {

	public FetTranslatorConfig load() {
		return queryForFirst("from FetTranslatorConfig e order by oid");
	}

	public FetTranslatorConfig save(FetTranslatorConfig config) {
		return persist(config);
	}

}
