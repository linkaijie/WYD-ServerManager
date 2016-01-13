<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="java.util.List"%>
<%@ include file="/commons/taglibs.jsp"%>
<script src="${widgethome}/open/lhgdialog.js" type=text/javascript></script>
<script type="text/javascript">
	var ctx = "${ctx}";

	function changePwd(){
		lhgdialog.opendlg('修改密码',ctx+"/user/userPwdForm.action",480,200,true,true);
	}
</script>
<div class="top">
	<div id="logo" title="部署服务器"></div>
	<div id="dishi">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="select_dishi">&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2"></td>
			</tr>
		</table>
	</div>
	<div class="userinfo">
		<table width="100%" border="0" cellspacing="0" cellpadding="5">
			<tr>
				<td>
					<a href="javascript:changePwd();" id="icon2">修改密码 </a>&nbsp;&nbsp;|&nbsp;&nbsp;
					<a href="${ctx}/user/userList.action" class="nav" id="tab2"><span>系统管理</span></a>&nbsp;&nbsp;|&nbsp;&nbsp;
					<a href="${ctx}/j_spring_security_logout" id="icon2">退出系统 </a>&nbsp;&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
		</table>
	</div>
	<div class="topnav">
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td align="center" id="nav">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0">
						<tr>
							<td align="center" id="nav">
								<table width="760" border="0" align="center" cellpadding="0"
									cellspacing="0">
									<tr>
										<td id="sys_menu_td">
											<a href="${ctx}/index.jsp" class="nav" id="tab">
											<span>首 页 </span></a>
											<div></div> 
											<a href="${ctx}/accountserver/accountServerList.action" class="nav" id="tab1"><span>认证服</span></a>
											<div></div> 
											<a href="${ctx}/ipdserver/ipdServerList.action" class="nav" id="tab2"><span>IPD</span></a>
											<div></div> 
											<a href="${ctx}/battleserver/battleServerList.action" class="nav" id="tab3"><span> 战斗服 </span></a>
											<div></div> 
											<a href="${ctx}/worldserver/worldServerList.action" class="nav" id="tab3"><span> 世界服 </span></a>
											<div></div> 
											<a href="${ctx}/dispatchserver/dispatchServerList.action" class="nav" id="tab4"><span>分发服</span></a>
											<div></div> 
											<a href="${ctx}/servermanage/serverManageList.action" class="nav" id="tab4"><span>服务管理</span></a>
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
