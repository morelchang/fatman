package tw.com.mds.fet.femtocellportal.core;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import tw.com.mds.fet.femtocellportal.dao.PersistentObject;

@MappedSuperclass
public abstract class Config extends PersistentObject {

	private static final long serialVersionUID = 1L;

	private Date updateTime;
	
	public Config() {
		super();
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public abstract void validate() throws ConfigurationException;

}