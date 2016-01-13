package com.zhwyd.server.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.User;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.config.ServerResult;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.monitor.BathCreative;
import com.zhwyd.server.monitor.Monitor;
import com.zhwyd.server.service.MonitorService;
import com.zhwyd.server.service.impl.SystemLogService;
/**
 * 监控接口
 * 
 * @author linkaijie
 */
public class MonitorServlet extends HttpServlet {
    private static final long     serialVersionUID   = 1L;
    protected final Logger        log                = Logger.getLogger(getClass());
    private static String         loginName          = "thirdLogin";
    private static String         password           = "888888";
    private List<DispatchServer>  dispatchServerList = null;
    private static MonitorService monitorService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        process(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServerResult serverResult;
        if (CacheService.getIsMonitor()) {
            try {
                System.out.println("time=" + System.currentTimeMillis() / 1000);
                System.out.println("接口监控中。。。");
                StringBuffer errorSb = new StringBuffer();
                if (monitorService == null) {
                    monitorService = Application.getBean(MonitorService.class);
                }
                dispatchServerList = monitorService.getNeedMonitorList(errorSb);
                monitorService.checkIpdMainList(dispatchServerList, errorSb);
                if (dispatchServerList != null && dispatchServerList.size() > 0) {
                    for (DispatchServer dispatchServer : dispatchServerList) {
                        errorSb.append(monitor(dispatchServer));
                    }
                }
                serverResult = new ServerResult();
                // 发送邮件
                if (errorSb != null && errorSb.length() > 0) {
                    System.out.println("errorSb=" + errorSb);
                    SystemLogService.monitorLog(errorSb);
                    monitorService.sendEmail(errorSb.toString());
                    serverResult.setFail();
                    response.sendError(500, errorSb.toString());
                } else {
                    serverResult.setSuccess();
                }
                this.outputApiResult(response, serverResult);
                errorSb = null;
            } catch (Exception e) {
                log.error(e.toString(), e);
                e.printStackTrace();
            }
        }else {
            serverResult = new ServerResult();
            serverResult.setSuccess();
            this.outputApiResult(response, serverResult);
        }
    }

    public String monitor(DispatchServer dispatchServer) throws Exception {
        StringBuffer errorSb = new StringBuffer();
        errorSb.append("</br>");
        errorSb.append("分区名称为【" + CacheService.getWorldServerById(dispatchServer.getWorldId()).getName() + "】,");
        errorSb.append("Dispatch地址为【" + dispatchServer.getPublicserver() + "," + dispatchServer.getPort() + "】,");
        errorSb.append("</br>");
        System.out.println("errorSb=" + errorSb);
        String uuid = "";
        User user = new User();
        user.setUsername(loginName + dispatchServer.getId());
        System.out.println("userName=" + user.getUsername());
        user.setPassword(password);
        JSONObject loginJson = JSONObject.fromObject(BathCreative.login(user));// 账号登录
        uuid = loginJson.getString("uuid");
        if (loginJson.get("code").toString().equals("0")) {
            JSONObject registerJson = BathCreative.register(user);
            // 账号注册：code返回0代表不成功,不成功则返回错误信息,发送邮件
            if ("0".equals(registerJson.getString("code"))) {
                errorSb.append("注册账号时" + user.getUsername() + "," + registerJson.getString("message"));
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

    private void outputApiResult(HttpServletResponse response, ServerResult result) throws IOException {
        writeResult(response, result.getJson().toString());
    }

    /**
     * 输出结果
     * 
     * @param response
     *            HttpServletResponse
     * @param res
     *            结果串
     * @throws IOException
     */
    public void writeResult(HttpServletResponse response, CharSequence res) throws IOException {
        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(res.toString());
        out.flush();
        out.close();
    }
}
