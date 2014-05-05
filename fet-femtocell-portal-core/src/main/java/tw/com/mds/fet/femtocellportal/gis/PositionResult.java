package tw.com.mds.fet.femtocellportal.gis;

import tw.com.mds.fet.femtocellportal.core.LocatingState;
import tw.com.mds.fet.femtocellportal.core.Position;

public class PositionResult {

	private Position position;
	private LocatingState locatingState;

	public PositionResult(Position position, LocatingState locatingState) {
		super();
		this.position = position;
		this.locatingState = locatingState;
	}

	public Position getPosition() {
		return position;
	}

	public LocatingState getLocatingState() {
		return locatingState;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((locatingState == null) ? 0 : locatingState.hashCode());
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PositionResult other = (PositionResult) obj;
		if (locatingState != other.locatingState)
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PositionResult [position=" + position + ", locatingState="
				+ locatingState + "]";
	}

}
