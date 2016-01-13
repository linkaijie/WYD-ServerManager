package com.zhwyd.server.sshxcute;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.sshxcute.pool.ConnectionPoolManager;
import net.neoremind.sshxcute.core.IOptionName;
import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
public class SSHExces {
    public SSHExces() {
    }

    /**
     * 执行命令
     * 
     * @param server
     * @param customTasks
     * @return
     */
    public static Object[] exce(Server server, CustomTask customTasks) {
        return SSHExces.exce(server, customTasks, true);
    }

    /**
     * 执行命令
     * 
     * @param server
     * @param customTasks
     * @return
     */
    public static Object[] exce(Server server, CustomTask customTasks, boolean isPrintResult) {
        System.out.println("command=" + customTasks.getCommand());
        Object[] execResult = new Object[2];
        SSHExec sshExec = null;
        try {
            SSHExec.setOption(IOptionName.HALT_ON_FAILURE, true);
            sshExec = ConnectionPoolManager.getInstance().getConnection(server.getServerIp());
            Result res = sshExec.exec(customTasks);
            if (res.isSuccess) {
                execResult[0] = res.rc;
                execResult[1] = res.sysout;
            } else {
                execResult[0] = res.rc;
                execResult[1] = res.error_msg;
            }
            if (isPrintResult) {
                System.out.println("execResult[0]=" + execResult[0]);
                System.out.println("execResult[1]=" + execResult[1]);
            }
        } catch (TaskExecFailException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionPoolManager.getInstance().close(server.getServerIp(), sshExec);
        }
        return execResult;
    }
}
