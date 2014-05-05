package tw.com.mds.fet.femtocellportal.translator.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


@XmlAccessorType(XmlAccessType.FIELD)
public abstract class FetDefaultTranslatorResponse {

	@XmlElement(name = "RETURN_CODE")
	protected String returnCode = "";
	@XmlElement(name = "DESCRIPTION")
	protected String description = "";

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
