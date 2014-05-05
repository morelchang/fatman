package tw.com.mds.fet.femtocellportal.core.dao;

import tw.com.mds.fet.femtocellportal.core.impl.ExceptionHandlerConfig;

public interface ExceptionHandlerConfigDao {

	public ExceptionHandlerConfig load();
	
	public ExceptionHandlerConfig save(ExceptionHandlerConfig config);
	
}
