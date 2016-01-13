<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<link href="${ctx}/styles/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/scripts/table.js"></script>
<div class="div_content">
	<table class="bought-table" id="boughttable">
		<thead>
			<tr class="toolbar skin-gray">
				<td colspan="6">
					<a href="javascript:batchStart('${pager.pageNumber}','${pager.pageSize}');" class="toolbtn" id="batchStart">批量启动</a>
					<a href="javascript:batchStop('${pager.pageNumber}','${pager.pageSize}');" class="toolbtn" id="batchStop">批量关闭</a>
					<a href="javascript:synchroniz();" class="toolbtn" id="synchroniz">同步缓存</a>
					<a href="javascript:opeartMinotor(0);" class="toolbtn" id="opeartMinotor0">关闭监控</a>
					<a href="javascript:opeartMinotor(1);" class="toolbtn" id="opeartMinotor1">开启监控</a>
					<a href="javascript:updateRestart();" class="toolbtn" id="updateRestart">更新重启</a>
				</td>
			</tr>
		</thead>
	</table>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
				<table class="bought-table">
					<thead>
						<tr style="color:#5555FF; background: #DDDDDD;">
							<td width=48>全选<input class="all-selector" type="checkbox" id="c" onclick="comBoxChange(this);"/></td>
							<td align="center"><b>编号</b></td>
							<td align="center"><b>ACCOUNT</b></td>
							<!-- 
							<td align="center">IPD</td> -->
							<td align="center"><b>WORLD</b></td>
							<td align="center" colspan="5"><b>DISPATCH</b></td>
							<td align="center"><b>总操作</b></td>
						</tr>
					</thead>
					<tbody class=" success-order">
					<c:if test="${pager ne null}">
						<c:forEach items="${pager.list}" var="data" varStatus="status">
							<tr <c:choose>
							<c:when test="${status.index % 2 == 1}">class="odd"</c:when>
							<c:otherwise>class="even"</c:otherwise>
						</c:choose>>
								<td style="text-align: center" width="30" rowspan="2">
									<input type="checkbox" class="all-selector" name="table_id"
										value="${data.worldId}" />
								</td>
								<td name = "accList" align="center" rowspan="2">${data.areaId}</td>
								<td name = "accList" align="center">
									<c:choose>
										<c:when test="${data.accountState eq 1}">
											<a href="${ctx}/accountserver/accountServerList.action?accountId=${data.accountId}">${data.accountName}</a>
										</c:when>
										<c:when test="${data.accountState eq 0}">
											<a href="${ctx}/accountserver/accountServerList.action?accountId=${data.accountId}">${data.accountName}</a>
										</c:when>
									</c:choose>
									<c:choose>
									<c:when test="${data.accountState eq 1}">
										<img title="启动" src="${ctx}/images/status/green.gif" />
									</c:when>
									<c:when test="${data.accountState eq 0}">
										<img title="关闭" src="${ctx}/images/status/gay.gif" />
									</c:when>
								</c:choose>
								</td>
								 <!-- 
								<td name = "accList" align="center">
									<c:choose>
										<c:when test="${data.ipdState eq 1}">
											<a href="${ctx}/ipdserver/ipdServerList.action?ipdId=${data.ipdId}">${data.ipdName}</a>
										</c:when>
										<c:when test="${data.ipdState eq 0}">
											<a href="${ctx}/ipdserver/ipdServerList.action?ipdId=${data.ipdId}">${data.ipdName}</a>
										</c:when>
									</c:choose>
								</td>
								-->
								<td name = "accList" align="center">
									<c:choose>
										<c:when test="${data.worldState eq 1}">
											<a href="${ctx}/worldserver/worldServerList.action?worldId=${data.worldId}">${data.worldName}</a>
										</c:when>
										<c:when test="${data.worldState eq 0}">
											<a href="${ctx}/worldserver/worldServerList.action?worldId=${data.worldId}">${data.worldName}</a>
										</c:when>
									</c:choose>
									<c:choose>
										<c:when test="${data.worldState eq 1}">
											<img title="启动" src="${ctx}/images/status/green.gif" />
										</c:when>
										<c:when test="${data.worldState eq 0}">
											<img title="关闭" src="${ctx}/images/status/gay.gif" />
										</c:when>
									</c:choose>
								</td>
								<td name = "accList" align="center">
									<c:choose>
										<c:when test="${data.dispatchOneState eq 1}">
											<a href="${ctx}/dispatchserver/dispatchServerList.action?dispatchId=${data.dispatchOneId}">${data.dispatchOneName}</a>
										</c:when>
										<c:when test="${data.dispatchOneState eq 0}">
											<a href="${ctx}/dispatchserver/dispatchServerList.action?dispatchId=${data.dispatchOneId}">${data.dispatchOneName}</a>
										</c:when>
									</c:choose>
									<c:choose>
										<c:when test="${data.dispatchOneState eq 1}">
											<img title="启动" src="${ctx}/images/status/green.gif" />
										</c:when>
										<c:when test="${data.dispatchOneState eq 0}">
											<img title="关闭" src="${ctx}/images/status/gay.gif" />
										</c:when>
									</c:choose>
								</td>
								<td name = "accList" align="center">
									<c:choose>
										<c:when test="${data.dispatchTwoState eq 1}">
											<a href="${ctx}/dispatchserver/dispatchServerList.action?dispatchId=${data.dispatchTwoId}">${data.dispatchTwoName}</a>
										</c:when>
										<c:when test="${data.dispatchTwoState eq 0}">
											<a href="${ctx}/dispatchserver/dispatchServerList.action?dispatchId=${data.dispatchTwoId}">${data.dispatchTwoName}</a>
										</c:when>
									</c:choose>
									<c:choose>
										<c:when test="${data.dispatchTwoState eq 1}">
											<img title="启动" src="${ctx}/images/status/green.gif" />
										</c:when>
										<c:when test="${data.dispatchTwoState eq 0}">
											<img title="关闭" src="${ctx}/images/status/gay.gif" />
										</c:when>
									</c:choose>
								</td>
								<td name = "accList" align="center">
									<c:if test="${data.dispatchThreeId > 0}">
										<c:if test="${data.dispatchThreeId > 0}">
											<c:choose>
												<c:when test="${data.dispatchThreeState eq 1}">
													<a href="${ctx}/dispatchserver/dispatchServerList.action?dispatchId=${data.dispatchThreeId}">${data.dispatchThreeName}</a>
												</c:when>
												<c:when test="${data.dispatchThreeState eq 0}">
													<a href="${ctx}/dispatchserver/dispatchServerList.action?dispatchId=${data.dispatchThreeId}">${data.dispatchThreeName}</a>
												</c:when>
											</c:choose>
											<c:choose>
											<c:when test="${data.dispatchThreeState eq 1}">
												<img title="启动" src="${ctx}/images/status/green.gif" />
											</c:when>
											<c:when test="${data.dispatchThreeState eq 0}">
												<img title="关闭" src="${ctx}/images/status/gay.gif" />
											</c:when>
										</c:choose>
										</c:if>
									</c:if>
								</td>
								<td name = "accList" align="center">
									<c:if test="${data.dispatchFourId > 0}">
										<c:if test="${data.dispatchFourId > 0}">
											<c:choose>
												<c:when test="${data.dispatchFourState eq 1}">
													<a href="${ctx}/dispatchserver/dispatchServerList.action?dispatchId=${data.dispatchFourId}">${data.dispatchFourName}</a>
												</c:when>
												<c:when test="${data.dispatchFourState eq 0}">
													<a href="${ctx}/dispatchserver/dispatchServerList.action?dispatchId=${data.dispatchFourId}">${data.dispatchFourName}</a>
												</c:when>
											</c:choose>
											<c:choose>
											<c:when test="${data.dispatchFourState eq 1}">
												<img title="启动" src="${ctx}/images/status/green.gif" />
											</c:when>
											<c:when test="${data.dispatchFourState eq 0}">
												<img title="关闭" src="${ctx}/images/status/gay.gif" />
											</c:when>
										</c:choose>
										</c:if>
									</c:if>
								</td>
								<td name = "accList" align="center">
									<c:if test="${data.dispatchFiveId > 0}">
										<c:if test="${data.dispatchFiveId > 0}">
											<c:choose>
												<c:when test="${data.dispatchFiveState eq 1}">
													<a href="${ctx}/dispatchserver/dispatchServerList.action?dispatchId=${data.dispatchFiveId}">${data.dispatchFiveName}</a>
												</c:when>
												<c:when test="${data.dispatchFiveState eq 0}">
													<a href="${ctx}/dispatchserver/dispatchServerList.action?dispatchId=${data.dispatchFiveId}">${data.dispatchFiveName}</a>
												</c:when>
											</c:choose>
											<c:choose>
											<c:when test="${data.dispatchFiveState eq 1}">
												<img title="启动" src="${ctx}/images/status/green.gif" />
											</c:when>
											<c:when test="${data.dispatchFiveState eq 0}">
												<img title="关闭" src="${ctx}/images/status/gay.gif" />
											</c:when>
										</c:choose>
										</c:if>
									</c:if>
								</td>
								<td name = "accList" align="center" rowspan="2">
									<c:choose>
										<c:when test="${data.worldState eq 1}">
											启动 |&nbsp; <input type="button" value="关闭" onclick="stopServer('${data.worldId}','${pager.pageNumber}','${pager.pageSize}');"/>
										</c:when>
										<c:when test="${data.worldState eq 0}">
											<input type="button" value="启动" onclick="startServer('${data.worldId}','${pager.pageNumber}','${pager.pageSize}');"/> &nbsp;| 关闭
										</c:when>
									</c:choose>
								</td>
							</tr>
						<!-- 
						<tr <c:choose>
						<c:when test="${status.index % 2 == 1}">class="odd"</c:when>
						<c:otherwise>class="even"</c:otherwise>
						</c:choose>>
								<td name = "accList" align="center">
								<c:choose>
									<c:when test="${data.accountState eq 1}">
										<img title="启动" src="${ctx}/images/status/green.gif" />
									</c:when>
									<c:when test="${data.accountState eq 0}">
										<img title="关闭" src="${ctx}/images/status/gay.gif" />
									</c:when>
								</c:choose>
								</td>
								<td name = "accList" align="center">
									<c:choose>
										<c:when test="${data.ipdState eq 1}">
											<img title="启动" src="${ctx}/images/status/green.gif" />
										</c:when>
										<c:when test="${data.ipdState eq 0}">
											<img title="关闭" src="${ctx}/images/status/gay.gif" />
										</c:when>
									</c:choose>
								</td>
								<td name = "accList" align="center">
									<c:choose>
										<c:when test="${data.worldState eq 1}">
											<img title="启动" src="${ctx}/images/status/green.gif" />
										</c:when>
										<c:when test="${data.worldState eq 0}">
											<img title="关闭" src="${ctx}/images/status/gay.gif" />
										</c:when>
									</c:choose>
								</td>
								<td name = "accList" align="center">
									<c:choose>
										<c:when test="${data.dispatchOneState eq 1}">
											<img title="启动" src="${ctx}/images/status/green.gif" />
										</c:when>
										<c:when test="${data.dispatchOneState eq 0}">
											<img title="关闭" src="${ctx}/images/status/gay.gif" />
										</c:when>
									</c:choose>
								</td>
								<td name = "accList" align="center">
									<c:choose>
										<c:when test="${data.dispatchTwoState eq 1}">
											<img title="启动" src="${ctx}/images/status/green.gif" />
										</c:when>
										<c:when test="${data.dispatchTwoState eq 0}">
											<img title="关闭" src="${ctx}/images/status/gay.gif" />
										</c:when>
									</c:choose></td>
								<td name = "accList" align="center">
									<c:if test="${data.dispatchThreeId > 0}">
										<c:choose>
											<c:when test="${data.dispatchThreeState eq 1}">
												<img title="启动" src="${ctx}/images/status/green.gif" />
											</c:when>
											<c:when test="${data.dispatchThreeState eq 0}">
												<img title="关闭" src="${ctx}/images/status/gay.gif" />
											</c:when>
										</c:choose>
									</c:if>
								</td>
								<td name = "accList" align="center">
									<c:if test="${data.dispatchFourId > 0}">
										<c:choose>
											<c:when test="${data.dispatchFourState eq 1}">
												<img title="启动" src="${ctx}/images/status/green.gif" />
											</c:when>
											<c:when test="${data.dispatchFourState eq 0}">
												<img title="关闭" src="${ctx}/images/status/gay.gif" />
											</c:when>
										</c:choose>
									</c:if>
								</td>
								<td name = "accList" align="center">
									<c:if test="${data.dispatchFiveId > 0}">
										<c:choose>
											<c:when test="${data.dispatchFiveState eq 1}">
												<img title="启动" src="${ctx}/images/status/green.gif" />
											</c:when>
											<c:when test="${data.dispatchFiveState eq 0}">
												<img title="关闭" src="${ctx}/images/status/gay.gif" />
											</c:when>
										</c:choose>
									</c:if>
								</td>
						</tr>
						 -->
						<tr <c:choose>
						<c:when test="${status.index % 2 == 1}">class="odd"</c:when>
						<c:otherwise>class="even"</c:otherwise>
						</c:choose>>
								<td name = "accList" align="center">
									<c:choose>
										<c:when test="${data.accountState eq 1}">
											启动 &nbsp; <input type="button" value="关闭" onclick="stopAccount('${data.accountId}');"/>
										</c:when>
										<c:when test="${data.accountState eq 0}">
											<input type="button" value="启动" onclick="startAccount('${data.accountId}');"/> &nbsp; 关闭
										</c:when>
									</c:choose>
								</td>
								 <!-- 
									<td name = "accList" align="center">
										<c:choose>
											<c:when test="${data.ipdState eq 1}">
												start &nbsp; <input type="button" value="stop" onclick="stopIpd('${data.ipdId}');"/>
											</c:when>
											<c:when test="${data.ipdState eq 0}">
												<input type="button" value="start" onclick="startIpd('${data.ipdId}');"/> &nbsp; stop
											</c:when>
										</c:choose>
									</td>
								 -->
								<td name = "accList" align="center">
									<c:choose>
										<c:when test="${data.worldState eq 1}">
											启动 &nbsp; <input type="button" value="关闭" onclick="stopWorld('${data.worldId}');"/>
										</c:when>
										<c:when test="${data.worldState eq 0}">
											<input type="button" value="启动" onclick="startWorld('${data.worldId}');"/> &nbsp; 关闭
										</c:when>
									</c:choose>
								</td>
								<td name = "accList" align="center">
									<c:choose>
										<c:when test="${data.dispatchOneState eq 1}">
											启动 &nbsp; <input type="button" value="关闭" onclick="stopDispatchOne('${data.dispatchOneId}');"/>
										</c:when>
										<c:when test="${data.dispatchOneState eq 0}">
											<input type="button" value="启动" onclick="startDispatchOne('${data.dispatchOneId}');"/> &nbsp; 关闭
										</c:when>
									</c:choose>
								</td>
								<td name = "accList" align="center">
									<c:choose>
										<c:when test="${data.dispatchTwoState eq 1}">
											启动 &nbsp; <input type="button" value="关闭" onclick="stopDispatchTwo('${data.dispatchTwoId}');"/>
										</c:when>
										<c:when test="${data.dispatchTwoState eq 0}">
											<input type="button" value="启动" onclick="startDispatchTwo('${data.dispatchTwoId}');"/> &nbsp; 关闭
										</c:when>
									</c:choose>
								</td>
								<td name = "accList" align="center">
									<c:if test="${data.dispatchThreeId > 0}">
										<c:choose>
											<c:when test="${data.dispatchThreeState eq 1}">
												启动 &nbsp; <input type="button" value="关闭" onclick="stopDispatchThree('${data.dispatchThreeId}');"/>
											</c:when>
											<c:when test="${data.dispatchThreeState eq 0}">
												<input type="button" value="启动" onclick="startDispatchThree('${data.dispatchThreeId}');"/> &nbsp; 关闭
											</c:when>
										</c:choose>
									</c:if>
								</td>
								<td name = "accList" align="center">
									<c:if test="${data.dispatchFourId > 0}">
										<c:choose>
											<c:when test="${data.dispatchFourState eq 1}">
												启动 &nbsp; <input type="button" value="关闭" onclick="stopDispatchThree('${data.dispatchFourId}');"/>
											</c:when>
											<c:when test="${data.dispatchFourState eq 0}">
												<input type="button" value="启动" onclick="startDispatchFour('${data.dispatchFourId}');"/> &nbsp; 关闭
											</c:when>
										</c:choose>
									</c:if>
								</td>
								<td name = "accList" align="center">
									<c:if test="${data.dispatchFiveId > 0}">
										<c:choose>
											<c:when test="${data.dispatchFiveState eq 1}">
												启动 &nbsp; <input type="button" value="关闭" onclick="stopDispatchThree('${data.dispatchFiveId}');"/>
											</c:when>
											<c:when test="${data.dispatchFiveState eq 0}">
												<input type="button" value="启动" onclick="startDispatchFive('${data.dispatchFiveId}');"/> &nbsp; 关闭
											</c:when>
										</c:choose>
									</c:if>
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
