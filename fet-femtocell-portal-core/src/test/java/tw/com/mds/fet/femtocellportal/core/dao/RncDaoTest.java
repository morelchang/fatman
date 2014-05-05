package tw.com.mds.fet.femtocellportal.core.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import tw.com.mds.fet.femtocellportal.test.DefaultTestCase;
import tw.com.mds.fet.femtocellportal.test.TestData;

public class RncDaoTest extends DefaultTestCase {

	@Autowired
	private TestData data;
	
	@Test
	public void testFind() {
		Assert.assertNotNull(data);
	}
	
}
