package tw.com.mds.fet.femtocellportal.core;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import tw.com.mds.fet.femtocellportal.dao.PersistentObject;

@Entity
public class Announcement extends PersistentObject implements Modifiable {

	private static final long serialVersionUID = 1L;

	private Date startTime;
	private Date endTime;
	private String title;
	private String content;
	private UserType userType;
	private Date createTime;
	private Date updateTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(length = 100)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(length = 1000)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Enumerated
	@Column(nullable = false)
	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
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
		return "Announcement [startTime=" + startTime + ", endTime=" + endTime
				+ ", title=" + title + ", content=" + content + ", userType="
				+ userType + ", createTime=" + createTime + ", updateTime="
				+ updateTime + "]";
	}

}
