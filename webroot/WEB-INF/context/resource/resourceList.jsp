<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><s:text name="CompanyName"></s:text>
		</title>
		<%@ include file="/commons/meta.jsp"%>
		<link href="${ctx}/styles/css.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="${ctx}/scripts/zTree/zTreeStyle/zTreeStyle.css" type="text/css" />
		<script type="text/javascript" src="${ctx}/scripts/zTree/jquery.ztree.core-3.0.js"></script>
		<script type="text/javascript" src="${ctx}/scripts/zTree/jquery.ztree.excheck-3.0.js"></script>
		<script type="text/javascript" src="${ctx}/scripts/loadTree/resourceTree.js"></script>
		<link href="${ctx}/scripts/colorbox/colorbox.css" rel="stylesheet" type="text/css" />
		<script language="javascript" src="${ctx}/scripts/colorbox/jquery.colorbox.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				loadResourceTree();//加载tree
			});
		</script>
	</head>
	<body>
		<jsp:include flush="false" page="/admin_menu.jsp" />
		<div style="height: 10px;">
		</div>
		<div class="table_div">
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
										<s:text name="CurrentPosition_SystemManagement_ResourceManagement"></s:text>
									</div>
								</div>
								<!--               <div class="seach_div">

            <table width="100%" border="0" cellpadding="5" cellspacing="0" class="search_table">

      

              <tr>

                <td><a>新建</a>



  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>

                </tr>

            </table>

            </div>-->
								<div class="div_content">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="180" height="100%" valign="top" style="border: #dedee0 1px solid;">
												<div class="modlue_menu_div">
													<ul id="resourceTree" class="ztree"></ul>
												</div>
											</td>
											<td>
												<td width="100%" height="100%" valign="top">
													<div id="biaoge"></div>
												</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
				</tr>
			</table>
		</div>
		<div class="bottom" id="bottom">
			<jsp:include flush="false" page="/down.jsp" />
		</div>
	</body>
</html>
