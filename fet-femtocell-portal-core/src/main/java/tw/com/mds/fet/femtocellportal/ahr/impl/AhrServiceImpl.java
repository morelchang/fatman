package tw.com.mds.fet.femtocellportal.ahr.impl;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import tw.com.mds.fet.femtocellportal.ahr.AhrException;
import tw.com.mds.fet.femtocellportal.ahr.AhrService;
import tw.com.mds.fet.femtocellportal.ahr.impl.messages.MessageBuilder;
import tw.com.mds.fet.femtocellportal.ahr.impl.messages.SoapEnvelope;
import tw.com.mds.fet.femtocellportal.ahr.impl.messages.UePermoission;
import tw.com.mds.fet.femtocellportal.core.FemtoProfile;
import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.core.LocationDetectMode;
import tw.com.mds.fet.femtocellportal.core.UserEquipment;
import tw.com.mds.fet.femtocellportal.util.Utils;

public class AhrServiceImpl implements AhrService {
	
	private static final Log logger = LogFactory.getLog(AhrServiceImpl.class);
	
	// TODO AHR: configuration variables
	private String uri;
	private String interfaceVersion;
	private String defaultPlmnId;
	private LocationDetectMode defaultLocationDetectMode;
	private int maxPermissionListSize = 32;
	private List<Integer> ignoringErrorCode = new ArrayList<Integer>();
	private boolean enableRetry = true;
	private int maxRetry = 3;
	private long retryWait = 1000;

	public void addProfile(FemtoUser user, FemtoProfile profile) throws AhrException {
		int serialNo = generateSerialNo();
		
		// apply default location detect mode
		if (profile.getLocationDetectMode() == null) {
			profile.setLocationDetectMode(defaultLocationDetectMode);
		}
		
		SoapEnvelope request = new MessageBuilder().newMessage(interfaceVersion, serialNo).registerAp(user, profile);
		executeRequest(request);
	}

	public void deleteProfile(FemtoProfile profile) throws AhrException {
		int serialNo = generateSerialNo();
		SoapEnvelope request = new MessageBuilder().newMessage(interfaceVersion, serialNo).unRegisterAp(profile);
		executeRequest(request);
	}

	public void setLocation(FemtoProfile profile) throws AhrException {
		int serialNo = generateSerialNo();
		SoapEnvelope request;
		request = new MessageBuilder().newMessage(interfaceVersion, serialNo).setLocation(profile, defaultPlmnId);
		executeRequest(request);
	}

	public List<UserEquipment> queryPermissionList(FemtoProfile profile) throws AhrException {
		int serialNo = generateSerialNo();
		SoapEnvelope request = new MessageBuilder().newMessage(interfaceVersion, serialNo).QueryPermissionList(profile);
		SoapEnvelope response = executeRequest(request);
		return convertToPermissionList(response.getBody().getQueryPermissionListResponse().getUePermissionList());
	}

	private List<UserEquipment> convertToPermissionList(
			List<UePermoission> uePermissionList) {
		List<UserEquipment> result = new ArrayList<UserEquipment>();
		for (UePermoission up : uePermissionList) {
			result.add(new UserEquipment(up.getMsisdn(), up.getImsi()));
		}
		return result;
	}

	public void modifyPermissionList(FemtoProfile profile, Date updateTime) throws AhrException {
		int serialNo = generateSerialNo();
		SoapEnvelope request = new MessageBuilder().newMessage(interfaceVersion, serialNo).modifyUeList(profile, updateTime);
		executeRequest(request);
	}

	public void suspend(FemtoProfile profile) throws AhrException {
		int serialNo = generateSerialNo();
		SoapEnvelope request = new MessageBuilder().newMessage(interfaceVersion, serialNo).suspendAp(profile);
		executeRequest(request);
	}

	public void resume(FemtoProfile profile) throws AhrException {
		int serialNo = generateSerialNo();
		SoapEnvelope request = new MessageBuilder().newMessage(interfaceVersion, serialNo).resumeAp(profile);
		executeRequest(request);
	}

	private int generateSerialNo() {
		return new Random().nextInt(Integer.MAX_VALUE);
	}

	private SoapEnvelope executeRequest(SoapEnvelope request) throws AhrException {
		// send request and receive response
		String marshalledRequest = marshallRequest(request);
		
		// AHR namespace prefix problem!
		String requestXml = adjustNamespacePrefix(marshalledRequest);
		String responseXml = null;
		if (enableRetry) {
			responseXml = tryExecute(requestXml);
		} else {
			responseXml = submitXmlHttpRequest(requestXml);
		}
		SoapEnvelope response = unmarshallResponse(responseXml, SoapEnvelope.class);
		
		// check return code
		throwExceptionIfNotSuccess(response);
		SoapEnvelope envelope = response;
		return envelope;
	}

