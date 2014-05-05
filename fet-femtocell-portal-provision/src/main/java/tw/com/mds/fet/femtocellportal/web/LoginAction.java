package tw.com.mds.fet.femtocellportal.web;

import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.core.AdminUser;
import tw.com.mds.fet.femtocellportal.core.ProvisionService;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.core.UserLogType;
import tw.com.mds.fet.femtocellportal.util.Utils;

public class LoginAction extends IdentityAction {

	private static final long serialVersionUID = 1L;

	private ProvisionService provisionService;

	private String account;
	private String password;

	@Override
	public void prepare() throws Exception {
		this.account = "";
		this.password = "";
		super.prepare();
	}
	
	public String displayLoginAdmin() {
		return LOGIN;
	}

	public String loginAdmin() {
		if (StringUtils.isBlank(account)) {
			addActionError("帳號不可空白");
			return ERROR;
		}
		
		if (StringUtils.isEmpty(password)) {
			addActionError("密碼不可空白");
			return ERROR;
		}
		
		AdminUser adminUser = null;
		try {
			adminUser = provisionService.loginAdminUser(account, password, getSourceIp());
			getSession().invalidate();
			setLoginAdminUser(adminUser);
		} catch (ServiceException e) {
			addActionError("系統錯誤, 原因:{0}", e.getMessage());
			logEvent(UserLogType.ADMINUSER_LOGINFAILED, Utils.format("帳號:{0}, 錯誤原因:{1}", account, e.getMessage()));
			return ERROR;
		}
		
		if (adminUser != null) {
			logEvent(UserLogType.ADMINUSER_LOGIN, Utils.format("登入人員:{0}", describe(adminUser)));
			return SUCCESS;
		} else {
			logEvent(UserLogType.ADMINUSER_LOGINFAILED, Utils.format("帳號:{0}", account));
			addActionError("登入失敗");
			return ERROR;
		}
	}

	public String logoutAdmin() {
		AdminUser adminUser = getLoginAdminUser();
		if (adminUser != null) {
			logEvent(UserLogType.ADMINUSER_LOGOUT, Utils.format("登出人員:{0}", describe(adminUser)));
		}
		
		clearLoginAdminUser();
		getSession().invalidate();
		return SUCCESS;
	}
	
	public ProvisionService getProvisionService() {
		return provisionService;
	}

	public void setProvisionService(ProvisionService provisionService) {
		this.provisionService = provisionService;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
