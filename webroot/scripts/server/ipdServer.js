function start(id){
		if (confirm("您确定要开启该服务吗？") == true) {
			var $button = $("td[name='accList']");
			var $acc = $("a[name='acc']");
			$button.attr("disabled", true);
			$acc.attr("hidden", true);
			$button.css({ color: "#fff", background: "gray" });		
			$acc.css({ color: "#fff", background: "gray" });
			lhgdialog.opendlg("IPD日志","${ctx}/ipdserver/showStdout.action?ipdId="+id,1020,500,true,true);
			$.ajax({
				type : 'POST',
				url : "ipdserver/runIpd.action",
				data : {
					"ipdId" : id,
				},
				success : function(msg) {
					if(msg == 'true'){
						//lhgdialog.opendlg("ACCOUNT日志","${ctx}/ipdserver/showStdout.action?ipdId="+id,1020,500,true,true);
					}else{
						alert(msg);				
						window.location.href=ctx+"/ipdserver/ipdServerList.action";
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
				url : "ipdserver/killIpd.action",
				data : {
					"ipdId" : id,
				},
				success : function(msg) {
					alert(msg);
					window.location.href=ctx+"/ipdserver/ipdServerList.action";
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
			lhgdialog.opendlg("IPD部署日志","${ctx}/ipdserver/showDeployResult.action?ipdId="+id,1020,500,true,true);
			$.ajax({
				type : 'POST',
				url : "ipdserver/deployIpd.action",
				data : {
					"ipdId" : id,
				},
				success : function(msg) {
					if(msg != 'success'){
						//alert("恭喜你，部署成功~~！");	
					//}else{
						alert(msg);				
						window.location.href=ctx+"/ipdserver/ipdServerList.action";
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
				url : "ipdserver/updateIpd.action",
				data : {
					"ipdId" : id,
				},
				success : function(msg) {
					if(msg == 'success'){
						alert("恭喜你，更新成功~~！");	
					}else{
						alert(msg);				
					}
					window.location.href=ctx+"/ipdserver/ipdServerList.action";
				}
			});
		}
	}
	
	function input(id) {
		var url = "ipdserver/ipdServerInput.action";
		if (id) {
			url += "?ipdId=" + id;
		}
		window.location.href = url;
	}
	
	function deletes(id) {
		if (confirm("您确定要删除该服务吗？") == true) {
			$.ajax({
				type : 'POST',
				url : "ipdserver/delete.action",
				data : {
					"id" : id,
				},
				success : function(msg) {
					if(msg != 'success'){
						alert("删除成功~~！");	
					}else{
						alert(msg);				
					}
					window.location.href=ctx+"/ipdserver/ipdServerList.action";
				}
			});
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
			alert('请选择要启动Ipd服务!');
			return;
		}
		if (data != "") {
			if (!confirm('您确定要启动当前选中Ipd服务吗?')) {
				return;
			}
		} else {
			return;
		}
		lhgdialog.opendlg("Ipd日志",ctx+"/ipdserver/batchStartIpd.action",1020,500,true,true);
		$.post(ctx + "/ipdserver/batchStartIpd.action", {
			ipdIds : subString(data)
		}, function(msg) {
			if (msg == true) {
				//lhgdialog.opendlg("Ipd日志",ctx+"/ipdserver/batchStartIpd.action",1020,500,true,true);
			} else {
				alert(msg);
				window.location.href=ctx+"/ipdserver/ipdServerList.action";
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
			alert('请选择要关闭的Ipd服务!');
			return;
		}
		if (data != "") {
			if (!confirm('您确定要关闭当前选中的Ipd服务吗?')) {
				return;
			}
		} else {
			return;
		}
		lhgdialog.opendlg("Ipd日志",ctx+"/ipdserver/batchCloseIpd.action",1020,500,true,true);
		$.post(ctx + "/ipdserver/batchCloseIpd.action", {
			ipdIds : subString(data)
		}, function(msg) {
			if (msg == true) {
				//lhgdialog.opendlg("Ipd日志",ctx+"/ipdserver/batchCloseIpd.action",1020,500,true,true);
			} else {
				alert(msg);
				window.location.href=ctx+"/ipdserver/ipdServerList.action";
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
            alert('请选择要同步的Ipd服务!');
            return;
        }
        if (data != "") {
            if (!confirm('您确定要同步当前选中的Ipd服务吗?')) {
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
        $.post(ctx + "/ipdserver/synchronizeConfig.action", {
            ipdIds : subString(data)
        }, function(msg) {
            if (msg == true) {
                alert("恭喜你，同步成功~~！"); 
            } else {
                alert(msg);
            }
            window.location.href=ctx+"/ipdserver/ipdServerList.action";
        }, "json");
    }
	
	function batchUpdate(pageNumber,pageSize) {
	    var updateNameList = "";
        $.ajax({
            type : 'POST',
            url : "common/getFileNameList.action",
            data: {
                   beanType:'IpdServer', 
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
                    alert('请选择要更新的Ipd服务!');
                    return;
                }
                if (data != "") {
                    if (!confirm('您确定要更新当前选中的服务吗?更新内容有：'+updateNameList)) {
                        return;
                    }
                } else {
                    return;
                }
                $.post(ctx + "/ipdserver/updateServerByScp.action", {
                    ipdIds : subString(data),
                    beanType:'IpdServer', 
                    pathType:'update'
                }, function(msg) {
                    lhgdialog.opendlg("IPD更新日志",ctx+"/updatelog.jsp?redirectType=IpdServer&pageNumber="+pageNumber+"&pageSize="+pageSize,250,500,true,true);
                    if (msg == true) {
        //                alert("恭喜你，更新成功~~！");  
                    } else {
                        alert(msg);
        //                window.location.href=ctx+"/ipdserver/ipdServerList.action";
                    }
                }, "json");
                  }
                });
    }