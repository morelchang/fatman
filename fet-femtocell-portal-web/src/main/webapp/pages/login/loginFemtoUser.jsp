<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript">
<!--
function login() {
	var message = '';
	if ($.trim($('#account').val()) == '') {
		message += '請輸入登入帳號\n';
	}
	
	if (message != '') {
		alert(message);
		return false;
	}
	return true;
}

function clearText() {
	$('#account').val('');
	$('#password').val('');
}
//-->
</script>

<!-- title -->
<table class="noBorder" align="center">
<tr><td valign="middle" align="center">
<div>
	<h3>Femto用戶登入</h3>
</div>

<!-- content -->
<div class="grayBorder" align="left">
	<s:form namespace="/loginFemtoUser" action="loginFemtoUser" theme="simple">
		<s:label for="id">登入帳號:</s:label>
		<s:textfield id="account" name="account" maxlength="50" size="16" /><br/>
		<s:label for="firstName">登入密碼:</s:label>
		<s:password id="password" name="password" maxlength="50" size="16" /><br/>

		<span><s:actionerror /></span><span><s:actionmessage /></span><br/>

		<div class="noBorder" align="right">
			<s:submit value="登入" onclick="javascript: return login();" />
			<input type="button" value="清除" onclick="javascript: clearText();" />
		</div>
	</s:form>
</div><br/>
</td></tr>
</table>