<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/context/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/context/commons/head.jsp"%>
<script type="text/javascript" src="${ctx}/scripts/table.js"></script>
<script type="text/javascript" src="${ctx}/scripts/jquery-1.7.2.min.js"></script>
<title>更新并重启服务</title>
<script type="text/javascript">
        myInterval = setInterval(function() {
        var sessionVoListName = $("#sessionVoListName").val();
        $.ajax({
            type : 'POST',
            url : "common/getOperateList.action",
            data: {sessionType: sessionVoListName },
            success : function(msg) {
            var dataObj=eval("("+msg+")");   
            if(msg!=null && dataObj!='' && dataObj!=null && dataObj.length>0)
            {
               for(var i=0;i<dataObj.length;i++) 
               {
                   $("#status_"+dataObj[i].id).text(dataObj[i].remark).css("color",dataObj[i].color);
                   if(dataObj[i].color=="red"){
                       alert("操作出错，请检查！");
                       break;
                   }
               }
            }
          }
        });
    }, 1200);

    var P = window.parent, E = P.loadinndlg();
    function destroySession(){
        var sessionVoListName = $("#sessionVoListName").val();
        var sessionBeanListName = $("#sessionBeanListName").val();
        var sessionName = sessionVoListName+","+sessionBeanListName;
        E.close(sessionName);
        E.doSearch();
    }
    
    window.onunload = function() {
        var sessionVoListName = $("#sessionVoListName").val();
        var sessionBeanListName = $("#sessionBeanListName").val();
        var sessionName = sessionVoListName+","+sessionBeanListName;
        E.close(sessionName);
        E.doSearch();
    };
</script>
</head>
<body>
<div id="hld">
	<div class="row-fluid">
		<div class="block">
		<input type="hidden" id="sessionVoListName" value="${sessionVoListName}"></input>
        <input type="hidden" id="sessionBeanListName" value="${sessionBeanListName}"></input>
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
                    <c:if test="${sessionScope.updateRestartError ne null}">
                        <font style="color:red">${sessionScope.updateRestartError}</font>
                    </c:if>
                    <c:if test="${beanInterfaceList ne null}">
                        <c:forEach items="${beanInterfaceList}" var="data" varStatus="status">
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
                            <td><input name="input" type="button" " value="确 定" class="btn_w66_write" onclick="destroySession();" /></td>
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