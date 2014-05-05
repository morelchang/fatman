<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript">
<!--
$(function() {
	$.unblockUI();
});

function removeUserConfirm(event, userName, apei, imsi) {
	var removing = confirm("確定刪除用戶:" + userName + " 的 APEI:" + apei + ", IMSI:" + imsi + " 申裝?");
	if (!removing) {
		// to prevent loading message
		event.stopImmediatePropagation();
	}
	return removing;
}

function suspendUserConfirm(event, userName, apei, imsi, state) {
	if (state == 'SUSPENDED') {
		alert("用戶:" + userName + " 的 APEI:" + apei + ", IMSI:" + imsi + " 已經停用!");
		event.stopImmediatePropagation();
		return false;
	}
	
	var suspending = confirm("確定暫停用戶:" + userName + " 的 APEI:" + apei + ", IMSI:" + imsi + " 申裝?");
	if (!suspending) {
		// to prevent loading message
		event.stopImmediatePropagation();
	}
	return suspending;
}

function resumeUserConfirm(event, userName, apei, imsi, state) {
	if (state == 'ACTIVE') {
		alert("用戶:" + userName + " 的 APEI:" + apei + ", IMSI:" + imsi + " 已經啟用!");
		event.stopImmediatePropagation();
		return false;
	}
	
	var resuming = confirm("確定啟用用戶:" + userName + " 的 APEI:" + apei + ", IMSI:" + imsi + " 申裝?");
	if (!resuming) {
		// to prevent loading message
		event.stopImmediatePropagation();
	}
	return resuming;
}

function clearSearchText() {
	$('#searchUserName').val('');
	$('#searchUserMobile').val('');
	$('#apei').val('');
	$('#imsi').val('');
}
//-->
</script>

<!-- title -->
<div>
	<h3>
		用戶申裝查詢 
		<s:if test="%{getText('SearchProfileAction.Mode.' + mode) != ''}">
		(<s:text name="SearchProfileAction.Mode.%{mode}" />)
		</s:if>
	</h3>
</div>

<!-- content -->
<s:include value="/pages/templates/messages.jsp"></s:include>

<s:if test="%{mode.toString() != 'CHANGE_PERMISSIONLIST_BY_FEMTOUSER' }">
<div style="width: 98%;" class="grayBorder">
	<s:form id="searchForm" namespace="/provision" action="searchProfile" validate="false" theme="simple">
		<s:label for="searchUserName">用戶姓名:</s:label>
		<s:textfield id="searchUserName" name="searchUserName" maxlength="50" size="6"/>
		
		<s:label for="searchUserMobile">用戶門號:</s:label>
		<s:textfield id="searchUserMobile" name="searchUserMobile" maxlength="10" size="10"/>
		
		<s:label for="apei">APEI:</s:label>
		<s:textfield id="apei" name="searchApei" maxlength="21" size="15" />
		
		<s:label for="imsi">AP IMSI:</s:label>
		<s:textfield id="imsi" name="searchImsi" maxlength="15" size="15" />
		
		<s:submit value="查詢" />
		<input type="button" value="清除" onclick="javascript: clearSearchText();" />
	</s:form>
</div><br/>
</s:if>

