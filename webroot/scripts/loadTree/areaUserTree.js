/**
 * 加载区域tree
 */

function loadAreaTree(){
	
	
	$.ajax({
		   type: "POST",
		   url: ctx+"/area/loadTree.action?node=",
		   success: function(msg){
			    zNodes = eval(msg);
			  	treeObj = $.fn.zTree.init($("#areaTree"), setting, zNodes);
				treeObj.expandNode(treeObj.getNodes()[0], true, true, true);
				treeObj.setting.async.url = ctx+"/area/loadTree.action?node="+treeObj.getNodes()[0].id;
				treeObj.reAsyncChildNodes(treeObj.getNodes()[0], "refresh");
				
				selectNodes();//默认选中第一个节点
		   }
		});
	
	
}




function selectNodes(){
	var treeObj = $.fn.zTree.getZTreeObj("areaTree");
	var nodes = treeObj.getNodes();
	
	if (nodes.length>0) {
		treeObj.selectNode(nodes[0]);
		
		//默认首次加载
		$.ajax({
			  url: ctx+"/user/pageList.action?treeId="+treeObj.getSelectedNodes()[0].id+"&ajax=true",
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
			beforeExpand: beforeExpand,
			onAsyncSuccess: onAsyncSuccess,
			onAsyncError: onAsyncError,
			onClick: zTreeOnClick

		}
	};



function getUrl(treeId, treeNode) {
	
	return ctx+"/area/loadTree.action?node="+treeNode.id;
}
function zTreeOnClick(e, treeId, treeNode) {
	
	  $.ajax({
		  url: ctx+"/user/pageList.action?treeId="+treeNode.id+"&ajax=true",
		  cache: false,
		  success: function(html){
		    $("#biaoge").html(html);
		  }
		});
	
}

	
function beforeExpand(treeId, treeNode) {
	 var treeObj = $.fn.zTree.getZTreeObj("areaTree");
	 treeObj.setting.async.url = getUrl(treeId,treeNode);
}

function onAsyncSuccess(event, treeId, treeNode, msg) {
	 var treeObj = $.fn.zTree.getZTreeObj("areaTree");
	 treeObj.setting.async.url = getUrl(treeId,treeNode);
}

function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
	var zTree = $.fn.zTree.getZTreeObj("areaTree");
	alert("异步获取数据出现异常。");
	treeNode.icon = "";
	zTree.updateNode(treeNode);
}