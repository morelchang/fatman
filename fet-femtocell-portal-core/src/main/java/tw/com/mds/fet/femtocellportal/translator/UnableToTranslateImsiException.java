package tw.com.mds.fet.femtocellportal.translator;

import tw.com.mds.fet.femtocellportal.util.Utils;


public class UnableToTranslateImsiException extends TranslatorException {

	private static final long serialVersionUID = 1L;

	{
		errorCode = 14008;
	}
	
	private final String msisdn;
	private final String sigErrorCode;
	private final String sigErrorMessage;

	public UnableToTranslateImsiException(String msisdn, String sigErrorCode, String sigErrorMessage) {
		super(Utils.format(
				"msisdn:{0}, return code:{1}, description:{2}", 
				msisdn, sigErrorCode, sigErrorMessage));
		this.msisdn = msisdn;
		this.sigErrorCode = sigErrorCode;
		this.sigErrorMessage = sigErrorMessage;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public String getSigErrorCode() {
		return sigErrorCode;
	}

	public String getSigErrorMessage() {
		return sigErrorMessage;
	}

}
