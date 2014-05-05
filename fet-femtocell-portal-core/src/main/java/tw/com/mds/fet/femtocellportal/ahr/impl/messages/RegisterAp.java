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
		"permissionMode", "locationDetectMode", "maxUeUserCount",
		"uePermissionList", "phone", "cardId", "zipCode", "userName",
		"apZoneName", "mobile" })
public class RegisterAp {

	@XmlElement(name = "interfaceVersion", nillable = false)
	private String interfaceVersion;
	@XmlElement(name = "serialNo", nillable = false)
	private int serialNo;
	@XmlElement(name = "APIdentity", nillable = false)
	private ApId apIdentity;
	@XmlElement(name = "permissionMode", nillable = false)
	private int permissionMode;
	@XmlElement(name = "locationDetectMode")
	private int locationDetectMode;
	@XmlElement(name = "maxUEUserCount", nillable = false)
	private int maxUeUserCount;
	@XmlElementWrapper(name = "UEPermissionList", nillable = false)
	@XmlElement(name = "UEPermission")
	private List<UePermoission> uePermissionList = new ArrayList<UePermoission>();
	@XmlElement(name = "phone")
	private String phone;
	@XmlElement(name = "cardID")
	private String cardId;
	@XmlElement(name = "zipCode")
	private String zipCode;
	@XmlElement(name = "userName")
	private String userName;
	@XmlElement(name = "APZoneName", nillable = false)
	private String apZoneName;
	@XmlElement(name = "mobile")
	private String mobile;

	public RegisterAp() {
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

	public int getPermissionMode() {
		return permissionMode;
	}

	public void setPermissionMode(int permissionMode) {
		this.permissionMode = permissionMode;
	}

	public int getLocationDetectMode() {
		return locationDetectMode;
	}

	public void setLocationDetectMode(int locationDetectMode) {
		this.locationDetectMode = locationDetectMode;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getApZoneName() {
		return apZoneName;
	}

	public void setApZoneName(String apZoneName) {
		this.apZoneName = apZoneName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		return "RegisterAp [interfaceVersion=" + interfaceVersion
				+ ", serialNo=" + serialNo + ", apIdentity=" + apIdentity
				+ ", permissionMode=" + permissionMode
				+ ", locationDetectMode=" + locationDetectMode
				+ ", maxUeUserCount=" + maxUeUserCount + ", uePermission="
				+ uePermissionList + ", phone=" + phone + ", cardId=" + cardId
				+ ", zipCode=" + zipCode + ", userName=" + userName
				+ ", apZoneName=" + apZoneName + ", mobile=" + mobile + "]";
	}
}
