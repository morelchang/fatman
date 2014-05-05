package tw.com.mds.fet.femtocellportal.core.dao;

import java.util.List;

import tw.com.mds.fet.femtocellportal.core.ApZone;
import tw.com.mds.fet.femtocellportal.core.Cell;
import tw.com.mds.fet.femtocellportal.core.FemtoProfile;
import tw.com.mds.fet.femtocellportal.dao.GenericDao;

/**
 * femto profile dao
 * 
 * @author morel
 *
 */
public interface FemtoProfileDao extends GenericDao<FemtoProfile> {

	/**
	 * find profile by apei
	 * 
	 * @param apei
	 * @return
	 */
	public FemtoProfile findByApei(String apei);

	public FemtoProfile findByImsi(String imsi);

	public void removePermissionList(FemtoProfile profile);

	/**
	 * find profiles having specified cells
	 * 
	 * @param cells
	 */
	public List<FemtoProfile> findHavingCells(List<Cell> cells);
	
	public List<FemtoProfile> findByApZone(ApZone apZone);
	
}
