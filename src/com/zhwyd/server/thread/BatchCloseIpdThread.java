package com.zhwyd.server.thread;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.IpdServer;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.service.IpdServerService;
import com.zhwyd.server.service.impl.SystemLogService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
public class BatchCloseIpdThread implements Runnable {
    private int              ipdId;
    private IpdServerService ipdServerService = null;
    private HttpSession      session;

    public BatchCloseIpdThread(int ipdId, IpdServerService ipdServerService, HttpSession session) {
        this.ipdId = ipdId;
        this.ipdServerService = ipdServerService;
        this.session = session;
    }

    @Override
    public void run() {
        try {
            IpdServer ipdServer = ipdServerService.get(ipdId);
            StringBuilder stringBuilder = new StringBuilder();
            if (ipdServer == null) {
                stringBuilder.append("ID为：" + ipdId + "的Ipd不存在");
                this.updateSession(stringBuilder);
                return;
            }
            stringBuilder.append("名称" + ipdServer.getName() + ",ID:" + ipdServer.getId() + " ");
            Server server = CacheService.getServerById(ipdServer.getServerId());
            if (server == null) {
                stringBuilder.append("ID为：" + ipdServer.getServerId() + "的Server不存在");
                this.updateSession(stringBuilder);
                return;
            }
            Object[] killResult = SshxcuteUtils.killExec(server, ipdServer.getPath());
            if (killResult != null && killResult.length > 0 && (Integer)killResult[0] != CommanConstant.RESULT_TRUE_STATE) {
                stringBuilder.append(killResult[1].toString());
                this.updateSession(stringBuilder);
                return;
            }
            boolean result = SshxcuteUtils.progressIsExistNew(server, ipdServer.getPath(), Global.IPDMAIN_PID_FILE);
            if (!result) {
                ipdServer.setState(CommanConstant.STATE_STOP);
                ipdServerService.update(ipdServer);
                CacheService.getIpdServerById(ipdServer.getId()).setState(CommanConstant.STATE_STOP);
                stringBuilder.append("关闭成功");
            } else {
                stringBuilder.append("关闭失败");
            }
            this.updateSession(stringBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 记录session数据
     */
    public void updateSession(StringBuilder stringBuilder) {
        StringBuffer batchIpd = null;
        synchronized (this.session) {
            if (session.getAttribute("batchIpd") != null) {
                batchIpd = (StringBuffer) session.getAttribute("batchIpd");
                batchIpd.append(stringBuilder + "</br>");
            } else {
                batchIpd = new StringBuffer();
                batchIpd.append(stringBuilder + "</br>");
            }
            int batchIpdNum = 1;
            if (session.getAttribute("batchIpdNum") != null) {
                batchIpdNum = (Integer) session.getAttribute("batchIpdNum") + 1;
            }
            System.out.println("batchIpdNum=" + batchIpdNum);
            session.setAttribute("batchIpdNum", batchIpdNum);
            System.out.println("batchIpd=" + batchIpd);
            session.setAttribute("batchIpd", batchIpd);
        }
        SystemLogService.batchIpdLog(session, stringBuilder.toString());
    }
}
