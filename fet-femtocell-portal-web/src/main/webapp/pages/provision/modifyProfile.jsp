<%@ page language="java" contentType="text/html; charset=UTF8"
	pageEncoding="UTF8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
<!--
$.blockUI({ message: loadingHtml });

$(function() {
	resetPageState();
	$.unblockUI();
});

function resetPageState() {
	changeProfileStaticIpState();
	changePermissionListState($('#currentProfile_maxPermissionListSize').val(), $('#currentProfile_UePermissionMode').val());
	changePageModeState('<s:property value="mode" />');
}

function changePageModeState(mode) {
	if (mode == 'CREATE_USER_PROFILE') {
		$('#backButton').hide();
		
	} else if (mode == 'CREATE_PROFILE') {
		$('#userSection input').attr('disabled', 'disabled');

	} else if (mode == 'DISPLAY') {
		$('#userSection input').attr('disabled', 'disabled');
		$('#profileSection input,#profileSection select').attr('disabled', 'disabled');
		$('#permissionListSection input').attr('disabled', 'disabled');
		$('#resetButton').hide();
		$('#submitButton').hide();
		
	} else if (mode == 'CHANGE_PERMISSIONLIST') {
		$('#userSection').attr('disabled', 'disabled');
		$('#userSection input').attr('disabled', 'disabled');
		$('#profileSection input,#profileSection select').attr('disabled', 'disabled');
		
	} else if (mode == 'CHANGE_PERMISSIONLIST_BY_FEMTOUSER') {
		$('#userSection').attr('disabled', 'disabled');
		$('#userSection input').attr('disabled', 'disabled');
		$('#profileSection input,#profileSection select').attr('disabled', 'disabled');
		$('#cellsSection').hide();
		
	} else if (mode == 'CHANGE_UEPERMISSIONMODE') {
		$('#userSection input').attr('disabled', 'disabled');
		$('#profileSection input,#profileSection select').attr('disabled', 'disabled');
		$('#currentProfile_UePermissionMode').removeAttr('disabled', 'disabled');
		
	} else if (mode == 'CHANGE_PROFILE') {
		$('#userSection').attr('disabled', 'disabled');
		$('#userSection input').attr('disabled', 'disabled');
	}
}

function clearValue(input, count) {
	var maxPermissionListSize = $('#currentProfile_maxPermissionListSize').val();
	if (!isNaN(maxPermissionListSize) && count > maxPermissionListSize) {
		input.removeAttr('style');
		input.attr('disabled', 'disabled');
	}
	
	input.val('');
	return false;
}

function addMsisdn(tableElement) {
	var maxRowCount = <s:property value="maxPermissionListSize" />;
	var table = $('#' + tableElement);
	if (table.attr('rows').length >= maxRowCount) {
		alert('最多僅能輸入' + maxRowCount + '組門號');
		return false;
	}
	var newRowCount = table.attr('rows').length + 1;
	var newTextFieldName = 'currentProfile.permissionList[' + (newRowCount - 1) + '].msisdn';
	var clearButton = '<input type="image" src="<s:url value='%{serverContext}/images/buttonClose24.png'/>" />';
	$('tr:last', table).after('<tr><td>' + newRowCount + ':</td><td><input type="text" name="' + newTextFieldName + '" maxlength="15" size="15"/></td><td>' + clearButton + '</td><td></td></tr>');
	$('tr:last input:last', table).click(function() {return clearValue(newTextFieldName);} );
	return false;
}

function changeProfileStaticIpState() {
	var connectionModes = $('input[name="currentProfile.connectionMode"]');
	if (connectionModes.get(2).checked) {
		$('#currentProfileStaticIp').removeAttr("disabled");
		$('#currentProfileStaticIpSection').show();
	} else {
		$('#currentProfileStaticIp').attr("disabled", "disabled");
		$('#currentProfileStaticIpSection').hide();
	}
}

