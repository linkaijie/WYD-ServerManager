<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="java.util.List"%>
<%@ include file="/commons/taglibs.jsp"%>
<script language="javascript"  src="${ctx}/scripts/resize.js"></script>
<div class="table_left" id="table_left">
     <div class="moudle_div">
       <div class="sidebar_title">
         <div class="sidebar_title_border menu_title1"><s:text name="SystemManagement"/></div>
       </div>
       <div class="div_content">
         <div class="wodeyingyong">
           <div id="invite_div">
			 	<div><a id="q_qun" class="btn_sys_1" href="${ctx}/user/userList.action">用户管理</a></div>
			 	<div><a id="q_qun" class="qbgl_btn_10" href="${ctx}/server/serverList.action">Server</a></div>
			 	<div><a id="q_qun" class="qbgl_btn_10" href="${ctx}/mail/mailList.action">邮件</a></div>
			 <!-- 
             	<div><a id="q_qun" class="btn_sys_3" href="${ctx}/role/pageList.action">角色管理 </a></div>
             	<div><a id="q_qun" class="btn_sys_4" href="${ctx}/department/listPage.action">部门管理 </a></div>
             	<div><a id="q_qun" class="btn_sys_5" href="${ctx}/resource/listPage.action">资源管理</a></div>
			  -->
          </div>
          <div style="clear:both;"></div>
        </div>
      </div>
</div>
</div>

