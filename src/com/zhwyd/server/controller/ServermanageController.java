package com.zhwyd.server.controller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.zhwyd.server.bean.AccountServer;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.IpdServer;
import com.zhwyd.server.bean.WorldServer;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.common.util.CommonUtil;
import com.zhwyd.server.controller.model.WorldServerModel;
import com.zhwyd.server.service.AccountServerService;
import com.zhwyd.server.service.DispatchServerService;
import com.zhwyd.server.service.IpdServerService;
import com.zhwyd.server.service.ServerManageService;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.service.WorldServerService;
import com.zhwyd.server.service.impl.SystemLogService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
import com.zhwyd.server.web.page.Pager;
@Controller
public class ServermanageController {
    private static Logger         serverManegeLog  = Logger.getLogger("serverManege");
    @SuppressWarnings("unused")
    private static final long     serialVersionUID = 1L;
    @Autowired
    private AccountServerService  accountServerService;
    @Autowired
    private IpdServerService      ipdServerService;
    @Autowired
    private WorldServerService    worldServerService;
    @Autowired
    private DispatchServerService dispatchServerService;
    @Autowired
    private ServerManageService   serverManageService;
    @Autowired
    private ServerService         serverService;
    @SuppressWarnings("unused")
    private Map<String, Object>   resultMap        = new HashMap<String, Object>();

    @RequestMapping
    public ModelAndView refreshCache() {
        CacheService.initAll();
        return new ModelAndView("redirect:serverManageList.action");
    }

    @RequestMapping
    public ModelAndView opeartMinotor(int isMonitor) {
        CacheService.setIsMonitor(isMonitor);
        return new ModelAndView("redirect:serverManageList.action");
    }

