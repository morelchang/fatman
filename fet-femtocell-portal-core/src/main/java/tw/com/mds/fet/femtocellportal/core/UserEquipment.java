package tw.com.mds.fet.femtocellportal.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import tw.com.mds.fet.femtocellportal.dao.PersistentObject;

@Entity
public class UserEquipment extends PersistentObject {

	private static final long serialVersionUID = 1L;
	
	private String msisdn;
	private String imsi;
	private FemtoProfile profile;

	public UserEquipment() {
		super();
	}

	public UserEquipment(String msisdn, String imsi) {
		this.msisdn = msisdn;
		this.imsi = imsi;
	}

	@Column(length = 15, nullable = false)
	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	@Column(length = 15, nullable = false)
	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public FemtoProfile getProfile() {
		return profile;
	}

	public void setProfile(FemtoProfile profile) {
		this.profile = profile;
	}

	public UserEquipment copyTo(FemtoProfile profile) {
		UserEquipment result = new UserEquipment(getMsisdn(), getImsi());
		result.setProfile(profile);
		profile.getPermissionList().add(result);
		return result;
	}

	@Override
	public String toString() {
		return "UserEquipment [msisdn=" + msisdn + ", imsi=" + imsi + "]";
	}

}
