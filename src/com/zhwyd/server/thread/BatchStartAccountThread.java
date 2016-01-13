package com.zhwyd.server.thread;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.AccountServer;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.service.AccountServerService;
import com.zhwyd.server.service.impl.SystemLogService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
public class BatchStartAccountThread implements Runnable {
    private int                  accountId;
    private AccountServerService accountServerService = null;
    private HttpSession          session;

    public BatchStartAccountThread(int accountId, AccountServerService accountServerService, HttpSession session) {
        this.accountId = accountId;
        this.accountServerService = accountServerService;
        this.session = session;
    }

    @Override
    public void run() {
        try {
            AccountServer accountServer = accountServerService.get(accountId);
            StringBuilder stringBuilder = new StringBuilder();
            if (accountServer == null) {
                stringBuilder.append("ID为：" + accountId + "的Account不存在");
                this.updateSession(stringBuilder);
                return;
            }
            Server server = CacheService.getServerById(accountServer.getServerId());
            if (server == null) {
                stringBuilder.append("ID为：" + accountServer.getServerId() + "的Server不存在");
                this.updateSession(stringBuilder);
                return;
            }
            stringBuilder.append("名称" + accountServer.getName() + ",ID:" + accountServer.getId() + " ");
            boolean result = SshxcuteUtils.progressIsExistNew(server, accountServer.getPath(), Global.ACCOUNT_PID_FILE);
            this.doRun(server, accountServer, stringBuilder, result);
            this.updateSession(stringBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doRun(Server server, AccountServer accountServer, StringBuilder stringBuilder, boolean result) throws Exception {
        if (!result) {
            Object[] runResult = SshxcuteUtils.runExec(server, accountServer.getPath());
            if (runResult != null && runResult.length > 0 && (Integer) runResult[0] != CommanConstant.RESULT_TRUE_STATE) {
                stringBuilder.append(runResult[1].toString());
                SshxcuteUtils.killExec(server, accountServer.getPath());
                this.updateSession(stringBuilder);
                return;
            }
            for (int i = 0; i < 90; i++) {
                Boolean bResult = this.getStdout(server, accountServer, stringBuilder);
                if (bResult != null) {
                    if (bResult) {
                        stringBuilder.append("Account服务启动成功! </br>");
                    } else {
                        stringBuilder.append("===========Account服务启动失败=========== </br>");
                        SshxcuteUtils.killExec(server, accountServer.getPath());
                    }
                    break;
                }
                Thread.sleep(2000);
                if (i == 89) {
                    stringBuilder.append("===========Account服务启动超时=========== </br>");
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
    public Boolean getStdout(Server server, AccountServer accountServer, StringBuilder stringBuilder) throws Exception {
        String successStr = "认证服务器启动";
        return SshxcuteUtils.getBatchStdout(session, accountServer, accountServerService, successStr);
    }

    /**
     * 记录session数据
     */
    public void updateSession(StringBuilder stringBuilder) {
        StringBuffer batchAccount = null;
        synchronized (this.session) {
            if (session.getAttribute("batchAccount") != null) {
                batchAccount = (StringBuffer) session.getAttribute("batchAccount");
                batchAccount.append(stringBuilder + "</br>");
            } else {
                batchAccount = new StringBuffer();
                batchAccount.append(stringBuilder + "</br>");
            }
            int batchAccountNum = 1;
            if (session.getAttribute("batchAccountNum") != null) {
                batchAccountNum = (Integer) session.getAttribute("batchAccountNum") + 1;
            }
            System.out.println("batchAccountNum=" + batchAccountNum);
            session.setAttribute("batchAccountNum", batchAccountNum);
            System.out.println("batchAccount=" + batchAccount);
            session.setAttribute("batchAccount", batchAccount);
        }
        SystemLogService.batchAccountLog(session, stringBuilder.toString());
    }
}
