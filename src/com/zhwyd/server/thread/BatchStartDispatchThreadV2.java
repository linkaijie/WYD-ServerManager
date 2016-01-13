package com.zhwyd.server.thread;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.service.DispatchServerService;
import com.zhwyd.server.service.impl.SystemLogService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
/**
 * 停用
 * 
 * @author Administrator
 */
public class BatchStartDispatchThreadV2 implements Runnable {
    private int                   dispatchId;
    private DispatchServerService dispatchServerService = null;
    private HttpSession           session;

    public BatchStartDispatchThreadV2(int dispatchId, DispatchServerService dispatchServerService, HttpSession session) {
        this.dispatchId = dispatchId;
        this.dispatchServerService = dispatchServerService;
        this.session = session;
    }

    @Override
    public void run() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            DispatchServer dispatchServer = dispatchServerService.get(dispatchId);
            this.clearSession(dispatchServer.getWorldId());
            stringBuilder.append("名称为：" + dispatchServer.getName() + ",ID:" + dispatchServer.getId() + " ");
            Server server = CacheService.getServerById(dispatchServer.getServerId());
            if (server == null) {
                stringBuilder.append("ID为：" + dispatchServer.getServerId() + "的Server不存在");
                this.updateSession(stringBuilder, dispatchServer.getWorldId());
                return;
            }
            boolean result = SshxcuteUtils.progressIsExistNew(server, dispatchServer.getPath(), Global.DISPATCH_PID_FILE);
            this.doJob(server, dispatchServer, stringBuilder, result);
            this.updateSession(stringBuilder, dispatchServer.getWorldId());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stringBuilder = null;
        }
    }

    public void doJob(Server server, DispatchServer dispatchServer, StringBuilder stringBuilder, boolean result) throws Exception {
        if (result) {
            stringBuilder.append("<font color='red'>Dispatch服务已启动，请关闭重新启动!</font> </br>");
            return;
        }
        // 对应的world服务未启动
        if (CacheService.getWorldServerById(dispatchServer.getWorldId()).getState() != CommanConstant.STATE_START) {
            stringBuilder.append("<font color='red'>ID为【" + dispatchServer.getWorldId() + "】的World服务未启动，请重新启动后再试!</font> </br>");
            return;
        }
        Object[] runResult = SshxcuteUtils.runExec(server, dispatchServer.getPath());
        // 启动服务出错
        if (runResult != null && runResult.length > 0 && (Integer) runResult[0] != CommanConstant.RESULT_TRUE_STATE) {
            stringBuilder.append(runResult[1].toString());
            SshxcuteUtils.killExec(server, dispatchServer.getPath());
            return;
        }
        for (int i = 0; i < 180; i++) {
            String successStr = Global.DISPATCH_SUCCESS_RESULT;
            Boolean bResult = SshxcuteUtils.getBatchStdout(session, dispatchServer, dispatchServerService, successStr);
            if (bResult != null) {
                if (bResult) {
                    stringBuilder.append("<font color='green'>Dispatch服务启动成功! </font></br>");
                } else {
                    stringBuilder.append("<font color='red'>Dispatch服务启动失败 </font></br>");
                    // 启动失败时关闭进程
                    SshxcuteUtils.killExec(server, dispatchServer.getPath());
                }
                break;
            }
            Thread.sleep(2000);
            if (i >= 179) {
                stringBuilder.append("<font color='red'>Dispatch服务启动超时 </font></br>");
            }
        }
    }

    /**
     * 记录session数据
     */
    public void updateSession(StringBuilder stringBuilder, int worldId) {
        StringBuffer batchDispatch = null;
        synchronized (this.session) {
            if (session.getAttribute("batchDispatch") != null) {
                batchDispatch = (StringBuffer) session.getAttribute("batchDispatch");
                batchDispatch.append(stringBuilder + "</br>");
            } else {
                batchDispatch = new StringBuffer();
                batchDispatch.append(stringBuilder + "</br>");
            }
            session.setAttribute("batchDispatch", batchDispatch);
            int batchDispatchNum = 1;
            if (session.getAttribute("batchDispatchNum" + worldId) != null) {
                batchDispatchNum = (Integer) session.getAttribute("batchDispatchNum" + worldId) + 1;
            }
            System.out.println("batchDispatchNum" + worldId + "=" + batchDispatchNum);
            session.setAttribute("batchDispatchNum" + worldId, batchDispatchNum);
        }
        SystemLogService.batchDispatchLog(session, stringBuilder.toString());
        batchDispatch = null;
    }

    public void clearSession(int worldId) {
        session.removeAttribute("batchDispatchNum" + worldId);
    }
}
