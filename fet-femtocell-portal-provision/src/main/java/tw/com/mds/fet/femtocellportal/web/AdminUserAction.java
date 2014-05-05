package tw.com.mds.fet.femtocellportal.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import tw.com.mds.fet.femtocellportal.core.AdminUser;
import tw.com.mds.fet.femtocellportal.core.Permission;

public class AdminUserAction extends CrudAction<AdminUser> {

	private static final long serialVersionUID = 1L;
	
	private List<String> availableResourceIds = ResourceConstant.getAllResourceIds();
	
	private List<String> permissionResult = new ArrayList<String>();
	
	@Override
	protected List<AdminUser> search(AdminUser criteria) throws Exception {
		List<AdminUser> result = administrationService.findAdminUserByCriteria(criteria);
		return complementPermissions(result);
	}

	@Override
	protected AdminUser createDefaultNew() throws Exception {
		AdminUser adminUser = super.createDefaultNew();
		complementPermissions(Arrays.asList(adminUser));
		return adminUser;
	}

	@Override
	protected boolean validateSave(AdminUser saving) {
		if (StringUtils.isEmpty(saving.getUserId())) {
			addActionError("必須輸入使用者ID");
			return false;
		}
		if (StringUtils.isEmpty(saving.getUserName())) {
			addActionError("必須輸入使用者名稱");
			return false;
		}
		if (StringUtils.isEmpty(saving.getAccount())) {
			addActionError("必須輸入登入帳號");
			return false;
		}
		
		AdminUser exists = administrationService.findAdminUserByUserId(saving.getUserId());
		if (exists != null && !exists.getOid().equals(saving.getOid())) {
			addActionError("使用者ID:{0} 已存在, 不可重複", exists.getUserId());
			return false;
		}
		exists = administrationService.findAdminUserByAccount(saving.getAccount());
		if (exists != null && !exists.getOid().equals(saving.getOid())) {
			addActionError("使用者帳號:{0} 已存在, 不可重複", exists.getAccount());
			return false;
		}
		
		return true;
	}

	@Override
	protected void beforeSaving(AdminUser saving) {
		if (StringUtils.isEmpty(saving.getPassword())) {
			saving.setPassword(getOriginal().getPassword());
		}
		compactPermissions(Arrays.asList(saving));
		buildPermissionRelationship(saving);
	}

	private void buildPermissionRelationship(AdminUser saving) {
		// set relationship and createtime information
		Date now = new Date();
		List<Permission> permissions = saving.getPermissions();
		for (Permission p : permissions) {
			if (p.getCreateTime() == null) {
				p.setCreateTime(now);
				p.setAdminUser(saving);
			}
		}
	}

	@Override
	protected void beforeEditing(AdminUser editing) {
		// new one has been complemented in createDefaultNew() method
		if (editing.getOid() != null) {
			complementPermissions(Arrays.asList(editing));
		}
	}

	private List<AdminUser> complementPermissions(List<AdminUser> result) {
		for (AdminUser u : result) {
			List<Permission> complemented = new ArrayList<Permission>();
			for (String resourceId : availableResourceIds) {
				Permission p = u.getPermission(resourceId);
				if (p != null) {
					p.setEnabled(true);
				} else {
					p = new Permission();
					p.setResourceId(resourceId);
					p.setAdminUser(u);
				}
				complemented.add(p);
			}
			u.setPermissions(complemented);
		}
		return result;
	}

	public String savePermissions() {
		List<AdminUser> users = compactPermissions(getSearchResult());
		try {
			administrationService.saveAdminUserPermissions(users);
			addActionMessage("權限儲存成功");
		} catch (Exception e) {
			addActionError("系統錯誤, 原因:{0}", e.getMessage());
			return ERROR;
		}
		return search();
	}

	private List<AdminUser> compactPermissions(List<AdminUser> adminUsers) {
		Map<Long, List<String>> selectedPermissionMap = convertMap(permissionResult);
		for (AdminUser u : adminUsers) {
			Iterator<Permission> it = u.getPermissions().iterator();
			while (it.hasNext()) {
				Permission p = it.next();
				if (!isSelectedPermission(selectedPermissionMap, u, p)) {
					it.remove();
				} else {
					p.setEnabled(true);
				}
			}
		}
		return adminUsers;
	}

	private boolean isSelectedPermission(
			Map<Long, List<String>> selectedPermissionMap, AdminUser u, Permission p) {
		List<String> selectedPermissions = selectedPermissionMap.get(u.getOid());
		if (selectedPermissions == null) {
			return false;
		}
		return selectedPermissions.contains(p.getResourceId());
	}

	/**
	 * convert the permissionResult value submit by end user into {@link Map}
	 * which key is the permission owner's oid {@link AdminUser#getOid()} and the
	 * map value is the {@link Permission#getResourceId()} list selected by end user
	 * 
	 * @param permissionResult
	 * @return
	 */
	private Map<Long, List<String>> convertMap(List<String> permissionResult) {
		Map<Long, List<String>> result = new HashMap<Long, List<String>>();
		for (String pr : permissionResult) {
			String[] values = StringUtils.split(pr, ",");
			values = fixNullAdminUserOidSplitResult(values);
			
			// put as adminUser.oid and resourceId
			Long adminUserOid = null;
			if (StringUtils.isNumeric(values[0])) {
				adminUserOid = Long.parseLong(values[0]);
			}
			
			List<String> permissions = result.get(adminUserOid);
			if (permissions == null) {
				permissions = new ArrayList<String>();
			}
			permissions.add(values[1]);
			result.put(adminUserOid, permissions);
		}
		return result;
	}

	private String[] fixNullAdminUserOidSplitResult(String[] values) {
		if (values.length == 1) {
			values = new String[] {null, values[0]};
		}
		return values;
	}

	public List<String> getPermissionResult() {
		return permissionResult;
	}

	public void setPermissionResult(List<String> permissionResult) {
		this.permissionResult = permissionResult;
	}
	
}
