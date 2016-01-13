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
		<script language="javascript" src="${ctx}/scripts/jquery-1.3.2.js"></script>
		<script language="javascript" src="${ctx}/scripts/colorbox/jquery.colorbox.js"></script>
		<link href="${ctx}/scripts/colorbox/colorbox.css" rel="stylesheet" type="text/css" />
		<script language="javascript" src="${ctx}/scripts/colorbox/jquery.colorbox.js"></script>
		<script type="text/javascript">
		$(document).ready(function(){
			$(".iframe").colorbox({iframe:true, width:"35%", height:"60%"});
		});
		
		function subString(str){
			return str.substr(0,str.lastIndexOf(','))
		}
		
		function dataDelete(url){
			var boxs = document.getElementsByName('table_id');
			var data = "";
			//对象不存在,不处理,跳出方法
			if (!boxs) {
				return;
			}
			//获取选中行的ID
			for (i = 0; i < boxs.length; i++) {
				if (boxs[i].checked == true) {
					data += boxs[i].value + ",";
				}
			}
			//没有选择提示错误
			if (data == "") {
				alert($("#MustSelectCol").val());
				return;
			}
			//确认用户是否删除选中数据
			if (data != "") {
				if (!confirm($("#AreYouSureDeleteTheseData").val())) {
					return;
				}
			} else {
				return;
			}
			window.location.href = url+'?ids='+subString(data);
		}
</script>
	</head>
	<body>
		<input type="hidden" name="AreYouSureDeleteTheseData" id="AreYouSureDeleteTheseData" value="<s:text name="AreYouSureDeleteTheseData"></s:text>" />
		<input type="hidden" name="MustSelectCol" id="MustSelectCol" value="<s:text name="MustSelectCol"></s:text>" />
		<!-- 头部菜单 -->
		<jsp:include flush="false" page="/admin_menu.jsp" />
		<div style="height: 10px;">
		</div>
		<div class="table_div">
			<table border="0" cellspacing="0" cellpadding="0" width="" style="width: 100%;">
				<tr>
					<td width="188" valign="top">
						<!-- 嵌入分页页面 -->
						<%@ include file="/include/adminLeft.jsp"%>
					</td>
					<td valign="top">
						<div class="table_right">
							<div class="moudle_div">
								<div class="sidebar_title">
									<div class="sidebar_title_border">
										<s:text name="CurrentPosition_SystemManagement_RoleManagement"></s:text>
									</div>
								</div>
								<div class="div_content">
									<table class="bought-table">
										<thead>
											<tr class="toolbar skin-gray">
												<td colspan="5">
													<label>
														<input type="checkbox" class="all-selector" id="J_AllSelector" onclick="comBoxChange(this);" />
														<s:text name="SelectAll"></s:text>
													</label>
													<a href="${ctx}/role/form.action" class=" toolbtn"><s:text name="New_2"></s:text> </a>
													<a id="J_CombinPay2" href="javascript:dataDelete('${ctx}/role/deleteRole.action');" class="toolbtn"><s:text name="BatchDelete"></s:text>
													</a>
												</td>
											</tr>
										</thead>
									</table>
									<form action="${ctx}/role/pageList.action" method="post">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td>
													<table class="bought-table">
														<thead>
															<tr>
																<th style="text-align: center" width="30"></th>
																<th style="text-align: center" width="200">
																	<s:text name="RoleName_1"></s:text>
																</th>
																<th width="200" align="center">
																	<s:text name="RoleEncoding_1"></s:text>
																</th>
																<th width="200" align="center">
																	<s:text name="RoleRemark_1"></s:text>
																</th>
																<th width="160" align="center">
																	<s:text name="ResourceAllocation"></s:text>
																</th>
																<th width="160" align="center">
																	<s:text name="ViewAllocation"></s:text>
																</th>
																<th width="15%" align="center">
																	<s:text name="Operate"></s:text>
																</th>
															</tr>
														</thead>
														<tbody class=" success-order">
															<c:forEach items="${page.list}" var="item">
																<tr>
																	<td align="center">
																		<input type="checkbox" class="all-selector" name="table_id" value="${item.id}" />
																	</td>
																	<td align="center">
																		${item.roleName}
																	</td>
																	<td align="center">
																		${item.roleCode}
																	</td>
																	<td align="center">
																		${item.roleDesc}
																	</td>
																	<td align="center">
																		<a class="iframe" href="${ctx}/jsp/admin/role/showResourceTree.jsp?id=${item.id}"><s:text name="ResourceAllocation"></s:text> </a>
																	</td>
																	<td align="center">
																		<a class="iframe" href="${ctx}/jsp/admin/role/showUserRoleTree.jsp?id=${item.id}"><s:text name="ViewAllocation"></s:text> </a>
																	</td>
																	<td rowspan="1" align="center">
																		<a href="${ctx}/role/form.action?id=${item.id}"><s:text name="Edit"></s:text> </a>
																		<a href="${ctx}/role/deleteRole.action?ids=${item.id}"><s:text name="Delete"></s:text> </a>
																	</td>
																</tr>
															</c:forEach>
														</tbody>
														<tfoot>
															<tr class="sep-row">
																<td colspan="5"></td>
															</tr>
															<tr class="pagebar">
																<td colspan="5">
																	<table width="100%" border="0" cellspacing="0" cellpadding="5">
																		<tr>
																			<td>
																				&nbsp;
																			</td>
																			<td width="410">
																				<!-- 嵌入分页页面 -->
																				<%@ include file="/include/pageAjax.jsp"%>
																			</td>
																		</tr>
																	</table>
																</td>
															</tr>
														</tfoot>
													</table>
													<div style="clear: both;"></div>
												</td>
											</tr>
										</table>
									</form>
								</div>
							</div>
						</div>
					</td>
				</tr>
			</table>
		</div>
		<div class="bottom" id="bottom">
			<jsp:include flush="false" page="/down.jsp" />
		</div>
	</body>
</html>
