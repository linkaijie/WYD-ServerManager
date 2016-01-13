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
            url : "common/getChangeServerList.action",
            success : function(msg) {
            var dataObj=eval("("+msg+")");   
//            var changeServerBeanList = ${sessionScope.changeServerBeanList};
            if(msg!=null && dataObj!='' && dataObj!=null && dataObj.length>0)
            {
               for(var i=0;i<dataObj.length;i++) 
               {
                   $("#status_"+dataObj[i].id).text(dataObj[i].remark).css("color",dataObj[i].color);
               }
//               if(changeServerBeanList.length == dataObj.length){
//                   alert("好了");
//                   clearInterval(myInterval); 
//               }
            }
        }
        });
    }, 2000);

    var P = window.parent, E = P.loadinndlg();
    function closreload()
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
            <td>
                <table class="bought-table" width="100%">
                    <thead>
                        <tr>
                            <th>id</th>
                            <th>名称</th>
                            <th>启用状态</th>
                        </tr>
                    </thead>
                    <tbody class=" success-order">
                    <c:if test="${sessionScope.changeServerError ne null}">
                        <font style="color:red">${sessionScope.changeServerError}</font>
                    </c:if>
                    <c:if test="${sessionScope.changeServerBeanList ne null}">
                        <c:forEach items="${sessionScope.changeServerBeanList}" var="data" varStatus="status">
                            <tr <c:choose>
                            <c:when test="${status.index % 2 == 1}">class="odd"</c:when>
                            <c:otherwise>class="even"</c:otherwise>
                            </c:choose>>
                                <td id = "id_${data.id}" name = "accList" align="center">${data.id}</td>
                                <td id = "name_${data.id}" name = "accList" align="center">${data.name}</td>
                                <td id = "status_${data.id}" name = "accList" align="center"><forn style = "color:blue">准备中</forn></td>
                            </tr>
                        </c:forEach>
                        <tr>
                        <span align="center">
                            <td></td><td></td>
                            <td><input name="input" type="button" " value="确 定" class="btn_w66_write" onclick="closreload();" /></td>
                        </span>
                    </tr>   
                    </c:if>
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