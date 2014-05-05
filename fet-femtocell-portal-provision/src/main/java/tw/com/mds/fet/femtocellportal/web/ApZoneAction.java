package tw.com.mds.fet.femtocellportal.web;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.core.ApZone;
import tw.com.mds.fet.femtocellportal.core.ProvisionService;

public class ApZoneAction extends CrudAction<ApZone> {

	private static final long serialVersionUID = 1L;
	private ProvisionService provisionService;

	@Override
	protected List<ApZone> search(ApZone criteria) throws Exception {
		if (StringUtils.isBlank(criteria.getName())) {
			return administrationService.listAllApZones();
		}
		return administrationService.findApZonesByFuzzyName(criteria.getName());
	}

	@Override
	protected boolean validateSave(ApZone saving) {
		if (StringUtils.isEmpty(saving.getName())) {
			addActionError("必須輸入Zone Name");
			return false;
		}
		return true;
	}

	@Override
	protected boolean validateDelete(ApZone deleting) {
		if (!provisionService.findProfileByApZone(deleting).isEmpty()) {
			addActionError("AP Zone:{0} 被使用中，無法刪除", deleting.getName());
			return false;
		}
		return true;
	}

	public ProvisionService getProvisionService() {
		return provisionService;
	}

	public void setProvisionService(ProvisionService provisionService) {
		this.provisionService = provisionService;
	}

}
