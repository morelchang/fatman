package tw.com.mds.fet.femtocellportal.translator.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.core.Config;
import tw.com.mds.fet.femtocellportal.core.ConfigurationException;
import tw.com.mds.fet.femtocellportal.util.Utils;



@Entity
public class FetTranslatorConfig extends Config {

	private static final long serialVersionUID = 1L;

	// protocol setting
	private String xmlEncoding;
	private String sgwA004Url;
	private String sgwA015Url;
	// request identity setting
	private String userNameF;
	private String passwordF;
	private String userIdF;
	private String channelIdF;
	private String userNameK;
	private String passwordK;
	private String entity;
	// maintenance time setting
	private boolean enableMaintananceTimeCheck;
	private int maintenanceHourFrom;
	private int maintenanceHourTo;
	private String errorCodeMessageMappingValue;

	public FetTranslatorConfig() {
		super();
	}

	@Column(length = 50)
	public String getXmlEncoding() {
		return xmlEncoding;
	}

	public void setXmlEncoding(String xmlEncoding) {
		this.xmlEncoding = xmlEncoding;
	}

	@Column(length = 100)
	public String getSgwA004Url() {
		return sgwA004Url;
	}

	public void setSgwA004Url(String sgwA004Url) {
		this.sgwA004Url = sgwA004Url;
	}

	@Column(length = 100)
	public String getSgwA015Url() {
		return sgwA015Url;
	}

	public void setSgwA015Url(String sgwA015Url) {
		this.sgwA015Url = sgwA015Url;
	}

	@Column(length = 50)
	public String getUserNameF() {
		return userNameF;
	}

	public void setUserNameF(String userNameF) {
		this.userNameF = userNameF;
	}

	@Column(length = 50)
	public String getPasswordF() {
		return passwordF;
	}

	public void setPasswordF(String passwordF) {
		this.passwordF = passwordF;
	}

	@Column(length = 50)
	public String getUserIdF() {
		return userIdF;
	}

	public void setUserIdF(String userIdF) {
		this.userIdF = userIdF;
	}

	@Column(length = 50)
	public String getChannelIdF() {
		return channelIdF;
	}

	public void setChannelIdF(String channelIdF) {
		this.channelIdF = channelIdF;
	}

	@Column(length = 50)
	public String getUserNameK() {
		return userNameK;
	}

	public void setUserNameK(String userNameK) {
		this.userNameK = userNameK;
	}

	@Column(length = 50)
	public String getPasswordK() {
		return passwordK;
	}

	public void setPasswordK(String passwordK) {
		this.passwordK = passwordK;
	}

	@Column(length = 50)
	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	@Column
	public boolean isEnableMaintananceTimeCheck() {
		return enableMaintananceTimeCheck;
	}

	public void setEnableMaintananceTimeCheck(boolean enableMaintananceTimeCheck) {
		this.enableMaintananceTimeCheck = enableMaintananceTimeCheck;
	}

	@Column
	public int getMaintenanceHourFrom() {
		return maintenanceHourFrom;
	}

	public void setMaintenanceHourFrom(int maintenanceHourFrom) {
		this.maintenanceHourFrom = maintenanceHourFrom;
	}

	@Column
	public int getMaintenanceHourTo() {
		return maintenanceHourTo;
	}

	public void setMaintenanceHourTo(int maintenanceHourTo) {
		this.maintenanceHourTo = maintenanceHourTo;
	}

	@Column(length = 4000)
	public String getErrorCodeMessageMappingValue() {
		return errorCodeMessageMappingValue;
	}

	public void setErrorCodeMessageMappingValue(String errorCodeMessageMappingValue) {
		this.errorCodeMessageMappingValue = errorCodeMessageMappingValue;
	}

	public void validate() throws ConfigurationException {
		// xmlEncoding
		String xmlEncoding = getXmlEncoding();
		if (!Charset.isSupported(xmlEncoding)) {
			throw new ConfigurationException(Utils.format(
					"unsupported encoding:{0}", xmlEncoding));
		}
		
		validateUrl("SGWA004", getSgwA004Url());
		validateUrl("SGWA015", getSgwA015Url());
		
		validateMaintenanceHour(getMaintenanceHourFrom());
		validateMaintenanceHour(getMaintenanceHourTo());
	}

	private void validateMaintenanceHour(int hour) throws ConfigurationException {
		if (hour < 0 || hour > 23) {
			throw new ConfigurationException(Utils.format("incorrect maintenance hour value:{0}", hour));
		}
	}

	private void validateUrl(String urlName, String urlValue) throws ConfigurationException {
		if (StringUtils.isEmpty(urlValue)) {
			throw new ConfigurationException(urlName + " URL is not set");
		}
		try {
			new URL(urlValue);
		} catch (MalformedURLException e) {
			throw new ConfigurationException(Utils.format(
					"{0} incorrect format of URL:{1}", urlName, urlValue));
		}
	}

	@Override
	public String toString() {
		return "FetTranslatorConfig [xmlEncoding=" + xmlEncoding
				+ ", sgwA004Url=" + sgwA004Url + ", sgwA015Url=" + sgwA015Url
				+ ", userNameF=" + userNameF + ", passwordF=" + passwordF
				+ ", userIdF=" + userIdF + ", channelIdF=" + channelIdF
				+ ", userNameK=" + userNameK + ", passwordK=" + passwordK
				+ ", entity=" + entity + ", enableMaintananceTimeCheck="
				+ enableMaintananceTimeCheck + ", maintenanceHourFrom="
				+ maintenanceHourFrom + ", maintenanceHourTo="
				+ maintenanceHourTo + ", errorCodeMessageMappingValue="
				+ errorCodeMessageMappingValue + "]";
	}

}
