<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@ include file="/WEB-INF/context/commons/head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<link href="${ctx}/styles/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/scripts/table.js"></script>
<script type="text/javascript">

	function pageGo(page) {
		$("#pageNumber").val(page);
		var pageInfoNum = parseInt($("#pageInfoNum").val());
		var shopId = $("#shopId").val();
		var isOnSale = $("#isOnSale").val();
		var sex = $("#sex").val();
		var type = $("#type").val();
		var qualityType = $("#qualityType").val();

		$.post(ctx + "/shopItems/shopItemsList.action",
				{ajax: "true", shopId: shopId,isOnSale:isOnSale,type:type,sex:sex,qualityType:qualityType,pageNumber:page,pageInfoNum:pageInfoNum},
					function(data){
				       $("#biaoge").html(data);
					});	
	}
	
	function input(id) {
		var url = "model/modelInput.action";
		if (id) {
			url += "?id=" + id;
		}
		window.location.href = url;
	}
	
	function deletes(id) {
		if (confirm("您确定要删除该服务吗？") == true) {
			$.ajax({
				type : 'POST',
				url : "model/delete.action",
				data : {
					"id" : id,
				},
				success : function(msg) {
					if(msg == 'success'){
						alert("删除成功~~！");	
					}else{
						alert(msg);				
					}
					window.location.href=ctx+"/model/modelList.action";
				}
			});
		}
	}
</script>
<div class="div_content">
	<c:choose>
		<c:when test="${IS_ADMIN eq true}">
			<table class="bought-table">
				<thead>
					<tr class="toolbar skin-gray">
						<td colspan="7">
							<a href="${ctx}/accountserver/form.action" class=" toolbtn">
								新増
							</a>
						</td>
					</tr>
				</thead>
			</table>
		</c:when>
	</c:choose>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
				<table class="bought-table">
					<thead>
						<tr>
							<th>编号</th>
							<th>名称</th>
							<th>属于游戏</th>
							<th>所在服务器</th>
							<th>ACCOUNT路径</th>
							<th>IPD路径</th>
							<th>WORLD路径</th>
							<th>DISPATCH路径</th>
							<th><a name = "acc" href="javascript:input();">添加</a></th>
						</tr>
					</thead>
					<tbody class=" success-order">
					<c:if test="${pager ne null}">
						<c:forEach items="${pager.list}" var="data" varStatus="status">
							<tr <c:choose>
							<c:when test="${status.index % 2 == 1}">class="odd"</c:when>
							<c:otherwise>class="even"</c:otherwise>
						</c:choose>>
								<td name = "accList" align="center">${data.id}</td>
								<td name = "accList" align="center">${data.name}</td>
								<td name = "accList" align="center">${data.gameId}</td>
								<td name = "accList" align="center">${data.serverId}</td>
								<td name = "accList" align="center">
									<c:if test="${fn:length(data.accountPath) > 15 }"> <span title="${data.accountPath}">${fn:substring(data.accountPath, 0, 15)}...</span> </c:if>
									<c:if test="${fn:length(data.accountPath) < 15 }">${data.accountPath}</c:if>
								</td>
								<td name = "accList" align="center">
									<c:if test="${fn:length(data.ipdmainPath) > 15 }"> <span title="${data.ipdmainPath}">${fn:substring(data.ipdmainPath, 0, 15)}...</span> </c:if>
									<c:if test="${fn:length(data.ipdmainPath) < 15 }">${data.ipdmainPath}</c:if>
								</td>
								<td name = "accList" align="center">
									<c:if test="${fn:length(data.worldPath) > 15 }"> <span title="${data.worldPath}">${fn:substring(data.worldPath, 0, 15)}...</span> </c:if>
									<c:if test="${fn:length(data.worldPath) < 15 }">${data.worldPath}</c:if>
								</td>
								<td name = "accList" align="center">
									<c:if test="${fn:length(data.dispatchPatch) > 15 }"> <span title="${data.dispatchPatch}">${fn:substring(data.dispatchPatch, 0, 15)}...</span> </c:if>
									<c:if test="${fn:length(data.dispatchPatch) < 15 }">${data.dispatchPatch}</c:if>
								</td>
								<td name = "accList" align="center">
									<a name = "acc" href="javascript:input(${data.id});">编辑</a> -- 
									<a name = "acc" href="javascript:deletes(${data.id});">删除</a>
								</td>
							</tr>
						</c:forEach>
						</c:if>
					</tbody>
					<tfoot>
						<tr class="sep-row">
							<td colspan="5"></td>
						</tr>
						<tr class="pagebar">
							<td colspan="5">
								<table width="100%" border="0" >
									<tr>
										<c:if test="${pager ne null}">
											<%@ include file="/WEB-INF/context/commons/pager.jsp"%>
										</c:if>
									</tr>
								</table>
							</td>
						</tr>
					</tfoot>
				</table>
				<div style="clear: both;"></div>
			</td>
		</tr>
	</table>
</div>
