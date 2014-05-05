package tw.com.mds.fet.femtocellportal.gis;

import tw.com.mds.fet.femtocellportal.core.ServiceException;

public class GisException extends ServiceException {

	private static final long serialVersionUID = 1L;

	{
		errorCode = 12000;
	}
	
	public GisException() {
		super();
	}

	public GisException(int errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public GisException(String message, Throwable cause) {
		super(message, cause);
	}

	public GisException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public GisException(String message) {
		super(message);
	}

	public GisException(Throwable cause) {
		super(cause);
	}

}
