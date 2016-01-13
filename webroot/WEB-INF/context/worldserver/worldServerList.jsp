<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<%
            String addressName=application.getInitParameter("addressName");
        %>
		<title><%=addressName%>-WORLD服务列表 </title>
		<%@ include file="/commons/meta.jsp"%>
		<%@ include file="/WEB-INF/context/commons/head.jsp"%>
		<script src="${widgethome}/open/lhgdialog.js" type=text/javascript></script>
        <script src="${wdatehome}/WdatePicker.js" type="text/javascript"></script>
        <script type="text/javascript">
            function doSearch() {
                var listForm = $("#listForm");
                listForm.submit();
            }
        
            function updateModelTime() {
                if (confirm("您确定要修改最后更新时间吗？") == true) {
                    var worldUpdateTime = $("#worldUpdateTime").val();
                    var newstr = worldUpdateTime.replace(/-/g,'/'); 
                    var date =  new Date(newstr); 
                    var time_str = date.getTime().toString();
                    $.post(ctx + "/model/UpdateModelDate.action", {
                        "updateModeTime" : time_str,
                        "updateType" : "world"
                    }, function(data) {
                        if(data == true){
                            alert("更新成功~~！");   
                        }else{
                            alert(data);             
                        }
                        window.location.href=ctx+"/worldserver/worldServerList.action";
                    }, "json");
                }
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
										现在的位置：WORLD服务管理 &gt;&nbsp;WORLD列表
									</div>
								</div>
								<form action="${ctx}/worldserver/worldServerList.action" name="listForm" id="listForm" method="post" >
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
                                            <option value="1" <c:if test="${serverType == 1}">selected="selected"</c:if>>国服IOS=1</option>
                                            <option value="2" <c:if test="${serverType == 2}">selected="selected"</c:if>>混服=2</option>
                                            <option value="3" <c:if test="${serverType == 3}">selected="selected"</c:if>>硬核=3</option>
                                            <option value="2" <c:if test="${serverType == 2}">selected="selected"</c:if>>台湾=2</option>
                                            <option value="1" <c:if test="${serverType == 1}">selected="selected"</c:if>>越南=1</option>
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
										<input name="input" type="button" value="重 置" class="btn_w66_write1" onclick="window.location.href='${ctx}/worldserver/worldServerList.action'" />
										</br>
										</br>
										最后更新时间:
										<c:set var="pattern" value="yyyy-MM-dd HH:mm:ss"></c:set>
										<fmt:formatDate pattern="${pattern}" value="${updateDate}" var="et" />
		                                <input class="Wdate" type="text" id="worldUpdateTime" name="worldUpdateTime" value="${et}"
		                                    onfocus="WdatePicker({el:$dp.$(this),dateFmt:'${pattern}'});" />
										&nbsp;&nbsp;
										<input name="input" type="button" value="修改" class="btn_w66_write1" onclick="updateModelTime();" />
									</div>
									<div id="depolydiv" class="seach_div" style="display: none;">
									                数据库：
						                 <select id="jdbcType" name="jdbcType">
                                            <option value="">请选择数据库</option>
                                            <c:forEach items="${jdbcUrlList}" var="jdbcUrlList">
                                                <option value="${jdbcUrlList.jdbcKey}">${jdbcUrlList.remark}</option>
                                            </c:forEach>
                                         </select>
									     <!--           
                                         <select id="jdbcType" name="jdbcType">
                                             <option value="">------------请选择------------</option>
                                             <option value="JDBC_HD">硬核数据库</option>
                                             <option value="JDBC_BEFORE_36">1-36数据库</option>
                                             <option value="JDBC_AFTER_37">37以后数据库（ssd1）</option>
                                             <option value="JDBC_AFTER_161">161以后数据库（ssd2）</option>
                                             <option value="JDBC_6">.6</option>
                                             <option value="JDBC_IOS_WORLD1">国服IOS数据库1</option>
                                             <option value="JDBC_TW_TEST">台湾</option>
                                             <option value="JDBC_VN_TEST">越南测试</option>
                                         </select>
                                         -->
                                                                                                                         内存分配：
                                         <select id="memory" name="memory">
                                             <option value="">请选择</option>
                                             <option value="1">1G</option>
                                             <option value="2">2G</option>
                                             <option value="4">4G</option>
                                             <option value="6">6G</option>
                                         </select>
                                         &nbsp;&nbsp;   
                                         <input name="input" type="button" value="部署" class="btn_w66_write1" onclick="deployServerByScp();" />
                                         &nbsp;&nbsp;
                                         <input name="input" type="button" value="放弃" class="btn_w66_write1" onclick="doSearch(this.form);" />
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

