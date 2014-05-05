package tw.com.mds.fet.femtocellportal.ahr.impl.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "", "", "", "", "" })
public class LocationInfo {

	@XmlElement(name = "type", nillable = false)
	private int type;
	@XmlElement(name = "PLMNID", nillable = false)
	private int plmnid;
	@XmlElement(name = "RNCID", nillable = false)
	private int rncid;
	@XmlElement(name = "LACID", nillable = false)
	private int lacid;
	@XmlElement(name = "cellID", nillable = false)
	private int cellId;

	public LocationInfo() {
		super();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPlmnid() {
		return plmnid;
	}

	public void setPlmnid(int plmnid) {
		this.plmnid = plmnid;
	}

	public int getRncid() {
		return rncid;
	}

	public void setRncid(int rncid) {
		this.rncid = rncid;
	}

	public int getLacid() {
		return lacid;
	}

	public void setLacid(int lacid) {
		this.lacid = lacid;
	}

	public int getCellId() {
		return cellId;
	}

	public void setCellId(int cellId) {
		this.cellId = cellId;
	}

	@Override
	public String toString() {
		return "LocationInfo [type=" + type + ", plmnid=" + plmnid + ", rncid="
				+ rncid + ", lacid=" + lacid + ", cellId=" + cellId + "]";
	}

}
