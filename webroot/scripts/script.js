// JavaScript Document

//主菜单切换效果

function mainMenu(thisObj,Num){
if(thisObj.className == "active")return;
var tabObj = thisObj.parentNode.id;
var tabList = document.getElementById(tabObj).getElementsByTagName("li");

      //切换菜单样式为当前状态	
	  for(i=0; i <tabList.length; i++)
	  {
		txt="normal_"+i;
		txt2="active_"+Num;
		tabList[i].className = txt; 
	  } 



//	switch(Num)
//{
//	     case 0:
//        {	
//                document.getElementById("leftiframe").src='http://www.baidu.com';
//				 document.getElementById("rightiframe").src='http://www.163.com';
//		
//        }
//        break;
//	
//        default:
//        {
//                
//        }
//  
//}






tabList[Num].className = txt2;

//alert(tabList[Num].className)


}




/**/

/**/

function bar_click(){
	
if(document.getElementById("side").style.display !="block"){

showb();
}
else{
hid();
//alert('收');
}
function set_qbdiv(){

	h=( document.documentElement.clientHeight-228)+"px";
	h2=( document.documentElement.clientHeight-115)+"px";
	document.getElementById("qb_div").style.height=h;
	document.getElementById("qb_div3").style.height=h;
	document.getElementById("qb_div4").style.height=h;
	document.getElementById("qb_iframe").style.height=h;
	
	if(document.getElementById("main")){
		
		
		document.getElementById("main").style.height=h2;
		}
		else
		{
			
	}

}

}



function show_div(){
	
	 document.getElementById("hidden_div").style.display='block';
}



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


//-------------------------

function dishi(){
document.getElementById("dishi2").style.display='block';

}
function hid_dishi(){
document.getElementById("dishi2").style.display='none';

}


// 情报列表里的更多连接
function morelink(){
document.getElementById("morelink2").style.display='block';

}
function morelink2(){
document.getElementById("morelink2").style.display='none';

}
////////////////////////////////////////////////////////////////////
function pinglun(){
if(document.getElementById("pinglun").style.display==""){
document.getElementById("pinglun").style.display="none";
}
else{
document.getElementById("pinglun").style.display="";
}
}

function pinglun41(){
if(document.getElementById("pinglun41").style.display==""){
document.getElementById("pinglun41").style.display="none";
}
else{
document.getElementById("pinglun41").style.display="";
}
}













////////////////////////////////////////////////////////////////////


