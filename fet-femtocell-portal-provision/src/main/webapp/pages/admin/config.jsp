<%@ page language="java" contentType="text/html; charset=UTF8"
	pageEncoding="UTF8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="jscalendar" uri="/jscalendar"%>
<script type="text/javascript">
$(function() {
	var tabs = $('#configTabs').tabs();
	tabs.tabs('select', <s:property value="tabIndex" />);
});

function save() {
	if (!validateSave()) {
		return false;
	}
	return confirm('確定儲存套用?');	
}

function validateSave() {
	var message = '';
	/*
	if($.trim($('#current_name').val()) == '') {
		message += '請輸入AP Zone名稱\n';
	}
	*/
	if (message != '') {
		alert(message);
		return false;
	}
	return true;
}
</script>

<!-- title -->
<div>
	<h3>系統設定</h3>
	<s:include value="/pages/templates/messages.jsp"></s:include>
</div>

<!-- content -->
<div id="configTabs">
	<ul>
		<li><a href="#provisionConfigTab" class="noloading">開通與用戶設定</a></li>
		<li><a href="#translatorConfigTab" class="noloading">Translator設定</a></li>
		<li><a href="#flowControlConfigTab" class="noloading">Translator流量控制</a></li>
		<li><a href="#nedbConfigTab" class="noloading">NEDB設定</a></li>
		<li><a href="#schedulingConfigTab" class="noloading">排程設定</a></li>
		<li><a href="#exceptionHandlerConfigTab" class="noloading">錯誤通報設定</a></li>
		<!-- 
		<li><a href="#ahrConfigTab" class="noloading">AHR設定</a></li>
		 -->
	</ul>

	<div id="provisionConfigTab" class="grayBorder baseFont">
		<s:form namespace="/admin" action="applyProvisionConfig"
			validate="false" theme="simple">
			<s:hidden name="tabIndex" value="0" />
			<s:hidden name="provisionConfig.oid" />
			<table cellpadding="2" class="noBorder">
				<tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>Default max user equipment size:</s:label>
					</td>
					<td><s:textfield id="provisionConfig_defaultMaxUserEquipmentSize"
							name="provisionConfig.defaultMaxUserEquipmentSize" maxlength="2" size="2" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>Enable SSO authentication:</s:label>
					</td>
					<td>
						<s:radio list="#{'true':'true', 'false':'false'}" name="provisionConfig.enableSsoAuthentication"
								id="provisionConfig_enableSsoAuthentication"></s:radio>
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>SSO login URL:</s:label>
					</td>
					<td><s:textfield id="provisionConfig_ssoLoginUrl"
							name="provisionConfig.ssoLoginUrl" maxlength="200" size="50" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>SSO logout URL:</s:label>
					</td>
					<td><s:textfield id="provisionConfig_ssoLogoutUrl"
							name="provisionConfig.ssoLogoutUrl" maxlength="200" size="50" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>SSO MSISDN header key:</s:label>
					</td>
					<td><s:textfield id="provisionConfig_ssoMsisdnHeaderKey"
							name="provisionConfig.ssoMsisdnHeaderKey" maxlength="80" size="15" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>Enable local authentication:</s:label>
					</td>
					<td>
						<s:radio list="#{'true':'true', 'false':'false'}" name="provisionConfig.enableLocalAuthentication"
								id="provisionConfig_enableLocalAuthentication"></s:radio>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="right">
						<s:submit value="儲存套用" onclick="javascript: return save();"></s:submit>
						<s:reset value="取消"></s:reset>
					</td>
				</tr>
			</table>
		</s:form>
	</div>
	
	<div id="translatorConfigTab" class="grayBorder baseFont">
		<s:form namespace="/admin" action="applyFetTranslatorConfig"
			validate="false" theme="simple">
			<s:hidden name="tabIndex" value="1" />
			<s:hidden name="fetTranslatorConfig.oid" />
			<table cellpadding="2" class="noBorder">
				<tr>
					<td class="verticalDataTableCaption" class="verticalDataTableCaption" align="right"><s:label>XML Encoding:</s:label>
					</td>
					<td><s:textfield id="fetTranslatorConfig_xmlEncoding"
							name="fetTranslatorConfig.xmlEncoding" maxlength="50" size="15" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right"><s:label>SGWA004 URL:</s:label>
					</td>
					<td><s:textfield id="fetTranslatorConfig_sgwA004Url"
							name="fetTranslatorConfig.sgwA004Url" maxlength="100" size="70" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right"><s:label>SGWA015 URL:</s:label>
					</td>
					<td><s:textfield id="fetTranslatorConfig_sgwA015Url"
							name="fetTranslatorConfig.sgwA015Url" maxlength="100" size="70" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right"><s:label>UserNameF:</s:label>
					</td>
					<td><s:textfield id="fetTranslatorConfig_userNameF"
							name="fetTranslatorConfig.userNameF" maxlength="50" size="30" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right"><s:label>PasswordF:</s:label>
					</td>
					<td><s:textfield id="fetTranslatorConfig_passwordF"
							name="fetTranslatorConfig.passwordF" maxlength="50" size="30" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right"><s:label>UserIdF:</s:label>
					</td>
					<td><s:textfield id="fetTranslatorConfig_userIdF"
							name="fetTranslatorConfig.userIdF" maxlength="50" size="30" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right"><s:label>ChannelIdF:</s:label>
					</td>
					<td><s:textfield id="fetTranslatorConfig_channelIdF"
							name="fetTranslatorConfig.channelIdF" maxlength="50" size="30" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right"><s:label>UserNameK:</s:label>
					</td>
					<td><s:textfield id="fetTranslatorConfig_userNameK"
							name="fetTranslatorConfig.userNameK" maxlength="50" size="30" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right"><s:label>PasswordK:</s:label>
					</td>
					<td><s:textfield id="fetTranslatorConfig_passwordK"
							name="fetTranslatorConfig.passwordK" maxlength="50" size="30" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right"><s:label>Entity:</s:label>
					</td>
					<td><s:textfield id="fetTranslatorConfig_entity"
							name="fetTranslatorConfig.entity" maxlength="50" size="30" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right"><s:label>Enable maintenance time check:</s:label>
					</td>
					<td>
						<s:radio list="#{'true':'true', 'false':'false'}" name="fetTranslatorConfig.enableMaintananceTimeCheck"
								id="fetTranslatorConfig_enableMaintananceTimeCheck"></s:radio>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right"><s:label>Maintenance hour from (0~23):</s:label>
					</td>
					<td><s:textfield
							id="fetTranslatorConfig_maintenanceHourFrom"
							name="fetTranslatorConfig.maintenanceHourFrom" maxlength="2"
							size="2" /><br /></td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right"><s:label>Maintenance hour to (0~23):</s:label>
					</td>
					<td><s:textfield id="fetTranslatorConfig_maintenanceHourTo"
							name="fetTranslatorConfig.maintenanceHourTo" maxlength="2"
							size="2" /><br /></td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" class="verticalDataTableCaption" align="right" valign="top">
						<s:label>Custom error code/message mapping:</s:label>
					</td>
					<td><s:textarea id="fetTranslatorConfig_errorCodeMessageMappingValue" maxlength="2000"
							name="fetTranslatorConfig.errorCodeMessageMappingValue" cols="80" rows="6" /><br />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="right">
						<s:submit value="儲存套用" onclick="javascript: return save();"></s:submit>
						<s:reset value="取消"></s:reset>
					</td>
				</tr>
			</table>
		</s:form>
	</div>
	
	<div id="flowControlConfigTab" class="grayBorder baseFont">
		<s:form namespace="/admin" action="applyFlowControlConfig"
			validate="false" theme="simple">
			<s:hidden name="tabIndex" value="2" />
			<s:hidden name="flowControlConfig.oid" />
			<table cellpadding="2" class="noBorder">
				<tr>
					<td class="verticalDataTableCaption" class="verticalDataTableCaption" align="right">
						<s:label>Max request per second:</s:label>
					</td>
					<td><s:textfield id="flowControlConfig_executePerSecond"
							name="flowControlConfig.executePerSecond" maxlength="8" size="4" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>Eenable retry:</s:label>
					</td>
					<td><s:radio id="flowControlConfig_enableRetry" name="flowControlConfig.enableRetry" 
							list="#{'true':'true', 'false':'false'}" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>Max retry:</s:label>
					</td>
					<td><s:textfield id="flowControlConfig_maxRertry"
							name="flowControlConfig.maxRertry" maxlength="3" size="2" /><br />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="right">
						<s:submit value="儲存套用" onclick="javascript: return save();"></s:submit>
						<s:reset value="取消"></s:reset>
					</td>
				</tr>
			</table>
		</s:form>
	</div>
	
	<div id="nedbConfigTab" class="grayBorder baseFont">
		<s:form namespace="/admin" action="applyNedbConfig"
			validate="false" theme="simple">
			<s:hidden name="tabIndex" value="3" />
			<s:hidden name="nedbConfig.oid" />
			<table cellpadding="2" class="noBorder">
				<tr>
					<td class="verticalDataTableCaption" class="verticalDataTableCaption" align="right" valign="top">
						<s:label>Batch query cell sql:</s:label>
					</td>
					<td><s:textarea id="nedbConfig_batchQueryCellSql" maxlength="500"
							name="nedbConfig.batchQueryCellSql" cols="80" rows="6" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>Import cell default PLMNID:</s:label>
					</td>
					<td><s:textfield id="nedbConfig_defaultPlmnId"
							name="nedbConfig.defaultPlmnId" maxlength="10" size="8" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right" valign="top">
						<s:label>Import APM file path:</s:label>
					</td>
					<td><s:textfield id="nedbConfig_importApmFilePath"
							name="nedbConfig.importApmFilePath" maxlength="100" size="60" /><br/>
						(path patten: using \${yyyy}, \${mm}, \${dd} to indicate a date related file path)<br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>Import APM file encoding:</s:label>
					</td>
					<td><s:textfield id="nedbConfig_importApmFileEncoding"
							name="nedbConfig.importApmFileEncoding" maxlength="30" size="10" /><br/>
					</td>
				</tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>Import APM file GZIP:</s:label>
					</td>
					<td>
						<s:radio list="#{'true':'true', 'false':'false'}" name="nedbConfig.importApmFileGzip"
								id="nedbConfig_importApmFileGzip"></s:radio>
					</td>
				<tr>
					<td class="verticalDataTableCaption" align="right" valign="top">
						<s:label>Export APM file path:</s:label>
					</td>
					<td><s:textfield id="nedbConfig_exportFilePath"
							name="nedbConfig.exportFilePath" maxlength="100" size="60" /><br/>
						(path patten: using \${yyyy}, \${mm}, \${dd} to indicate a date related file path)<br />
					</td>
				</tr>
				<!-- 
				<tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>Export APM file new line:</s:label>
					</td>
					<td><s:textfield id="nedbConfig_exportNewLine"
							name="nedbConfig.exportNewLine" maxlength="10" size="5" /><br />
					</td>
				</tr>
				 -->
				<tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>Export APM file separator:</s:label>
					</td>
					<td><s:textfield id="nedbConfig_exportSeparator"
							name="nedbConfig.exportSeparator" maxlength="10" size="5" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>Export APM file default rnc_id value:</s:label>
					</td>
					<td><s:textfield id="nedbConfig_exportDefaultRncId"
							name="nedbConfig.exportDefaultRncId" maxlength="10" size="6" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>Export APM file default beamDirection value:</s:label>
					</td>
					<td><s:textfield id="nedbConfig_exportDefaultBeamDirection"
							name="nedbConfig.exportDefaultBeamDirection" maxlength="5" size="3" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" class="verticalDataTableCaption" align="right">
						<s:label>Execute cell batch import:</s:label>
					</td>
					<td>
						<input type="button" value="import" 
							onclick="javascript: if (confirm('確定執行？')) {$('#executNedbCellBatchImportForm').submit(); } else {return false;}" />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" class="verticalDataTableCaption" align="right">
						<s:label>Execute APM export:</s:label>
					</td>
					<td>
						<input type="button" value="export" 
							onclick="javascript: if (confirm('確定執行？')) {$('#executApmExportForm').submit(); } else {return false;}" />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="right">
						<s:submit value="儲存套用" onclick="javascript: return save();"></s:submit>
						<s:reset value="取消"></s:reset>
					</td>
				</tr>
			</table>
		</s:form>
		<s:form id="executNedbCellBatchImportForm" namespace="/admin" action="executNedbCellBatchImport"
			validate="false" theme="simple" >
			<s:hidden name="tabIndex" value="3" />
		</s:form>
		<s:form id="executApmExportForm" namespace="/admin" action="executApmExport"
			validate="false" theme="simple" >
			<s:hidden name="tabIndex" value="3" />
		</s:form>
	</div>
	
	<div id="schedulingConfigTab" class="grayBorder baseFont">
		<s:form namespace="/admin" action="applySchedulingConfig"
			validate="false" theme="simple">
			<s:hidden name="tabIndex" value="4" />
			<s:hidden name="schedulingConfig.oid" />
			<table cellpadding="2" class="noBorder">
				<tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>Daily import cells time:</s:label>
					</td>
					<td><s:textfield id="schedulingConfig_dailySyncCellsTime"
							name="schedulingConfig.dailySyncCellsTime" maxlength="5" size="5" /> (HH:MM, HH:0~23, MM:0~59)<br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>Daily export APM file time:</s:label>
					</td>
					<td><s:textfield id="schedulingConfig_dailySyncFemtoProfilesTime"
							name="schedulingConfig.dailySyncFemtoProfilesTime" maxlength="5" size="5" /> (HH:MM, HH:0~23, MM:0~59)<br />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="right">
						<s:submit value="儲存套用" onclick="javascript: return save();"></s:submit>
						<s:reset value="取消"></s:reset>
					</td>
				</tr>
			</table>
		</s:form>
	</div>
	
	<div id="exceptionHandlerConfigTab" class="grayBorder baseFont">
		<s:form namespace="/admin" action="applyExceptionHandlerConfig"
			validate="false" theme="simple">
			<s:hidden name="tabIndex" value="5" />
			<s:hidden name="exceptionHandlerConfig.oid" />
			<table cellpadding="2" class="noBorder">
				<tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>FTP IP address:</s:label>
					</td>
					<td><s:textfield id="exceptionHandlerConfig_ftpIpAddress"
							name="exceptionHandlerConfig.ftpIpAddress" maxlength="20" size="15" /><br />
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>FTP login user:</s:label>
					</td>
					<td><s:textfield id="exceptionHandlerConfig_ftpLoginUser"
							name="exceptionHandlerConfig.ftpLoginUser" maxlength="50" size="15" /><br/>
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>FTP login password:</s:label>
					</td>
					<td><s:password id="exceptionHandlerConfig_ftpLoginPassword"
							name="exceptionHandlerConfig.ftpLoginPassword" maxlength="50" size="15" /><br/>
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" align="right">
						<s:label>FTP remote dir:</s:label>
					</td>
					<td><s:textfield id="exceptionHandlerConfig_ftpRemoteDir"
							name="exceptionHandlerConfig.ftpRemoteDir" maxlength="100" size="20" /><br/>
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" class="verticalDataTableCaption" align="right" valign="top">
						<s:label>Error Code list:</s:label>
					</td>
					<td><s:textarea id="exceptionHandlerConfig_errorCodeListValue" maxlength="500"
							name="exceptionHandlerConfig.errorCodeListValue" cols="80" rows="6" /><br />
						(comma separated value, eg: "11000, 12000, 13001")
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" class="verticalDataTableCaption" align="right" valign="top">
						<s:label>Caller list:</s:label>
					</td>
					<td><s:textarea id="exceptionHandlerConfig_callerListValue" maxlength="500"
							name="exceptionHandlerConfig.callerListValue" cols="80" rows="6" /><br />
						(comma separated value, eg: "1111111111, 2222222222, 3333333333")
					</td>
				</tr>
				<tr>
					<td class="verticalDataTableCaption" class="verticalDataTableCaption" align="right">
						<s:label>Test FTP connection:</s:label>
					</td>
					<td>
						<input type="button" value="test" 
							onclick="javascript: if (confirm('確定執行？')) {$('#testExceptionHandlerFtpConnectionForm').submit(); } else {return false;}" />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="right">
						<s:submit value="儲存套用" onclick="javascript: return save();"></s:submit>
						<s:reset value="取消"></s:reset>
					</td>
				</tr>
			</table>
		</s:form>
		<s:form id="testExceptionHandlerFtpConnectionForm" namespace="/admin" action="testExceptionHandlerFtpConnection"
			validate="false" theme="simple" >
			<s:hidden name="tabIndex" value="5" />
		</s:form>
	</div>
	
</div>