package tw.com.mds.fet.femtocellportal.core.dao;

import java.util.List;

import tw.com.mds.fet.femtocellportal.core.ApZone;
import tw.com.mds.fet.femtocellportal.dao.GenericDao;

public interface ApZoneDao extends GenericDao<ApZone> {

	public List<ApZone> findByFuzzyName(String name);

	public ApZone findExactlyByName(String apZoneName);

}
