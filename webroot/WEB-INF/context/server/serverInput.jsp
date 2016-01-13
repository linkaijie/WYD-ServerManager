<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/context/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
String addressName=application.getInitParameter("addressName");
%>
<title><%=addressName%>-SERVER编辑</title>
<script language="javascript" src="${ctx}/scripts/script.js"></script>
<link href="${ctx}/styles/css.css" rel="stylesheet" type="text/css" />
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
			var serverIp = $("input[name=serverIp]");
            if (serverIp.val() == '') {
                alert("服务器IP不能为空");
                serverIp.focus();
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
		<form action="saveOrUpdate.action" method="post" onsubmit="return doSubmit();">
			<input type="hidden" name="id" value="${server.id}" />
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
										SERVER编辑
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
												<td class="text"><input type="text" name="serverName"
													value="${server.serverName}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>serverIp:</label></td>
												<td class="text"><input type="text" name="serverIp"
													value="${server.serverIp}" size="30" /><font color='red'>(请正确填写)</font></td>
											</tr>
											<tr>
												<td class="lab"><label>serverPort:</label></td>
												<td class="text"><input type="text" name="serverPort"
													value="${server.serverPort}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>httpPort:</label></td>
												<td class="text"><input type="text" name="httpPort"
													value="${server.httpPort}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>sshName:</label></td>
												<td class="text"><input type="text" name="sshName"
													value="${server.sshName}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>sshPwd:</label></td>
												<td class="text"><input type="password" name="sshPwd"
													value="${server.sshPwd}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>sshUrl:</label></td>
												<td class="text"><input type="text" name="sshUrl"
													value="${server.sshUrl}" size="40" /></td>
											</tr>
											<tr>
                                                <td class="lab"><label>使用类型:</label></td>
                                                <td class="text">
                                                <select id="useType" name="useType">
                                                    <option value="1" <c:if test="${server.useType == 1}">selected="selected"</c:if>>游戏服务器</option>
                                                    <option value="2" <c:if test="${server.useType == 2}">selected="selected"</c:if>>缓存服务器</option>
                                                    <option value="3" <c:if test="${server.useType == 3}">selected="selected"</c:if>>数据库服务器</option>
                                                </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="lab"><label>描述:</label></td>
                                                <td class="text"><input type="text" name="remark"
                                                    value="${server.remark}" size="40" /></td>
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



