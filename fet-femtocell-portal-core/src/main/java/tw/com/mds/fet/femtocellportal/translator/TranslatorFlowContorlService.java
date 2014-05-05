package tw.com.mds.fet.femtocellportal.translator;

import tw.com.mds.fet.femtocellportal.core.ServiceException;

public interface TranslatorFlowContorlService {

	/**
	 * execute request asynchronously
	 * 
	 * @param request
	 * @return
	 */
	public ImsiQueryResponse asyncExecuteRequest(ImsiQueryRequest request);

	/**
	 * submit request to translator
	 * 
	 * @throws ServiceException
	 */
	public void execute() throws ServiceException;

	/**
	 * change the throughput of the flow
	 * 
	 * @param executePerSecond
	 *            how many query submit per second
	 * @throws ServiceException
	 */
	public void changeThroughput(double executePerSecond)
			throws ServiceException;

	public boolean isDelegateUnderMaintenance();

}