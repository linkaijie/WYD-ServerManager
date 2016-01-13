function start(id){
		if (confirm("您确定要开启该服务吗？") == true) {
			var $button = $("td[name='accList']");
			var $acc = $("a[name='acc']");
			$button.attr("disabled", true);
			$acc.attr("hidden", true);
			$button.css({ color: "#fff", background: "gray" });		
			$acc.css({ color: "#fff", background: "gray" });
			lhgdialog.opendlg("dispatch日志","${ctx}/dispatchserver/showStdout.action?dispatchId="+id,1020,500,true,true);
			$.ajax({
				type : 'POST',
				url : "dispatchserver/runDispatch.action",
				data : {
					"dispatchId" : id
				},
				success : function(msg) {
					if(msg == 'true'){
						//lhgdialog.opendlg("dispatch日志","${ctx}/dispatchserver/showStdout.action?dispatchId="+id,1020,500,true,true);
					}else{
						alert(msg);
						window.location.href=ctx+"/dispatchserver/dispatchServerList.action";
					}
				}
			});
		}
	}
	function stop(id) {
		if (confirm("您确定要关闭该服务吗？") == true) {
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
					"dispatchId" : id
				},
				success : function(msg) {
					alert(msg);	
					window.location.href=ctx+"/dispatchserver/dispatchServerList.action";
				}
			});
		}
	}
	
	function deploy(id){
		if (confirm("您确定要部署该服务吗？") == true) {
			var $button = $("td[name='accList']");
			var $acc = $("a[name='acc']");
			$button.attr("disabled", true);
			$acc.attr("hidden", true);
			$button.css({ color: "#fff", background: "gray" });		
			$acc.css({ color: "#fff", background: "gray" });
			lhgdialog.opendlg("Dispatch部署日志","${ctx}/dispatchserver/showDeployResult.action?dispatchId="+id,1020,500,true,true);
			$.ajax({
				type : 'POST',
				url : "dispatchserver/deployDispatch.action",
				data : {
					"dispatchId" : id
				},
				success : function(msg) {
					if(msg != 'success'){
						//alert("恭喜你，部署成功~~！");	
					//}else{
						alert(msg);				
						window.location.href=ctx+"/dispatchserver/dispatchServerList.action";
					}
				}
			});
		}
	}
	
	function update(id){
		if (confirm("您确定要更新该服务吗？") == true) {
			var $button = $("td[name='accList']");
			var $acc = $("a[name='acc']");
			$button.attr("disabled", true);
			$acc.attr("hidden", true);
			$button.css({ color: "#fff", background: "gray" });		
			$acc.css({ color: "#fff", background: "gray" });
			$.ajax({
				type : 'POST',
				url : "dispatchserver/updateDispatch.action",
				data : {
					"dispatchId" : id
				},
				success : function(msg) {
					if(msg == 'success'){
						alert("恭喜你，部署成功~~！");	
					}else{
						alert(msg);				
					}
					window.location.href=ctx+"/dispatchserver/dispatchServerList.action";
				}
			});
		}
	}
	
	function input(id) {
		var url = "dispatchserver/dispatchServerInput.action";
		if (id) {
			url += "?dispatchId=" + id;
		}
		window.location.href = url;
	}

	function inputMore() {
        var url = "dispatchserver/dispatchServerMoreInput.action";
        window.location.href = url;
    }
	
	function dispatchServerMoreCreate() {
        var url = "dispatchserver/dispatchServerMoreCreate.action";
        window.location.href = url;
    }
	
	function deletes(id) {
		if (confirm("您确定要删除该服务吗？") == true) {
			var url = "dispatchserver/delete.action?id=" + id;
			window.location.href = url;
		}
	}
	
	function batchStart() {
		var boxs = document.getElementsByName('table_id');
		var data = "";
		//对象不存在,不处理,跳出方法
		if (!boxs) {
			return;
		}
		//获取选中行的ID
		for (var i = 0; i < boxs.length; i++) {
			if (boxs[i].checked == true) {
				data += boxs[i].value + ",";
			}
		}
		//没有选择提示错误
		if (data == "") {
			alert('请选择要启动的服务!');
			return;
		}
		if (data != "") {
			if (!confirm('您确定要启动当前选中的服务吗?')) {
				return;
			}
		} else {
			return;
		}
		lhgdialog.opendlg("dispatch日志",ctx+"/dispatchserver/batchStartDispatch.action",1020,500,true,true);
		$.post(ctx + "/dispatchserver/batchStartDispatch.action", {
			dispatchIds : subString(data)
		}, function(msg) {
			if (msg == true) {
				//lhgdialog.opendlg("dispatch日志",ctx+"/dispatchserver/batchStartDispatch.action",1020,500,true,true);
			} else {
				alert(msg);
				window.location.href=ctx+"/dispatchserver/dispatchServerList.action";
			}
		}, "json");
	}
	
	function batchStop() {
		var boxs = document.getElementsByName('table_id');
		var data = "";
		//对象不存在,不处理,跳出方法
		if (!boxs) {
			return;
		}
		//获取选中行的ID
		for (var i = 0; i < boxs.length; i++) {
			if (boxs[i].checked == true) {
				data += boxs[i].value + ",";
			}
		}
		//没有选择提示错误
		if (data == "") {
			alert('请选择要关闭的服务!');
			return;
		}
		if (data != "") {
			if (!confirm('您确定要关闭当前选中的服务吗?')) {
				return;
			}
		} else {
			return;
		}
		lhgdialog.opendlg("WORLD日志",ctx+"/dispatchserver/batchCloseDispatch.action",1020,500,true,true);
		$.post(ctx + "/dispatchserver/batchCloseDispatch.action", {
			dispatchIds : subString(data)
		}, function(msg) {
			if (msg == true) {
				//lhgdialog.opendlg("WORLD日志",ctx+"/dispatchserver/batchCloseDispatch.action",1020,500,true,true);
			} else {
				alert(msg);
				window.location.href=ctx+"/dispatchserver/dispatchServerList.action";
			}
		}, "json");
	}
	
	function synchronize() {
        var boxs = document.getElementsByName('table_id');
        var data = "";
        //对象不存在,不处理,跳出方法
        if (!boxs) {
            return;
        }
        //获取选中行的ID
        for (var i = 0; i < boxs.length; i++) {
            if (boxs[i].checked == true) {
                data += boxs[i].value + ",";
            }
        }
        //没有选择提示错误
        if (data == "") {
            alert('请选择要同步的Dispatch服务!');
            return;
        }
        if (data != "") {
            if (!confirm('您确定要同步当前选中的Dispatch服务吗?')) {
                return;
            }
        } else {
            return;
        }
        var $button = $("td[name='accList']");
        var $acc = $("a[name='acc']");
        $button.attr("disabled", true);
        $acc.attr("hidden", true);
        $button.css({ color: "#fff", background: "gray" });     
        $acc.css({ color: "#fff", background: "gray" });
        $.post(ctx + "/dispatchserver/synchronizeConfig.action", {
            dispatchIds : subString(data)
        }, function(msg) {
            if (msg == true) {
                alert("恭喜你，同步成功~~！"); 
            } else {
                alert(msg);
            }
            window.location.href=ctx+"/dispatchserver/dispatchServerList.action";
        }, "json");
    }
	
	function batchUpdate(pageNumber,pageSize) {
	    var updateNameList = "";
        $.ajax({
            type : 'POST',
            url : "common/getFileNameList.action",
            data: {
                   beanType:'DispatchServer', 
                   pathType:'update'
                  },
            success : function(msg) {
                // 获取更新名称列表
                updateNameList = msg;
                if(updateNameList=="notFile"){
                    alert("没有可更新文件");
                    return;
                }
                var boxs = document.getElementsByName('table_id');
                var data = "";
                //对象不存在,不处理,跳出方法
                if (!boxs) {
                    return;
                }
                //获取选中行的ID
                for (var i = 0; i < boxs.length; i++) {
                    if (boxs[i].checked == true) {
                        data += boxs[i].value + ",";
                    }
                }
                //没有选择提示错误
                if (data == "") {
                    alert('请选择要更新的服务!');
                    return;
                }
                if (data != "") {
                    if (!confirm('您确定要更新当前选中的服务吗?更新内容有：'+updateNameList)) {
                        return;
                    }
                } else {
                    return;
                }
                $.post(ctx + "/dispatchserver/updateServerByScp.action", {
                    dispatchIds : subString(data),
                    beanType:'DispatchServer', 
                    pathType:'update'
                }, function(msg) {
                    lhgdialog.opendlg("DISPATCH日志",ctx+"/updatelog.jsp?redirectType=DispatchServer&pageNumber="+pageNumber+"&pageSize="+pageSize,400,600,true,true);
                    if(msg == true){
        //                alert("恭喜你，更新成功~~！");   
                    } else {
                        alert(msg);
                    }
        //            window.location.href=ctx+"/dispatchserver/dispatchServerList.action";
                }, "json");
              }
          });
    }
	
	function batchUpdateRemoteCfg(pageNumber,pageSize) {
        var boxs = document.getElementsByName('table_id');
        var data = "";
        //对象不存在,不处理,跳出方法
        if (!boxs) {
            return;
        }
        //获取选中行的ID
        for (var i = 0; i < boxs.length; i++) {
            if (boxs[i].checked == true) {
                data += boxs[i].value + ",";
            }
        }
        //没有选择提示错误
        if (data == "") {
            alert('请选择要更新的服务!');
            return;
        }
        if (data != "") {
            if (!confirm('您确定要更新当前选中的遠程配置文件吗?')) {
                return;
            }
        } else {
            return;
        }
        $.post(ctx + "/dispatchserver/updateRemoteConfig.action", {
            dispatchIds : subString(data),
            beanType : 'DispatchServer', 
            pathType : 'update'
        }, function(msg) {
            lhgdialog.opendlg("DispatchServer日志",ctx+"/updateRemoteCfglog.jsp?redirectType=DispatchServer&pageNumber="+pageNumber+"&pageSize="+pageSize,400,600,true,true);
            if (msg == true) {
            } else {
                alert(msg);
            }
        }, "json");
    }
	
	function deployServerByScp() {
        var boxs = document.getElementsByName('table_id');
        var data = "";
        //对象不存在,不处理,跳出方法
        if (!boxs) {
            return;
        }
        //获取选中行的ID
        for (var i = 0; i < boxs.length; i++) {
            if (boxs[i].checked == true) {
                data += boxs[i].value + ",";
            }
        }
        //没有选择提示错误
        if (data == "") {
            alert('请选择要更新的服务!');
            return;
        }
        if (data != "") {
            if (!confirm('您确定要部署当前选中的服务吗?')) {
                return;
            }
        } else {
            return;
        }
        $.post(ctx + "/dispatchserver/deployServerByScp.action", {
            dispatchIds : subString(data),
        }, function(msg) {
            lhgdialog.opendlg("DispatchServer部署",ctx+"/deployServerLog.jsp",450,500,true,true);
            if (msg == true) {
            } else {
                alert(msg);
            }
        }, "json");
    }