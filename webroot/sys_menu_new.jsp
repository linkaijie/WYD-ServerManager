<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="java.util.List"%>
<%@ include file="/commons/taglibs.jsp"%>
<script src="${widgethome}/open/lhgdialog.js" type=text/javascript></script>


<script type="text/javascript" src="${ctx}/scripts/dist/css/bootstrap.min.css"></script>
<link rel="stylesheet" href="http://www.bootcss.com/p/layoutit/css/bootstrap-combined.min.css">
<script src="${ctx}/scripts/dist/js/bootstrap.min.js" type=text/javascript></script>



<script type="text/javascript">
	var ctx = "${ctx}";
	function changePwd(){
		lhgdialog.opendlg('修改密码',ctx+"/user/userPwdForm.action",480,200,true,true);
	}
</script>
<div class="top">
	<!--<div id="logo" title="开服工具"></div>-->
	<div id="dishi">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="select_dishi">&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2"></td>
			</tr>
		</table>
	</div>
	<!--
	<div class="userinfo">
		<table width="100%" border="0" cellspacing="0" cellpadding="5">
			<tr>
				<td>
					<a href="javascript:changePwd();" id="icon2">修改密码 </a>&nbsp;&nbsp;|&nbsp;&nbsp;
					<a href="${ctx}/user/userList.action" class="nav" id="tab2"><span>系统管理</span></a>&nbsp;&nbsp;|&nbsp;&nbsp;
					<a href="${ctx}/j_spring_security_logout" id="icon2">退出系统 </a>&nbsp;&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
		</table>
	</div>
	-->
</div>

<div class="container-fluid">
<div class="row-fluid">
    <div class="span12">
        <div class="navbar">
            <div class="navbar-inner">
                <div class="container-fluid">
                     <a data-target=".navbar-responsive-collapse" data-toggle="collapse" class="btn btn-navbar"><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></a> <a href="#" class="brand">网站名</a>
                    <div class="nav-collapse collapse navbar-responsive-collapse">
                        <ul class="nav">
                            <li class="active">
                                <a href="#">主页</a>
                            </li>
                            <li>
                                <a href="#">链接</a>
                            </li>
                            <li>
                                <a href="#">链接</a>
                            </li>
                            <li class="dropdown">
                                 <a data-toggle="dropdown" class="dropdown-toggle" href="#">系统管理<strong class="caret"></strong></a>
                                <ul class="dropdown-menu">
                                    <li>
                                        <a href="javascript:changePwd();" id="icon2">修改密码 </a>
                                    </li>
                                    <li>
                                        <a href="${ctx}/user/userList.action" class="nav" id="tab2"><span>系统管理</span></a>
                                    </li>
                                    <li>
                                        <a href="#">其他</a>
                                    </li>
                                    <li class="divider">
                                    </li>
                                    <li class="nav-header">
                                                                                                                        标签
                                    </li>
                                    <li>
                                        <a href="#">链接1</a>
                                    </li>
                                    <li>
                                        <a href="#">链接2</a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                        <ul class="nav pull-right">
                            <li>
                                <a href="#">右边链接</a>
                            </li>
                            <li class="divider-vertical">
                            </li>
                            <li class="dropdown">
                                 <a data-toggle="dropdown" class="dropdown-toggle" href="#">系统管理<strong class="caret"></strong></a>
                                <ul class="dropdown-menu">
                                    <li>
                                        <a href="javascript:changePwd();" id="icon2">修改密码 </a>
                                    </li>
                                    <li>
                                        <a href="${ctx}/user/userList.action" class="nav" id="tab2"><span>系统管理</span></a>
                                    </li>
                                    <li>
                                        <a href="#">其他</a>
                                    </li>
                                        </ul>
                                    </li>
                                </ul>
                    </div>
                    
                </div>
            </div>
            
        </div>
    </div>
</div>
</div>

