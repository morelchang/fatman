package tw.com.mds.fet.femtocellportal.core;


public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;
	protected int errorCode = 10000;

	public ServiceException() {
		super();
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(int errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	@Override
	public String getMessage() {
		return "[" + errorCode + "] " + super.getMessage();
	}

}
