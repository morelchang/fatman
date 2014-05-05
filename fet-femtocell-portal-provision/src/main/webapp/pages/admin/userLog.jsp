<%@ page language="java" contentType="text/html; charset=UTF8"
	pageEncoding="UTF8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="jscalendar" uri="/jscalendar" %>
<script type="text/javascript">
<!--
function clearSearchCriteria() {
	$('#searchForm input[type="text"]').each(function(count, input) {
		$(input).val('');
	});
	$('#searchForm select').each(function(count, select) {
		select.selectedIndex = '0';
	});
}

function validateSearch() {
	var validated = false;
	$('#searchForm input[type="text"]').each(function(count, input) {
		if ($.trim($(input).val()) != '') {
			validated = true;
		}
	});
	$('#searchForm select').each(function(count, select) {
		if (select.selectedIndex != '0') {
			validated = true;
		}
	});
	if (!validated) {
		alert('至少輸入一個查詢條件');
	}
	return validated;
}
//-->
</script>

<!-- title -->
<div>
	<h3>使用記錄查詢</h3>
</div>

<!-- content -->
<s:include value="/pages/templates/messages.jsp"></s:include>

<table cellpadding="0" cellspacing="0" width="100%" class="noBorder">
	<tr><td>
		<div class="grayBorder">
			<s:form id="searchForm" namespace="/admin" action="searchUserLog" validate="false" theme="simple">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="noBorder">
					<tr>
						<td align="right">
							<s:label>類別:</s:label>
						</td>
						<td>
							<s:select id="searchCriteria_type" name="searchCriteria.type" list="availableUserLogTypes" 
								listValue="%{getText('UserLogType.' + toString())}"
								headerKey="%{null}" headerValue="===請選擇===" />
						</td>
						<td align="right">
							<s:label>操作者ID:</s:label>
						</td>
						<td>
							<s:textfield id="searchCriteria_operatorId" name="searchCriteria.operatorId" maxlength="50" size="10"/>
						</td>
						<td align="right">
							<s:label>操作者姓名:</s:label>
						</td>
						<td>
							<s:textfield id="searchCriteria_operatorName" name="searchCriteria.operatorName" maxlength="50" size="6"/>
						</td>
						<td align="right">
							<s:label>操作者來源IP:</s:label>
						</td>
						<td colspan="2">
							<s:textfield id="searchCriteria_operatorSourceIp" name="searchCriteria.operatorSourceIp" maxlength="50" size="12"/>
						</td>
					</tr>
					<tr>
						<td align="right">
							<s:label>用戶名稱:</s:label>
						</td>
						<td>
							<s:textfield id="searchCriteria_userName" name="searchCriteria.userName" maxlength="50" size="6"/>
						</td>
						<td align="right">
							<s:label>用戶門號:</s:label>
						</td>
						<td>
							<s:textfield id="searchCriteria_userMobile" name="searchCriteria.userMobile" maxlength="10" size="10"/>
						</td>
						<td align="right">
							<s:label>APEI:</s:label>
						</td>
						<td>
							<s:textfield id="searchCriteria_profileApei" name="searchCriteria.profileApei" maxlength="20" size="20"/>
						</td>
						<td align="right">
							<s:label>IMSI:</s:label>
						</td>
						<td colspan="2">
							<s:textfield id="searchCriteria_profileImsi" name="searchCriteria.profileImsi" maxlength="15" size="15"/>
						</td>
					</tr>
					<tr>
						<td align="right">
							<s:label>時間:</s:label>
						</td>
						<td colspan="3">
							<jscalendar:jscalendar id="searchCriteria_startCreateTime" name="startCreateTime" 
								format="%{@tw.com.mds.fet.femtocellportal.web.converter.JsCalendarDateConverter@JS_CALENDAR_DATE_FORMAT}"
								maxlength="19" showstime="19" size="16"/>
							~
							<jscalendar:jscalendar id="searchCriteria_endCreateTime" name="endCreateTime"
								format="%{@tw.com.mds.fet.femtocellportal.web.converter.JsCalendarDateConverter@JS_CALENDAR_DATE_FORMAT}"
								maxlength="19" showstime="19" size="16"/>
						</td>
						<td align="right">
							<s:label>說明關鍵字:</s:label>
						</td>
						<td colspan="3">
							<s:textfield id="searchCriteria_content" name="searchCriteria.content" maxlength="30" size="30"/>
						</td>
						<td colspan="2">
							<s:submit value="查詢" onclick="javascript: return validateSearch();"></s:submit>
							<input type="button" value="清除" onclick="javascript: clearSearchCriteria(); return false;" />
						</td>
					</tr>
				</table>
			</s:form>
		</div><br/>
	</td></tr>
	<tr><td>
		<table border="1" cellpadding="10px"  width="100%" class="searchResultTable">
			<tr class="searchResultTableHeader">
				<td rowspan="2" width="60px">時間</td>
				<td rowspan="2">類別</td>
				<td colspan="3">操作者</td>
				<td colspan="2">處理用戶</td>
				<td colspan="2">處理AP</td>
				<td rowspan="2">說明</td>
			</tr>
			<tr class="searchResultTableHeader">
				<td>ID</td>
				<td>名稱</td>
				<td>來源</td>
				<td>名稱</td>
				<td>門號</td>
				<td>APEI</td>
				<td>IMSI</td>
			</tr>
			<s:iterator value="searchResult">
			<tr>
				<td><s:date name="createTime" format="yyyy/MM/dd HH:mm:ss" /></td>
				<td><s:text name="UserLogType.%{type}" /></td>
				<td><s:property value="operatorId" /></td>
				<td><s:property value="operatorName" /></td>
				<td><s:property value="operatorSourceIp" /></td>
				<td><s:property value="userName" /></td>
				<td><s:property value="userMobile" /></td>
				<td><s:property value="profileApei" /></td>
				<td><s:property value="profileImsi" /></td>
				<td><s:property value="content" /></td>
			</tr>
			</s:iterator>
		</table>
	</td></tr>
</table>

