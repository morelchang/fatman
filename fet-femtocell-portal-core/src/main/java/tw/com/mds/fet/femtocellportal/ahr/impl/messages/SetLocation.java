package tw.com.mds.fet.femtocellportal.ahr.impl.messages;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "interfaceVersion", "serialNo", "apIdentity",
		"locationInfoList" })
public class SetLocation {

	@XmlElement(name = "interfaceVersion", nillable = false)
	private String interfaceVersion;
	@XmlElement(name = "serialNo", nillable = false)
	private int serialNo;
	@XmlElement(name = "APIdentity", nillable = false)
	private ApId apIdentity;
	@XmlElementWrapper(name = "locationInfoList")
	@XmlElement(name = "locationInfo", nillable = false)
	private List<LocationInfo> locationInfoList = new ArrayList<LocationInfo>();

	public SetLocation() {
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

	public ApId getApIdentity() {
		return apIdentity;
	}

	public void setApIdentity(ApId apIdentity) {
		this.apIdentity = apIdentity;
	}

	public List<LocationInfo> getLocationInfoList() {
		return locationInfoList;
	}

	public void setLocationInfoList(List<LocationInfo> locationInfoList) {
		this.locationInfoList = locationInfoList;
	}

	@Override
	public String toString() {
		return "SetLocation [interfaceVersion=" + interfaceVersion
				+ ", serialNo=" + serialNo + ", apIdentity=" + apIdentity
				+ ", locationInfoList=" + locationInfoList + "]";
	}

}
