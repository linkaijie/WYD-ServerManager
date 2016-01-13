<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<link href="${ctx}/styles/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/scripts/table.js"></script>
<script type="text/javascript" src="${ctx}/scripts/server/accountServer.js"></script>
<div class="div_content">
	<c:choose>
		<c:when test="${IS_ADMIN eq true}">
			<table class="bought-table">
				<thead>
					<tr class="toolbar skin-gray">
						<td colspan="7">
							<a href="${ctx}/accountserver/form.action" class=" toolbtn">
								新増
							</a>
						</td>
					</tr>
				</thead>
			</table>
		</c:when>
	</c:choose>
	<table class="bought-table" id="boughttable">
		<thead>
			<tr class="toolbar skin-gray">
				<td colspan="7">
					<input name = "acc" type="checkbox" class="all-selector" id="J_AllSelector" onclick="comBoxChange(this);"/>全选
					<a name = "acc" href="javascript:batchStart();" class="toolbtn">批量启动</a>
					<a name = "acc" href="javascript:batchStop();" class="toolbtn">批量关闭</a>
					<a name = "acc" href="javascript:synchronize();" class="toolbtn">同步配置</a>
					<a name = "acc" href="javascript:batchUpdate('${pager.pageNumber}','${pager.pageSize}');" class="toolbtn">更新服务</a>
					<a name = "acc" href="javascript:deploydiv();" class="toolbtn">部署服务</a>
				</td>
			</tr>
		</thead>
	</table>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
				<table class="bought-table">
					<thead>
						<tr>
							<th></th>
							<th>编号</th>
							<th>名称</th>
							<th>IP</th>
							<th>端口</th>
							<th>更新时间</th>
							<th>状态</th>
							<th>是否已部署</th>
							<th><a name = "acc" href="javascript:input();">添加</a></th>
						</tr>
					</thead>
					<tbody class=" success-order">
					<c:if test="${pager ne null}">
						<c:forEach items="${pager.list}" var="data" varStatus="status">
							<tr <c:choose>
							<c:when test="${status.index % 2 == 1}">class="odd"</c:when>
							<c:otherwise>class="even"</c:otherwise>
						</c:choose>>
								<td style="text-align: center" width="30">
									<input type="checkbox" class="all-selector" name="table_id"
										value="${data.id}" />
								</td>
								<td name = "accList" align="center">${data.id}</td>
								<td name = "accList" align="center">${data.name}</td>
								<td name = "accList" align="center">${data.serverip}</td>
								<td name = "accList" align="center">${data.port}</td>
								<td name = "accList" align="center">${data.updateTime}</td>
								<c:choose>
									<c:when test="${data.state == 1}">
										<td name = "accList" align="center" style = "color:green">
											已开启
										</td>
									</c:when>
									<c:otherwise>
										<td name = "accList" align="center">
											已关闭
										</td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${data.isDeploy == 1}">
										<td  name = "accList" align="center" style = "color:green">
											已部署
										</td>
									</c:when>
									<c:otherwise>
										<td name = "accList" align="center">
											未部署
										</td>
									</c:otherwise>
								</c:choose>
								<td name = "accList" align="center">
									<a name = "acc" href="javascript:start(${data.id});">开启</a> -- 
									<a name = "acc" href="javascript:stop(${data.id});">关闭</a> -- 
									<!--
									<a name = "acc" href="javascript:deploy(${data.id});">部署</a> --
									<a name = "acc" href="javascript:update(${data.id});">更新</a> --
							        -->
									<a name = "acc" href="javascript:input(${data.id});">编辑</a> -- 
									<a name = "acc" href="javascript:deletes(${data.id});">删除</a>
								</td>
							</tr>
						</c:forEach>
						</c:if>
					</tbody>
				</table>
				<%@ include file="/WEB-INF/context/commons/pager.jsp"%>
			</td>
		</tr>
	</table>
</div>
