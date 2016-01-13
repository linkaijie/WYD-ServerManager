
// 默认的图片路径
Ext.BLANK_IMAGE_URL = ctx+'/widgets/ext/resources/images/default/s.gif';
// 初始化开启Ext节点的提示功能
Ext.QuickTips.init();

var root;

Ext.onReady( function(){
var Tree = Ext.tree; // 创建Tree对象
// 实例化tree树对象
	tree = new Tree.TreePanel({
			el:'gameTree',	// 目标div容器
			checkModel: 'cascade', // 'multiple':多选; 'single':单选; 'cascade':级联多选,包括所有父节点以及子节点; 'cascadechild':级联多选,包括所有子节点; 'child':级联多选,只包括已加载子节点
			onlyLeafCheckable: false, // false:所有节点都可选; true:只有leaf节点才可选
			animate: false,   
			rootVisible: true, // 隐藏根
			autoScroll : true,	
			enableDD : false,
			containerScroll : true,
			loader : new Tree.TreeLoader( {
			dataUrl : ctx+'/gameGroup/loadGameTree.action',
			baseAttrs:{ uiProvider: Ext.tree.TreeCheckNodeUI }	 // 添加 uiProvider 属性,TreeCheckNodeUI的功能具体请看TreeCheckNodeUI.js文件，里面有详细说明
		})
	})
		// 实例化根节点
			root = new Tree.AsyncTreeNode({
					text: '游戏服务器',
					draggable:true,
					id:0
				});
			tree.setRootNode(root);
			tree.render();
			root.expand();
			tree.expandAll();
});		

//获取所有选中的值
function getChecked(){
	var data="";
	//获取所有选中的节点
	var nodeNum=tree.getChecked();
	//采用for循环遍历节点
	for (var i=0; i < nodeNum.length; i++) {
			data += nodeNum[i].id+",";
	}
	return subString(data);
}

function subString(str){
	return str.substr(0,str.lastIndexOf(','))
}