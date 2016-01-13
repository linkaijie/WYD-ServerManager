/**Account服务开启*/
function startAccount(id){
	if (confirm("您确定要开启该ACCOUNT服务吗？") == true) {
		var $button = $("td[name='accList']");
		var $acc = $("a[name='acc']");
		$button.attr("disabled", true);
		$acc.attr("hidden", true);
		$button.css({ color: "#fff", background: "gray" });		
		$acc.css({ color: "#fff", background: "gray" });
		lhgdialog.opendlg("ACCOUNT启动日志",ctx+"/servermanage/showStdout.action?serverType=account&serverId="+id,1020,500,true,true);
		$.ajax({
			type : 'POST',
			url : "accountserver/runAccount.action",
			data : {
				"accountId" : id,
			},
			success : function(msg) {
				if(msg == 'true'){
//					lhgdialog.opendlg("ACCOUNT日志",ctx+"/accountserver/showStdout.action?accountId="+id,1020,500,true,true);
				}else{
					alert(msg);
					window.location.href=ctx+"/servermanage/serverManageList.action";
				}
			}
		});
	}
}

/**Ipd服务开启*/
function startIpd(id){
	if (confirm("您确定要开启该服务吗？") == true) {
		var $button = $("td[name='accList']");
		var $acc = $("a[name='acc']");
		$button.attr("disabled", true);
		$acc.attr("hidden", true);
		$button.css({ color: "#fff", background: "gray" });		
		$acc.css({ color: "#fff", background: "gray" });
		lhgdialog.opendlg("IPD启动日志",ctx+"/servermanage/showStdout.action?serverType=ipd&serverId="+id,1020,500,true,true);
		$.ajax({
			type : 'POST',
			url : "ipdserver/runIpd.action",
			data : {
				"ipdId" : id,
			},
			success : function(msg) {
				if(msg == 'true'){
//					lhgdialog.opendlg("ACCOUNT日志",ctx+"/ipdserver/showStdout.action?ipdId="+id,1020,500,true,true);
				}else{
					alert(msg);				
				}
			}
		});
	}
}

/**World服务开启*/
function startWorld(id){
	if (confirm("您确定要开启该服务吗？") == true) {
		var $button = $("td[name='accList']");
		var $acc = $("a[name='acc']");
		$button.attr("disabled", true);
		$acc.attr("hidden", true);
		$button.css({ color: "#fff", background: "gray" });		
		$acc.css({ color: "#fff", background: "gray" });
		lhgdialog.opendlg("WORLD启动日志",ctx+"/servermanage/showStdout.action?serverType=world&serverId="+id,1020,500,true,true);
		$.ajax({
			type : 'POST',
			url : "worldserver/runWorld.action",
			data : {
				"worldId" : id,
			},
			success : function(msg) {
				if(msg == 'true'){
//					lhgdialog.opendlg("WORLD日志",ctx+"/worldserver/showStdout.action?worldId="+id,1020,500,true,true);
				}else{
					alert(msg);
					window.location.href=ctx+"/servermanage/serverManageList.action";
				}
			}
		});
	}
}

/**Dispatch服务开启*/
function startDispatchOne(id){
	if (confirm("您确定要开启该服务吗？") == true) {
		var $button = $("td[name='accList']");
		var $acc = $("a[name='acc']");
		$button.attr("disabled", true);
		$acc.attr("hidden", true);
		$button.css({ color: "#fff", background: "gray" });		
		$acc.css({ color: "#fff", background: "gray" });
		lhgdialog.opendlg("dispatch启动日志",ctx+"/servermanage/showStdout.action?serverType=dispatch&serverId="+id,1020,500,true,true);
		$.ajax({
			type : 'POST',
			url : "dispatchserver/runDispatch.action",
			data : {
				"dispatchId" : id,
			},
			success : function(msg) {
				if(msg == 'true'){
//					lhgdialog.opendlg("dispatch日志",ctx+"/dispatchserver/showStdout.action?dispatchId="+id,1020,500,true,true);
				}else{
					alert(msg);
					window.location.href=ctx+"/servermanage/serverManageList.action";
				}
			}
		});
	}
}

