<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/context/commons/taglibs.jsp"%>
<%@ include file="/WEB-INF/context/commons/head.jsp"%>
<link href="${ctx}/styles/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/scripts/table.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(".iframe").colorbox( {
			iframe : true,
			width : "35%",
			height : "60%"
		});
	});

	function pageGo(page) {
		$("#pageNumber").val(page);
		var pageInfoNum = parseInt($("#pageInfoNum").val());
		$.post(ctx + "/mail/mailList.action",
				{ajax: "true",pageNumber:page,pageInfoNum:pageInfoNum},
					function(data){
				       $("#biaoge").html(data);
					});	
	}
	
	function input(id) {
		var url = "mail/mailInput.action";
		if (id) {
			url += "?id=" + id;
		}
		window.location.href = url;
	}
	
	function deletes(id) {
		if (confirm("您确定要删除该服务吗？") == true) {
			$.ajax({
				type : 'POST',
				url : "mail/delete.action",
				data : {
					"id" : id,
				},
				success : function(msg) {
					if(msg == 'success'){
						alert("删除成功~~！");	
					}else{
						alert(msg);				
					}
					window.location.href=ctx+"/mail/mailList.action";
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
							<a href="${ctx}/mail/form.action" class=" toolbtn">
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
							<th>邮件地址</th>
							<th>启用状态</th>
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
								<td name = "accList" align="center">${data.mail}</td>
								<td name = "accList" align="center">
									<c:if test="${data.statue == 1}">启用</c:if>
									<c:if test="${data.statue == 0}">禁用</c:if>
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
