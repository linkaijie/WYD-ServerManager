<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<link href="${ctx}/styles/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/scripts/table.js"></script>
<script type="text/javascript">
</script>
<div class="div_content">
	<c:choose>
		<c:when test="${IS_ADMIN eq true}">
			<table class="bought-table">
				<thead>
					<tr class="toolbar skin-gray">
						<td colspan="7">
							<a href="${ctx}/shopItems/form.action" class=" toolbtn">
								新増
							</a>
						</td>
					</tr>
				</thead>
			</table>
		</c:when>
	</c:choose>
	<table class="bought-table">
		<thead>
			<tr class="toolbar skin-gray">
				<td colspan="7">
					<lable><input name = "acc" type="checkbox" class="all-selector" id="J_AllSelector" onclick="comBoxChange(this);"/>全选</lable>
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
							<th>名称</th>
							<th>adminport</th>
							<th>端口</th>
							<th>PublicIP</th>
							<th>HttpIp</th>
							<th>HttpPort</th>
							<th>分区ID</th>
							<th>机器码</th>
							<th>更新时间</th>
							<th>状态</th>
							<th>是否已部署</th>
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
								<!--<td name = "accList" align="center">${data.id}</td>-->
								<td name = "accList" align="center">${data.name}</td>
								<td name = "accList" align="center">${data.adminport}</td>
								<td name = "accList" align="center">${data.port}</td>
								<td name = "accList" align="center">${data.publicip}</td>
								<td name = "accList" align="center">${data.httpip}</td>
								<td name = "accList" align="center">${data.httpport}</td>
								<!--<td name = "accList" align="center">${data.authport}</td>
								<td name = "accList" align="center">${data.authip}</td>-->
								<td name = "accList" align="center">${data.areaid}</td>
								<td name = "accList" align="center">${data.machinecode}</td>
								<td name = "accList" align="center">
								    <c:if test="${data.updateTime ge updateDate}" var="rs"> 
								        <font style="color:green">${data.updateTime}</font>
								    </c:if>
								    <c:if test="${data.updateTime lt updateDate}" var="rs"> 
                                        ${data.updateTime}
                                    </c:if>
								</td>
								<c:choose>
									<c:when test="${data.state == 1}">
										<td name = "accList" style = "color:green" align="center">
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
										<td name = "accList" style = "color:green" align="center">
											已部署
										</td>
									</c:when>
									<c:otherwise>
										<td name = "accList" align="center">
											未部署
										</td>
									</c:otherwise>
								</c:choose>
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
