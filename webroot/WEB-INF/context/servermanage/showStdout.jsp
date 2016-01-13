<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/context/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/context/commons/head.jsp"%>
<meta http-equiv="refresh" content="2">
<title>Account服务日志</title>
<script type="text/javascript">
	
	//window.opener.location.href=window.opener.location.href;window.close();
	//window.opener.location.replace(window.opener.document.referrer);window.close();
	
	 $(document).ready(function () {
// 		 var accountId = $("input[name=accountId]");
//          $.ajax({
//              type: "POST",
//              data : {
//  				"accountId" : accountId,
//  			},
//  			 timeout : 3000,
//              url: "accountserver/showStdout.action",
//          });
	});
	var P = window.parent, E = P.loadinndlg();
    function closreload()
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
//          E.location.href=ctx+"/servermanage/serverManageList.action"; //此句为关闭此子窗口同时父窗口跳转到  ListData.aspx 页面
//          P.cancel(); 
         E.doSearch();//guanyou
    }
</script>
</head>
<body >
<div id="hld">
	<div class="row-fluid">
		<div class="block">
			<form id="listForm" action="account/list.action" method="post">
				<div class="block_head">
					<div class="condition_bar">
						<h2>Server操作日志</h2>
					</div>
				</div>
				<div class="block_content">
					<table class="sortable">
						<tr align="center">
							<td>${stdout}</td>
						</tr>	
						<tr>
							<td>
								<span align="center">
									<input name="input" type="button" " value="确 定" class="btn_w66_write" onclick="closreload();" />
								</span>
							</td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</div>
</div>
</body>
</html>