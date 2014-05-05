package tw.com.mds.fet.femtocellportal.gis;

import java.util.List;

import tw.com.mds.fet.femtocellportal.core.Cell;
import tw.com.mds.fet.femtocellportal.core.LocationDetectMode;
import tw.com.mds.fet.femtocellportal.core.Position;

public interface GisService {

	public PositionResult queryPositionByAddress(String address)
			throws GisException, AddressFormatException;

	public List<Cell> queryNearestCellsByPosition(Position position,
			LocationDetectMode locationDetectMode) throws GisException, NoCellFoundException;

	public String getGisLocationLink();

}
