package tw.com.mds.fet.femtocellportal.core;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import tw.com.mds.fet.femtocellportal.core.dao.CellDao;
import tw.com.mds.fet.femtocellportal.test.DefaultTestCase;

public class BatchServiceTest extends DefaultTestCase {

	@Autowired
	private BatchService service;
	
	@Autowired
	private CellDao dao;
	
	@Test
	public void testSyncCells() throws ServiceException {
		data.createProfile("1", "1");
		data.createProfile("2", "2");
		service.syncCells();
		
		System.out.println(dao.findAll());
	}
	
}