    /**
     * 获取服务管理列表
     */
    @RequestMapping
    public ModelAndView serverManageList(WorldServerModel worldServerModel, HttpSession session) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        if (session.getAttribute(Global.GAME_ID) == null) {
            worldServerModel.setGameId(1);
        } else {
            worldServerModel.setGameId((Integer) session.getAttribute(Global.GAME_ID));
        }
        Pager pager = serverManageService.getServerManageModelList(worldServerModel);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("server", serverService.getAll());
        modelAndView.addObject("model", worldServerModel);
        serverManegeLog.info(session.getAttribute(Global.LOGIN_CURRENT_NAME) + ":查看serverManegeList服务列表");
        return modelAndView;
    }

    /**
     * 开启服务(停用)
     */
    @RequestMapping
    public ModelAndView startServer(HttpServletResponse response, Integer worldId, HttpSession session) throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        StringBuilder sb = new StringBuilder();
        stringBuffer.append("正在开启中。。。</br>");
        response.getWriter().write("true");
        System.out.println("worldId=" + worldId);
        if (worldId != null) {
            SystemLogService.serverManegeLog(session, "开启WorldId为" + worldId + "的服务");
            if (worldId > 0) {
                sb = worldServerService.getAccountState(worldId + "");
                // System.out.println("sb=" + sb);
                if (sb != null && sb.length() > 0) {
                    stringBuffer.append("ID为：" + sb + "的ACCOUNT未启动，请启动完再重试");
                } else {
                    serverManageService.startServer(worldServerService, worldId, session, stringBuffer);
                    return null;
                }
            } else {
                stringBuffer.append("ID为：" + worldId + "的WorldServer不存在，启动失败");
                session.removeAttribute("startServerInfo");
            }
            session.setAttribute("startServerInfo", stringBuffer + "</br>");
            stringBuffer = null;
            sb = null;
        }
        return new ModelAndView();
    }

    /**
     * 关闭服务(停用)
     */
    @RequestMapping
    public ModelAndView closeServer(HttpServletResponse response, Integer worldId, HttpSession session) throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("正在关闭中。。。</br>");
        response.getWriter().write("true");
        if (worldId != null) {
            SystemLogService.serverManegeLog(session, "关闭WorldId为" + worldId + "的服务");
            if (worldId > 0) {
                serverManageService.closeServer(worldServerService, worldId, session, stringBuffer);
                return null;
            } else {
                stringBuffer.append("ID为：" + worldId + "的WorldServer不存在，关闭失败");
                session.setAttribute("closeServerInfo", stringBuffer + "</br>");
            }
            stringBuffer = null;
        }
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    /**
     * 批量开启服务
     */
    @RequestMapping
    public ModelAndView batchStartServer(HttpServletResponse response, Pager pager, String worldIds, HttpSession session) throws Exception {
        ModelAndView modelAndView = new ModelAndView("servermanage/batchStartLog");
        modelAndView.addObject("pager", pager);
        if (!StringUtils.isEmpty(worldIds)) {
            SystemLogService.serverManegeLog(session, "批量启动WorldId为：" + worldIds + "服务");
            this.clearSession(session);// 清除session
            Map<Integer, List<Integer>> dispatchIdMap = new HashMap<Integer, List<Integer>>();// dispatchid列表
            Map<WorldServer, List<DispatchServer>> batchWorldAndDispatchMap = new HashMap<WorldServer, List<DispatchServer>>();
            StringBuilder accountIdByState = worldServerService.getAccountState(worldIds);// 未开启account的Id
            String dispatchIds = serverManageService.getDispatchIds(session, worldIds, CommanConstant.STATE_STOP, dispatchIdMap, batchWorldAndDispatchMap);// 需要开启的dispatch的id串
            session.setAttribute("batchStartWorldAndDispatchMap", sortMap(batchWorldAndDispatchMap));
            StringBuilder ipdCloseIds = dispatchServerService.getIpdState(dispatchIds);// 未开启ipd的Id
            if (accountIdByState != null && accountIdByState.length() > 0) {
                session.setAttribute("batchStartServerError", "ID为：" + accountIdByState + "的ACCOUNT未启动，请启动完再重试");
            } else if (ipdCloseIds != null && ipdCloseIds.length() > 0) {
                session.setAttribute("batchStartServerError", "ID为：" + ipdCloseIds + "的Ipd未启动，请启动完再重试");
            } else if (StringUtils.isEmpty(dispatchIds)) {
                session.setAttribute("batchStartServerError", "没有可以开启的Dispatch服务");
            } else {
                serverManageService.batchStartWorldAndDispatch(worldIds, session, dispatchIdMap);
            }
            response.getWriter().write("true");
            return null;
        }
        return modelAndView;
    }

    /**
     * Map排序
     */
    public List<Map.Entry<WorldServer, List<DispatchServer>>> sortMap(Map<WorldServer, List<DispatchServer>> batchWorldAndDispatchMap) {
        List<Map.Entry<WorldServer, List<DispatchServer>>> list = new ArrayList<Map.Entry<WorldServer, List<DispatchServer>>>(batchWorldAndDispatchMap.entrySet());
        for (int i = 0; i < list.size(); i++) {
            String id = ((WorldServer) list.get(i).getKey()).getId().toString();
            System.out.println("id1=" + id);
        }
        // 排序
        Collections.sort(list, new Comparator<Map.Entry<WorldServer, List<DispatchServer>>>() {
            public int compare(Map.Entry<WorldServer, List<DispatchServer>> o1, Map.Entry<WorldServer, List<DispatchServer>> o2) {
                // return (o2.getValue() - o1.getValue());
                return (o1.getKey().getId().toString()).compareTo(o2.getKey().getId().toString());
            }
        });
        for (int i = 0; i < list.size(); i++) {
            String id = ((WorldServer) list.get(i).getKey()).getId().toString();
            System.out.println("id2=" + id);
        }
        return list;
    }

    /**
     * 批量关闭服务
     */
    @RequestMapping
    public ModelAndView batchCloseServer(HttpServletResponse response, Pager pager, String worldIds, HttpSession session) throws Exception {
        ModelAndView modelAndView = new ModelAndView("servermanage/batchCloseLog");
        modelAndView.addObject("pager", pager);
        if (!StringUtils.isEmpty(worldIds)) {
            SystemLogService.serverManegeLog(session, "批量关闭WorldId为：" + worldIds + "服务");
            this.clearCloseSession(session);
            Map<Integer, List<Integer>> openDispatchIdMap = new HashMap<Integer, List<Integer>>();
            Map<WorldServer, List<DispatchServer>> batchWorldAndDispatchMap = new HashMap<WorldServer, List<DispatchServer>>();
            String dispatchIds = serverManageService.getDispatchIds(session, worldIds, CommanConstant.STATE_START, openDispatchIdMap, batchWorldAndDispatchMap);
            session.setAttribute("batchCloseWorldAndDispatchMap", sortMap(batchWorldAndDispatchMap));
            session.setAttribute("openDispatchIdMap", openDispatchIdMap);
            if (!StringUtils.isEmpty(dispatchIds)) {
                serverManageService.batchCloseDispatch(dispatchIds, session);
            }
            serverManageService.batchCloseWorld(worldIds, session, openDispatchIdMap);
            response.getWriter().write("true");
            return null;
        }
        return modelAndView;
    }

    /**
     * 查看操作日志
     */
    @RequestMapping
    public ModelAndView showStdout(int serverId, String serverType, HttpSession session) throws Exception {
        session.setAttribute("stdout", "");
        // TODO
        if (serverType.equals("account")) {
            AccountServer accountServer = accountServerService.get(serverId);
            if (accountServer != null) {
                resultMap = accountServerService.showStdout(accountServer, session);
            }
        } else if (serverType.equals("ipd")) {
            IpdServer ipdServer = ipdServerService.get(serverId);
            if (ipdServer != null) {
                resultMap = ipdServerService.showStdout(ipdServer, session);
            }
        } else if (serverType.equals("dispatch")) {
            DispatchServer dispatchServer = dispatchServerService.get(serverId);
            if (dispatchServer != null) {
                resultMap = dispatchServerService.showStdout(dispatchServer, session);
            }
        } else if (serverType.equals("world")) {
            WorldServer worldServer = worldServerService.get(serverId);
            if (worldServer != null) {
                resultMap = worldServerService.showStdout(worldServer, session);
            }
        }
        return new ModelAndView();
    }

    /**
     * 清除session信息
     */
    public void clearSession(HttpSession session) {
        session.removeAttribute("batchWorldNum");
        session.removeAttribute("batchDispatchNum");
        session.removeAttribute("batchWorld");
        session.removeAttribute("batchDispatch");
        session.removeAttribute("batchStartWorldAndDispatchMap");
        session.removeAttribute(Global.BATCH_START_DISPATCH);
        session.removeAttribute(Global.BATCH_START_WORLD);
        session.removeAttribute("batchStartServerError");
    }

    /**
     * 清除session信息
     * @param session
     */
    public void clearCloseSession(HttpSession session) {
        session.removeAttribute("batchWorldNum");
        session.removeAttribute("batchDispatchNum");
        session.removeAttribute("batchWorld");
        session.removeAttribute("batchDispatch");
        session.removeAttribute("batchCloseWorldAndDispatchMap");
        session.removeAttribute(Global.BATCH_CLOSE_WORLD);
        session.removeAttribute(Global.BATCH_CLOSE_DISPATCH);
        session.removeAttribute("batchCloseServerError");
    }

    /**
     * 更新并重启服务
     * @param worldServerModel
     * @param worldIds
     * @param session
     * @throws Exception
     */
    @RequestMapping
    public ModelAndView updateAndRestart(HttpServletResponse response, HttpSession session, String worldIds, Integer updateType, String sessionVoListName, String sessionBeanListName) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            if (!StringUtils.isEmpty(worldIds)) {
                // updateType 更新類型（1：只更新world；2：只更新dispatch；3：更新world和dispatch；4：只重启dispatch，不更新；5：重启world和dispatch，不更新）
                WorldServer worldServer = null;
                DispatchServer dispatchServer = null;
                boolean synResult = true;
                if (updateType == CommanConstant.UPDATE_RESTARY_WORLD) {
                    worldServer = CacheService.getWorldServerAllList().get(0);
                    // 更新前将update文件夹里的文件拷到model中
                    synResult = SshxcuteUtils.updateServerByScp(null, null, worldServer.getUpdatePath(), worldServer.getModelPath(), CommanConstant.STATE_UPLOAD_ALL);
                } else if (updateType == CommanConstant.UPDATE_RESTARY_DISPATCH) {
                    dispatchServer = CacheService.getDispatchServerAllList().get(0);
                    // 更新前将update文件夹里的文件拷到model中
                    synResult = SshxcuteUtils.updateServerByScp(null, null, dispatchServer.getUpdatePath(), dispatchServer.getModelPath(), CommanConstant.STATE_UPLOAD_ALL);
                } else if (updateType == CommanConstant.UPDATE_RESTARY_WORLD_DISPATCH) {
                    worldServer = CacheService.getWorldServerAllList().get(0);
                    // 更新前将update文件夹里的文件拷到model中
                    synResult = SshxcuteUtils.updateServerByScp(null, null, worldServer.getUpdatePath(), worldServer.getModelPath(), CommanConstant.STATE_UPLOAD_ALL);
                    if (synResult) {
                        dispatchServer = CacheService.getDispatchServerAllList().get(0);
                        // 更新前将update文件夹里的文件拷到model中
                        synResult = SshxcuteUtils.updateServerByScp(null, null, dispatchServer.getUpdatePath(), dispatchServer.getModelPath(), CommanConstant.STATE_UPLOAD_ALL);
                    }
                }
                Properties properties = CommonUtil.getBeanAndVoName();
                properties.put("worldIds", worldIds);
                properties.put("updateType", updateType);
                serverManageService.updateAndRestart(session, properties);
                SystemLogService.serverManegeLog(session, ":更新并重启worldId为" + worldIds + "的服务");
                response.getWriter().print(new JSONObject().accumulate("result", "true").accumulate("sessionVoListName", properties.getProperty("sessionVoListName")).accumulate("sessionBeanListName", properties.getProperty("sessionBeanListName")).toString());
                return null;
            }
            modelAndView.addObject("sessionVoListName", sessionVoListName);
            modelAndView.addObject("sessionBeanListName", sessionBeanListName);
            modelAndView.addObject("beanInterfaceList", session.getAttribute(sessionBeanListName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelAndView;
    }
}
