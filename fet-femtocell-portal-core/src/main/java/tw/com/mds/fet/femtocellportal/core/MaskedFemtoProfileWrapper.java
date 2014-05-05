package tw.com.mds.fet.femtocellportal.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MaskedFemtoProfileWrapper {

	private FemtoProfile delegatee;
	private PrivacyMasker privacyMasker = new PrivacyMasker();

	public MaskedFemtoProfileWrapper() {
		super();
	}

	public MaskedFemtoProfileWrapper(FemtoProfile delegatee) {
		super();
		this.delegatee = delegatee;
	}

	public static MaskedFemtoProfileWrapper wrap(FemtoProfile user) {
		return new MaskedFemtoProfileWrapper(user);
	}
	
	public static List<MaskedFemtoProfileWrapper> wrapList(List<FemtoProfile> list) {
		List<MaskedFemtoProfileWrapper> result = new ArrayList<MaskedFemtoProfileWrapper>();
		for (FemtoProfile u : list) {
			result.add(new MaskedFemtoProfileWrapper(u));
		}
		return result;
	}
	
	public static List<FemtoProfile> unwrap(List<MaskedFemtoProfileWrapper> list) {
		List<FemtoProfile> result = new ArrayList<FemtoProfile>();
		for (MaskedFemtoProfileWrapper m : list) {
			result.add(m.getDelegatee());
		}
		return result;
	}

	public String getAddress() {
		if (delegatee == null) {
			return null;
		}
		if (delegatee.getOid() == null) {
			return delegatee.getAddress();
		}
		return privacyMasker.maskAddress(delegatee.getAddress());
	}

	public void setAddress(String address) {
		if (delegatee == null) {
			return;
		}
		if (address != null && address.equals(getAddress())) {
			return;
		}
		delegatee.setAddress(address);
	}

	public FemtoProfile getDelegatee() {
		return delegatee;
	}

	public void setDelegatee(FemtoProfile delegatee) {
		this.delegatee = delegatee;
	}

	public Long getOid() {
		return delegatee.getOid();
	}

	public void setOid(Long oid) {
		delegatee.setOid(oid);
	}

	public String getApei() {
		return delegatee.getApei();
	}

	public void setApei(String apei) {
		delegatee.setApei(apei);
	}

	public String getImsi() {
		return delegatee.getImsi();
	}

	public void setImsi(String imsi) {
		delegatee.setImsi(imsi);
	}

	public Position getPosition() {
		return delegatee.getPosition();
	}

	public void setPosition(Position position) {
		delegatee.setPosition(position);
	}

	public LocationDetectMode getLocationDetectMode() {
		return delegatee.getLocationDetectMode();
	}

	public void setLocationDetectMode(LocationDetectMode locationDetectMode) {
		delegatee.setLocationDetectMode(locationDetectMode);
	}

	public UePermissionMode getUePermissionMode() {
		return delegatee.getUePermissionMode();
	}

	public void setUePermissionMode(UePermissionMode uePermissionMode) {
		delegatee.setUePermissionMode(uePermissionMode);
	}

	public int getMaxUserCount() {
		return delegatee.getMaxUserCount();
	}

	public void setMaxUserCount(int maxUserCount) {
		delegatee.setMaxUserCount(maxUserCount);
	}

	public FemtoUser getUser() {
		return delegatee.getUser();
	}

	public void setUser(FemtoUser user) {
		delegatee.setUser(user);
	}

	public List<Cell> getCells() {
		return delegatee.getCells();
	}

	public void setCells(List<Cell> cells) {
		delegatee.setCells(cells);
	}

	public void addPermissionList(UserEquipment ue) {
		delegatee.addPermissionList(ue);
	}

	public List<UserEquipment> getPermissionList() {
		return delegatee.getPermissionList();
	}

	public void setPermissionList(List<UserEquipment> permissionList) {
		delegatee.setPermissionList(permissionList);
	}

	public ApZone getApZone() {
		return delegatee.getApZone();
	}

	public void setApZone(ApZone apZone) {
		delegatee.setApZone(apZone);
	}

	public int getMaxPermissionListSize() {
		return delegatee.getMaxPermissionListSize();
	}

	public void setMaxPermissionListSize(int maxPermissionListSize) {
		delegatee.setMaxPermissionListSize(maxPermissionListSize);
	}

	public ConnectionMode getConnectionMode() {
		return delegatee.getConnectionMode();
	}

	public void setConnectionMode(ConnectionMode connectionMode) {
		delegatee.setConnectionMode(connectionMode);
	}

	public String getStaticIp() {
		return delegatee.getStaticIp();
	}

	public void setStaticIp(String staticIp) {
		delegatee.setStaticIp(staticIp);
	}

	public FemtoState getState() {
		return delegatee.getState();
	}

	public void setState(FemtoState state) {
		delegatee.setState(state);
	}

	public Date getCreateTime() {
		return delegatee.getCreateTime();
	}

	public void setCreateTime(Date createTime) {
		delegatee.setCreateTime(createTime);
	}

	public Date getUpdateTime() {
		return delegatee.getUpdateTime();
	}

	public void setUpdateTime(Date updateTime) {
		delegatee.setUpdateTime(updateTime);
	}

}
