package tw.com.mds.fet.femtocellportal.core.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import tw.com.mds.fet.femtocellportal.ahr.impl.StubAhrServiceImpl;
import tw.com.mds.fet.femtocellportal.core.FemtoProfile;
import tw.com.mds.fet.femtocellportal.core.UserEquipment;
import tw.com.mds.fet.femtocellportal.test.TestData;

public class ProvisionServiceImplTest {

	private ProvisionServiceImpl service = new ProvisionServiceImpl();
	
	@Test
	public void testSyncPermissionListInternal() throws Exception {
		FemtoProfile profile = new TestData().createProfile("apei", "imsi");
		List<UserEquipment> permissionList = new StubAhrServiceImpl().queryPermissionList(profile);
		boolean changed = service.syncPermissionListInternal(profile, permissionList);
		Assert.assertTrue(changed);
		Assert.assertEquals(permissionList.size(), profile.getPermissionList().size());
		
		changed = service.syncPermissionListInternal(profile, profile.getPermissionList());
		Assert.assertFalse(changed);
		
		profile.getPermissionList().add(new UserEquipment("a test", "a test"));
		Assert.assertEquals(permissionList.size() + 1, profile.getPermissionList().size());
		changed = service.syncPermissionListInternal(profile, permissionList);
		Assert.assertTrue(changed);
		Assert.assertEquals(permissionList.size(), profile.getPermissionList().size());
	}
}
