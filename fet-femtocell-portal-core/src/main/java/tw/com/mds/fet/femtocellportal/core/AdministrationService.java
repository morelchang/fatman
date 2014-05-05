package tw.com.mds.fet.femtocellportal.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

import tw.com.mds.fet.femtocellportal.dao.PersistentObject;

public interface AdministrationService {

	public PersistentObject removeEntity(PersistentObject announcement)
			throws ServiceException;

	public PersistentObject saveEntity(PersistentObject announcement)
			throws ServiceException;

	public PersistentObject findEntityByOid(Long oid, Class<? extends PersistentObject> clazz) throws ServiceException;

	public List<Announcement> findPublishedAnnouncements(UserType type,
			Date atTime) throws ServiceException;

	public List<Announcement> findAnnouncements(UserType userType,
			String titleKeyword, String contentKeyword) throws ServiceException;

	public List<ApZone> findApZonesByFuzzyName(String name);

	public List<ApZone> listAllApZones();

	public Rnc findRncByRncId(String rncId);

	public List<Rnc> findRncsByFuzzyRncIdAndRncName(Rnc criteria);

	public List<UserLog> findUserLogByCriteria(UserLog criteria, Date startCreateTime, Date endCreateTime);

	public List<AdminUser> findAdminUserByCriteria(AdminUser criteria);

	public List<AdminUser> saveAdminUserPermissions(List<AdminUser> adminUsers);

	public Map<Integer, Config> getConfigMap();

	/**
	 * a facade interface
	 * 
	 * @deprecated using {@link ConfigurableService#applyConfig(Config)}
	 *             individually
	 * @param config
	 * @throws ConfigurationException
	 */
	public void applyConfig(Config config) throws ConfigurationException;

	public boolean checkUnderMaintenance();

	public AdminUser findAdminUserByUserId(String userId);

	public AdminUser findAdminUserByAccount(String account);

	public List<Rnc> findAllRncs();

}
