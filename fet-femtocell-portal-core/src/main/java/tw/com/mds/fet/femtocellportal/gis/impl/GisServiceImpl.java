package tw.com.mds.fet.femtocellportal.gis.impl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import tw.com.mds.fet.femtocellportal.core.Cell;
import tw.com.mds.fet.femtocellportal.core.LocatingState;
import tw.com.mds.fet.femtocellportal.core.LocationDetectMode;
import tw.com.mds.fet.femtocellportal.core.Position;
import tw.com.mds.fet.femtocellportal.core.dao.CellDao;
import tw.com.mds.fet.femtocellportal.core.dao.RncDao;
import tw.com.mds.fet.femtocellportal.gis.AddressFormatException;
import tw.com.mds.fet.femtocellportal.gis.GisException;
import tw.com.mds.fet.femtocellportal.gis.GisService;
import tw.com.mds.fet.femtocellportal.gis.NoCellFoundException;
import tw.com.mds.fet.femtocellportal.gis.PositionResult;
import tw.com.mds.fet.femtocellportal.util.Utils;
import tw.com.mds.fet.femtocellportal.ws.AxisWebServiceBean;

public class GisServiceImpl implements GisService {

	private static final Log logger = LogFactory.getLog(GisServiceImpl.class);
	
	private AxisWebServiceBean wsGetPositionByAddress;
	private AxisWebServiceBean wsGetReversePoi;
	private RncDao rncDao;
	private CellDao cellDao;
	private JSONParser jsonParser = new JSONParser();
	private List<LocatingState> incorrectLocatingStates = new ArrayList<LocatingState>();
	private String gisLocationLink;
	private boolean enableRetry = true;
	private int maxRetry = 3;
	private long retryWait = 1000;

	public PositionResult queryPositionByAddress(String address) throws GisException {
		String positionResultValu;
		try {
			Utils.info(logger, "invoking gis getPositionByAddress:{0}, with address:{1}", wsGetPositionByAddress, address);
			if (enableRetry) {
				positionResultValu = tryWebservice(wsGetPositionByAddress, address);
			} else {
				positionResultValu = (String) wsGetPositionByAddress.call(address);
			}
			Utils.info(logger, "result of gis getPositionByAddress:{0} by address:{1}", positionResultValu, address);
			
			PositionResult positionResult = parsePositionResult(positionResultValu);
			if (isIncorrectLocateState(positionResult)) {
				throw new AddressFormatException(address + " (GIS Locating State:" + positionResult.getLocatingState() + ")");
			}
			return positionResult;
		} catch (RemoteException e) {
			throw new GisException(12001, Utils.format(
					"getPositionByAddress called to GIS failed with param:{0}, reason:{1}", 
					address, e.getMessage()), e);
		} catch (ServiceException e) {
			throw new GisException(12001, Utils.format(
					"getPositionByAddress called to GIS failed with param:{0}, reason:{1}",
					address, e.getMessage()), e);
		}
	}

	private String tryWebservice(AxisWebServiceBean ws, Object... params)
			throws GisException {
		int retryCount = -1;
		Throwable last;
		do {
			try {
				return (String) ws.call(params);
			} catch (Throwable e) {
				retryCount++;
				last = e;
				logger.warn(MessageFormat.format(
						"failed to execute {0}, reason:{1}", ws.getEndpoint(), e.getMessage()), e);
				
				try {
					Thread.sleep(retryWait);
				} catch (InterruptedException e1) {
					logger.error("failed to wait for next retrying: cause" + e.getMessage());
				}
				
				if (retryCount < maxRetry) {
					Utils.debug(logger, "{0} retrying {1} to GIS", retryCount + 1, ws.getEndpoint());
				}
			}
		} while (retryCount < maxRetry);
		
		throw new GisException(12001, Utils.format("GIS {0} failed after retrying {1} times, reason:{2}", 
				ws.getEndpoint(), maxRetry, last.getMessage()), last);
	}

	private boolean isIncorrectLocateState(PositionResult positionResult) {
		if (incorrectLocatingStates.contains(positionResult.getLocatingState())) {
			return true;
		}
		return false;
	}

	private PositionResult parsePositionResult(String positionResult) throws GisException {
		Utils.debug(logger, "parsing position result:{0}", positionResult);
		
		String[] split = StringUtils.split(positionResult, ",");
		if (split.length != 3) {
			throw new GisException(12002, Utils.format(
					"incorrect format of wsGetPositionByAddress call result:{0}", positionResult));
		}
		
		// parse longitude
		BigDecimal longitude;
		try {
			longitude = new BigDecimal(StringUtils.trim(split[0]));
		} catch (Exception e) {
			throw new GisException(12002, Utils.format("incorrect format of longitude value:''{0}'', result:{1}", split[0], positionResult));
		}
		
		// parse latitude
		BigDecimal latitude;
		try {
			latitude = new BigDecimal(StringUtils.trim(split[1]));
		} catch (Exception e) {
			throw new GisException(12002, Utils.format("incorrect format of latitude value:''{0}'', result:{1}", split[1], positionResult));
		}
		
		// parse locatingState
		LocatingState locatingState = parseLocatingState(split[2]);
		
		PositionResult result = new PositionResult(new Position(longitude, latitude), locatingState);
		Utils.debug(logger, "parsed result:{0}", result);
		
		return result;
	}

