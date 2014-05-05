package tw.com.mds.fet.femtocellportal.translator.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


@XmlAccessorType(XmlAccessType.FIELD)
public abstract class FetDefaultTranslatorRequest {

	@XmlElement(name = "USER_NAMEF")
	protected String userNameF = "";
	@XmlElement(name = "PASSWORDF")
	protected String passwordF = "";
	@XmlElement(name = "USER_IDF")
	protected String userIdF = "";
	@XmlElement(name = "CHANNEL_IDF")
	protected String channelIdF = "";
	@XmlElement(name = "USER_NAMEK")
	protected String userNameK = "";
	@XmlElement(name = "PASSWORDK")
	protected String passwordK = "";
	@XmlElement(name = "ENTITY")
	private String entity = "";
	
	public FetDefaultTranslatorRequest() {
		super();
	}

	public String getUserNameF() {
		return userNameF;
	}

	public void setUserNameF(String userNameF) {
		this.userNameF = userNameF;
	}

	public String getPasswordF() {
		return passwordF;
	}

	public void setPasswordF(String passwordF) {
		this.passwordF = passwordF;
	}

	public String getUserIdF() {
		return userIdF;
	}

	public void setUserIdF(String userIdF) {
		this.userIdF = userIdF;
	}

	public String getChannelIdF() {
		return channelIdF;
	}

	public void setChannelIdF(String channelIdF) {
		this.channelIdF = channelIdF;
	}

	public String getUserNameK() {
		return userNameK;
	}

	public void setUserNameK(String userNameK) {
		this.userNameK = userNameK;
	}

	public String getPasswordK() {
		return passwordK;
	}

	public void setPasswordK(String passwordK) {
		this.passwordK = passwordK;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public abstract String getActionName();

}