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
public class BatchCloseAccountThread implements Runnable {
    private int                  accountId;
    private AccountServerService accountServerService = null;
    private HttpSession          session;

    public BatchCloseAccountThread(int accountId, AccountServerService accountServerService, HttpSession session) {
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
            Object[] killResult = SshxcuteUtils.killExec(server, accountServer.getPath());
            if (killResult != null && killResult.length > 0 && (Integer)killResult[0] != CommanConstant.RESULT_TRUE_STATE) {
                stringBuilder.append(killResult[1].toString());
                this.updateSession(stringBuilder);
                return;
            }
            boolean result = SshxcuteUtils.progressIsExistNew(server, accountServer.getPath(), Global.ACCOUNT_PID_FILE);
            if (!result) {
                accountServer.setState(CommanConstant.STATE_STOP);
                accountServerService.update(accountServer);
                CacheService.getAccountServerById(accountServer.getId()).setState(CommanConstant.STATE_STOP);
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
