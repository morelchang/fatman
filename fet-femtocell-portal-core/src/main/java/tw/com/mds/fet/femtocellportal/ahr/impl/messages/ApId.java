package tw.com.mds.fet.femtocellportal.ahr.impl.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"apei", "imsi"})
public class ApId {

	@XmlElement(name = "APEI")
	private String apei;
	@XmlElement(name = "IMSI", nillable = false)
	private String imsi;

	public ApId() {
		super();
	}

	public String getApei() {
		return apei;
	}

	public void setApei(String apei) {
		this.apei = apei;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	@Override
	public String toString() {
		return "ApId [apei=" + apei + ", imsi=" + imsi + "]";
	}

}
