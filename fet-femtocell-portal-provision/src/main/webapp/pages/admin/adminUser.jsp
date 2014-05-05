<%@ page language="java" contentType="text/html; charset=UTF8"
	pageEncoding="UTF8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="jscalendar" uri="/jscalendar" %>
<script type="text/javascript">
<!--
$(function() {
	var tabs = $('#tabs').tabs();
	var tabIndex = 0;
	if ('<s:property value="mode" />' == 'EDIT') {
		tabIndex = 1;
	}
	tabs.tabs('select', tabIndex);

	$('.removeLink').each(function(i) {
		$(this).click(function(event) {
			var removing = confirm("確定刪除?");
			if (!removing) {
				// to prevent loading message
				event.stopImmediatePropagation();
			}
			return removing;	
		});
	});
});

function save() {
	if (!validateSave()) {
		return false;
	}
	return confirm('確定儲存?');	
}

function clearSearchCriteria() {
	$('#searchForm input[type="text"]').each(function(count, input) {
		$(input).val('');
	});
	$('#searchForm select').each(function(count, select) {
		select.selectedIndex = '0';
	});
}

function clearCurrent() {
	$('#editForm input[type="text"]').each(function(count, input) {
		$(input).val('');
	});
	$('#editForm select').each(function(count, select) {
		select.selectedIndex = '0';
	});
	$('#editForm input[type="checkbox"]').each(function(count, input) {
		input.checked = false;
	});
}

function validateSearch() {
	if($.trim($('#searchCriteria_userId').val()) == ''
			&& $.trim($('#searchCriteria_userName').val()) == ''
			&& $.trim($('#searchCriteria_account').val()) == ''
			&& $.trim($('#searchCriteria_email').val()) == '') {
		alert('請輸入查詢條件');
		return false;
	}
	return true;
}

function validateSave() {
	var message = '';
	if($.trim($('#current_userId').val()) == '') {
		message += '請輸入使用者ID\n';
	}
	if($.trim($('#current_userName').val()) == '') {
		message += '請輸入使用者名稱\n';
	}
	if($.trim($('#current_account').val()) == '') {
		message += '請輸入登入帳號\n';
	}
	if ($('#current_password').val() != $('#current_confirmPassword').val()) {
		message += '確認登入密碼與密碼不一致\n';
	}
	if (message != '') {
		alert(message);
		return false;
	}
	return true;
}
//-->
</script>

<!-- title -->
<div>
	<h3>前台人員權限維護</h3>
</div>

<!-- content -->
<s:include value="/pages/templates/messages.jsp"></s:include>

