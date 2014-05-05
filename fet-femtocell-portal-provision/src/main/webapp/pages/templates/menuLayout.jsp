<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="jscalendar" uri="/jscalendar" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Femtocell Provision</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<link rel=StyleSheet href='<s:url value="/css/styles.css" />' type="text/css" />
<link rel=StyleSheet href='<s:url value="/css/styles.css" />' type="text/css" />
<link rel="stylesheet" href='<s:url value="/css/smoothness/jquery-ui-1.8.13.custom.css" />' type="text/css"/>

<script type="text/javascript" src='<s:url value="/scripts/jquery-1.5.2.min.js" />'></script> 
<script type="text/javascript" src='<s:url value="/scripts/jquery.blockUI.js" />'></script>
<script type="text/javascript" src='<s:url value="/scripts/jquery.qtip-1.0.0-rc3.min.js" />'></script>
<script type="text/javascript" src='<s:url value="/scripts/jquery-ui-1.8.13.custom.min.js" />'></script>
<script type="text/javascript" src='<s:url value="/scripts/utils.js" />'></script>

<s:head />
<jscalendar:head theme="xhtml" language="en" />
<s:include value="/pages/templates/commonHeader.jsp"></s:include>

</head>
<body>
<table width="100%" border="0" cellpadding="10px" cellspacing="0">
	<tr height="50px" valign="top" bgcolor="red">
		<!-- header -->
		<td colspan="2">
			<table width="100%" border="0" class="noBorder headerText" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<span class="fetnetTitle">遠傳Femto管理系統</span>
					</td>
					<td align="right">
						<s:if test="loginFemtoUser != null">
						目前登入:<s:property value="loginFemtoUser.account"/>&nbsp;&nbsp;
						<span>
							[<s:a cssClass="logoutLink noloading" style="color:white;" namespace="/loginFemtoUser" 
								action="logoutFemtoUser" onclick="return confirm('確定登出系統?');">登出</s:a>]
						</span>
						</s:if>

						<s:if test="loginAdminUser != null">
						目前登入:<s:property value="loginAdminUser.account"/>&nbsp;&nbsp;
						<span>
							[<s:a cssClass="logoutLink noloading" style="color:white;" namespace="/login" 
								action="logoutAdmin" onclick="return confirm('確定登出系統?');">登出</s:a>]
						</span>
						</s:if>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr height="600px">
		<!-- content -->
		<td valign="top" width="14%">
			<s:include value="/pages/templates/menu.jsp"></s:include>
		</td>
		<td valign="top">
			<tiles:insertAttribute name="content" />
		</td>
	</tr>
</table>

<s:include value="/pages/templates/loading.jsp"></s:include>

</body>
</html>
