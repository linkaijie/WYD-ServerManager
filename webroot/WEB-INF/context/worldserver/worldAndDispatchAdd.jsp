<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
String addressName=application.getInitParameter("addressName");
%>
<title><%=addressName%>-新增world和dispatch记录</title>
<%@ include file="/commons/meta.jsp"%>
<link href="${ctx}/styles/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
		function doSubmit() {
		    var name = document.getElementById("name");
            if (name.value == '') {
                alert("请输入名称");
                name.focus();
                return false;
            }
			var serverId = document.getElementById("serverId");
			if (serverId.value == '') {
                alert("请选择所属服务器");
                serverId.focus();
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
            var machinecodesTemp = machinecodes.value;
//            if(machinecodesTemp<0){
//                if(!reg.test(name.value)){
//                    alert("名稱请输入数字!");  
//                    name.focus();
//                    return false;
//                }
//            }
            var dispatchServerId = document.getElementById("dispatchServerId");
            if (dispatchServerId.value == '') {
                alert("dispatch配置文件中的id不能為空");
                dispatchServerId.focus();
                return false;
            }
            var battleip = document.getElementById("battleip");
            if (battleip.value == '') {
                alert("战斗服ip不能為空");
                battleip.focus();
                return false;
            }
            var battleport = document.getElementById("battleport");
            if (battleport.value == '') {
                alert("战斗服端口不能為空");
                battleport.focus();
                return false;
            }
            var serverTypes = document.getElementById("serverType");
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
            var mergeIds = document.getElementById("mergeIds");
            var isDeploy = document.getElementById("isDeploy");
            var save = document.getElementById("save");
            save.disabled=true;
            $.ajax({
                type : 'POST',
                data: {
                    name : name.value, 
                    serverId : serverId.value,
                    machinecode : machinecodes.value,
                    dispatchServerId : dispatchServerId.value,
                    battleip : battleip.value,
                    battleport : battleport.value,
                    serverType : serverTypes.value,
                    creatrNumber : creatrNumber.value,
                    isDeploy : isDeploy.value,
                    mergeIds : mergeIds.value
                   },
                url : "worldAndDispatchSave.action",
                success : function(msg) {
                if(msg!="" && msg=='true')
                {
                    alert("创建成功");
                    window.location.href=ctx+"/worldserver/worldServerList.action?serverId="+serverId.value;
                } else {
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
										新增world和dispatch信息
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
                                                <td class="lab"><label>分區號:</label></td>
                                                <td class="text"><input type="text" id="machinecode" name="machinecode" size="12" />（输入数字,如1、66，10001等）</td>
                                            </tr>
											<tr>
                                                <td class="lab"><label>dispatch配置文件中的id:</label></td>
                                                <td class="text"><input type="text" id="dispatchServerId" name="dispatchServerId" size="12" />（这里填写主机号，如主机s2则填2，主机s18则填18）</td>
                                            </tr>
                                            <tr>
                                                <td class="lab"><label>战斗服ip:</label></td>
                                                <td class="text"><input type="text" id="battleip" name="battleip" size="12" value="${battleIp}" readOnly="true" /></td>
                                                <!--<td class="text"><input type="text" id="battleip" name="battleip" size="12" value="10.10.40.139" /></td>--><!--混服
                                                <td class="text">
                                                    <select id="battleip" name="battleip">
                                                        <option value="">------请选择-------</option>
                                                        <option value="10.46.64.112" >国服IOS:10.46.64.112</option>
                                                        <option value="10.10.40.139" >TX:10.10.40.139</option>
                                                        <option value="10.0.0.181" >台湾:10.0.0.181</option>
                                                        <option value="125.212.244.5" >越南测试:125.212.244.5</option>
                                                    </select>
                                                </td>
                                                -->
                                            </tr>
                                            <tr>
                                                <td class="lab"><label>战斗服端口:</label></td>
                                                <td class="text"><input type="text" id="battleport" name="battleport" size="12" value="2000" /></td>
                                            </tr>
                                            <!--
                                            <tr>
                                                <td class="lab"><label>类型选择:</label></td>
                                                <td class="text">
                                                    <select id="serverType" name="serverType">
                                                        <option value="">------请选择-------</option>
                                                        <option value="1" <c:if test="${serverType == 1}">selected="selected"</c:if>>国服IOS=1</option>
                                                        <option value="2" <c:if test="${serverType == 2}">selected="selected"</c:if>>混服=2</option>
                                                        <option value="3" <c:if test="${serverType == 3}">selected="selected"</c:if>>硬核=3</option>
                                                        <option value="2" <c:if test="${serverType == 2}">selected="selected"</c:if>>台湾=2</option>
                                                        <option value="1" <c:if test="${serverType == 1}">selected="selected"</c:if>>越南=1</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            -->
                                            
                                            <tr>
                                                <td class="lab"><label>类型选择:</label></td>
                                                <td class="text">
                                                
                                                    <select id="serverType" name="serverType">
                                                            <option value="">------请选择-------</option>
                                                            <c:forEach items="${serverTypeList}" var="serverTypeList">
                                                                <option value="${serverTypeList.id}">${serverTypeList.name}</option>
                                                            </c:forEach>
                                                    </select>
                                                
                                                </td>
                                            </tr>
                                            
                                            
                                            <tr>
                                                <td class="lab"><label>dispatch生成数量:</label></td>
                                                <td class="text"><input type="text" id="creatrNumber" name="creatrNumber" size="12" />(输入数字)</td>
                                            </tr>
                                            <tr>
                                                <td class="lab"><label>部署状态:</label></td>
                                                <td class="text">
                                                    <select id="isDeploy" name="isDeploy">
                                                        <option value="0" <c:if test="${isDeploy == 0}">selected="selected"</c:if>>未部署</option>
                                                        <option value="1" <c:if test="${isDeploy == 1}">selected="selected"</c:if>>已部署</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="lab"><label>合服前分區ID串:</label></td>
                                                <td class="text"><input type="text" id="mergeIds" name="mergeIds" size="30" />(输入数字,以‘,’隔開 )</td>
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



