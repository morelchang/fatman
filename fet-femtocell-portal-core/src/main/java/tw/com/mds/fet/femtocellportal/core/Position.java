package tw.com.mds.fet.femtocellportal.core;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Position implements Serializable {

	private static final long serialVersionUID = 1L;
	private BigDecimal longitude;
	private BigDecimal latitude;

	public Position() {
		super();
	}

	public Position(BigDecimal longitude, BigDecimal latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	@Column(precision = 14, scale = 10, nullable = false)
	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	@Column(precision = 14, scale = 10, nullable = false)
	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public Position toTw97() {
		if (getLongitude() == null) {
			throw new IllegalStateException("failed to convert tw97 value, longitude is null");
		}
		if (getLatitude() == null) {
			throw new IllegalStateException("failed to convert tw97 value, latitude is null");
		}
		double[] values = latLonToTM2(getLongitude().doubleValue(), getLatitude().doubleValue());
		BigDecimal lon = new BigDecimal(values[0]);
		BigDecimal lat = new BigDecimal(values[1]);

		Position tw97 = new Position();
		tw97.setLongitude(lon);
		tw97.setLatitude(lat);
		return tw97;
	}

	private double[] latLonToTM2(double lon, double lat) {
		lon = Math.PI / 180 * lon;
		lat = Math.PI / 180 * lat;

		double a = 6378137.0;
		double b = 6356752.314245;
		double long0 = Math.PI / 180 * 121;
		double k0 = 0.9999;
		double dx = 250000;

		double e = Math.pow(1 - (Math.pow(b, 2) / Math.pow(a, 2)), 0.5);
		double e2 = Math.pow(e, 2) / (1 - Math.pow(e, 2));

		double n = (a - b) / (a + b);
		double nu = a
				/ Math.pow((1 - Math.pow(e, 2) * Math.pow(Math.sin(lat), 2)),
						0.5);
		double p = lon - long0;

		double A = a
				* (1 - n + (5 / 4.0) * (Math.pow(n, 2) - Math.pow(n, 3)) + (81 / 64.0)
						* (Math.pow(n, 4) - Math.pow(n, 5)));
		double B = (3 * a * n / 2.0)
				* (1 - n + (7 / 8.0) * (Math.pow(n, 2) - Math.pow(n, 3)))
				+ (55 / 64.0) * (Math.pow(n, 4) - Math.pow(n, 5));

		double C = (15 * a * (Math.pow(n, 2)) / 16.0)
				* (1 - n + (3 / 4.0) * (Math.pow(n, 2) - Math.pow(n, 3)));
		double D = (35 * a * (Math.pow(n, 3)) / 48.0)
				* (1 - n + (11 / 16.0) * (Math.pow(n, 2) - Math.pow(n, 3)));
		double E = 315 * a * (Math.pow(n, 4) / 51.0) * (1 - n);

		double S = A * lat - B * Math.sin(2 * lat) + C * Math.sin(4 * lat) - D
				* Math.sin(6 * lat) + E * Math.sin(8 * lat);

		double K1 = S * k0;
		double K2 = k0 * nu * Math.sin(2 * lat) / 4.0;
		double K3 = (k0 * nu * Math.sin(lat) * (Math.pow(Math.cos(lat), 3)) / 24.0)
				* (5 - Math.pow(Math.tan(lat), 2) + 9 * e2
						* (Math.pow(Math.cos(lat), 2)) + 4 * Math.pow(e2, 2)
						* Math.pow(Math.cos(lat), 4));

		double y = K1 + K2 * (Math.pow(p, 2)) + K3 * (Math.pow(p, 4));

		double K4 = k0 * nu * Math.cos(lat);
		double K5 = (k0 * nu * (Math.pow(Math.cos(lat), 3)) / 6.0)
				* (1 - Math.pow(Math.tan(lat), 2) + e2
						* (Math.pow(Math.cos(lat), 2)));

		double x = K4 * p + K5 * (Math.pow(p, 3)) + dx;
		double[] center = new double[] { x, y };
		return center;
	}

	@Override
	public String toString() {
		return "Position [longitude=" + longitude + ", latitude=" + latitude
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result
				+ ((longitude == null) ? 0 : longitude.hashCode());
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
		Position other = (Position) obj;
		if (latitude == null) {
			if (other.latitude != null)
				return false;
		} else if (!latitude.equals(other.latitude))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
			return false;
		return true;
	}

}
