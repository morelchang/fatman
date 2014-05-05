package tw.com.mds.fet.femtocellportal.core.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tw.com.mds.fet.femtocellportal.ahr.AhrException;
import tw.com.mds.fet.femtocellportal.ahr.AhrService;
import tw.com.mds.fet.femtocellportal.core.AdminUser;
import tw.com.mds.fet.femtocellportal.core.ApZone;
import tw.com.mds.fet.femtocellportal.core.Cell;
import tw.com.mds.fet.femtocellportal.core.Config;
import tw.com.mds.fet.femtocellportal.core.ConfigurableService;
import tw.com.mds.fet.femtocellportal.core.ConfigurationException;
import tw.com.mds.fet.femtocellportal.core.FemtoProfile;
import tw.com.mds.fet.femtocellportal.core.FemtoState;
import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.core.LocationDetectMode;
import tw.com.mds.fet.femtocellportal.core.LoginUser;
import tw.com.mds.fet.femtocellportal.core.ProvisionService;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.core.UePermissionMode;
import tw.com.mds.fet.femtocellportal.core.UserEquipment;
import tw.com.mds.fet.femtocellportal.core.dao.AdminUserDao;
import tw.com.mds.fet.femtocellportal.core.dao.FemtoProfileDao;
import tw.com.mds.fet.femtocellportal.core.dao.FemtoUserDao;
import tw.com.mds.fet.femtocellportal.gis.AddressFormatException;
import tw.com.mds.fet.femtocellportal.gis.GisException;
import tw.com.mds.fet.femtocellportal.gis.GisService;
import tw.com.mds.fet.femtocellportal.gis.NoCellFoundException;
import tw.com.mds.fet.femtocellportal.gis.PositionResult;
import tw.com.mds.fet.femtocellportal.translator.TranslatorService;
import tw.com.mds.fet.femtocellportal.util.Utils;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = ServiceException.class)
public class ProvisionServiceImpl implements ProvisionService, ConfigurableService {

	private static final Log logger = LogFactory.getLog(ProvisionServiceImpl.class);
	
	private TranslatorService translatorService;
	private AhrService ahrService;
	private GisService gisService;
	private FemtoUserDao femtoUserDao;
	private FemtoProfileDao femtoProfileDao;
	private AdminUserDao adminUserDao;
	private ProvisionConfigDao provisionConfigDao;
	
	private ProvisionConfig config;

	public void init() throws ConfigurationException {
		reloadConfig();
	}
	
	public AdminUser loginAdminUser(String account, String password, String sourceIp)
			throws ServiceException {
		AdminUser adminUser = adminUserDao.findByAccount(account);
		if (adminUser != null && adminUser.getPassword() != null
				&& !adminUser.getPassword().equals(password)) {
			adminUser = null;
		}
		return adminUser;
	}

	public FemtoUser loginFemtoUser(String account, String password, String sourceIp)
			throws ServiceException {
		FemtoUser femtoUser = femtoUserDao.findByAccount(account);
		if (femtoUser != null && femtoUser.getPassword() != null
				&& !femtoUser.getPassword().equals(password)) {
			femtoUser = null;
		}
		return femtoUser;
	}

	public FemtoUser findUserByOid(Long oid) {
		return femtoUserDao.findByOid(oid);
	}

	public List<FemtoUser> findUsers(String userName) {
		return femtoUserDao.findByUserName(userName);
	}
	
	public List<FemtoUser> findUsersByMobile(String mobile) {
		return femtoUserDao.findByMobile(mobile);
	}

	public FemtoUser findUserByProfile(String apei, String imsi) {
		// find profile
		FemtoProfile profile = null;
		if (!StringUtils.isEmpty(apei)) {
			profile = femtoProfileDao.findByApei(apei);
		} else {
			profile = femtoProfileDao.findByImsi(imsi);
		}
		
		// get user of the profile
		if (profile != null) {
			return profile.getUser();
		}
		return null;
	}

	public List<FemtoUser> listAllUsers() {
		return femtoUserDao.findAll();
	}

	public FemtoProfile findProfileByOid(Long oid) throws ServiceException {
		return femtoProfileDao.findByOid(oid);
	}

	public FemtoProfile findProfileByApei(String apei) {
		return femtoProfileDao.findByApei(apei);
	}

	public FemtoProfile findProfileByImsi(String imsi) {
		return femtoProfileDao.findByImsi(imsi);
	}

