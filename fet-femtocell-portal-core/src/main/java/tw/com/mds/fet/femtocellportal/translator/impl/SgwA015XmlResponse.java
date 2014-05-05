package tw.com.mds.fet.femtocellportal.translator.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SIGAPI_OUTPUT")
@XmlAccessorType(XmlAccessType.FIELD)
public class SgwA015XmlResponse extends FetDefaultTranslatorResponse {

	@XmlElement(name = "IMSI")
	private String imsi = "";
	@XmlElement(name = "ICCID")
	private String iccId = "";
	@XmlElement(name = "HLR")
	private String hlr = "";
	@XmlElement(name = "SIMTYPE")
	private String simType = "";
	@XmlElement(name = "SIMSTATUS")
	private String simStatus = "";
	@XmlElement(name = "PRODUCTTYPE")
	private String productType = "";
	@XmlElement(name = "SERVICETYPE")
	private String serviceType = "";
	@XmlElement(name = "SERVICECATEGORY")
	private String serviceCategory = "";
	@XmlElement(name = "MVNO")
	private String mvNo = "";

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getIccId() {
		return iccId;
	}

	public void setIccId(String iccId) {
		this.iccId = iccId;
	}

	public String getHlr() {
		return hlr;
	}

	public void setHlr(String hlr) {
		this.hlr = hlr;
	}

	public String getSimType() {
		return simType;
	}

	public void setSimType(String simType) {
		this.simType = simType;
	}

	public String getSimStatus() {
		return simStatus;
	}

	public void setSimStatus(String simStatus) {
		this.simStatus = simStatus;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * value '2G' or '3G', indicates the system type
	 * 
	 * @return
	 */
	public String getServiceCategory() {
		return serviceCategory;
	}

	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}

	public String getMvNo() {
		return mvNo;
	}

	public void setMvNo(String mvNo) {
		this.mvNo = mvNo;
	}

}
