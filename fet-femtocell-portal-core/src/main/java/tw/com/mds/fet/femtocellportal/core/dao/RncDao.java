package tw.com.mds.fet.femtocellportal.core.dao;

import java.util.List;

import tw.com.mds.fet.femtocellportal.core.Rnc;
import tw.com.mds.fet.femtocellportal.dao.GenericDao;

public interface RncDao extends GenericDao<Rnc> {

	public Rnc findByRncId(String rncId);

	public List<Rnc> findByFuzzyIdAndName(Rnc criteria);

	public List<Rnc> findbyRncName(String rncName);

}
