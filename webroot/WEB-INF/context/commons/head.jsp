<%@ page language="java" pageEncoding="UTF-8"%>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<base href="${basePath}" />
<script type='text/javascript' src="scripts/utils/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="scripts/utils/jquery.pager.js"></script>
<script type="text/javascript" src="scripts/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="scripts/list.js"></script>
<script type="text/javascript" src="scripts/utils.js"></script>
<script type='text/javascript' src="widgets/open/lhgdialog.js"></script>
<!-- <link rel="stylesheet" type="text/css" href="styles/theme.css" />-->
<link rel="stylesheet" type="text/css" href="styles/pager.css" />
<link rel="stylesheet" type="text/css" href="styles/style.css" />
<c:set var="ctx" value="${pageContext.request.contextPath eq '/'?'':pageContext.request.contextPath}"/>
<script type="text/javascript">
	var ctx = "${ctx}";
	function doSearch(form) {
		if (document.all['pager.pageNumber'] != undefined) {
			document.all['pager.pageNumber'].value = 1;
		}
		if (form != undefined) {
			form.submit();
		}
	}
</script>