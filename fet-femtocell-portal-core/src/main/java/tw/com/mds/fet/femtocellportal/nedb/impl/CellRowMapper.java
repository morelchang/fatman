package tw.com.mds.fet.femtocellportal.nedb.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;

import tw.com.mds.fet.femtocellportal.core.Cell;
import tw.com.mds.fet.femtocellportal.core.CellType;
import tw.com.mds.fet.femtocellportal.core.Position;
import tw.com.mds.fet.femtocellportal.core.Rnc;
import tw.com.mds.fet.femtocellportal.util.Utils;

public class CellRowMapper implements RowMapper<Cell> {

	private static final Log logger = LogFactory.getLog(CellRowMapper.class);
	
	private Map<String, Rnc> rncMap = new HashMap<String, Rnc>();
	private Map<String, CellType> cellTypeMap = new HashMap<String, CellType>();
	private boolean enableRncNameConsistencyCheck;
	private boolean enableRncIdConsistencyCheck;
	private String defaultPlmnId;

	public CellRowMapper(List<Rnc> rncs, String defaultPlmnId, boolean enableRncNameConsistencyCheck,
			boolean enableRncIdConsistencyCheck) {
		super();
		this.enableRncNameConsistencyCheck = enableRncNameConsistencyCheck;
		this.enableRncIdConsistencyCheck = enableRncIdConsistencyCheck;
		this.rncMap = convertToMap(rncs);
		this.defaultPlmnId = defaultPlmnId;
		initializeDefaultCellTypeMap();
	}

	private void initializeDefaultCellTypeMap() {
		this.cellTypeMap.put("2G", CellType.TwoG);
		this.cellTypeMap.put("3G", CellType.ThreeG);
	}

	private Map<String, Rnc> convertToMap(List<Rnc> rncs) {
		Map<String, Rnc> result = new HashMap<String, Rnc>();
		for (Rnc rnc : rncs) {
			result.put(rnc.getRncId(), rnc);
		}
		return result;
	}

	public Cell mapRow(ResultSet rs, int count) {
		String cellId = null;
		String cellName = null;
//		String systype = null;
		String lacId = null;
		String rncId = null;
		String rncName = null;
		BigDecimal longitude = null;
		BigDecimal latitude = null;
		String dir = null;
		String ven = null;
		String mcc = null;
		String mnc = null;
		
		// get cell record
		try {
			cellId = rs.getString("CELL_ID");
			cellName = rs.getString("CELL_NAME");
//			systype = rs.getString("SYSTYPE");
			lacId = rs.getString("LAC");
			rncId = rs.getString("BSC_RNC_ID");
			rncName = rs.getString("RNC_NAME");
			longitude = rs.getBigDecimal("LONGITUDE");
			latitude = rs.getBigDecimal("LATITUDE");
			dir = rs.getString("DIR");
			ven = rs.getString("VEN");
			mcc = rs.getString("MCC");
			mnc = rs.getString("MNC");
		} catch (SQLException e) {
			// TODO error handling, notify if mapping failed
			Utils.error(logger, "failed to map, reason:{0}", e, e.getMessage());
		}

		Cell cell = new Cell();
		cell.setCellId(cellId);
		cell.setCellName(cellName);
		
		// plmnId
		cell.setPlmId(defaultPlmnId);
		
		// celltype conversion
		CellType cellType = mapCellType(ven);
		if (cellType == null && !StringUtils.isEmpty(ven)) {
			// TODO error handling, notify if unknown SYSTYPE found
			throw new RuntimeException(Utils.format("invalid VEN:{0} to convert to CellType, count:{1}", ven, count));
		}
		cell.setType(cellType);
		
		// rncId conversion
		Rnc rnc = findRnc(rncId, rncName);
		cell.setRnc(rnc);
		
		// longitude, latitude conversion
		Position p = new Position(longitude, latitude);
		cell.setPosition(p);
		
		// ignore cells having no long/lat value
		if (p.getLatitude() == null || p.getLongitude() == null) {
			Utils.warn(logger, "cellId:{0}, having long:{1}, lat:{2}, one of the values is null, skip this cell", 
					cell.getCellId(), p.getLongitude(), p.getLatitude());
			return null;
		}
		// twd97 position 
		Position tw97Position;
		tw97Position = p.toTw97();
		cell.setLonIdx(tw97Position.getLongitude().intValue());
		cell.setLatIdx(tw97Position.getLatitude().intValue());

		if (StringUtils.isNotEmpty(dir) && StringUtils.isNumeric(dir)) {
			cell.setDir(Integer.valueOf(dir));
		}
		cell.setLacId(lacId);
		cell.setVen(ven);
		cell.setMcc(mcc);
		cell.setMnc(mnc);
		return cell;
	}

	private CellType mapCellType(String ven) {
		if (ven == null) {
			return null;
		}
		
		Iterator<Entry<String, CellType>> it = cellTypeMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, CellType> entry = it.next();
			if (ven.contains(entry.getKey())) {
				return entry.getValue();
			}
		}
		return null;
	}

	private Rnc findRnc(String rncId, String rncName) {
		Rnc rnc = null;
		// find rnc by id
		if (!StringUtils.isEmpty(rncId)) {
			rnc = findRncByRncId(rncId);
		}
		if (rnc != null) {
//			Utils.debug(logger, "found rnc:{0} by rncId:{1}", rnc, rncId);
			if (enableRncNameConsistencyCheck && !rnc.getRncName().equalsIgnoreCase(rncName)) {
				// TODO error handling: inconsistency of rnc name
				Utils.warn(logger, "inconsistency of rnc name, local:{0}, remote:{1}", rnc.getRncName(), rncName);
			}
		}

		// find rnc by name
		if (!StringUtils.isEmpty(rncName)) {
			rnc = findRncByName(rncName);
		}
		if (rnc != null) {
//			Utils.debug(logger, "found rnc:{0} by rncName:{1}", rnc, rncName);
			if (enableRncIdConsistencyCheck && !rnc.getRncId().equalsIgnoreCase(rncId)) {
				// TODO error handling: inconsistency of rnc name
				Utils.warn(logger, "inconsistency of rnc id, local:{0}, remote:{1}", rnc.getRncId(), rncId);
			}
		}
		
		return rnc;
	}

	private Rnc findRncByName(String rncName) {
		Set<Entry<String, Rnc>> entrySet = rncMap.entrySet();
		Iterator<Entry<String, Rnc>> it = entrySet.iterator();
		while (it.hasNext()) {
			Rnc rnc = it.next().getValue();
			if (rnc.getRncName().equalsIgnoreCase(rncName)) {
				return rnc;
			}
		}
		return null;
	}

	private Rnc findRncByRncId(String rncId) {
		Rnc rnc = rncMap.get(rncId);
		return rnc;
	}

	public Map<String, Rnc> getRncMap() {
		return rncMap;
	}

	public void setRncMap(Map<String, Rnc> rncMap) {
		this.rncMap = rncMap;
	}

	public Map<String, CellType> getCellTypeMap() {
		return cellTypeMap;
	}

	public void setCellTypeMap(Map<String, CellType> cellTypeMap) {
		this.cellTypeMap = cellTypeMap;
	}

}
