package tw.com.mds.fet.femtocellportal.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MaskedFemtoUserWrapper {

	private FemtoUser delegatee;
	private PrivacyMasker privacyMasker = new PrivacyMasker();

	public MaskedFemtoUserWrapper() {
		super();
	}

	public FemtoUser getDelegatee() {
		return delegatee;
	}

	public void setDelegatee(FemtoUser delegatee) {
		this.delegatee = delegatee;
	}

	public MaskedFemtoUserWrapper(FemtoUser delegatee) {
		super();
		this.delegatee = delegatee;
	}
	
	public static MaskedFemtoUserWrapper wrap(FemtoUser user) {
		return new MaskedFemtoUserWrapper(user);
	}
	
	public static List<MaskedFemtoUserWrapper> wrapList(List<FemtoUser> list) {
		List<MaskedFemtoUserWrapper> result = new ArrayList<MaskedFemtoUserWrapper>();
		for (FemtoUser u : list) {
			result.add(new MaskedFemtoUserWrapper(u));
		}
		return result;
	}
	
	public static List<FemtoUser> unwrap(List<MaskedFemtoUserWrapper> list) {
		List<FemtoUser> result = new ArrayList<FemtoUser>();
		for (MaskedFemtoUserWrapper m : list) {
			result.add(m.getDelegatee());
		}
		return result;
	}

	public String getUserName() {
		if (delegatee == null) {
			return null;
		}
		if (delegatee.getOid() == null) {
			return delegatee.getUserName();
		}
		return privacyMasker.maskUserName(delegatee.getUserName());
	}

	public void setUserName(String userName) {
		if (delegatee == null) {
			return;
		}
		if (userName != null && userName.equals(getUserName())) {
			return;
		}
		delegatee.setUserName(userName);
	}

	public String getMobile() {
		return delegatee.getMobile();
	}

	public void setMobile(String mobile) {
		delegatee.setMobile(mobile);
	}

	public Long getOid() {
		return delegatee.getOid();
	}

	public void setOid(Long oid) {
		delegatee.setOid(oid);
	}

	public String getAccount() {
		return delegatee.getAccount();
	}

	public void setAccount(String account) {
		delegatee.setAccount(account);
	}

	public String getPassword() {
		return delegatee.getPassword();
	}

	public void setPassword(String password) {
		delegatee.setPassword(password);
	}

	public String getCardId() {
		return delegatee.getCardId();
	}

	public void setCardId(String cardId) {
		delegatee.setCardId(cardId);
	}

	public String getZipCode() {
		return delegatee.getZipCode();
	}

	public void setZipCode(String zipCode) {
		delegatee.setZipCode(zipCode);
	}

	public String getPhone() {
		return delegatee.getPhone();
	}

	public void setPhone(String phone) {
		delegatee.setPhone(phone);
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

	public List<MaskedFemtoProfileWrapper> getProfiles() {
		return MaskedFemtoProfileWrapper.wrapList(delegatee.getProfiles());
	}

	public void setProfiles(List<MaskedFemtoProfileWrapper> profiles) {
		delegatee.setProfiles(MaskedFemtoProfileWrapper.unwrap(profiles));
	}

}
