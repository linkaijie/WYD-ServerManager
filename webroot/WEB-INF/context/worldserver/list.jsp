<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/context/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/context/commons/head.jsp"%>
<title>World服务管理</title>
<script type="text/javascript">
function start(id){
	$.ajax({
		type : 'POST',
		url : "worldserver/runWorld.action",
		data : {
			"worldId" : id,
		},
		success : function(msg) {
			if(msg == 'true'){
				lhgdialog.opendlg("ACCOUNT日志","${ctx}/worldserver/showStdout.action?worldId="+id,1020,500,true,true);
			}else{
				alert(msg);				
			}
		}
	});
}
function stop(id) {
	$.ajax({
		type : 'POST',
		url : "worldserver/killWorld.action",
		data : {
			"worldId" : id,
		},
		success : function(msg) {
			alert(msg);	
		}
	});
}

function deploy(id){
	$.ajax({
		type : 'POST',
		url : "worldserver/deployWorld.action",
		data : {
			"worldId" : id,
		},
		success : function(msg) {
			if(msg == 'success'){
				alert("恭喜你，部署成功~~！");	
			}else{
				alert(msg);				
			}
		}
	});
}
</script>
</head>
<body>
<div id="hld">
	<div class="row-fluid">
		<div class="block">
			<form id="listForm" action="worldserver/list.action" method="post">
				<div class="block_head">
					<div class="condition_bar">
						<h2>World服务管理</h2>
					</div>
				</div>
				<div class="block_content">
					<table class="sortable">
						<tr>
							<th>编号</th>
							<th>名称</th>
							<th>LocalIP</th>
							<th>端口</th>
							<th>PublicIP</th>
							<th>HttpPort</th>
							<th>AuthPort</th>
							<th>AuthIp</th>
							<th>分区ID</th>
							<th>状态</th>
							<th>是否已部署</th>
							<th><a href="javascript:input();">添加</a></th>
						</tr>
						<c:forEach items="${worldServerList}" var="data" varStatus="status">
							<tr <c:choose>
							<c:when test="${status.index % 2 == 1}">class="odd"</c:when>
							<c:otherwise>class="even"</c:otherwise>
						</c:choose>>
								<td>${data.id}</td>
								<td>${data.name}</td>
								<td>${data.localIp}</td>
								<td>${data.port}</td>
								<td>${data.publicIp}</td>
								<td>${data.httpPort}</td>
								<td>${data.authPort}</td>
								<td>${data.authIp}</td>
								<td>${data.areaId}</td>
								<c:choose>
									<c:when test="${data.state == 1}">
										<td style = "color:green">
											已开启
										</td>
									</c:when>
									<c:otherwise>
										<td>
											已关闭
										</td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${data.isDeploy == 1}">
										<td style = "color:green">
											已部署
										</td>
									</c:when>
									<c:otherwise>
										<td>
											未部署
										</td>
									</c:otherwise>
								</c:choose>
								<td>
								<a href="javascript:start(${data.id});">开启	</a>-- 
								<a href="javascript:stop(${data.id});">关闭</a> --
								<a href="javascript:deploy(${data.id});">部署</a>
								</td>
							</tr>
						</c:forEach>
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