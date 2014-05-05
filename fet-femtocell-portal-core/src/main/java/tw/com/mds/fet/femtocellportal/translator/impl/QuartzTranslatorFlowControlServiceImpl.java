package tw.com.mds.fet.femtocellportal.translator.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdScheduler;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tw.com.mds.fet.femtocellportal.core.Config;
import tw.com.mds.fet.femtocellportal.core.ConfigurableService;
import tw.com.mds.fet.femtocellportal.core.ConfigurationException;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.translator.ImsiQueryRequest;
import tw.com.mds.fet.femtocellportal.translator.ImsiQueryResponse;
import tw.com.mds.fet.femtocellportal.translator.ImsiQueryResponse.Result;
import tw.com.mds.fet.femtocellportal.translator.TranslatorException;
import tw.com.mds.fet.femtocellportal.translator.TranslatorFlowContorlService;
import tw.com.mds.fet.femtocellportal.translator.TranslatorService;
import tw.com.mds.fet.femtocellportal.translator.UnableToTranslateImsiException;
import tw.com.mds.fet.femtocellportal.util.Utils;

/**
 * quartz implementation
 * 
 * @author morel
 *
 */
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = ServiceException.class)
public class QuartzTranslatorFlowControlServiceImpl implements TranslatorFlowContorlService, ConfigurableService {
	
	private static final Log logger = LogFactory.getLog(QuartzTranslatorFlowControlServiceImpl.class);

	private int firedCount = 0;
	
	private StdScheduler scheduler;
	private SimpleTrigger currentTrigger;
	private TranslatorService translatorService;
	private Queue<ImsiQueryRequest> queue = new ConcurrentLinkedQueue<ImsiQueryRequest>();
	private FlowControlConfigDao flowControlConfigDao;

	private FlowControlConfig config;

	public void init() throws ConfigurationException {
		reloadConfig();
	}
	
	public ImsiQueryResponse asyncExecuteRequest(ImsiQueryRequest request) {
		ImsiQueryResponse response = new ImsiQueryResponse(null, Result.QUERYING);
		request.setResponse(response);
		addQueue(request);
		return response;
	}

	public void execute() throws ServiceException {
		ImsiQueryRequest request = takeQueue();
		if (request == null) {
			if (logger.isTraceEnabled()) {
				logger.trace("queue is empty, waiting for request");
			}
			return;
		}
		
		// fake request for working thread interruption
		if (request.getMsisdn() == null) {
			return;
		}
		
		int currentMaxRetry = (config.getEnableRetry()) ? (config.getMaxRertry()) : (0);
		int currentRerty = 0;
		long interval = calculateInterval(config.getExecutePerSecond());
		ImsiQueryResponse response = request.getResponse();
		
		String imsi = null;
		Result result = Result.FAILED;
		Exception exception = null;
		
		// retrying block
		do {
			if (currentRerty > 0) {
				Utils.info(logger, Utils.format(
						"retrying:{0} for request:{1}, maxRerty:{2}", 
						currentRerty, request, currentMaxRetry));
			}
			
			try {
				// query imsi
				Utils.debug(logger, Utils.format("flow control sending request with msisdn:" + request.getMsisdn()));
				imsi = translatorService.queryImsiByMsisdn(request.getMsisdn());
				result = Result.SUCCESSFUL;
				firedCount++;
				break;
			} catch (ServiceUnderMaintenanceException e) {
				result = Result.SERVICE_MAINTENANCE;
				exception = e;
				break;
			} catch (UnableToTranslateImsiException e) {
				exception = e;
				break;
			} catch (Exception e) {
				Utils.debug(logger, Utils.format(
						"delaying {0} milisecond for next time retry of request:{1}", 
						interval, request));
				// wait to retry
				try {
					Thread.sleep(interval);
				} catch (Exception ie) {
					result = Result.FAILED;
					exception = ie;
					logger.error(Utils.format(
							"thread sleep exception when translating imsi, msisdn:{0}, reason:{0}", 
							request.getMsisdn(), ie.getMessage()), ie);
				}
				currentRerty++;
				
				// reach max retry
				if (currentRerty > currentMaxRetry) {
					result = Result.SERVICE_UAVAILABLE;
					exception = e;
					Utils.info(logger, Utils.format(
							"request:{0} max retry:{1} reached, stop to retry", 
							request, config.getMaxRertry()));
				}
			}
		} while(currentRerty <= currentMaxRetry);
		
		synchronized (response) {
			response.copy(new ImsiQueryResponse(imsi, result, exception));
			response.notifyAll();
		}
	}

	private void addQueue(ImsiQueryRequest request) {
		queue.add(request);
	}

	private ImsiQueryRequest takeQueue() throws ServiceException {
		return queue.poll();
	}

