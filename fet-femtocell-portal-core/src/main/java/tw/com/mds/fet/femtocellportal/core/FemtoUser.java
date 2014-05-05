package tw.com.mds.fet.femtocellportal.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.annotations.Fetch;

import tw.com.mds.fet.femtocellportal.dao.PersistentObject;

@Entity
public class FemtoUser extends PersistentObject implements LoginUser, Modifiable {

	private static final long serialVersionUID = 1L;

	private String userId;
	private String userName;
	private String account;
	private String password;
	private String cardId;
	private String zipCode;
	private String phone;
	private String mobile;
	private List<FemtoProfile> profiles = new ArrayList<FemtoProfile>();
	private Date createTime;
	private Date updateTime;
	private String sourceIp;

	public FemtoUser() {
		super();
	}

	@Transient
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(length = 50)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(length = 50)
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(length = 50)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(length = 32)
	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	@Column(length = 10)
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Column(length = 16)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(length = 16, unique = true)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	@OrderBy("createTime")
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	public List<FemtoProfile> getProfiles() {
		return profiles;
	}
	
	public void setProfiles(List<FemtoProfile> profiles) {
		this.profiles = profiles;
	}

	@Transient
	public String getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

	@PrePersist
	@PreUpdate
	public void encryptPrivacy() {
		setUserName(Base64.encodeBase64String(getUserName().getBytes()));
	}
	
	@PostLoad
	@PostPersist
	@PostUpdate
	public void decryptPrivacy() {
		setUserName(new String(Base64.decodeBase64(getUserName())));
	}
	
	@Override
	public String toString() {
		PrivacyMasker m = new PrivacyMasker();
		return "FemtoUser [userName=" + m.maskUserName(userName)
				+ ", account=" + account + ", cardId=" + cardId + ", zipCode="
				+ zipCode + ", phone=" + phone + ", mobile=" + mobile
				+ ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}

}
