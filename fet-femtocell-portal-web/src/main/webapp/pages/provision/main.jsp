<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript">
<!--
$(function() {
	$.unblockUI();
});
//-->
</script>

<!-- title -->
<div>
	<h3>歡迎登入Femtocell用戶專區</h3>
</div>

<!-- content -->
<s:if test="%{underMaintenance}">
<div style="width: 98%;" class="grayBorder">
	<table id="announcementTable" cellpadding="5" cellspacing="0" class="noBorder">
		<thead>
			<tr><td>
				<span>系統維護通知</span>
			</td></tr>
		</thead>
		<tbody>
			<tr>
				<td class="announcementTitle">
					系統維護中，維護時間自<s:property value="maintenanceHourFrom" />時～<s:property value="maintenanceHourTo" />時，請稍後再登入
				</td>
			</tr>
		</tbody>
	</table>
</div><br/>
</s:if>

<div style="width: 98%;" class="grayBorder">
	<table id="announcementTable" cellpadding="5" cellspacing="0" class="noBorder">
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
			<tr><td class="announcementTitle">
				<span>
					<s:property value="title" />
				</span>&nbsp;
				<span>[<s:property value="startTime" />]</span>
				<br/>
			</td></tr>
			<tr><td class="announcementContent"><span><s:property value="content" /></span>&nbsp;<br/>
			</td></tr>
			</s:iterator>
		</s:else>
		</tbody>
	</table>
</div><br/>
