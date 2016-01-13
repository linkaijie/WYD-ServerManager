<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
String addressName=application.getInitParameter("addressName");
%>
<title><%=addressName%>-迁服配置修改</title>
<%@ include file="/commons/meta.jsp"%>
<link href="${ctx}/styles/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
		function doSubmit() {
		    var worldIds = document.getElementById("worldId");
            if (worldIds.value == '') {
                alert("请选择WORLD服务器");
                worldIds.focus();
                return false;
            }
			var serverId = document.getElementById("serverId");
			if (serverId.value == '') {
                alert("请选择所属服务器");
                serverId.focus();
                return false;
            }
            var dispatchServerId = document.getElementById("dispatchServerId");
            if (dispatchServerId.value == '') {
                alert("dispatch配置文件中的id不能為空");
                dispatchServerId.focus();
                return false;
            }
            var save = document.getElementById("save");
            save.disabled=true;
            $.ajax({
                type : 'POST',
                data: {
                    worldIds : worldIds.value, 
                    serverId : serverId.value,
                    dispatchServerId : dispatchServerId.value,
                   },
                url : "changeServerCfg.action",
                success : function(msg) {
                if(msg!="" && msg=='success')
                {
                    alert("修改成功");
                    window.location.href=ctx+"/common/changeServerInput.action";
                }else{
                    alert(msg);
                }
                save.disabled=false;
            }
            });
		}
</script>
</head>
<body>
	<!-- 头部菜单 -->
	<jsp:include flush="false" page="/sys_menu.jsp" />
	<div style="height: 10px;"></div>
	<div class="table_div">
		<form >
			<table border="0" cellspacing="0" cellpadding="0" style="width: 100%;">
				<tr>
					<td width="188" valign="top">
						<%@ include file="/include/dataLeft.jsp"%>
					</td>
					<td valign="top">

						<div class="table_right">

							<div class="moudle_div">

								<div class="sidebar_title">

									<div class="sidebar_title_border">
										迁服配置修改
									</div>
								</div>

								<div style="border: #CCC solid 1px;" align="center">
									<div >
										<table  style="font-size: 15px;">
											<tr>
												<td height="30" align="right" class="left_txt2">
													&nbsp;
												</td>
												<td>
													&nbsp;
												</td>
												<td height="30">
													&nbsp;
												</td>
												<td align="right">
													&nbsp;
												</td>
												<td>
													&nbsp;
												</td>
												<td height="30">
													&nbsp;
												</td>
											</tr>
											<tr>
                                                <td class="lab"><label>WORLD服务器:</label></td>
                                                <td class="text">
                                                <select id="worldId" name="worldId">
                                                        <option value="">请选择WORLD服务器</option>
                                                        <c:forEach items="${worldServer}" var="worldServer">
                                                            <option value="${worldServer.id}" <c:if test="${dispatchServer.worldId == worldServer.id}">selected="selected"</c:if>>${worldServer.name}</option>
                                                        </c:forEach>
                                                </select>
                                                </td>
                                            </tr>
											<tr>
												<td class="lab"><label>目标服务器:</label></td>
												<td class="text">
												<select id="serverId" name="serverId">
														<option value="">请选择服务器</option>
														<c:forEach items="${server}" var="server">
															<option value="${server.id}" >${server.serverName}</option>
														</c:forEach>
												</select>
												</td>
											</tr>
											<tr>
                                                <td class="lab"><label>dispatch配置文件中的id:</label></td>
                                                <td class="text"><input type="text" id="dispatchServerId" name="dispatchServerId" size="12" />（这里填写主机号，如主机s2则填2，主机s18则填18）</td>
                                            </tr>
											<tr>
												<td class="lab"></td>
											</tr>
											<tr>
												<td height="30" align="right" class="left_txt2">
													&nbsp;
												</td>
												<td>
													&nbsp;
												</td>
												<td height="30">
													&nbsp;
												</td>
												<td align="right">
													&nbsp;
												</td>
												<td>
													&nbsp;
												</td>
												<td height="30">
													&nbsp;
												</td>
											</tr>
											<tr>
												<td height="30" colspan="2" align="center">
												<span class="left_txt2"> 
													 <input id="save" name="input" type="button" class="btn_w102_blue" value="保存" onclick="javascript:doSubmit();" />&nbsp;&nbsp; 
													 <input id="back" name="input" type="button" class="btn_w102_blue" value="返回" onclick="history.go(-1);" />
												</span>
												</td>

												<td width="5">&nbsp;</td>
												<td height="30">&nbsp;</td>
											</tr>
											<tr>
												<td height="17" colspan="6" align="right">&nbsp;</td>
											</tr>
										</table>
									</div>
									<div style="clear: both;"></div>
								</div>
							</div>

						</div>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div class="bottom" id="bottom">
		<%@ include file="/WEB-INF/context/commons/bottom.jsp"%>
	</div>
</body>
</html>



