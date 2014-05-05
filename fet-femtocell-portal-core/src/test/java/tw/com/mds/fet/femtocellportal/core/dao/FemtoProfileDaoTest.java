package tw.com.mds.fet.femtocellportal.core.dao;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import tw.com.mds.fet.femtocellportal.core.ApZone;
import tw.com.mds.fet.femtocellportal.core.Cell;
import tw.com.mds.fet.femtocellportal.core.FemtoProfile;
import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.core.UserEquipment;
import tw.com.mds.fet.femtocellportal.test.DefaultTestCase;

public class FemtoProfileDaoTest extends DefaultTestCase {

	@Autowired
	private FemtoProfileDao dao;

	@Autowired
	private CellDao cellDao;

	@Autowired
	private ApZoneDao apZoneDao;

	@Autowired
	private FemtoUserDao femtoUserDao;
	
	@Test
	public void testSave() {
		FemtoProfile a = createFemtoProfile();
		dao.flushAndClear();
		Assert.assertNotNull(a.getUser());
		Assert.assertNotNull(a.getApZone());
		Assert.assertFalse(a.getCells().isEmpty());
		Assert.assertFalse(a.getPermissionList().isEmpty());
		
		a.getPermissionList().clear();
		a = dao.persist(a);
		dao.flushAndClear();
		Assert.assertTrue(a.getPermissionList().isEmpty());
	}

	private FemtoProfile createFemtoProfile() {
		Cell c1 = cellDao.persist(data.createCell("1"));
		ApZone z1 = apZoneDao.persist(data.createApZone("z1"));
		FemtoUser u1 = femtoUserDao.persist(data.createFemtoUser("u1"));
		
		FemtoProfile p = data.createProfile(data.createFemtoUser("user"), "apei", "imsi");
		p.setUser(u1);
		p.setApZone(z1);
		p.setCells(Arrays.asList(c1));
		
		FemtoProfile a = dao.persist(p);
		return a;
	}
	
	@Test
	public void testPermissionList() {
		FemtoProfile p = createFemtoProfile();
		p.getPermissionList().clear();
		
		p = dao.persist(p);
		dao.flushAndClear();
		
		p.addPermissionList(new UserEquipment("1", "1"));
		p.addPermissionList(new UserEquipment("2", "2"));
		p.addPermissionList(new UserEquipment("3", "3"));
		p = dao.persist(p);
		dao.flushAndClear();
		
		p.getPermissionList().remove(0);
		p.getPermissionList().remove(0);
		p.addPermissionList(new UserEquipment("4", "4"));
		p = dao.persist(p);
		dao.flushAndClear();
		
		Assert.assertEquals(2, p.getPermissionList().size());
	}

	@Test
	public void testFindByImsi() throws Exception {
		FemtoProfile p = createFemtoProfile();
		Assert.assertEquals(p, dao.findByImsi(p.getImsi()));
	}
	
	@Test
	public void testFindHavingCells() throws Exception {
		List<Cell> cells = Arrays.asList(data.createCell("1"), data.createCell("2"), data.createCell("3"), data.createCell("4"));
		List<FemtoProfile> profiles = Arrays.asList(data.createProfile("1", "1"), data.createProfile("2", "2"), data.createProfile("3", "3"));
		profiles.get(0).setCells(cells.subList(0, 2));
		profiles.get(1).setCells(cells.subList(1, 3));
		profiles.get(2).setCells(Arrays.asList(cells.get(0), cells.get(2)));
		profiles = dao.persist(profiles);
		dao.flushAndClear();
		
		Assert.assertEquals(
				Arrays.asList(profiles.get(0), profiles.get(2)),
				dao.findHavingCells(Arrays.asList(cells.get(0))));
		
		Assert.assertEquals(
				Arrays.asList(profiles.get(0), profiles.get(1)),
				dao.findHavingCells(Arrays.asList(cells.get(1))));

		Assert.assertEquals(
				Arrays.asList(profiles.get(1), profiles.get(2)),
				dao.findHavingCells(Arrays.asList(cells.get(2))));
		
		Assert.assertEquals(
				profiles,
				dao.findHavingCells(cells.subList(0, 2)));

		Assert.assertEquals(
				profiles,
				dao.findHavingCells(cells.subList(1, 3)));

		Assert.assertEquals(
				profiles,
				dao.findHavingCells(Arrays.asList(cells.get(0), cells.get(2))));

		Assert.assertTrue(
				dao.findHavingCells(Arrays.asList(cells.get(3))).isEmpty());

		Assert.assertEquals(
				profiles,
				dao.findHavingCells(cells));
	}
	
}
