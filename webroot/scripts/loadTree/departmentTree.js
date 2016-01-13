/**
 * 加载部门ree
 */

var zNodes =[
	 			{ id:1, pId:0, name:"枪魂", open:true, url:ctx+"/index.jsp", iconOpen:"../../../WYD-Server/images/zTreeStyle/img/diy/1_open.png", iconClose:"../../../WYD-Server/images/zTreeStyle/img/diy/1_close.png"},
	 			{ id:11, pId:1, name:"account", icon:"../../../WYD-Server/images/zTreeStyle/img/diy/2.png"},
	 			{ id:12, pId:1, name:"server", icon:"../../../WYD-Server/images/zTreeStyle/img/diy/3.png"},
	 			{ id:13, pId:1, name:"IPD", icon:"../../../WYD-Server/images/zTreeStyle/img/diy/5.png"},
	 			{ id:14, pId:1, name:"dispatcher", icon:"../../../WYD-Server/images/zTreeStyle/img/diy/5.png"},
	 			{ id:2, pId:0, name:"一站到底PK2", open:true, iconOpen:"../../../WYD-Server/images/zTreeStyle/img/diy/1_open.png", iconClose:"../../../WYD-Server/images/zTreeStyle/img/diy/1_close.png"},
	 			{ id:21, pId:2, name:"account", icon:"../../../WYD-Server/images/zTreeStyle/img/diy/2.png"},
	 			{ id:22, pId:2, name:"server", icon:"../../../WYD-Server/images/zTreeStyle/img/diy/3.png"},
	 			{ id:23, pId:2, name:"IPD", icon:"../../../WYD-Server/images/zTreeStyle/img/diy/5.png"},
	 			{ id:24, pId:2, name:"dispatcher", icon:"../../../WYD-Server/images/zTreeStyle/img/diy/5.png"},
	 		];

function loadDepartmentTree(){
  	treeObj = $.fn.zTree.init($("#departmentTree"), setting, zNodes);
//	$.ajax({
//		   type: "POST",
//		   url: ctx+"/department/loadTree.action?node=",
//		   success: function(msg){
//				console.log("msg:" + msg);
//			    zNodes = eval([[{"checked":false,"childs":[],"icon":"/gm/scripts/zTree/zTreeStyle/img/join.gif","iconClose":"","iconOpen":"","id":"2","isParent":"false","name":"EFUN","open":true,"pId":"","temp1":"","temp2":"","temp3":"","type":""},{"checked":false,"childs":[],"icon":"/gm/scripts/zTree/zTreeStyle/img/join.gif","iconClose":"","iconOpen":"","id":"3","isParent":"false","name":"网易达","open":true,"pId":"","temp1":"","temp2":"","temp3":"","type":""},{"checked":false,"childs":[],"icon":"/gm/scripts/zTree/zTreeStyle/img/join.gif","iconClose":"","iconOpen":"","id":"4","isParent":"false","name":"幕和","open":true,"pId":"","temp1":"","temp2":"","temp3":"","type":""},{"checked":false,"childs":[],"icon":"/gm/scripts/zTree/zTreeStyle/img/join.gif","iconClose":"","iconOpen":"","id":"7","isParent":"false","name":"管理员","open":true,"pId":"","temp1":"","temp2":"","temp3":"","type":""}]]);
//			  	treeObj = $.fn.zTree.init($("#departmentTree"), setting, zNodes);
//				treeObj.expandNode(treeObj.getNodes()[0], true, true, true);
//				//treeObj.setting.async.url = ctx+"/department/loadTree.action?isgl=true&node="+treeObj.getNodes()[0].id;
//				treeObj.reAsyncChildNodes(treeObj.getNodes()[0], "refresh");
//				//selectNodes();//默认选中第一个节点
//		   }
//		});
}


function selectNodes(){
	var treeObj = $.fn.zTree.getZTreeObj("departmentTree");
	var nodes = treeObj.getNodes();
	
	
	if (nodes.length>0) {
		treeObj.selectNode(nodes[0]);
		
		//默认首次加载
		$.ajax({
			  url: ctx+"/user/pageList.action?ajax=true",
			  cache: false,
			  success: function(html){
			    $("#biaoge").html(html);
			  }
		});
	} 
}


var setting = {
		async: {
			enable: true,
			url: getUrl,
			autoParam: ["id", "name"]
		},
		check: {
			enable: false
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		view: {

			dblClickExpand: false,

			showLine: true,

			selectedMulti: false,

			expandSpeed: ($.browser.msie && parseInt($.browser.version)<=6)?"":"fast"

		},

		callback: {
//			beforeExpand: beforeExpand,
			onAsyncSuccess: onAsyncSuccess,
			onAsyncError: onAsyncError,
			onClick: zTreeOnClick

		}
	};



function getUrl(treeId, treeNode) {
	console.log("getUrl");
	return ctx+"/department/loadTree.action?node="+treeNode.id;
}
function zTreeOnClick(e, treeId, treeNode) {
	console.log("ctx:" + ctx);
	  $.ajax({
		  url: ctx + "/sampleList.action",
		  cache: false,
		  success: function(html){  
		    $("#biaoge").html(html);
		  }    
		});    
}
   
	
function beforeExpand(treeId, treeNode) {
	console.log("beforeExpand");
	 var treeObj = $.fn.zTree.getZTreeObj("departmentTree");
	 treeObj.setting.async.url = getUrl(treeId,treeNode);
}

function onAsyncSuccess(event, treeId, treeNode, msg) {
	console.log("onAsyncSuccess");
	 var treeObj = $.fn.zTree.getZTreeObj("departmentTree");
	 treeObj.setting.async.url = getUrl(treeId,treeNode);
}

function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
	var zTree = $.fn.zTree.getZTreeObj("departmentTree");
	alert("异步获取数据出现异常。");
	treeNode.icon = "";
	zTree.updateNode(treeNode);
}