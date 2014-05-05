package tw.com.mds.fet.femtocellportal.ahr.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.mds.fet.femtocellportal.ahr.AhrException;
import tw.com.mds.fet.femtocellportal.ahr.AhrService;
import tw.com.mds.fet.femtocellportal.core.FemtoProfile;
import tw.com.mds.fet.femtocellportal.core.FemtoUser;
import tw.com.mds.fet.femtocellportal.core.UserEquipment;
import tw.com.mds.fet.femtocellportal.util.Utils;

public class StubAhrServiceImpl implements AhrService {
	
	private static final Log logger = LogFactory.getLog(StubAhrServiceImpl.class);
	
	private Map<String, FemtoProfile> cache = new HashMap<String, FemtoProfile>();

	public void addProfile(FemtoUser user, FemtoProfile profile) throws AhrException {
		FemtoProfile exists = cache.get(profile.getApei());
		if (exists != null) {
			throw new AhrException(Utils.format("APEI:{0} duplicated", profile.getApei()));
		}
		cache.put(profile.getApei(), profile);
		Utils.debug(logger, "apei:{0} added", profile.getApei());
	}

	public void deleteProfile(FemtoProfile profile) throws AhrException {
		checkExists(profile);
		cache.remove(profile.getApei());
		Utils.debug(logger, "apei:{0} removed", profile.getApei());
	}

	public void setLocation(FemtoProfile profile) throws AhrException {
		checkExists(profile);
		cache.put(profile.getApei(), profile);
		Utils.debug(logger, "apei:{0} location set", profile.getApei());
	}

	private void checkExists(FemtoProfile profile) throws AhrException {
		FemtoProfile exists = cache.get(profile.getApei());
		if (exists == null) {
			throw new AhrException(Utils.format("APEI:{0} not found", profile.getApei()));
		}
	}

	public List<UserEquipment> queryPermissionList(FemtoProfile profile)
			throws AhrException {
		checkExists(profile);
		List<UserEquipment> result = new ArrayList<UserEquipment>();
		result.add(new UserEquipment("1234567890", "023456789012345"));
		result.add(new UserEquipment("2234567890", "123456789012346"));
		result.add(new UserEquipment("3234567890", "123456789012347"));
		Utils.debug(logger, "apei:{0} query permission list with fiexed value:{1}", 
				profile.getApei(), result);
		return result;
	}

	public void modifyPermissionList(FemtoProfile profile, Date updateTime)
			throws AhrException {
		checkExists(profile);
		cache.put(profile.getApei(), profile);
		Utils.debug(logger, "apei:{0} modified permission list", profile.getApei());
	}

	public void suspend(FemtoProfile profile) throws AhrException {
		return;
	}

	public void resume(FemtoProfile profile) throws AhrException {
		return;
	}

	public int getMaxPermissionListSize() {
		return 32;
	}

}
