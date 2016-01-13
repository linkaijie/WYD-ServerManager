<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<%
            String addressName=application.getInitParameter("addressName");
        %>
		<title><%=addressName%>-ServerManage服务列表 </title>
		<%@ include file="/commons/meta.jsp"%>
		<%@ include file="/WEB-INF/context/commons/head.jsp"%>
		<script src="${widgethome}/open/lhgdialog.js" type="text/javascript"></script>
		<script type="text/javascript" src="${ctx}/scripts/server/serverManager.js"></script>
		<script type="text/javascript">
			function doSearch() {
				var listForm = $("#listForm");
				listForm.submit();
			}
			
			function close(sessionName) {
                $.ajax({
                    type : 'POST',
                    url : "common/destroySessionByName.action",
                    data: {sessionName:sessionName},
                });
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
				<!-- 
					<td width="188" valign="top">
						<%@ include file="/include/dataLeft.jsp"%>
					</td> -->
					<td valign="top">
						<div class="table_right">
							<div class="moudle_div">
								<div class="sidebar_title">
									<div class="sidebar_title_border">
										现在的位置：ServerManage服务管理 &gt;&nbsp;ServerManage列表
									</div>
								</div>
								<form action="${ctx}/servermanage/serverManageList.action" name="listForm" id="listForm" method="post" >
									<div class="seach_div">
										类型：
										<select id="serverType" name="serverType">
											<option value="">请选择</option>
											<option value="2" <c:if test="${model.serverType == 2}">selected="selected"</c:if>>混服</option>
											<option value="3" <c:if test="${model.serverType == 3}">selected="selected"</c:if>>硬核</option>
											<option value="2" <c:if test="${model.serverType == 2}">selected="selected"</c:if>>台湾=2</option>
										</select>
										&nbsp;&nbsp;   
										分区号:
                                        <input name="areaIds" id="areaIds" type="text" size="10" value="${model.areaIds}"/>(逗號分隔)
                                        &nbsp;&nbsp;   
										编号:
										<input name="startAreaId" id="startAreaId" type="text" size="1" value="${model.startAreaId}"/>
										至
										<input name="endAreaId" id="endAreaId" type="text" size="1" value="${model.endAreaId}"/>
                                        &nbsp;&nbsp;
                                                                                                                          状态：
                                        <select id="status" name="status">
                                            <option value="">请选择</option>
                                            <option value="0" <c:if test="${model.status == 0}">selected="selected"</c:if>>关闭</option>
                                            <option value="1" <c:if test="${model.status == 1}">selected="selected"</c:if>>开启</option>
                                        </select>
                                        &nbsp;&nbsp;
                                                                                                                        所属服务器:
                                        <select id="serverId" name="serverId">
                                                <option value="">请选择服务器</option>
                                                <c:forEach items="${server}" var="server">
                                                    <option value="${server.id}" <c:if test="${model.serverId == server.id}">selected="selected"</c:if>>${server.serverName}</option>
                                                </c:forEach>
                                        </select>
                                        &nbsp;&nbsp;
										<input name="input" type="button" value="确 定" class="btn_w66_write1" onclick="doSearch(this.form);" />
										&nbsp;&nbsp;
										<input name="input" type="button" value="重 置" class="btn_w66_write1" onclick="window.location.href='${ctx}/servermanage/serverManageList.action'" />
									</div>
									<div id="updateTypediv" class="seach_div" style="display: none;">
    								           更新类型：
                                        <select id="updateType" name="updateType">
                                            <option value="">----------------请选择----------------</option>
                                            <option value="1" <c:if test="${model.updateType == 1}">selected="selected"</c:if>>只更新world</option>
                                            <option value="2" <c:if test="${model.updateType == 2}">selected="selected"</c:if>>只更新dispatch</option>
                                            <option value="3" <c:if test="${model.updateType == 3}">selected="selected"</c:if>>更新world和dispatch</option>
                                            <option value="4" <c:if test="${model.updateType == 4}">selected="selected"</c:if>>重启dispatch，不更新</option>
                                            <option value="5" <c:if test="${model.updateType == 5}">selected="selected"</c:if>>重启world和dispatch，不更新</option>
                                        </select>
                                        &nbsp;&nbsp;   
                                        <input name="input" type="button" value="更新重启" class="btn_w66_write1" onclick="updateAndRestart();" />
                                        &nbsp;&nbsp;
                                        <input name="input" type="button" value="放弃" class="btn_w66_write1" onclick="doSearch(this.form);" />
                                    </div>
									<div class="div_content">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td width="100%" height="100%" valign="top" colspan="2">
													<div id="biaoge"><jsp:include page="serverManageListTable.jsp"/></div>
												</td>
											</tr>
										</table>
									</div>
								</form>
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

