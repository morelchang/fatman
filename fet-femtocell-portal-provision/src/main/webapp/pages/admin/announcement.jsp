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
	$('#editForm textarea').each(function(count, input) {
		$(input).val('');
	});
	$('#editForm select').each(function(count, select) {
		select.selectedIndex = '0';
	});
}

function validateSearch() {
	if(!$('#searchCriteria_userType').val() 
			&& $.trim($('#searchCriteria_content').val()) == '' 
			&& $.trim($('#searchCriteria_title').val()) == '') {
		alert('請輸入查詢條件');
		return false;
	}
	return true;
}

function validateSave() {
	var message = '';
	if(!$('#current_userType').val()) {
		message += '請輸入公告對象\n';
	}
	if($.trim($('#current_title').val()) == '') {
		message += '請輸入公告標題\n';
	}
	if($.trim($('#current_content').val()) == '') {
		message += '請輸入公告內容\n';
	}
	if($.trim($('#current_startTime').val()) == '') {
		message += '請輸入公告起始時間\n';
	}
	if($.trim($('#current_endTime').val()) == '') {
		message += '請輸入公告結束時間\n';
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
	<h3>系統公告維護</h3>
</div>

<!-- content -->
<s:include value="/pages/templates/messages.jsp"></s:include>

<div id="tabs">
	<ul>
		<li><a href="#search" class="noloading">公告查詢</a></li>
		<li><a href="#create" class="noloading" onclick="javascript: $('#editForm').submit()">公告新增/修改</a></li>
	</ul>
	
	<s:form id="editForm" namespace="/admin" action="editAnnouncement" validate="false" theme="simple">
		<s:hidden name="current.oid" value=""></s:hidden>
	</s:form>
						
	<div id="search" class="grayBorder baseFont">
		<div class="grayBorder">
			<table width="100%" border="0" class="noBorder">
				<tr>
					<td>
						<s:form id="searchForm" namespace="/admin" action="searchAnnouncement" validate="false" theme="simple">
							<s:label>公告對象:</s:label>
							<s:select id="searchCriteria_userType" name="searchCriteria.userType" list="availableUserTypes" 
								listValue="%{getText('UserType.' + toString())}"
								headerKey="%{null}" headerValue="===請選擇===" />
							
							<s:label>公告標題:</s:label>
							<s:textfield id="searchCriteria_title" name="searchCriteria.title" maxlength="25" size="15"/>
							
							<s:label>公告內容:</s:label>
							<s:textfield id="searchCriteria_content" name="searchCriteria.content" maxlength="30" size="15"/>
							
							<s:submit value="查詢" onclick="javascript: return validateSearch();"></s:submit>
							<input type="button" value="清除" onclick="javascript: clearSearchCriteria(); return false;" />
						</s:form>
					</td>
				</tr>
			</table>
		</div><br/>
		
		<table border="1" cellpadding="10px"  width="100%" class="searchResultTable">
			<tr class="searchResultTableHeader">
				<td>對象</td>
				<td>標題</td>
				<td>內容</td>
				<td>起始時間</td>
				<td>結束時間</td>
				<td>新增時間</td>
				<td>修改時間</td>
				<td>操作</td>
			</tr>
			<s:iterator value="searchResult">
			<tr>
				<td><s:hidden value="oid" /><s:text name="UserType.%{userType}" /></td>
				<td><s:property value="title" /></td>
				<td>
					<s:if test="content.length() > 20" >
					<s:property value="content.substring(0, 20)" />...
					</s:if>
					<s:else>
					<s:property value="content" />
					</s:else>
				</td>
				<td><s:date name="startTime" format="yyyy/MM/dd HH:mm:ss" /></td>
				<td><s:date name="endTime" format="yyyy/MM/dd HH:mm:ss" /></td>
				<td><s:date name="createTime" format="yyyy/MM/dd HH:mm:ss" /></td>
				<td><s:date name="updateTime" format="yyyy/MM/dd HH:mm:ss" /></td>
				<td>
					<s:a namespace="/admin" action="editAnnouncement" ><s:param name="current.oid" value="oid" />修改</s:a>
					<s:a cssClass="removeLink" namespace="/admin" action="deleteAnnouncement" ><s:param name="current.oid" value="oid" />刪除</s:a>
				</td>
			</tr>
			</s:iterator>
		</table>
	</div>
	
	<div id="create" class="grayBorder baseFont">
		<span>新增/修改公告</span>
		<s:form id="editForm" namespace="/admin" action="saveAnnouncement" validate="false" theme="simple" >
			<s:hidden name="current.oid" />
			
			<table border="0" class="noBorder">
				<tr>
					<td align="right">
						<s:label>公告對象:</s:label>
					</td>
					<td>
						<s:select id="current_userType" name="current.userType" list="availableUserTypes" 
							listValue="%{getText('UserType.' + toString())}"
							headerKey="%{null}" headerValue="===請選擇===" />
					</td>
				</tr>
				<tr>
					<td align="right">
						<s:label>公告起始時間:</s:label>
					</td>
					<td>
						<jscalendar:jscalendar id="current_startTime" name="current.startTime" 
							format="%{@tw.com.mds.fet.femtocellportal.web.converter.JsCalendarDateConverter@JS_CALENDAR_DATE_FORMAT}"
							maxlength="19" showstime="19" />
					</td>
				</tr>
				<tr>
					<td align="right">
						<s:label>公告結束時間:</s:label>
					</td>
					<td>
						<jscalendar:jscalendar id="current_endTime" name="current.endTime"
							format="%{@tw.com.mds.fet.femtocellportal.web.converter.JsCalendarDateConverter@JS_CALENDAR_DATE_FORMAT}"
							maxlength="19" showstime="19" />
					</td>
				</tr>
				<tr>
					<td align="right">
						<s:label>公告標題:</s:label>
					</td>
					<td>
						<s:textfield id="current_title" name="current.title" maxlength="50" size="50"/>
					</td>
				</tr>
				<tr valign="top">
					<td align="right">
						<s:label>公告內容:</s:label>
					</td>
					<td>
						<s:textarea  id="current_content" name="current.content" cols="80" rows="10" mexlength="800" />
					</td>
				</tr>
				<tr>
					<td align="right" colspan="2">
						<s:submit value="儲存" onclick="javascript: return save();"></s:submit>
						<input type="button" value="清除" onclick="javascript: clearCurrent(); return false;" />
						<s:reset value="復原"></s:reset>
					</td>
				</tr>
			</table>
		</s:form>
	</div>
</div>
