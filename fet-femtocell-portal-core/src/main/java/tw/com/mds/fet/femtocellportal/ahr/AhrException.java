package tw.com.mds.fet.femtocellportal.ahr;

import tw.com.mds.fet.femtocellportal.core.ServiceException;

public class AhrException extends ServiceException {

	private static final long serialVersionUID = 8340899226374799725L;

	{
		errorCode = 11000;
	}

	public AhrException() {
		super();
	}

	public AhrException(int errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public AhrException(String message, Throwable cause) {
		super(message, cause);
	}

	public AhrException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public AhrException(String message) {
		super(message);
	}

	public AhrException(Throwable cause) {
		super(cause);
	}

}
