<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<%
            String addressName=application.getInitParameter("addressName");
        %>
		<title><%=addressName%>-MODEL列表 </title>
		<%@ include file="/commons/meta.jsp"%>
		<%@ include file="/WEB-INF/context/commons/head.jsp"%>
		<script src="${widgethome}/open/lhgdialog.js" type=text/javascript></script>
		<script type="text/javascript">
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
						<%@ include file="/include/dataLeft.jsp"%>
					</td>
					<td valign="top">
						<div class="table_right">
							<div class="moudle_div">
								<div class="sidebar_title">
									<div class="sidebar_title_border">
										现在的位置：数据管理 > MODEL列表
									</div>
								</div>
								<form action="" name="newForm" id="newForm" method="post" >
								<!-- 
								<div class="seach_div">
									商品名称或ID:
									<input name="shopId" id="shopId" type="text" size="25" />
									&nbsp;&nbsp;
									<input name="input" type="button" " value="确 定" class="btn_w66_write" onclick="sub()" />
									&nbsp;&nbsp;
									<input name="input" type="button" " value="重置" class="btn_w66_write" onclick="window.location.href='shopItems/shopItemsList.action'" />
								</div>
								 -->
								</form>
								<div class="div_content">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="100%" height="100%" valign="top" colspan="2">
												<div id="biaoge"><jsp:include flush="false" page="modelListTable.jsp"/></div>
											</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
				</tr>
			</table>
		</div>
		<div class="bottom" id="bottom">
			<jsp:include flush="false" page="/down.jsp" />
		</div>
	</body>
</html>

