package tw.com.mds.fet.femtocellportal.core.dao;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import tw.com.mds.fet.femtocellportal.core.AdminUser;
import tw.com.mds.fet.femtocellportal.core.FemtoProfile;
import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.core.ProvisionService;
import tw.com.mds.fet.femtocellportal.test.DefaultTestCase;
import tw.com.mds.fet.femtocellportal.test.TestData;

public class PorvisionServiceTest extends DefaultTestCase {

	@Autowired
	private ProvisionService service;
	
	@Autowired
	private ApZoneDao apZoneDao;
	
	@Test
	@Ignore
	public void testCreateUser() throws Exception {
		// to create unpersist entity
		TestData data = new TestData();
		
		FemtoUser user = data.createFemtoUser("morel");
		FemtoProfile profile = data.createProfile("APEI", "IMSI");
		profile.setApZone(apZoneDao.persist(data.createApZone("zone1")));

		AdminUser adminUser = data.createAdminUser();
		
		FemtoUser u = service.createUser(user, profile, adminUser);
		Assert.assertEquals(user.getUserName(), u.getUserName());
		Assert.assertEquals(1, u.getProfiles().size());
		Assert.assertEquals(profile.getImsi(), u.getProfiles().get(0).getImsi());
	}
	
}
