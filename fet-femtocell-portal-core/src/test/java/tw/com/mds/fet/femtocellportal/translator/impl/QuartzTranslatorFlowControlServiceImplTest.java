package tw.com.mds.fet.femtocellportal.translator.impl;

import org.junit.Assert;
import org.junit.Test;

import tw.com.mds.fet.femtocellportal.test.DefaultTestCase;

public class QuartzTranslatorFlowControlServiceImplTest extends DefaultTestCase {

	private QuartzTranslatorFlowControlServiceImpl job = new QuartzTranslatorFlowControlServiceImpl();
	
	@Test
	public void testCalculateInterval() {
		Assert.assertEquals(1000, job.calculateInterval(1));
		Assert.assertEquals(500, job.calculateInterval(2));
		Assert.assertEquals(250, job.calculateInterval(4));
		Assert.assertEquals(10, job.calculateInterval(100));
		Assert.assertEquals(2000, job.calculateInterval(0.5));
		Assert.assertEquals(10000, job.calculateInterval(0.1));
		Assert.assertEquals(1000000, job.calculateInterval(0.001));
		try {
			job.calculateInterval(0);
			job.calculateInterval(-1);
			job.calculateInterval(-0.1);
			job.calculateInterval(-0.00001);
			Assert.fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
		}
	}
	
}
