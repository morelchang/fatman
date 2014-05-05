package tw.com.mds.fet.femtocellportal.translator.impl;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tw.com.mds.fet.femtocellportal.core.Config;
import tw.com.mds.fet.femtocellportal.core.ConfigurableService;
import tw.com.mds.fet.femtocellportal.core.ConfigurationException;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.translator.TranslatorException;
import tw.com.mds.fet.femtocellportal.translator.TranslatorService;
import tw.com.mds.fet.femtocellportal.translator.TranslatorUnavailableException;
import tw.com.mds.fet.femtocellportal.translator.UnableToTranslateImsiException;
import tw.com.mds.fet.femtocellportal.util.Utils;

/**
 * an XML/HTTP protocol implementation of {@link TranslatorService} for FET
 * 
 * @author morel
 * 
 */
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = ServiceException.class)
public class FetTranslatorServiceImpl implements TranslatorService,
		ConfigurableService {

	private static final Log logger = LogFactory.getLog(FetTranslatorServiceImpl.class);
	private static final String DEFAULT_SIG_SUCCESS_RESPONSE_CODE = "00000000";

	private FetTranslatorConfigDao fetTranslatorConfigDao;
	private FetTranslatorConfig config = new FetTranslatorConfig();
	private ErrorMappingConverter errorCodeConverter = new ErrorMappingConverter();
	private Map<String, String> sigErrorCodeMessageMapping = new HashMap<String, String>();
	private HttpClient httpClient = new DefaultHttpClient();
	
	public void init() throws ConfigurationException {
		reloadConfig();
	}

	public String queryImsiByMsisdn(String msisdn)
			throws ServiceUnderMaintenanceException, TranslatorException, ConfigurationException {
		if (config == null) {
			throw new ConfigurationException("config setting, a FetTranslatorCofig must be set");
		}
		config.validate();
		
		if (isUnderMaintenance()) {
			String errorMessage = MessageFormat.format(
					"service is under maintenance time:{0}:00~{1}:00", 
					config.getMaintenanceHourFrom(), config.getMaintenanceHourTo());
			Utils.info(logger, errorMessage);
			throw new ServiceUnderMaintenanceException(errorMessage);
		}

		// query subscriberId by sgwA004
		SgwA004XmlRequest sgwA004XmlRequest = createSgwA004XmlRequest(msisdn);
		SgwA004XmlResponse sgwA004XmlResponse = submitTranslator(config.getSgwA004Url(), sgwA004XmlRequest, SgwA004XmlResponse.class);
		throwIfReturnErrorCode(msisdn, sgwA004XmlResponse);
		
		// query imsi by sgwA015
		String subscriberId = sgwA004XmlResponse.getSubscriberId();
		String entity = sgwA004XmlResponse.getEntity();
		SgwA015XmlRequest sgwA015XmlRequest = createSgwA015Request(subscriberId, entity);
		SgwA015XmlResponse sgwA015XmlResponse = submitTranslator(config.getSgwA015Url(), sgwA015XmlRequest, SgwA015XmlResponse.class);
		throwIfReturnErrorCode(msisdn, sgwA015XmlResponse);

		return sgwA015XmlResponse.getImsi();
	}

	private void throwIfReturnErrorCode(String msisdn,
			FetDefaultTranslatorResponse response)
			throws UnableToTranslateImsiException {
		if (response.getReturnCode().equals(DEFAULT_SIG_SUCCESS_RESPONSE_CODE)) {
			return;
		}

		// map custom error message
		String errorMessage = sigErrorCodeMessageMapping.get(response.getReturnCode());
		if (errorMessage == null) {
			errorMessage = response.getDescription();
		}
		throw new UnableToTranslateImsiException(msisdn, response.getReturnCode(), errorMessage);
	}

	public boolean isUnderMaintenance() {
		if (!config.isEnableMaintananceTimeCheck()) {
			Utils.debug(logger, "maintenance time check is disabled, skip check");
			return false;
		}
		int currentHour = Utils.getCurrentHour();
		return Utils.isHourBetween(currentHour, config.getMaintenanceHourFrom(), config.getMaintenanceHourTo());
	}

	private <T extends FetDefaultTranslatorResponse> T submitTranslator(
			String url, FetDefaultTranslatorRequest request,
			Class<T> responseClazz) throws TranslatorException {

		// encoding
		String xmlEncoding = config.getXmlEncoding();
		Utils.debug(logger, "translator xml encoding:{0}", xmlEncoding);

		// prepare request xml content
		Utils.debug(logger, "requesting to tranlator url:{0} with request:{1}", url, request);
		String requestXmlContent;
		try {
			requestXmlContent = Utils.marshalByJaxb(request, xmlEncoding);
		} catch (Exception e) {
			throw new TranslatorException(14001, Utils.format(
					"failed to generate request xml content:{0}", e.getMessage()), e);
		}
		Utils.debug(logger, "request xml content:{0}", requestXmlContent);
		
		// send http post request
		URI uri;
		try {
			String composedUrl = composeGetUriWithParams(url, request, requestXmlContent, xmlEncoding);
			logger.info(Utils.format("submitting HTTP GET request:{0}", composedUrl));
			uri = new URI(composedUrl);
		} catch (Exception e) {
			throw new TranslatorException(14001, Utils.format("failed to prepare request for translator:{0}, reason:{1}", 
					url, e.getMessage()), e);
		}
		
		HttpGet httpRequest = new HttpGet(uri);
		String responseXmlContent;
		try {
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			
			// check HTTP STATUS CODE
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new TranslatorUnavailableException(Utils.format(
						"HTTP response {0} error from Translator", 
						httpResponse.getStatusLine().getStatusCode()));
			}
			
			// unmarshal content
			InputStream responseSream = httpResponse.getEntity().getContent();
			responseXmlContent = IOUtils.toString(responseSream, xmlEncoding);
		} catch (Exception e) {
			throw new TranslatorException(14002, Utils.format("failed to submit request to translator:{0}, reason:{1}", 
					url, e.getMessage()), e);
		}
		Utils.info(logger, "receive response with xml content:{0}", responseXmlContent);
		
		T response;
		try {
			response = Utils.unmarshalByJaxb(responseXmlContent, responseClazz);
		} catch (Exception e) {
			throw new TranslatorException(14003, Utils.format(
					"failed to parse response xml, reason:{0}", e.getMessage()), e);
		}
		Utils.debug(logger, "parsed response xml content as:{0}", response);
		return response;
	}

	private String composeGetUriWithParams(String url,
			FetDefaultTranslatorRequest request, String requestXmlContent,
			String encoding) throws UnsupportedEncodingException {
		// prepare http params
		StringBuilder result = new StringBuilder(url);
		result.append("?action=" + URLEncoder.encode(request.getActionName(), encoding));
		result.append("&xml=" + URLEncoder.encode(requestXmlContent, encoding));
		result.append("&type=99&Submit=Submit");
		return result.toString();
	}

	private SgwA004XmlRequest createSgwA004XmlRequest(String msisdn) {
		SgwA004XmlRequest request = new SgwA004XmlRequest();
		request.setUserNameF(config.getUserNameF());
		request.setPasswordF(config.getPasswordF());
		request.setUserIdF(config.getUserIdF());
		request.setChannelIdF(config.getChannelIdF());
		request.setUserNameK(config.getUserNameK());
		request.setPasswordK(config.getPasswordK());
		request.setEntity(config.getEntity());
		request.setMsisdn(msisdn);
		return request;
	}

	private SgwA015XmlRequest createSgwA015Request(String subscriberId, String entity) {
		SgwA015XmlRequest request = new SgwA015XmlRequest();
		request.setUserNameF(config.getUserNameF());
		request.setPasswordF(config.getPasswordF());
		request.setUserIdF(config.getUserIdF());
		request.setChannelIdF(config.getChannelIdF());
		request.setUserNameK(config.getUserNameK());
		request.setPasswordK(config.getPasswordK());
		request.setEntity(config.getEntity());
		if (!StringUtils.isEmpty(entity)) {
			request.setEntity(entity);
		}
		request.setSubscriberId(subscriberId);
		return request;
	}

	/* (non-Javadoc)
	 * @see tw.com.mds.fet.femtocellportal.translator.impl.ConfigurableService#reloadConfig()
	 */
	public void reloadConfig() throws ConfigurationException {
		FetTranslatorConfig loaded = fetTranslatorConfigDao.load();
		if (loaded == null) {
			Utils.warn(logger, "no FetTranslatorConfig found, using current config:{0}", config);
			return;
		}
		
		loaded.validate();
		this.config = loaded;
		
		this.sigErrorCodeMessageMapping = errorCodeConverter.convert(this.config.getErrorCodeMessageMappingValue());
		Utils.info(logger, "reloaded FetTranslatorConfig:{0}", this.config);
	}

	/* (non-Javadoc)
	 * @see tw.com.mds.fet.femtocellportal.translator.impl.ConfigurableService#applyConfig(tw.com.mds.fet.femtocellportal.translator.impl.FetTranslatorConfig)
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public void applyConfig(Config config) throws ConfigurationException {
		FetTranslatorConfig newConfig = (FetTranslatorConfig) config;
		newConfig.validate();
		
		newConfig.setOid(this.config.getOid());
		newConfig.setUpdateTime(new Date());
		
		newConfig = fetTranslatorConfigDao.save(newConfig);
		this.sigErrorCodeMessageMapping = errorCodeConverter.convert(newConfig.getErrorCodeMessageMappingValue());
		this.config = newConfig;
		Utils.info(logger, "applied FetTranslatorConfig:{0}", newConfig);
	}
	
	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	/* (non-Javadoc)
	 * @see tw.com.mds.fet.femtocellportal.translator.impl.ConfigurableService#getConfig()
	 */
	public FetTranslatorConfig getConfig() {
		try {
			return (FetTranslatorConfig) BeanUtils.cloneBean(config);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see tw.com.mds.fet.femtocellportal.translator.impl.ConfigurableService#setConfig(tw.com.mds.fet.femtocellportal.translator.impl.FetTranslatorConfig)
	 */
	public void setConfig(FetTranslatorConfig config) {
		this.config = config;
	}

	public FetTranslatorConfigDao getFetTranslatorConfigDao() {
		return fetTranslatorConfigDao;
	}

	public void setFetTranslatorConfigDao(
			FetTranslatorConfigDao fetTranslatorConfigDao) {
		this.fetTranslatorConfigDao = fetTranslatorConfigDao;
	}

}
