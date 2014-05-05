<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript">
<!--
$(function() {
	$.unblockUI();
});

function clearSearchText() {
	$('#searchUserName').val('');
	$('#searchUserMobile').val('');
}
//-->
</script>

<!-- title -->
<div>
	<h3>用戶查詢</h3>
</div>

<!-- content -->
<s:include value="/pages/templates/messages.jsp"></s:include>

<div style="width: 98%;" class="grayBorder">
	<s:form id="searchForm" namespace="/provision" action="searchUser" validate="false" theme="simple">
		<s:label for="searchUserName">用戶姓名:</s:label>
		<s:textfield id="searchUserName" name="searchUserName" maxlength="50" size="15"/>
		
		<s:label for="searchUserMobile">用戶門號:</s:label>
		<s:textfield id="searchUserMobile" name="searchUserMobile" maxlength="10" size="10"/>
		
		<s:submit value="查詢" />
		<input type="button" value="清除" onclick="javascript: clearSearchText();" />
	</s:form>
</div><br/>

<table border="1" width="100%" cellpadding="5px" class="searchResultTable">
	<thead>
		<tr class="searchResultTableHeader">
			<td>姓名</td>
			<td>用戶門號</td>
			<td>建立時間</td>
			<td>異動時間</td>
			<td>操作</td>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="searchResult">
		<tr valign="top">
			<td><s:property value="userName" />
			</td>
			<td><s:property value="mobile" />
			</td>
			<td><s:date name="createTime" />
			</td>
			<td><s:date name="updateTime" />
			</td>
			<td>
				<s:url var="createProfileUrl" namespace="/provision" action="displayCreateProfile">
					<s:param name="current.oid" value="oid"></s:param>
				</s:url>
				
				<s:if test="%{mode.toString() == 'CREATE_PROFILE'}">
				<a href="<s:property value="createProfileUrl"/>">申請Femto</a>
				</s:if> 
			</td>
		</tr>
		</s:iterator>
	</tbody>
</table>
