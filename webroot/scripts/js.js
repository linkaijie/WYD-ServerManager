/*第一种形式 第二种形式 更换显示样式*/
function setTab(m,n){
var tli=document.getElementById("menu"+m).getElementsByTagName("li");
var mli=document.getElementById("main"+m).getElementsByTagName("ul");
for(i=0;i<tli.length;i++){
menu_name="m_"+i;
menu_name_hover="m_"+i+"_hover";

tli[i].className=i==n?menu_name_hover:menu_name;
mli[i].style.display=i==n?"block":"none";
}
}


function setTab2(m,n){
var tli=document.getElementById("menu"+m).getElementsByTagName("a");
var mli=document.getElementById("main"+m).getElementsByTagName("ul");
for(i=0;i<tli.length;i++){
//menu_name="m_"+i;
//menu_name_hover="m_"+i+"_hover";

tli[i].className=i==n?"hover":"";
mli[i].style.display=i==n?"block":"none";
}
}