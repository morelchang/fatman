package tw.com.mds.fet.femtocellportal.nedb;

import tw.com.mds.fet.femtocellportal.core.ServiceException;

public class NedbException extends ServiceException {

	private static final long serialVersionUID = 1L;

	{
		errorCode = 13000;
	}
	
	public NedbException() {
		super();
	}

	public NedbException(int errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public NedbException(String message, Throwable cause) {
		super(message, cause);
	}

	public NedbException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public NedbException(String message) {
		super(message);
	}

	public NedbException(Throwable cause) {
		super(cause);
	}

}
