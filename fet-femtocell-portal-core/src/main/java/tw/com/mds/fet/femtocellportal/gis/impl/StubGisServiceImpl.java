package tw.com.mds.fet.femtocellportal.gis.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tw.com.mds.fet.femtocellportal.core.Cell;
import tw.com.mds.fet.femtocellportal.core.CellType;
import tw.com.mds.fet.femtocellportal.core.LocatingState;
import tw.com.mds.fet.femtocellportal.core.LocationDetectMode;
import tw.com.mds.fet.femtocellportal.core.Position;
import tw.com.mds.fet.femtocellportal.core.Rnc;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.core.dao.CellDao;
import tw.com.mds.fet.femtocellportal.core.dao.RncDao;
import tw.com.mds.fet.femtocellportal.gis.AddressFormatException;
import tw.com.mds.fet.femtocellportal.gis.GisException;
import tw.com.mds.fet.femtocellportal.gis.GisService;
import tw.com.mds.fet.femtocellportal.gis.NoCellFoundException;
import tw.com.mds.fet.femtocellportal.gis.PositionResult;
import tw.com.mds.fet.femtocellportal.test.TestData;
import tw.com.mds.fet.femtocellportal.util.Utils;

public class StubGisServiceImpl implements GisService {
	
	private Map<String, Position> cache = new HashMap<String, Position>();
	private List<Cell> cells = new ArrayList<Cell>();
	
	@Autowired
	private RncDao rncDao;
	@Autowired
	private CellDao cellDao;
	private String gisLocationLink;
	
	public PositionResult queryPositionByAddress(String address) throws GisException {
		Position pos = cache.get(address);
		if (pos == null) {
			TestData data = new TestData();
			pos = data.buildPosition();
			cache.put(address, pos);
		}
		
		if (address != null && address.startsWith("0")) {
			throw new AddressFormatException(Utils.format(
					"address:{0} format is not correct", address));
		}
		return new PositionResult(pos, LocatingState.LOCATED_ABSOLUTELY);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public List<Cell> queryNearestCellsByPosition(Position position, LocationDetectMode locationDetectMode)
			throws GisException {
		if (locationDetectMode == LocationDetectMode.SECOND_AND_THIRG_G) {
			throw new NoCellFoundException(null, null);
		}
		
		// position = null
		if (position == null) {
			throw new GisException("test exception");
		}
		
		// using configured cell as result first
		if (!cells.isEmpty()) {
			initFixedTestingCells();
			return this.cells;
		}

		// using random generated 2G+3G as result
		if (locationDetectMode == LocationDetectMode.SECOND_AND_THIRG_G) {
			List<Cell> result = randomCells(CellType.ThreeG, 6);
			result.addAll(randomCells(CellType.TwoG, 6));
			return result;
		}
		
		// random generated 6
		return randomCells(locationDetectMode);
	}

	public String getGisLocationLink() {
		return gisLocationLink;
	}

	public List<Cell> initFixedTestingCells() {
		Date now = new Date();
		List<Cell> persisCells = new ArrayList<Cell>();
		Iterator<Cell> it = cells.iterator();
		while (it.hasNext()) {
			Cell cell = it.next();
			Cell existsCell = cellDao.findByCellId(cell.getCellId());
			if (existsCell != null) {
				persisCells.add(existsCell);
				continue;
			}
			
			Rnc rnc = cell.getRnc();
			Rnc exists = rncDao.findByRncId(rnc.getRncId());
			if (exists == null) {
				rnc.setCreateTime(now);
				rnc.setUpdateTime(now);
				rnc = rncDao.persist(rnc);
			} else {
				rnc = exists;
			}
			rncDao.flush();
			
			cell.setRnc(rnc);
			cell.setCreateTime(now);
			cell.setUpdateTime(now);
			Position tw97p = cell.getPosition().toTw97();
			cell.setLonIdx(tw97p.getLongitude().intValue());
			cell.setLatIdx(tw97p.getLatitude().intValue());
			cell = cellDao.persist(cell);
			cellDao.flush();
			persisCells.add(cell);
		}
		this.cells = persisCells;
		return persisCells;
	}

	private List<Cell> randomCells(LocationDetectMode locationDetectMode) {
		CellType targetType = CellType.ThreeG;
		if (locationDetectMode == LocationDetectMode.SECOND_G) {
			targetType = CellType.TwoG;
		}
		return randomCells(targetType, 6);
	}

	private List<Cell> randomCells(CellType cellType, int count) {
		List<Cell> result = new ArrayList<Cell>();
		Random r = new Random();
		while (result.size() < count) {
			String cellId = String.valueOf(r.nextInt(10000));
			Cell cell = cellDao.findByCellId(cellId);
			if (cell == null) {
				String rncId = "rncId";
				TestData data = new TestData();
				Rnc rnc = fetchRnc(rncId, data);
				cell = data.createCell(rnc, cellId);
				cell.setType(cellType);
				if (cellType == CellType.ThreeG) {
					cell.setLacId("0");
				}
				if (cellType == CellType.TwoG) {
					cell.setRnc(null);
				}
				cell = cellDao.persist(cell);
			} else {
				if (cell.getType() != cellType) {
					continue;
				}
			}
			result.add(cell);
		}
		
		return result;
	}

	private Rnc fetchRnc(String rncId, TestData data) {
		Rnc rnc = rncDao.findByRncId(rncId);
		if (rnc == null) {
			rnc = data.createRnc(rncId, "rncName");
			rnc = rncDao.persist(rnc);
		}
		return rnc;
	}

	public List<Cell> getCells() {
		return cells;
	}

	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}

	public void setGisLocationLink(String gisLocationLink) {
		this.gisLocationLink = gisLocationLink;
	}

}
