package tw.com.mds.fet.femtocellportal.web;

import java.util.Arrays;
import java.util.List;

public abstract class ResourceConstant {

	public static final String CREATE_USER_PROFILE = "provision.create_user_profile";
	public static final String CREATE_PROFILE = "provision.create_profile";
	public static final String SEARCH_PROFILE = "provision.search_profile";
	public static final String CHANGE_PERMISSIONLIST = "provision.change_permissionlsit";
	public static final String CHANGE_UEPERMISSIONMODE = "provision.change_uepermissionmode";
	public static final String CHANGE_PROFILE = "provision.change_profile";
	public static final String SUSPEND_PROFILE = "provision.suspend_profile";
	public static final String RESUME_PROFILE = "provision.resume_profile";
	public static final String DELETE_PROFILE = "provision.delete_profile";
	public static final String MANAGE_ANNOUNCEMENT = "admin.manage_announcement";
	public static final String MANAGE_ADMINUSER = "admin.manage_adminusesr";
	public static final String MANAGE_RNC = "admin.manage_rnc";
	public static final String MANAGE_APZONE = "admin.manage_apzone";
	public static final String SEARCH_USERLOG = "admin.search_userlog";
	public static final String MANAGE_CONFIG = "admin.manage_config";
	public static final String CHANGE_LOCATIONDETECTMODE = "provision.change_locationdetectmode";
	
	public static List<String> getAllResourceIds() {
		return Arrays.asList(CREATE_USER_PROFILE, 
				CREATE_PROFILE, 
				SEARCH_PROFILE, 
				CHANGE_PERMISSIONLIST, 
				CHANGE_UEPERMISSIONMODE,
				CHANGE_LOCATIONDETECTMODE,
				CHANGE_PROFILE, 
				SUSPEND_PROFILE, 
				RESUME_PROFILE, 
				DELETE_PROFILE, 
				MANAGE_ANNOUNCEMENT, 
				MANAGE_ADMINUSER, 
				MANAGE_RNC, 
				MANAGE_APZONE, 
				SEARCH_USERLOG, 
				MANAGE_CONFIG);
	}
}
