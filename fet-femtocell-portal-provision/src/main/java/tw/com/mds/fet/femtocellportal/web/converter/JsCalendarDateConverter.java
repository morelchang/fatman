package tw.com.mds.fet.femtocellportal.web.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.util.StrutsTypeConverter;

public class JsCalendarDateConverter extends StrutsTypeConverter {
	
	private static final Log logger = LogFactory.getLog(JsCalendarDateConverter.class);
	
	public static final String JS_CALENDAR_DATE_FORMAT = "%Y/%m/%d %H:%M:%S";
	
	private static final String JAVA_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(JAVA_DATE_FORMAT);

	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		if (values == null || values.length < 1) {
			return null;
		}
		try {
			return SIMPLE_DATE_FORMAT.parse(values[0]);
		} catch (ParseException e) {
			logger.warn(e);
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map context, Object o) {
		if (o instanceof Date) {
			return SIMPLE_DATE_FORMAT.format(o);
		}
		return "";
	}

}
