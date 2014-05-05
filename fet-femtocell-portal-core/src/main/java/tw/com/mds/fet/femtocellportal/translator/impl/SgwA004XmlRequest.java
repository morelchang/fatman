package tw.com.mds.fet.femtocellportal.translator.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SIGAPI_INPUT")
@XmlAccessorType(XmlAccessType.FIELD)
public class SgwA004XmlRequest extends FetDefaultTranslatorRequest {

	@XmlElement(name = "MSISDN")
	private String msisdn = "";

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	@Override
	public String getActionName() {
		return "SGW_A004";
	}

	@Override
	public String toString() {
		return "SgwA004XmlRequest [msisdn=" + msisdn + ", userNameF="
				+ userNameF + ", passwordF=" + passwordF + ", userIdF="
				+ userIdF + ", channelIdF=" + channelIdF + ", userNameK="
				+ userNameK + ", passwordK=" + passwordK + "]";
	}
	
}
