<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/context/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
String addressName=application.getInitParameter("addressName");
%>
<title><%=addressName%>-DISPATCH编辑</title>
<%@ include file="/commons/meta.jsp"%>
<link href="${ctx}/styles/css.css" rel="stylesheet" type="text/css" />
<script src="${wdatehome}/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
		function doSubmit() {
			var username = $("input[name=username]");
			if (username.val() == '') {
				alert("用户名不能为空");
				username.focus();
				return false;
			}
			var id = $("input[name=id]");
			if (id.val() == '') {
				var password = $("input[name=password]");
				if (password.val() == '') {
					alert("密码不能为空");
					password.focus();
					return false;
				}
			}
		}
</script>
</head>
<body>
	<!-- 头部菜单 -->
	<jsp:include flush="false" page="/sys_menu.jsp" />
	<div style="height: 10px;"></div>
	<div class="table_div">
		<form action="saveOrUpdate.action" method="post" onsubmit="return doSubmit();">
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
												<input type="text" name="name"
													value="${dispatchServer.name}" size="30" /></td>
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
															<input type="text" name="modelPath" value="${dispatchServer.modelPath}" size="30" />
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
															<input type="text" name="updatePath" value="${dispatchServer.updatePath}" size="30" />
														</c:when>
														<c:otherwise>
															<input type="text" name="updatePath" value="/data/apps/model/update/dispatchServer" size="30" />
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<tr>
												<td class="lab"><label>worldip:</label></td>
												<td class="text"><input type="text" name="worldip"
													value="${dispatchServer.worldip}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>worldport:</label></td>
												<td class="text"><input type="text" name="worldport"
													value="${dispatchServer.worldport}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>localip:</label></td>
												<td class="text"><input type="text" name="localip"
													value="${dispatchServer.localip}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>port:</label></td>
												<td class="text"><input type="text" name="port"
													value="${dispatchServer.port}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>servertype:</label></td>
												<td class="text"><input type="text" name="servertype"
													value="${dispatchServer.servertype}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>publicserver:</label></td>
												<td class="text"><input type="text" name="publicserver"
													value="${dispatchServer.publicserver}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>publicport:</label></td>
												<td class="text"><input type="text" name="publicport"
													value="${dispatchServer.publicport}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>dispatchServerId:</label></td>
												<td class="text"><input type="text" name="dispatchServerId"
													value="${dispatchServer.dispatchServerId}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>serverarea:</label></td>
												<td class="text"><input type="text" name="serverarea"
													value="${dispatchServer.serverarea}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>machinecode:</label></td>
												<td class="text"><input type="text" name="machinecode"
													value="${dispatchServer.machinecode}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>companycode:</label></td>
												<td class="text"><input type="text" name="companycode"
													value="${dispatchServer.companycode}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>dispatcherserver:</label></td>
												<td class="text"><input type="text" name="dispatcherserver"
													value="${dispatchServer.dispatcherserver}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>dispatcherport:</label></td>
												<td class="text"><input type="text" name="dispatcherport"
													value="${dispatchServer.dispatcherport}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>服务器目录:</label></td>
												<td class="text"><input type="text" name="path"
													value="${dispatchServer.path}" size="30" /></td>
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
														<option value="0" <c:if test="${dispatchServer.state == 0}">selected="selected"</c:if>>已关闭</option>
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
														<option value="1" <c:if test="${dispatchServer.isDeploy == 1}">selected="selected"</c:if>>已部署</option>
													</select>
												</td>
											</tr>
											<tr>
												<td class="lab"><label>排序:</label></td>
												<td class="text"><input type="text" name="orders"
													value="${dispatchServer.orders}" size="30" /></td>
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
													 <input name="input" type="submit" class="btn_w102_blue" value="保存" onclick="submitForm();" />&nbsp;&nbsp; 
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



