package tw.com.mds.fet.femtocellportal.core.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tw.com.mds.fet.femtocellportal.core.AdminUser;
import tw.com.mds.fet.femtocellportal.core.AdministrationService;
import tw.com.mds.fet.femtocellportal.core.Announcement;
import tw.com.mds.fet.femtocellportal.core.ApZone;
import tw.com.mds.fet.femtocellportal.core.Config;
import tw.com.mds.fet.femtocellportal.core.ConfigurableService;
import tw.com.mds.fet.femtocellportal.core.ConfigurationException;
import tw.com.mds.fet.femtocellportal.core.Modifiable;
import tw.com.mds.fet.femtocellportal.core.Permission;
import tw.com.mds.fet.femtocellportal.core.Rnc;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.core.UserLog;
import tw.com.mds.fet.femtocellportal.core.UserType;
import tw.com.mds.fet.femtocellportal.core.dao.AdminUserDao;
import tw.com.mds.fet.femtocellportal.core.dao.AnnouncementDao;
import tw.com.mds.fet.femtocellportal.core.dao.ApZoneDao;
import tw.com.mds.fet.femtocellportal.core.dao.GeneralDao;
import tw.com.mds.fet.femtocellportal.core.dao.RncDao;
import tw.com.mds.fet.femtocellportal.core.dao.UserLogDao;
import tw.com.mds.fet.femtocellportal.dao.PersistentObject;
import tw.com.mds.fet.femtocellportal.translator.TranslatorService;
import tw.com.mds.fet.femtocellportal.util.Utils;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = ServiceException.class)
public class AdministrationServiceImpl implements AdministrationService {

	private GeneralDao generalDao;
	private AnnouncementDao announcementDao;
	private ApZoneDao apZoneDao;
	private RncDao rncDao;
	private UserLogDao userLogDao;
	private AdminUserDao adminUserDao;
	private List<ConfigurableService> configurableServices = new ArrayList<ConfigurableService>();
	private TranslatorService translatorService;

	public PersistentObject findEntityByOid(Long oid, Class<? extends PersistentObject> clazz) throws ServiceException {
		return generalDao.findByOid(oid, clazz);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public PersistentObject removeEntity(PersistentObject entity)
			throws ServiceException {
		PersistentObject refreshed = generalDao.findByOid(entity.getOid(), entity.getClass());
		if (refreshed == null) {
			return null;
		}
		generalDao.remove(refreshed);
		return refreshed;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public PersistentObject saveEntity(PersistentObject entity)
			throws ServiceException {
		if (entity instanceof Modifiable) {
			Modifiable modifiable = (Modifiable) entity;
			Date updateTime = new Date();
			if (entity.getOid() == null) {
				modifiable.setCreateTime(updateTime);
			}
			modifiable.setUpdateTime(updateTime);
		}
		return generalDao.persist(entity);
	}

	public List<Announcement> findPublishedAnnouncements(UserType type, Date atTime)
			throws ServiceException {
		return announcementDao.findPublished(type, atTime);
	}

	public List<Announcement> findAnnouncements(UserType userType,
			String titleKeyword, String contentKeyword) throws ServiceException {
		return announcementDao.findByKeyword(userType, titleKeyword, contentKeyword);
	}

	public List<ApZone> findApZonesByFuzzyName(String name) {
		return apZoneDao.findByFuzzyName(name);
	}

	public List<ApZone> listAllApZones() {
		return apZoneDao.findAll();
	}

	public Rnc findRncByRncId(String rncId) {
		return rncDao.findByRncId(rncId);
	}

	public List<Rnc> findRncsByFuzzyRncIdAndRncName(Rnc criteria) {
		return rncDao.findByFuzzyIdAndName(criteria);
	}

	public List<Rnc> findAllRncs() {
		return rncDao.findAll();
	}

	public List<UserLog> findUserLogByCriteria(UserLog criteria,
			Date startCreateTime, Date endCreateTime) {
		return userLogDao.findByCriteria(criteria, startCreateTime, endCreateTime);
	}

	public List<AdminUser> findAdminUserByCriteria(AdminUser criteria) {
		return adminUserDao.findByCriteria(criteria);
	}

	public List<AdminUser> saveAdminUserPermissions(List<AdminUser> adminUsers) {
		Date now = new Date();
		for (AdminUser au : adminUsers) {
			for (Permission p : au.getPermissions()) {
				if (p.getAdminUser() == null) {
					p.setAdminUser(au);
				}
				if (p.getCreateTime() == null) {
					p.setCreateTime(now);
				}
			}
		}
		return adminUserDao.persist(adminUsers);
	}

	public Map<Integer, Config> getConfigMap() {
		HashMap<Integer, Config> result = new HashMap<Integer, Config>();
		for (ConfigurableService cs : configurableServices) {
			// TODO config: make a copy of config object
			result.put(cs.hashCode(), cs.getConfig());
		}
		return result;
	}

	public void applyConfig(Config config) throws ConfigurationException {
		for (ConfigurableService cs : configurableServices) {
			if (cs.getConfig().getOid() == null || 
					cs.getConfig().getOid().equals(config.getOid())) {
				cs.applyConfig(config);
				return;
			}
		}
		throw new ConfigurationException(Utils.format(
				"no corresponding ConfigurableService found for specified config:{0}", config));
	}

	public boolean checkUnderMaintenance() {
		return translatorService.isUnderMaintenance();
	}

	public AdminUser findAdminUserByUserId(String userId) {
		return adminUserDao.findById(userId);
	}

	public AdminUser findAdminUserByAccount(String account) {
		return adminUserDao.findByAccount(account);
	}

	public AnnouncementDao getAnnouncementDao() {
		return announcementDao;
	}

	public void setAnnouncementDao(AnnouncementDao announcementDao) {
		this.announcementDao = announcementDao;
	}

	public GeneralDao getGeneralDao() {
		return generalDao;
	}

	public void setGeneralDao(GeneralDao generalDao) {
		this.generalDao = generalDao;
	}

	public ApZoneDao getApZoneDao() {
		return apZoneDao;
	}

	public void setApZoneDao(ApZoneDao apZoneDao) {
		this.apZoneDao = apZoneDao;
	}

	public RncDao getRncDao() {
		return rncDao;
	}

	public void setRncDao(RncDao rncDao) {
		this.rncDao = rncDao;
	}

	public UserLogDao getUserLogDao() {
		return userLogDao;
	}

	public void setUserLogDao(UserLogDao userLogDao) {
		this.userLogDao = userLogDao;
	}

	public AdminUserDao getAdminUserDao() {
		return adminUserDao;
	}

	public void setAdminUserDao(AdminUserDao adminUserDao) {
		this.adminUserDao = adminUserDao;
	}

	public List<ConfigurableService> getConfigurableServices() {
		return configurableServices;
	}

	public void setConfigurableServices(
			List<ConfigurableService> configurableServices) {
		this.configurableServices = configurableServices;
	}

	public TranslatorService getTranslatorService() {
		return translatorService;
	}

	public void setTranslatorService(TranslatorService translatorService) {
		this.translatorService = translatorService;
	}

}
