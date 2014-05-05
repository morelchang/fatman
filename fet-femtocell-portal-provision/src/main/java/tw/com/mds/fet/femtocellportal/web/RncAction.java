package tw.com.mds.fet.femtocellportal.web;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.core.Rnc;
import tw.com.mds.fet.femtocellportal.nedb.NedbService;

public class RncAction extends CrudAction<Rnc> {

	private static final long serialVersionUID = 1L;
	
	private NedbService nedbService;
	
	@Override
	protected boolean validateSave(Rnc saving) {
		if (StringUtils.isEmpty(saving.getRncId())) {
			addActionError("必須輸入RNC ID");
			return false;
		}
		
		Rnc existed = administrationService.findRncByRncId(saving.getRncId());
		if (existed != null && !existed.getOid().equals(saving.getOid())) {
			addActionError("RNC ID:{0} 已存在, 不可重複", existed.getRncId());
			return false;
		}
		return true;
	}

	@Override
	protected List<Rnc> search(Rnc criteria) throws Exception {
		if (StringUtils.isBlank(criteria.getRncId()) && StringUtils.isBlank(criteria.getRncName())) {
			return administrationService.findAllRncs();
		}
		return administrationService.findRncsByFuzzyRncIdAndRncName(criteria);
	}

	@Override
	protected boolean validateDelete(Rnc deleting) {
		if (nedbService.isRncInUse(deleting)) {
			addActionError("RNC:{0} 被使用中，無法刪除", deleting.getRncName());
			return false;
		}
		return true;
	}

	public NedbService getNedbService() {
		return nedbService;
	}

	public void setNedbService(NedbService nedbService) {
		this.nedbService = nedbService;
	}

}
