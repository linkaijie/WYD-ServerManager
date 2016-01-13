<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/context/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>DISPATCH编辑</title>
<%@ include file="/commons/meta.jsp"%>
<link href="${ctx}/styles/css.css" rel="stylesheet" type="text/css" />
<script src="${wdatehome}/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
		function doSubmit() {
		    var name = document.getElementById("name");
			if (name.value == '') {
				alert("名称不能为空");
				name.focus();
				return false;
			}
			var gameId = document.getElementById("gameId");
			if (gameId.value == '') {
				alert("请选择所属游戏");
				gameId.focus();
				return false;
			}
			var serverId = document.getElementById("serverId");
			if (serverId.value == '') {
                alert("请选择所属服务器");
                serverId.focus();
                return false;
            }
			var ipdId = document.getElementById("ipdId");
			if (ipdId.value == '') {
                alert("请选择IPD服务器");
                ipdId.focus();
                return false;
            }
			var worldId = document.getElementById("worldId");
			if (worldId.value == '') {
                alert("请选择WORLD服务器");
                worldId.focus();
                return false;
            }
			var isUse = document.getElementById("isUse");
            if (isUse.value == '') {
                alert("请选择是否启用");
                isUse.focus();
                return false;
            }
            var state = document.getElementById("state");
            if (state.value == '') {
                alert("请选择IPD服务器");
                state.focus();
                return false;
            }
            var isDeploy = document.getElementById("isDeploy");
            if (isDeploy.value == '') {
                alert("请选择部署状态");
                isDeploy.focus();
                return false;
            }
            var serverTypes = document.getElementById("serverTypes");
            if (serverTypes.value == '') {
                alert("请选择类型");
                serverTypes.focus();
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
            var path = document.getElementById("path");
            if (path.value == '') {
                alert("请输入路径");
                path.focus();
                return false;
            }
            if (path.value.indexOf("_x_x_") > 0) {
                alert("请修改'_x_x_'中的'x'部分");
                path.focus();
                return false;
            }
		}
</script>
</head>
<body>
	<!-- 头部菜单 -->
	<jsp:include flush="false" page="/sys_menu.jsp" />
	<div style="height: 10px;"></div>
	<div class="table_div">
		<form action="saveOrUpdateMore.action" method="post" onsubmit="return doSubmit();">
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
												<td class="lab"><label>名称:</label></td>
												<td class="text">
												<input type="text" id="name" name="name" value="${dispatchServer.name}" size="30" />（只需输入名称，不用加后面的编号，如“1服分发服”）</td>
											</tr>
											<tr>
												<td class="lab"><label>所属游戏:</label></td>
												<td class="text">
													<select id="gameId" name="gameId">
														<option value="">请选择服务器</option>
														<c:forEach items="${game}" var="game">
															<option value="${game.id}" <c:if test="${dispatchServer.gameId == game.id}">selected="selected"</c:if>>${game.gameName}</option>
														</c:forEach>
													</select>
												</td>
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
												<td class="lab"><label>IPD服务器:</label></td>
												<td class="text">
												<select id="ipdId" name="ipdId">
														<option value="">请选择IPD服务器</option>
														<c:forEach items="${ipdServer}" var="ipdServer">
															<option value="${ipdServer.id}" <c:if test="${dispatchServer.ipdId == ipdServer.id}">selected="selected"</c:if>>${ipdServer.name}</option>
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
												<td class="lab"><label>上传模板:</label></td>
												<td class="text">
													<c:choose>
														<c:when test="${dispatchServer.id > 0}">
															<input type="text" id="modelPath" name="modelPath" value="${dispatchServer.modelPath}" size="30" />
														</c:when>
														<c:otherwise>
															<select id="modelPath" name="modelPath">
																<option value="">请选择模板路径</option>
																<c:forEach items="${model}" var="model">
																	<option value="${model.dispatchPatch}">${model.name}</option>
																</c:forEach>
															</select>
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<tr>
												<td class="lab"><label>更新模板:</label></td>
												<td class="text">
													<c:choose>
														<c:when test="${dispatchServer ne null }">
															<input type="text" id="updatePath" name="updatePath" value="${dispatchServer.updatePath}" size="40" />
														</c:when>
														<c:otherwise>
															<input type="text" name="updatePath" value="/data/apps/model/update/dispatchServer" size="40" />
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<tr>
												<td class="lab"><label>服务器目录:</label></td>
												<td class="text"><input type="text" id="path" name="path" value="/data/apps/gunsoul/dispatchServer_x_x_" size="40" />（只需修改‘x’，第一个‘x’为serverType，第二个‘x’为第几区，后面不用填）</td>
											</tr>
											<tr>
												<td class="lab">
													<label>是否启用:</label>
												</td>
												<td class="text">
													<select id="isUse" name="isUse">
														<option value="">请选择</option>
														<option value="0" <c:if test="${dispatchServer.isUse == 0}">selected="selected"</c:if>> 禁用</option>
														<option value="1" <c:if test="${dispatchServer.isUse == 1}">selected="selected"</c:if>>启用</option>
													</select>
												</td>
											</tr>
											<tr>
												<td class="lab">
													<label>状态:</label>
												</td>
												<td class="text">
													<select id="state" name="state">
														<option value="">请选择</option>
														<c:if test="${empty dispatchServer.state}">
                                                            <option value="0" selected="selected" <c:if test="${dispatchServer.state == 0}">selected="selected"</c:if>>已关闭</option>
                                                        </c:if>
                                                        <c:if test="${not empty dispatchServer.state}">
                                                            <option value="0" <c:if test="${dispatchServer.state == 0}">selected="selected"</c:if>>已关闭</option>
                                                        </c:if>
                                                        <option value="1" <c:if test="${dispatchServer.state == 1}">selected="selected"</c:if>>已开启</option>
													</select>
												</td>
											</tr>
											<tr>
												<td class="lab"><label>是否已部署:</label></td>
												<td class="text">
													<select id="isDeploy" name="isDeploy">
														<option value="">请选择</option>
														<option value="0" <c:if test="${dispatchServer.isDeploy == 0}">selected="selected"</c:if>>未部署</option>
														<c:if test="${empty dispatchServer.isDeploy}">
														    <option value="1" selected="selected" <c:if test="${dispatchServer.isDeploy == 1}">selected="selected"</c:if>>已部署</option>
														</c:if>
														<c:if test="${not empty dispatchServer.isDeploy}">
														    <option value="1" <c:if test="${dispatchServer.isDeploy == 1}">selected="selected"</c:if>>已部署</option>
														</c:if>
													</select>
												</td>
											</tr>
											<tr>
                                            <td class="lab"><label>类型选择:</label></td>
                                            <td class="text">
                                                <select id="serverTypes" name="serverTypes">
                                                    <option value="">请选择</option>
                                                    <option value="1" <c:if test="${serverTypes == 1}">selected="selected"</c:if>>IOS=1</option>
                                                    <option value="2" <c:if test="${serverTypes == 2}">selected="selected"</c:if>>混服=2</option>
                                                    <option value="3" <c:if test="${serverTypes == 3}">selected="selected"</c:if>>硬核=3</option>
                                                    <option value="2" <c:if test="${serverTypes == 2}">selected="selected"</c:if>>台湾=2</option>
                                                </select>
                                            </td>
                                        </tr>
											<tr>
                                                <td class="lab"><label>生成数量:</label></td>
                                                <td class="text"><input type="text" id="creatrNumber" name="creatrNumber" size="30" />(输入数字)</td>
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
													 <input name="input" type="submit" class="btn_w102_blue" value="保存" />&nbsp;&nbsp; 
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



