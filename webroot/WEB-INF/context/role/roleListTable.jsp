<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name="CompanyName"></s:text></title>
		<%@ include file="/commons/meta.jsp"%>
		<script language="javascript" src="${ctx}/scripts/script.js"></script>
		<link href="${ctx}/styles/css.css" rel="stylesheet" type="text/css" />
		<script language="javascript" src="${ctx}/scripts/colorbox/jquery.colorbox.js"></script>
		<link href="${ctx}/scripts/colorbox/colorbox.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript">

$(document).ready(function(){
	
	
	$(".iframe").colorbox({iframe:true, width:"70%", height:"60%"});
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
			<table border="0" cellspacing="0" cellpadding="0" width="" style="width: 100%;">
				<tr>
					<td width="188" valign="top">
						<!-- 嵌入分页页面 -->
						<%@ include file="/include/adminLeft.jsp"%>
					</td>
					<td valign="top">
						<!---->
						<div class="table_right">
							<div class="moudle_div">
								<div class="sidebar_title">
									<div class="sidebar_title_border">
										<s:text name="CurrentPosition_SystemManagement_AdministrativeArea"></s:text>
									</div>
								</div>
								<div class="seach_div">
									<table width="100%" border="0" cellpadding="5" cellspacing="0" class="search_table">
										<tr>
											<td width="600">
												<s:text name="IntelligenceTitle"></s:text>
												<input name="input3" type="text" />
												&nbsp;&nbsp;&nbsp;&nbsp;
												<s:text name="AuditStatus"></s:text>
												<label for="select2"></label>
												<select name="select" id="select2">
													<option>
														<s:text name="Auditing"></s:text>
													</option>
													<option>
														<s:text name="Published"></s:text>
													</option>
													<option>
														<s:text name="Return_2"></s:text>
													</option>
												</select>
												&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
												<s:text name="IntelligenceType"></s:text>
												<label for="select4"></label>
												<select name="select4" id="select4">
													<option>
														<s:text name="AllTypes"></s:text>
													</option>
												</select>
												<label for="select"></label>
											</td>
											<td rowspan="2">
												<input name="input" type="button" value="<s:text name="Search"></s:text>" class="btn_w66_write" />
											</td>
										</tr>
										<tr>
											<td>
												<s:text name="SubmitTime"></s:text>
												<input name="input2" type="text" />
												－
												<input name="input2" type="text" />
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											</td>
										</tr>
									</table>
								</div>
								<div class="div_content">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td>
												<table class="bought-table">
													<thead>
														<tr>
															<th style="text-align: left">
																<s:text name="AreaName"></s:text>
																<a class="delay">sssssssssss</a>
															</th>
															<th width="100" align="center">
																<s:text name="AreaType"></s:text>
															</th>
															<th width="160" align="center">
																<s:text name="Remark"></s:text>
															</th>
															<th width="100" align="center">
																<s:text name="Created"></s:text>
															</th>
															<th width="120" align="center">
																<s:text name="Operate"></s:text>
															</th>
														</tr>
													</thead>
													<tbody class=" success-order">
														<c:forEach items="${page.list}" var="item">
															<tr>
																<td align="left">
																	${item.areaName}
																</td>
																<td align="center">
																	${item.areaType}
																</td>
																<td align="center">
																	${item.areaCode}
																</td>
																<td align="center">
																	<fmt:formatDate pattern="yyyy-MM-dd" value="${item.updateDate}" var="ud" />
																	${ud}
																</td>
																<td rowspan="1" align="center">
																	<a href="#"><s:text name="Details"></s:text> </a>
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
																			<%@ include file="/include/page.jsp"%>
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
								</div>
							</div>
						</div>
						<!---->
					</td>
				</tr>
			</table>
		</div>
		<div id="footer">
			<div class="content">
				<div class="content_l"></div>
				<div class="content_r">
					<s:text name="CMCC_AllRightsReserved"></s:text>
					&nbsp;&nbsp;&nbsp; &copy;2012 cmcc Corporation. All rights reserved.
				</div>
			</div>
		</div>
	</body>
</html>