<table border="1" width="100%" cellpadding="5px" class="searchResultTable">
	<thead>
		<tr class="searchResultTableHeader">
			<s:if test="%{mode.toString() != 'CHANGE_PERMISSIONLIST_BY_FEMTOUSER' }">
			<td rowspan="2" width="55px">姓名</td>
			<td rowspan="2">用戶門號</td>
			</s:if>
			<td colspan="8">已申裝Femto</td>
		</tr>
		<tr class="searchResultTableHeader">
			<td>APEI</td>
	        <td>AP IMSI</td>
			<td>裝機地址</td>
			<td>狀態</td>
			<td>申裝時間</td>
			<td>異動時間</td>
			<td>操作</td>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="searchResult">
		<tr valign="top">
			<s:if test="%{mode.toString() != 'CHANGE_PERMISSIONLIST_BY_FEMTOUSER' }">
			<td rowspan="<s:property value='profiles.size'/>"><s:property value="userName" />
			</td>
			<td  rowspan="<s:property value='profiles.size'/>"><s:property value="mobile" />
			</td>
			</s:if>
						
			<s:iterator value="profiles" status="status">
			<s:if test="%{!#status.first}">
			<tr>
			</s:if>
			<td><s:property value="apei" />
			</td>
			<td><s:property value="imsi" />
			</td>
			<td>
				<a class="noloading" href="<s:property value='gisLocationLink'/><s:property value='apei'/>" target="_blank">
				<s:property value="address" />
				</a>
			</td>
			<td><s:text name="FemtoState.%{state}" />
			</td>
			<td><s:date name="createTime" />
			</td>
			<td><s:date name="updateTime" />
			</td>
			<td>
				<s:if test="%{mode.toString() == 'SEARCH'}">
				<s:url var="detailUrl" namespace="/provision" action="displayDetail">
					<s:param name="currentProfile.oid" value="oid"></s:param>
				</s:url>
				<a href="<s:property value="detailUrl"/>">查看明細</a>
				</s:if> 
				
				<s:if test="%{mode.toString() == 'CHANGE_PERMISSIONLIST'}">
				<s:url var="modifyUrl" namespace="/provision" action="displayChangePermissionList">
					<s:param name="currentProfile.oid" value="oid"></s:param>
				</s:url>
				<a href="<s:property value="modifyUrl"/>">修改白名單</a>
				</s:if>

				<s:if test="%{mode.toString() == 'CHANGE_UEPERMISSIONMODE'}">
				<s:url var="changeUePermissionModeUrl" namespace="/provision" action="displayChangeUePermissionMode">
					<s:param name="currentProfile.oid" value="oid"></s:param>
				</s:url>
				<a href="<s:property value="changeUePermissionModeUrl"/>">切換網路模式</a>
				</s:if>

				<s:if test="%{mode.toString() == 'CHANGE_LOCATIONDETECTMODE'}">
				<s:url var="changeUePermissionModeUrl" namespace="/provision" action="displayChangeLocationDetectMode">
					<s:param name="currentProfile.oid" value="oid"></s:param>
				</s:url>
				<a href="<s:property value="changeUePermissionModeUrl"/>">鎖定模式切換</a>
				</s:if>

				<s:if test="%{mode.toString() == 'CHANGE_PROFILE'}">
				<s:url var="changeProfileUrl" namespace="/provision" action="displayChangeProfile">
					<s:param name="currentProfile.oid" value="oid"></s:param>
				</s:url>
				<a href="<s:property value="changeProfileUrl"/>">換機</a>
				</s:if>

				<s:if test="%{mode.toString() == 'SUSPEND'}">
				<s:url var="suspendUrl" namespace="/provision" action="suspendProfile">
					<s:param name="currentProfile.oid" value="oid"></s:param>
				</s:url>
				<a href="<s:property value="suspendUrl"/>"
					onclick="return suspendUserConfirm(event, '<s:property value="userName" />', '<s:property value="apei" />', '<s:property value="imsi" />', '<s:property value="state" />');">
					停用</a> 
				</s:if>

				<s:if test="%{mode.toString() == 'RESUME'}">
				<s:url var="resumeUrl" namespace="/provision" action="resumeProfile">
					<s:param name="currentProfile.oid" value="oid"></s:param>
				</s:url>
				<a href="<s:property value="resumeUrl"/>"
					onclick="return resumeUserConfirm(event, '<s:property value="userName" />', '<s:property value="apei" />', '<s:property value="imsi" />', '<s:property value="state" />');">
					重啟</a>  
				</s:if>

				<s:if test="%{mode.toString() == 'DELETE'}">
				<s:url var="removeUrl" namespace="/provision" action="removeProfile">
					<s:param name="currentProfile.oid" value="oid"></s:param>
				</s:url>
				<a href="<s:property value="removeUrl"/>"
					onclick="return removeUserConfirm(event, '<s:property value="userName" />', '<s:property value="apei" />', '<s:property value="imsi" />');">
					刪除</a>&nbsp;
				</s:if>

				<s:if test="%{mode.toString() == 'CHANGE_PERMISSIONLIST_BY_FEMTOUSER'}">
				<s:url var="modifyUrl" namespace="/femtouser" action="displayChangePermissionListByFemtoUser">
					<s:param name="currentProfile.oid" value="oid"></s:param>
				</s:url>
				<a href="<s:property value="modifyUrl"/>">修改白名單</a>
				</s:if>

			</td>
			</s:iterator>
		</tr>
		</s:iterator>
	</tbody>
</table>
