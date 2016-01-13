package com.zhwyd.server.thread;
import net.sf.json.JSONObject;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.User;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.monitor.BathCreative;
import com.zhwyd.server.monitor.Monitor;
import com.zhwyd.server.service.MonitorService;
import com.zhwyd.server.service.impl.SystemLogService;
public class BatchMonitorThread implements Runnable {
    private static String  loginName = "MonitorLogin";
    private static String  password  = "888888";
    private DispatchServer dispatchServer;
    private MonitorService monitorService;

    public BatchMonitorThread(DispatchServer dispatchServer, MonitorService monitorService) {
        this.dispatchServer = dispatchServer;
        this.monitorService = monitorService;
    }

    @Override
    public void run() {
        try {
            StringBuffer errorSb = this.getHeadString(dispatchServer);
            // System.out.println("message=" + errorSb);
            String uuid = "";
            User user = new User();
            user.setUsername(loginName + dispatchServer.getId());
            // System.out.println("userName=" + user.getUsername());
            user.setPassword(password);
            JSONObject loginJson = JSONObject.fromObject(BathCreative.login(user));// 账号登录
            uuid = loginJson.getString("uuid");
            if (loginJson.get("code").toString().equals("0")) {
                JSONObject registerJson = BathCreative.register(user);
                // 账号注册：code返回0代表不成功,不成功则返回错误信息,发送邮件
                if ("0".equals(registerJson.getString("code"))) {
                    errorSb.append("注册账号时" + user.getUsername() + "," + registerJson.getString("message"));
                }
                uuid = registerJson.getString("uuid");
            }
            user.setUuid(uuid);
            Monitor monitor = new Monitor(dispatchServer.getPublicserver(), dispatchServer.getPublicport(), user);
            // Monitor monitor = new Monitor("192.168.3.137", 19990, user);
            String monitorResult = monitor.thirdLoginProcess(0);
            // 成功返回空字符串，否则视为失败，发送邮件
            if (monitorResult != null && !monitorResult.equals("")) {
                errorSb.append(monitorResult);
                System.out.println(monitorResult);
                monitorService.sendEmail(errorSb.toString());
                SystemLogService.monitorLog(errorSb);
            }
            errorSb = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public StringBuffer getHeadString(DispatchServer dispatchServer) {
        StringBuffer headSb = new StringBuffer();
        headSb.append("</br>");
        headSb.append("分区名称为【" + CacheService.getWorldServerById(dispatchServer.getWorldId()).getName() + "】,");
        headSb.append("Dispatch地址为【" + dispatchServer.getPublicserver() + "," + dispatchServer.getPort() + "】,");
        headSb.append("</br>");
        return headSb;
    }
}
