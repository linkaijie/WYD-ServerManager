<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/context/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/context/commons/head.jsp"%>
<meta http-equiv="refresh" content="2">
<title>批量关闭server操作日志</title>
<script type="text/javascript">
	 $(document).ready(function () {
	});
	var P = window.parent, E = P.loadinndlg();
    function closreload(pageNumber, pageSize)
    {
    	//window.parent.window.location.href=ctx+"/operateLog/operatingLogList.action";
    	// test10为打开子窗口的父窗口的id，D.J.dialog.infrm['test10'].contentWindow 为父窗口的window对象。
    	// 如果是想跳转父窗口页面P.reload()函数就要加上第三个参数，使其为真，第二个参数为要跳转到的页面，
    	// 注意第二个参数不能是跨域的页面，不然会出错。
    	//if(!url)
    	//    P.reload(D.J.dialog.infrm['newForm'].contentWindow);
    	//else
    	//    P.reload(D.J.dialog.infrm['newForm'],ctx+"/operateLog/operatingLogList.action",true);
    	 //E.location.reload();  //此句为关闭此子窗口同时刷新父窗口
//          E.location.href=ctx+"/servermanage/serverManageList.action?pager.pageNumber="+pageNumber+"&pager.pageSize="+pageSize; //此句为关闭此子窗口同时父窗口跳转到  ListData.aspx 页面
//          P.cancel();
         E.doSearch();//guanyou
    }
</script>
</head>
<body >
<div id="hld">
	<div class="row-fluid">
		<div class="block">
			<form id="listForm" action="dispatch/list.action" method="post">
			    <input type="hidden" name="pageNumber" value="${pager.pageNumber}"> 
			    <input type="hidden" name="pageSize" value="${pager.pageSize}"> 
				<div class="block_head">
					<div class="condition_bar">
						<h2>批量关闭server操作日志</h2>
					</div>
				</div>
				<div class="block_content">
					<table class="sortable">
						<tr align="center">
							<td>${batchDispatch}</td>
						</tr>
						<tr align="center">
							<td>${closeDispatchNum}</td>
						</tr>
						<tr align="center">
							<td>${batchWorld}</td>
						</tr>
						<tr align="center">
							<td>${closeWorldNum}</td>
						</tr>
						<tr>
							<span align="center">
								<td><input name="input" type="button" " value="确 定" class="btn_w66_write" onclick="closreload('${pager.pageNumber}','${pager.pageSize}');" /></td>
							</span>
						</tr>	
					</table>
				</div>
			</form>
		</div>
	</div>
</div>
</body>
</html>