package tw.com.mds.fet.femtocellportal.test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tw.com.mds.fet.femtocellportal.core.AdminUser;
import tw.com.mds.fet.femtocellportal.core.ApZone;
import tw.com.mds.fet.femtocellportal.core.Cell;
import tw.com.mds.fet.femtocellportal.core.CellType;
import tw.com.mds.fet.femtocellportal.core.ConnectionMode;
import tw.com.mds.fet.femtocellportal.core.FemtoProfile;
import tw.com.mds.fet.femtocellportal.core.FemtoState;
import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.core.LocationDetectMode;
import tw.com.mds.fet.femtocellportal.core.Modifiable;
import tw.com.mds.fet.femtocellportal.core.Position;
import tw.com.mds.fet.femtocellportal.core.Rnc;
import tw.com.mds.fet.femtocellportal.core.UePermissionMode;
import tw.com.mds.fet.femtocellportal.core.UserEquipment;
import tw.com.mds.fet.femtocellportal.core.dao.GeneralDao;
import tw.com.mds.fet.femtocellportal.dao.PersistentObject;
import tw.com.mds.fet.femtocellportal.translator.impl.FetTranslatorConfig;

@Component
public class TestData {
	
	@Autowired
	private GeneralDao dao;
	private int sequence = 0;

	public ApZone createApZone(String name) {
		ApZone a = new ApZone(name);
		return tryPersistAndClearCache(a);
	}

	@SuppressWarnings("unchecked")
	private <T extends PersistentObject> T tryPersistAndClearCache(T a) {
		if (a instanceof Modifiable) {
			modify((Modifiable) a);
		}
		if (dao != null) {
			a = (T) dao.persist(a);
			dao.flushAndClear();
		}
		return a;
	}

	private Modifiable modify(Modifiable m) {
		Date now = new Date();
		m.setCreateTime(now);
		m.setUpdateTime(now);
		return m;
	}

	public FemtoProfile createProfile(String apei, String imsi) {
		FemtoUser user = createFemtoUser("userName" + apei);
		return createProfile(user, apei, imsi);
	}

	public FemtoProfile createProfile(FemtoUser user, String apei, String imsi) {
		FemtoProfile p = new FemtoProfile();
		p.setApei(apei);
		p.setImsi(imsi);
		p.setAddress("台北市信義區忠孝東路四段100號10樓");
		p.setApZone(createApZone("test4"));
//		Cell c = createCell("1");
//		c.setPlmId("46601");
//		p.setCells(Arrays.asList(c));
		p.setPosition(new Position(new BigDecimal("10"), new BigDecimal("10")));
		p.setLocationDetectMode(LocationDetectMode.NOT_PERFORMED);
		p.setMaxUserCount(4);
		p.setMaxPermissionListSize(32);
		p.setUePermissionMode(UePermissionMode.CLOSE);
		p.addPermissionList(buildUerEquipment("0910112233", "0910112233"));
		p.addPermissionList(buildUerEquipment("0910223344", "0910223344"));
		p.setState(FemtoState.ACTIVE);
		p.setUser(user);
		p.setConnectionMode(ConnectionMode.PPPOE);
		return tryPersistAndClearCache(p);
	}

	private UserEquipment buildUerEquipment(String msisdn, String imsi) {
		UserEquipment e = new UserEquipment(msisdn, imsi);
		return e;
	}
	
	public Cell createCell(String id) {
		Rnc rnc = createRnc(nextId(), "rnc name");
		Position pos = buildPosition();
		return createCell(rnc, id, pos);
	}
	
	public Cell createCell(Rnc rnc, String id) {
		Position pos = buildPosition();
		return createCell(rnc, id, pos);
	}

	public Cell createCell(Rnc rnc, String id, Position pos) {
		Cell c = new Cell();
		c.setCellId(id);
		c.setLacId("2");
		c.setPlmId("3");
		c.setRnc(rnc);
		c.setType(CellType.TwoG);
		c.setPosition(pos);
		Position tw97p = pos.toTw97();
		c.setLonIdx(tw97p.getLongitude().intValue());
		c.setLatIdx(tw97p.getLatitude().intValue());
		return tryPersistAndClearCache(c);
	}

	public Rnc createRnc(String rncId, String rncName) {
		Rnc rnc = new Rnc(rncId, rncName);
		return tryPersistAndClearCache(rnc);
	}

	public FemtoUser createFemtoUser(String userName) {
		FemtoUser e = new FemtoUser();
		e.setUserName(userName);
		e.setAccount(userName);
		e.setCardId("cardID");
		e.setMobile(String.valueOf(userName.hashCode()));
		e.setPhone("26267711");
		e.setZipCode("123");
		return tryPersistAndClearCache(e);
	}

	public Position buildPosition() {
		Position pos;
		double longitude = new Random().nextDouble() * 100;
		double latitude = new Random().nextDouble() * 100;
		pos = new Position(BigDecimal.valueOf(longitude).round(new MathContext(5)), BigDecimal.valueOf(latitude).round(new MathContext(5)));
		return pos;
	}
	
	private String nextId() {
		return String.valueOf(sequence++);
	}

	public AdminUser createAdminUser() {
		return createAdminUser("userId" + nextId(), "account");
	}

	public AdminUser createAdminUser(String userId, String account) {
		AdminUser adminUser = new AdminUser(userId, account);
		adminUser.setEmail("email@email.com.tw");
		adminUser.setUserName("John Doe - " + userId);
		return tryPersistAndClearCache(adminUser);
	}

	public FetTranslatorConfig createFetTranslatorConfig() {
		FetTranslatorConfig c = new FetTranslatorConfig();
		c.setChannelIdF("channelIdF");
		c.setEnableMaintananceTimeCheck(true);
		c.setEntity("entity");
		c.setMaintenanceHourFrom(0);
		c.setMaintenanceHourTo(1);
		c.setPasswordF("passwordF");
		c.setPasswordK("passwordK");
		c.setSgwA004Url("sgwA004Url");
		c.setSgwA015Url("sgwA015Url");
		c.setUserIdF("userIdF");
		c.setUserNameF("userNameF");
		c.setUserNameK("userNameK");
		c.setXmlEncoding("xmlEncoding");
		c.setUpdateTime(new Date());
		return tryPersistAndClearCache(c);
	}
}
