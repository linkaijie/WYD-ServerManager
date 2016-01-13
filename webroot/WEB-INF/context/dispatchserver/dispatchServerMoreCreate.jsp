<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
    String addressName=application.getInitParameter("addressName");
%>
<title><%=addressName%>-DISPATCH生成</title>
<%@ include file="/commons/meta.jsp"%>
<link href="${ctx}/styles/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
		function doSubmit() {
		    var machinecode = document.getElementById("machinecode");
            var reg = new RegExp("^[0-9,-]*$"); 
            if (machinecode.value == '') {
                alert("分区号不能为空");
                machinecode.focus();
                return false;
            }
            if(!reg.test(machinecode.value)){  
                alert("请输入数字!");  
                machinecode.focus();
                return false;
            }  
            var dispatchServerId = document.getElementById("dispatchServerId");
            var reg = new RegExp("^[0-9]*$"); 
            if (dispatchServerId.value == '') {
                alert("配置文件中的id不能为空");
                dispatchServerId.focus();
                return false;
            }
            if(!reg.test(dispatchServerId.value)){  
                alert("请输入数字!");  
                dispatchServerId.focus();
                return false;
            }  
			var serverId = document.getElementById("serverId");
			if (serverId.value == '') {
                alert("请选择所属服务器");
                serverId.focus();
                return false;
            }
			var worldId = document.getElementById("worldId");
			if (worldId.value == '') {
                alert("请选择WORLD服务器");
                worldId.focus();
                return false;
            }
            var serverType = document.getElementById("serverType");
            if (serverType.value == '') {
                alert("请选择类型");
                serverType.focus();
                return false;
            }
            var creatrNumber = document.getElementById("creatrNumber");
            var reg = new RegExp("^[0-9]*$"); 
            if (creatrNumber.value == '') {
                alert("生成数量不能为空");
                creatrNumber.focus();
                return false;
            }
            if(!reg.test(creatrNumber.value)){  
                alert("请输入数字!");  
                creatrNumber.focus();
                return false;
            }  
            $.ajax({
                type : 'POST',
                data: {
                    machinecode : machinecode.value,
                    serverId : serverId.value,
                    worldId : worldId.value,
                    dispatchServerId : dispatchServerId.value,
                    serverType : serverType.value,
                    creatrNumber : creatrNumber.value
                   },
                url : "saveOrUpdateMoreDispatch.action",
                success : function(msg) {
                if(msg!="" && msg=='true')
                {
                    window.location.href=ctx+"/dispatchserver/dispatchServerList.action";
                }else{
                    alert(msg);
                }
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
		<form method="post">
			<input type="hidden" name="id" value="${dispatchServer.id}" />
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
										DISPATCH编辑
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
                                                <td class="lab"><label>分區號:</label></td>
                                                <td class="text"><input type="text" id="machinecode" name="machinecode" size="12" />（输入数字,如1、66，10001等）</td>
                                            </tr>
                                            <tr>
                                                <td class="lab"><label>配置文件中的id:</label></td>
                                                <td class="text"><input type="text" id="dispatchServerId" name="dispatchServerId" size="12" />（这里填写主机号，如主机s2则填2，主机s18则填18）</td>
                                            </tr>
											<tr>
												<td class="lab"><label>所属服务器:</label></td>
												<td class="text">
												<select id="serverId" name="serverId">
														<option value="">请选择服务器</option>
														<c:forEach items="${server}" var="server">
															<option value="${server.id}" <c:if test="${dispatchServer.serverId == server.id}">selected="selected"</c:if>>${server.serverName}</option>
														</c:forEach>
												</select>
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
                                                <td class="lab"><label>类型选择:</label></td>
                                                <td class="text">
                                                    <select id="serverType" name="serverType">
                                                        <option value="">请选择</option>
                                                        <option value="1" <c:if test="${serverType == 1}">selected="selected"</c:if>>IOS=1</option>
                                                        <option value="2" <c:if test="${serverType == 2}">selected="selected"</c:if>>混服=2</option>
                                                        <option value="3" <c:if test="${serverType == 3}">selected="selected"</c:if>>硬核=3</option>
                                                        <option value="2" <c:if test="${serverType == 2}">selected="selected"</c:if>>台湾=2</option>
                                                    </select>
                                                </td>
                                            </tr>
											<tr>
                                                <td class="lab"><label>生成数量:</label></td>
                                                <td class="text"><input type="text" id="creatrNumber" name="creatrNumber" size="12" />(输入数字)</td>
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
												     <input name="input" type="button" class="btn_w102_blue" value="保存" onclick="javascript:doSubmit();" />&nbsp;&nbsp;
													 <input name="input" type="button" class="btn_w102_blue" value="返回" onclick="history.go(-1);" />
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



