package tw.com.mds.fet.femtocellportal.ahr.impl.messages;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"interfaceVersion", "serialNo", "apIdentity", "permissionMode", "maxUeUserCount", "uePermissionListSize", "time", "uePermissionList"})
public class ModifyUeList {

	@XmlElement(name = "interfaceVersion", nillable = false)
	private String interfaceVersion;
	@XmlElement(name = "serialNo", nillable = false)
	private int serialNo;
	@XmlElement(name = "APIdentity", nillable = false)
	private ApId apIdentity;
	@XmlElement(name = "permissionMode", nillable = false)
	private int permissionMode;
	@XmlElement(name = "maxUEUserCount", nillable = false)
	private int maxUeUserCount;
	@XmlElement(name = "UEPermissionListSize", nillable = false)
	private int uePermissionListSize;
	@XmlElement(name = "time", nillable = false)
	private String time;
	@XmlElementWrapper(name = "UEPermissionList", nillable = false)
	@XmlElement(name = "UEPermission")
	private List<UePermoission> uePermissionList = new ArrayList<UePermoission>();

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

	public int getPermissionMode() {
		return permissionMode;
	}

	public void setPermissionMode(int permissionMode) {
		this.permissionMode = permissionMode;
	}

	public int getMaxUeUserCount() {
		return maxUeUserCount;
	}

	public void setMaxUeUserCount(int maxUeUserCount) {
		this.maxUeUserCount = maxUeUserCount;
	}

	public int getUePermissionListSize() {
		return uePermissionListSize;
	}

	public void setUePermissionListSize(int uePermissionListSize) {
		this.uePermissionListSize = uePermissionListSize;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<UePermoission> getUePermissionList() {
		return uePermissionList;
	}

	public void setUePermissionList(List<UePermoission> uePermissionList) {
		this.uePermissionList = uePermissionList;
	}

}