/**Dispatch服务开启*/
function startDispatchTwo(id){
	if (confirm("您确定要开启该服务吗？") == true) {
		var $button = $("td[name='accList']");
		var $acc = $("a[name='acc']");
		$button.attr("disabled", true);
		$acc.attr("hidden", true);
		$button.css({ color: "#fff", background: "gray" });		
		$acc.css({ color: "#fff", background: "gray" });
		lhgdialog.opendlg("dispatch启动日志",ctx+"/servermanage/showStdout.action?serverType=dispatch&serverId="+id,1020,500,true,true);
		$.ajax({
			type : 'POST',
			url : "dispatchserver/runDispatch.action",
			data : {
				"dispatchId" : id,
			},
			success : function(msg) {
				if(msg == 'true'){
//					lhgdialog.opendlg("dispatch日志",ctx+"/dispatchserver/showStdout.action?dispatchId="+id,1020,500,true,true);
				}else{
					alert(msg);
					window.location.href=ctx+"/servermanage/serverManageList.action";
				}
			}
		});
	}
}

/**Dispatch服务开启*/
function startDispatchThree(id){
	if (confirm("您确定要开启该服务吗？") == true) {
		var $button = $("td[name='accList']");
		var $acc = $("a[name='acc']");
		$button.attr("disabled", true);
		$acc.attr("hidden", true);
		$button.css({ color: "#fff", background: "gray" });		
		$acc.css({ color: "#fff", background: "gray" });
		lhgdialog.opendlg("dispatch启动日志",ctx+"/servermanage/showStdout.action?serverType=dispatch&serverId="+id,1020,500,true,true);
		$.ajax({
			type : 'POST',
			url : "dispatchserver/runDispatch.action",
			data : {
				"dispatchId" : id,
			},
			success : function(msg) {
				if(msg == 'true'){
//					lhgdialog.opendlg("dispatch日志",ctx+"/dispatchserver/showStdout.action?dispatchId="+id,1020,500,true,true);
				}else{
					alert(msg);
					window.location.href=ctx+"/servermanage/serverManageList.action";
				}
			}
		});
	}
}

/**Dispatch服务开启*/
function startDispatchFour(id){
	if (confirm("您确定要开启该服务吗？") == true) {
		var $button = $("td[name='accList']");
		var $acc = $("a[name='acc']");
		$button.attr("disabled", true);
		$acc.attr("hidden", true);
		$button.css({ color: "#fff", background: "gray" });		
		$acc.css({ color: "#fff", background: "gray" });
		lhgdialog.opendlg("dispatch启动日志",ctx+"/servermanage/showStdout.action?serverType=dispatch&serverId="+id,1020,500,true,true);
		$.ajax({
			type : 'POST',
			url : "dispatchserver/runDispatch.action",
			data : {
				"dispatchId" : id,
			},
			success : function(msg) {
				if(msg == 'true'){
//					lhgdialog.opendlg("dispatch日志",ctx+"/dispatchserver/showStdout.action?dispatchId="+id,1020,500,true,true);
				}else{
					alert(msg);
					window.location.href=ctx+"/servermanage/serverManageList.action";
				}
			}
		});
	}
}

/**Dispatch服务开启*/
function startDispatchFive(id){
	if (confirm("您确定要开启该服务吗？") == true) {
		var $button = $("td[name='accList']");
		var $acc = $("a[name='acc']");
		$button.attr("disabled", true);
		$acc.attr("hidden", true);
		$button.css({ color: "#fff", background: "gray" });		
		$acc.css({ color: "#fff", background: "gray" });
		lhgdialog.opendlg("dispatch启动日志",ctx+"/servermanage/showStdout.action?serverType=dispatch&serverId="+id,1020,500,true,true);
		$.ajax({
			type : 'POST',
			url : "dispatchserver/runDispatch.action",
			data : {
				"dispatchId" : id,
			},
			success : function(msg) {
				if(msg == 'true'){
//					lhgdialog.opendlg("dispatch日志",ctx+"/dispatchserver/showStdout.action?dispatchId="+id,1020,500,true,true);
				}else{
					alert(msg);
					window.location.href=ctx+"/servermanage/serverManageList.action";
				}
			}
		});
	}
}

//=================================================
/**Account服务关闭*/
function stopAccount(id) {
	if (confirm("您确定要关闭该Account服务吗？") == true) {
		var $button = $("td[name='accList']");
		var $acc = $("a[name='acc']");
		$button.attr("disabled", true);
		$acc.attr("hidden", true);
		$button.css({ color: "#fff", background: "gray" });		
		$acc.css({ color: "#fff", background: "gray" });		
		$.ajax({
			type : 'POST',
			url : "accountserver/killAccount.action",
			data : {
				"accountId" : id,
			},
			success : function(msg) {
				alert(msg);	
				window.location.href=ctx+"/servermanage/serverManageList.action";
			}
		});
	}
}

