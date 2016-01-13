package com.zhwyd.server.sshxcute.pool;
import java.util.ArrayList;
import java.util.List;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.common.util.CryptionUtil;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.sshxcute.Sshbean;
public class SshInitInfo {
    public static List<Sshbean> beans = null;
    // 这里加载所有ssh主机信息
    static {
        List<Server> serverList = Application.getBean(ServerService.class).getAll();
        beans = new ArrayList<Sshbean>();
        Sshbean sshbean = null;
        for (Server server : serverList) {
            sshbean = new Sshbean();
            sshbean.setPoolName(server.getServerIp());
            sshbean.setSshIp(server.getServerIp());
            sshbean.setUserName(server.getSshName());
            sshbean.setSshPort(server.getServerPort());
            sshbean.setUseType(server.getUseType());
            String psw = CryptionUtil.getDecryptString(server.getSshPwd(), Application.getConfig("system", "key"));
            sshbean.setPassword(psw);
            beans.add(sshbean);
        }
    }
}
