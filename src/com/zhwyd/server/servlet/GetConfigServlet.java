package com.zhwyd.server.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.config.ServerResult;
import com.zhwyd.server.service.AccountServerService;
import com.zhwyd.server.service.DispatchServerService;
import com.zhwyd.server.service.WorldServerService;
/**
 * 获取服务配置信息
 * 
 * @author linkaijie
 */
public class GetConfigServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected final Logger    log              = Logger.getLogger(getClass());

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
            ServerResult serverResult = new ServerResult();
            String configStr = "";
            // 请求为json字符串，type为服务类型（account、world，dispatch），account-IP，world—area_id，dispatch—area_id+number
            if (request.getParameter("condition") != null && !request.getParameter("condition").equals("")) {
                String condition = request.getParameter("condition");
                JSONObject object = JSONObject.fromObject(condition);
                String type = object.getString("type");
                if (StringUtils.isEmpty(type)) {
                    serverResult.setFail("请求类型出错");
                } else {
                    if (type.equals("account")) {
                        configStr = Application.getBean(AccountServerService.class).getConfig(object);
                    } else if (type.equals("ipd")) {
                        // configStr = Application.getBean(IpdServerService.class);
                    } else if (type.equals("dispatch")) {
                        configStr = Application.getBean(DispatchServerService.class).getConfig(object);
                    } else if (type.equals("world")) {
                        configStr = Application.getBean(WorldServerService.class).getConfig(object);
                    }
                    serverResult.setString("config", configStr);
                }
            } else {
                serverResult.setFail("请求condition出错");
            }
            this.outputApiResult(response, serverResult);
        } catch (Exception e) {
            log.error(e.toString(), e);
            e.printStackTrace();
        }
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
        res = null;
    }
}
