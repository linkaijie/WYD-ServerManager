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
			<form action="${ctx}/resource/saveOrUpdateResource.action" method="post" name="newForm" id="newForm">
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
											<s:text name="CurrentPosition_SystemManagement_AddResource"></s:text>
										</div>
									</div>
									<div class="div_content border_css" style="border: #CCC solid 1px;">
										<div class="form_body">
											<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_item">
												<tr>
													<td height="30" align="right" class="left_txt2">
														<s:text name="ResourceName"></s:text>
														：
													</td>
													<td width="5">
														&nbsp;
													</td>
													<td height="30">
														<input name="resoName" id="resoName" type="text" value="${info.resoName}" class="required" />
													</td>
													<td width="100" align="right">
														<span class="left_txt2"><s:text name="ResourceCode"></s:text>：</span>
													</td>
													<td width="5">
														&nbsp;
													</td>
													<td height="30">
														<input name="resoCode" id="resoCode" type="text" value="${info.resoCode}" class="required" />
													</td>
												</tr>
												<tr>
													<td height="30" align="right" class="left_txt2">
														<s:text name="ResourceUrl"></s:text>
													</td>
													<td width="5">
														&nbsp;
													</td>
													<td height="30">
														<input name="resoUrl" id="resoUrl" type="text" value="${info.resoUrl}" />
													</td>
													<td width="100" align="right">
														<span class="left_txt2"><s:text name="ResourceLevel"></s:text>：</span>
													</td>
													<td width="5">
														&nbsp;
													</td>
													<td height="30">
														<select name="resoLevel" id="resoLevel">
															<c:choose>
																<c:when test="${info.resoLevel eq '2'}">
																	<option value="2" selected="selected">
																		<s:text name="SecondMenu"></s:text>
																	</option>
																	<option value="3">
																		<s:text name="ThirdMenu"></s:text>
																	</option>
																	<option value="4">
																		<s:text name="ForthMenu"></s:text>
																	</option>
																</c:when>
																<c:when test="${info.resoLevel eq '3'}">
																	<option value="2">
																		<s:text name="SecondMenu"></s:text>
																	</option>
																	<option value="3" selected="selected">
																		<s:text name="ThirdMenu"></s:text>
																	</option>
																	<option value="4">
																		<s:text name="ForthMenu"></s:text>
																	</option>
																</c:when>
																<c:when test="${info.resoLevel eq '4'}">
																	<option value="2">
																		<s:text name="SecondMenu"></s:text>
																	</option>
																	<option value="3">
																		<s:text name="ThirdMenu"></s:text>
																	</option>
																	<option value="4" selected="selected">
																		<s:text name="ForthMenu"></s:text>
																	</option>
																</c:when>
																<c:otherwise>
																	<option value="2">
																		<s:text name="SecondMenu"></s:text>
																	</option>
																	<option value="3">
																		<s:text name="ThirdMenu"></s:text>
																	</option>
																	<option value="4">
																		<s:text name="ForthMenu"></s:text>
																	</option>
																</c:otherwise>
															</c:choose>
														</select>
													</td>
												</tr>
												<tr>
													<td height="30" align="right" class="left_txt2">
														<s:text name="ResourceSequence"></s:text>
														：
													</td>
													<td width="5">
														&nbsp;
													</td>
													<td height="30">
														<select name="resoNo" id="resoNo">
															<c:choose>
																<c:when test="${info.resoNo eq '1'}">
																	<option value="1" selected="selected">
																		<s:text name="One"></s:text>
																	</option>
																	<option value="2">
																		<s:text name="Two"></s:text>
																	</option>
																	<option value="3">
																		<s:text name="Three"></s:text>
																	</option>
																	<option value="4">
																		<s:text name="Four"></s:text>
																	</option>
																	<option value="5">
																		<s:text name="Five"></s:text>
																	</option>
																	<option value="6">
																		<s:text name="Six"></s:text>
																	</option>
																	<option value="7">
																		<s:text name="Seven"></s:text>
																	</option>
																	<option value="8">
																		<s:text name="Eight"></s:text>
																	</option>
																	<option value="9">
																		<s:text name="Nine"></s:text>
																	</option>
																	<option value="10">
																		<s:text name="Ten"></s:text>
																	</option>
																	<option value="11">
																		<s:text name="Eleven"></s:text>
																	</option>
																	<option value="12">
																		<s:text name="Twelve"></s:text>
																	</option>
																</c:when>
																<c:when test="${info.resoNo eq '2'}">
																	<option value="1">
																		<s:text name="One"></s:text>
																	</option>
																	<option value="2" selected="selected">
																		<s:text name="Two"></s:text>
																	</option>
																	<option value="3">
																		<s:text name="Three"></s:text>
																	</option>
																	<option value="4">
																		<s:text name="Four"></s:text>
																	</option>
																	<option value="5">
																		<s:text name="Five"></s:text>
																	</option>
																	<option value="6">
																		<s:text name="Six"></s:text>
																	</option>
																	<option value="7">
																		<s:text name="Seven"></s:text>
																	</option>
																	<option value="8">
																		<s:text name="Eight"></s:text>
																	</option>
																	<option value="9">
																		<s:text name="Nine"></s:text>
																	</option>
																	<option value="10">
																		<s:text name="Ten"></s:text>
																	</option>
																	<option value="11">
																		<s:text name="Eleven"></s:text>
																	</option>
																	<option value="12">
																		<s:text name="Twelve"></s:text>
																	</option>
																</c:when>
																<c:when test="${info.resoNo eq '3'}">
																	<option value="1">
																		<s:text name="One"></s:text>
																	</option>
																	<option value="2">
																		<s:text name="Two"></s:text>
																	</option>
																	<option value="3" selected="selected">
																		<s:text name="Three"></s:text>
																	</option>
																	<option value="4">
																		<s:text name="Four"></s:text>
																	</option>
																	<option value="5">
																		<s:text name="Five"></s:text>
																	</option>
																	<option value="6">
																		<s:text name="Six"></s:text>
																	</option>
																	<option value="7">
																		<s:text name="Seven"></s:text>
																	</option>
																	<option value="8">
																		<s:text name="Eight"></s:text>
																	</option>
																	<option value="9">
																		<s:text name="Nine"></s:text>
																	</option>
																	<option value="10">
																		<s:text name="Ten"></s:text>
																	</option>
																	<option value="11">
																		<s:text name="Eleven"></s:text>
																	</option>
																	<option value="12">
																		<s:text name="Twelve"></s:text>
																	</option>
																</c:when>
																<c:when test="${info.resoNo eq '4'}">
																	<option value="1">
																		<s:text name="One"></s:text>
																	</option>
																	<option value="2">
																		<s:text name="Two"></s:text>
																	</option>
																	<option value="3">
																		<s:text name="Three"></s:text>
																	</option>
																	<option value="4" selected="selected">
																		<s:text name="Four"></s:text>
																	</option>
																	<option value="5">
																		<s:text name="Five"></s:text>
																	</option>
																	<option value="6">
																		<s:text name="Six"></s:text>
																	</option>
																	<option value="7">
																		<s:text name="Seven"></s:text>
																	</option>
																	<option value="8">
																		<s:text name="Eight"></s:text>
																	</option>
																	<option value="9">
																		<s:text name="Nine"></s:text>
																	</option>
																	<option value="10">
																		<s:text name="Ten"></s:text>
																	</option>
																	<option value="11">
																		<s:text name="Eleven"></s:text>
																	</option>
																	<option value="12">
																		<s:text name="Twelve"></s:text>
																	</option>
																</c:when>
																<c:when test="${info.resoNo eq '5'}">
																	<option value="1">
																		<s:text name="One"></s:text>
																	</option>
																	<option value="2">
																		<s:text name="Two"></s:text>
																	</option>
																	<option value="3">
																		<s:text name="Three"></s:text>
																	</option>
																	<option value="4">
																		<s:text name="Four"></s:text>
																	</option>
																	<option value="5" selected="selected">
																		<s:text name="Five"></s:text>
																	</option>
																	<option value="6">
																		<s:text name="Six"></s:text>
																	</option>
																	<option value="7">
																		<s:text name="Seven"></s:text>
																	</option>
																	<option value="8">
																		<s:text name="Eight"></s:text>
																	</option>
																	<option value="9">
																		<s:text name="Nine"></s:text>
																	</option>
																	<option value="10">
																		<s:text name="Ten"></s:text>
																	</option>
																	<option value="11">
																		<s:text name="Eleven"></s:text>
																	</option>
																	<option value="12">
																		<s:text name="Twelve"></s:text>
																	</option>
																</c:when>
																<c:when test="${info.resoNo eq '6'}">
																	<option value="1">
																		<s:text name="One"></s:text>
																	</option>
																	<option value="2">
																		<s:text name="Two"></s:text>
																	</option>
																	<option value="3">
																		<s:text name="Three"></s:text>
																	</option>
																	<option value="4">
																		<s:text name="Four"></s:text>
																	</option>
																	<option value="5">
																		<s:text name="Five"></s:text>
																	</option>
																	<option value="6" selected="selected">
																		<s:text name="Six"></s:text>
																	</option>
																	<option value="7">
																		<s:text name="Seven"></s:text>
																	</option>
																	<option value="8">
																		<s:text name="Eight"></s:text>
																	</option>
																	<option value="9">
																		<s:text name="Nine"></s:text>
																	</option>
																	<option value="10">
																		<s:text name="Ten"></s:text>
																	</option>
																	<option value="11">
																		<s:text name="Eleven"></s:text>
																	</option>
																	<option value="12">
																		<s:text name="Twelve"></s:text>
																	</option>
																</c:when>
																<c:when test="${info.resoNo eq '7'}">
																	<option value="1">
																		<s:text name="One"></s:text>
																	</option>
																	<option value="2">
																		<s:text name="Two"></s:text>
																	</option>
																	<option value="3">
																		<s:text name="Three"></s:text>
																	</option>
																	<option value="4">
																		<s:text name="Four"></s:text>
																	</option>
																	<option value="5">
																		<s:text name="Five"></s:text>
																	</option>
																	<option value="6">
																		<s:text name="Six"></s:text>
																	</option>
																	<option value="7" selected="selected">
																		<s:text name="Seven"></s:text>
																	</option>
																	<option value="8">
																		<s:text name="Eight"></s:text>
																	</option>
																	<option value="9">
																		<s:text name="Nine"></s:text>
																	</option>
																	<option value="10">
																		<s:text name="Ten"></s:text>
																	</option>
																	<option value="11">
																		<s:text name="Eleven"></s:text>
																	</option>
																	<option value="12">
																		<s:text name="Twelve"></s:text>
																	</option>
																</c:when>
																<c:when test="${info.resoNo eq '8'}">
																	<option value="1">
																		<s:text name="One"></s:text>
																	</option>
																	<option value="2">
																		<s:text name="Two"></s:text>
																	</option>
																	<option value="3">
																		<s:text name="Three"></s:text>
																	</option>
																	<option value="4">
																		<s:text name="Four"></s:text>
																	</option>
																	<option value="5">
																		<s:text name="Five"></s:text>
																	</option>
																	<option value="6">
																		<s:text name="Six"></s:text>
																	</option>
																	<option value="7">
																		<s:text name="Seven"></s:text>
																	</option>
																	<option value="8" selected="selected">
																		<s:text name="Eight"></s:text>
																	</option>
																	<option value="9">
																		<s:text name="Nine"></s:text>
																	</option>
																	<option value="10">
																		<s:text name="Ten"></s:text>
																	</option>
																	<option value="11">
																		<s:text name="Eleven"></s:text>
																	</option>
																	<option value="12">
																		<s:text name="Twelve"></s:text>
																	</option>
																</c:when>
																<c:when test="${info.resoNo eq '9'}">
																	<option value="1">
																		<s:text name="One"></s:text>
																	</option>
																	<option value="2">
																		<s:text name="Two"></s:text>
																	</option>
																	<option value="3">
																		<s:text name="Three"></s:text>
																	</option>
																	<option value="4">
																		<s:text name="Four"></s:text>
																	</option>
																	<option value="5">
																		<s:text name="Five"></s:text>
																	</option>
																	<option value="6">
																		<s:text name="Six"></s:text>
																	</option>
																	<option value="7">
																		<s:text name="Seven"></s:text>
																	</option>
																	<option value="8">
																		<s:text name="Eight"></s:text>
																	</option>
																	<option value="9" selected="selected">
																		<s:text name="Nine"></s:text>
																	</option>
																	<option value="10">
																		<s:text name="Ten"></s:text>
																	</option>
																	<option value="11">
																		<s:text name="Eleven"></s:text>
																	</option>
																	<option value="12">
																		<s:text name="Twelve"></s:text>
																	</option>
																</c:when>
																<c:when test="${info.resoNo eq '10'}">
																	<option value="1">
																		<s:text name="One"></s:text>
																	</option>
																	<option value="2">
																		<s:text name="Two"></s:text>
																	</option>
																	<option value="3">
																		<s:text name="Three"></s:text>
																	</option>
																	<option value="4">
																		<s:text name="Four"></s:text>
																	</option>
																	<option value="5">
																		<s:text name="Five"></s:text>
																	</option>
																	<option value="6">
																		<s:text name="Six"></s:text>
																	</option>
																	<option value="7">
																		<s:text name="Seven"></s:text>
																	</option>
																	<option value="8">
																		<s:text name="Eight"></s:text>
																	</option>
																	<option value="9">
																		<s:text name="Nine"></s:text>
																	</option>
																	<option value="10" selected="selected">
																		<s:text name="Ten"></s:text>
																	</option>
																	<option value="11">
																		<s:text name="Eleven"></s:text>
																	</option>
																	<option value="12">
																		<s:text name="Twelve"></s:text>
																	</option>
																</c:when>
																<c:when test="${info.resoNo eq '11'}">
																	<option value="1">
																		<s:text name="One"></s:text>
																	</option>
																	<option value="2">
																		<s:text name="Two"></s:text>
																	</option>
																	<option value="3">
																		<s:text name="Three"></s:text>
																	</option>
																	<option value="4">
																		<s:text name="Four"></s:text>
																	</option>
																	<option value="5">
																		<s:text name="Five"></s:text>
																	</option>
																	<option value="6">
																		<s:text name="Six"></s:text>
																	</option>
																	<option value="7">
																		<s:text name="Seven"></s:text>
																	</option>
																	<option value="8">
																		<s:text name="Eight"></s:text>
																	</option>
																	<option value="9">
																		<s:text name="Nine"></s:text>
																	</option>
																	<option value="10">
																		<s:text name="Ten"></s:text>
																	</option>
																	<option value="11" selected="selected">
																		<s:text name="Eleven"></s:text>
																	</option>
																	<option value="12">
																		<s:text name="Twelve"></s:text>
																	</option>
																</c:when>
																<c:when test="${info.resoNo eq '12'}">
																	<option value="1">
																		<s:text name="One"></s:text>
																	</option>
																	<option value="2">
																		<s:text name="Two"></s:text>
																	</option>
																	<option value="3">
																		<s:text name="Three"></s:text>
																	</option>
																	<option value="4">
																		<s:text name="Four"></s:text>
																	</option>
																	<option value="5">
																		<s:text name="Five"></s:text>
																	</option>
																	<option value="6">
																		<s:text name="Six"></s:text>
																	</option>
																	<option value="7">
																		<s:text name="Seven"></s:text>
																	</option>
																	<option value="8">
																		<s:text name="Eight"></s:text>
																	</option>
																	<option value="9">
																		<s:text name="Nine"></s:text>
																	</option>
																	<option value="10">
																		<s:text name="Ten"></s:text>
																	</option>
																	<option value="11">
																		<s:text name="Eleven"></s:text>
																	</option>
																	<option value="12" selected="selected">
																		<s:text name="Twelve"></s:text>
																	</option>
																</c:when>
																<c:otherwise>
																	<option value="1">
																		<s:text name="One"></s:text>
																	</option>
																	<option value="2">
																		<s:text name="Two"></s:text>
																	</option>
																	<option value="3">
																		<s:text name="Three"></s:text>
																	</option>
																	<option value="4">
																		<s:text name="Four"></s:text>
																	</option>
																	<option value="5">
																		<s:text name="Five"></s:text>
																	</option>
																	<option value="6">
																		<s:text name="Six"></s:text>
																	</option>
																	<option value="7">
																		<s:text name="Seven"></s:text>
																	</option>
																	<option value="8">
																		<s:text name="Eight"></s:text>
																	</option>
																	<option value="9">
																		<s:text name="Nine"></s:text>
																	</option>
																	<option value="10">
																		<s:text name="Ten"></s:text>
																	</option>
																	<option value="11">
																		<s:text name="Eleven"></s:text>
																	</option>
																	<option value="12">
																		<s:text name="Twelve"></s:text>
																	</option>
																</c:otherwise>
															</c:choose>
														</select>
													</td>
													<td width="100" align="right">
														<span class="left_txt2"><s:text name="StyleCode"></s:text> </span>
													</td>
													<td width="5">
														&nbsp;
													</td>
													<td height="30">
														<input name="resoClass" id="resoClass" type="text" value="${info.resoClass}" />
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
													<td height="30" colspan="2" align="center">
														<span class="left_txt2"> <a href="#"> <input name="input" type="submit" class="btn_w102_blue"
																	value="<s:text name="Save"></s:text>" />&nbsp;&nbsp;</a> <input name="input" type="button" class="btn_w102_blue"
																value="<s:text name="Return"></s:text>" onclick="history.go(-1);" /> </span>
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
