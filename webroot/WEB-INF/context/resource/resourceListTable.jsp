<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<link href="${ctx}/styles/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/scripts/table.js"></script>
<link href="${ctx}/scripts/colorbox/colorbox.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="${ctx}/scripts/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript">
<!--

$(document).ready(function(){
	
	
	$(".iframe").colorbox({iframe:true, width:"35%", height:"60%"});
});
//-->
</script>
<div class="div_content">
	<table class="bought-table">
		<thead>
			<tr class="toolbar skin-gray">
				<td colspan="7">
					<label>
						<input type="checkbox" class="all-selector" id="J_AllSelector" onclick="comBoxChange(this);" />
						<s:text name="SelectAll"></s:text>
					</label>
					<a href="${ctx}/resource/form.action?treeId=${clickId}" class=" toolbtn"><s:text name="New_2"></s:text> </a>
					<a id="J_CombinPay2" href="javascript:dataDelete('${ctx}/resource/deleteResource.action');" class=" toolbtn"><s:text name="BatchDelete"></s:text>
					</a>
				</td>
			</tr>
		</thead>
	</table>
	<form action="${ctx}/resource/listPage.action" method="post">
		<input name="urls" id="urls" value="${ctx}/resource/listPage.action?ajax=true&treeId=${clickId}" type="hidden" />
		<input name="treeId" value="${clickId}" type="hidden" />
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr class="toolbar skin-gray">
				<td>
					<br />
				</td>
			</tr>
			<tr>
				<td>
					<table class="bought-table">
						<thead>
							<tr>
								<th style="text-align: center" width=30></th>
								<th width="55" align="center">
									<s:text name="ResourceSequence"></s:text>
								</th>
								<th width="55" align="center">
									<s:text name="ResourceLevel"></s:text>
								</th>
								<th width="100" align="center">
									<s:text name="ResourceName"></s:text>
								</th>
								<th width="100" align="center">
									<s:text name="ResourceCode"></s:text>
								</th>
								<th width="15%" align="center">
									<s:text name="ViewAllocation"></s:text>
								</th>
								<th width="20%" align="center">
									<s:text name="Operate"></s:text>
								</th>
							</tr>
						</thead>
						<c:forEach items="${page.list}" var="item">
							<tbody class="success-order">
								<tr>
									<td colspan="5" align="left" class="jiange"></td>
								</tr>
								<tr>
									<td rowspan="2">
										<input type="checkbox" class="all-selector" name="table_id" value="${item.id}" />
									</td>
									<td colspan="6" align="left" class="header_item">
										<span> URL: ${item.resoUrl} </span>
									</td>
								</tr>
								<tr>
									<td align="left">
										<div class="item_body">
											${item.resoNo}
										</div>
									</td>
									<td align="left">
										<div class="item_body">
											${item.resoLevel}
										</div>
									</td>
									<td align="center">
										${ item.resoName}
									</td>
									<td align="center">
										${item.resoCode}
									</td>
									<td align="center">
										<a class="iframe" href="${ctx}/jsp/admin/resource/showRoleResourceTree.jsp?id=${item.id}"><s:text name="ViewAllocation"></s:text> </a>
									</td>
									<td rowspan="1" align="center">
										<a href="${ctx}/resource/form.action?treeId=${clickId}"><s:text name="New_2"></s:text> </a>
										<a href="${ctx}/resource/form.action?id=${item.id}&treeId=${clickId}"><s:text name="Edit"></s:text> </a>
										<a href="${ctx}/resource/deleteResource.action?ids=${item.id}"><s:text name="Delete"></s:text> </a>
									</td>
								</tr>
							</tbody>
						</c:forEach>
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
