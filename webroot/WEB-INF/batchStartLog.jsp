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

    setInterval(function() {
        $.ajax({
            type : 'POST',
            url : "common/getBatchStartWorldAndDispatchList.action",
            success : function(msg) {
            var dataObj=eval("("+msg+")"); 
            var worldList = dataObj.worldList;
            var dispatchList = dataObj.dispatchList;
            if(msg!=null && worldList!=null && worldList!='' && worldList.length>0)
            {
               for(var i=0;i<worldList.length;i++) 
               {
                   $("#wstatus_"+worldList[i].id).text(worldList[i].remark).css("color","green");
               }       
            }
            if(msg!=null && dispatchList!=null && dispatchList!='' && dispatchList.length>0)
            {
               for(var i=0;i<dispatchList.length;i++) 
               {
                   $("#dstatus_"+dispatchList[i].id).text(dispatchList[i].remark).css("color","green");
               }       
            }
        }
        });
    }, 2000);

    var P = window.parent, E = P.loadinndlg();
    function closreload(pageNumber, pageSize)
    {
         E.location.href=ctx+"/servermanage/serverManageList.action?pager.pageNumber="+pageNumber+"&pager.pageSize="+pageSize+"&redirectType="+$("input[name=redirectType]").val(); //此句为关闭此子窗口同时父窗口跳转到  ListData.aspx 页面
         P.cancel();
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
            <font style="color:red">${sessionScope.batchStartServerError}</font>
        </c:if>
            <td>
                <table class="bought-table" width="100%">
                    <thead>
                        <tr>
                            <th>WorldID</th>
                            <th>World名称</th>
                            <th>World状态</th>
                            <th>Dispatch名称</th>
                            <th>Dispatch状态</th>
                        </tr>
                    </thead>
                    <tbody class=" success-order">
                        <c:if test="${sessionScope.batchStartServerError ne null}">
                        <script language='javascript'>
                                                              　　            window.location.reload(true);
                                                              　　 </script>
                        </c:if>
                        <c:forEach items="${sessionScope.batchStartWorldAndDispatchMap}" var="batchMap" varStatus="status">
                            <tr <c:choose>
                            <c:when test="${status.index % 2 == 1}">class="odd"</c:when>
                            <c:otherwise>class="even"</c:otherwise>
                            </c:choose>>
                                <td id = "wid_${batchMap.key.id}" name = "accList" align="center">${batchMap.key.id}</td>
                                <td id = "wname_${batchMap.key.id}" name = "accList" align="center">${batchMap.key.name}</td>
                                <td id = "wstatus_${batchMap.key.id}" name = "accList" align="center"><forn style = "color:blue">开启中</forn></td>
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
                                            <td  id = "dstatus_${batchList.id}" name = "accList" align="center"><forn style = "color:blue">开启中</forn></td>
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