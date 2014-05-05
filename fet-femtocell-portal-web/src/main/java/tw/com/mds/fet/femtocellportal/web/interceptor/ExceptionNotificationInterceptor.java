package tw.com.mds.fet.femtocellportal.web.interceptor;

import org.springframework.remoting.RemoteAccessException;

import tw.com.mds.fet.femtocellportal.core.ExceptionHandler;
import tw.com.mds.fet.femtocellportal.core.ServiceException;

import com.caucho.hessian.HessianException;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ExceptionNotificationInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	private ExceptionHandler exceptionHandler;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String result = invocation.invoke();
		if (!"systemError".equals(result)) {
			return result;
		}

		Object e = invocation.getStack().findValue("exception");
		if (e != null && (e instanceof RemoteAccessException || e instanceof HessianException)) {
			Exception rae = (Exception) e;
			ServiceException translatedException = 
				new ServiceException(10002, "unable to connect to provision server:" + rae.getMessage(), rae);
			exceptionHandler.handleException(translatedException);
		}
		return result;
	}

	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

}
