package tw.com.mds.fet.femtocellportal.core;

import java.util.List;

public interface DataLoadService {

	public List<ApZone> loadApZones() throws ServiceException;
	
	public List<AdminUser> loadAdminUsers() throws ServiceException;
	
	public List<Rnc> loadRncs() throws ServiceException;

	public List<Cell> loadCells() throws ServiceException;
	
}
