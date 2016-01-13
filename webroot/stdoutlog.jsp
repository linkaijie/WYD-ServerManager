<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/context/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/context/commons/head.jsp"%>
<meta http-equiv="refresh" content="2">
<title>Account服务管理</title>
<script type="text/javascript">
	function start(id) {
		var url = "accountserver/runAccount.action";
		if (id) {
			url += "?accountId=" + id;
		}
		window.location.href = url;
	}
	function stop(id) {
		var url = "accountserver/killAccount.action";
		if (id) {
			url += "?accountId=" + id;
		}
		window.location.href = url;
	}
	
	 $(document).ready(function () {
         $.ajax({
             type: "POST",
             data : {
 				"accountId" : 1,
 			},
             url: "accountserver/showStdout.action",
         });
	});
</script>
</head>
<body>
<div id="hld">
	<div class="row-fluid">
		<div class="block">
			<form id="listForm" action="" method="post">
				<div class="block_head">
					<div class="condition_bar">
						<h2>Account服务管理</h2>
					</div>
				</div>
				<div class="block_content">
					<table class="sortable">
						${stdout}						
					</table>
					<!-- 
					<%@ include file="/WEB-INF/context/commons/pager.jsp"%>
					 -->
				</div>
			</form>
		</div>
	</div>
	<%@ include file="/WEB-INF/context/commons/bottom.jsp"%>
</div>
</body>
</html>