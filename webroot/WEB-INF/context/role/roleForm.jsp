<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name="CompanyName"></s:text>
		</title>
		<%@ include file="/commons/meta.jsp"%>
		<script language="javascript" src="${ctx}/scripts/script.js"></script>
		<link href="${ctx}/styles/css.css" rel="stylesheet" type="text/css" />
		<link href="${ctx}/scripts/validate/jquery.validate.css" type="text/css" rel="stylesheet" />
		<script src="${ctx}/scripts/validate/jquery.validate.js" type="text/javascript"></script>
		<script src="${ctx}/scripts/validate/messages_cn.js" type="text/javascript"></script>
		<script type="text/javascript">
$(document).ready(function(){
	
	//选中菜单
	$("#system").attr("class","nav hover");
	//为newForm注册validate函数
	$("#newForm").validate({
	
	});

	});


</script>
		<!--[if lte IE 6]>

<link href="css/ie_hack.css" rel="stylesheet" type="text/css" />

<![endif]-->
	</head>
	<body>
		<!-- 头部菜单 -->
		<jsp:include flush="false" page="/sys_menu.jsp" />
		<div style="height: 10px;">
		</div>
		<div class="table_div">
			<form action="${ctx}/role/saveOrUpdateRole.action" method="post" name="newForm" id="newForm">
				<input type="hidden" id="id" name="id" value="${info.id}" />
				<input type="hidden" name="treeId" id="treeId" value="${treeId}" />
				<table border="0" cellspacing="0" cellpadding="0" width="" style="width: 100%;">
					<tr>
						<td width="188" valign="top">
							<!-- 嵌入右侧菜单页面 -->
							<%@ include file="/include/adminLeft.jsp"%>
						</td>
						<td valign="top">
							<!---->
							<div class="table_right">
								<div class="moudle_div">
									<div class="sidebar_title">
										<div class="sidebar_title_border">
											<s:text name="CurrentPosition_RoleManagement_NewRole"></s:text>
										</div>
									</div>
									<div class="div_content border_css" style="border: #CCC solid 1px;">
										<div class="form_body">
											<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_item">
												<tr>
													<td width="26%" height="30" align="right" class="left_txt2">
														<s:text name="RoleName_2"></s:text>
													</td>
													<td width="5">
														&nbsp;
													</td>
													<td width="30%" height="30">
														<input name="roleName" id="roleName" type="text" value="${info.roleName}" class="required" />
													</td>
													<td width="100" align="right">
														&nbsp;
													</td>
													<td width="5">
														&nbsp;
													</td>
													<td height="30">
														&nbsp;
													</td>
												</tr>
												<tr>
													<td width="26%" height="30" align="right" class="left_txt2">
														<s:text name="RoleEncoding_2"></s:text>
													</td>
													<td width="5">
														&nbsp;
													</td>
													<td width="30%" height="30">
														<input name="roleCode" id="roleCode" type="text" value="${info.roleCode}" class="required" />
													</td>
													<td width="100" align="right">
														&nbsp;
													</td>
													<td width="5">
														&nbsp;
													</td>
													<td height="30">
														&nbsp;
													</td>
												</tr>
												<tr>
													<td height="30" align="right" class="left_txt2">
														<s:text name="RoleRemark_2"></s:text>
													</td>
													<td width="5">
														&nbsp;
													</td>
													<td height="30">
														<textarea name="roleDesc" rows="5" class="input1" id="roleDesc">${info.roleDesc}</textarea>
													</td>
													<td width="100" align="right">
														&nbsp;
													</td>
													<td width="5">
														&nbsp;
													</td>
													<td height="30">
														&nbsp;
													</td>
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
													<td height="30" align="right" class="left_txt2">
														&nbsp;
													</td>
													<td width="5">
														&nbsp;
													</td>
													<td height="30">
														<span class="left_txt2"><a href="#"> <input name="input" type="submit" class="btn_w102_blue"
																	value="<s:text name="Save"></s:text>" /> &nbsp;&nbsp;</a> <input name="input" type="button" class="btn_w102_blue"
																value="<s:text name="Cancel"></s:text>" onclick="history.go(-1);" /> </span>
													</td>
													<td width="100" align="right">
														&nbsp;
													</td>
													<td width="5">
														&nbsp;
													</td>
													<td height="30">
														&nbsp;
													</td>
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
													<td height="17" colspan="6" align="right">
														&nbsp;
													</td>
												</tr>
											</table>
										</div>
										<div style="clear: both;"></div>
									</div>
								</div>
							</div>
							<!---->
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div class="bottom" id="bottom">
			<jsp:include flush="false" page="/down.jsp" />
		</div>
	</body>
</html>
