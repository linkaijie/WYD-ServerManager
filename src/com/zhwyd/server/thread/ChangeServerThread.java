package com.zhwyd.server.thread;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.vo.ChangeServerVo;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.service.CommonService;
import com.zhwyd.server.service.impl.SystemLogService;
public class ChangeServerThread implements Runnable {
    private HttpSession session;
    private String      worldIds;
    private int         serverId;
    private int         dispatchServerId;

    public ChangeServerThread(HttpSession session, String worldIds, int serverId, int dispatchServerId) {
        this.session = session;
        this.worldIds = worldIds;
        this.serverId = serverId;
        this.dispatchServerId = dispatchServerId;
    }

    @Override
    public void run() {
        try {
            session.removeAttribute(Global.CHANGE_SERVER_VO_LIST);
            session.removeAttribute("changeServerError");
            CommonService commonService = Application.getBean(CommonService.class);
            boolean result = false;
            ChangeServerVo changeServerVo = null;
            String[] ids = worldIds.split(",");
            List<ChangeServerVo> changeServerVoList = new ArrayList<ChangeServerVo>();
            session.setAttribute(Global.CHANGE_SERVER_VO_LIST, changeServerVoList);
            for (int i = 0; i < ids.length; i++) {
                changeServerVo = new ChangeServerVo();
                changeServerVoList.add(changeServerVo);
                int beanId = Integer.valueOf(ids[i]);
                changeServerVo.setId(beanId);
                changeServerVo.setServerId(serverId);
                changeServerVo.setDispatchServerId(dispatchServerId);
                changeServerVo.setRemark("开始迁移。。");
                // scp拷貝文件
                result = commonService.scpFileToRemote(changeServerVo, session);
                if (!result) {
                    session.setAttribute("changeServerError", "scp迁移文件时出错，请检查");
                    break;
                }
                // 修改DB數據
                result = commonService.changeOneServerDBCfg(changeServerVo, session);
                if (!result) {
                    session.setAttribute("changeServerError", "修改数据库数据时出错，请检查");
                    break;
                }
                // 同步配置文件
                result = commonService.synRemoteCfg(changeServerVo, session);
                if (!result) {
                    session.setAttribute("changeServerError", "同步远程配置文件时出错，请检查");
                    break;
                } else {
                    changeServerVo.setRemark("迁服成功");
                }
            }
            SystemLogService.serverManegeLog(session, ":迁移worldId为" + worldIds + "的服务");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
