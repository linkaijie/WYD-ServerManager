package com.zhwyd.server.thread;
import java.util.List;
import net.sf.json.JSONObject;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.User;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.monitor.BathCreative;
import com.zhwyd.server.monitor.Monitor;
import com.zhwyd.server.service.MonitorService;
import com.zhwyd.server.service.impl.SystemLogService;
public class MonitorThread implements Runnable {
    private List<DispatchServer>  dispatchServerList = null;
    private static String         loginName          = "thirdLogin";
    private static String         password           = "888888";
    private static MonitorService monitorService;

    public void start() {
//        Thread t = new Thread(this);
//        t.setName("MonitorThread-Thread");
//        t.setDaemon(true);
//        t.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(3 * 60 * 1000L);
                System.out.println("time=" + System.currentTimeMillis() / 1000);
                System.out.println("监控中。。。");
                StringBuffer errorSb = new StringBuffer();
                if (monitorService == null) {
                    monitorService = Application.getBean(MonitorService.class);
                }
                dispatchServerList = monitorService.getNeedMonitorList(errorSb);
                monitorService.checkIpdMainList(dispatchServerList, errorSb);
                if (dispatchServerList != null && dispatchServerList.size() > 0) {
                    for (DispatchServer dispatchServer : dispatchServerList) {
                        errorSb.append(process(dispatchServer));
                    }
                }
                // 发送邮件
                if (errorSb != null && errorSb.length() > 0) {
                    System.out.println("errorSb=" + errorSb);
                    SystemLogService.monitorLog(errorSb);
                    monitorService.sendEmail(errorSb.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String process(DispatchServer dispatchServer) throws Exception {
        StringBuffer errorSb = new StringBuffer();
        errorSb.append("</br>");
        errorSb.append("分区名称为【" + CacheService.getWorldServerById(dispatchServer.getWorldId()).getName() + "】,");
        errorSb.append("Dispatch地址为【" + dispatchServer.getPublicserver() + "," + dispatchServer.getPort() + "】,");
        errorSb.append("</br>");
        System.out.println("errorSb=" + errorSb);
        String uuid = "";
        User user = new User();
        user.setUsername(loginName);
        user.setPassword(password);
        JSONObject loginJson = JSONObject.fromObject(BathCreative.login(user));// 账号登录
        System.out.println("loginJson=" + loginJson);
        uuid = loginJson.getString("uuid");
        if (loginJson.get("code").toString().equals("0")) {
            JSONObject registerJson = BathCreative.register(user);
            // 账号注册：code返回0代表不成功,不成功则返回错误信息,发送邮件
            if ("0".equals(registerJson.getString("code"))) {
                errorSb.append("注册账号时" + user.getUsername() + "," + registerJson.getString("message"));
                System.out.println(errorSb);
                return errorSb.toString();
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
            return errorSb.toString();
        }
        return "";
    }
}
