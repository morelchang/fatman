package tw.com.mds.fet.femtocellportal.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.dao.PersistentObject;

@Entity
@Table(name = "NE_STG_3G_HW_CELL")
public class ApmRecord extends PersistentObject {

	private static final long serialVersionUID = 1L;

	private String apei;
	private String rncname;
	private String nodebname;
	private String cellid;
	private String cellname;
	private String lac;
	private String locell;
	private String mcc;
	private String mnc;
	private String maxtxpower;
	private String rac;
	private String sac;
	private String uarfcndownlink;
	private String uarfcnuplink;
	private String pscrambcode;
	private String hspdschcodenum;
	private String hsscchcodenum;
	private String hspdschmaxcodenum;
	private String hspdschmincodenum;
	private String pcpichpower;
	private String pschpower;
	private String maxhsdpausernum;
	private String rnc_id;
	private String longitude;
	private String latitude;
	private String beamdirection;

	@Transient
	public String getApei() {
		return apei;
	}

	public void setApei(String apei) {
		this.apei = apei;
	}

	@Column(length = 10)
	public String getRncname() {
		return rncname;
	}

	public void setRncname(String rncname) {
		this.rncname = rncname;
	}

	@Column(length = 10)
	public String getNodebname() {
		return nodebname;
	}

	public void setNodebname(String nodebname) {
		this.nodebname = nodebname;
	}

	@Column(length = 10)
	public String getCellid() {
		return cellid;
	}

	public void setCellid(String cellid) {
		this.cellid = cellid;
	}

	@Column(length = 10)
	public String getCellname() {
		return cellname;
	}

	public void setCellname(String cellname) {
		this.cellname = cellname;
	}

	@Column(length = 5)
	public String getLac() {
		return lac;
	}

	public void setLac(String lac) {
		this.lac = lac;
	}

	@Column(length = 10)
	public String getLocell() {
		return locell;
	}

	public void setLocell(String locell) {
		this.locell = locell;
	}

	@Column(length = 5)
	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	@Column(length = 5)
	public String getMnc() {
		return mnc;
	}

	public void setMnc(String mnc) {
		this.mnc = mnc;
	}

	@Column(length = 5)
	public String getMaxtxpower() {
		return maxtxpower;
	}

	public void setMaxtxpower(String maxtxpower) {
		this.maxtxpower = maxtxpower;
	}

	@Column(length = 5)
	public String getRac() {
		return rac;
	}

	public void setRac(String rac) {
		this.rac = rac;
	}

	@Column(length = 5)
	public String getSac() {
		return sac;
	}

	public void setSac(String sac) {
		this.sac = sac;
	}

	@Column(length = 5)
	public String getUarfcndownlink() {
		return uarfcndownlink;
	}

	public void setUarfcndownlink(String uarfcndownlink) {
		this.uarfcndownlink = uarfcndownlink;
	}

	@Column(length = 5)
	public String getUarfcnuplink() {
		return uarfcnuplink;
	}

	public void setUarfcnuplink(String uarfcnuplink) {
		this.uarfcnuplink = uarfcnuplink;
	}

	@Column(length = 5)
	public String getPscrambcode() {
		return pscrambcode;
	}

	public void setPscrambcode(String pscrambcode) {
		this.pscrambcode = pscrambcode;
	}

	@Column(length = 5)
	public String getHspdschcodenum() {
		return hspdschcodenum;
	}

	public void setHspdschcodenum(String hspdschcodenum) {
		this.hspdschcodenum = hspdschcodenum;
	}

	@Column(length = 5)
	public String getHsscchcodenum() {
		return hsscchcodenum;
	}

	public void setHsscchcodenum(String hsscchcodenum) {
		this.hsscchcodenum = hsscchcodenum;
	}

	@Column(length = 5)
	public String getHspdschmaxcodenum() {
		return hspdschmaxcodenum;
	}

	public void setHspdschmaxcodenum(String hspdschmaxcodenum) {
		this.hspdschmaxcodenum = hspdschmaxcodenum;
	}

	@Column(length = 5)
	public String getHspdschmincodenum() {
		return hspdschmincodenum;
	}

	public void setHspdschmincodenum(String hspdschmincodenum) {
		this.hspdschmincodenum = hspdschmincodenum;
	}

	@Column(length = 10)
	public String getPcpichpower() {
		return pcpichpower;
	}

	public void setPcpichpower(String pcpichpower) {
		this.pcpichpower = pcpichpower;
	}

	@Column(length = 10)
	public String getPschpower() {
		return pschpower;
	}

	public void setPschpower(String pschpower) {
		this.pschpower = pschpower;
	}

	@Column(length = 10)
	public String getMaxhsdpausernum() {
		return maxhsdpausernum;
	}

	public void setMaxhsdpausernum(String maxhsdpausernum) {
		this.maxhsdpausernum = maxhsdpausernum;
	}

	@Column(length = 10)
	public String getRnc_id() {
		return rnc_id;
	}

	public void setRnc_id(String rnc_id) {
		this.rnc_id = rnc_id;
	}

	@Column(length = 20)
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@Column(length = 20)
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	@Column(length = 5)
	public String getBeamdirection() {
		return beamdirection;
	}

	public void setBeamdirection(String beamdirection) {
		this.beamdirection = beamdirection;
	}

	public String toString(String separator) {
		return StringUtils.defaultIfEmpty(rncname, "") + separator
				+ StringUtils.defaultIfEmpty(nodebname, "") + separator
				+ StringUtils.defaultIfEmpty(cellid, "") + separator
				+ StringUtils.defaultIfEmpty(cellname, "") + separator
				+ StringUtils.defaultIfEmpty(lac, "") + separator
				+ StringUtils.defaultIfEmpty(locell, "") + separator
				+ StringUtils.defaultIfEmpty(mcc, "") + separator
				+ StringUtils.defaultIfEmpty(mnc, "") + separator
				+ StringUtils.defaultIfEmpty(maxtxpower, "") + separator
				+ StringUtils.defaultIfEmpty(rac, "") + separator
				+ StringUtils.defaultIfEmpty(sac, "") + separator
				+ StringUtils.defaultIfEmpty(uarfcndownlink, "") + separator
				+ StringUtils.defaultIfEmpty(uarfcnuplink, "") + separator
				+ StringUtils.defaultIfEmpty(pscrambcode, "") + separator
				+ StringUtils.defaultIfEmpty(hspdschcodenum, "") + separator
				+ StringUtils.defaultIfEmpty(hsscchcodenum, "") + separator
				+ StringUtils.defaultIfEmpty(hspdschmaxcodenum, "") + separator
				+ StringUtils.defaultIfEmpty(hspdschmincodenum, "") + separator
				+ StringUtils.defaultIfEmpty(pcpichpower, "") + separator
				+ StringUtils.defaultIfEmpty(pschpower, "") + separator
				+ StringUtils.defaultIfEmpty(maxhsdpausernum, "") + separator
				+ StringUtils.defaultIfEmpty(rnc_id, "") + separator
				+ StringUtils.defaultIfEmpty(longitude, "") + separator
				+ StringUtils.defaultIfEmpty(latitude, "") + separator
				+ StringUtils.defaultIfEmpty(beamdirection, "");
	}

}
