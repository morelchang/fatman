package tw.com.mds.fet.femtocellportal.ahr.impl.messages;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "permissionMode", "maxUeUserCount", "uePermissionList" })
public class QueryPermissionListResponse extends Response {

	@XmlElement(name = "permissionMode", nillable = false)
	private int permissionMode;
	@XmlElement(name = "maxUEUserCount", nillable = false)
	private int maxUeUserCount;
	@XmlElementWrapper(name = "UEPermissionList", nillable = false)
	@XmlElement(name = "UEInfo")
	private List<UePermoission> uePermissionList = new ArrayList<UePermoission>();

	public QueryPermissionListResponse() {
		super();
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

	public List<UePermoission> getUePermissionList() {
		return uePermissionList;
	}

	public void setUePermissionList(List<UePermoission> uePermissionList) {
		this.uePermissionList = uePermissionList;
	}

	@Override
	public String toString() {
		return "QueryPermissionListResponse [permissionMode=" + permissionMode
				+ ", maxUeUserCount=" + maxUeUserCount + ", uePermissionList="
				+ uePermissionList + "]";
	}

}
