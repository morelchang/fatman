package tw.com.mds.fet.femtocellportal.core.dao;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import tw.com.mds.fet.femtocellportal.core.Cell;
import tw.com.mds.fet.femtocellportal.test.DefaultTestCase;

public class CellDaoTest extends DefaultTestCase {

	@Autowired
	private CellDao cellDao;
	
	@Test
	public void testFindAll() {
		Cell c = data.createCell("1");
		
		cellDao.persist(c);
		cellDao.flushAndClear();
		Cell created = cellDao.findByCellId(c.getCellId());
		
		Assert.assertNotNull(created.getOid());
		Assert.assertEquals(c.getCellId(), created.getCellId());
		Assert.assertEquals(c.getLacId(), created.getLacId());
		Assert.assertEquals(c.getCreateTime(), created.getCreateTime());
		Assert.assertEquals(c.getPlmId(), created.getPlmId());
		Assert.assertEquals(c.getRnc(), created.getRnc());
		Assert.assertEquals(c.getType(), created.getType());
		Assert.assertEquals(c.getUpdateTime(), created.getUpdateTime());
		
		List<Cell> all = cellDao.findAll();
		Assert.assertEquals(all.size(), 1);
		Assert.assertEquals(created, all.get(0));
	}
	
	@Test
	public void testDeleteExpired() {
		Date basetime = new Date();
		Cell c1 = data.createCell("1");
		c1.setUpdateTime(new Date(basetime.getTime() - 1));
		Cell c2 = data.createCell("2");
		c2.setUpdateTime(new Date(basetime.getTime()));
		Cell c3 = data.createCell("3");
		c3.setUpdateTime(new Date(basetime.getTime() + 1));
		
		c1 = cellDao.persist(c1);
		c2 = cellDao.persist(c2);
		c3 = cellDao.persist(c3);
		cellDao.flushAndClear();
		
		Assert.assertEquals(Arrays.asList(c1), cellDao.findExpired(basetime));
		Assert.assertEquals(Collections.EMPTY_LIST, cellDao.findExpired(new Date(basetime.getTime() - 1)));
		Assert.assertEquals(Arrays.asList(c1, c2), cellDao.findExpired(new Date(basetime.getTime() + 1)));
	}

}
