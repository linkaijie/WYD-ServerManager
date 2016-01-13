<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/context/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/context/commons/head.jsp"%>
<script type="text/javascript" src="${ctx}/scripts/table.js"></script>
<script type="text/javascript" src="${ctx}/scripts/jquery-1.7.2.min.js"></script>
<title>更新服务管理</title>
<script type="text/javascript">
        myInterval = setInterval(function() {
        $.ajax({
            type : 'POST',
            url : "common/getBatchCloseWorldAndDispatchList.action",
            success : function(msg) {
            var dataObj=eval("("+msg+")"); 
            var worldList = dataObj.worldList;
            var dispatchList = dataObj.dispatchList;
            var batchDispatchNum = ${sessionScope.batchDispatchNum1}; //batchDispatchNum1代表关闭时dispatch的数量
            if(msg!=null && worldList!=null && worldList!='' && worldList.length>0)
            {
               for(var i=0;i<worldList.length;i++) 
               {
                   $("#wstatus_"+worldList[i].id).text(worldList[i].remark).css("color",worldList[i].color);
               }       
            }
            if(msg!=null && dispatchList!=null && dispatchList!='' && dispatchList.length>0)
            {
               for(var i=0;i<dispatchList.length;i++) 
               {
                   $("#dstatus_"+dispatchList[i].id).text(dispatchList[i].remark).css("color",dispatchList[i].color);
               }
            }
        }
        });
    }, 1500);

    var P = window.parent, E = P.loadinndlg();
    function closreload(pageNumber, pageSize)
    {
        E.doSearch();
    }
</script>
</head>
<body>
<div id="hld">
	<div class="row-fluid">
		<div class="block">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<%
		    String redirectType=request.getParameter("redirectType"); 
    		String pageNumber=request.getParameter("pageNumber"); 
    		String pageSize=request.getParameter("pageSize"); 
		%>
		<input type="hidden" name="redirectType" value="<%=redirectType%>" />
		<input type="hidden" name="pageNumber" value="<%=pageNumber%>"> 
        <input type="hidden" name="pageSize" value="<%=pageSize%>"> 
        <c:if test="${sessionScope.batchStartServerError ne null}">
            <font style="color:red">${sessionScope.batchCloseServerError}</font>
        </c:if>
            <td>
                <table class="bought-table" width="100%">
                    <thead>
                        <tr>
                            <th>分区ID</th>
                            <th>World名称</th>
                            <th>World状态</th>
                            <th>Dispatch名称</th>
                            <th>Dispatch状态</th>
                        </tr>
                    </thead>
                    <tbody class=" success-order">
                        <c:if test="${sessionScope.batchCloseServerError ne null}">
                        <script language='javascript'>
                                                              　　            window.location.reload(true);
                                                              　　 </script>
                        </c:if>
                        <c:forEach items="${sessionScope.batchCloseWorldAndDispatchMap}" var="batchMap" varStatus="status">
                            <tr <c:choose>
                            <c:when test="${status.index % 2 == 1}">class="odd"</c:when>
                            <c:otherwise>class="even"</c:otherwise>
                            </c:choose>>
                                <td id = "wid_${batchMap.key.id}" name = "accList" align="center">${batchMap.key.areaid}</td>
                                <td id = "wname_${batchMap.key.id}" name = "accList" align="center">${batchMap.key.name}</td>
                                <td id = "wstatus_${batchMap.key.id}" name = "accList" align="center"><forn style = "color:blue">关闭中</forn></td>
                                <td>   
                                    <table width="100%">
                                        <c:forEach items="${batchMap.value}" var="batchList">
                                            <tr>
                                                <td  id = "dname_${batchList.id}" name = "accList" align="center">${batchList.name}</td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </td>
                                <td>   
                                <table width="100%">
                                    <c:forEach items="${batchMap.value}" var="batchList">
                                        <tr>
                                            <td  id = "dstatus_${batchList.id}" name = "accList" align="center"><forn style = "color:blue">关闭中</forn></td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </td>  
                            </tr>
                        </c:forEach>
                        <tr>
                        <span align="center">
                            <td></td><td></td>
                            <td><input name="input" type="button" " value="确 定" class="btn_w66_write" onclick="closreload('<%=pageNumber%>','<%=pageSize%>');" /></td>
                            <td></td><td></td>
                        </span>
                    </tr>   
                    </tbody>
                </table>
                <div style="clear: both;"></div>
            </td>
        </tr>
    </table>
		</div>
	</div>
	<%@ include file="/WEB-INF/context/commons/bottom.jsp"%>
</div>
</body>
</html>