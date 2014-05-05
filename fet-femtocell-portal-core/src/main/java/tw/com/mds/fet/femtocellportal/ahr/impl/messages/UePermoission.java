package tw.com.mds.fet.femtocellportal.ahr.impl.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"imsi", "msisdn"})
public class UePermoission {

	@XmlElement(name = "IMSI", nillable = false)
	private String imsi;
	@XmlElement(name = "MSISDN")
	private String msisdn;

	public UePermoission() {
		super();
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	@Override
	public String toString() {
		return "UePermoission [imsi=" + imsi + ", msisdn=" + msisdn + "]";
	}

}
