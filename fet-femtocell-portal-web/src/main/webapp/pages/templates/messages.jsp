<%@ page language="java" contentType="text/html; charset=UTF8"
	pageEncoding="UTF8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!-- menu bar -->
<s:if test="%{hasActionMessages()}">
<div id="actionMessages" class="grayBorder" style="cursor: pointer; background-color: #CCFFCC;" 
	onclick="javascript: $('#actionMessages').fadeOut(); ">
	<table border="0" class="noBorder">
		<tr>
			<td align="right">
				<img src="<s:url value='%{serverContext}/images/checkmark24.png'/>" />
			</td>
			<td align="left">
				<span><s:actionmessage  theme="simple" /></span>
			</td>
		</tr>
	</table>
</div><br/>
</s:if>

<s:if test="%{hasActionErrors() || exception != null}">
<div id="actionMessages" class="grayBorder" style="cursor: pointer; background-color: #FFCCCC;" 
	onclick="javascript: $('#actionMessages').fadeOut(); ">
	<table border="0" class="noBorder">
		<tr>
			<td align="right">
				<img src="<s:url value='%{serverContext}/images/exclamation24.png'/>" />
			</td>
			<td align="left">
				<span><s:property value="exception" /></span>
				<span><s:actionerror theme="simple" /></span>
			</td>
		</tr>
	</table>
</div><br/>
</s:if>