function changePermissionListState(maxPermissionListSize, uePermissionMode) {
	// restore default state of permissionList section
	$('#permissionListOpenNote').hide();
	$('#permissionListTable').show();
	
	// hide permission list when OPEN mode
	if (uePermissionMode == 'OPEN') {
		$('#permissionListOpenNote').show();
		$('#permissionListTable').hide();
	}
		
	if (isNaN(maxPermissionListSize)) {
		return;
	}
	
	var systemMaxPermissionListSize = <s:property value="maxPermissionListSize" />;
	
	// change msisdn field state
	var overLimitedSize = false;
	for (i = 0; i < systemMaxPermissionListSize; i++) {
		var msisdn = $('#currentProfile_permissionList_msisdn_' + (i + 1));
		msisdn.removeAttr('style');
		msisdn.removeAttr("disabled");
		if (i >= maxPermissionListSize) {
			if (msisdn.val() != '') {
				overLimitedSize = true;
				msisdn.attr("disabled", "disabled");

				if (uePermissionMode != 'OPEN') {
					msisdn.attr('style', 'background-color: #EECCCC;');
				}
			} else {
				msisdn.attr("disabled", "disabled");
			}
		}
	}
	
	if (overLimitedSize && uePermissionMode == 'CLOSE') {
		alert('注意! 用戶白名單門號清單中, 有些門號超過白名單數限制, 這些門號將不會被儲存至白名單清單中.');
	}
}

function save() {
	if (validateSave()) {
		return confirm('確定儲存?');
	}
	return false;
}

function validateSave() {
	var message = '';
/*
	message += validateNonEmpty('current_userName', '用戶名稱為必填');
	message += validateNonEmpty('current_mobile', '用戶門號為必填');
	message += validateNonEmpty('current_account', '登入帳號為必填');
	if (!$('#current_password').attr('disabled')) {
		message += validateNonEmpty('current_password', '登入密碼為必填');
	}
	if ($('#current_password').val() != $('#current_confirmPassword').val()) {
		message += '再次輸入密碼不正確\n';
	}
	message += validateNonEmpty('currentProfile_apei', 'APEI為必填');
	message += validateNonEmpty('currentProfile_imsi', 'IMSI為必填');
	message += validateNonEmpty('currentProfile_address', '裝機地址為必填');
	message += validateSelected('currentProfile_UePermissionMode', '網路模式為必填');
	message += validateNonEmpty('currentProfile_maxPermissionListSize', '白名單數限制為必填');
	message += validateNonEmpty('currentProfile_maxUserCount', '同時最大用戶數為必填');
	message += validateSelected('currentProfile_apZone', 'AP Zone為必填');
	message += validateChecked('currentProfile\\.connectionMode', '連線方式為必填');
	var connectionModes = $('input[name="currentProfile.connectionMode"]');
	if (connectionModes.get(2).checked) {
		message += validateNonEmpty('currentProfileStaticIp', 'Static IP為必填');
	}

	if (message != '') {
		alert(message);
		return false;
	}
*/
	return true;
}

function validateChecked(name, msg) {
	var message = '';
	if ($('input[name="' + name + '"]:checked').length == 0) {
		message += msg + '\n';
	}
	return message;
}

function validateSelected(id, msg) {
	var message = '';
	if ($('#' + id)[0].selectedIndex == 0) {
		message += msg + '\n';
	}
	return message;
}

function validateNonEmpty(id, msg) {
	var message = '';
	if(!$.trim($('#' + id).val())) {
		message += msg + '\n';
	}
	return message;
}

function validateRequired(id, msg) {
	var message = '';
	if(!$('#' + id).val()) {
		message += msg + '\n';
	}
	return message;
}
//-->
</script>

<!-- title -->
<div>
	<h3>用戶申請明細(<s:text name="ModifyUserAction.Mode.%{mode}" />)</h3>
</div>

<!-- content -->
<s:if test="%{mode.toString() == 'CHANGE_PERMISSIONLIST_BY_FEMTOUSER'}">
	<s:set var="namespace" value="%{'/femtouser'}"></s:set>
</s:if>
<s:else>
	<s:set var="namespace" value="%{'/provision'}"></s:set>
</s:else>

<s:form id="addUserForm" namespace="%{serverContext}%{#namespace}" action="save" validate="true" includeContext="false" theme="simple">