	private LocatingState parseLocatingState(String locatingStateValue) throws GisException {
		Integer index = -1;
		try {
			index = Integer.valueOf(StringUtils.trim(locatingStateValue));
		} catch (NumberFormatException e) {
			throw new GisException(12002, Utils.format(
					"incorrect LocatingState value:{0}, must be an integer value",
					locatingStateValue));
		}
		
		if (index >= LocatingState.values().length) {
			throw new GisException(12002, Utils.format(
							"incorrect LocatingState value:{0}, out of enum range:{1}",
							locatingStateValue, LocatingState.values().length));
		}
		return LocatingState.values()[index];
	}

	public List<Cell> queryNearestCellsByPosition(Position position, LocationDetectMode locationDetectMode)
			throws GisException, NoCellFoundException {
		String jsonCellsResult;
		try {
			Utils.info(logger, "invoking gis getReversePoi:{0}, with position:{1}", wsGetReversePoi, position);
			if (enableRetry) {
				jsonCellsResult = tryWebservice(wsGetReversePoi, position.getLongitude().toString(), position.getLatitude().toString(), String.valueOf(locationDetectMode.ordinal()));
			} else {
				jsonCellsResult = (String) wsGetReversePoi.call(position.getLongitude().toString(), position.getLatitude().toString(), String.valueOf(locationDetectMode.ordinal()));
			}
			Utils.info(logger, "result of gis getReversePoi:{0}", jsonCellsResult);
			
			// if gis return a number, it indicate an error occurred
			if (StringUtils.isNumeric(jsonCellsResult)) {
				Integer errorCode = Integer.valueOf(jsonCellsResult);
				if (errorCode == 1) {
					throw new NoCellFoundException(position, locationDetectMode);
				}
				throw new GisException(12003, Utils.format(
						"failed to query cells from GIS server, GIS getReversePoi return error:{0}", jsonCellsResult));
			}
			
			return parseJsonCells(jsonCellsResult);
		} catch (RemoteException e) {
			throw new GisException(12001, Utils.format(
					"wsGetReversePoi called to GIS failed with param:{0}, reason:{1}", 
					position, e.getMessage()), e);
		} catch (ServiceException e) {
			throw new GisException(12001, Utils.format(
					"wsGetReversePoi called to GIS failed with param:{0}, reason:{1}",
					position, e.getMessage()), e);
		}
	}

	@SuppressWarnings("unchecked")
	private List<Cell> parseJsonCells(String jsonCellsResult) throws GisException {
		// parse json string to json objects
		Utils.debug(logger, "parsing json cell result:{0}", jsonCellsResult);
		List<JSONObject> jsonObjects;
		try {
			jsonObjects = (List<JSONObject>) jsonParser.parse(jsonCellsResult);
		} catch (ParseException e) {
			throw new GisException(12004, Utils.format("incorrect format of JSON cell result:{0}", jsonCellsResult));
		}
		Utils.debug(logger, "parsed {0} json object", jsonObjects.size());
		
		// explain json object to cells
		List<Cell> result = new ArrayList<Cell>();
		for (JSONObject jsonObject : jsonObjects) {
			// get values in json objects
//			String cellId = (String) jsonObject.get("CELLID");
			String cellName = (String) jsonObject.get("CELLNAME");
			// 2G does not have RNC
//			String rncOidValue = (String) jsonObject.get("RNCOID");
//			Long rncOid = null;
//			try {
//				rncOid = Long.valueOf(rncOidValue);
//			} catch (NumberFormatException e) {
//				throw new GisException(12005, Utils.format("can't convert rncOid value to Long:{0}", rncOidValue));
//			}
			
			// find cell by rnc, cellId
			Cell cell = cellDao.findByCellName(cellName);
			if (cell == null) {
				throw new GisException(12006, Utils.format(
						"cant not find cell with cellName:{0}", cellName));
			}
			result.add(cell);
		}
		
		Utils.debug(logger, "parsed cells:{0}", result);
		return result;
	}
//
//	private Cell findCellBy(Long rncOid, String cellId) throws GisException {
//		Rnc rnc = rncDao.findByOid(rncOid);
//		if (rnc == null) {
//			throw new GisException(12005, Utils.format(
//					"cant not find rnc with rncOid:{0}", rncOid));
//		}
//		return cellDao.findByRncCellId(rnc, cellId);
//	}

	public AxisWebServiceBean getWsGetPositionByAddress() {
		return wsGetPositionByAddress;
	}

	public void setWsGetPositionByAddress(AxisWebServiceBean wsGetPositionByAddress) {
		this.wsGetPositionByAddress = wsGetPositionByAddress;
	}

	public AxisWebServiceBean getWsGetReversePoi() {
		return wsGetReversePoi;
	}

	public void setWsGetReversePoi(AxisWebServiceBean wsGetReversePoi) {
		this.wsGetReversePoi = wsGetReversePoi;
	}

	public CellDao getCellDao() {
		return cellDao;
	}

	public void setCellDao(CellDao cellDao) {
		this.cellDao = cellDao;
	}

	public JSONParser getJsonParser() {
		return jsonParser;
	}

	public void setJsonParser(JSONParser jsonParser) {
		this.jsonParser = jsonParser;
	}

	public List<LocatingState> getIncorrectLocatingStates() {
		return incorrectLocatingStates;
	}

	public void setIncorrectLocatingStates(
			List<LocatingState> incorrectLocatingStates) {
		this.incorrectLocatingStates = incorrectLocatingStates;
	}

	public String getGisLocationLink() {
		return gisLocationLink;
	}

	public void setGisLocationLink(String gisLocationLink) {
		this.gisLocationLink = gisLocationLink;
	}

	public RncDao getRncDao() {
		return rncDao;
	}

	public void setRncDao(RncDao rncDao) {
		this.rncDao = rncDao;
	}
 
}
