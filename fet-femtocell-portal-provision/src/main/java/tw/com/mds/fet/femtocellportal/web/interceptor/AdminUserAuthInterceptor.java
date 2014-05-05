package tw.com.mds.fet.femtocellportal.web.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsConstants;
import org.apache.struts2.StrutsStatics;

import tw.com.mds.fet.femtocellportal.core.AdminUser;
import tw.com.mds.fet.femtocellportal.util.Utils;
import tw.com.mds.fet.femtocellportal.web.IdentityAction;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;


public class AdminUserAuthInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;
	private static final Log logger = LogFactory.getLog(AdminUserAuthInterceptor.class);
	
	@Inject(value = StrutsConstants.STRUTS_DEVMODE)
	private String devMode;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		if (Boolean.valueOf(devMode)) {
			Utils.info(logger, "devMode enabled, skip authenticating adminuser");
			return invocation.invoke();
		}
		
		Map<String, Object> sessionMap = invocation.getInvocationContext().getSession();
		AdminUser adminUser = getLoginAdminUser(sessionMap);
		if (adminUser == null) {
			Utils.debug(logger, "session not found, redirect to local login page");
			return Action.LOGIN;
		}
		
		// set source ip address to login user
		String sourceIp = getHttpServletRequest(invocation).getRemoteAddr();
		adminUser.setSourceIp(sourceIp);
		
		Utils.debug(logger, "AdminUser authenticated:{0}", adminUser.getUserId());
		return invocation.invoke();
	}

	private HttpServletRequest getHttpServletRequest(ActionInvocation invocation) {
		return (HttpServletRequest) invocation.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
	}

	private AdminUser getLoginAdminUser(Map<String, Object> sessionMap) {
		return (AdminUser) sessionMap.get(IdentityAction.LOGIN_ADMIN_USER_SESSION_KEY);
	}

}
