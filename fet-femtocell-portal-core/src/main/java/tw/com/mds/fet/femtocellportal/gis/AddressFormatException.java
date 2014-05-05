package tw.com.mds.fet.femtocellportal.gis;

public class AddressFormatException extends GisException {

	private static final long serialVersionUID = 1L;

	{
		errorCode = 12007;
	}
	
	public AddressFormatException() {
		super();
	}

	public AddressFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public AddressFormatException(String message) {
		super(message);
	}

	public AddressFormatException(Throwable cause) {
		super(cause);
	}

}
