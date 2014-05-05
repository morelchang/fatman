package tw.com.mds.fet.femtocellportal.translator.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SIGAPI_OUTPUT")
@XmlAccessorType(XmlAccessType.FIELD)
public class SgwA004XmlResponse extends FetDefaultTranslatorResponse {
	
	@XmlElement(name = "SUBSCRIBERID")
	private String subscriberId = "";
	@XmlElement(name = "ACCOUNTID")
	private String accountId = "";
	@XmlElement(name = "CUSTOMERID")
	private String customerId = "";
	@XmlElement(name = "PCNID")
	private String pcnId = "";
	@XmlElement(name = "ENTITY")
	private String entity;

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getPcnId() {
		return pcnId;
	}

	public void setPcnId(String pcnId) {
		this.pcnId = pcnId;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

}
