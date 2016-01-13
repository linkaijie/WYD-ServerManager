<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<%
    	  String addressName=application.getInitParameter("addressName");
    	%>
		<title><%=addressName%>-Redis列表 </title>
		<%@ include file="/commons/meta.jsp"%>
		<%@ include file="/WEB-INF/context/commons/head.jsp"%>
		<script src="${widgethome}/open/lhgdialog.js" type=text/javascript></script>
		<script type="text/javascript">
            function doSearch() {
                var listForm = $("#listForm");
                listForm.submit();
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
										现在的位置：redis配置管理 &gt;&nbsp;redis配置列表
									</div>
								</div>
								<form action="${ctx}/redisconfig/redisConfigList.action" name="listForm" id="listForm" method="post" >
									<div class="seach_div">
										编号:
										<input name="redisConfigId" id="redisConfigId" type="text" size="10" value="${model.redisConfigId}"/>
										&nbsp;&nbsp;
										所属服务器:
                                        <select id="serverId" name="serverId">
                                                <option value="">请选择服务器</option>
                                                <c:forEach items="${server}" var="server">
                                                    <option value="${server.id}" <c:if test="${model.serverId == server.id}">selected="selected"</c:if>>${server.serverName}</option>
                                                </c:forEach>
                                        </select>
                                        &nbsp;&nbsp;
                                                                                                                        所属地区:
                                        <select id="areaId" name="areaId">
                                                <option value="">请选择地区</option>
                                                <c:forEach items="${areaMap}" var="areaMap">
                                                    <option value="${areaMap.key}" <c:if test="${model.areaId == areaMap.key}">selected="selected"</c:if>>${areaMap.value.name}</option>
                                                </c:forEach>
                                        </select>
                                        &nbsp;&nbsp;
										<input name="input" type="button" value="确 定" class="btn_w66_write1" onclick="doSearch(this.form);" />
										&nbsp;&nbsp;
										<input name="input" type="button" value="重 置" class="btn_w66_write1" onclick="window.location.href='${ctx}/redisconfig/redisConfigList.action'" />
										</br></br>
										<div id="depolydiv" class="seach_div" style="display: none;">
										                数据库：
							                 <select id="jdbcType" name="jdbcType">
                                                <option value="">请选择服务器</option>
                                                <c:forEach items="${jdbcUrlList}" var="jdbcUrlList">
                                                    <option value="${jdbcUrlList.jdbcKey}">${jdbcUrlList.remark}</option>
                                                </c:forEach>
                                             </select>
                                             &nbsp;&nbsp;   
                                             <input name="input" type="button" value="部署" class="btn_w66_write1" onclick="deployServerByScp();" />
                                             &nbsp;&nbsp;
                                             <input name="input" type="button" value="放弃" class="btn_w66_write1" onclick="doSearch(this.form);" />
                                        </div>
									</div>
									<div class="div_content">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td width="100%" height="100%" valign="top" colspan="2">
													<div id="biaoge"><jsp:include page="redisConfigListTable.jsp"/></div>
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

