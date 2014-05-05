package tw.com.mds.fet.femtocellportal.translator;


/**
 * represents asynchronous response from {@link TranslatorFlowControlService}
 * 
 * @author morel
 * 
 */
public class ImsiQueryResponse {

	/**
	 * asynchronous query result
	 * 
	 * @author morel
	 * 
	 */
	public static enum Result {
		/**
		 * represents request is under processing
		 */
		QUERYING,
		/**
		 * represents query query successful
		 */
		SUCCESSFUL,
		/**
		 * represents query failed due to remote service unavailable
		 */
		SERVICE_UAVAILABLE,
		/**
		 * represents query failed due to remote service is under maintenance
		 */
		SERVICE_MAINTENANCE,
		/**
		 * represents other unexpected failure of querying
		 */
		FAILED
	}

	private String imsi;
	private Result result;
	private Exception exception;

	public ImsiQueryResponse(String imsi, Result result) {
		this(imsi, result, null);
	}

	public ImsiQueryResponse(String imsi, Result result, Exception exception) {
		super();
		this.imsi = imsi;
		this.result = result;
		this.exception = exception;
	}

	public void copy(ImsiQueryResponse response) {
		this.imsi = response.imsi;
		this.result = response.result;
		this.exception = response.exception;
	}

	/**
	 * get result against the request
	 * 
	 * @return
	 */
	public Result getResult() {
		return result;
	}

	/**
	 * get IMSI against the request
	 * 
	 * @return
	 */
	public String getImsi() {
		return imsi;
	}

	public Exception getException() {
		return exception;
	}

	public boolean isQuerying() {
		return Result.QUERYING == result;
	}

	public boolean isFailed() {
		return Result.FAILED == result;
	}

	public boolean isSuccessful() {
		return Result.SUCCESSFUL == result;
	}

	public boolean isServiceUnavailable() {
		return Result.SERVICE_UAVAILABLE == result;
	}

}
