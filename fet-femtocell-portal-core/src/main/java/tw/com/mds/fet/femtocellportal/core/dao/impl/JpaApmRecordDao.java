package tw.com.mds.fet.femtocellportal.core.dao.impl;

import tw.com.mds.fet.femtocellportal.core.ApmRecord;
import tw.com.mds.fet.femtocellportal.core.dao.ApmRecordDao;
import tw.com.mds.fet.femtocellportal.dao.impl.JpaGenericDao;

public class JpaApmRecordDao extends JpaGenericDao<ApmRecord> implements
		ApmRecordDao {

	public void removeAll() {
		executeUpdate("delete ApmRecord e");
	}

}
