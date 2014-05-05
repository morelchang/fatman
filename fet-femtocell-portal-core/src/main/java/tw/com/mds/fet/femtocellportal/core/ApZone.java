package tw.com.mds.fet.femtocellportal.core;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import tw.com.mds.fet.femtocellportal.dao.PersistentObject;

@Entity
public class ApZone extends PersistentObject implements Modifiable {

	private static final long serialVersionUID = 1L;

	private String name;
	private Date createTime;
	private Date updateTime;

	public ApZone() {
		super();
	}

	public ApZone(String name) {
		this.name = name;
	}

	@Column(nullable = false, length = 128)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		return "ApZone [name=" + name + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}

}
