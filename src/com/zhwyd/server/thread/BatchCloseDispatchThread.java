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
public class BatchCloseDispatchThread implements Runnable {
    private int                   dispatchId;
    private DispatchServerService dispatchServerService = null;
    private HttpSession           session;

    public BatchCloseDispatchThread(int dispatchId, DispatchServerService dispatchServerService, HttpSession session) {
        this.dispatchId = dispatchId;
        this.dispatchServerService = dispatchServerService;
        this.session = session;
    }

    @Override
    public void run() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            DispatchServer dispatchServer = dispatchServerService.get(dispatchId);
            if (dispatchServer == null) {
                stringBuilder.append("ID为" + dispatchId + "DispatchServer不存在");
                this.updateSession(stringBuilder);
                return;
            }
            if (dispatchServer.getIsDeploy() == CommanConstant.STATE_STOP || dispatchServer.getIsUse() == CommanConstant.STATE_STOP) {
                stringBuilder.append("ID为" + dispatchId + "DispatchServer未部署或未启用");
                this.updateSession(stringBuilder);
                return;
            }
            stringBuilder.append("名称" + dispatchServer.getName() + ",ID:" + dispatchServer.getId() + "的Dispatch");
            Server server = CacheService.getServerById(dispatchServer.getServerId());
            if (server == null) {
                stringBuilder.append("ID为" + dispatchServer.getServerId() + "Server不存在");
                this.updateSession(stringBuilder);
                return;
            }
            // 关闭Dispatch服务
            Object[] killResult = SshxcuteUtils.killExec(server, dispatchServer.getPath());
            if (killResult != null && killResult.length > 0 && (Integer) killResult[0] != CommanConstant.RESULT_TRUE_STATE) {
                stringBuilder.append(killResult[1].toString());
                this.updateSession(stringBuilder);
                return;
            }
            boolean result = SshxcuteUtils.progressIsExistNew(server, dispatchServer.getPath(), Global.DISPATCH_PID_FILE);
            if (!result) {
                dispatchServer.setState(CommanConstant.STATE_STOP);
                dispatchServerService.update(dispatchServer);
                CacheService.getDispatchServerById(dispatchServer.getId()).setState(CommanConstant.STATE_STOP);
                stringBuilder.append("关闭成功");
            } else {
                stringBuilder.append("关闭失败");
            }
            this.updateSession(stringBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stringBuilder = null;
        }
    }

    /**
     * 记录session数据
     */
    public void updateSession(StringBuilder stringBuilder) {
        StringBuffer batchDispatch = null;
        synchronized (this.session) {
            if (session.getAttribute("batchDispatch") != null) {
                batchDispatch = (StringBuffer) session.getAttribute("batchDispatch");
                batchDispatch.append(stringBuilder + "</br>");
            } else {
                batchDispatch = new StringBuffer();
                batchDispatch.append(stringBuilder + "</br>");
            }
            int batchDispatchNum = 1;
            if (session.getAttribute("batchDispatchNum") != null) {
                batchDispatchNum = (Integer) session.getAttribute("batchDispatchNum") + 1;
            }
            System.out.println("batchDispatchNum=" + batchDispatchNum);
            session.setAttribute("batchDispatchNum", batchDispatchNum);
            System.out.println("batchDispatch=" + batchDispatch);
            session.setAttribute("batchDispatch", batchDispatch);
        }
        SystemLogService.batchDispatchLog(session, stringBuilder.toString());
    }
}
