package tw.com.mds.fet.femtocellportal.web;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tw.com.mds.fet.femtocellportal.core.AdminUser;
import tw.com.mds.fet.femtocellportal.core.AdministrationService;
import tw.com.mds.fet.femtocellportal.core.DataLoadService;
import tw.com.mds.fet.femtocellportal.core.FemtoProfile;
import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.core.ProvisionService;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.gis.AddressFormatException;
import tw.com.mds.fet.femtocellportal.test.TestData;

@Component
public class FemtocellInitializer {
	
	@Autowired
	private DataLoadService dataLoadService;
	@Autowired
	private ProvisionService provisionService;
	@Autowired
	private AdministrationService administrationService;

	@PostConstruct
	public void init() throws ServiceException {
		dataLoadService.loadApZones();
		dataLoadService.loadAdminUsers();
		dataLoadService.loadRncs();
		dataLoadService.loadCells();
		loadFemtoUserTestData();
	}

	private void loadFemtoUserTestData() throws ServiceException,
			AddressFormatException {
		TestData data = new TestData();
		FemtoUser u1 = data.createFemtoUser("morel chang");
		FemtoUser u2 = data.createFemtoUser("wilson liu");
		FemtoUser u3 = data.createFemtoUser("isaac liao");

		FemtoProfile p1 = data.createProfile("1111111", "09201111111");
		p1.setApZone(administrationService.listAllApZones().get(0));

		FemtoProfile p2 = data.createProfile("2222222", "09202222222");
		p2.setApZone(administrationService.listAllApZones().get(1));

		FemtoProfile p3 = data.createProfile("3333333", "0920333333");
		p3.setApZone(administrationService.listAllApZones().get(1));

		AdminUser system = data.createAdminUser("system", "system");
		data.createAdminUser("AR0820", "morelchang");
		
		u1 = provisionService.createUser(u1, p1, system);
		provisionService.createUser(u1, p2, system);
		provisionService.createUser(u1, p3, system);
		
		FemtoProfile p4 = data.createProfile("4444444", "0920444444");
		p4.setApZone(administrationService.listAllApZones().get(1));

		FemtoProfile p5 = data.createProfile("5555555", "0920555555");
		p5.setApZone(administrationService.listAllApZones().get(1));

		u2 = provisionService.createUser(u2, p4, system);
		provisionService.createUser(u2, p5, system);
		
		FemtoProfile p6 = data.createProfile("6666666", "0920666666");
		p6.setApZone(administrationService.listAllApZones().get(1));

		provisionService.createUser(u3, p6, system);
	}

}
