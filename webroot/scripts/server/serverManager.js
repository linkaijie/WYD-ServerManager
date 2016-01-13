function batchStart(pageNumber,pageSize) {
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
//		lhgdialog.opendlg("server日志",ctx+"/servermanage/batchStartServer.action?pageNumber="+pageNumber+"&pageSize="+pageSize,600,500,true,true);
		$.post(ctx + "/servermanage/batchStartServer.action", {
			worldIds : subString(data),
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
	
	function batchStop(pageNumber,pageSize) {
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
//		lhgdialog.opendlg("server日志",ctx+"/servermanage/batchCloseServer.action?pageNumber="+pageNumber+"&pageSize="+pageSize,1020,500,true,true);
		$.post(ctx + "/servermanage/batchCloseServer.action", {
			worldIds : subString(data),
			pageNumber : pageNumber,
			pageSize : pageSize
		}, function(msg) {
		    lhgdialog.opendlg("server批量关闭日志",ctx+"/servermanage/batchCloseServer.action?pageNumber="+pageNumber+"&pageSize="+pageSize,600,500,true,true);
//		    lhgdialog.opendlg("server批量关闭日志",ctx+"/batchCloseLog.jsp?pageNumber="+pageNumber+"&pageSize="+pageSize,600,500,true,true);
			if (msg == true) {
				//lhgdialog.opendlg("server日志",ctx+"/servermanage/batchCloseServer.action",1020,500,true,true);
			} else {
				alert(msg);
				this.doSearch();
//				window.location.href=ctx+"/servermanage/serverManageList.action?pageNumber="+${pager.pageNumber}+"&pageSize="+${pager.pageSize};
			}
		}, "json");
	}
	
function synchroniz() {
	window.location.href=ctx+"/servermanage/refreshCache.action";
}

function opeartMinotor(isMonitor) {
	if (confirm("您确定要修改监控状态吗？") == true) {
		window.location.href=ctx+"/servermanage/opeartMinotor.action?isMonitor="+isMonitor;
	}
}
	
function updateRestart(){
    var updateTypediv = document.getElementById('updateTypediv');
    var updateRestart = document.getElementById('updateRestart');
    var boughttable = document.getElementById('boughttable').style.display='none';
    updateTypediv.style.display='block';
    updateRestart.style.display='none';
}
	
function updateAndRestart() {
    var updateType = document.getElementById("updateType");
    if(!updateType.value){
        alert("请选择更新类型");
        return;
    }
    var updateNameList = "";
    $.ajax({
        type : 'POST',
        url : "common/getUpdateFileNameList.action",
        data: {
               updateType:updateType.value 
              },
        success : function(msg) {
          if(updateType.value!=4 && updateType.value!=5){
              // 获取更新名称列表
              updateNameList = msg;
              if(updateNameList=="notFile"){
                  alert("没有可更新文件");
                  return;
              }
              if(updateNameList.indexOf("没有可更新文件，请检查") > 0 ){
                  alert(updateNameList);
                  return;
              }
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
            if(updateType.value==4 || updateType.value==5){
                if (!confirm('您确定要重启当前选中的服务吗?')) {
                    return;
                }
            } else {
                if (data != "") {
                    if (!confirm('您确定要更新当前选中的服务吗?更新内容有：\n'+updateNameList)) {
                        return;
                    }
                } else {
                    return;
                }
            }
            $.post(ctx + "/servermanage/updateAndRestart.action", {
                worldIds : subString(data),
                updateType : updateType.value
            }, function(msg) {
                var json = eval(msg);
                var sessionVoListName = json.sessionVoListName;
                var sessionBeanListName = json.sessionBeanListName;
                lhgdialog.opendlg("更新并重启",ctx+"/servermanage/updateAndRestart.action?sessionVoListName="+sessionVoListName+"&sessionBeanListName="+sessionBeanListName,550,700,true,true);
            }, "json");
        }
        });
}