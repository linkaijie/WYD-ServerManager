<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/context/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>RedisConfig编辑</title>
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
	<div style="height: 20px;"></div>
	<div class="table_div">
		<form action="saveOrUpdate.action" method="post" onsubmit="return doSubmit();">
			<input type="hidden" name="id" value="${redisConfig.id}" />
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
										redisConfig编辑
									</div>
								</div>

								<div style="border: #CCC solid 1px;" align="center">
									<div >
										<table style="font-size: 20px;">
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
												<td class="text"><input type="text" name="name" value="${redisConfig.name}" size="30" /></td>
											</tr>
											<tr>
                                                <td class="lab"><label>所属游戏:</label></td>
                                                <td class="text">
                                                <select id="gameId" name="gameId">
                                                    <option value="">请选择服务器</option>
                                                    <c:forEach items="${game}" var="game">
                                                        <option value="${game.id}" <c:if test="${redisConfig.gameId == game.id}">selected="selected"</c:if>>${game.gameName}</option>
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
															<option value="${server.id}" <c:if test="${redisConfig.serverId == server.id}">selected="selected"</c:if>>${server.serverName}</option>
														</c:forEach>
												</select>
												</td>
											</tr>
											<tr>
                                                <td class="lab"><label>地区编号:</label></td>
                                                <td class="text">
                                                <select id="areaId" name="areaId">
                                                        <option value="">请选择地区</option>
                                                        <c:forEach items="${areaList}" var="area">
                                                            <option value="${area.id}" <c:if test="${redisConfig.areaId == area.id}">selected="selected"</c:if>>${area.name}</option>
                                                        </c:forEach>
                                                </select>
                                            </tr>
                                            <tr>
                                                <td class="lab"><label>redis类型:</label></td>
                                                <td class="text">
                                                    <select id="redisType" name="redisType">
                                                        <option value="">请选择redis类型</option>
                                                        <option value="1" <c:if test="${redisConfig.redisType == 1}">selected="selected"</c:if>>游戏服redis</option>
                                                        <option value="2" <c:if test="${redisConfig.redisType == 2}">selected="selected"</c:if>>角色服redis</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="lab"><label>端口号:</label></td>
                                                <td class="text"><input type="text" name="ports" value="${redisConfig.ports}" size="30" /></td>
                                            </tr>
											<tr>
												<td class="lab"><label>上传模板:</label></td>
												<td class="text">
													<input type="text" name="modelPath" value="${redisConfig.modelPath}" size="30" />
												</td>
											</tr>
											<tr>
												<td class="lab"><label>更新模板:</label></td>
												<td class="text">
													<input type="text" name="updatePath" value="${redisConfig.updatePath}" size="30" />
												</td>
											</tr>
											<tr>
												<td class="lab"><label>服务器目录:</label></td>
												<td class="text"><input type="text" name="path" value="${redisConfig.path}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab">
													<label>状态:</label>
												</td>
												<td class="text">
													<select id="state" name="state">
														<option value="">请选择</option>
														<option value="0" <c:if test="${redisConfig.state == 0}">selected="selected"</c:if>>已关闭</option>
														<option value="1" <c:if test="${redisConfig.state == 1}">selected="selected"</c:if>>已开启</option>
													</select>
												</td>
											</tr>
											<tr>
												<td class="lab"><label>是否已部署:</label></td>
												<td class="text">
													<select id="isDeploy" name="isDeploy">
														<option value="">请选择</option>
														<option value="0" <c:if test="${redisConfig.isDeploy == 0}">selected="selected"</c:if>>未部署</option>
														<option value="1" <c:if test="${redisConfig.isDeploy == 1}">selected="selected"</c:if>>已部署</option>
													</select>
												</td>
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



