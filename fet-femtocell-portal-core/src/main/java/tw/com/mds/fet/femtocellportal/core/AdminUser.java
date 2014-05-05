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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.annotations.Fetch;

import tw.com.mds.fet.femtocellportal.dao.PersistentObject;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"userId"}))
public class AdminUser extends PersistentObject implements LoginUser, Modifiable {

	private static final long serialVersionUID = 1L;

	private String userId;
	private String userName;
	private String account;
	private String password;
	private String email;
	private List<Permission> permissions = new ArrayList<Permission>();
	private String sourceIp;
	private Date createTime;
	private Date updateTime;

	public AdminUser() {
		super();
	}

	public AdminUser(String userId, String account) {
		super();
		this.userId = userId;
		this.account = account;
	}

	@Column(length = 50, nullable = false)
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

	@Column(length = 50, nullable = false)
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(length = 100)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(length = 100)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Permission getPermission(String resourceId) {
		for (Permission p : getPermissions()) {
			if (p.getResourceId().equals(resourceId)) {
				return p;
			}
		}
		return null;
	}

	@OrderBy("resourceId")
	@OneToMany(mappedBy = "adminUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
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
		setPassword(Base64.encodeBase64String(getPassword().getBytes()));
	}
	
	@PostLoad
	@PostPersist
	@PostUpdate
	public void decryptPrivacy() {
		setPassword(new String(Base64.decodeBase64(getPassword())));
	}

	@Override
	public String toString() {
		return "AdminUser [userId=" + userId + ", userName=" + userName
				+ ", account=" + account + ", email=" + email
				+ ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}

}
