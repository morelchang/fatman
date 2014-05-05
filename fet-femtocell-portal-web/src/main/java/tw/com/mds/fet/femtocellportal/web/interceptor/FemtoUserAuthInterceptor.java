package tw.com.mds.fet.femtocellportal.web.interceptor;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsConstants;
import org.apache.struts2.StrutsStatics;

import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.core.ProvisionService;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.util.Utils;
import tw.com.mds.fet.femtocellportal.web.IdentityAction;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class FemtoUserAuthInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;
	private static final Log logger = LogFactory.getLog(FemtoUserAuthInterceptor.class);

	private static final String RESULT_SSO_LOGIN = "ssoLogin";
	private static final String RESULT_SSO_NO_FEMTO_SERVICE = "ssoNoFemtoService";
	
	@Inject(value = StrutsConstants.STRUTS_DEVMODE)
	private String devMode;
	
	private ProvisionService provisionService;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Utils.debug(logger, "authenticating femtouser...");
		if (Boolean.valueOf(devMode)) {
			Utils.info(logger, "devMode enabled, skip authenticating femtouser");
			return invocation.invoke();
		}
		
		// user authenticated
		Map<String, Object> sessionMap = getSessionMap(invocation);
		FemtoUser loginFemtoUser = getLoginFemtoUser(sessionMap);
		if (loginFemtoUser != null) {
			Utils.debug(logger, "femtouser:{0} found in session, no authentication needed", loginFemtoUser.getMobile());
			String sourceIp = getHttpServletRequest(invocation).getRemoteAddr();
			loginFemtoUser.setSourceIp(sourceIp);
			return invocation.invoke();
		}

		// trying to authenticate by sso header
		if (provisionService.isEnableSsoAuthentication()) {
			Utils.debug(logger, "sso enabled, authenticating by sso...");  
			String ssoMsisdnHeaderValue = getSsoMsisdnHeaderValue(invocation);
			if (ssoMsisdnHeaderValue != null) {
				String femtoUserMsisdn = parseSsoMsisdnHeaderValue(ssoMsisdnHeaderValue);
				Utils.debug(logger, "parsed msisdn:{0}", femtoUserMsisdn);
				
				if (femtoUserMsisdn != null) {
					return loginBySso(invocation, sessionMap, femtoUserMsisdn);
				}
			} else {
				if (!provisionService.isEnableLocalAuthentication()) {
					Utils.debug(logger, "return local authentication page result");
					return RESULT_SSO_LOGIN;
				}
			}
		}
		
		// trying to authenticate by local
		if (provisionService.isEnableLocalAuthentication()) {
			Utils.debug(logger, "local authentication enabled, trying to authenticate with local session");
			return Action.LOGIN;
		}
		
		Utils.error(logger, "no authentication method was set!");
		return Action.ERROR;
	}

	private FemtoUser getLoginFemtoUser(Map<String, Object> sessionMap) {
		return (FemtoUser) sessionMap.get(IdentityAction.LOGIN_FEMTOUSER_SESSION_KEY);
	}

	private static String parseSsoMsisdnHeaderValue(String femtoUserMsisdn) {
		if (femtoUserMsisdn != null && femtoUserMsisdn.length() > 0
				&& femtoUserMsisdn.indexOf("@") >= 0) {
			return femtoUserMsisdn.substring(0, femtoUserMsisdn.indexOf("@"));
		}
		return femtoUserMsisdn;
	}

	private String getSsoMsisdnHeaderValue(ActionInvocation invocation) {
		HttpServletRequest request= getHttpServletRequest(invocation);
		String headerKey = provisionService.getSsoMsisdnHeaderKey();
		String headerValue = request.getHeader(headerKey);
		Utils.debug(logger, "header key:{0}, value:{1}", headerKey, headerValue);
		return headerValue;
	}

	private String loginBySso(ActionInvocation invocation,
			Map<String, Object> sessionMap, String femtoUserMsisdn)
			throws ServiceException, Exception {
		List<FemtoUser> femtoUsers = provisionService.findUsersByMobile(femtoUserMsisdn);
		// no femto user found
		if (femtoUsers.isEmpty()) {
			Utils.warn(logger, "can''t find FemtoUser by msisdn:{0}", femtoUserMsisdn);
			return RESULT_SSO_NO_FEMTO_SERVICE;
		}
		
		// more than one femto users found!
//		if (femtoUsers.size() > 1) {
//			Utils.error(logger, "more than one FemtoUser found by msisdn:{0}, FemtoUsers:{1}", 
//					femtoUserMsisdn, femtoUsers);
//			return RESULT_SSO_LOGIN;
//		}
		FemtoUser femtoUser = femtoUsers.get(0);
		
		// set source ip
		String sourceIp = getHttpServletRequest(invocation).getRemoteAddr();
		femtoUser.setSourceIp(sourceIp);

		// login as femto user
		Utils.debug(logger, "login as FemtoUser:{0}", femtoUser);
		
		/*
		 *  mark this since sso may not support session
		 */
//		FemtoUser loginFemtoUser = provisionService.loginFemtoUser(
//				femtoUser.getAccount(), femtoUser.getPassword(),
//				femtoUser.getSourceIp());
		
		/*
		 * put to session for CURRENT request
		 */
		sessionMap.put(IdentityAction.LOGIN_FEMTOUSER_SESSION_KEY, femtoUser);
		return invocation.invoke();
	}

	private Map<String, Object> getSessionMap(ActionInvocation invocation) {
		return invocation.getInvocationContext().getSession();
	}

	private HttpServletRequest getHttpServletRequest(ActionInvocation invocation) {
		return (HttpServletRequest) invocation.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
	}

	public ProvisionService getProvisionService() {
		return provisionService;
	}

	public void setProvisionService(ProvisionService provisionService) {
		this.provisionService = provisionService;
	}

}
