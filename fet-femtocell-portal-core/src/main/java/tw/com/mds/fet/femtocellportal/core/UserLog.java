package tw.com.mds.fet.femtocellportal.core;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import tw.com.mds.fet.femtocellportal.dao.PersistentObject;

@Entity
public class UserLog extends PersistentObject {

	private static final long serialVersionUID = 1L;

	private UserLogType type;
	private String operatorId;
	private String operatorName;
	private String operatorSourceIp;
	private String userName;
	private String userMobile;
	private String profileApei;
	private String profileImsi;
	private String content;
	private Date createTime;

	@Enumerated
	@Column(nullable = false)
	public UserLogType getType() {
		return type;
	}

	public void setType(UserLogType type) {
		this.type = type;
	}

	@Column(length = 50)
	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	@Column(length = 50)
	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	@Column(length = 50)
	public String getOperatorSourceIp() {
		return operatorSourceIp;
	}

	public void setOperatorSourceIp(String operatorSourceIp) {
		this.operatorSourceIp = operatorSourceIp;
	}

	@Column(length = 50)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(length = 16)
	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	@Column(length = 21)
	public String getProfileApei() {
		return profileApei;
	}

	public void setProfileApei(String profileApei) {
		this.profileApei = profileApei;
	}

	@Column(length = 15)
	public String getProfileImsi() {
		return profileImsi;
	}

	public void setProfileImsi(String profileImsi) {
		this.profileImsi = profileImsi;
	}

	@Column(length = 4000)	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
		return "UserLog [type=" + type + ", operatorId=" + operatorId
				+ ", operatorName=" + operatorName + ", operatorSourceIp="
				+ operatorSourceIp + ", userName=" + userName + ", userMobile="
				+ userMobile + ", profileApei=" + profileApei
				+ ", profileImsi=" + profileImsi + ", content=" + content
				+ ", createTime=" + createTime + "]";
	}

}
