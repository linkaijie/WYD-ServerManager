<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/context/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
String addressName=application.getInitParameter("addressName");
%>
<title><%=addressName%>-WORLD编辑</title>
<%@ include file="/commons/meta.jsp"%>
<link href="${ctx}/styles/css.css" rel="stylesheet" type="text/css" />
<script src="${wdatehome}/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
		$(document).ready(function(){
			//选中菜单
			$("#system").attr("class","nav hover");
			$(".iframe").colorbox({iframe:true, width:"35%", height:"60%"});
		});
		
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
			<input type="hidden" name="id" value="${worldServer.id}" />
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
										WORLD编辑
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
												<td class="text"><input type="text" name="name"
													value="${worldServer.name}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>所属游戏:</label></td>
												<td class="text">
													<select id="gameId" name="gameId">
														<option value="">请选择服务器</option>
														<c:forEach items="${game}" var="game">
															<option value="${game.id}" <c:if test="${worldServer.gameId == game.id}">selected="selected"</c:if>>${game.gameName}</option>
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
															<option value="${server.id}" <c:if test="${worldServer.serverId == server.id}">selected="selected"</c:if>>${server.serverName}</option>
														</c:forEach>
												</select>
												</td>
											</tr>
											<tr>
												<td class="lab"><label>ACCOUNT服务器:</label></td>
												<td class="text">
												<select id="accountId" name="accountId">
														<option value="">请选择ACCOUNT服务器</option>
														<c:forEach items="${accountServer}" var="accountServer">
															<option value="${accountServer.id}" <c:if test="${worldServer.accountId == accountServer.id}">selected="selected"</c:if>>${accountServer.name}</option>
														</c:forEach>
												</select>
												</td>
											</tr>
											<tr>
												<td class="lab"><label>上传模板:</label></td>
												<td class="text">
													<c:choose>
														<c:when test="${worldServer.id > 0}">
															<input type="text" name="modelPath" value="${worldServer.modelPath}" size="30" />
														</c:when>
														<c:otherwise>
															<select id="modelPath" name="modelPath">
																<option value="">请选择模板路径</option>
																<c:forEach items="${model}" var="model">
																	<option value="${model.worldPath}">${model.name}</option>
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
														<c:when test="${worldServer ne null }">
															<input type="text" name="updatePath" value="${worldServer.updatePath}" size="30" />
														</c:when>
														<c:otherwise>
															<input type="text" name="updatePath" value="/data/apps/model/update/worldServer" size="30" />
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<tr>
												<td class="lab"><label>localip:</label></td>
												<td class="text"><input type="text" name="localip" value="${worldServer.localip}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>port:</label></td>
												<td class="text"><input type="text" name="port" value="${worldServer.port}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>adminip:</label></td>
												<td class="text"><input type="text" name="adminip" value="${worldServer.adminip}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>adminport:</label></td>
												<td class="text"><input type="text" name="adminport" value="${worldServer.adminport}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>httpip:</label></td>
												<td class="text"><input type="text" name="httpip" value="${worldServer.httpip}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>publicip:</label></td>
												<td class="text"><input type="text" name="publicip" value="${worldServer.publicip}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>httpport:</label></td>
												<td class="text"><input type="text" name="httpport" value="${worldServer.httpport}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>authip:</label></td>
												<td class="text"><input type="text" name="authip" value="${worldServer.authip}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>authport:</label></td>
												<td class="text"><input type="text" name="authport" value="${worldServer.authport}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>serverpassword:</label></td>
												<td class="text"><input type="text" name="serverpassword" value="${worldServer.serverpassword}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>areaid:</label></td>
												<td class="text"><input type="text" name="areaid" value="${worldServer.areaid}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>machinecode:</label></td>
												<td class="text"><input type="text" name="machinecode" value="${worldServer.machinecode}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>serverType:</label></td>
												<td class="text"><input type="text" name="serverType" value="${worldServer.serverType}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>path:</label></td>
												<td class="text"><input type="text" name="path" value="${worldServer.path}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab">
													<label>状态:</label>
												</td>
												<td class="text">
													<select id="state" name="state">
														<option value="">请选择</option>
														<option value="0" <c:if test="${worldServer.state == 0}">selected="selected"</c:if>>已关闭</option>
														<option value="1" <c:if test="${worldServer.state == 1}">selected="selected"</c:if>>已开启</option>
													</select>
												</td>
											</tr>
											<tr>
												<td class="lab"><label>是否已部署:</label></td>
												<td class="text">
													<select id="isDeploy" name="isDeploy">
														<option value="">请选择</option>
														<option value="0" <c:if test="${worldServer.isDeploy == 0}">selected="selected"</c:if>>未部署</option>
														<option value="1" <c:if test="${worldServer.isDeploy == 1}">selected="selected"</c:if>>已部署</option>
													</select>
												</td>
											</tr>
											<tr>
												<td class="lab"><label>排序:</label></td>
												<td class="text"><input type="text" name="orders" value="${worldServer.orders}" size="30" /></td>
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



