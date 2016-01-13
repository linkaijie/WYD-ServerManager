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
public class BatchCloseDispatchThreadNew implements Runnable {
    private int                   dispatchId;
    private DispatchServerService dispatchServerService = null;
    private HttpSession           session;

    public BatchCloseDispatchThreadNew(int dispatchId, DispatchServerService dispatchServerService, HttpSession session) {
        this.dispatchId = dispatchId;
        this.dispatchServerService = dispatchServerService;
        this.session = session;
    }

    @Override
    public void run() {
        try {
            BatchOpearVo batchDispatchVo = new BatchOpearVo();
            batchDispatchVo.setId(dispatchId);
            DispatchServer dispatchServer = dispatchServerService.get(dispatchId);
            if (dispatchServer == null) {
                batchDispatchVo.isNotExist("dispatchServer");
                CacheService.updateSession(session, batchDispatchVo, Global.BATCH_CLOSE_DISPATCH);
                return;
            }
            if (dispatchServer.getIsDeploy() == CommanConstant.STATE_STOP || dispatchServer.getIsUse() == CommanConstant.STATE_STOP) {
                batchDispatchVo.isFail("未部署或未启用");
                CacheService.updateSession(session, batchDispatchVo, Global.BATCH_CLOSE_DISPATCH);
                return;
            }
            Server server = CacheService.getServerById(dispatchServer.getServerId());
            if (server == null) {
                batchDispatchVo.isFail("ID为" + dispatchServer.getServerId() + "的Server不存在");
                CacheService.updateSession(session, batchDispatchVo, Global.BATCH_CLOSE_DISPATCH);
                return;
            }
            // 关闭Dispatch服务
            Object[] killResult = SshxcuteUtils.killExec(server, dispatchServer.getPath());
            if (killResult != null && killResult.length > 0 && (Integer) killResult[0] != CommanConstant.RESULT_TRUE_STATE) {
                batchDispatchVo.isFail("關閉出错");
                CacheService.updateSession(session, batchDispatchVo, Global.BATCH_CLOSE_DISPATCH);
                return;
            }
            // boolean result = SshxcuteUtils.progressIsExistNew(server, dispatchServer.getPath(), Global.DISPATCH_PID_FILE);// 检测线程是否存在，经检验，可不测
            // if (!result) {
            dispatchServer.setState(CommanConstant.STATE_STOP);
            dispatchServerService.update(dispatchServer);
            CacheService.getDispatchServerById(dispatchServer.getId()).setState(CommanConstant.STATE_STOP);
            batchDispatchVo.isSUCCESS("關閉成功");
            CacheService.updateSession(session, batchDispatchVo, Global.BATCH_CLOSE_DISPATCH);
            // } else {
            // batchDispatchVo.isFail("關閉出错");
            // CacheService.updateSession(session, batchDispatchVo, Global.BATCH_CLOSE_DISPATCH);
            // }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.updateSession();
        }
    }

    /**
     * 记录session数据
     */
    public void updateSession() {
        synchronized (this.session) {
            int batchDispatchNum = 1;
            if (session.getAttribute("batchDispatchNum") != null) {
                batchDispatchNum = (Integer) session.getAttribute("batchDispatchNum") + 1;
            }
            System.out.println("batchDispatchNum=" + batchDispatchNum);
            session.setAttribute("batchDispatchNum", batchDispatchNum);
        }
    }
}
