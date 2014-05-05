package tw.com.mds.fet.femtocellportal.ahr.impl.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class SoapBody {

	@XmlElement(name = "registerAP")
	private RegisterAp registerAp;
	@XmlElement(name = "registerAPResponse")
	private Response registerApResponse;
	
	@XmlElement(name = "unRegisterAP")
	private ApIdentityRequest unRegisterAp;
	@XmlElement(name = "unRegisterAPResponse")
	private Response unRegisterApResponse;
	
	@XmlElement(name = "modifyUEList")
	private ModifyUeList modifyUeList;
	@XmlElement(name = "modifyUEListResponse")
	private Response modifyUeListResponse;
	
	@XmlElement(name = "setLocation")
	private SetLocation setLocation;
	@XmlElement(name = "setLocationResponse")
	private Response setLocationResponse;

	@XmlElement(name = "queryPermissionList")
	private QueryPermissionList queryPermissionList;
	@XmlElement(name = "queryPermissionListResponse")
	private QueryPermissionListResponse queryPermissionListResponse;
	
	@XmlElement(name = "suspendAP")
	private ApIdentityRequest suspendAp;
	@XmlElement(name = "suspendAPResponse")
	private Response suspendApResponse;
	
	@XmlElement(name = "resumeAP")
	private ApIdentityRequest resumeAp;
	@XmlElement(name = "resumeAPResponse")
	private Response resumeApResponse;
	
	public SoapBody() {
		super();
	}

	public RegisterAp getRegisterAp() {
		return registerAp;
	}

	public void setRegisterAp(RegisterAp registerAp) {
		this.registerAp = registerAp;
	}

	public Response getRegisterApResponse() {
		return registerApResponse;
	}

	public void setRegisterApResponse(Response registerApResponse) {
		this.registerApResponse = registerApResponse;
	}

	public ApIdentityRequest getUnRegisterAp() {
		return unRegisterAp;
	}

	public void setUnRegisterAp(ApIdentityRequest unRegisterAp) {
		this.unRegisterAp = unRegisterAp;
	}

	public Response getUnRegisterApResponse() {
		return unRegisterApResponse;
	}

	public void setUnRegisterApResponse(Response unRegisterApResponse) {
		this.unRegisterApResponse = unRegisterApResponse;
	}

	public ModifyUeList getModifyUeList() {
		return modifyUeList;
	}

	public void setModifyUeList(ModifyUeList modifyUeList) {
		this.modifyUeList = modifyUeList;
	}

	public Response getModifyUeListResponse() {
		return modifyUeListResponse;
	}

	public void setModifyUeListResponse(Response modifyUeListResponse) {
		this.modifyUeListResponse = modifyUeListResponse;
	}

	public SetLocation getSetLocation() {
		return setLocation;
	}

	public void setSetLocation(SetLocation setLocation) {
		this.setLocation = setLocation;
	}

	public Response getSetLocationResponse() {
		return setLocationResponse;
	}

	public void setSetLocationResponse(Response setLocationResponse) {
		this.setLocationResponse = setLocationResponse;
	}

	public QueryPermissionListResponse getQueryPermissionListResponse() {
		return queryPermissionListResponse;
	}

	public void setQueryPermissionListResponse(
			QueryPermissionListResponse queryPermissionListResponse) {
		this.queryPermissionListResponse = queryPermissionListResponse;
	}

	public QueryPermissionList getQueryPermissionList() {
		return queryPermissionList;
	}

	public void setQueryPermissionList(QueryPermissionList queryPermissionList) {
		this.queryPermissionList = queryPermissionList;
	}

	public ApIdentityRequest getSuspendAp() {
		return suspendAp;
	}

	public void setSuspendAp(ApIdentityRequest suspendAp) {
		this.suspendAp = suspendAp;
	}

	public Response getSuspendApResponse() {
		return suspendApResponse;
	}

	public void setSuspendApResponse(Response suspendApResponse) {
		this.suspendApResponse = suspendApResponse;
	}

	public ApIdentityRequest getResumeAp() {
		return resumeAp;
	}

	public void setResumeAp(ApIdentityRequest resumeAp) {
		this.resumeAp = resumeAp;
	}

	public Response getResumeApResponse() {
		return resumeApResponse;
	}

	public void setResumeApResponse(Response resumeApResponse) {
		this.resumeApResponse = resumeApResponse;
	}

	@Override
	public String toString() {
		return "SoapBody [registerAp=" + registerAp + ", registerApResponse="
				+ registerApResponse + ", unRegisterAp=" + unRegisterAp
				+ ", unRegisterApResponse=" + unRegisterApResponse
				+ ", modifyUeList=" + modifyUeList + ", modifyUeListResponse="
				+ modifyUeListResponse + ", setLocation=" + setLocation
				+ ", setLocationResponse=" + setLocationResponse
				+ ", queryPermissionList=" + queryPermissionList
				+ ", queryPermissionListResponse="
				+ queryPermissionListResponse + ", suspendAp=" + suspendAp
				+ ", suspendApResponse=" + suspendApResponse + ", resumeAp="
				+ resumeAp + ", resumeApResponse=" + resumeApResponse + "]";
	}

}
