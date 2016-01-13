/* 主页面上的JS函数，供内页调用 */ 


var jq = jQuery.noConflict(); //把$让给第一个实现它的库，用jq代替

// 首页快捷键下拉，用DIV模拟


//////////////////////


function hid(){
jq("#side").hide(300);
//parent.document.getElementById("side").style.display="none";
jq("#main").toggleClass("main_hid_bar");

jq("#left_bar").toggleClass("left_bar_hid_bar");
jq("#bottom").toggleClass("bottom_hid_bar");

};
	
function showb(){
jq("#side").fadeIn("fast");
//parent.document.getElementById("side").style.display="block";
jq("#main").toggleClass("main_hid_bar");
jq("#left_bar").toggleClass("left_bar_hid_bar");
jq("#bottom").toggleClass("bottom_hid_bar");jq("#side").toggleClass("side_hid_bar");
};






