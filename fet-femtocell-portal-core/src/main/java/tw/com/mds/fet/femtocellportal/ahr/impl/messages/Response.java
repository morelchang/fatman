package tw.com.mds.fet.femtocellportal.ahr.impl.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "interfaceVersion", "serialNo", "returnCode",
		"apIdentity" })
public class Response {

	@XmlElement(name = "interfaceVersion", nillable = false)
	private String interfaceVersion;
	@XmlElement(name = "serialNo", nillable = false)
	private int serialNo;
	@XmlElement(name = "returnCode", nillable = false)
	private int returnCode;
	@XmlElement(name="APIdentity", nillable = false)
	private ApId apIdentity;

	public Response() {
		super();
	}

	public String getInterfaceVersion() {
		return interfaceVersion;
	}

	public void setInterfaceVersion(String interfaceVersion) {
		this.interfaceVersion = interfaceVersion;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public ApId getApIdentity() {
		return apIdentity;
	}

	public void setApIdentity(ApId apIdentity) {
		this.apIdentity = apIdentity;
	}

	@Override
	public String toString() {
		return "Response [interfaceVersion=" + interfaceVersion + ", serialNo="
				+ serialNo + ", returnCode=" + returnCode + ", apIdentity="
				+ apIdentity + "]";
	}

}
