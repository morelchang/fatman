<%@ page language="java" contentType="text/html; charset=UTF8"
	pageEncoding="UTF8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!-- menu bar -->
<div id="menu" style="line-height: 135%">
	<table border="0" class="noBorder">
		<tr>
			<td nowrap="nowrap">
				<div>
					&nbsp;&nbsp;<s:a namespace="%{serverContext}/femtouser" action="main">回首頁</s:a><br/>
					<s:if test="%{underMaintenance}">
					&nbsp;&nbsp;<span style="color:#AEAEAE">[功能維護中]</span>
					</s:if>
					<s:else>
					&nbsp;&nbsp;<s:a namespace="%{serverContext}/femtouser" action="displaySerchProfiles">AP管理</s:a><br/>
					</s:else>
				</div>
				<br/>
			</td>
		</tr>
	</table>
	
	<s:if test="%{!hideMenuAnnouncement}">
	<div class="grayBorder">
		<table id="announcementTable" cellpadding="3" cellspacing="0" class="noBorder">
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
