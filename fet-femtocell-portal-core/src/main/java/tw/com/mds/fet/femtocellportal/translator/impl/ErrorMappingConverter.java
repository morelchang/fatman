package tw.com.mds.fet.femtocellportal.translator.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class ErrorMappingConverter {

	Map<String, String> convert(
			String errorCodeMessageMappingValue) {
		Map<String, String> result = new HashMap<String, String>();
		if (StringUtils.isBlank(errorCodeMessageMappingValue)) {
			return result;
		}
		String[] pairs = errorCodeMessageMappingValue.split(",");
		for (String p : pairs) {
			String[] codeMsg = p.split("=");
			if (codeMsg == null || codeMsg.length != 2 || StringUtils.isBlank(codeMsg[0])) {
				continue;
			}
			result.put(StringUtils.trim(codeMsg[0]), StringUtils.trim(codeMsg[1]));
		}
		return result;
	}

}
