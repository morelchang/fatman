package tw.com.mds.fet.femtocellportal.core;

import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.util.Utils;

public class PrivacyMasker {

	String maskUserName(String userName) {
		return Utils.mask(userName, 2, 2, "＊");
	}

	String maskMobileNumber(String mobile) {
		return Utils.mask(mobile, 5, 7, "＊");
	}

	String maskAddress(String femtoAddress) {
		if (StringUtils.isEmpty(femtoAddress)) {
			return femtoAddress;
		}
		
		if (femtoAddress.length() > 10) {
			return Utils.maskAfter(femtoAddress, 7, "＊");
		}
		return Utils.maskAfter(femtoAddress, 5, "＊");
	}

}