<s:include value="/pages/templates/messages.jsp"></s:include>

<table border="0" width="100%" cellpadding="0" cellspacing="0" class="noBorder">
	<tr>
		<td width="440px" valign="top">
			<div id="userSection" style="width: 94%;" class="grayBorder">
				<span>用戶基本資訊</span>
				<s:hidden id="oid" name="current.oid" />
				<table class="noBorder">
					<tr><td align="right"><s:label>用戶姓名:</s:label></td>
						<td><s:textfield id="current_userName" name="current.userName" maxlength="32" size="16"/></td></tr>
					<tr><td align="right"><s:label>用戶門號:</s:label></td>
						<td><s:textfield id="current_mobile" name="current.mobile" maxlength="10" size="10"/></td></tr>
					<s:if test="%{enableLocalAuthentication}">
					<tr><td align="right"><s:label>登入帳號:</s:label></td>
						<td><s:textfield id="current_account" name="current.account" maxlength="50" size="16"/></td></tr>
					<tr><td align="right"><s:label>登入密碼:</s:label></td>
						<td><s:password id="current_password" name="current.password" maxlength="50" size="16"/></td></tr>
					<tr><td align="right"><s:label>再次輸入密碼:</s:label></td>
						<td><s:password id="current_confirmPassword" name="confirmPassword" maxlength="50" size="16"/></td></tr>
					</s:if>
				</table>
			</div><br/>
	
			<div id="profileSection" style="width: 94%;" class="grayBorder">
				<span>用戶申裝資訊</span>
				<s:hidden id="currentProfile_oid" name="currentProfile.oid" />
				<table class="noBorder">
					<tr><td width="140px" align="right"><s:label>APEI:</s:label></td>
						<td><s:textfield id="currentProfile_apei" name="currentProfile.apei" maxlength="21" size="21"/></td></tr>
					<tr><td align="right"><s:label>AP IMSI:</s:label></td>
						<td><s:textfield id="currentProfile_imsi" name="currentProfile.imsi" maxlength="15" size="15"/></td></tr>
					<tr><td align="right"><s:label>裝機地址:</s:label></td>
						<td><s:textfield id="currentProfile_address" name="currentProfile.address" maxlength="100" size="38"/></td></tr>
					<tr><td align="right"><s:label>網路模式:</s:label></td>
						<td><s:select id="currentProfile_UePermissionMode" 
							list="availableUePermissionModes" name="currentProfile.uePermissionMode" 
							headerKey="%{null}" headerValue="===請選擇==="
							onchange="javascript: resetPageState();"></s:select></td></tr>
					<tr><td align="right"><s:label>鎖定模式:</s:label></td>
						<td><s:radio id="currentProfile_locationDetectMode" list="availableLocationDetectModes" 
								name="currentProfile.locationDetectMode" listValue="%{getText('LocationDetectMode.' + toString())}">
						</s:radio></td></tr>
					<tr><td align="right"><s:label>白名單數限制:</s:label></td>
						<td><s:textfield id="currentProfile_maxPermissionListSize" name="currentProfile.maxPermissionListSize" 
								maxlength="3" size="3" onblur="javascript: resetPageState(); " /></td></tr>
					<tr><td align="right"><s:label>同時最大用戶數:</s:label></td>
						<td><s:textfield id="currentProfile_maxUserCount" name="currentProfile.maxUserCount" maxlength="3" size="3"/></td></tr>
					<tr><td align="right"><s:label>AP Zone:</s:label></td>
						<td><s:select id="currentProfile_apZone" list="availableApZones" name="selectedApZoneOid" 
							listKey="oid" listValue="name"
							headerKey="%{null}" headerValue="===請選擇==="></s:select></td></tr>
					<tr><td align="right"><s:label>連線方式:</s:label></td>
						<td><s:radio id="currentProfile_connectionMode" list="availableConnectionModes" 
							name="currentProfile.connectionMode" listValue="%{getText('ConnectionMode.' + toString())}"
							onclick="changeProfileStaticIpState();">
						</s:radio></td></tr>
					<tr id="currentProfileStaticIpSection"><td align="right"><s:label>IP:</s:label></td>
						<td><s:textfield id="currentProfileStaticIp" name="currentProfile.staticIp" maxlength="25" size="15"
								disabled="disabled" /></td>
					</tr>
				</table>
			</div><br/>
		</td>
		<td valign="top">
			<div id="permissionListSection" style="width: 94%; height:300px;" class="grayBorder">
				<span>用戶白名單門號清單</span>
				<s:hidden id="currentProfile_oid" name="currentProfile.oid" />
				<table id="permissionListOpenNote" class="noBorder" cellpadding="30px">
					<tr><td>網路模式為OPEN, 不需輸入白名單</td></tr>
				</table>
				
				<table id="permissionListTable" border="0" class="noBorder">
					<tr>
						<td>
							<table id="permissionListTable" class="noBorder" cellspacing="0" cellpadding="0">
								<s:iterator value="currentProfile.permissionList" status="status" >
								<s:if test="%{#status.index % 3 == 0}">
								<tr>
								</s:if>
									<s:hidden name="currentProfile.permissionList[%{#status.index}].oid" value="%{oid}"/>
									<s:hidden name="currentProfile.permissionList[%{#status.index}].imsi" value="%{imsi}"/>
									<td align="right"><s:property value="%{#status.count}" />:</td>
									<td><s:textfield id="currentProfile_permissionList_msisdn_%{#status.count}" name="currentProfile.permissionList[%{#status.index}].msisdn" value="%{msisdn}" maxlength="10" size="8"/></td>
									<td><input type="image" src='<s:url value="%{serverContext}/images/buttonClose24.png"/>' 
										onclick="javascript: return clearValue($('#currentProfile_permissionList_msisdn_<s:property value="%{#status.count}" />'), <s:property value="%{#status.count}" />); " /></td>
									<td>&nbsp;&nbsp;&nbsp;</td>
								<s:if test="%{#status.index % 3 == 2}">
								</tr>
								</s:if>
								</s:iterator>
							</table>
						</td>
						<!-- 
						<td valign="bottom" align="right">
							<input type="image" src='<s:url value="%{serverContext}/images/buttonAdd24.png"/>' 
								onclick="javascript: return addMsisdn('permissionListTable'); " />
						</td>
						 -->
					</tr>
				</table>
			</div><br/>
			
			<div id="cellsSection" style="width: 94%;" class="grayBorder">
				<span>基地台資訊</span>
				<s:if test="currentProfile.cells.isEmpty()">
					<table class="noBorder" cellpadding="30px">
					<tr><td>目前尚無基地台資訊</td></tr>
					</table>
				</s:if>
				<s:else>
					<table border="1" cellpadding="5px" class="searchResultTable">
						<tr class="searchResultTableHeader">
							<td>RNC ID</td>
							<td>Cell ID</td>
							<td>經度</td>
							<td>緯度</td>
							<td>RNC ID</td>
							<td>Cell ID</td>
							<td>經度</td>
							<td>緯度</td>
						</tr>
					<s:iterator value="currentProfile.cells" status="status">
						<s:if test="%{#status.index % 2 == 0}">
						<tr>
						</s:if>
							<td align="right"><s:property value="rnc.rncId" /></td>
							<td align="right"><s:property value="cellId" /></td>
							<td align="right"><s:property value="position.longitude" /></td>
							<td align="right"><s:property value="position.latitude" /></td>
						<s:if test="%{#status.index % 2 == 1}">
						</tr>
						</s:if>
					</s:iterator>
					</table>
				</s:else>
			</div><br/>
			
			<div style="width: 94%; text-align: right;" class="noBorder">
				<s:submit id="submitButton" value="儲存" onclick="javascript: return save();"></s:submit>
				<input id="resetButton" type="button" value="復原" onclick="javascript: reset(); resetPageState();"></input>
				<input id="backButton" type="button" value="回上一頁" onclick="javascript: $('#leaveForm').submit(); " />
			</div>
		</td>
	</tr>
</table>
</s:form>
<s:form id="leaveForm" namespace="%{serverContext}/femtouser" action="leaveModifyProfile">
</s:form>