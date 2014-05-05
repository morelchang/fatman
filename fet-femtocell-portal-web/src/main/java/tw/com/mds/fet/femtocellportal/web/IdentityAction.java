package tw.com.mds.fet.femtocellportal.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.mds.fet.femtocellportal.core.AdminUser;
import tw.com.mds.fet.femtocellportal.core.AdministrationService;
import tw.com.mds.fet.femtocellportal.core.Announcement;
import tw.com.mds.fet.femtocellportal.core.Config;
import tw.com.mds.fet.femtocellportal.core.FemtoProfile;
import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.core.LoggingService;
import tw.com.mds.fet.femtocellportal.core.PrivacyBeanDescriber;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.core.UserLogType;
import tw.com.mds.fet.femtocellportal.core.UserType;
import tw.com.mds.fet.femtocellportal.translator.impl.FetTranslatorConfig;
import tw.com.mds.fet.femtocellportal.util.BeanDescriber;
import tw.com.mds.fet.femtocellportal.util.BeanDescriberImpl;

public class IdentityAction extends DefaultAction {

	private static final long serialVersionUID = 1L;
	private static final Log logger = LogFactory.getLog(IdentityAction.class);

	public static final String LOGIN_ADMIN_USER_SESSION_KEY = "tw.com.mds.fet.femtocell.web.LoginAction.LOGIN_ADMIN_USER";
	public static final String LOGIN_FEMTOUSER_SESSION_KEY = "tw.com.mds.fet.femtocell.web.LoginAction.LOGIN_SUBSCRIBER";

	protected AdministrationService administrationService;
	private LoggingService loggingService;
	
	private List<Announcement> announcements = new ArrayList<Announcement>();
	private boolean underMaintenance;
	private int maintenanceHourFrom;
	private int maintenanceHourTo;
	private String ssoLoginUrl;
	private String ssoLogoutUrl;
	
	private BeanDescriber beanDescriber = new PrivacyBeanDescriber(new BeanDescriberImpl());
	
	public IdentityAction() {
		super();
	}

	protected String describe(Object o) {
		if (o == null) {
			return "";
		}
		return beanDescriber.describe(o);
	}

	@Override
	public void prepare() throws Exception {
		this.underMaintenance = administrationService.checkUnderMaintenance();
		this.ssoLoginUrl = provisionService.getSsoLoginUrl();
		this.ssoLogoutUrl = provisionService.getSsoLogoutUrl();
		prepareMaintenanceTime();
		reloadAnnouncement();
		super.prepare();
	}

	private void prepareMaintenanceTime() {
		Map<Integer, Config> configMap = administrationService.getConfigMap();
		Iterator<Entry<Integer, Config>> it = configMap.entrySet().iterator();
		while (it.hasNext()) {
			Config config = it.next().getValue();
			if (config instanceof FetTranslatorConfig) {
				FetTranslatorConfig translatorConfig = (FetTranslatorConfig) config;
				this.maintenanceHourFrom = translatorConfig.getMaintenanceHourFrom();
				this.maintenanceHourTo = translatorConfig.getMaintenanceHourTo();
			}
		}
	}

	protected void reloadAnnouncement() throws ServiceException {
		if (getLoginFemtoUser() == null) {
			logger.warn("no user found in session");
		}
		
		// TODO announcement: make a cache for announcements
		announcements = administrationService.findPublishedAnnouncements(UserType.FEMTOUSER, new Date());
	}

	protected void logEvent(UserLogType type, String message) {
		logEvent(type, null, message);
	}

	protected void logEvent(UserLogType type, FemtoProfile profile, String message) {
		loggingService.logEvent(type, getSourceIp(), getLoginFemtoUser(), profile, message);
	}
	
	public FemtoUser getLoginFemtoUser() {
		if (isSsoLogin()) {
			return getSsoLoginUser();
		}
		FemtoUser femtoUser = getSessionObject(LOGIN_FEMTOUSER_SESSION_KEY);
		if (femtoUser == null) {
			return femtoUser;
		}
		femtoUser = provisionService.findUserByOid(femtoUser.getOid());
		femtoUser.setSourceIp(getRemoteAddr());
		return femtoUser;
	}
	
	protected void setLoginFemtoUser(FemtoUser femtoUser) {
		putSessionObject(LOGIN_FEMTOUSER_SESSION_KEY, femtoUser);
	}
	
	protected void clearLoginFemtoUser() {
		removeSessionObject(LOGIN_FEMTOUSER_SESSION_KEY);
	}

	protected void setLoginAdminUser(AdminUser adminUser) {
		putSessionObject(LOGIN_ADMIN_USER_SESSION_KEY, adminUser);
	}

	protected void clearLoginAdminUser() {
		removeSessionObject(LOGIN_ADMIN_USER_SESSION_KEY);
	}
	
	protected AdminUser getLoginAdminUser() {
		return getSessionObject(LOGIN_ADMIN_USER_SESSION_KEY);
	}

	public List<Announcement> getAnnouncements() {
		return announcements;
	}

	public AdministrationService getAdministrationService() {
		return administrationService;
	}

	public void setAdministrationService(AdministrationService administrationService) {
		this.administrationService = administrationService;
	}

	public boolean isUnderMaintenance() {
		return underMaintenance;
	}

	public int getMaintenanceHourFrom() {
		return maintenanceHourFrom;
	}

	public int getMaintenanceHourTo() {
		return maintenanceHourTo;
	}

	public LoggingService getLoggingService() {
		return loggingService;
	}

	public void setLoggingService(LoggingService loggingService) {
		this.loggingService = loggingService;
	}

	public String getSsoLoginUrl() {
		return ssoLoginUrl;
	}

	public String getSsoLogoutUrl() {
		return ssoLogoutUrl;
	}

}