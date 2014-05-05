package tw.com.mds.fet.femtocellportal.core.impl;

import javax.persistence.Column;
import javax.persistence.Entity;

import tw.com.mds.fet.femtocellportal.core.Config;
import tw.com.mds.fet.femtocellportal.core.ConfigurationException;

@Entity
public class ProvisionConfig extends Config {

	private static final long serialVersionUID = 1L;

	private Integer defaultMaxUserEquipmentSize;
	private Boolean enableSsoAuthentication;
	private String ssoLoginUrl;
	private String ssoLogoutUrl;
	private String ssoMsisdnHeaderKey;
	private Boolean enableLocalAuthentication;
	private Boolean defaultListAll;
	
	@Override
	public void validate() throws ConfigurationException {
		return;
	}

	@Column
	public Integer getDefaultMaxUserEquipmentSize() {
		return defaultMaxUserEquipmentSize;
	}

	public void setDefaultMaxUserEquipmentSize(Integer defaultMaxUserEquipmentSize) {
		this.defaultMaxUserEquipmentSize = defaultMaxUserEquipmentSize;
	}

	@Column
	public Boolean getEnableSsoAuthentication() {
		return enableSsoAuthentication;
	}

	public void setEnableSsoAuthentication(Boolean enableSsoAuthentication) {
		this.enableSsoAuthentication = enableSsoAuthentication;
	}

	@Column(length = 250)
	public String getSsoLoginUrl() {
		return ssoLoginUrl;
	}

	public void setSsoLoginUrl(String ssoLoginUrl) {
		this.ssoLoginUrl = ssoLoginUrl;
	}

	@Column(length = 250)
	public String getSsoLogoutUrl() {
		return ssoLogoutUrl;
	}

	public void setSsoLogoutUrl(String ssoLogoutUrl) {
		this.ssoLogoutUrl = ssoLogoutUrl;
	}

	@Column(length = 100)
	public String getSsoMsisdnHeaderKey() {
		return ssoMsisdnHeaderKey;
	}

	public void setSsoMsisdnHeaderKey(String ssoMsisdnHeaderKey) {
		this.ssoMsisdnHeaderKey = ssoMsisdnHeaderKey;
	}

	@Column
	public Boolean getEnableLocalAuthentication() {
		return enableLocalAuthentication;
	}

	public void setEnableLocalAuthentication(Boolean enableLocalAuthentication) {
		this.enableLocalAuthentication = enableLocalAuthentication;
	}

	@Column
	public Boolean getDefaultListAll() {
		return defaultListAll;
	}

	public void setDefaultListAll(Boolean defaultListAll) {
		this.defaultListAll = defaultListAll;
	}

	@Override
	public String toString() {
		return "ProvisionConfig [defaultMaxUserEquipmentSize="
				+ defaultMaxUserEquipmentSize + ", enableSsoAuthentication="
				+ enableSsoAuthentication + ", ssoLoginUrl=" + ssoLoginUrl
				+ ", ssoLogoutUrl=" + ssoLogoutUrl + ", ssoMsisdnHeaderKey="
				+ ssoMsisdnHeaderKey + ", enableLocalAuthentication="
				+ enableLocalAuthentication + ", defaultListAll="
				+ defaultListAll + "]";
	}

}
