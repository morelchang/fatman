package tw.com.mds.fet.femtocellportal.gis;

import tw.com.mds.fet.femtocellportal.core.LocationDetectMode;
import tw.com.mds.fet.femtocellportal.core.Position;
import tw.com.mds.fet.femtocellportal.util.Utils;

public class NoCellFoundException extends GisException {

	private static final long serialVersionUID = 1L;
	
	private final Position position;
	private final LocationDetectMode locationDetectMode;
	
	public NoCellFoundException(Position position,
			LocationDetectMode locationDetectMode) {
		super(Utils.format("no cell found with parameters, position:{0}, locationDetectMode:{1}", 
				position, locationDetectMode));
		this.position = position;
		this.locationDetectMode = locationDetectMode;
	}

	public Position getPosition() {
		return position;
	}

	public LocationDetectMode getLocationDetectMode() {
		return locationDetectMode;
	}

}
