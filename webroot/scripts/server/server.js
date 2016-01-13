function input(id) {
        var url = "server/serverInput.action";
        if (id) {
            url += "?id=" + id;
        }
        window.location.href = url;
    }
    
    function deletes(id) {
        if (confirm("您确定要删除该服务吗？") == true) {
            $.ajax({
                type : 'POST',
                url : "server/delete.action",
                data : {
                    "id" : id,
                },
                success : function(msg) {
                    if(msg == 'success'){
                        alert("删除成功~~！");   
                    }else{
                        alert(msg);             
                    }
                    window.location.href=ctx+"/server/serverList.action";
                }
            });
        }
    }
    
    function copySshRsa(id) {
        if (confirm("您确定要授权该服务吗？") == true) {
            $.ajax({
                type : 'POST',
                url : "server/copySshRsa.action",
                data : {
                    "id" : id,
                },
                success : function(msg) {
                    if(msg == 'success'){
                        alert("授权成功~~！");   
                    }else{
                        alert(msg);             
                    }
                    window.location.href=ctx+"/server/serverList.action";
                }
            });
        }
    }
    
    function batchUpdateServerLib() {
        var updateNameList = "";
        $.ajax({
            type : 'POST',
            url : "common/getFileNameList.action",
            data: {
                   beanType:'serverLib', 
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
                    alert('请选择要更新的serverLib服务!');
                    return;
                }
                if (data != "") {
                    if (!confirm('您确定要更新当前选中的服务吗?更新内容有：'+updateNameList)) {
                        return;
                    }
                } else {
                    return;
                }
                $.post(ctx + "/server/updateServerByScp.action", {
                    ids : subString(data),
                    beanType:'serverLib', 
                    pathType:'update',
                    type:'0'
                }, function(msg) {
                    lhgdialog.opendlg("serverLib更新日志",ctx+"/updatelog.jsp",250,500,true,true);
                    if (msg == true) {
                    } else {
                        alert(msg);
                    }
                }, "json");
                  }
                });
    }
    
    function batchUpdateLib() {
        var updateNameList = "";
        $.ajax({
            type : 'POST',
            url : "common/getFileNameList.action",
            data: {
                   beanType:'lib', 
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
                    alert('请选择要更新的lib服务!');
                    return;
                }
                if (data != "") {
                    if (!confirm('您确定要更新当前选中的服务吗?更新内容有：'+updateNameList)) {
                        return;
                    }
                } else {
                    return;
                }
                $.post(ctx + "/server/updateServerByScp.action", {
                    ids : subString(data),
                    beanType:'lib', 
                    pathType:'update',
                        type:'1'
                }, function(msg) {
                    lhgdialog.opendlg("lib更新日志",ctx+"/updatelog.jsp",250,500,true,true);
                    if (msg == true) {
                    } else {
                        alert(msg);
                    }
                }, "json");
                  }
                });
    }
    
    function runCommand() {
        var commands = document.getElementById('command').value;
        alert("commands="+commands);
        if (commands == '') {
            alert("命令不能為空");
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
            alert('请选择要执行的服务!');
            return;
        }
        if (data != "") {
            if (!confirm('您确定要执行当前命令吗?')) {
                return;
            }
        } else {
            return;
        }
        $.post(ctx + "/server/runCommand.action", {
            ids : subString(data),
            command : commands
        }, function(msg) {
            lhgdialog.opendlg("命令執行日志",ctx+"/updatelog.jsp",400,650,true,true);
            if (msg == true) {
            } else {
                alert(msg);
            }
        }, "json");
    }