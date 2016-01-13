<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<script type="text/javascript">
	function showAdmin(url){
		var w=(document.documentElement.clientWidth+15)+"px";
		var h=(document.documentElement.clientHeight)+"px";
		url=ctx+"/"+url;
		window.open (url,'newwindow', 'height='+h+', width='+w+', top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes,resizable=yes,location=yes, status=yes');
	}
	function changePwd(){
		lhgdialog.opendlg('<s:text name="ModifyPassword"/>',ctx+"/user/toPwdPage.action?id="+"${LOGIN_CURRENT_USER.id}",480,200,true,true);
	}
</script>
<div class="top">
	<div id="logo" title="<s:text name="GMTool"/>"></div>
	<div id="dishi" style="top: 48px">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="select_dishi">
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
			</tr>
			<tr>
				<td colspan="2"></td>
			</tr>
		</table>
	</div>
	<div class="userinfo">
		<table width="100%" border="0" cellspacing="0" cellpadding="5">
			<tr>
				<td align="left">
					&nbsp;&nbsp;&nbsp;&nbsp;${LOGIN_CURRENT_USER.userName}【${LOGIN_CURRENT_USER.staffTable.fullName}】，
					<s:text name="WelcomeGMTool"></s:text>
					&nbsp;(&nbsp;${LOGIN_CURRENT_GAME_CHILD_OBJECT.gameServer.gameName}&nbsp;|&nbsp;${LOGIN_CURRENT_GAME_CHILD_OBJECT.gameName}&nbsp;)
				</td>
				<td>
					<a href="javascript:changePwd();" id="icon2"><s:text name="ModifyPassword" /> </a>&nbsp;&nbsp;|&nbsp;&nbsp;
					<%
					    String hasAdmin = (String) request.getSession().getAttribute("FIRST_ADMIN_URL");
					    if (hasAdmin != null) {
					%>
					<a href="javascript:showAdmin('<%=hasAdmin%>');" id="icon2"><s:text name="SystemManagement" /> </a>&nbsp;&nbsp;|&nbsp;&nbsp;
					<%
					    }
					%>
					<a href="${ctx}/logout/logout.action" id="icon2"><s:text name="ExitSystem" /> </a>&nbsp;&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
		</table>
	</div>
	<div class="topnav">
		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td align="center" id="nav">
					<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
						<tr>
							<td align="center" id="nav">
								<table width="660" border="0" align="center" cellpadding="0" cellspacing="0">
									<tr>
										<td id="sys_menu_td">
											<a href="#" class="nav"></a>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
</div>
