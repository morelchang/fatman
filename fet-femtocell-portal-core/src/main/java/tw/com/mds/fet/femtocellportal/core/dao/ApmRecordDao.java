package tw.com.mds.fet.femtocellportal.core.dao;

import tw.com.mds.fet.femtocellportal.core.ApmRecord;
import tw.com.mds.fet.femtocellportal.dao.GenericDao;

public interface ApmRecordDao extends GenericDao<ApmRecord> {

	public void removeAll();

}
