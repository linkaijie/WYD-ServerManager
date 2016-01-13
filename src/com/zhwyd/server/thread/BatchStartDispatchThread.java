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
public class BatchStartDispatchThread implements Runnable {
    private int                   dispatchId;
    private DispatchServerService dispatchServerService = null;
    private HttpSession           session;

    public BatchStartDispatchThread(int dispatchId, DispatchServerService dispatchServerService, HttpSession session) {
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
                stringBuilder.append("ID为：" + dispatchId + "的Dispatch服务不存在</br>");
                this.updateSession(stringBuilder);
                return;
            }
            if (dispatchServer.getIsDeploy() == CommanConstant.STATE_STOP) {
                stringBuilder.append("名称为：" + dispatchServer.getName() + ",ID:" + dispatchId + "的Dispatch服务未部署</br>");
                this.updateSession(stringBuilder);
                return;
            }
            stringBuilder.append("名称为：" + dispatchServer.getName() + ",ID:" + dispatchServer.getId() + " ");
            Server server = CacheService.getServerById(dispatchServer.getServerId());
            if (server == null) {
                stringBuilder.append("ID为：" + dispatchServer.getServerId() + "的Server不存在");
                this.updateSession(stringBuilder);
                return;
            }
            boolean result = SshxcuteUtils.progressIsExistNew(server, dispatchServer.getPath(), Global.DISPATCH_PID_FILE);
            System.out.println("result=" + result);
            this.doJob(server, dispatchServer, stringBuilder, result);
            this.updateSession(stringBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stringBuilder = null;
        }
    }

    public void doJob(Server server, DispatchServer dispatchServer, StringBuilder stringBuilder, boolean result) throws Exception {
        System.out.println("doJob=" + server.getServerIp());
        if (result) {
            stringBuilder.append("Dispatch服务已启动，请关闭重新启动! </br>");
            this.updateSession(stringBuilder);
            return;
        }
        // 对应的world服务未启动
        if (CacheService.getWorldServerById(dispatchServer.getWorldId()).getState() != CommanConstant.STATE_START) {
            stringBuilder.append("<font color='red'>ID为【" + dispatchServer.getWorldId() + "】的World服务未启动，请重新启动后再试!</font> </br>");
            this.updateSession(stringBuilder);
            return;
        }
        Object[] runResult = SshxcuteUtils.runExec(server, dispatchServer.getPath());
        // 启动服务出错
        if (runResult != null && runResult.length > 0 && (Integer) runResult[0] != CommanConstant.RESULT_TRUE_STATE) {
            stringBuilder.append(runResult[1].toString());
            SshxcuteUtils.killExec(server, dispatchServer.getPath());
            this.updateSession(stringBuilder);
            return;
        }
        for (int i = 0; i < 120; i++) {
            Boolean bResult = this.getStdout(server, dispatchServer, stringBuilder);
            if (bResult != null) {
                if (bResult) {
                    stringBuilder.append("Dispatch服务启动成功! </br>");
                } else {
                    stringBuilder.append("Dispatch服务启动失败 </br>");
                    // 启动失败时关闭进程
                    SshxcuteUtils.killExec(server, dispatchServer.getPath());
                }
                break;
            }
            Thread.sleep(4000);
            if (i >= 119) {
                stringBuilder.append("Dispatch服务启动超时 </br>");
            }
        }
    }

    /**
     * 获取运行结果信息
     * 
     * @author:LKJ 2014-12-18
     * @throws Exception
     */
    public Boolean getStdout(Server server, DispatchServer dispatchServer, StringBuilder stringBuilder) throws Exception {
        String successStr = Global.DISPATCH_SUCCESS_RESULT;
        return SshxcuteUtils.getBatchStdout(session, dispatchServer, dispatchServerService, successStr);
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
        batchDispatch = null;
    }
}
