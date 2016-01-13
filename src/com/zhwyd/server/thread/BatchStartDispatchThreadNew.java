package com.zhwyd.server.thread;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.bean.vo.BatchOpearVo;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.service.DispatchServerService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
public class BatchStartDispatchThreadNew implements Runnable {
    private int                   dispatchId;
    private DispatchServerService dispatchServerService = null;
    private HttpSession           session;

    public BatchStartDispatchThreadNew(int dispatchId, DispatchServerService dispatchServerService, HttpSession session) {
        this.dispatchId = dispatchId;
        this.dispatchServerService = dispatchServerService;
        this.session = session;
    }

    @Override
    public void run() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BatchOpearVo batchDispatchVo = new BatchOpearVo();
            batchDispatchVo.setId(dispatchId);
            DispatchServer dispatchServer = dispatchServerService.get(dispatchId);
            Server server = CacheService.getServerById(dispatchServer.getServerId());
            if (server == null) {
                batchDispatchVo.isFail("ID为：" + dispatchServer.getServerId() + "的Server不存在");
                CacheService.updateSession(session, batchDispatchVo, Global.BATCH_START_DISPATCH);
                return;
            }
            boolean result = this.doJob(server, dispatchServer, stringBuilder);
            if (!result) {
                batchDispatchVo.isFail(stringBuilder.toString());
                CacheService.updateSession(session, batchDispatchVo, Global.BATCH_START_DISPATCH);
                return;
            } else {
                batchDispatchVo.isSUCCESS(stringBuilder.toString());
                CacheService.updateSession(session, batchDispatchVo, Global.BATCH_START_DISPATCH);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stringBuilder = null;
        }
    }

    /**
     * 开启
     */
    public boolean doJob(Server server, DispatchServer dispatchServer, StringBuilder stringBuilder) throws Exception {
        boolean result = SshxcuteUtils.progressIsExistNew(server, dispatchServer.getPath(), Global.DISPATCH_PID_FILE);
        boolean startResult = false;
        if (result) {
            stringBuilder.append("服务已启动，请关闭重启");
            return startResult;
        }
        // 对应的world服务未启动
        if (CacheService.getWorldServerById(dispatchServer.getWorldId()).getState() != CommanConstant.STATE_START) {
            stringBuilder.append("World未启动");
            return startResult;
        }
        Object[] runResult = SshxcuteUtils.runExec(server, dispatchServer.getPath());
        // 启动服务出错
        if (runResult != null && runResult.length > 0 && (Integer) runResult[0] != CommanConstant.RESULT_TRUE_STATE) {
            stringBuilder.append("启动出错");
            SshxcuteUtils.killExec(server, dispatchServer.getPath());
            return startResult;
        }
        for (int i = 0; i < 200; i++) {// 获取启动日志
            Boolean bResult = SshxcuteUtils.getBatchStdout(session, dispatchServer, dispatchServerService, Global.DISPATCH_SUCCESS_RESULT);
            if (bResult != null) {
                if (bResult) {
                    stringBuilder.append("启动成功");
                    startResult = true;
                } else {
                    stringBuilder.append("启动失败 ");
                    // 启动失败时关闭进程
                    SshxcuteUtils.killExec(server, dispatchServer.getPath());
                }
                break;
            }
            Thread.sleep(1500);
            if (i >= 199) {
                stringBuilder.append("启动超时");
            }
        }
        return startResult;
    }
}
