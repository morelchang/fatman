package tw.com.mds.fet.femtocellportal.core.impl;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.SchedulerException;
import org.quartz.impl.StdScheduler;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tw.com.mds.fet.femtocellportal.core.Config;
import tw.com.mds.fet.femtocellportal.core.ConfigurableService;
import tw.com.mds.fet.femtocellportal.core.ConfigurationException;
import tw.com.mds.fet.femtocellportal.core.SchedulingService;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.util.Utils;

public class SchedulingServiceImpl implements SchedulingService, ConfigurableService {
	
	private static final Log logger = LogFactory.getLog(SchedulingServiceImpl.class);
	
	private SchedulingConfigDao schedulingConfigDao;
	private SchedulingConfig config;
	private StdScheduler scheduler;
	private CronTriggerBean nedbSyncCellsTrigger;
	private CronTriggerBean nedbSyncFemtoProfilesTrigger;

	public void init() throws ConfigurationException {
		reloadConfig();
	}
	
	public void reloadConfig() throws ConfigurationException {
		SchedulingConfig loaded = schedulingConfigDao.load();
		if (loaded == null) {
			Utils.warn(logger, "no SchedulingConfig found, using current config:{0}", this.config);
			return;
		}
		
		loaded.validate();
		this.config = loaded;
		
		nedbSyncCellsTrigger = recheduleCronTrigger(nedbSyncCellsTrigger, this.config.getDailySyncCellsTime());
		nedbSyncFemtoProfilesTrigger = recheduleCronTrigger(nedbSyncFemtoProfilesTrigger, this.config.getDailySyncFemtoProfilesTime());
		Utils.info(logger, "reloaded SchedulingConfig:{0}", loaded);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public void applyConfig(Config config) throws ConfigurationException {
		SchedulingConfig newConfig = (SchedulingConfig) config;
		newConfig.validate();
		newConfig.format();
		
		newConfig.setOid(this.config.getOid());
		newConfig.setUpdateTime(new Date());
		
		newConfig = schedulingConfigDao.save(newConfig);
		this.config = newConfig;
		
		nedbSyncCellsTrigger = recheduleCronTrigger(nedbSyncCellsTrigger, this.config.getDailySyncCellsTime());
		nedbSyncFemtoProfilesTrigger = recheduleCronTrigger(nedbSyncFemtoProfilesTrigger, this.config.getDailySyncFemtoProfilesTime());
		Utils.info(logger, "applied SchedulingConfig:{0}", newConfig);
	}

	private CronTriggerBean recheduleCronTrigger(
			CronTriggerBean currentTrigger, String time)
			throws ConfigurationException {
		CronTriggerBean newTrigger = new CronTriggerBean();
		newTrigger.setName(currentTrigger.getName());
		newTrigger.setJobName(currentTrigger.getJobName());
		newTrigger.setStartTime(new Date());
		try {
			newTrigger.setCronExpression(formatCronExpression(time));
			scheduler.rescheduleJob(currentTrigger.getName(), currentTrigger.getGroup(), newTrigger);
			return newTrigger;
			
		} catch (SchedulerException e) {
			throw new ConfigurationException(Utils.format(
					"failed to reschedule trigger:{0}, reason:{1}", 
					currentTrigger, e.getMessage()), e);
		} catch (ParseException e) {
			throw new ConfigurationException(Utils.format(
					"failed to reschedule trigger:{0}, reason:{1}", 
					currentTrigger, e.getMessage()), e);
		}
	}

	private String formatCronExpression(String time) {
		String hh = StringUtils.split(time, ":")[0];
		String mm = StringUtils.split(time, ":")[1];
		return Utils.format("0 {0} {1} * * ?", mm, hh);
	}

	public StdScheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(StdScheduler scheduler) {
		this.scheduler = scheduler;
	}

	public SchedulingConfig getConfig() {
		try {
			return (SchedulingConfig) BeanUtils.cloneBean(config);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setConfig(SchedulingConfig config) {
		this.config = config;
	}

	public CronTriggerBean getNedbSyncCellsTrigger() {
		return nedbSyncCellsTrigger;
	}

	public void setNedbSyncCellsTrigger(CronTriggerBean nedbSyncCellsTrigger) {
		this.nedbSyncCellsTrigger = nedbSyncCellsTrigger;
	}

	public CronTriggerBean getNedbSyncFemtoProfilesTrigger() {
		return nedbSyncFemtoProfilesTrigger;
	}

	public void setNedbSyncFemtoProfilesTrigger(
			CronTriggerBean nedbSyncFemtoProfilesTrigger) {
		this.nedbSyncFemtoProfilesTrigger = nedbSyncFemtoProfilesTrigger;
	}

	public SchedulingConfigDao getSchedulingConfigDao() {
		return schedulingConfigDao;
	}

	public void setSchedulingConfigDao(SchedulingConfigDao schedulingConfigDao) {
		this.schedulingConfigDao = schedulingConfigDao;
	}

}
