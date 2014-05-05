package tw.com.mds.fet.femtocellportal.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Utils {

	private static final String DEFAULT_JAXB_ENCODING = "UTF-8";

	public static <T> T getSpringBean(String beanName) {
		String aplicationContextFile = "applicationContext*.xml";
		return Utils.<T>getSpringBean(beanName, aplicationContextFile);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getSpringBean(String beanName, String aplicationContextFile) {
		ApplicationContext c = new ClassPathXmlApplicationContext(
				aplicationContextFile);
		T bean = (T) c.getBean(beanName);
		return bean;
	}

	public static <T> T unmarshalByJaxb(String xmlContent, Class<T> clazz)
			throws JAXBException, XMLStreamException, IOException {
		return unmarshalByJaxb(xmlContent, clazz, DEFAULT_JAXB_ENCODING);
	}

	@SuppressWarnings("unchecked")
	public static <T> T unmarshalByJaxb(String xmlContent, Class<T> clazz,
			String encoding) throws JAXBException, PropertyException, XMLStreamException, IOException {
		
//		XMLInputFactory xif = XMLInputFactory.newInstance();
//        XMLStreamReader xsr = xif.createXMLStreamReader(new ByteArrayInputStream(IOUtil.toByteArray(xmlContent)));
//        xsr = new CaseInsensitiveXMLStreamReader(xsr);
		
        JAXBContext jc = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
//		unmarshaller.setProperty("jaxb.encoding", encoding);
		return (T) unmarshaller.unmarshal(new ByteArrayInputStream(xmlContent.getBytes()));
	}

	public static <T> String marshalByJaxb(T object)
			throws JAXBException {
		return marshalByJaxb(object, DEFAULT_JAXB_ENCODING);
	}

	public static <T> String marshalByJaxb(T object, String encoding)
			throws JAXBException, PropertyException {
		JAXBContext jc = JAXBContext.newInstance(object.getClass());
		Marshaller marshaller = jc.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(object, sw);
		return sw.toString();
	}

	public static int getCurrentHour() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.HOUR_OF_DAY);
	}

	public static void debug(Log logger, String messagePattern, Object... arguments) {
		if (!logger.isDebugEnabled()) {
			return;
		}
		logger.debug(MessageFormat.format(messagePattern, arguments));
	}

	public static void info(Log logger, String messagePattern, Object... arguments) {
		if (!logger.isInfoEnabled()) {
			return;
		}
		logger.info(MessageFormat.format(messagePattern, arguments));
	}

	public static void warn(Log logger, String messagePattern, Object... arguments) {
		if (!logger.isWarnEnabled()) {
			return;
		}
		logger.warn(MessageFormat.format(messagePattern, arguments));
	}
	
	public static void error(Log logger, String messagePattern, Throwable e, Object... arguments) {
		if (!logger.isErrorEnabled()) {
			return;
		}
		if (e != null) {
			logger.error(MessageFormat.format(messagePattern, arguments), e);
		} else {
			logger.error(MessageFormat.format(messagePattern, arguments));
		}
	}
	
	public static void error(Log logger, String messagePattern, Object... arguments) {
		error(logger, messagePattern, null, arguments);
	}

	public static String format(String pattern, Object... param) {
		return MessageFormat.format(pattern, param);
	}

	public static String maskRight(String value, int maskCount, String maskChar) {
		if (maskCount <= 0 || value == null) {
			return value;
		}
		if (value.length() <= maskCount) {
			return StringUtils.repeat(maskChar, value.length());
		}
		return value.substring(0, value.length() - maskCount) + StringUtils.repeat(maskChar, maskCount);
	}

	public static String mask(String value, int startCharCount, int endCharCount, String maskChar) {
		if (startCharCount <= 0 || endCharCount <= 0) {
			throw new IllegalArgumentException(Utils.format("invalid startCount:{0} or endCharCount:{1}, must larger then 0", 
					startCharCount, endCharCount));
		}
		if (startCharCount > endCharCount) {
			throw new IllegalArgumentException(Utils.format("startCount:{0} must less than endCharCount:{1}", 
					startCharCount, endCharCount));
		}
		if (value == null) {
			return value;
		}
		if (startCharCount > value.length()) {
			return value;
		}
		
		if (endCharCount > value.length()) {
			endCharCount = value.length();
		}
		int maskCahrCount = endCharCount - startCharCount + 1;
		return value.substring(0, startCharCount - 1) + StringUtils.repeat(maskChar, maskCahrCount) + value.substring(endCharCount);
	}

	public static String maskAfter(String value, int startCharCount, String maskChar) {
		if (value == null || startCharCount >= value.length()) {
			return value;
		}
		return mask(value, startCharCount + 1, value.length(), maskChar);
	}

	public static Date addDays(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		return c.getTime();
	}

	public static boolean isHourBetween(int hour,
			int hourFrom, int hourTo) {
		validateHour(hour);
		validateHour(hourFrom);
		validateHour(hourTo);
		if (hourFrom <= hourTo) {
			return hour >= hourFrom && hour < hourTo;
		} else {
			return hour > hourFrom || hour <= hourTo;
		}
	}

	private static void validateHour(int hour) {
		if (hour < 0 || hour > 23) {
			throw new IllegalArgumentException(format("value {0} is not a valid hour value(0~23)", hour));
		}
		return;
	}

}
