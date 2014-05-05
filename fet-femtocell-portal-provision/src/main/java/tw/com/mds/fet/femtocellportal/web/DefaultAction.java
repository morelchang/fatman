package tw.com.mds.fet.femtocellportal.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

import tw.com.mds.fet.femtocellportal.util.Utils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class DefaultAction extends ActionSupport implements SessionAware,
		Preparable {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> map;

	public DefaultAction() {
		super();
	}

	public void prepare() throws Exception {
		clearErrorsAndMessages();
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

	private HttpServletRequest getHttpServletRequest() {
		return ((HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST));
	}

}