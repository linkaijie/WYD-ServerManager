<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/context/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>用户管理</title>
<script type="text/javascript">
	function input(id) {
		var url = "account/input.action";
		if (id) {
			url += "?accountId=" + id;
		}
		window.location.href = url;
	}
</script>
</head>
<body>
<div id="hld">
	<div class="row-fluid">
		<div class="block">
			<form id="listForm" action="account/list.action" method="post">
				<div class="block_head">
					<div class="condition_bar">
					</div>
				</div>
				<div class="block_content">
					<table class="sortable">
						<tr>
							<th>id</th>
							<th>账号</th>
							<th><a href="javascript:input();">添加</a></th>
						</tr>
						<c:forEach items="${pager.list}" var="data" varStatus="status">
							<tr <c:choose>
							<c:when test="${status.index % 2 == 1}">class="odd"</c:when>
							<c:otherwise>class="even"</c:otherwise>
						</c:choose>>
								<td>${data.id}</td>
								<td>${data.username}</td>
								<td><a href="javascript:input(${data.id});">编辑</a></td>
							</tr>
						</c:forEach>
					</table>
					<%@ include file="/WEB-INF/context/commons/pager.jsp"%>
				</div>
			</form>
		</div>
	</div>
</div>
</body>
</html>