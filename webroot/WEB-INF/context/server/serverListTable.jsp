<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<link href="${ctx}/styles/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/scripts/table.js"></script>
<script src="${ctx}/scripts/player/player.js" type=text/javascript></script>
<script type="text/javascript" src="${ctx}/scripts/server/server.js"></script>
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
	<table class="bought-table">
        <thead>
            <tr class="toolbar skin-gray" id="skingray">
                <td colspan="7">
                    <a name = "acc" href="javascript:batchUpdateLib();" class="toolbtn">更新lib</a>
                    &nbsp;
                    <a name = "acc" href="javascript:batchUpdateServerLib();" class="toolbtn2">更新serverLib</a>
                    &nbsp;
                    <a name = "acc" href="javascript:commandInput();" class="toolbtn">命令行</a>
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
						    <th width=48>全选<input class="all-selector" type="checkbox" id="c" onclick="comBoxChange(this);"/></th>
							<th>编号</th>
							<th>名称</th>
							<th>serverIp</th>
							<th>serverPort</th>
							<th>sshName</th>
							<th>使用类型</th>
							<th>授权时间</th>
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
                                    <input type="checkbox" class="all-selector" name="table_id" value="${data.id}" />
                                </td>
								<td name = "accList" align="center">${data.id}</td>
								<td name = "accList" align="center">${data.serverName}</td>
								<td name = "accList" align="center">${data.serverIp}</td>
								<td name = "accList" align="center">${data.serverPort}</td>
								<td name = "accList" align="center">${data.sshName}</td>
								<c:choose>
                                    <c:when test="${data.useType == 1}">
                                        <td name = "accList" align="center">
                                                                                                                            游戏服务器
                                        </td>
                                    </c:when>
                                    <c:when test="${data.useType == 2}">
                                        <td name = "accList" align="center">
                                                                                                                            缓存服务器
                                        </td>
                                    </c:when>
                                    <c:when test="${data.useType == 3}">
                                        <td name = "accList" align="center">
                                                                                                                            数据库服务器
                                        </td>
                                    </c:when>
                                </c:choose>
								<td name = "accList" align="center">${data.rsaTime}</td>
								<td name = "accList" align="center">
									<a name = "acc" href="javascript:input(${data.id});">编辑</a> -- 
							        <a name = "acc" href="javascript:copySshRsa(${data.id});">授权</a> -- 
									<a name = "acc" href="javascript:deletes(${data.id});">删除</a>
								</td>
							</tr>
						</c:forEach>
						</c:if>
					</tbody>
					<tfoot>
						<tr class="sep-row">
							<td colspan="5"></td>
						</tr>
						<tr class="pagebar">
							<td colspan="5">
								<table width="100%" border="0" >
									<tr>
										<c:if test="${pager ne null}">
											<%@ include file="/WEB-INF/context/commons/pager.jsp"%>
										</c:if>
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
