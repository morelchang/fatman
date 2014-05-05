package tw.com.mds.fet.femtocellportal.translator;

import tw.com.mds.fet.femtocellportal.core.ServiceException;

/**
 * translator service interface, defines IMSI/MSISDN translation query service.
 * 
 * @author morel
 * 
 */
public interface TranslatorService {

	/**
	 * query IMSI by MSISDN
	 * 
	 * @param msisdn
	 * @return IMSI
	 * @throws ServiceException
	 */
	public String queryImsiByMsisdn(String msisdn) throws ServiceException;

	/**
	 * determine the service is under maintenance not not
	 * 
	 * @return
	 */
	public boolean isUnderMaintenance();

}
