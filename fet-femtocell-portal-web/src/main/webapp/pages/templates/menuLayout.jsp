<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="jscalendar" uri="/jscalendar" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Femtocell Portal</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel=StyleSheet href='<s:url value="%{serverContext}/css/styles.css" />' type="text/css" />
<script type="text/javascript" src='<s:url value="%{serverContext}/scripts/jquery-1.5.2.min.js" />'></script> 
<script type="text/javascript" src='<s:url value="%{serverContext}/scripts/jquery.blockUI.js" />'></script>
<script type="text/javascript" src='<s:url value="%{serverContext}/scripts/jquery.qtip-1.0.0-rc3.min.js" />'></script>

<s:head theme="simple" />
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
						<s:if test="ssoLogin">
						目前登入:<s:property value="loginFemtoUser.mobile"/>&nbsp;&nbsp;
						</s:if>
						<s:else>
						目前登入:<s:property value="loginFemtoUser.account"/>&nbsp;&nbsp;
						</s:else>
						
						<s:if test="ssoLogin">
						<span>
							[<a href="#" class="logoutLink noloading" style="color:white;" onclick="javascript: if (confirm('確定登出系統?')) { $('#ssoLogoutForm').submit(); } else {return false; }">登出</a>]
							<form id="ssoLogoutForm" action='<s:property value="ssoLogoutUrl" />' method="post">
								<input type="hidden" name="url" value='<s:property value="ssoLoginUrl" />' />
							</form>
						</span>
						</s:if>
						<s:else>
						<span>
							[<s:a cssClass="logoutLink noloading" style="color:white;" namespace="%{serverContext}/loginFemtoUser" 
								action="logoutFemtoUser" onclick="return confirm('確定登出系統?');">登出</s:a>]
						</span>
						</s:else>
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
