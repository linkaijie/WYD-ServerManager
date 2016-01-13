package com.zhwyd.server.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.zhwyd.server.bean.WorldServer;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.service.WorldServerService;
/**
 * Gm工具配置同步
 * 
 * @author linkaijie
 */
public class SynGmToolServlet extends HttpServlet {
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
            // Map<String, String> ipAndPortMap = new HashMap<String, String>();
            JSONObject jsonObject = new JSONObject();
            WorldServerService worldServerService = Application.getBean(WorldServerService.class);
            List<WorldServer> worldServerList = worldServerService.getAll();
            for (WorldServer worldServer : worldServerList) {
                // 只有开启时才会被同步
                if (worldServer.getState() == CommanConstant.STATE_STOP) {
                    continue;
                }
                String gmStr = "";
                String mergeIds = "";
                if (worldServer.getMachinecode() < 0) {// 合服分区
                    // 合服前分区id
                    mergeIds = worldServer.getMergeIds();
                    if (!StringUtils.isEmpty(mergeIds)) {
                        for (String areaId : mergeIds.split(",")) {
                            // 内网ip|adminPort,name,serverType
                            gmStr = CacheService.getServerById(worldServer.getServerId()).getServerIp() + "|" + worldServer.getAdminport() + "," + worldServer.getName() + "," + worldServer.getServerType();
                            // ipAndPortMap.put(areaId, gmStr);
                            jsonObject.put(areaId, gmStr);
                        }
                    }
                } else {// 未合服分区
                    // 内网ip|adminPort,name,serverType
                    gmStr = CacheService.getServerById(worldServer.getServerId()).getServerIp() + "|" + worldServer.getAdminport() + "," + worldServer.getName() + "," + worldServer.getServerType();
                    // ipAndPortMap.put(worldServer.getAreaid(), gmStr);
                    jsonObject.put(worldServer.getAreaid(), gmStr);
                }
            }
            // JSONObject jsonArray = JSONObject.fromObject(ipAndPortMap);
            outputApiResult(response, jsonObject.toString());
            // System.out.println("jsonObject=" + jsonObject.toString());
            // JSONObject json = JSONObject.fromObject(jsonArray.toString());
            // Map<String, Object> map = new HashMap<String, Object>();
            // for (Object k : json.keySet()) {
            // Object v = json.get(k);
            // map.put(k.toString(), v);
            // System.out.println(k+"="+v);
            // }
        } catch (Exception e) {
            log.error(e.toString(), e);
            e.printStackTrace();
        }
    }

    private void outputApiResult(HttpServletResponse response, String result) throws IOException {
        writeResult(response, result);
    }

    /**
     * 输出结果
     * 
     * @param response      HttpServletResponse
     * @param res           结果串
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
