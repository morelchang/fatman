package tw.com.mds.fet.femtocellportal.core.dao.impl;

import tw.com.mds.fet.femtocellportal.core.dao.ExceptionHandlerConfigDao;
import tw.com.mds.fet.femtocellportal.core.impl.ExceptionHandlerConfig;
import tw.com.mds.fet.femtocellportal.dao.impl.JpaGenericDao;

public class JpaExceptionHandlerConfigDao extends
		JpaGenericDao<ExceptionHandlerConfig> implements
		ExceptionHandlerConfigDao {

	public ExceptionHandlerConfig load() {
		return queryForFirst("from ExceptionHandlerConfig e order by oid");
	}

	public ExceptionHandlerConfig save(ExceptionHandlerConfig config) {
		return persist(config);
	}

}
