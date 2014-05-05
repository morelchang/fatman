package tw.com.mds.fet.femtocellportal.core;


public class ConfigurationException extends ServiceException {

	private static final long serialVersionUID = 1L;

	{
		errorCode = 10001;
	}
	
	public ConfigurationException() {
		super();
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(Throwable cause) {
		super(cause);
	}

}
