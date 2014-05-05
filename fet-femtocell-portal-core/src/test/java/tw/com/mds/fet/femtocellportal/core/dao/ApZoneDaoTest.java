package tw.com.mds.fet.femtocellportal.core.dao;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import tw.com.mds.fet.femtocellportal.core.ApZone;
import tw.com.mds.fet.femtocellportal.test.DefaultTestCase;

public class ApZoneDaoTest extends DefaultTestCase {

	@Autowired
	private ApZoneDao dao;
	
	private List<ApZone> testData;
	
	@Before
	public void init() {
		testData = Arrays.asList(
				data.createApZone("一號區"),
				data.createApZone("二號區"),
				data.createApZone("三號區"));
	}
	
	@Test
	public void testFindFuzzyByName() {
		Assert.assertEquals(Arrays.asList(testData.get(0)), dao.findByFuzzyName("一"));
		Assert.assertEquals(Arrays.asList(testData.get(1)), dao.findByFuzzyName("二"));
		
		Assert.assertTrue(dao.findByFuzzyName("區").containsAll(testData));
	}
	
	@Test
	public void testFindExactlyByName() {
		Assert.assertEquals(Arrays.asList(testData.get(0)), dao.findByFuzzyName("一號區"));
		Assert.assertEquals(Arrays.asList(testData.get(2)), dao.findByFuzzyName("三號區"));
		
		Assert.assertNull(dao.findExactlyByName("區"));
	}

}
