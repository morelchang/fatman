package tw.com.mds.fet.femtocellportal.core.dao.impl;

import java.util.ArrayList;
import java.util.List;

import tw.com.mds.fet.femtocellportal.core.ApZone;
import tw.com.mds.fet.femtocellportal.core.Cell;
import tw.com.mds.fet.femtocellportal.core.FemtoProfile;
import tw.com.mds.fet.femtocellportal.core.dao.FemtoProfileDao;
import tw.com.mds.fet.femtocellportal.dao.impl.JpaGenericDao;

public class JpaFemtoProfileDao extends JpaGenericDao<FemtoProfile> implements
		FemtoProfileDao {

	public FemtoProfile findByApei(String apei) {
		if (apei == null) {
			return null;
		}
		return queryForFirst("from FemtoProfile e where e.apei = ?", apei);
	}

	public void removePermissionList(FemtoProfile profile) {
		if (profile == null) {
			return;
		}
		executeUpdate("delete from UserEquipment e where e.profile = ?", profile);
	}

	public FemtoProfile findByImsi(String imsi) {
		if (imsi == null) {
			return null;
		}
		return queryForFirst("from FemtoProfile e where e.imsi = ?", imsi);
	}

	@SuppressWarnings("unchecked")
	public List<FemtoProfile> findHavingCells(List<Cell> cells) {
		if (cells == null || cells.isEmpty() || cells == null) {
			return new ArrayList<FemtoProfile>();
		}
		
		return createQuery("select distinct e from FemtoProfile e left join fetch e.cells c where c in (:cells) order by e.apei")
				.setParameter("cells", cells)
				.getResultList();
	}

	public List<FemtoProfile> findByApZone(ApZone apZone) {
		if (apZone == null) {
			return new ArrayList<FemtoProfile>();
		}
		return query("from FemtoProfile e where e.apZone = ?", apZone);
	}

}
