package tw.com.mds.fet.femtocellportal.web;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import tw.com.mds.fet.femtocellportal.core.BatchService;
import tw.com.mds.fet.femtocellportal.core.Config;
import tw.com.mds.fet.femtocellportal.core.ConfigurableService;
import tw.com.mds.fet.femtocellportal.core.ConfigurationException;
import tw.com.mds.fet.femtocellportal.core.ExceptionHandler;
import tw.com.mds.fet.femtocellportal.core.ProvisionService;
import tw.com.mds.fet.femtocellportal.core.SchedulingService;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.core.UserLogType;
import tw.com.mds.fet.femtocellportal.core.impl.ExceptionHandlerConfig;
import tw.com.mds.fet.femtocellportal.nedb.NedbException;
import tw.com.mds.fet.femtocellportal.nedb.NedbService;
import tw.com.mds.fet.femtocellportal.translator.TranslatorFlowContorlService;
import tw.com.mds.fet.femtocellportal.translator.TranslatorService;
import tw.com.mds.fet.femtocellportal.util.Utils;

/**
 * @author morel
 *
 */
public class ConfigAction extends IdentityAction {

	private static final long serialVersionUID = 1L;
	
	private int tabIndex;

	@Autowired(required = true)
	private TranslatorService fetTranslatorService;
	private Config fetTranslatorConfig;
	
	@Autowired(required = true)
	private TranslatorFlowContorlService translatorFlowContorlService;
	private Config flowControlConfig;
	
	@Autowired(required = true)
	private NedbService nedbService;
	private Config nedbConfig;
	
	@Autowired(required = true)
	private SchedulingService schedulingService;
	private Config schedulingConfig;

	@Autowired(required = true)
	private ProvisionService provisionService;
	private Config provisionConfig;
	
	@Autowired(required = true)
	private BatchService batchService;

	@Autowired(required = true)
	private ExceptionHandler exceptionHandler;
	private Config exceptionHandlerConfig;
	
	public String display() {
		fetTranslatorConfig = ((ConfigurableService) fetTranslatorService).getConfig();
		flowControlConfig = ((ConfigurableService) translatorFlowContorlService).getConfig();
		nedbConfig = ((ConfigurableService) nedbService).getConfig();
		schedulingConfig = ((ConfigurableService) schedulingService).getConfig();
		provisionConfig = ((ConfigurableService) provisionService).getConfig();
		exceptionHandlerConfig = ((ConfigurableService) exceptionHandler).getConfig();
		return SUCCESS;
	}

	public String applyFetTranslatorConfig() throws ConfigurationException {
		fetTranslatorConfig.validate();
		Config original = ((ConfigurableService) fetTranslatorService).getConfig();
		((ConfigurableService)fetTranslatorService).applyConfig(fetTranslatorConfig);
		
		logEvent(UserLogType.SYSTEM_MANAGEMENT, Utils.format(
				"組態儲存成功, 修改前:{0}, 修改為:{1}", original, fetTranslatorConfig));
		addActionMessage("儲存成功");
		return SUCCESS;
	}
	
	public String applyFlowControlConfig() throws ConfigurationException {
		flowControlConfig.validate();
		Config original = ((ConfigurableService) translatorFlowContorlService).getConfig();
		((ConfigurableService) translatorFlowContorlService).applyConfig(flowControlConfig);
		logEvent(UserLogType.SYSTEM_MANAGEMENT, Utils.format(
				"組態儲存成功, 修改前:{0}, 修改為:{1}", original, flowControlConfig));
		addActionMessage("儲存成功");
		return SUCCESS;
	}

	public String applyNedbConfig() throws ConfigurationException {
		nedbConfig.validate();
		Config original = ((ConfigurableService) nedbService).getConfig();
		((ConfigurableService) nedbService).applyConfig(nedbConfig);
		
		logEvent(UserLogType.SYSTEM_MANAGEMENT, Utils.format(
				"組態儲存成功, 修改前:{0}, 修改為:{1}", original, nedbConfig));
		addActionMessage("儲存成功");
		return SUCCESS;
	}
	
	public String executNedbCellBatchImport() throws ServiceException {
		batchService.syncCells();
		addActionMessage("execute completed");
		return SUCCESS;
	}

	public String executApmExport() throws NedbException, ConfigurationException {
		nedbService.syncFemtoProfiles();
		addActionMessage("execute completed");
		return SUCCESS;
	}

	public String applySchedulingConfig() throws ConfigurationException {
		schedulingConfig.validate();
		Config original = ((ConfigurableService) schedulingService).getConfig();
		((ConfigurableService) schedulingService).applyConfig(schedulingConfig);
		
		logEvent(UserLogType.SYSTEM_MANAGEMENT, Utils.format(
				"組態儲存成功, 修改前:{0}, 修改為:{1}", original, schedulingConfig));
		addActionMessage("儲存成功");
		return SUCCESS;
	}

	public String applyProvisionConfig() throws ConfigurationException {
		provisionConfig.validate();
		Config original = ((ConfigurableService) provisionService).getConfig();
		((ConfigurableService) provisionService).applyConfig(provisionConfig);
		
		logEvent(UserLogType.SYSTEM_MANAGEMENT, Utils.format(
				"組態儲存成功, 修改前:{0}, 修改為:{1}", original, provisionConfig));
		addActionMessage("儲存成功");
		return SUCCESS;
	}
	
	public String applyExceptionHandlerConfig() throws ConfigurationException {
		exceptionHandlerConfig.validate();
		ExceptionHandlerConfig original = (ExceptionHandlerConfig) ((ConfigurableService) exceptionHandler).getConfig();
		ExceptionHandlerConfig current = (ExceptionHandlerConfig) exceptionHandlerConfig;
		if (StringUtils.isEmpty(current.getFtpLoginPassword())) {
			current.setFtpLoginPassword(original.getFtpLoginPassword());
		}
		((ConfigurableService) exceptionHandler).applyConfig(exceptionHandlerConfig);
		
		logEvent(UserLogType.SYSTEM_MANAGEMENT, Utils.format(
				"組態儲存成功, 修改前:{0}, 修改為:{1}", original, exceptionHandlerConfig));
		addActionMessage("儲存成功");
		return SUCCESS;
	}
	
	public String testExceptionHandlerFtpConnection() {
		try {
			exceptionHandler.testFtpConnection();
		} catch (Exception e) {
			addActionError("connection failed:{0}", e.getMessage());
			return ERROR;
		}
		addActionMessage("connection success");
		return SUCCESS;
	}
	
	public Config getFetTranslatorConfig() {
		return fetTranslatorConfig;
	}

	public Config getFlowControlConfig() {
		return flowControlConfig;
	}

	public Config getNedbConfig() {
		return nedbConfig;
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}

	public Config getSchedulingConfig() {
		return schedulingConfig;
	}

	public Config getProvisionConfig() {
		return provisionConfig;
	}

	public Config getExceptionHandlerConfig() {
		return exceptionHandlerConfig;
	}

}
