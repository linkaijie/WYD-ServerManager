<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="WEB-INF/context/commons/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
String addressName=application.getInitParameter("addressName");
%>
<title><%=addressName%>-珠海网易达电子科技发展有限公司 </title>
<link href="${ctx}/styles/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	function submitResult(){
		//alert($("j_username").val());
		//if($("#j_username").val()==""){
		//	alert("<s:text name='请输入登录名称'/>");
		//	return false;
		//}
		//if($("#j_password").val()==""){
		//	alert("<s:text name='请输入密码'/>");
		//	return false;
		//}
		document.getElementById("newForm").submit();
	}
	
	function keyDownSubmit(event){
		if(event.keyCode==13){
			if($("#j_username").val()==""){
				alert("<s:text name='InputUserName'/>");
				return false;
			}
			if($("#j_password").val()==""){
				alert("<s:text name='InputPassword'/>");
				return false;
			}
			document.getElementById("newForm").submit();
		}
	}
</script>
</head>
<body>
<div class="login_body">
  <div id="container">
    <DIV id=main>
      <DIV id=right>
        <DIV class=login-frm>
        <form id="newForm"  method="post" name='newForm' action="${ctx}/j_spring_security_check">
          <table border="0">
           	<tr>
              <td height="15">&nbsp;</td>
              <td height="15">&nbsp;</td>
            </tr>
            <tr>
              <td width="60" height="25" align="left">登陆名称</td>
              <td><input type="text" name="j_username" id="j_username"  onkeydown="keyDownSubmit(event);"/></td>
            </tr>
            <tr>
              <td height="15">&nbsp;</td>
              <td height="15">&nbsp;</td>
            </tr>
            <tr>
              <td width="60" height="25" align="left">密码</td>
              <td><input type="password" name="j_password" id="j_password"  onkeydown="keyDownSubmit(event);"/></td>
            </tr>
            <tr>
              <td height="15">&nbsp;</td>
              <td height="15">&nbsp;</td>
            </tr>
            <tr>
              <td height="25">&nbsp;</td>
              <td><span class="ft">
                <input style=" background:url(${ctx}/images/login_v2/bgbtn.png) no-repeat; width:89px; height:29px; border:0px;" tabindex="4"  type="button" onclick="submitResult();"  style="border:0px; width:89px; height:29px;"/>
                </span></td>
            </tr>
          </table>
          </form>
        </DIV>
      </DIV>
    </DIV>
  </div>
</div>
<script type="text/javascript">
	function msgInfo(msg){
		alert(msg);
	}
</script>
<%session.removeAttribute("SPRING_SECURITY_LAST_EXCEPTION"); %>
</body>
</html>