	private String tryExecute(String requestXml) throws AhrException {
		int retryCount = -1;
		Throwable last;
		do {
			try {
				return submitXmlHttpRequest(requestXml);
			} catch (Throwable e) {
				retryCount++;
				last = e;
				logger.warn(MessageFormat.format(
						"failed to submitXmlHttprequest, reason:{0}", e.getMessage()), e);
				
				try {
					Thread.sleep(retryWait);
				} catch (InterruptedException e1) {
					logger.error("failed to wait for next retrying: cause" + e.getMessage());
				}
				
				if (retryCount < maxRetry) {
					Utils.debug(logger, "{0} retrying submitXmlHttpRequest to AHR", retryCount + 1);
				}
			}
		} while (retryCount < maxRetry);
		
		throw new AhrException(11001, Utils.format("AHR failed after retrying {0} times, reason:{1}", maxRetry, 
				last.getMessage()), last);
	}

	private String adjustNamespacePrefix(String requestXml) {
		requestXml = requestXml.replace("<ns2:", "<env:");
		requestXml = requestXml.replace("xmlns:ns2", "xmlns:env");
		requestXml = requestXml.replace("</ns2:", "</env:");
		return requestXml;
	}

	private String marshallRequest(SoapEnvelope request)
			throws AhrException {
		try {
			return Utils.marshalByJaxb(request);
		} catch (Exception e) {
			throw new AhrException(11000, Utils.format(
					"failed to marshall AHR request:{0}, request:{1}", e.getMessage(), request), e);
		}
	}

	public String submitXmlHttpRequest(String requestXml) throws AhrException {
		// prepare http request
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpRequest = new HttpPost(uri);
		httpRequest.setHeader("Connection", "Close");
		httpRequest.setHeader("Content-Type", "text/xml; charset=UTF-8");
		try {
			httpRequest.setEntity(new StringEntity(requestXml, HTTP.UTF_8));

			// submit and wait for response
			Utils.info(logger, "submitting to url:{0}, request xml:\n{1}", uri, requestXml); // direct xml content
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			Utils.debug(logger, "response object:{0}", httpResponse);
			
			// unmarshall response
			InputStream responseSream = httpResponse.getEntity().getContent();
			String responseXml = IOUtils.toString(responseSream);
			Utils.info(logger, "receive response xml:\n{0}", responseXml); // direct xml content
			return responseXml;
		} catch (Throwable e) {
			Utils.error(logger, "failed to request ahr:{0}", e, e.getMessage());
			throw new AhrException(11001, Utils.format("failed to request AHR server:{0}", e.getMessage()), e);
		}
	}

	private <T> T unmarshallResponse(String responseXml, Class<T> responseClass)
			throws AhrException {
		try {
			return Utils.unmarshalByJaxb(responseXml, responseClass);
		} catch (Exception e) {
			throw new AhrException(11002, Utils.format(
					"failed to unmarshall AHR response:{0}, response xml:{1}", e.getMessage(), responseXml), e);
		}
	}

	private void throwExceptionIfNotSuccess(SoapEnvelope response) throws AhrException {
		int errorCode = 0;
		if (response.getBody().getModifyUeListResponse() != null) {
			errorCode = response.getBody().getModifyUeListResponse().getReturnCode();
		} else if (response.getBody().getQueryPermissionListResponse() != null) {
			errorCode = response.getBody().getQueryPermissionListResponse().getReturnCode();
		} else if (response.getBody().getRegisterApResponse() != null) {
			errorCode = response.getBody().getRegisterApResponse().getReturnCode();
		} else if (response.getBody().getResumeApResponse() != null) {
			errorCode = response.getBody().getResumeApResponse().getReturnCode();
		} else if (response.getBody().getSetLocationResponse() != null) {
			errorCode = response.getBody().getSetLocationResponse().getReturnCode();
		} else if (response.getBody().getSuspendApResponse() != null) {
			errorCode = response.getBody().getSuspendApResponse().getReturnCode();
		} else if (response.getBody().getUnRegisterApResponse() != null) {
			errorCode = response.getBody().getUnRegisterApResponse().getReturnCode();
		}
		if (errorCode == 0) {
			return;
		}
		if (ignoringErrorCode.contains(errorCode)) {
			Utils.info(logger, "ignore error code:{0} in response:{1}", errorCode, response);
			return;
		}
		throw new AhrException(11003, Utils.format("AHR Error:{0}, response detail:{1}", errorCode, response));
	}

	public int getMaxPermissionListSize() throws AhrException {
		return maxPermissionListSize;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getInterfaceVersion() {
		return interfaceVersion;
	}

	public void setInterfaceVersion(String interfaceVersion) {
		this.interfaceVersion = interfaceVersion;
	}

	public LocationDetectMode getDefaultLocationDetectMode() {
		return defaultLocationDetectMode;
	}

	public void setDefaultLocationDetectMode(
			LocationDetectMode defaultLocationDetectMode) {
		this.defaultLocationDetectMode = defaultLocationDetectMode;
	}

	public String getDefaultPlmnId() {
		return defaultPlmnId;
	}

	public void setDefaultPlmnId(String defaultPlmnId) {
		this.defaultPlmnId = defaultPlmnId;
	}

	public List<Integer> getIgnoringErrorCode() {
		return ignoringErrorCode;
	}

	public void setIgnoringErrorCode(List<Integer> ignoringErrorCode) {
		this.ignoringErrorCode = ignoringErrorCode;
	}

}
