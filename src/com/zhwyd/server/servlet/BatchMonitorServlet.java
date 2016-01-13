package com.zhwyd.server.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.config.ServerResult;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.service.MonitorService;
import com.zhwyd.server.service.impl.SystemLogService;
/**
 * 监控接口(多线程)
 * 
 * @author linkaijie
 */
public class BatchMonitorServlet extends HttpServlet {
    private static final long     serialVersionUID   = 1L;
    protected final Logger        log                = Logger.getLogger(getClass());
    private List<DispatchServer>  dispatchServerList = null;
    private static MonitorService monitorService;
    private static int            monitorNum         = 0;

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
            monitorNum++;
            StringBuffer errorSb = new StringBuffer();
            ServerResult serverResult = new ServerResult();
            if (CacheService.getIsMonitor()) {
                System.out.println("time=" + System.currentTimeMillis() / 1000);
                System.out.println("接口监控中。。。");
                if (monitorService == null) {
                    monitorService = Application.getBean(MonitorService.class);
                }
                // 获取需要监控的Dispatch列表
                dispatchServerList = monitorService.getNeedMonitorList(errorSb);
                // 比较Dispatch于IPD列表
                monitorService.checkIpdMainList(dispatchServerList, errorSb);
                // 监控
                monitorService.batchMonitor(dispatchServerList);
                // 异常时发送错误邮件
                if (errorSb != null && errorSb.length() > 0) {
                    System.out.println("errorSb=" + errorSb);
                    SystemLogService.monitorLog(errorSb);
                    monitorService.sendEmail(errorSb.toString());
                    serverResult.setFail();
                    response.sendError(500, errorSb.toString());
                } else {
                    serverResult.setSuccess();
                }
            } else {
                System.out.println("监控已关闭。。。");
                serverResult.setSuccess();
            }
            this.outputApiResult(response, serverResult);
            serverResult = null;
            errorSb = null;
            dispatchServerList = null;
            if (monitorNum >= 20) {
                monitorNum = 0;
                System.gc();
                System.runFinalization();
            }
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
