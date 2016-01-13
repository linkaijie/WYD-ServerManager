package com.zhwyd.server.servlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.zhwyd.server.bean.WorldServer;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.util.CommonUtil;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.service.WorldServerService;
/**
 * 充值平台获取http端口信息接口
 * 
 * @author linkaijie
 */
public class GetHttpCfgServlet extends HttpServlet {
    private static final long  serialVersionUID = 1L;
    protected final Logger     log              = Logger.getLogger(getClass());
    private WorldServerService worldServerService;

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
            worldServerService = Application.getBean(WorldServerService.class);
            List<WorldServer> worldServerList = worldServerService.getAll();
            List<Properties> propertieList = new ArrayList<Properties>();
            Properties properties = null;
            for (WorldServer worldServer : worldServerList) {
                // 只有开启时才会被同步
                if (worldServer.getState() == CommanConstant.STATE_STOP) {
                    continue;
                }
                properties = new Properties();
                properties.put("areaId", worldServer.getAreaid());
                properties.put("name", worldServer.getName());
                properties.put("httpIp", CacheService.getServerById(worldServer.getServerId()).getServerIp());
                properties.put("httpPort", worldServer.getHttpport());
                properties.put("mergeAreaIds", StringUtils.isEmpty(worldServer.getMergeIds()) ? "" : worldServer.getMergeIds());
                propertieList.add(properties);
            }
            String result = "";
            if (propertieList.size() > 0) {
                JSONArray jsonArray = JSONArray.fromObject(propertieList);
                result = jsonArray.toString();
            }
            CommonUtil.outputApiResult(response, result);
        } catch (Exception e) {
            log.error(e.toString(), e);
            e.printStackTrace();
        }
    }
}
