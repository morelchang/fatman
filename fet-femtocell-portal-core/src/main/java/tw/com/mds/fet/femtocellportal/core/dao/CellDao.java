package tw.com.mds.fet.femtocellportal.core.dao;

import java.util.Date;
import java.util.List;

import tw.com.mds.fet.femtocellportal.core.Cell;
import tw.com.mds.fet.femtocellportal.core.Rnc;
import tw.com.mds.fet.femtocellportal.dao.GenericDao;

/**
 * cell dao
 * 
 * @author morel
 *
 */
public interface CellDao extends GenericDao<Cell> {

	/**
	 * find cell by cell id
	 * 
	 * @param cellId
	 * @return
	 */
	Cell findByCellId(String cellId);

	/**
	 * find all cells which update time is before specified baseTime
	 * 
	 * @param baseTime
	 * @return
	 */
	List<Cell> findExpired(Date baseTime);

	Cell findByRncCellId(Rnc rnc, String cellId);

	long countByRnc(Rnc rnc);
	
	Cell findByCellName(String name);
	
}
