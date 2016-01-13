<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/context/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>USER编辑</title>
<%@ include file="/commons/meta.jsp"%>
<script language="javascript" src="${ctx}/scripts/script.js"></script>
<link href="${ctx}/styles/css.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="${ctx}/scripts/colorbox/jquery.colorbox.js"></script>
<link href="${ctx}/scripts/colorbox/colorbox.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/scripts/validate/jquery.validate.css" type="text/css" rel="stylesheet" />
<script src="${ctx}/scripts/validate/jquery.validate.js" type="text/javascript"></script>
<script src="${ctx}/scripts/validate/messages_cn.js" type="text/javascript"></script>
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
			<input type="hidden" name="id" value="${user.id}" />
			<table border="0" cellspacing="0" cellpadding="0" style="width: 100%;">
				<tr>
					<td width="188" valign="top">
						<%@ include file="/include/adminLeft.jsp"%>
					</td>
					<td valign="top">

						<div class="table_right">

							<div class="moudle_div">

								<div class="sidebar_title">

									<div class="sidebar_title_border">
										USER编辑
									</div>
								</div>

								<div style="border: #CCC solid 1px;" align="center">
									<div >
										<table >
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
												<td class="lab"><label>昵称:</label></td>
												<td class="text"><input type="text" name="nickName"
													value="${userTable.nickName}" size="30" /></td>
											</tr>
											<tr>
												<td class="lab"><label>登录名:</label></td>
												<td class="text"><input type="text" name="userName"
													value="${userTable.userName}" size="30" /></td>
											</tr>
											<tr>
									            <td height="30" align="right" class="left_txt2">新密码</td>
									            <td height="30"><input type="password" name="pwd" id="pwd"  size="30"/></td>
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



