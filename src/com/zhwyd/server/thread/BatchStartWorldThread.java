package com.zhwyd.server.thread;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.bean.WorldServer;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.service.WorldServerService;
import com.zhwyd.server.service.impl.SystemLogService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
public class BatchStartWorldThread implements Runnable {
    private int                 worldId;
    Map<Integer, List<Integer>> dispatchIdMap;
    private WorldServerService  worldServerService = null;
    private HttpSession         session;

    public BatchStartWorldThread(int accountId, WorldServerService worldServerService, HttpSession session, Map<Integer, List<Integer>> dispatchIdMap) {
        this.worldId = accountId;
        this.dispatchIdMap = dispatchIdMap;
        this.worldServerService = worldServerService;
        this.session = session;
    }

    @Override
    public void run() {
        try {
            WorldServer worldServer = worldServerService.get(worldId);
            StringBuilder stringBuilder = new StringBuilder();
            if (worldServer == null) {
                stringBuilder.append("ID为：" + worldId + "的World不存在");
                this.updateSession(stringBuilder);
                return;
            }
            if (worldServer.getIsDeploy() == CommanConstant.STATE_STOP) {
                stringBuilder.append("名称为" + worldServer.getName() + ",ID: " + worldId + "的World未部署");
                this.updateSession(stringBuilder);
                return;
            }
            stringBuilder.append("名称为" + worldServer.getName() + ",ID:" + worldServer.getId() + " ");
            Server server = CacheService.getServerById(worldServer.getServerId());
            if (server == null) {
                stringBuilder.append("ID为" + worldServer.getServerId() + "的Server不存在");
                this.updateSession(stringBuilder);
                return;
            }
            boolean result = SshxcuteUtils.progressIsExistNew(server, worldServer.getPath(), Global.WORLD_PID_FILE);
            this.doJob(server, worldServer, stringBuilder, result);
            this.updateSession(stringBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动相关操作
     */
    public void doJob(Server server, WorldServer worldServer, StringBuilder stringBuilder, boolean result) throws Exception {
        if (result) {
            stringBuilder.append("World服务已启动，请关闭重新启动! </br>");
            // SshExecutorUtils.killExec(server, worldServer.getPath());
            return;
        }
        Object[] runResult = SshxcuteUtils.runExec(server, worldServer.getPath());
        // 启动服务出错，关闭服务
        if (runResult != null && runResult.length > 0 && (Integer) runResult[0] != CommanConstant.RESULT_TRUE_STATE) {
            stringBuilder.append(runResult[1].toString());
            SshxcuteUtils.killExec(server, worldServer.getPath());
            return;
        }
        for (int i = 0; i < 120; i++) {
            Boolean bResult = this.getStdout(server, worldServer, stringBuilder);
            if (bResult != null) {
                if (bResult) {
                    stringBuilder.append("World服务启动成功! </br>");
                } else {
                    stringBuilder.append("World服务启动失败</br>");
                    SshxcuteUtils.killExec(server, worldServer.getPath());
                }
                break;
            }
            Thread.sleep(4000l);
            if (i >= 119) {
                stringBuilder.append("World服务启动超时</br>");
            }
        }
    }

    /**
     * 获取运行结果信息
     * 
     * @author:LKJ 2014-12-18
     * @return
     * @throws Exception
     */
    public Boolean getStdout(Server server, WorldServer worldServer, StringBuilder stringBuilder) throws Exception {
        String successStr = Global.WORLD_SUCCESS_RESULT;
        return SshxcuteUtils.getBatchStdout(session, worldServer, worldServerService, successStr);
    }

    /**
     * 记录session数据
     */
    public void updateSession(StringBuilder stringBuilder) {
        StringBuffer batchWorld = null;
        synchronized (this.session) {
            if (session.getAttribute("batchWorld") != null) {
                batchWorld = (StringBuffer) session.getAttribute("batchWorld");
                batchWorld.append(stringBuilder + "</br>");
            } else {
                batchWorld = new StringBuffer();
                batchWorld.append(stringBuilder + "</br>");
            }
            int batchWorldNum = 1;
            if (session.getAttribute("batchWorldNum") != null) {
                batchWorldNum = (Integer) session.getAttribute("batchWorldNum") + 1;
            }
            System.out.println("batchWorldNum=" + batchWorldNum);
            session.setAttribute("batchWorldNum", batchWorldNum);
            System.out.println("batchWorld=" + batchWorld);
            session.setAttribute("batchWorld", batchWorld);
        }
        SystemLogService.batchWorldLog(session, stringBuilder.toString());
        stringBuilder = null;
        batchWorld = null;
    }
}
