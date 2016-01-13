<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@ include file="/WEB-INF/context/commons/head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
  <title><s:text name="ModifyPassword"></s:text></title>
	<%@ include file="/commons/meta.jsp"%>
	<link rel="stylesheet" type="text/css" href="${exthome}/resources/css/ext-all.css" />
	<script type="text/javascript" src="${exthome}/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="${exthome}/ext-all.js"></script>
	<script type="text/javascript">
	var p = window.parent;
	var d = p.loadinndlg();
	function newFormSubmit(){
		var newPwd=$("#newPwd").val();
		var password2=$("#password2").val();
		if(newPwd!=password2){
			alert("密码不一致，请重新输入");
			return ;
		}
		$.ajax({
			type : "POST",
			url : ctx+"/user/savePwd.action",
			data : {
				"pwd" : $("#newPwd").val(),
			},
			success : function(msg) {
				alert(msg);			
				p.cancel();	
			}
		});
	}	
	
	function checkedPwd(){
		p.crebtn('button',$("#Confirm").val(),newFormSubmit);
	} 
	</script>
  </head> 
  <body>
  <input type="hidden" name="Confirm" id="Confirm" value="确定" />
  <input type="hidden" name="userId" id="userId" value="<%=request.getParameter("id")%>"/>
  	<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top"><table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td>
        <form action="" method="post" name="newForm" id="newForm">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_item">
          <tr>
            <td height="30" align="right" class="left_txt2">新密码</td>
            <td height="30"><input type="password" name="newPwd" id="newPwd"  size="30" onfocus="checkedPwd();"/></td>
            </tr>
          <tr>
            <td height="30" align="right" valign="top" class="left_txt2">确认密码</td>
            <td height="30"><input type="password" name="password2" id="password2" size="30" onfocus="checkedPwd();"/></td>
            </tr>
          </table>
          </form>
          </td>
      </tr>
    </table></td>
  </tr>
</table>
  </body>
</html>
