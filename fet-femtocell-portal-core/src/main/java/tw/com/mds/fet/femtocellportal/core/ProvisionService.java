package tw.com.mds.fet.femtocellportal.core;

import java.util.List;

import tw.com.mds.fet.femtocellportal.gis.AddressFormatException;
import tw.com.mds.fet.femtocellportal.gis.NoCellFoundException;

public interface ProvisionService {

	public AdminUser loginAdminUser(String account, String password,
			String sourceIp) throws ServiceException;

	public FemtoUser loginFemtoUser(String account, String password,
			String sourceIp) throws ServiceException;

	public FemtoUser findUserByOid(Long oid);

	public List<FemtoUser> findUsers(String userName);

	public List<FemtoUser> findUsersByMobile(String mobile);

	public FemtoUser findUserByProfile(String apei, String imsi);

	public List<FemtoUser> listAllUsers();

	public FemtoProfile findProfileByOid(Long oid) throws ServiceException;

	public FemtoProfile findProfileByApei(String apei);

	public FemtoProfile findProfileByImsi(String imsi);

	public List<FemtoProfile> findProfileByApZone(ApZone apZone);

	public FemtoProfile syncPermissionList(FemtoProfile profile) throws ServiceException;
	
	public FemtoUser createUser(FemtoUser user, FemtoProfile profile,
			AdminUser operator) throws ServiceException, AddressFormatException, NoCellFoundException;

	public FemtoProfile createProfile(FemtoUser user, FemtoProfile profile,
			AdminUser operator) throws ServiceException, AddressFormatException, NoCellFoundException;

	public FemtoProfile savePermissionList(FemtoProfile profile,
			LoginUser operator) throws ServiceException;

	public FemtoProfile changeProfile(FemtoProfile oldProfile,
			FemtoProfile newProfile, AdminUser operator)
			throws ServiceException;

	public FemtoProfile suspendFemtoProfile(FemtoProfile profile,
			AdminUser operator) throws ServiceException;

	public FemtoProfile resumeFemtoProfile(FemtoProfile profile,
			AdminUser operator) throws ServiceException;

	public FemtoProfile removeProfileByOid(Long oid, AdminUser operator)
			throws ServiceException;
	
	public void updateProfileLocation() throws ServiceException; 
	
	public FemtoProfile changeProfileLocation(FemtoProfile profile)
			throws ServiceException, NoCellFoundException;

	public int getMaxPermissionListSize() throws ServiceException;

	public Integer getDefaultMaxUserEquipmentSize() throws ServiceException;

	public String getGisLocationLink();

	public String getSsoLoginUrl();

	public boolean isEnableSsoAuthentication();

	public String getSsoMsisdnHeaderKey();

	public boolean isEnableLocalAuthentication();

	public boolean isDefaultListAll();

	public String getSsoLogoutUrl();

	public FemtoUser findUserByAccount(String account);
	
}
