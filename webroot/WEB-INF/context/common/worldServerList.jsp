<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<%
            String addressName=application.getInitParameter("addressName");
        %>
		<title><%=addressName%>-迁服管理列表 </title>
		<%@ include file="/commons/meta.jsp"%>
		<%@ include file="/WEB-INF/context/commons/head.jsp"%>
		<script src="${widgethome}/open/lhgdialog.js" type=text/javascript></script>
        <script src="${wdatehome}/WdatePicker.js" type="text/javascript"></script>
        <script type="text/javascript">
            function doSearch() {
                var listForm = $("#listForm");
                listForm.submit();
            }
        
            function changeServer() {
                var changeServerId = document.getElementById("changeServerId");
                if (changeServerId.value == '') {
                    alert('请选择要迁到哪台机子');
                    changeServerId.focus();
                    return false;
                }
                var dispatchServerId = document.getElementById("dispatchServerId");
                if (dispatchServerId.value == '') {
                    alert('请输入dispatch配置文件中的id');
                    dispatchServerId.focus();
                    return false;
                }
                var boxs = document.getElementsByName('table_id');
                var data = "";
                //对象不存在,不处理,跳出方法
                if (!boxs) {
                    return;
                }
                //获取选中行的ID
                for (var i = 0; i < boxs.length; i++) {
                    if (boxs[i].checked == true) {
                        data += boxs[i].value + ",";
                    }
                }
                //没有选择提示错误
                if (data == "") {
                    alert('请选择要迁移的服务!');
                    return;
                }
                if (data != "") {
                    if (!confirm('您确定要迁移当前选中的服务吗?')) {
                        return;
                    }
                } else {
                    return;
                }
                $.post(ctx + "/common/changeServer.action", {
                    worldIds : subString(data),
                    serverId : changeServerId.value, 
                    dispatchServerId : dispatchServerId.value
                }, function(msg) {
                    lhgdialog.opendlg("迁移日志",ctx+"/changeserverlog.jsp",400,500,true,true);
                    if (msg == true) {
                    } else {
                        alert(msg);
                    }
                }, "json");
            }
        </script>
	</head>
	<body>
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
										现在的位置：迁服管理 &gt;&nbsp;WORLD列表
									</div>
								</div>
								<form action="${ctx}/common/worldServerList.action" name="listForm" id="listForm" method="post" >
									<div class="seach_div">
									            分区号:
                                        <input name="areaIds" id="areaIds" type="text" size="10" value="${model.areaIds}"/>(逗號分隔)
									    &nbsp;&nbsp;        
									            分区号:
                                        <input name="startAreaId" id="startAreaId" type="text" size="1" value="${model.startAreaId}"/>
                                                                                                                        至
                                        <input name="endAreaId" id="endAreaId" type="text" size="1" value="${model.endAreaId}"/>
                                        &nbsp;&nbsp;
    									类型：
                                        <select id="serverType" name="serverType">
                                            <option value="">请选择</option>
                                            <option value="2" <c:if test="${model.serverType == 2}">selected="selected"</c:if>>混服</option>
                                            <option value="3" <c:if test="${model.serverType == 3}">selected="selected"</c:if>>硬核</option>
                                        </select>
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
										<input name="input" type="button" value="重 置" class="btn_w66_write1" onclick="window.location.href='${ctx}/common/worldServerList.action'" />
										</br>
										</br>
										迁到哪台机子:
                                        <select id="changeServerId" name="changeServerId">
                                                <option value="">请选择服务器</option>
                                                <c:forEach items="${server}" var="server">
                                                    <option value="${server.id}" <c:if test="${model.serverId == server.id}">selected="selected"</c:if>>${server.serverName}</option>
                                                </c:forEach>
                                        </select>
                                        &nbsp;&nbsp;
                                        dispatch配置文件中的id:
                                        <input type="text" id="dispatchServerId" name="dispatchServerId" size="12" />（这里填写主机号，如主机s2则填2，主机s18则填18）
										<input name="input" type="button" value="迁移" class="btn_w66_write1" onclick="changeServer();" />
									</div>
									<div class="div_content">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td width="100%" height="100%" valign="top" colspan="2">
													<div id="biaoge"><jsp:include page="worldServerListTable.jsp"/></div>
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

