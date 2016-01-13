<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
    String addressName=application.getInitParameter("addressName");
%>
<title><%=addressName%>-珠海网易达电子科技发展有限公司 </title>
<%@ include file="/commons/meta.jsp"%>
<link href="${ctx}/styles/main.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/styles/buttons.css" rel="stylesheet" type="text/css" />
<script language="javascript"  src="${jshome}/jquery-1.7.2.min.js"></script>
<style type="text/css">
#Map_info_div {
	height: 320px;
	width:100%;
	_width:498px;
	overflow-y:scroll;
}

.t1 {background-color:#fff;}/* 第一行的背景色 */
.t2 {background-color:#f5f5f5;}/* 第二行的背景色 */
.t3 {background-color:#e3efff;}/* 鼠标经过时的背景色 */

</style>
<script type="text/javascript">
var ctx = "${ctx}";
window.onload = function(){
	if(document.getElementById("tab0")!=null){
		document.getElementById("tab0").className = "nav hover";
	}
};
</script>

</head>
<body>
<jsp:include flush="false" page="/sys_menu.jsp"/>
	<div style="height: 10px;"></div>
	<div class="table_div" >
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
		  <tr>
		    <td width="17" valign="top" background="${ctx}/images/mail_leftbg.gif"><img src="${ctx}/images/left-top-right.gif" width="17" height="29" /></td>
		    <td valign="top">
		    <table width="100%" height="31" border="0" cellpadding="0" cellspacing="0" class="left_topbg" id="table2">
		        <tr>
		          <td height="31"></td>
		        </tr>
		    </table>
		    </td>
		    <td width="16" valign="top" background="${ctx}/images/mail_rightbg.gif"><img src="${ctx}/images/nav-right-bg.gif" width="16" height="29" /></td>
		  </tr>
		  <tr>
		    <td valign="middle" background="${ctx}/images/mail_leftbg.gif">&nbsp;</td>
		    <td valign="top" bgcolor="#fbfbfb">
		    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		        <tr>
		          <td colspan="3" valign="top">&nbsp;</td>
		        </tr>
		        <tr align="center">
		            <td rowspan="5" valign="top">
		                <a href="http://180.150.188.84:8080/WYD-ServerManager2" class="button button-glow button-border button-rounded button-primary">混服</a>
		                <a href="http://qztest.ttqz.lo97.com:8080/WYD-ServerManager" class="button button-glow button-border button-rounded button-primary">IOS</a>
		                <a href="http://qztwtest.1758play.com:8080/WYD-ServerManager" class="button button-glow button-border button-rounded button-primary">台湾</a>
		                <a href="http://qztest.zwvn.tdp-vn.com:8080/WYD-ServerManager" class="button button-glow button-border button-rounded button-primary">越南</a>
		            </td>
                </tr>
		    </table>
		    </td>
		    <td background="${ctx}/images/mail_rightbg.gif">&nbsp;</td>
		  </tr>
		  <tr>
		    <td valign="bottom" background="${ctx}/images/mail_leftbg.gif"><img src="${ctx}/images/buttom_left2.gif" width="17" height="17" /></td>
		    <td background="${ctx}/images/buttom_bgs.gif"><img src="${ctx}/images/buttom_bgs.gif" width="17" height="17" /></td>
		    <td valign="bottom" background="${ctx}/images/mail_rightbg.gif"><img src="${ctx}/images/buttom_right2.gif" width="16" height="17" /></td>
		  </tr>
		</table>
	</div>
</body>
</html>
