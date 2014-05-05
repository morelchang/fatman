package tw.com.mds.fet.femtocellportal.translator.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.translator.TranslatorService;
import tw.com.mds.fet.femtocellportal.translator.UnableToTranslateImsiException;

public class StubTranslatorServiceImpl implements TranslatorService {
	
	private Map<String, String> msisdnImsiMap = new HashMap<String, String>();

	public String queryImsiByMsisdn(String msisdn) throws ServiceException {
		return ramdonImsi(msisdn);
	}

	private String ramdonImsi(String msisdn) throws ServiceException {
		if (!StringUtils.isNumeric(msisdn)) {
			throw new UnableToTranslateImsiException(msisdn, "sig004error_2001", "這是客製錯誤訊息");
		}
		
		String imsi = msisdnImsiMap.get(msisdn);
		if (imsi == null) {
			imsi = StringUtils.leftPad(String.valueOf(new Random().nextInt(Integer.MAX_VALUE)), 15, "0");
			msisdnImsiMap.put(msisdn, imsi);
		}
		return imsi;
	}

	public boolean isUnderMaintenance() {
		return false;
	}

	public Map<String, String> getMsisdnImsiMap() {
		return msisdnImsiMap;
	}

	public void setMsisdnImsiMap(Map<String, String> msisdnImsiMap) {
		this.msisdnImsiMap = msisdnImsiMap;
	}

}
