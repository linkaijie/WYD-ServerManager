package com.zhwyd.server.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.zhwyd.server.bean.vo.DispatchVo;
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
public class MonitorIpdLoadServlet extends HttpServlet {
    private static final long       serialVersionUID = 1L;
    protected final Logger          log              = Logger.getLogger(getClass());
    private static List<DispatchVo> dispatchVoList;
    private static MonitorService   monitorService;
    private static int              monitorNum       = 0;

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
            boolean isOpareting = CacheService.isOpareting();
            if (CacheService.getIsMonitor() && monitorNum%2 == 0 && !isOpareting) {
                System.out.println("time=" + System.currentTimeMillis() / 1000);
                System.out.println("接口监控中。。。");
                if (monitorService == null) {
                    monitorService = Application.getBean(MonitorService.class);
                }
                List<DispatchVo> dispatchVoTempList = monitorService.getIpdLoadList(errorSb);
                System.out.println("dispatch Size："+dispatchVoTempList.size());
                // 监控
                monitorService.batchMonitorNew(dispatchVoTempList);
                if (dispatchVoList != null && dispatchVoList.size() > 0) {
                    System.out.println("dispatchVoList.size()="+dispatchVoList.size());
                    if (dispatchVoList.size() > dispatchVoTempList.size()) {
                        for (DispatchVo dispatchVo : dispatchVoList) {
                            if (!dispatchVoTempList.contains(dispatchVo)) {
                                errorSb.append("IP为：<font red='green'>" + dispatchVo.getDispatchIp() + "</font>,端口为：<font red='green'>" + dispatchVo.getDispatchPort());
                                errorSb.append("</font>,machineCode为：<font red='green'>" + dispatchVo.getDispatchCode() + "</font>的线路已关闭，请检查！</br>");
                            }
                        }
                    }
                }
                dispatchVoList = dispatchVoTempList;
                // 异常时发送错误邮件
                if (errorSb != null && errorSb.length() > 0) {
                    System.out.println("errorSb=" + errorSb);
                    SystemLogService.monitorLog(errorSb);
                    for (int i = 0; i <= 3; i++) {
                        monitorService.sendEmail(errorSb.toString());
                    }
                    serverResult.setFail();
                    response.sendError(500, errorSb.toString());
                } else {
                    serverResult.setSuccess();
                }
            } else {
                System.out.println("监控已跳过。。。");
                serverResult.setSuccess();
            }
            this.outputApiResult(response, serverResult);
            serverResult = null;
            errorSb = null;
            System.out.println("monitorNum=" + monitorNum);
            if (monitorNum >= 20) {
                monitorNum = 0;
                System.gc();
                System.runFinalization();
                System.out.println("已GC。。。");
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
