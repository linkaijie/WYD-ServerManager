<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="java.util.List"%>
<%@ include file="/commons/taglibs.jsp"%>
<script language="javascript"  src="${ctx}/scripts/resize.js"></script>
<div class="table_left" id="table_left">
     <div class="moudle_div">
       <div class="sidebar_title">
         <div class="sidebar_title_border menu_title1">游戏管理</div>
       </div>
       <div class="div_content">
         <div class="wodeyingyong">
           <div id="invite_div">
 			 <div><a id="q_qun" class="qbgl_btn_10" href="${ctx}/gamechoose/chooseGame.action?gameId=1">枪魂</a></div>
 			 <div></div>
          </div>
          <div style="clear:both;"></div>
        </div>
      </div>
      <div class="sidebar_title">
         <div class="sidebar_title_border menu_title1">数据管理</div>
      </div>
      <div class="div_content">
         <div class="wodeyingyong">
           <div id="invite_div">
    		 <div><a id="q_qun" class="qbgl_btn_10" href="${ctx}/model/modelList.action">Model</a></div>
          </div>
          <div id="invite_div">
              <div><a id="q_qun" class="qbgl_btn_10" href="${ctx}/server/serverList.action">Server</a></div>
          </div>
          <div id="invite_div">
              <div><a id="q_qun" class="qbgl_btn_10" href="${ctx}/common/changeServerInput.action">迁服配置修改</a></div>
           </div>
           <div id="invite_div">
               <div><a id="q_qun" class="qbgl_btn_10" href="${ctx}/common/worldServerList.action">迁服管理</a></div>
            </div>
            <div id="invite_div">
            <div><a id="q_qun" class="qbgl_btn_10" href="${ctx}/redisconfig/redisConfigList.action">redis管理</a></div>
         </div>
          <div style="clear:both;"></div>
        </div>
     </div>
</div>
</div>

