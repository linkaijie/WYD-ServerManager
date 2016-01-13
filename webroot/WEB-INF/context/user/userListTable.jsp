<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@ include file="/WEB-INF/context/commons/head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<link href="${ctx}/styles/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/scripts/table.js"></script>
<link href="${ctx}/scripts/colorbox/colorbox.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="${ctx}/scripts/colorbox/jquery.colorbox.js"></script>
<script src="${ctx}/scripts/player/player.js" type=text/javascript></script>
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
		var url = "user/userForm.action";
		if (id) {
			url += "?userId=" + id;
		}
		window.location.href = url;
	}
	
	function deleteUser(id) {
		var url = "user/deleteUser.action?userId="+id;
		window.location.href = url;
	}
	
	function changePwd(){
		lhgdialog.opendlg('修改密码',ctx+"/user/userPwdForm.action",480,200,true,true);
	}
</script>
<div class="div_content">
	<c:choose>
		<c:when test="${IS_ADMIN eq true}">
			<table class="bought-table">
				<thead>
					<tr class="toolbar skin-gray">
						<td colspan="7">
							<a href="${ctx}/user/form.action" class=" toolbtn">
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
							<th>昵称</th>
							<th>登录名</th>
							<th>创建时间</th>
							<th>修改时间</th>
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
								<td name = "accList" align="center">${data.nickName}</td>
								<td name = "accList" align="center">${data.userName}</td>
								<td name = "accList" align="center">${data.createDate}</td>
								<td name = "accList" align="center">${data.updateDate}</td>
								<td name = "accList" align="center">
									<a name = "acc" href="javascript:changePwd();">修改密码</a> -- 
									<a name = "acc" href="javascript:deleteUser(${data.id});">删除</a>
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
