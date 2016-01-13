package com.zhwyd.server.sshxcute;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.sshxcute.pool.ConnectionPoolManager;
import net.neoremind.sshxcute.core.IOptionName;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
public class UploadDataToServer {
    public UploadDataToServer() {
    }

    public void exce(int type, Server server, String fromLocalDir, String toServerDir) {
        SSHExec sshExec = null;
        try {
            SSHExec.setOption(IOptionName.HALT_ON_FAILURE, true);
            sshExec = ConnectionPoolManager.getInstance().getConnection(server.getServerIp());
            if (type == CommanConstant.STATE_UPLOAD_SINGLE) {
                sshExec.uploadSingleDataToServer(fromLocalDir, toServerDir);
            } else {
                sshExec.uploadAllDataToServer(fromLocalDir, toServerDir);
            }
        } catch (TaskExecFailException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionPoolManager.getInstance().close(server.getServerIp(), sshExec);
        }
    }
    
    public static void main(String[] args) {
        Server server = CacheService.getServerById(2);
        UploadDataToServer uploadDataToServer = new  UploadDataToServer();
        uploadDataToServer.exce(CommanConstant.STATE_UPLOAD_ALL, server, "D://logs//gunsoul_report", "/data/apps/modeltest/");
        
    }
    
    public static void upload(String serverId, String localPath, String toPath) {
        UploadDataToServer uploadDataToServer = new  UploadDataToServer();
        Server server = CacheService.getServerById(Integer.valueOf(serverId));
        uploadDataToServer.exce(CommanConstant.STATE_UPLOAD_ALL, server, localPath, toPath);
    }
}
