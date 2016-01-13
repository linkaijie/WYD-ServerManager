	function deploy(id){
		if (confirm("您确定要部署该服务吗？") == true) {
			var $button = $("td[name='accList']");
			var $acc = $("a[name='acc']");
			$button.attr("disabled", true);
			$acc.attr("hidden", true);
			$button.css({ color: "#fff", background: "gray" });		
			$acc.css({ color: "#fff", background: "gray" });
			lhgdialog.opendlg("ACCOUNT部署日志","${ctx}/accountserver/showDeployResult.action?accountId="+id,1020,500,true,true);
			$.ajax({
				type : 'POST',
				url : "accountserver/deployAccount.action",
				data : {
					"accountId" : id,
				},
				success : function(msg) {
					if(msg != 'success'){
						//lhgdialog.opendlg("ACCOUNT日志","${ctx}/accountserver/showStdout.action?accountId="+id,1020,500,true,true);
						//alert("恭喜你，部署成功~~！");	
					//}else{
						alert(msg);				
						window.location.href=ctx+"/accountserver/accountServerList.action";
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
				url : "accountserver/updateAccount.action",
				data : {
					"accountId" : id,
				},
				success : function(msg) {
					if(msg == 'success'){
						alert("恭喜你，更新成功~~！");	
					}else{
						alert(msg);				
					}
					window.location.href=ctx+"/accountserver/accountServerList.action";
				}
			});
		}
	}
	
	function input(id) {
		var url = "battleserver/battleServerInput.action";
		if (id) {
			url += "?battleId=" + id;
		}
		window.location.href = url;
	}
	
	function deletes(id) {
		if (confirm("您确定要删除该服务吗？") == true) {
			$.ajax({
				type : 'POST',
				url : "accountserver/delete.action",
				data : {
					"id" : id,
				},
				success : function(msg) {
					if(msg == 'success'){
						alert("删除成功~~！");	
					}else{
						alert(msg);				
					}
					window.location.href=ctx+"/accountserver/accountServerList.action";
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
	    $.post(ctx + "/battleserver/operateBattle.action", {
	        ids : subString(data),
	        operateType : 'start',
	    }, function(msg) {
	        var json = eval(msg);
	        var sessionVoListName = json.sessionVoListName;
	        var sessionBeanListName = json.sessionBeanListName;
	        lhgdialog.opendlg("战斗服开启日志",ctx+"/battleserver/operateBattle.action?operateType=start&sessionVoListName="+sessionVoListName+"&sessionBeanListName="+sessionBeanListName,600,500,true,true);
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
	    $.post(ctx + "/battleserver/operateBattle.action", {
	        ids : subString(data),
	        operateType : 'stop',
	    }, function(msg) {
	        var json = eval(msg);
            var sessionVoListName = json.sessionVoListName;
            var sessionBeanListName = json.sessionBeanListName;
            lhgdialog.opendlg("战斗服关闭日志",ctx+"/battleserver/operateBattle.action?operateType=stop&sessionVoListName="+sessionVoListName+"&sessionBeanListName="+sessionBeanListName,600,500,true,true);
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
            alert('请选择要同步的Account服务!');
            return;
        }
        if (data != "") {
            if (!confirm('您确定要同步当前选中的Account服务吗?')) {
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
        $.post(ctx + "/accountserver/synchronizeConfig.action", {
            accountIds : subString(data)
        }, function(msg) {
            if (msg == true) {
                alert("恭喜你，同步成功~~！"); 
            } else {
                alert(msg);
            }
            window.location.href=ctx+"/accountserver/accountServerList.action";
        }, "json");
    }
	
	function batchUpdate(pageNumber,pageSize) {
	    var updateNameList = "";
        $.ajax({
            type : 'POST',
            url : "common/getFileNameList.action",
            data: {
                   beanType:'AccountServer', 
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
                    alert('请选择要更新的Account服务!');
                    return;
                }
                if (data != "") {
                    if (!confirm('您确定要更新当前选中的服务吗?更新内容有：'+updateNameList)) {
                        return;
                    }
                } else {
                    return;
                }
                $.post(ctx + "/accountserver/updateServerByScp.action", {
                    accountIds : subString(data),
                    beanType:'AccountServer', 
                    pathType:'update'
                }, function(msg) {
                    lhgdialog.opendlg("ACCOUNT日志",ctx+"/updatelog.jsp?redirectType=AccountServer&pageNumber="+pageNumber+"&pageSize="+pageSize,250,500,true,true);
                    if (msg == true) {
        //                alert("恭喜你，更新成功~~！");   
                    } else {
                        alert(msg);
                    }
        //            window.location.href=ctx+"/saccountserver/accountServerList.action";
                }, "json");
                }
                });
    }
	
	function deployServerByScp() {
        var jdbcType = document.getElementById('jdbcType').value;
        if(jdbcType == ''){
            alert('请选择数据库!');
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
        $.post(ctx + "/accountserver/deployServerByScp.action", {
            accountIds : subString(data),
            jdbcType : jdbcType,
        }, function(msg) {
            lhgdialog.opendlg("ACCOUNT部署",ctx+"/deployServerLog.jsp",450,500,true,true);
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