<%@ page language="java" contentType="text/html; charset=UTF8"
	pageEncoding="UTF8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!-- menu bar -->
<div id="menu" class="menuStyle">
	<table border="0" class="noBorder">
		<tr>
			<td nowrap="nowrap">
				<div>
					&nbsp;&nbsp;<s:a namespace="/provision" action="main">回首頁</s:a><br/>
					
					<s:if test="%{underMaintenance}">
					&nbsp;&nbsp;<span style="color:#AEAEAE">[開通功能維護中]</span>
					</s:if>
					<s:else>
					<s:if test="%{loginAdminUser.getPermission(@tw.com.mds.fet.femtocellportal.web.ResourceConstant@CREATE_USER_PROFILE)}">
					&nbsp;&nbsp;<s:a namespace="/provision" action="displayAddUserProfile" disabled="true">新用戶申請</s:a><br/>
					</s:if>
					<s:if test="%{loginAdminUser.getPermission(@tw.com.mds.fet.femtocellportal.web.ResourceConstant@CREATE_PROFILE)}">
					&nbsp;&nbsp;<s:a namespace="/provision" action="searchUser">舊用戶申請</s:a><br/>
					</s:if>
					<s:if test="%{loginAdminUser.getPermission(@tw.com.mds.fet.femtocellportal.web.ResourceConstant@SEARCH_PROFILE)}">
					&nbsp;&nbsp;<s:a namespace="/provision" action="searchProfile"><s:param name="mode" value="%{'SEARCH'}" />用戶查詢</s:a><br/>
					</s:if>
					<s:if test="%{loginAdminUser.getPermission(@tw.com.mds.fet.femtocellportal.web.ResourceConstant@CHANGE_PERMISSIONLIST)}">
					&nbsp;&nbsp;<s:a namespace="/provision" action="searchProfile"><s:param name="mode" value="%{'CHANGE_PERMISSIONLIST'}" />用戶使用白名單修改</s:a><br/>
					</s:if>
					<s:if test="%{loginAdminUser.getPermission(@tw.com.mds.fet.femtocellportal.web.ResourceConstant@CHANGE_UEPERMISSIONMODE)}">
					&nbsp;&nbsp;<s:a namespace="/provision" action="searchProfile"><s:param name="mode" value="%{'CHANGE_UEPERMISSIONMODE'}" />用戶網路模式切換</s:a><br/>
					</s:if>
					<s:if test="%{loginAdminUser.getPermission(@tw.com.mds.fet.femtocellportal.web.ResourceConstant@CHANGE_LOCATIONDETECTMODE)}">
					&nbsp;&nbsp;<s:a namespace="/provision" action="searchProfile"><s:param name="mode" value="%{'CHANGE_LOCATIONDETECTMODE'}" />用戶鎖定模式切換</s:a><br/>
					</s:if>
					<s:if test="%{loginAdminUser.getPermission(@tw.com.mds.fet.femtocellportal.web.ResourceConstant@CHANGE_PROFILE)}">
					&nbsp;&nbsp;<s:a namespace="/provision" action="searchProfile"><s:param name="mode" value="%{'CHANGE_PROFILE'}" />用戶換機</s:a><br/>
					</s:if>
					<s:if test="%{loginAdminUser.getPermission(@tw.com.mds.fet.femtocellportal.web.ResourceConstant@SUSPEND_PROFILE)}">
					&nbsp;&nbsp;<s:a namespace="/provision" action="searchProfile"><s:param name="mode" value="%{'SUSPEND'}" />用戶停機</s:a><br/>
					</s:if>
					<s:if test="%{loginAdminUser.getPermission(@tw.com.mds.fet.femtocellportal.web.ResourceConstant@RESUME_PROFILE)}">
					&nbsp;&nbsp;<s:a namespace="/provision" action="searchProfile"><s:param name="mode" value="%{'RESUME'}" />用戶重啟</s:a><br/>
					</s:if>
					<s:if test="%{loginAdminUser.getPermission(@tw.com.mds.fet.femtocellportal.web.ResourceConstant@DELETE_PROFILE)}">
					&nbsp;&nbsp;<s:a namespace="/provision" action="searchProfile"><s:param name="mode" value="%{'DELETE'}" />用戶退機(刪除)</s:a><br/> 
					</s:if>
					</s:else>
				</div><br/>
				
				<div>
					<s:if test="%{loginAdminUser.getPermission(@tw.com.mds.fet.femtocellportal.web.ResourceConstant@MANAGE_ANNOUNCEMENT)}">
					&nbsp;&nbsp;<s:a namespace="/admin" action="displayAnnouncement"><s:param name="mode" value="%{'SEARCH'}" />系統公告維護</s:a><br/>
					</s:if>
					<s:if test="%{loginAdminUser.getPermission(@tw.com.mds.fet.femtocellportal.web.ResourceConstant@MANAGE_ADMINUSER)}">
					&nbsp;&nbsp;<s:a namespace="/admin" action="displayAdminUser"><s:param name="mode" value="%{'SEARCH'}" />前台帳號維護</s:a><br/>
					</s:if>
					<s:if test="%{loginAdminUser.getPermission(@tw.com.mds.fet.femtocellportal.web.ResourceConstant@MANAGE_RNC)}">
					&nbsp;&nbsp;<s:a namespace="/admin" action="displayRnc"><s:param name="mode" value="%{'SEARCH'}" />RNC維護</s:a><br/>
					</s:if>
					<s:if test="%{loginAdminUser.getPermission(@tw.com.mds.fet.femtocellportal.web.ResourceConstant@MANAGE_APZONE)}">
					&nbsp;&nbsp;<s:a namespace="/admin" action="displayApZone"><s:param name="mode" value="%{'SEARCH'}" />AP Zone維護</s:a><br/>
					</s:if>
					<s:if test="%{loginAdminUser.getPermission(@tw.com.mds.fet.femtocellportal.web.ResourceConstant@SEARCH_USERLOG)}">
					&nbsp;&nbsp;<s:a namespace="/admin" action="displayUserLog">使用記錄查詢</s:a><br/>
					</s:if>
					<s:if test="%{loginAdminUser.getPermission(@tw.com.mds.fet.femtocellportal.web.ResourceConstant@MANAGE_CONFIG)}">
					&nbsp;&nbsp;<s:a namespace="/admin" action="displayConfig">系統設定</s:a><br/>
					</s:if>
				</div>
				<br/>
			</td>
		</tr>
	</table>
	
	<s:if test="%{!hideMenuAnnouncement}">
	<div class="grayBorder">
		<table border="0" id="announcementTable" cellpadding="3" cellspacing="0" class="noBorder">
			<thead>
				<tr><td>
					<span>系統公告</span>
				</td></tr>
			</thead>
			<tbody>
			<s:if test="%{announcements.empty}">
				<tr>
					<td class="announcementTitle">
						目前暫無公告
					</td>
				</tr>
			</s:if>
			<s:else>
				<s:iterator value="announcements">
				<tr><td class="announcementTitle"
						annTitle="<s:property value='title' />" 
						annContent="<s:property value='content' />"
						annStartTime="<s:property value='startTime' />" >
					<span>
						<s:property value="title" />
					</span>&nbsp;
					<span>[<s:property value="startTime" />]</span>
					<br/>
				</td></tr>
				</s:iterator>
			</s:else>
			</tbody>
		</table>
	</div>
	
	<!-- load announcements as tooltips -->
	<script type="text/javascript">
	$('#announcementTable .announcementTitle').each(function() {
		if (!$(this).attr('annTitle')) {
			return;
		}
		$(this).qtip({
			content: {
				text: $(this).attr('annContent'),
				title: {
					text: $(this).attr('annTitle') + ' [' + $(this).attr('annStartTime') + ']'
				}
			},
			style: {
				background: '#FEFEFE',
				'white-space' : 'pre-wrap',
				border: {
					width: 4
				},
				width: {
					min: 'auto',
					max: 400
				}
			}
		});
	});
	</script>
	</s:if>
</div>
