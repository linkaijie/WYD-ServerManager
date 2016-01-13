package com.zhwyd.server.thread;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import com.zhwyd.server.bean.IpdServer;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.service.IpdServerService;
import com.zhwyd.server.service.impl.SystemLogService;
import com.zhwyd.server.ssh.util.SshExecutorUtils;
public class BatchStartIpdThread implements Runnable {
    private int              ipdId;
    private IpdServerService ipdServerService = null;
    private HttpSession      session;

    public BatchStartIpdThread(int ipdId, IpdServerService ipdServerService, HttpSession session) {
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
            boolean result = SshExecutorUtils.progressIsExistNew(server, ipdServer.getPath(), Global.IPDMAIN_PID_FILE);
            this.doRun(server, ipdServer, stringBuilder, result);
            this.updateSession(stringBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doRun(Server server, IpdServer ipdServer, StringBuilder stringBuilder, boolean result) throws Exception {
        if (!result) {
            Map<String, Object> resultMap = SshExecutorUtils.runExec(server, ipdServer.getPath());
            if (resultMap != null && resultMap.get("outErr") != null && !("").equals(resultMap.get("outErr"))) {
                stringBuilder.append(resultMap.get("outErr").toString().replace("</br>", ""));
                SshExecutorUtils.killExec(server, ipdServer.getPath());
                this.updateSession(stringBuilder);
                return;
            }
            String stdout = "";
            for (int i = 0; i < 90; i++) {
                stdout = this.getStdout(server, ipdServer, stringBuilder);
                if (!StringUtils.isEmpty(stdout) && (stdout.equals("true") || stdout.equals("false"))) {
                    if (stdout.equals("true")) {
                        stringBuilder.append("Ipd服务启动成功! </br>");
                    } else if (stdout.equals("false")) {
                        stringBuilder.append("===========Ipd服务启动失败=========== </br>");
                        SshExecutorUtils.killExec(server, ipdServer.getPath());
                    }
                    break;
                }
                Thread.sleep(2000);
                if (i == 89) {
                    stringBuilder.append("===========Ipd服务启动超时=========== </br>");
                }
            }
        } else {
            stringBuilder.append("服务已启动，请关闭重新启动! </br>");
        }
    }

    /**
     * 获取运行结果信息
     * 
     * @author:LKJ 2014-12-18
     * @return
     * @throws Exception
     */
    public String getStdout(Server server, IpdServer ipdServer, StringBuilder stringBuilder) throws Exception {
        String successStr = "服务分区公告器启动";
        String result = SshExecutorUtils.getBatchStdout(session, ipdServer, ipdServerService, stringBuilder, successStr);
        return result;
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
