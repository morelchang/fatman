package tw.com.mds.fet.femtocellportal.core.impl;

import tw.com.mds.fet.femtocellportal.core.BatchService;
import tw.com.mds.fet.femtocellportal.core.ProvisionService;
import tw.com.mds.fet.femtocellportal.core.ServiceException;
import tw.com.mds.fet.femtocellportal.nedb.NedbService;

public class BatchServiceImpl implements BatchService {
	
	private NedbService nedbService;
	private ProvisionService provisionService;

	public void syncCells() throws ServiceException {
		nedbService.syncCells();
		provisionService.updateProfileLocation();
	}

	public NedbService getNedbService() {
		return nedbService;
	}

	public void setNedbService(NedbService nedbService) {
		this.nedbService = nedbService;
	}

	public ProvisionService getProvisionService() {
		return provisionService;
	}

	public void setProvisionService(ProvisionService provisionService) {
		this.provisionService = provisionService;
	}

}
