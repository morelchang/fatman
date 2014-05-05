package tw.com.mds.fet.femtocellportal.test;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import tw.com.mds.fet.femtocellportal.core.ExceptionHandler;
import tw.com.mds.fet.femtocellportal.translator.TranslatorException;
import tw.com.mds.fet.femtocellportal.translator.TranslatorService;

public class AopTest extends DefaultTestCase {

	@Autowired
	private TranslatorService stubTranslatorService;
	
	@Autowired
	private ExceptionHandler e;
	
	@Test
	@Ignore
	public void test() throws Exception {
		TranslatorException e2 = null;
		try {
			stubTranslatorService.queryImsiByMsisdn("1");
			Assert.fail("excetion expected");
		} catch (TranslatorException e) {
			e2 = e;
		}
//		Assert.assertTrue(e.isInvoked());
//		Assert.assertEquals(e2, e.getE());
	}
	
}
