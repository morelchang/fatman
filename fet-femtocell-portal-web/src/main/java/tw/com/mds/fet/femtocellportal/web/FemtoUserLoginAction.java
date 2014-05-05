package tw.com.mds.fet.femtocellportal.web;

import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.core.UserLogType;
import tw.com.mds.fet.femtocellportal.util.Utils;

public class FemtoUserLoginAction extends IdentityAction {

	private static final long serialVersionUID = 1L;
	private String account;
	private String password;

	@Override
	public void prepare() throws Exception {
		this.account = "";
		this.password = "";
		super.prepare();
	}

	public String displayLoginFemtoUser() {
		return SUCCESS;
	}

	public String loginFemtoUser() {
		if (StringUtils.isBlank(account)) {
			addActionError("帳號不可空白");
			return ERROR;
		}

		FemtoUser femtoUser = null;
		try {
			femtoUser = provisionService.loginFemtoUser(account, password, getSourceIp());
			getSession().invalidate();
			setLoginFemtoUser(femtoUser);
			logEvent(UserLogType.FEMTOUSER_LOGIN, Utils.format("登入人員:{0}", describe(femtoUser)));
			
		} catch (ServiceException e) {
			logEvent(UserLogType.FEMTOUSER_LOGIN_FAILED, Utils.format("帳號:{0}, 錯誤原因:{1}", account, e.getMessage()));
			addActionError("系統錯誤, 原因:{0}", e.getMessage());
			return ERROR;
		}

		if (femtoUser == null) {
			logEvent(UserLogType.FEMTOUSER_LOGIN_FAILED, Utils.format("帳號:{0}", account));
			addActionError("帳號密碼錯誤，請聯絡系統管理者");
			return ERROR;
		}

		return SUCCESS;
	}

	public String logoutFemtoUser() {
		FemtoUser femtoUser = getLoginFemtoUser();
		if (femtoUser != null) {
			logEvent(UserLogType.FEMTOUSER_LOGOUT, Utils.format("登出人員:{0}", describe(femtoUser)));
		}
		
		clearLoginFemtoUser();
		getSession().invalidate();
		if (isSsoLogin()) {
			return "ssoLogin";
		}
		return SUCCESS;
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
