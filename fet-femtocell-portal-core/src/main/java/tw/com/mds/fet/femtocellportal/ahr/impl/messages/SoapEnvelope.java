package tw.com.mds.fet.femtocellportal.ahr.impl.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Envelope", namespace = "http://www.w3.org/2001/06/soap-envelope")
public class SoapEnvelope {

	@XmlElement(name = "Body", namespace = "http://www.w3.org/2001/06/soap-envelope", nillable = false)
	private SoapBody body;

	public SoapEnvelope() {
		super();
	}

	public SoapBody getBody() {
		return body;
	}

	public void setBody(SoapBody body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "SoapEnvelope [body=" + body + "]";
	}

}
