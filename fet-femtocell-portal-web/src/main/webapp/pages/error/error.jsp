<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript">
<!--
$(function() {
	$('#errorDetail').hide();
});

function showHideDetail() {
	if ($('#errorSummary').is(':visible')) {
		$('#errorSummary').hide();
		$('#errorDetail').show();
	} else {
		$('#errorSummary').show();
		$('#errorDetail').hide(); 
	}
}
//-->
</script>

<!-- title -->
<table class="noBorder" align="center" width="100%">
<tr><td>
	<div>
		<h3>系統錯誤</h3>
	</div>

	<!-- content -->
	<div class="grayBorder">
		錯誤原因:<br/>
		<s:property value="exception" />
	</div><br/>
	
	<div id="errorSummary" class="grayBorder" style="cursor: pointer;" onclick="javascript: showHideDetail();">
		<span>點選此查看詳細資訊...</span>
	</div>
	
	<div id="errorDetail" class="grayBorder" style="cursor: pointer;" onclick="javascript: showHideDetail();">
		詳細資訊:<br/>
		<s:property value="exceptionStack" />
	</div>
</td></tr>
<tr>
	<td align="right">
		<input type="button" value="回上頁" onclick="javascript: history.back();" />
	</td>
</tr>
</table>
