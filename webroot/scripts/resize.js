// JavaScript Document

window.onload = function(){	
//重置窗口
	table_left_height=( document.documentElement.clientHeight-182)+"px";
	document.getElementById("table_left").style.minHeight=table_left_height;
}

window.onresize= function(){	
//重置窗口
	table_left_height=( document.documentElement.clientHeight-182)+"px";
	document.getElementById("table_left").style.minHeight=table_left_height;
}
