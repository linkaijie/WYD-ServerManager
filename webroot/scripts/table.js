function jumpPage(pageNo) {
	$("#pageNo").val(pageNo);
	$("#mainForm").submit();
}

function goPage(totalPages){
	var gonumber = $("#J_PageNum").val();
	if(gonumber.length>0){
		//判断要去到的页数是否大于总页数
		if(gonumber > totalPages){
			alert('您输入的页面大于当前的总页数,请重新输入');
			$("#J_PageNum").focus();
		}else{
			jumpPage(gonumber);
		}
	}else{
		alert('请输入要去到的页码!');
	}
	
}

function sort(orderBy, defaultOrder) {
	if ($("#orderBy").val() == orderBy) {
		if ($("#order").val() == "") {
			$("#order").val(defaultOrder);
		}
		else if ($("#order").val() == "desc") {
			$("#order").val("asc");
		}
		else if ($("#order").val() == "asc") {
			$("#order").val("desc");
		}
	}
	else {
		$("#orderBy").val(orderBy);
		$("#order").val(defaultOrder);
	}

	$("#mainForm").submit();
}

function search() {
	$("#order").val("");
	$("#orderBy").val("");
	$("#pageNo").val("1");

	$("#mainForm").submit();
}

function comBoxChange(obj){
	if(obj.checked){
		chooseAllCheckBox('table_id',true);
	}else{
		reverseChoose('table_id');
	}
}

//页面上菜单栏全选
/*
方法名 chooseAllCheckBox(checkBoxName,boolean)修改全部复选框的选中状态
功能:将name是checkBoxName的复选框的选择状态全部修改等于参数boolean.
参数说明,
checkBoxName : 复选框的name,
boolean : true(选中)或false(未选中) ,
注:可以根据实际需要对方法体进行修改. 调用示列 onclick="chooseAllCheckBox('checkbox',true)" ,单击后所有name为checkbox的复选框全部选中,如boolean示false的话则取消选中
*/
function chooseAllCheckBox(checkBoxName,boolean)
{
	var objCheckBoxTeam = document.getElementsByName(checkBoxName);
	for(var i=0;i<objCheckBoxTeam.length;i++){
	   objCheckBoxTeam[i].checked=boolean;
	}
	objCheckBoxTeam=null;
}
/*
方法名 reverseChoose(checkBoxName)反向选择复选框
返回值:无返回值.
功能:将name是checkBoxName的复选框的选择状态全部反向选择.
参数说明,
checkBoxName : 复选框的name,
注: 可以根据实际需要对方法体进行修改
*/
function reverseChoose(checkBoxName){
	var objCheckBoxTeam=document.getElementsByName(checkBoxName);
	for(var i=0;i<objCheckBoxTeam.length;i++){
   		if(objCheckBoxTeam[i].checked==false){
    		objCheckBoxTeam[i].checked=true;
    	}
    	else{
    		objCheckBoxTeam[i].checked=false
  		}
	}
	objCheckBoxTeam=null;
}

function checkBoxClick(obj,comboxname){
	if(obj.value=='全选'){
		chooseAllCheckBox(comboxname,true);
		obj.value = '取消';
	}else{
		chooseAllCheckBox(comboxname,false);
		obj.value = '全选';
	}
}

//格式化id字符串,使用","号分割
function subString(str){
	return str.substr(0,str.lastIndexOf(','))
}

//删除数据
function deleteLine(url){
	if (!confirm('您确定要删除当前这条数据吗?')) {
		return;
	}
	window.location.href = url;
}

function dataDelete(url){
	var boxs = document.getElementsByName('table_id');
	var data = "";
	//对象不存在,不处理,跳出方法
	if (!boxs) {
		return;
	}
	//获取选中行的ID
	for (i = 0; i < boxs.length; i++) {
		if (boxs[i].checked == true) {
			data += boxs[i].value + ",";
		}
	}
	//没有选择提示错误
	if (data == "") {
		alert("请选择要删除的列!");
		return;
	}
	//确认用户是否删除选中数据
	if (data != "") {
		if (!confirm('您确定要删除当前选中的这些数据吗?')) {
			return;
		}
	} else {
		return;
	}
	window.location.href = url+'?ids='+subString(data);
}

function formPage(url){
	window.location.href = url;
}

function getValue(name){
	var data="";
	var boxs = document.getElementsByName(name);
	for (i = 0; i < boxs.length; i++) {
		data += boxs[i].value + ",";
	}
	return subString(data);
}

function subString(str){
	return str.substr(0,str.lastIndexOf(','))
}