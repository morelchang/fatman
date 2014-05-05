package tw.com.mds.fet.femtocellportal.core.impl;

import javax.persistence.Column;
import javax.persistence.Entity;

import tw.com.mds.fet.femtocellportal.core.Config;
import tw.com.mds.fet.femtocellportal.core.ConfigurationException;

@Entity
public class ExceptionHandlerConfig extends Config {

	private static final long serialVersionUID = 1L;

	private String ftpIpAddress;
	private String ftpLoginUser;
	private String ftpLoginPassword;
	private String ftpRemoteDir;
	private String callerListValue;
	private String errorCodeListValue;

	@Column(length = 20)
	public String getFtpIpAddress() {
		return ftpIpAddress;
	}

	public void setFtpIpAddress(String ftpIpAddress) {
		this.ftpIpAddress = ftpIpAddress;
	}

	@Column(length = 50)
	public String getFtpLoginUser() {
		return ftpLoginUser;
	}

	public void setFtpLoginUser(String ftpLoginUser) {
		this.ftpLoginUser = ftpLoginUser;
	}

	@Column(length = 50)
	public String getFtpLoginPassword() {
		return ftpLoginPassword;
	}

	public void setFtpLoginPassword(String ftpLoginPassword) {
		this.ftpLoginPassword = ftpLoginPassword;
	}

	@Column(length = 100)
	public String getFtpRemoteDir() {
		return ftpRemoteDir;
	}

	public void setFtpRemoteDir(String ftpRemoteDir) {
		this.ftpRemoteDir = ftpRemoteDir;
	}

	@Column(length = 1000)
	public String getCallerListValue() {
		return callerListValue;
	}

	public void setCallerListValue(String callerListValue) {
		this.callerListValue = callerListValue;
	}

	@Column(length = 1000)
	public String getErrorCodeListValue() {
		return errorCodeListValue;
	}

	public void setErrorCodeListValue(String errorCodeListValue) {
		this.errorCodeListValue = errorCodeListValue;
	}

	@Override
	public void validate() throws ConfigurationException {
		return;
	}

	@Override
	public String toString() {
		return "ExceptionHandlerConfig [ftpIpAddress=" + ftpIpAddress
				+ ", ftpLoginUser=" + ftpLoginUser + ", ftpRemoteDir="
				+ ftpRemoteDir + ", callerListValue=" + callerListValue
				+ ", errorCodeListValue=" + errorCodeListValue + "]";
	}

}
