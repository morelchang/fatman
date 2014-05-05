package tw.com.mds.fet.femtocellportal.translator;

import tw.com.mds.fet.femtocellportal.core.ServiceException;

public class TranslatorException extends ServiceException {

	private static final long serialVersionUID = 1L;

	{
		errorCode = 14000;
	}
	
	public TranslatorException() {
		super();
	}

	public TranslatorException(int errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public TranslatorException(String message, Throwable cause) {
		super(message, cause);
	}

	public TranslatorException(String message) {
		super(message);
	}

	public TranslatorException(int errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public TranslatorException(Throwable cause) {
		super(cause);
	}

}
