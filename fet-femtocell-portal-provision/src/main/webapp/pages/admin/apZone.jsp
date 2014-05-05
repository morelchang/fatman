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
}

function validateSearch() {
// 	if($.trim($('#searchCriteria_name').val()) == '') {
// 		alert('請輸入查詢條件');
// 		return false;
// 	}
	return true;
}

function validateSave() {
	var message = '';
	if($.trim($('#current_name').val()) == '') {
		message += '請輸入AP Zone名稱\n';
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
	<h3>AP Zone維護</h3>
</div>

<!-- content -->
<s:include value="/pages/templates/messages.jsp"></s:include>

<div id="tabs">
	<ul>
		<li><a href="#search" class="noloading">AP Zone查詢</a></li>
		<li><a href="#create" class="noloading" onclick="javascript: $('#editForm').submit()">AP Zone新增/修改</a></li>
	</ul>
	
	<s:form id="editForm" namespace="/admin" action="editApZone" validate="false" theme="simple">
		<s:hidden name="current.oid" value=""></s:hidden>
	</s:form>

	<div id="create" class="grayBorder baseFont">
		<span>新增/修改AP Zone</span>
		<s:form id="editForm" namespace="/admin" action="saveApZone" validate="false" theme="simple" >
			<s:hidden name="current.oid" />
			
			<table border="0" class="noBorder">
				<tr>
					<td align="right">
						<s:label>AP Zone名稱:</s:label>
					</td>
					<td>
						<s:textfield id="current_name" name="current.name" maxlength="50" size="30"/>
					</td>
				</tr>
				<tr>
					<td align="right" colspan="2">
						<s:submit value="儲存" onclick="javascript: return save();" ></s:submit>
						<input type="button" value="清除" onclick="javascript: clearCurrent(); return false;" />
						<s:reset value="復原"></s:reset>
					</td>
				</tr>
			</table>
		</s:form>
	</div>
	
	<div id="search" class="grayBorder baseFont">
		<div class="grayBorder">
			<table width="100%" border="0" class="noBorder">
				<tr>
					<td>
						<s:form id="searchForm" namespace="/admin" action="searchApZone" validate="false" theme="simple">
							<s:label>AP Zone名稱:</s:label>
							<s:textfield id="searchCriteria_name" name="searchCriteria.name" maxlength="25" size="25"/>
							
							<s:submit value="查詢" onclick="javascript: return validateSearch();"></s:submit>
							<input type="button" value="清除" onclick="javascript: clearSearchCriteria(); return false;" />
						</s:form>
					</td>
				</tr>
			</table>
		</div><br/>
		
		<table border="1" cellpadding="10px"  width="100%" class="searchResultTable">
			<tr class="searchResultTableHeader">
				<td>AP Zone名稱</td>
				<td>新增時間</td>
				<td>修改時間</td>
				<td>操作</td>
			</tr>
			<s:iterator value="searchResult">
			<tr>
				<td><s:hidden value="oid" /><s:property value="name" /></td>
				<td><s:date name="createTime" format="yyyy/MM/dd HH:mm:ss" /></td>
				<td><s:date name="updateTime" format="yyyy/MM/dd HH:mm:ss" /></td>
				<td>
					<s:a namespace="/admin" action="editApZone" ><s:param name="current.oid" value="oid" />修改</s:a>
					<s:a cssClass="removeLink" namespace="/admin" action="deleteApZone"><s:param name="current.oid" value="oid" />刪除</s:a>
				</td>
			</tr>
			</s:iterator>
		</table>
	</div>
</div>
