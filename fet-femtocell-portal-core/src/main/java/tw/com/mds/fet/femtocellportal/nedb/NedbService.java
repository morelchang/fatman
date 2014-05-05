package tw.com.mds.fet.femtocellportal.nedb;

import tw.com.mds.fet.femtocellportal.core.ConfigurationException;
import tw.com.mds.fet.femtocellportal.core.Rnc;


public interface NedbService {

	public void syncCells() throws NedbException;
	
	public void syncFemtoProfiles() throws NedbException, ConfigurationException;

	public boolean isRncInUse(Rnc rnc);
	
}