	/**
	 * reschedule the quartz scheduler by new trigger with new execution interval
	 * (calculated by executePerSecond argument)
	 * 
	 * @see tw.com.mds.fet.femtocellportal.translator.TranslatorFlowContorlService#changeThroughput(double)
	 */
	public synchronized void changeThroughput(double executePerSecond) throws ServiceException {
		long interval = calculateInterval(executePerSecond);
		SimpleTrigger newTrigger = new SimpleTrigger();
		newTrigger.setRepeatInterval(interval);
		newTrigger.setName(currentTrigger.getName());
		newTrigger.setJobName(currentTrigger.getJobName());
		newTrigger.setStartTime(new Date());
		newTrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
		try {
			scheduler.rescheduleJob(currentTrigger.getName(), currentTrigger.getGroup(), newTrigger);
			currentTrigger = newTrigger;
			this.config.setExecutePerSecond(executePerSecond);
			Utils.info(logger, Utils.format(
					"rescheduled Quartz StdScheduler to executePerSecond:{0}, trigger inverval:{1}", 
					executePerSecond, interval));
		} catch (SchedulerException e) {
			throw new TranslatorException(14005, e);
		}
	}

	public boolean isDelegateUnderMaintenance() {
		return translatorService.isUnderMaintenance();
	}

	long calculateInterval(double timesPerSecond) {
		if (timesPerSecond <= 0) {
			throw new IllegalArgumentException("timesPerSecond must larger than 0");
		}
		long interval = Math.round(1000 / timesPerSecond);
		return interval;
	}
	
	public void reloadConfig() throws ConfigurationException {
		FlowControlConfig loaded = flowControlConfigDao.load();
		if (loaded == null) {
			Utils.warn(logger, "no config found for FlowControlConfig, using current config:{0}", config);
			return;
		}
		
		loaded.validate();
		this.config = loaded;
		
		// change scheduler throughput
		try {
			changeThroughput(this.config.getExecutePerSecond());
		} catch (ServiceException e) {
			throw new ConfigurationException(Utils.format("failed to reload config:{0}", e.getMessage()), e);
		}
		Utils.info(logger, "reloaded FlowControlConfig:{0}", loaded);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public void applyConfig(Config config) throws ConfigurationException {
		FlowControlConfig newConfig = (FlowControlConfig) config;
		newConfig.validate();
		
		newConfig.setOid(this.config.getOid());
		newConfig.setUpdateTime(new Date());
		
		newConfig = flowControlConfigDao.save(newConfig);
		this.config = newConfig;

		// change scheduler throughput
		try {
			changeThroughput(this.config.getExecutePerSecond());
		} catch (ServiceException e) {
			throw new ConfigurationException(Utils.format("failed to reload config:{0}", e.getMessage()), e);
		}
		Utils.info(logger, "applied FlowControlConfig:{0}", newConfig);
	}

	public void testExecute(int times) {
		List<ImsiQueryResponse> result = new ArrayList<ImsiQueryResponse>();
		for (int i = 0; i < times; i++) {
			String msisdn = StringUtils.leftPad(String.valueOf(i), 10, "0");
			ImsiQueryRequest request = new ImsiQueryRequest(msisdn);
			result.add(asyncExecuteRequest(request));
		}
	}
	
	public void destroy() {
		// add a poision object(null msisdn request) to tell end of waiting
		addQueue(new ImsiQueryRequest(null));
	}
	
	public long getRepeatInterval() {
		return currentTrigger.getRepeatInterval();
	}
	
	public StdScheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(StdScheduler scheduler) {
		this.scheduler = scheduler;
	}

	public SimpleTrigger getCurrentTrigger() {
		return currentTrigger;
	}

	public void setCurrentTrigger(SimpleTrigger currentTrigger) {
		this.currentTrigger = currentTrigger;
	}

	public int getFiredCount() {
		return firedCount;
	}

	public TranslatorService getTranslatorService() {
		return translatorService;
	}

	public void setTranslatorService(TranslatorService translatorService) {
		this.translatorService = translatorService;
	}

	public Queue<ImsiQueryRequest> getQueue() {
		return queue;
	}

	public void setQueue(Queue<ImsiQueryRequest> queue) {
		this.queue = queue;
	}

	public FlowControlConfig getConfig() {
		try {
			return (FlowControlConfig) BeanUtils.cloneBean(config);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setConfig(FlowControlConfig config) {
		this.config = config;
	}

	public FlowControlConfigDao getFlowControlConfigDao() {
		return flowControlConfigDao;
	}

	public void setFlowControlConfigDao(FlowControlConfigDao flowControlConfigDao) {
		this.flowControlConfigDao = flowControlConfigDao;
	}

}
