package tw.com.mds.fet.femtocellportal.core.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.core.Cell;
import tw.com.mds.fet.femtocellportal.core.Rnc;
import tw.com.mds.fet.femtocellportal.core.dao.CellDao;
import tw.com.mds.fet.femtocellportal.dao.impl.JpaGenericDao;

public class JpaCellDao extends JpaGenericDao<Cell> implements CellDao {

	public Cell findByCellId(String cellId) {
		if (cellId == null) {
			return null;
		}
		return queryForFirst("from Cell e where e.cellId = ?", cellId);
	}

	public List<Cell> findExpired(Date baseTime) {
		if (baseTime == null) {
			return new ArrayList<Cell>();
		}
		return query("from Cell e where e.updateTime < ? order by e.cellId", baseTime);
	}

	public Cell findByRncCellId(Rnc rnc, String cellId) {
		if (rnc == null || cellId == null) {
			return null;
		}
		return queryForFirst("from Cell e where e.rnc = ? and e.cellId = ? ", rnc, cellId);
	}

	public long countByRnc(Rnc rnc) {
		return (Long) createQuery(
				"select count(e) from Cell e where e.rnc = ?", rnc)
				.getResultList().get(0);
	}

	public Cell findByCellName(String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		return queryForFirst("from Cell e where e.cellName = ?", name);
	}

}
