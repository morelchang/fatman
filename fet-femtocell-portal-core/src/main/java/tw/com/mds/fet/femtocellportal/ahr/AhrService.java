package tw.com.mds.fet.femtocellportal.ahr;

import java.util.Date;
import java.util.List;

import tw.com.mds.fet.femtocellportal.core.FemtoProfile;
import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.core.UserEquipment;

public interface AhrService {

	public void addProfile(FemtoUser user, FemtoProfile profile) throws AhrException;
	
	public void deleteProfile(FemtoProfile profile) throws AhrException;
	
	public void setLocation(FemtoProfile profile) throws AhrException;
	
	public List<UserEquipment> queryPermissionList(FemtoProfile profile) throws AhrException;
	
	public void modifyPermissionList(FemtoProfile profile, Date updateTime) throws AhrException;

	public int getMaxPermissionListSize() throws AhrException;

	public void suspend(FemtoProfile profile) throws AhrException;
	
	public void resume(FemtoProfile profile) throws AhrException;
	
}
