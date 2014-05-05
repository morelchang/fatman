package tw.com.mds.fet.femtocellportal.translator.impl;

import tw.com.mds.fet.femtocellportal.translator.TranslatorException;

public class ServiceUnderMaintenanceException extends TranslatorException {

	private static final long serialVersionUID = -344526710359853566L;

	{
		errorCode = 14006;
	}
	
	public ServiceUnderMaintenanceException() {
		super();
	}

	public ServiceUnderMaintenanceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceUnderMaintenanceException(String message) {
		super(message);
	}

	public ServiceUnderMaintenanceException(Throwable cause) {
		super(cause);
	}

}
