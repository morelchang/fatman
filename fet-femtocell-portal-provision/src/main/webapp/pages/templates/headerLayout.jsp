<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Femtocell Provision</title>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<link rel=StyleSheet href='<s:url value="/css/styles.css" />' type="text/css" />

<script type="text/javascript" src='<s:url value="/scripts/jquery-1.5.2.min.js" />'></script>
<script type="text/javascript" src='<s:url value="/scripts/jquery.blockUI.js" />'></script>
<script type="text/javascript" src='<s:url value="/scripts/jquery.qtip-1.0.0-rc3.min.js" />'></script>
<s:head />
<s:include value="/pages/templates/commonHeader.jsp"></s:include>
</head>

<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr height="50px" valign="top" bgcolor="red">
		<!-- header -->
		<td valign="middle" colspan="2"><span class="fetnetTitle">遠傳Femto管理系統</span></td>
	</tr>
	<tr height="600px">
		<td valign="top">
		<tiles:insertAttribute name="content"></tiles:insertAttribute>
		</td>
	</tr>
</table>

<s:include value="/pages/templates/loading.jsp"></s:include>

</body>
</html>