<%@ page language="java" contentType="text/html; charset=UTF8"
	pageEncoding="UTF8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript">
<!--
$('form').each(function(i, f){
	$(f).submit(function() {
		$.blockUI({ message: loadingHtml });
	});
	});
	
$('a').each(function(i, a){
	if ($(a).attr('href') == '#') {
		return;
	}
	if ($(a).hasClass('noloading')) {
		return;
	}
	$(a).click(function() {
		$.blockUI({ message: loadingHtml });
	});
	});
//-->
</script>
