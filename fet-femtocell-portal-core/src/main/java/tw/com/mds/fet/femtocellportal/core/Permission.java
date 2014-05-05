package tw.com.mds.fet.femtocellportal.core;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import tw.com.mds.fet.femtocellportal.dao.PersistentObject;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "adminUser_Oid", "resourceId" }))
public class Permission extends PersistentObject {

	private static final long serialVersionUID = 1L;

	private AdminUser adminUser;
	private String resourceId;
	private boolean enabled;
	private Date createTime;

	public Permission() {
		super();
	}

	public Permission(AdminUser au, String id, boolean b, Date date) {
		this.adminUser = au;
		this.resourceId = id;
		this.enabled = b;
		this.createTime = date;
	}

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	public AdminUser getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(AdminUser adminUser) {
		this.adminUser = adminUser;
	}

	@Column(nullable = false, length = 100)
	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	@Transient
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Permission [resourceId="
				+ resourceId + ", createTime=" + createTime + "]";
	}


}
