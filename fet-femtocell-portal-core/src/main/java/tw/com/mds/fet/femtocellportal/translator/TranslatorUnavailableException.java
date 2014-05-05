package tw.com.mds.fet.femtocellportal.translator;

public class TranslatorUnavailableException extends TranslatorException {

	private static final long serialVersionUID = 1L;

	{
		errorCode = 14007;
	}
	
	public TranslatorUnavailableException() {
		super();
	}

	public TranslatorUnavailableException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public TranslatorUnavailableException(String arg0) {
		super(arg0);
	}

	public TranslatorUnavailableException(Throwable arg0) {
		super(arg0);
	}

}
