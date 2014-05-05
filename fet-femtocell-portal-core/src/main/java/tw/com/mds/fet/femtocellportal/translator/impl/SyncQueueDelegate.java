package tw.com.mds.fet.femtocellportal.translator.impl;

import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.translator.ImsiQueryRequest;
import tw.com.mds.fet.femtocellportal.translator.ImsiQueryResponse;
import tw.com.mds.fet.femtocellportal.translator.TranslatorException;
import tw.com.mds.fet.femtocellportal.translator.TranslatorFlowContorlService;
import tw.com.mds.fet.femtocellportal.translator.TranslatorService;

/**
 * implementation of {@link TranslatorService} which delegates to
 * {@link QuartzTranslatorFlowControlServiceImpl} to accomplish flow control synchronously.
 * 
 * @author morel
 * 
 */
public class SyncQueueDelegate implements TranslatorService {

	private TranslatorFlowContorlService controlJob;

	// TODO Translator: configurable timeout
	private long waitTimeout = 60 * 1000;
	
	public String queryImsiByMsisdn(String msisdn)
			throws ServiceException {
		ImsiQueryResponse response = controlJob.asyncExecuteRequest(new ImsiQueryRequest(msisdn));
		try {
			synchronized (response) {
				if (response.isQuerying()) {
					response.wait(waitTimeout);
				}
				if (response.isQuerying()) {
					throw new TranslatorException("translator service timeout");
				}
			}
		} catch (InterruptedException e) {
			throw new TranslatorException("thread interrupted when waiting translator response", e);
		}

		if (!response.isSuccessful()) {
			Exception e = response.getException();
			if (e instanceof TranslatorException) {
				throw (TranslatorException) e;
			}
			
			String errorMessage = "";
			if (e != null) {
				errorMessage = e.getMessage();
			}
			throw new TranslatorException(errorMessage, e);
		}
		return response.getImsi();
	}

	public boolean isUnderMaintenance() {
		return controlJob.isDelegateUnderMaintenance();
	}

	public TranslatorFlowContorlService getControlJob() {
		return controlJob;
	}

	public void setControlJob(TranslatorFlowContorlService controlJob) {
		this.controlJob = controlJob;
	}

	public long getWaitTimeout() {
		return waitTimeout;
	}

	public void setWaitTimeout(long waitTimeout) {
		this.waitTimeout = waitTimeout;
	}

}
