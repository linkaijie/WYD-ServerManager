<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<%
            String addressName=application.getInitParameter("addressName");
        %>
		<title><%=addressName%>-SERVER列表 </title>
		<%@ include file="/WEB-INF/context/commons/head.jsp"%>
		<script type="text/javascript">
    		function doSearch() {
                var listForm = $("#listForm");
                listForm.submit();
            }
    		
    		function commandInput(){
    	        var commanddiv = document.getElementById('commanddiv');
    	        var skingray = document.getElementById('skingray').style.display='none';
    	        commanddiv.style.display='block';
    	    }
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
										现在的位置：数据管理 &gt;&nbsp;SERVER列表
									</div>
								</div>
								<form action="${ctx}/server/serverList.action" name="listForm" id="listForm" method="post" >
    								<div class="seach_div">
    								所属服务器:
    								    <select id="serverId" name="serverId">
    								<option value="">请选择服务器</option>
    								<c:forEach items="${server}" var="server">
    								<option value="${server.id}" <c:if test="${model.serverId == server.id}">selected="selected"</c:if>>${server.serverName}</option>
    								</c:forEach>
    								</select>
    								&nbsp;&nbsp;
    								使用类型:
								    <select id="useType" name="useType">
    								<option value="">选择使用类型</option>
    								<option value="1" <c:if test="${model.useType == 1}">selected="selected"</c:if>>游戏服务器</option>
    								<option value="2" <c:if test="${model.useType == 2}">selected="selected"</c:if>>缓存服务器</option>
    								<option value="3" <c:if test="${model.useType == 3}">selected="selected"</c:if>>数据库服务器</option>
    								</select>
    								&nbsp;&nbsp;
    								<input name="input" type="button" value="确 定" class="btn_w66_write1" onclick="doSearch(this.form);" />
    								&nbsp;&nbsp;
    								<input name="input" type="button" value="重 置" class="btn_w66_write1" onclick="window.location.href='${ctx}/server/serverList.action'" />
    								</div>
								</form>
								<div id="commanddiv" class="seach_div" style="display: none;">
								    <label>命令:</label>
                                    <textarea id="command" name="command" cols="80" rows="3"></textarea>
								     &nbsp;&nbsp;   
                                     <input name="input" type="button" value="執行" class="btn_w66_write1" onclick="runCommand();" />
                                     &nbsp;&nbsp;
                                     <input name="input" type="button" value="放弃" class="btn_w66_write1" onclick="doSearch(this.form);" />
                                </div>
								<div class="div_content">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="100%" height="100%" valign="top" colspan="2">
												<div id="biaoge"><jsp:include flush="false" page="serverListTable.jsp"/></div>
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

