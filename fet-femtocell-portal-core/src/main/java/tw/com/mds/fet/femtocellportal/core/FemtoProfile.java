package tw.com.mds.fet.femtocellportal.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.annotations.Fetch;

import tw.com.mds.fet.femtocellportal.dao.PersistentObject;
import tw.com.mds.fet.femtocellportal.util.Utils;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "apei", "imsi" }))
//@org.hibernate.annotations.Table(appliesTo = "FemtoProfile", indexes = @Index(name = "idx_aa", columnNames = {
//		"apei", "imsi" }))
public class FemtoProfile extends PersistentObject implements Modifiable {

	private static final long serialVersionUID = 1L;

	private String apei;
	private String imsi;
	private String address;
	private Position position;
	private LocatingState locatingState;
	private LocationDetectMode locationDetectMode;
	private UePermissionMode uePermissionMode;
	private int maxPermissionListSize;
	private int maxUserCount;
	private FemtoUser user;
	private List<Cell> cells = new ArrayList<Cell>();
	private List<UserEquipment> permissionList = new ArrayList<UserEquipment>();
	private ApZone apZone;
	private ConnectionMode connectionMode;
	private String staticIp;
	private FemtoState state;
	private Date createTime;
	private Date updateTime;

	@Column(length = 21, nullable = false)
	public String getApei() {
		return apei;
	}

	public void setApei(String apei) {
		this.apei = apei;
	}

	@Column(length = 15, nullable = false)
	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	@Column(length = 200, nullable = false)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Embedded
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@Enumerated
	public LocatingState getLocatingState() {
		return locatingState;
	}

	public void setLocatingState(LocatingState locatingState) {
		this.locatingState = locatingState;
	}

	@Enumerated
	public LocationDetectMode getLocationDetectMode() {
		return locationDetectMode;
	}

	public void setLocationDetectMode(LocationDetectMode locationDetectMode) {
		this.locationDetectMode = locationDetectMode;
	}

	@Enumerated
	@Column(nullable = false)
	public UePermissionMode getUePermissionMode() {
		return uePermissionMode;
	}

	public void setUePermissionMode(UePermissionMode uePermissionMode) {
		this.uePermissionMode = uePermissionMode;
	}

	@Column
	public int getMaxPermissionListSize() {
		return maxPermissionListSize;
	}

	public void setMaxPermissionListSize(int maxPermissionListSize) {
		this.maxPermissionListSize = maxPermissionListSize;
	}

	@Column(nullable = false)
	public int getMaxUserCount() {
		return maxUserCount;
	}

	public void setMaxUserCount(int maxUserCount) {
		this.maxUserCount = maxUserCount;
	}

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	public FemtoUser getUser() {
		return user;
	}

	public void setUser(FemtoUser user) {
		this.user = user;
	}

	@OrderBy("cellId")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "FemtoProfileCell")
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	public List<Cell> getCells() {
		return cells;
	}

	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}
	
	public void addPermissionList(UserEquipment ue) {
		ue.setProfile(this);
		getPermissionList().add(ue);
	}

	@OrderBy("msisdn")
	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	public List<UserEquipment> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<UserEquipment> permissionList) {
		this.permissionList = permissionList;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public ApZone getApZone() {
		return apZone;
	}

	public void setApZone(ApZone apZone) {
		this.apZone = apZone;
	}

	@Enumerated
	public ConnectionMode getConnectionMode() {
		return connectionMode;
	}

	public void setConnectionMode(ConnectionMode connectionMode) {
		this.connectionMode = connectionMode;
	}

	@Column(length = 25)
	public String getStaticIp() {
		return staticIp;
	}

	public void setStaticIp(String staticIp) {
		this.staticIp = staticIp;
	}

	@Enumerated
	@Column(nullable = false)
	public FemtoState getState() {
		return state;
	}

	public void setState(FemtoState state) {
		this.state = state;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void copyPermissionList(FemtoProfile profile) {
		if (profile == null) {
			throw new IllegalArgumentException("argument profile cant be null");
		}
		if (profile.getPermissionList() == null) {
			throw  new IllegalArgumentException("argument profile.permissionList cant be null");
		}
		if (profile.permissionList.size() > getMaxPermissionListSize()) {
			throw new IllegalArgumentException(Utils.format(
					"specified permission list size:{0} exceeds current maxPermissionListSize:{1}", 
					profile.getPermissionList().size(), getMaxPermissionListSize()));
		}
		
		getPermissionList().clear();
		for (UserEquipment ue : profile.getPermissionList()) {
			ue.copyTo(this);
		}
	}

	@Transient
	public boolean isLackingPermissionList() {
		return getUePermissionMode() == UePermissionMode.CLOSE && 
				getPermissionList().isEmpty();
	}

	@Transient
	public boolean isOverMaxPermissionListSize() {
		return getUePermissionMode() == UePermissionMode.CLOSE && 
				getPermissionList().size() > getMaxPermissionListSize();
	}

	@PrePersist
	@PreUpdate
	public void encryptPrivacy() {
		setAddress(Base64.encodeBase64String(getAddress().getBytes()));
	}
	
	@PostLoad
	@PostPersist
	@PostUpdate
	public void decryptPrivacy() {
		setAddress(new String(Base64.decodeBase64(getAddress())));
	}
	
	@Override
	public String toString() {
		PrivacyMasker m = new PrivacyMasker();
		return "FemtoProfile [apei=" + apei + ", imsi=" + imsi + ", address="
				+ m.maskAddress(address) + ", position=" + position
				+ ", locationDetectMode=" + locationDetectMode
				+ ", uePermissionMode=" + uePermissionMode
				+ ", maxPermissionListSize=" + maxPermissionListSize
				+ ", maxUserCount=" + maxUserCount + ", cells=" + cells
				+ ", permissionList=" + permissionList + ", apZone=" + apZone
				+ ", connectionMode=" + connectionMode + ", staticIp="
				+ staticIp + ", createTime=" + createTime + ", updateTime="
				+ updateTime + "]";
	}

}
