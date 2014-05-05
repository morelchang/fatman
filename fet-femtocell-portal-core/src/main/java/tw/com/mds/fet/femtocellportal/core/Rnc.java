package tw.com.mds.fet.femtocellportal.core;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import tw.com.mds.fet.femtocellportal.dao.PersistentObject;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "rncId" }))
public class Rnc extends PersistentObject implements Modifiable {

	private static final long serialVersionUID = 1L;

	private String rncId;
	private String rncName;
	private Date createTime;
	private Date updateTime;

	public Rnc() {
		super();
	}

	public Rnc(String rncId, String rncName) {
		this.rncId = rncId;
		this.rncName = rncName;
	}

	@Column(length = 10, nullable = false)
	public String getRncId() {
		return rncId;
	}

	public void setRncId(String rncId) {
		this.rncId = rncId;
	}

	@Column(length = 50)
	public String getRncName() {
		return rncName;
	}

	public void setRncName(String rncName) {
		this.rncName = rncName;
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

	@Override
	public String toString() {
		return "Rnc [rncId=" + rncId + ", rncName=" + rncName + ", createTime="
				+ createTime + ", updateTime=" + updateTime + "]";
	}

}
