function input(id) {
        var url = "redisconfig/redisConfigInput.action";
        if (id) {
            url += "?redisConfigId=" + id;
        }
        window.location.href = url;
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
    $.post(ctx + "/redisconfig/operateRedis.action", {
        ids : subString(data),
        operateType : 'start',
    }, function(msg) {
        lhgdialog.opendlg("redis开启日志",ctx+"/redisconfig/operateRedis.action?operateType=start",600,500,true,true);
        if (msg == true) {
        } else {
            alert(msg);
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
    $.post(ctx + "/redisconfig/operateRedis.action", {
        ids : subString(data),
        operateType : 'stop',
    }, function(msg) {
        lhgdialog.opendlg("redis关闭日志",ctx+"/redisconfig/operateRedis.action?operateType=stop",600,500,true,true);
        if (msg == true) {
        } else {
            alert(msg);
        }
    }, "json");
}

function start(id){
    if (confirm("您确定要开启该服务吗？") == true) {
        $.post(ctx + "/redisconfig/operateRedis.action", {
            ids : id,
            operateType : 'start',
        }, function(msg) {
            lhgdialog.opendlg("redis开启日志",ctx+"/redisconfig/operateRedis.action?operateType=start",600,500,true,true);
            if (msg == true) {
            } else {
                alert(msg);
            }
        }, "json");
    }
}

function stop(id){
    if (confirm("您确定要关闭该服务吗？") == true) {
        $.post(ctx + "/redisconfig/operateRedis.action", {
            ids : id,
            operateType : 'stop',
        }, function(msg) {
            lhgdialog.opendlg("redis关闭日志",ctx+"/redisconfig/operateRedis.action?operateType=stop",600,500,true,true);
            if (msg == true) {
            } else {
                alert(msg);
            }
        }, "json");
    }
}

function deletes(id) {
    if (confirm("您确定要删除该服务吗？") == true) {
        $.ajax({
            type : 'POST',
            url : "redisconfig/delete.action",
            data : {
                "id" : id,
            },
            success : function(msg) {
                if(msg == 'success'){
                    alert("删除成功~~！");   
                }else{
                    alert(msg);             
                }
                window.location.href=ctx+"/redisconfig/redisConfigList.action";
            }
        });
    }
}