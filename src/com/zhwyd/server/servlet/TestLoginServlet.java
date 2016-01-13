package com.zhwyd.server.servlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import com.zhwyd.server.bean.User;
import com.zhwyd.server.monitor.BathCreative;
import com.zhwyd.server.monitor.Monitor;
/**
 * 第三方登录测试
 * 
 * @author linkaijie
 */
public class TestLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected final Logger    log              = Logger.getLogger(getClass());
    private static String     address          = "192.168.3.137";
    private static Integer    port             = 19990;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        process(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
             if (request.getParameter("address") != null && !request.getParameter("address").equals("")) {
             address = request.getParameter("address");
             }
             if (request.getParameter("port") != null && !request.getParameter("port").equals("")) {
             port = Integer.valueOf(request.getParameter("port"));
             }
             String uuid = "";  
             User user = new User();
             user.setUsername("thirdLogin1");
             user.setPassword("888888");
             // 第三方登录
             JSONObject loginJson = JSONObject.fromObject(BathCreative.login(user));// 返回uuid和sessionId
             uuid = loginJson.getString("uuid");
             System.out.println(loginJson.toString());
             if (loginJson.get("code").toString().equals("0")) {
             JSONObject registerJson = BathCreative.register(user);
             // code返回0代表不成功,不成功则返回错误信息
             if ("0".equals(registerJson.getString("code"))) {
                 String resultMsg = user.getUsername() + "," + registerJson.getString("message");
                 System.out.println(resultMsg);
                 return;
             }
             uuid = registerJson.getString("uuid");
             }
             // 第三方登录后返回uuid
             user.setUuid(uuid);
             Monitor monitor = new Monitor(address, port, user);
             monitor.thirdLoginProcess(0);
             
             
//            StringBuffer stringBuffer =  new StringBuffer();
//            List<DispatchServer> dispatchServerList = Application.getBean(MonitorService.class).getNeedMonitorList(stringBuffer);
//            Application.getBean(MonitorService.class).checkIpdMainList(dispatchServerList, stringBuffer);
//            System.out.println("dispatchServerListSize= "+dispatchServerList.size());
//            for (DispatchServer dispatchServer: dispatchServerList) {
//                System.out.println("ip="+dispatchServer.getPublicserver()+", port="+dispatchServer.getPublicport());
//            }
//            List<String> IpdMianList = Application.getBean(MonitorService.class).getIpdMianList(stringBuffer);
//            for (String address: IpdMianList) {
//                System.out.println("address="+address);
//            }
        } catch (Exception e) {
            log.error(e.toString(), e);
            e.printStackTrace();
        }
    }
}
