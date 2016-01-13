<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/context/commons/taglibs.jsp"%>
<div class="navbar">
	<div class="navbar-inner">
		<div id="navigation_logo">
			<img src="images/sys_logo.png" />
		</div>
		<span class="brand">枪魂数据后台</span>
		<div id="navigation_button">
			用户名：<a href="#"><security:authentication property="name" /></a> | <a href="j_spring_security_logout">注销</a> &nbsp;<a href="admin/user!edit.action">修改密码</a>
		</div>
	</div>
</div>