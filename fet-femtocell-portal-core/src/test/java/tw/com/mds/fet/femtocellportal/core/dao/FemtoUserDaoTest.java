package tw.com.mds.fet.femtocellportal.core.dao;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.test.DefaultTestCase;

public class FemtoUserDaoTest extends DefaultTestCase {

	@Autowired
	private FemtoUserDao dao;
	
	private List<FemtoUser> testData;
	
	@Before
	public void init() {
		testData = dao.persist(Arrays.asList(
				data.createFemtoUser("張明煌"),
				data.createFemtoUser("張小黃"),
				data.createFemtoUser("陳小燻")));
		dao.flushAndClear();
	}
	
	@Test
	public void testFindByUserName() {
		List<FemtoUser> c = dao.findByUserName("張明煌");
		Assert.assertEquals(1, c.size());
		Assert.assertEquals(testData.get(0), c.get(0));
		
		c = dao.findByUserName("小");
		Assert.assertTrue(c.isEmpty());
	}
	
}
