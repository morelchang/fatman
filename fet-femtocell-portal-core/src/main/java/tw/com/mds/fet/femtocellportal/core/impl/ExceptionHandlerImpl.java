package tw.com.mds.fet.femtocellportal.core.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tw.com.mds.fet.femtocellportal.core.Config;
import tw.com.mds.fet.femtocellportal.core.ConfigurableService;
import tw.com.mds.fet.femtocellportal.core.ConfigurationException;
import tw.com.mds.fet.femtocellportal.core.ExceptionHandler;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.core.dao.ExceptionHandlerConfigDao;
import tw.com.mds.fet.femtocellportal.util.Utils;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPException;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = ServiceException.class)
public class ExceptionHandlerImpl implements ExceptionHandler, ConfigurableService {
	
	private static final Log logger = LogFactory.getLog(ExceptionHandlerImpl.class);
	
	private ExceptionHandlerConfig config;
	private ExceptionHandlerConfigDao exceptionHandlerConfigDao;
	
	public ExceptionHandlerImpl() throws ConfigurationException {
		super();
	}

	public void init() throws ConfigurationException {
		reloadConfig();
	}
	
	/* (non-Javadoc)
	 * @see tw.com.mds.fet.femtocellportal.core.impl.ExceptionHandler#handleException(tw.com.mds.fet.femtocellportal.core.ServiceException)
	 */
	public void handleException(ServiceException e) {
		String errorCode = String.valueOf(e.getErrorCode());
		if (getErrorCodeList().contains(errorCode)) {
			logger.info(MessageFormat.format(
					"handling exception:{0}, message:{1}, errorCode:{2}", 
					e.getClass().getName(), e.getMessage(), errorCode));
			sendSmsMessage(e);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug(MessageFormat.format(
						"ignoring exception:{0}, message:{1}, errorCode:{2}", 
						e.getClass().getName(), e.getMessage(), errorCode));
			}
		}
	}

	private List<String> getErrorCodeList() {
		return convertErrorCodeList(config.getErrorCodeListValue());
	}

	private List<String> convertErrorCodeList(String errorCodeListValue) {
		List<String> result = new ArrayList<String>();
		if (errorCodeListValue == null || StringUtils.isBlank(errorCodeListValue)) {
			return result;
		}
		
		String[] list = StringUtils.split(errorCodeListValue, ",");
		for (String errorCode : list) {
			if (StringUtils.isBlank(errorCode)) {
				continue;
			}
			result.add(StringUtils.trim(errorCode));
		}
		return result;
	}

	private void sendSmsMessage(ServiceException e) {
		try {
			FTPClient ftp = connectToRemotePath();
			
			Utils.debug(logger, "sending sms files...");
			String fileNameBase = "TSSSASA" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String smsFileName = fileNameBase + ".txt";
			String endFileName = fileNameBase + ".end";
			String message = composeSmsMessage(e, convertCallerList());
			Utils.debug(logger, "sms file message:\n{0}", message);
			
			InputStream is = new BufferedInputStream(new ByteArrayInputStream(message.getBytes()));
			ftp.put(is, smsFileName);
			InputStream isend = new BufferedInputStream(new ByteArrayInputStream("".getBytes()));
			ftp.put(isend, endFileName);
			ftp.quit();
			Utils.debug(logger, "completed sending sms files");
			
		} catch (IOException e1) {
			logger.error(Utils.format(
					"failed to send sms message to ftp server:{0}, reason:{1}", 
					config.getFtpIpAddress(), e1.getMessage()), e1);
		} catch (FTPException e1) {
			logger.error(Utils.format(
					"failed to send sms message to ftp server:{0}, reason:{1}", 
					config.getFtpIpAddress(), e1.getMessage()), e1);
		}
	}

	public static void main(String[] args) {
		System.out.println(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
	}
	private List<String> convertCallerList() {
		String value = config.getCallerListValue();
		List<String> result = new ArrayList<String>();
		if (StringUtils.isBlank(value)) {
			return result;
		}
		
		String[] list = StringUtils.split(value, ",");
		for (String number : list) {
			if (StringUtils.isBlank(number)) {
				continue;
			}
			result.add(StringUtils.trim(number));
		}
		return result;
	}

	private FTPClient connectToRemotePath() throws IOException, FTPException {
		FTPClient ftp = new FTPClient();
		Utils.debug(logger, "connecting to FTP server:{0}", config.getFtpIpAddress());
		ftp.setRemoteHost(config.getFtpIpAddress());
		ftp.connect();
		ftp.login(config.getFtpLoginUser(), config.getFtpLoginPassword());
		Utils.debug(logger, "changing remote dir:{0}", config.getFtpRemoteDir());
		ftp.chdir(config.getFtpRemoteDir());
		return ftp;
	}

	private String composeSmsMessage(ServiceException e, List<String> callers) {
		return Utils.format("femto provision error, {0}\nE\n{1}", 
				e.getMessage(), StringUtils.join(callers, "\n"));
	}
	
	public void testFtpConnection() throws IOException, FTPException {
		connectToRemotePath().quit();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public void applyConfig(Config config) throws ConfigurationException {
		ExceptionHandlerConfig newConfig = (ExceptionHandlerConfig) config;
		newConfig.validate();
		
		newConfig.setOid(this.config.getOid());
		newConfig.setUpdateTime(new Date());
		
		newConfig = exceptionHandlerConfigDao.save(newConfig);
		this.config = newConfig;
		Utils.info(logger, "applied ExceptionHandlerConfig:{0}", newConfig);
	}
	
	public void reloadConfig() throws ConfigurationException {
		ExceptionHandlerConfig loaded = exceptionHandlerConfigDao.load();
		if (loaded == null) {
			Utils.warn(logger, "no ExceptionHandlerConfig found, using current config:{0}", config);
			return;
		}
		
		loaded.validate();
		this.config = loaded;
		Utils.info(logger, "reloaded ExceptionHandlerConfig:{0}", loaded);
	}
	
	public ExceptionHandlerConfig getConfig() {
		try {
			return (ExceptionHandlerConfig) BeanUtils.cloneBean(config);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setConfig(ExceptionHandlerConfig config) {
		this.config = config;
	}

	public ExceptionHandlerConfigDao getExceptionHandlerConfigDao() {
		return exceptionHandlerConfigDao;
	}

	public void setExceptionHandlerConfigDao(
			ExceptionHandlerConfigDao exceptionHandlerConfigDao) {
		this.exceptionHandlerConfigDao = exceptionHandlerConfigDao;
	}

}
