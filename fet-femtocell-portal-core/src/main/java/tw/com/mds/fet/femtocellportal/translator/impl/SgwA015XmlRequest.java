package tw.com.mds.fet.femtocellportal.translator.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "SIGAPI_INPUT")
@XmlAccessorType(XmlAccessType.FIELD)
public class SgwA015XmlRequest extends FetDefaultTranslatorRequest {

	@XmlElement(name = "SUBSCRIBERID")
	private String subscriberId = "";

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	@Override
	public String getActionName() {
		return "SGW_A015";
	}

	@Override
	public String toString() {
		return "SgwA015XmlRequest [subscriberId=" + subscriberId
				+ ", userNameF=" + userNameF + ", passwordF=" + passwordF
				+ ", userIdF=" + userIdF + ", channelIdF=" + channelIdF
				+ ", userNameK=" + userNameK + ", passwordK=" + passwordK + "]";
	}
	
}