	public List<FemtoProfile> findProfileByApZone(ApZone apZone) {
		return femtoProfileDao.findByApZone(apZone);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public FemtoProfile syncPermissionList(FemtoProfile profile) throws AhrException {
		List<UserEquipment> permissionList = ahrService.queryPermissionList(profile);
		boolean changed = syncPermissionListInternal(profile, permissionList);
		if (changed) {
			return femtoProfileDao.persist(profile);
		}
		return profile;
	}

	boolean syncPermissionListInternal(FemtoProfile profile,
			List<UserEquipment> permissionList) {
		boolean localChanged = false;
		List<UserEquipment> synced = new ArrayList<UserEquipment>();
		for (UserEquipment remote : permissionList) {
			boolean notFound = true;
			for (UserEquipment local : profile.getPermissionList()) {
				if (remote.getMsisdn().equals(local.getMsisdn()) &&
						remote.getImsi().equals(local.getImsi())) {
					synced.add(local);
					notFound = false;
				}
			}
			
			if (notFound) {
				localChanged = true;
				synced.add(remote);
			}
		}
		
		if (!localChanged && synced.size() != profile.getPermissionList().size()) {
			localChanged = true;
		}
		
		if (localChanged) {
			profile.setPermissionList(synced);
			linkPermissionListRelationship(profile);
		}
		return localChanged;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public FemtoUser createUser(FemtoUser user, FemtoProfile profile,
			AdminUser operator) throws ServiceException, AddressFormatException, NoCellFoundException {
		FemtoUser savedUser = saveFemtoUser(user);
		createProfile(savedUser, profile, operator);
		return femtoUserDao.refresh(savedUser);
	}

	private FemtoUser saveFemtoUser(FemtoUser user) {
		Date saveTime = new Date();
		if (user.getOid() == null) {
			user.setCreateTime(saveTime);
		}
		user.setUpdateTime(saveTime);
		FemtoUser persistedUser = femtoUserDao.persist(user);
		return persistedUser;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public FemtoProfile createProfile(FemtoUser user, FemtoProfile profile, AdminUser operator)
			throws ServiceException, NoCellFoundException {
		linkPermissionListRelationship(profile);
		translateImsi(profile);
		ahrService.addProfile(user, profile);
		try {
			syncProfileLocationInfo(profile);
			return saveFemtoProfile(profile, user);
		} catch(RuntimeException e) {
			rollbackAhrAddProfile(profile);
			throw e;
		} catch(ServiceException e) {
			rollbackAhrAddProfile(profile);
			throw e;
		}
	}

	private void rollbackAhrAddProfile(FemtoProfile profile) {
		// rollback if any exception
		try {
			ahrService.deleteProfile(profile);
			Utils.info(logger, "rollbacked AHR addPorfile:{0}", profile);
		} catch (Exception e) {
			Utils.error(logger, "failed to rollback ahr addProfile:{0}", profile);
		}
	}

	private void linkPermissionListRelationship(FemtoProfile profile) {
		for (UserEquipment ue : profile.getPermissionList()) {
			ue.setProfile(profile);
		}
	}

	private void translateImsi(FemtoProfile profile) throws ServiceException {
		if (profile.getUePermissionMode() == UePermissionMode.OPEN) {
			profile.setPermissionList(new ArrayList<UserEquipment>());
			return;
		}
		
		for (UserEquipment ue : profile.getPermissionList()) {
			ue.setImsi(translatorService.queryImsiByMsisdn(ue.getMsisdn()));
		}
	}

	private void syncProfileLocationInfo(FemtoProfile profile) throws GisException,
			AddressFormatException, NoCellFoundException, AhrException {
		// do not query location if the position is specified 
		if (profile.getPosition() == null
				|| profile.getPosition().getLatitude() == null
				|| profile.getPosition().getLongitude() == null) {
			PositionResult posResult = gisService.queryPositionByAddress(profile.getAddress());
			profile.setPosition(posResult.getPosition());
			profile.setLocatingState(posResult.getLocatingState());
		}
		
		if (profile.getLocationDetectMode() != LocationDetectMode.NOT_PERFORMED) {
			List<Cell> cells = gisService.queryNearestCellsByPosition(profile.getPosition(), profile.getLocationDetectMode());
			profile.setCells(cells);
		}
		
		ahrService.setLocation(profile);
	}

	private FemtoProfile saveFemtoProfile(FemtoProfile profile, FemtoUser user) {
		Date saveTime = new Date();
		
		// profile state
		if (profile.getOid() == null) {
			profile.setCreateTime(saveTime);
		}
		profile.setUpdateTime(saveTime);
		if (profile.getState() == null) {
			profile.setState(FemtoState.ACTIVE);
		}
		
		// persist
		profile.setUser(user);
		return femtoProfileDao.persist(profile);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public FemtoProfile changeProfile(FemtoProfile oldProfile,
			FemtoProfile newProfile, AdminUser operator)
			throws ServiceException, GisException, AhrException,
			AddressFormatException {
		boolean ahrOldDeleted = false;
		boolean ahrNewCreated = false;
		oldProfile = femtoProfileDao.findByOid(oldProfile.getOid());
		FemtoUser user = femtoUserDao.findByOid(oldProfile.getUser().getOid());
		try {
			// delete ahr profile
			ahrService.deleteProfile(oldProfile);
			ahrOldDeleted = true;
			
			// create ahr new profile
			linkPermissionListRelationship(newProfile);
			translateImsi(newProfile);
			ahrService.addProfile(user, newProfile);
			ahrNewCreated = true;
			syncProfileLocationInfo(newProfile);

			// local remove profile
			femtoProfileDao.remove(oldProfile);
			femtoProfileDao.flush();
			
			// local create profile
//			newProfile.setOid(null);
			newProfile.setCreateTime(oldProfile.getCreateTime());
			newProfile.setUpdateTime(new Date());
			FemtoProfile savedProfile = femtoProfileDao.persist(newProfile);
			return savedProfile;
		} catch (RuntimeException e) {
			if (ahrNewCreated) {
				rollbackAhrAddProfile(newProfile);
			}
			if (ahrOldDeleted) {
				rollbackAhrDeleteProfile(user, oldProfile);
			}
			throw e;
		} catch (ServiceException e) {
			if (ahrNewCreated) {
				rollbackAhrAddProfile(newProfile);
			}
			if (ahrOldDeleted) {
				rollbackAhrDeleteProfile(user, oldProfile);
			}
			throw e;
		}
	}

	private void rollbackAhrDeleteProfile(FemtoUser user, FemtoProfile profile) {
		try {
			ahrService.addProfile(user, profile);
			Utils.info(logger, "rollbacked AHR deletePorfile:{0}", profile);
		} catch (Exception e1) {
			Utils.error(logger, "failed to rollback ahr deleteProfile:{0}", profile);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public FemtoProfile suspendFemtoProfile(FemtoProfile profile, AdminUser operator)
			throws ServiceException {
		if (!FemtoState.ACTIVE.equals(profile.getState())) {
			throw new ServiceException(Utils.format(
					"incorrect profile.state:{0}, valid is:{1}", profile.getState(), FemtoState.ACTIVE));
		}
		Date updateTime = new Date();
		ahrService.suspend(profile);
		
		profile.setUpdateTime(updateTime);
		profile.setState(FemtoState.SUSPENDED);
		return femtoProfileDao.persist(profile);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public FemtoProfile resumeFemtoProfile(FemtoProfile profile, AdminUser operator)
			throws ServiceException {
		if (!FemtoState.SUSPENDED.equals(profile.getState())) {
			throw new ServiceException(Utils.format(
					"incorrect profile.state:{0}, valid is:{1}", profile.getState(), FemtoState.SUSPENDED));
		}
		Date updateTime = new Date();
		ahrService.resume(profile);
		
		profile.setUpdateTime(updateTime);
		profile.setState(FemtoState.ACTIVE);
		return femtoProfileDao.persist(profile);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public FemtoProfile savePermissionList(FemtoProfile profile, LoginUser operator)
			throws ServiceException {
		linkPermissionListRelationship(profile);
		translateImsi(profile);

		Date updateTime = new Date();
		ahrService.modifyPermissionList(profile, updateTime);

		profile.setUpdateTime(updateTime);
		femtoProfileDao.findByOid(profile.getOid());
		return femtoProfileDao.persist(profile);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public FemtoProfile removeProfileByOid(Long oid, AdminUser operator) throws ServiceException {
		FemtoProfile removingProfile = femtoProfileDao.findByOid(oid);
		if (removingProfile == null) {
			return null;
		}
		
		// remove profile
		FemtoUser user = femtoUserDao.findByOid(removingProfile.getUser().getOid());
		ahrService.deleteProfile(removingProfile);
		try {
			femtoProfileDao.remove(removingProfile);
			femtoProfileDao.flush();
			
			// remove user having no profile
			user = femtoUserDao.refresh(user);
			if (user.getProfiles().isEmpty()) {
				femtoUserDao.remove(user);
			}
			femtoUserDao.flush();
			return removingProfile;
		} catch (RuntimeException e) {
			rollbackAhrDeleteProfile(user, removingProfile);
			throw e;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public void updateProfileLocation() throws ServiceException {
		Utils.info(logger, "starting profile location update:");
		
		// re-sync femto profile cells from gis
		int totalCount = 0;
		int errorCount = 0;
		List<FemtoProfile> allprofile = femtoProfileDao.findAll();
		for (FemtoProfile p : allprofile) {
			try {
				changeProfileLocation(p);
				totalCount++;
				if (totalCount % 1000 == 0) {
					femtoProfileDao.flushAndClear();
					Utils.debug(logger, "updating profile location... {0}", totalCount);
				}
			} catch (Exception e) {
				errorCount++;
				Utils.error(logger, "failed to change profile location:{0}, profile:{1}", 
						e, p, e.getMessage());
			}
		}
		femtoProfileDao.flushAndClear();
		Utils.info(logger, "completed profile location update, total:{0}, error:{1}", totalCount, errorCount);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public FemtoProfile changeProfileLocation(FemtoProfile profile)
			throws ServiceException, NoCellFoundException {
		syncProfileLocationInfo(profile);
		return femtoProfileDao.persist(profile);
	}

	public void reloadConfig() throws ConfigurationException {
		ProvisionConfig loaded = provisionConfigDao.load();
		if (loaded == null) {
			Utils.warn(logger, "no ProvisionConfig found, using current config:{0}", config);
			return;
		}
		
		loaded.validate();
		this.config = loaded;
		Utils.info(logger, "reloaded ProvisionConfig:{0}", loaded);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = ServiceException.class)
	public void applyConfig(Config config) throws ConfigurationException {
		ProvisionConfig newConfig = (ProvisionConfig) config;
		newConfig.validate();
		
		newConfig.setOid(this.config.getOid());
		newConfig.setUpdateTime(new Date());
		
		newConfig = provisionConfigDao.save(newConfig);
		this.config = newConfig;
		Utils.info(logger, "applied ProvisionConfig:{0}", newConfig);
	}

	public int getMaxPermissionListSize() throws ServiceException {
		return ahrService.getMaxPermissionListSize();
	}

	public Integer getDefaultMaxUserEquipmentSize() throws ServiceException {
		return config.getDefaultMaxUserEquipmentSize();
	}

	public String getGisLocationLink() {
		return gisService.getGisLocationLink();
	}

	public String getSsoLoginUrl() {
		return config.getSsoLoginUrl();
	}

	public boolean isEnableSsoAuthentication() {
		Boolean enable = config.getEnableSsoAuthentication();
		if (enable != null) {
			return enable;
		}
		return false;
	}

	public String getSsoMsisdnHeaderKey() {
		return config.getSsoMsisdnHeaderKey();
	}

	public boolean isEnableLocalAuthentication() {
		Boolean enable = config.getEnableLocalAuthentication();
		if (enable != null) {
			return enable;
		}
		return false;
	}

	public boolean isDefaultListAll() {
		Boolean defaultListAll = config.getDefaultListAll();
		if (defaultListAll == null) {
			return false;
		}
		return defaultListAll;
	}

	public String getSsoLogoutUrl() {
		return config.getSsoLogoutUrl();
	}

	public FemtoUser findUserByAccount(String account) {
		return femtoUserDao.findByAccount(account);
	}

	public FemtoUserDao getFemtoUserDao() {
		return femtoUserDao;
	}

	public void setFemtoUserDao(FemtoUserDao femtoUserDao) {
		this.femtoUserDao = femtoUserDao;
	}

	public FemtoProfileDao getFemtoProfileDao() {
		return femtoProfileDao;
	}

	public void setFemtoProfileDao(FemtoProfileDao femtoProfileDao) {
		this.femtoProfileDao = femtoProfileDao;
	}

	public TranslatorService getTranslatorService() {
		return translatorService;
	}

	public void setTranslatorService(TranslatorService translatorService) {
		this.translatorService = translatorService;
	}

	public GisService getGisService() {
		return gisService;
	}

	public void setGisService(GisService gisService) {
		this.gisService = gisService;
	}

	public AdminUserDao getAdminUserDao() {
		return adminUserDao;
	}

	public void setAdminUserDao(AdminUserDao adminUserDao) {
		this.adminUserDao = adminUserDao;
	}

	public AhrService getAhrService() {
		return ahrService;
	}

	public void setAhrService(AhrService ahrService) {
		this.ahrService = ahrService;
	}

	public ProvisionConfig getConfig() {
		try {
			return (ProvisionConfig) BeanUtils.cloneBean(config);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setConfig(ProvisionConfig config) {
		this.config = config;
	}

	public ProvisionConfigDao getProvisionConfigDao() {
		return provisionConfigDao;
	}

	public void setProvisionConfigDao(ProvisionConfigDao provisionConfigDao) {
		this.provisionConfigDao = provisionConfigDao;
	}

}