<div id="tabs">
	<ul>
		<li><a href="#search" class="noloading">前台人員查詢</a></li>
		<li><a href="#create" class="noloading" onclick="javascript: $('#editForm').submit()">前台人員新增/修改</a></li>
	</ul>
	
	<s:form id="editForm" namespace="/admin" action="editAdminUser" validate="false" theme="simple">
		<s:hidden name="current.oid" value=""></s:hidden>
	</s:form>
	
	<div id="search" class="noBorder baseFont">
		<div class="grayBorder">
			<table width="100%" border="0" class="noBorder">
				<tr>
					<td>
						<s:form id="searchForm" namespace="/admin" action="searchAdminUser" validate="false" theme="simple">
							<s:label>ID:</s:label>
							<s:textfield id="searchCriteria_userId" name="searchCriteria.userId" maxlength="50" size="10"/>
							
							<s:label>名稱:</s:label>
							<s:textfield id="searchCriteria_userName" name="searchCriteria.userName" maxlength="50" size="25"/>
							
							<s:label>登入帳號:</s:label>
							<s:textfield id="searchCriteria_account" name="searchCriteria.account" maxlength="80" size="15"/>
							
							<s:label>E-Mail:</s:label>
							<s:textfield id="searchCriteria_email" name="searchCriteria.email" maxlength="50" size="30"/>
							
							<s:submit value="查詢" onclick="javascript: return validateSearch();"></s:submit>
							<input type="button" value="清除" onclick="javascript: clearSearchCriteria(); return false;" />
						</s:form>
					</td>
				</tr>
			</table>
		</div><br/>
		
		<table border="1" cellpadding="10px"  width="100%" class="searchResultTable">
			<tr class="searchResultTableHeader">
				<td>ID</td>
				<td>名稱</td>
				<td>登入帳號</td>
				<td>E-Mail</td>
				<td>新增時間</td>
				<td>修改時間</td>
				<td>權限</td>
				<td>操作</td>
			</tr>
			<s:iterator value="searchResult">
			<tr>
				<td><s:hidden value="oid" /><s:property value="userId" /></td>
				<td><s:property value="userName" /></td>
				<td><s:property value="account" /></td>
				<td><s:property value="email" /></td>
				<td><s:date name="createTime" format="yyyy/MM/dd HH:mm:ss" /></td>
				<td><s:date name="updateTime" format="yyyy/MM/dd HH:mm:ss" /></td>
				<td>
					<table class="noBorder">
					<s:iterator value="permissions" var="permission" status="status">
						<s:if test="%{#status.count % 3 == 1}">
						<tr>
						</s:if>
						
						<td>
							<label>
								<s:checkbox name="permissionResult" fieldValue="%{#permission.adminUser.oid},{#permission.resourceId}" 
										value="%{#permission.enabled}" theme="simple" disabled="true"/>
								<s:if test="%{!#permission.enabled}">
								<span style="color:#A5A5A5;">
								</s:if>
									<s:text name="ResourceId.%{#permission.resourceId}" />
								<s:if test="%{!#permission.enabled}">
								</span>
								</s:if>
							</label>
						</td>
						
						<s:if test="%{#status.count % 3 == 0}">
						</tr>
						</s:if>
					</s:iterator>
					</table>
				</td>
				<td>
					<s:a namespace="/admin" action="editAdminUser" ><s:param name="current.oid" value="oid" />修改</s:a><br/>
					<s:a cssClass="removeLink" namespace="/admin" action="deleteAdminUser" ><s:param name="current.oid" value="oid" />刪除</s:a>
				</td>
			</tr>
			</s:iterator>
		</table>
	</div>
	
	<div id="create" class="grayBorder baseFont">
		<span>新增/修改前台人員</span>
		<s:form id="editForm" namespace="/admin" action="saveAdminUser" validate="false" theme="simple" >
			<s:hidden name="current.oid" />
			<table class="noBorder" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td align="right">
						<s:label>使用者ID:</s:label>
					</td>
					<td>
						<s:textfield id="current_userId" name="current.userId" maxlength="10" size="10"/>
					</td>
				</tr>
				<tr>
					<td align="right">
						<s:label>使用者名稱:</s:label>
					</td>
					<td>
						<s:textfield id="current_userName" name="current.userName" maxlength="50" size="10"/>
					</td>
				</tr>
				<tr>
					<td align="right">								
						<s:label>登入帳號:</s:label>
					</td>
					<td>
						<s:textfield id="current_account" name="current.account" maxlength="50" size="15"/>
					</td>
				</tr>
				<tr>
					<td align="right">
						<s:label>登入密碼:</s:label>
					</td>
					<td>
						<s:password id="current_password" name="current.password" value="current.password" maxlength="50" size="15"/>
					</td>
				</tr>
				<tr>
					<td align="right">								
						<s:label>確認登入密碼:</s:label>
					</td>
					<td>
						<s:password id="current_confirmPassword" value="current.password" maxlength="50" size="15"/>
					</td>
				</tr>
				<tr>
					<td align="right">
						<s:label>EMail:</s:label>
					</td>
					<td>
						<s:textfield id="current_email" name="current.email" maxlength="80" size="30"/>
					</td>
				</tr>
				<tr>
					<td align="right" valign="top">
						<s:label>權限設定:</s:label>
					</td>
					<td>
						<table class="noBorder">
						<s:iterator value="current.permissions" var="permission" status="status">
							<s:if test="%{#status.count % 5 == 1}">
							<tr>
							</s:if>
							
							<td>
								<label>
									<s:checkbox name="permissionResult" fieldValue="%{#permission.adminUser.oid},%{#permission.resourceId}" 
											value="%{#permission.enabled}" theme="simple" />
									<s:text name="ResourceId.%{#permission.resourceId}" />,
								</label>
							</td>
							
							<s:if test="%{#status.count % 5 == 0}">
							</tr>
							</s:if>
						</s:iterator>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="right">
						<s:submit value="儲存" onclick="javascript: return save();" ></s:submit>
						<input type="button" value="清除" onclick="javascript: clearCurrent(); return false;" />
						<s:reset value="取消"></s:reset>
					</td>
				</tr>
			</table>
		</s:form>
	</div>
</div>
