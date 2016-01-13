<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
String addressName=application.getInitParameter("addressName");
%>
<title><%=addressName%>-World服务生成</title>
<%@ include file="/commons/meta.jsp"%>
<link href="${ctx}/styles/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
		function doSubmit() {
		    var names = document.getElementById("name");
			if (names.value == '') {
				alert("名称不能为空");
				names.focus();
				return false;
			}
			var serverIds = document.getElementById("serverId");
			if (serverIds.value == '') {
                alert("请选择所属服务器");
                serverIds.focus();
                return false;
            }
            var machinecodes = document.getElementById("machinecode");
            var reg = new RegExp("^[0-9,-]*$"); 
            if (machinecodes.value == '') {
                alert("分區號不能为空");
                machinecodes.focus();
                return false;
            }
            if(!reg.test(machinecodes.value)){  
                alert("分區號请输入数字!");  
                machinecodes.focus();
                return false;
            }
            var machinecodesTemp = machinecodes.value
            if(machinecodesTemp<0){
                if(!reg.test(names.value)){
                    alert("名稱请输入数字!");  
                    names.focus();
                    return false;
                }
            }
            var serverTypes = document.getElementById("serverType");
            if (serverTypes.value == '') {
                alert("请选择类型");
                serverTypes.focus();
                return false;
            }
            $.ajax({
                type : 'POST',
                data: {
                    name : names.value, 
                    serverId : serverIds.value,
                    machinecode : machinecodes.value,
                    serverType : serverTypes.value
                   },
                url : "worldServerCreate.action",
                success : function(msg) {
                if(msg!="" && msg=='true')
                {
                    window.location.href=ctx+"/worldserver/worldServerList.action";
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
										WORLD創建
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
												<input type="text" id="name" name="name" size="12" />（只需输入名称，不用加分區编号，如“枪王之王”）</td>
											</tr>
											<tr>
												<td class="lab"><label>所属服务器:</label></td>
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
                                                <td class="lab"><label>分區號:</label></td>
                                                <td class="text"><input type="text" id="machinecode" name="machinecode" size="12" />（输入数字,如1、66，10001等）</td>
                                            </tr>
											<tr>
                                                <td class="lab"><label>类型选择:</label></td>
                                                <td class="text">
                                                    <select id="serverType" name="serverType">
                                                        <option value="">请选择</option>
                                                        <option value="1" <c:if test="${serverType == 1}">selected="selected"</c:if>>国服IOS</option>
                                                        <option value="2" <c:if test="${serverType == 2}">selected="selected"</c:if>>混服</option>
                                                        <option value="3" <c:if test="${serverType == 3}">selected="selected"</c:if>>硬核</option>
                                                        <option value="2" <c:if test="${serverType == 2}">selected="selected"</c:if>>台湾=2</option>
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



