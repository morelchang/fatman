package tw.com.mds.fet.femtocellportal.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.core.ProvisionService;
import tw.com.mds.fet.femtocellportal.util.Utils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class DefaultAction extends ActionSupport implements SessionAware,
		Preparable {

	private static final long serialVersionUID = 1L;
//	private static final Log logger = LogFactory.getLog(DefaultAction.class);
	
	private Map<String, Object> map;
	protected ProvisionService provisionService;

	public DefaultAction() {
		super();
	}

	public void prepare() throws Exception {
		clearErrorsAndMessages();
	}
	
	public String getRemoteAddr() {
		return getHttpServletRequest().getRemoteAddr();
	}
	
	public String getRemoteHost() {
		return getHttpServletRequest().getRemoteHost();
	}

	public String getServerContext() {
		HttpServletRequest httpServletRequest = getHttpServletRequest();
		String serverContext = null;
		if (isSsoLogin()) {
			serverContext = provisionService.getSsoLoginUrl();
		} else {
			serverContext = httpServletRequest.getScheme() + "://"
					+ httpServletRequest.getServerName() + ":"
					+ httpServletRequest.getServerPort()
					+ httpServletRequest.getContextPath();
		}
//		Utils.debug(logger, "serverContext:{0}", serverContext);
		return serverContext;
	}

	public boolean isSsoLogin() {
		String ssoHeaderValue = getSsoMsisdn();
//		Utils.debug(logger, "check sso header value:{0}, is sso login:{1}", ssoHeaderValue, ssoHeaderValue != null);
		return ssoHeaderValue != null;
	}

	private String getSsoMsisdn() {
		HttpServletRequest httpServletRequest = getHttpServletRequest();
		String ssoHeaderMsisdnValue = httpServletRequest.getHeader(provisionService.getSsoMsisdnHeaderKey());
		return parseSsoMsisdnHeaderValue(ssoHeaderMsisdnValue);
	}

	private HttpServletRequest getHttpServletRequest() {
		return ((HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST));
	}

	private static String parseSsoMsisdnHeaderValue(String femtoUserMsisdn) {
		if (femtoUserMsisdn != null && femtoUserMsisdn.length() > 0
				&& femtoUserMsisdn.indexOf("@") >= 0) {
			return femtoUserMsisdn.substring(0, femtoUserMsisdn.indexOf("@"));
		}
		return femtoUserMsisdn;
	}

	protected FemtoUser getSsoLoginUser() {
		List<FemtoUser> users = provisionService.findUsersByMobile(getSsoMsisdn());
		if (users.isEmpty()) {
			return null;
		}
		FemtoUser user = users.get(0);
		user.setSourceIp(getRemoteAddr());
		return user;
	}

	@SuppressWarnings("unchecked")
	public <T> T getSessionObject(String key) {
		return (T) map.get(key);
	}

	public <T> void putSessionObject(String key, T object) {
		map.put(key, object);
	}

	public void removeSessionObject(String key) {
		map.remove(key);
	}

	public void addActionMessage(String aMessage, Object... params) {
		this.addActionMessage(Utils.format(aMessage, params));
	}

	public void addActionError(String anErrorMessage, Object... params) {
		this.addActionError(Utils.format(anErrorMessage, params));
	}

	public void setSession(Map<String, Object> map) {
		this.map = map;
	}
	
	public SessionMap<String, Object> getSession() {
		return (SessionMap<String, Object>) this.map;
	}

	protected boolean validateStringLengthIsOver(String value, int length,
			String msg) {
				if (value != null && value.length() > length) {
					return true;
				}
				addActionError(msg);
				return false;
			}

	protected boolean validateStringEmpty(String value, String msg) {
		if (!StringUtils.isEmpty(value)) {
			return true;
		}
		addActionError(msg);
		return false;
	}

	protected boolean validateEmpty(Object value, String msg) {
		if (value == null) {
			addActionError(msg);
			return false;
		}
		return true;
	}

	protected String getSourceIp() {
		return getHttpServletRequest().getRemoteAddr();
	}

	public ProvisionService getProvisionService() {
		return provisionService;
	}

	public void setProvisionService(ProvisionService provisionService) {
		this.provisionService = provisionService;
	}

}