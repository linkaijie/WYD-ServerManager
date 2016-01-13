<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>用户信息列表 </title>
		<%@ include file="/commons/meta.jsp"%>
		<%@ include file="/WEB-INF/context/commons/head.jsp"%>
		<link rel="stylesheet" type="text/css" href="${exthome}/resources/css/ext-all.css" />
		<script type="text/javascript" src="${exthome}/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="${exthome}/ext-all.js"></script>
		<script src="${widgethome}/open/lhgdialog.js" type=text/javascript></script>
		<script type="text/javascript">
		/*
		function sub(){
			var isOnSale=$("#isOnSale").val();
			var type=$("#type").val();
			var sex=$("#sex").val();
			var shopId=$("#shopId").val();
			var qualityType=$("#qualityType").val();	
			$.post(ctx+"/shopItems/shopItemsList.action",{ajax: "true", isOnSale: isOnSale,type:type,sex:sex,shopId:shopId,qualityType:qualityType},
					   function(data){
			              $("#biaoge").html(data);
					   });
		}
		*/
		window.onbeforeunload = onbeforeunload_handler;   
		window.onunload = onunload_handler; 
		</script>
	</head>
	<body >
		<jsp:include flush="false" page="/sys_menu.jsp" />
		<div style="height: 10px;">
		</div>
		<div class="table_div">
			<table border="0" cellspacing="0" cellpadding="0" width=""
				style="width: 100%;">
				<tr>
					<td width="188" valign="top">
						<!-- 嵌入右侧菜单页面 -->
						<%@ include file="/include/adminLeft.jsp"%>
					</td>
					<td valign="top">
						<div class="table_right">
							<div class="moudle_div">
								<div class="sidebar_title">
									<div class="sidebar_title_border">
										现在的位置：用户管理 &gt;&nbsp;用户信息列表
									</div>
								</div>
								<form action="" name="newForm" id="newForm" method="post" >
								</form>
								<div class="div_content">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="100%" height="100%" valign="top" colspan="2">
												<div id="biaoge"><jsp:include flush="false" page="userListTable.jsp"/></div>
											</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
					</td>
				</tr>
			</table>
		</div>
		<div class="bottom" id="bottom">
			<jsp:include flush="false" page="/down.jsp" />
		</div>
	</body>
</html>

