package tw.com.mds.fet.femtocellportal.translator.impl;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.test.DefaultTestCase;

public class XmlHttpTranslatorServiceImplTest extends DefaultTestCase {

	@Autowired
	private FetTranslatorServiceImpl service;

	@Autowired
	private SyncQueueDelegate delegate;

	@Test
	@Ignore
	public void testIntegration() throws ServiceUnderMaintenanceException,
			ServiceException {
		String msisdn = "0915363414";
		String imsi = service.queryImsiByMsisdn(msisdn);
		Assert.assertEquals("466887244782654", imsi);
	}

	@Test
	@Ignore
	public void testSyncQueueDelegate() throws ServiceException {
		String msisdn = "0989075354";
		for (int i = 0; i < 20; i++) {
			msisdn = StringUtils.leftPad(String.valueOf(i), 10, "0");
			try {
				String imsi = delegate.queryImsiByMsisdn(msisdn);
				Assert.assertNotNull("", imsi);
			} catch (Exception e) {
			}
		}
	}
}