/**Ipd服务关闭*/
function stopIpd(id) {
	if (confirm("您确定要关闭该Ipd服务吗？") == true) {
		var $button = $("td[name='accList']");
		var $acc = $("a[name='acc']");
		$button.attr("disabled", true);
		$acc.attr("hidden", true);
		$button.css({ color: "#fff", background: "gray" });		
		$acc.css({ color: "#fff", background: "gray" });
		$.ajax({
			type : 'POST',
			url : "ipdserver/killIpd.action",
			data : {
				"ipdId" : id,
			},
			success : function(msg) {
				alert(msg);
				window.location.href=ctx+"/servermanage/serverManageList.action";
			}
		});
	}
}

/**World服务关闭*/
function stopWorld(id) {
	if (confirm("您确定要关闭该World服务吗？") == true) {
		var $button = $("td[name='accList']");
		var $acc = $("a[name='acc']");
		$button.attr("disabled", true);
		$acc.attr("hidden", true);
		$button.css({ color: "#fff", background: "gray" });		
		$acc.css({ color: "#fff", background: "gray" });
		$.ajax({
			type : 'POST',
			url : "worldserver/killWorld.action",
			data : {
				"worldId" : id,
			},
			success : function(msg) {
				alert(msg);	
				window.location.href=ctx+"/servermanage/serverManageList.action";
			}
		});
	}
}

/**Dispatch服务关闭*/
function stopDispatchOne(id) {
	if (confirm("您确定要关闭该Dispatch服务吗？") == true) {
		var $button = $("td[name='accList']");
		var $acc = $("a[name='acc']");
		$button.attr("disabled", true);
		$acc.attr("hidden", true);
		$button.css({ color: "#fff", background: "gray" });		
		$acc.css({ color: "#fff", background: "gray" });
		$.ajax({
			type : 'POST',
			url : "dispatchserver/killDispatch.action",
			data : {
				"dispatchId" : id,
			},
			success : function(msg) {
				alert(msg);	
				window.location.href=ctx+"/servermanage/serverManageList.action";
			}
		});
	}
}

/**Dispatch服务关闭*/
function stopDispatchTwo(id) {
	if (confirm("您确定要关闭该Dispatch服务吗？") == true) {
		var $button = $("td[name='accList']");
		var $acc = $("a[name='acc']");
		$button.attr("disabled", true);
		$acc.attr("hidden", true);
		$button.css({ color: "#fff", background: "gray" });		
		$acc.css({ color: "#fff", background: "gray" });
		$.ajax({
			type : 'POST',
			url : "dispatchserver/killDispatch.action",
			data : {
				"dispatchId" : id,
			},
			success : function(msg) {
				alert(msg);	
				window.location.href=ctx+"/servermanage/serverManageList.action";
			}
		});
	}
}

/**Dispatch服务关闭*/
function stopDispatchThree(id) {
	if (confirm("您确定要关闭该Dispatch服务吗？") == true) {
		var $button = $("td[name='accList']");
		var $acc = $("a[name='acc']");
		$button.attr("disabled", true);
		$acc.attr("hidden", true);
		$button.css({ color: "#fff", background: "gray" });		
		$acc.css({ color: "#fff", background: "gray" });
		$.ajax({
			type : 'POST',
			url : "dispatchserver/killDispatch.action",
			data : {
				"dispatchId" : id,
			},
			success : function(msg) {
				alert(msg);	
				window.location.href=ctx+"/servermanage/serverManageList.action";
			}
		});
	}
}

//=============================================
/**批量Account服务关闭*/
function batchStopAccount() {
	if (confirm("您确定要关闭该ACCOUNT服务吗？") == true) {
		var $button = $("td[name='accList']");
		var $acc = $("a[name='acc']");
		$button.attr("disabled", true);
		$acc.attr("hidden", true);
		$button.css({ color: "#fff", background: "gray" });		
		$acc.css({ color: "#fff", background: "gray" });
		lhgdialog.opendlg("ACCOUNT批量关闭日志",ctx+"/accountserver/batchCloseAccount.action",1020,500,true,true);
		$.ajax({
			type : 'POST',
			url : "accountserver/batchCloseAccount.action",
			data : {
				"accountIds" : '3',
			},
			success : function(msg) {
				if(msg == 'true'){
//					lhgdialog.opendlg("ACCOUNT日志",ctx+"/accountserver/batchCloseAccount.action",1020,500,true,true);
				}else{
					alert(msg);
					window.location.href=ctx+"/servermanage/serverManageList.action";
				}
			}
		});
	}
}

