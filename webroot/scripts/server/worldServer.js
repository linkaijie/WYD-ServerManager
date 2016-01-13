	function start(id){
		if (confirm("您确定要开启该服务吗？") == true) {
			var $button = $("td[name='accList']");
			var $acc = $("a[name='acc']");
			$button.attr("disabled", true);
			$acc.attr("hidden", true);
			$button.css({ color: "#fff", background: "gray" });		
			$acc.css({ color: "#fff", background: "gray" });
			lhgdialog.opendlg("WORLD日志","${ctx}/worldserver/showStdout.action?worldId="+id,1020,500,true,true);
			$.ajax({
				type : 'POST',
				url : "worldserver/runWorld.action",
				data : {
					"worldId" : id
				},
				success : function(msg) {
					if(msg == 'true'){
// 						lhgdialog.opendlg("WORLD日志","${ctx}/worldserver/showStdout.action?worldId="+id,1020,500,true,true);
					}else{
						alert(msg);
						window.location.href=ctx+"/worldserver/worldServerList.action";
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
				url : "worldserver/killWorld.action",
				data : {
					"worldId" : id
				},
				success : function(msg) {
					alert(msg);	
					window.location.href=ctx+"/worldserver/worldServerList.action";
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
		lhgdialog.opendlg("WORLD部署日志","${ctx}/worldserver/showDeployResult.action?worldId="+id,1020,700,true,true);
		$.ajax({
			type : 'POST',
			url : "worldserver/deployWorld.action",
			data : {
				"worldId" : id
			},
			success : function(msg) {
				if(msg != 'success'){
					//alert("恭喜你，部署成功~~！");	
				//}else{
					alert(msg);				
					window.location.href=ctx+"/worldserver/worldServerList.action";
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
			url : "worldserver/updateWorld.action",
			data : {
				"worldId" : id
			},
			success : function(msg) {
				if(msg == 'success'){
					alert("恭喜你，更新成功~~！");	
				}else{
					alert(msg);				
				}
				window.location.href=ctx+"/worldserver/worldServerList.action";
			}
		});
		}
	}
	
	function input(id) {
		var url = "worldserver/worldServerInput.action";
		if (id) {
			url += "?worldId=" + id;
		}
		window.location.href = url;
	}
	
	function deletes(id) {
		if (confirm("您确定要删除该服务吗？") == true) {
			$.ajax({
				type : 'POST',
				url : "worldserver/delete.action",
				data : {
					"id" : id
				},
				success : function(msg) {
					if(msg == 'success'){
						alert("删除成功~~！");	
					}else{
						alert(msg);				
					}
					window.location.href=ctx+"/worldserver/worldServerList.action";
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
		lhgdialog.opendlg("WORLD日志",ctx+"/worldserver/batchStartWorld.action",1020,500,true,true);
		$.post(ctx + "/worldserver/batchStartWorld.action", {
			worldIds : subString(data)
		}, function(msg) {
			if (msg == true) {
				//lhgdialog.opendlg("WORLD日志",ctx+"/worldserver/batchStartWorld.action",1020,500,true,true);
			} else {
				alert(msg);
				window.location.href=ctx+"/worldserver/worldServerList.action";
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
		lhgdialog.opendlg("WORLD日志",ctx+"/worldserver/batchCloseWorld.action",1020,500,true,true);
		$.post(ctx + "/worldserver/batchCloseWorld.action", {
			worldIds : subString(data)
		}, function(msg) {
			if (msg == true) {
				//lhgdialog.opendlg("WORLD日志",ctx+"/worldserver/batchCloseWorld.action",1020,500,true,true);
			} else {
				alert(msg);
				window.location.href=ctx+"/worldserver/worldServerList.action";
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
            alert('请选择要同步的World服务!');
            return;
        }
        if (data != "") {
            if (!confirm('您确定要同步当前选中的World服务吗?')) {
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
        $.post(ctx + "/worldserver/synchronizeConfig.action", {
            worldIds : subString(data)
        }, function(msg) {
            if (msg == true) {
                alert("恭喜你，同步成功~~！"); 
            } else {
                alert(msg);
            }
            window.location.href=ctx+"/worldserver/worldServerList.action";
        }, "json");
    }
	
	function batchUpdate(pageNumber,pageSize) {
	    var updateNameList = "";
	    $.ajax({
            type : 'POST',
            url : "common/getFileNameList.action",
            data: {
	               beanType:'WorldServer', 
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
                $.post(ctx + "/worldserver/updateServerByScp.action", {
                    worldIds : subString(data),
                    beanType : 'WorldServer', 
                    pathType : 'update'
                }, function(msg) {
                    lhgdialog.opendlg("WORLD日志",ctx+"/updatelog.jsp?redirectType=WorldServer&pageNumber="+pageNumber+"&pageSize="+pageSize,450,650,true,true);
                    if (msg == true) {
        //                alert("恭喜你，更新成功~~！");   
                    } else {
                        alert(msg);
                    }
        //            window.location.href=ctx+"/worldserver/worldServerList.action";
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
        $.post(ctx + "/worldserver/updateRemoteConfig.action", {
            worldIds : subString(data),
            beanType : 'WorldServer', 
            pathType : 'update'
        }, function(msg) {
            lhgdialog.opendlg("WORLD日志",ctx+"/updateRemoteCfglog.jsp?redirectType=WorldServer&pageNumber="+pageNumber+"&pageSize="+pageSize,450,650,true,true);
            if (msg == true) {
            } else {
                alert(msg);
            }
        }, "json");
    }
	
	function inputCreate() {
        var url = "worldserver/worldServerCreateInput.action";
        window.location.href = url;
    }
    
    function worldAndDispatchAdd() {
        var url = "worldserver/worldAndDispatchAdd.action";
        window.location.href = url;
    }
    
    function deployServerByScp() {
        var jdbcType = document.getElementById('jdbcType').value;
        var memory = document.getElementById('memory').value;
        if(jdbcType == ''){
            alert('请选择数据库!');
            return;
        }
        if(memory == ''){
            alert('请选择内存!');
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
            if (!confirm('您确定要部署当前选中的服务吗?')) {
                return;
            }
        } else {
            return;
        }
        $.post(ctx + "/worldserver/deployServerByScp.action", {
            worldIds : subString(data),
            jdbcType : jdbcType,
            memory : memory
        }, function(msg) {
            lhgdialog.opendlg("WORLD部署",ctx+"/deployServerLog.jsp",500,600,true,true);
            if (msg == true) {
            } else {
                alert(msg);
            }
        }, "json");
    }
    
    function deploydiv(){
        var depolydiv = document.getElementById('depolydiv').style.display='block';
        var boughttable = document.getElementById('boughttable').style.display='none';
    }
    
    
    
    function synVersion() {
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
        $.post(ctx + "/worldserver/synVersion.action", {
            worldIds : subString(data),
        }, function(msg) {
            lhgdialog.opendlg("WORLD日志",ctx+"/updateRemoteCfglog.jsp",500,600,true,true);
            if (msg == true) {
            } else {
                alert(msg);
            }
        }, "json");
    }
    