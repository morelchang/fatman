package tw.com.mds.fet.femtocellportal.core;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import tw.com.mds.fet.femtocellportal.dao.PersistentObject;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"cell_name"}))
public class Cell extends PersistentObject implements Modifiable {

	private static final long serialVersionUID = 1L;
	
	private String cellId;
	private String cellName;
	private CellType type;
	private String plmId;
	private Rnc rnc;
	private String lacId;
	private Position position;
	private Integer lonIdx;
	private Integer latIdx;
	private Integer dir;
	private String ven;
	private String mcc;
	private String mnc;
	private Date createTime;
	private Date updateTime;

	@Column(name = "cell_id", nullable = false, length = 20)
	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	@Column(name = "cell_name", length = 20)
	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	public CellType getType() {
		return type;
	}

	public void setType(CellType type) {
		this.type = type;
	}

	@Column(nullable = false, length = 20)
	public String getPlmId() {
		return plmId;
	}

	public void setPlmId(String plmId) {
		this.plmId = plmId;
	}

	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	public Rnc getRnc() {
		return rnc;
	}

	public void setRnc(Rnc rnc) {
		this.rnc = rnc;
	}

	@Column(nullable = false, length = 20)
	public String getLacId() {
		return lacId;
	}

	public void setLacId(String lacId) {
		this.lacId = lacId;
	}

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "longitude", column = @Column(name = "lon", precision = 14, scale = 10, nullable = false)),
			@AttributeOverride(name = "latitude", column = @Column(name = "lat", precision = 14, scale = 10, nullable = false)) })
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	
	@Column(name = "lon_idx")
	public Integer getLonIdx() {
		return lonIdx;
	}

	public void setLonIdx(Integer lonIdx) {
		this.lonIdx = lonIdx;
	}

	@Column(name = "lat_idx")
	public Integer getLatIdx() {
		return latIdx;
	}

	public void setLatIdx(Integer latIdx) {
		this.latIdx = latIdx;
	}

	@Column
	public Integer getDir() {
		return dir;
	}

	public void setDir(Integer dir) {
		this.dir = dir;
	}

	@Column(length = 50)
	public String getVen() {
		return ven;
	}

	public void setVen(String ven) {
		this.ven = ven;
	}

	@Column(length = 50)
	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	@Column(length = 50)
	public String getMnc() {
		return mnc;
	}

	public void setMnc(String mnc) {
		this.mnc = mnc;
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

	/**
	 * copy value from specified cell object
	 * <p>
	 * except id and createTime property
	 * </p>
	 * 
	 * @param cell
	 */
	public void copy(Cell cell) {
		setCellId(cell.getCellId());
		setCellName(cell.getCellName());
		setType(cell.getType());
		setPlmId(cell.getPlmId());
		setRnc(cell.getRnc());
		setLacId(cell.getLacId());
		Position p = cell.getPosition();
		Position tw97p = p.toTw97();
		setLonIdx(tw97p.getLongitude().intValue());
		setLatIdx(tw97p.getLatitude().intValue());
		setDir(cell.getDir());
		setVen(cell.getVen());
		setMcc(cell.getMcc());
		setMnc(cell.getMnc());
		setUpdateTime(cell.getUpdateTime());
	}

	@Override
	public String toString() {
		return "Cell [cellId=" + cellId + ", cellName=" + cellName + ", type="
				+ type + ", plmId=" + plmId + ", rnc=" + rnc + ", lacId="
				+ lacId + ", position=" + position + ", lonIdx=" + lonIdx
				+ ", latIdx=" + latIdx + ", dir=" + dir + ", ven=" + ven
				+ ", mcc=" + mcc + ", mnc=" + mnc + ", createTime="
				+ createTime + ", updateTime=" + updateTime + "]";
	}

}
