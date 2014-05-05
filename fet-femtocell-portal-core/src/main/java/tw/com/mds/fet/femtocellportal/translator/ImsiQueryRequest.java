package tw.com.mds.fet.femtocellportal.translator;

/**
 * represents request to {@link TranslatorFlowControlService}
 * 
 * @author morel
 *
 */
public class ImsiQueryRequest {

	private String msisdn;
	private ImsiQueryResponse response;

	public ImsiQueryRequest(String msisdn) {
		super();
		this.msisdn = msisdn;
	}

	public String getMsisdn() {
		return msisdn;
	}

	@Override
	public String toString() {
		return getMsisdn();
	}

	public void setResponse(ImsiQueryResponse response) {
		this.response = response;
	}

	public ImsiQueryResponse getResponse() {
		return response;
	}

}