/**批量Ipd服务关闭*/
function batchStopIpd() {
	if (confirm("您确定要关闭该Account服务吗？") == true) {
		var $button = $("td[name='accList']");
		var $acc = $("a[name='acc']");
		$button.attr("disabled", true);
		$acc.attr("hidden", true);
		$button.css({ color: "#fff", background: "gray" });		
		$acc.css({ color: "#fff", background: "gray" });		
		$.ajax({
			type : 'POST',
			url : "ipdserver/batchCloseIpd.action",
			data : {
				"accountIds" : ids,
			},
			success : function(msg) {
				alert(msg);	
				window.location.href=ctx+"/servermanage/serverManageList.action";
			}
		});
	}
}


//-------------------
function batchStartAccount(){
	if (confirm("您确定要开启该ACCOUNT服务吗？") == true) {
		var $button = $("td[name='accList']");
		var $acc = $("a[name='acc']");
		$button.attr("disabled", true);
		$acc.attr("hidden", true);
		$button.css({ color: "#fff", background: "gray" });		
		$acc.css({ color: "#fff", background: "gray" });
		$.ajax({
			type : 'POST',
			url : "accountserver/batchStartAccount.action",
			data : {
				"accountIds" : '1,3,2,9,2,3,9,2,3,9,2,3,9,2,3,9,2,3,9,2,3,9,2,3,9,2,3,9,2,3,9',
			},
			success : function(msg) {
				if(msg == 'true'){
					lhgdialog.opendlg("ACCOUNT日志",ctx+"/accountserver/batchStartAccount.action",1020,500,true,true);
				}else{
					alert(msg);
					window.location.href=ctx+"/servermanage/serverManageList.action";
				}
			}
		});
	}
}


function startServer(id,pageNumber,pageSize){
	if (confirm("您确定要开启该Server服务吗？") == true) {
		var $button = $("td[name='accList']");
		var $acc = $("a[name='acc']");
		$button.attr("disabled", true);
		$acc.attr("hidden", true);
		$button.css({ color: "#fff", background: "gray" });		
		$acc.css({ color: "#fff", background: "gray" });
		$.post(ctx + "/servermanage/batchStartServer.action", {
			worldIds : id,
            pageNumber : pageNumber,
            pageSize : pageSize
		}, function(msg) {
		    lhgdialog.opendlg("server日志",ctx+"/servermanage/batchStartServer.action?pageNumber="+pageNumber+"&pageSize="+pageSize,600,500,true,true);
//		    lhgdialog.opendlg("server批量開啟日志",ctx+"/batchStartLog.jsp?pageNumber="+pageNumber+"&pageSize="+pageSize,600,500,true,true);
			if (msg == true) {
			} else {
				alert(msg);
				window.location.href=ctx+"/servermanage/serverManageList.action?pageNumber="+pageNumber+"&pageSize="+pageSize;
			}
		}, "json");
	}
}

function stopServer(id,pageNumber,pageSize){
	if (confirm("您确定要关闭该Server服务吗？") == true) {
		var $button = $("td[name='accList']");
		var $acc = $("a[name='acc']");
		$button.attr("disabled", true);
		$acc.attr("hidden", true);
		$button.css({ color: "#fff", background: "gray" });		
		$acc.css({ color: "#fff", background: "gray" });
		$.post(ctx + "/servermanage/batchCloseServer.action", {
			worldIds : id,
			pageNumber : pageNumber,
			pageSize : pageSize,
		}, function(msg) {
		    lhgdialog.opendlg("server日志",ctx+"/servermanage/batchCloseServer.action?pageNumber="+pageNumber+"&pageSize="+pageSize,600,500,true,true);
//		    lhgdialog.opendlg("server批量開啟日志",ctx+"/batchCloseLog.jsp?pageNumber="+pageNumber+"&pageSize="+pageSize,600,500,true,true);
			if (msg == true) {
				//lhgdialog.opendlg("server日志",ctx+"/servermanage/batchCloseServer.action",1020,500,true,true);
			} else {
				alert(msg);
				window.location.href=ctx+"/servermanage/serverManageList.action?pageNumber="+pageNumber+"&pageSize="+pageSize;
			}
		}, "json");
	}
}
