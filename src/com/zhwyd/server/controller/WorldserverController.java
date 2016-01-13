package com.zhwyd.server.controller;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.JdbcUrl;
import com.zhwyd.server.bean.Model;
import com.zhwyd.server.bean.ServerType;
import com.zhwyd.server.bean.WorldServer;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.controller.model.WorldServerModel;
import com.zhwyd.server.service.AccountServerService;
import com.zhwyd.server.service.DispatchServerService;
import com.zhwyd.server.service.GameService;
import com.zhwyd.server.service.JdbcUrlService;
import com.zhwyd.server.service.ModelService;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.service.WorldServerService;
import com.zhwyd.server.service.impl.SystemLogService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
import com.zhwyd.server.web.page.Pager;
@Controller
public class WorldserverController {
    @SuppressWarnings("unused")
    private static final long    serialVersionUID = 1L;
    @Autowired
    private WorldServerService   worldServerService;
    @Autowired
    private AccountServerService accountServerService;
    @Autowired
    private ServerService        serverService;
    @Autowired
    private ModelService         modelService;
    @Autowired
    private GameService          gameService;
    @Autowired
    private JdbcUrlService       jdbcUrlService;
    private WorldServer          worldServer;
    Map<String, Object>          resultMap        = new HashMap<String, Object>();

    /**
     * 获取WorldServer服务列表
     * 
     * @author:LKJ 2014-9-9
     */
    @RequestMapping
    public ModelAndView worldServerList(WorldServerModel worldServerModel, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        Model model = modelService.getAll().get(0);
        Date updateDate = new Date();
        if (model != null) {
            if (model.getWorldUpdateTime() != null) {
                updateDate = model.getWorldUpdateTime();
                modelAndView.addObject("updateDate", updateDate);
            }
        }
        List<JdbcUrl> jdbcUrlList = jdbcUrlService.getJdbcUrlList(CommanConstant.TYPE_JDBC_WORLD);
        worldServerModel.setGameId((Integer) session.getAttribute(Global.GAME_ID));
        Pager pager = worldServerService.getWorldServerList(worldServerModel);
        modelAndView.addObject("jdbcUrlList", jdbcUrlList);
        modelAndView.addObject("model", worldServerModel);
        modelAndView.addObject("server", serverService.getAll());
        modelAndView.addObject("pager", pager);
        SystemLogService.worldServerLog(session, ":查看WORLD服务列表");
        return modelAndView;
    }

    /**
     * 编辑World
     * 
     * @author:LKJ 2014-11-24
     */
    @RequestMapping
    public ModelAndView worldServerInput(WorldServerModel worldServerModel, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        if (worldServerModel != null && worldServerModel.getWorldId() != null && worldServerModel.getWorldId() > 0) {
            WorldServer worldServer = worldServerService.get(worldServerModel.getWorldId());
            modelAndView.addObject("worldServer", worldServer);
        }
        modelAndView.addObject("accountServer", accountServerService.getAll());
        modelAndView.addObject("server", serverService.getAll());
        modelAndView.addObject("model", modelService.getAll());
        modelAndView.addObject("game", gameService.getAll());
        SystemLogService.worldServerLog(session, ":编辑ID为" + worldServerModel.getWorldId() + "的WORLD信息");
        return modelAndView;
    }

    /**
     * 启动World服务
     * 
     * @author:LKJ 2014-9-6
     */
    @RequestMapping
    public ModelAndView runWorld(HttpServletResponse response, WorldServerModel worldServerModel, HttpSession session) throws Exception {
        if (worldServerModel != null && worldServerModel.getWorldId() > 0) {
            WorldServer worldServer = worldServerService.get(worldServerModel.getWorldId());
            String result = worldServerService.startServer(worldServer);
            if (result.equals("true")) {
                String successStr = Global.WORLD_SUCCESS_RESULT;
                SshxcuteUtils.checkStartServer(worldServer, worldServerService, session, successStr);
            }
            response.getWriter().write(result);
        }
        SystemLogService.worldServerLog(session, ":启动ID为" + worldServerModel.getWorldId() + "的WORLD信息");
        return null;
    }

    /**
     * kill WorldServer服务
     * 
     * @author:LKJ 2014-9-6
     */
    @RequestMapping
    public void killWorld(HttpServletResponse response, WorldServerModel worldServerModel, HttpSession session) throws Exception {
        if (worldServerModel != null && worldServerModel.getWorldId() > 0) {
            WorldServer worldServer = worldServerService.get(worldServerModel.getWorldId());
            if (worldServer != null) {
                String result = worldServerService.killServer(worldServer);
                response.getWriter().write(result);
            } else {
                response.getWriter().write("ID为：" + worldServerModel.getWorldId() + "的WORLD不存在");
            }
        }
        SystemLogService.worldServerLog(session, ":停止ID为" + worldServerModel.getWorldId() + "的WORLD信息");
    }

    /**
     * 查看WorldServer操作日志
     * 
     * @author:LKJ 2014-9-6
     */
    @RequestMapping
    public ModelAndView showStdout(WorldServerModel worldServerModel, HttpSession session) throws Exception {
        session.setAttribute("stdout", "");
        if (worldServerModel != null && worldServerModel.getWorldId() > 0) {
            WorldServer worldServer = worldServerService.get(worldServerModel.getWorldId());
            if (worldServer != null) {
                resultMap = worldServerService.showStdout(worldServer, session);
            }
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("worldId", worldServerModel.getWorldId());
        return modelAndView;
    }

    /**
     * 部署WORLD服务（即將停用）
     * 
     * @author:LKJ 2014-9-25
     */
    @RequestMapping
    public ModelAndView deployWorld(HttpServletResponse response, WorldServerModel worldServerModel, HttpSession session) throws Exception {
        if (worldServerModel != null && worldServerModel.getWorldId() > 0) {
            WorldServer worldServer = worldServerService.get(worldServerModel.getWorldId());
            if (worldServer != null) {
                worldServerService.deployServer(response, worldServer, session);
            }
        }
        SystemLogService.worldServerLog(session, ":部署ID为" + worldServerModel.getWorldId() + "的WORLD信息");
        return null;
    }

    @RequestMapping
    public ModelAndView showDeployResult(HttpServletResponse response, HttpSession session, String worldId) {
        return new ModelAndView();
    }

    /**
     * 更新WORLD服务(停用)
     */
    @RequestMapping
    public ModelAndView updateWorld(HttpServletResponse response, WorldServerModel worldServerModel, HttpSession session) throws Exception {
        if (worldServerModel != null && worldServerModel.getWorldId() > 0) {
            WorldServer worldServer = worldServerService.get(worldServerModel.getWorldId());
            if (worldServer != null) {
                worldServerService.updateServer(response, worldServer, session);
            }
        }
        SystemLogService.worldServerLog(session, ":更新ID为" + worldServerModel.getWorldId() + "的WORLD信息");
        return null;
    }

    /**
     * 更新WORLDSERVER信息
     */
    @RequestMapping
    public String saveOrUpdate(WorldServer worldServers, HttpSession session) {
        worldServerService.saveOrUpdateWorld(worldServers);
        SystemLogService.worldServerLog(session, ":保存ID为" + worldServers.getId() + "的WORLD信息");
        return "redirect:worldServerList.action";
    }

    /**
     * 删除WorldServer
     * 
     * @author:LKJ 2014-11-28
     */
    @RequestMapping
    public void delete(HttpServletResponse response, WorldServer worldServers, HttpSession session) throws IOException {
        if (worldServers.getId() != null) {
            if (worldServerService.checkIsRelate(worldServers.getId())) {
                response.getWriter().write(Global.IS_RELATE);
            } else {
                worldServer = worldServerService.get(worldServers.getId());
                worldServerService.delete(worldServer);
                CacheService.initWorldServer();
                SystemLogService.worldServerLog(session, ":删除ID为" + worldServers.getId() + "的WORLD信息");
                response.getWriter().write("success");
            }
        }
    }

    /**
     * 批量关闭World
     * 
     * @author:LKJ 2014-12-17
     */
    @RequestMapping
    public ModelAndView batchCloseWorld(HttpServletResponse response, String worldIds, HttpSession session) throws Exception {
        if (!StringUtils.isEmpty(worldIds)) {
            session.removeAttribute("batchWorld");
            session.removeAttribute("batchWorldNum");
            worldServerService.batchCloseWorld(worldIds, session, null);
            response.getWriter().write("true");
            SystemLogService.worldServerLog(session, "批量关闭ID为：" + worldIds + "的World服务");
            return null;
        }
        return new ModelAndView();
    }

    /**
     * 批量开启World
     * 
     * @author:LKJ 2014-12-17
     */
    @RequestMapping
    public ModelAndView batchStartWorld(HttpServletResponse response, String worldIds, HttpSession session) throws Exception {
        if (!StringUtils.isEmpty(worldIds)) {
            StringBuilder sb = worldServerService.getAccountState(worldIds);
            if (sb != null && sb.length() > 0) {
                response.getWriter().write("ID为：" + sb + "的ACCOUNT未启动，请启动完再重试");
            } else {
                System.out.println("worldIds=" + worldIds);
                session.removeAttribute("batchWorld");
                session.removeAttribute("batchWorldNum");
                worldServerService.batchStartWorld(worldIds, session, null);
                response.getWriter().write("true");
                SystemLogService.worldServerLog(session, "批量开启ID为：" + worldIds + "的World服务");
                return null;
            }
        }
        return new ModelAndView();
    }

    /**
     * 同步world配置文件
     */
    @RequestMapping
    public void synchronizeConfig(HttpServletResponse response, HttpSession session, String worldIds) {
        try {
            if (!StringUtils.isEmpty(worldIds)) {
                worldServerService.synchronizeConfig(worldIds);
                CacheService.initWorldServer();
                response.getWriter().write("true");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过SCP更新world服务 beanType 这里必须为WorldServer pathType 这里必须为update
     * 
     * @author:LKJ 2014-9-25
     */
    @RequestMapping
    public void updateServerByScp(HttpServletResponse response, String worldIds, HttpSession session, String beanType, String pathType) {
        try {
            if (worldIds != null && !"".equals(worldIds)) {
                worldServer = CacheService.getWorldServerAllList().get(0);
                // 更新前将update文件夹里的文件拷到model中
                boolean synResult = SshxcuteUtils.updateServerByScp(null, null, worldServer.getUpdatePath(), worldServer.getModelPath(), CommanConstant.STATE_UPLOAD_ALL);
                if (synResult) {
                    // 获取本地文件MD5值
                    SshxcuteUtils.getJarMD5(session, beanType, pathType);
                    worldServerService.updateServerByScp(session, worldIds, worldServerService);
                    response.getWriter().write("true");
                    CacheService.initWorldServer();
                    SystemLogService.worldServerLog(session, ":更新ID为" + worldIds + "的world服务成功");
                } else {
                    response.getWriter().write("从update同步WorldServer服务到model文件夹失败，请检查");
                }
            }
        } catch (Exception e) {
            SystemLogService.worldServerLog(session, ":更新ID为" + worldIds + "的world服务出错");
            e.printStackTrace();
        }
    }

    /**
     * 上传文件夹下所有内容到指定位置（未使用）
     * 
     * @author:LKJ 2014-9-25
     */
    @RequestMapping
    public ModelAndView uploadAllDataToServer(HttpServletResponse response, WorldServerModel worldServerModel, HttpSession session) throws Exception {
        if (worldServerModel != null && worldServerModel.getWorldId() > 0) {
            WorldServer worldServer = worldServerService.get(worldServerModel.getWorldId());
            if (worldServer != null) {
                SshxcuteUtils.uploadAllDataToServer(worldServer);
            }
        }
        SystemLogService.worldServerLog(session, ":部署ID为" + worldServerModel.getWorldId() + "的WORLD信息");
        return null;
    }

    /**
     * 更新远程配置文件
     */
    @RequestMapping
    public void updateRemoteConfig(HttpServletResponse response, String worldIds, HttpSession session) {
        try {
            if (worldIds != null && !"".equals(worldIds)) {
                worldServerService.updateRemoteCfg(worldIds, Global.CONFIG_WORLD_PROPERTIES, worldServerService, session);
                response.getWriter().write("true");
                SystemLogService.worldServerLog(session, ":更新ID为" + worldIds + "的远程配置文件成功");
            }
        } catch (Exception e) {
            SystemLogService.worldServerLog(session, ":更新ID为" + worldIds + "的远程配置文件成功");
            e.printStackTrace();
        }
    }

    /**
     * 编辑DISPATCH
     * 
     * @author:LKJ
     */
    @RequestMapping
    public ModelAndView worldServerCreateInput(WorldServerModel worldServerModel, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("worldServer", worldServerService.getAll());
        modelAndView.addObject("server", serverService.getAll());
        modelAndView.addObject("model", modelService.getAll());
        SystemLogService.worldServerLog(session, ":编辑ID为" + worldServerModel.getWorldId() + "的WORLD信息");
        return modelAndView;
    }

    /**
     * 创建world
     */
    @RequestMapping
    public String worldServerCreate(HttpServletResponse response, WorldServer worldServers, HttpSession session) {
        try {
            String result = worldServerService.createWorldServer(worldServers, null);
            response.getWriter().print(result);
            SystemLogService.worldServerLog(session, ":保存ID为" + worldServers.getId() + "的WORLD信息");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 编辑DISPATCH
     * 
     * @author:LKJ
     */
    @RequestMapping
    public ModelAndView worldAndDispatchAdd(WorldServerModel worldServerModel, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        String battleIp = Application.getConfig("system", "battleIp");// 对战服ip
        modelAndView.addObject("battleIp", battleIp);
        modelAndView.addObject("worldServer", worldServerService.getAll());
        modelAndView.addObject("server", serverService.getAll());
        modelAndView.addObject("model", modelService.getAll());
        SystemLogService.worldServerLog(session, ":编辑ID为" + worldServerModel.getWorldId() + "的WORLD信息");
        return modelAndView;
    }

    /**
     * 创建world和dispatch记录
     */
    @SuppressWarnings("unchecked")
    @RequestMapping
    public void worldAndDispatchSave(HttpServletResponse response, HttpSession session, WorldServer worldServers, Integer dispatchServerId, Integer creatrNumber, String mergeIds) {
        try {
            Map<Integer, ServerType> serverTypeMap = (Map<Integer, ServerType>) session.getAttribute("serverTypeMap");
            int serverType = serverTypeMap.get(worldServers.getServerType()).getServerType();
            int orders = serverTypeMap.get(worldServers.getServerType()).getOrders();
            worldServers.setServerType(serverType);
            worldServers.setOrders(orders);
            String createWorldResult = worldServerService.createWorldServer(worldServers, mergeIds);
            if (!createWorldResult.equals("true")) {
                response.getWriter().print(createWorldResult);
                return;
            }
            WorldServer worldServer = worldServerService.get("machinecode", worldServers.getMachinecode());
            if (worldServer == null) {
                response.getWriter().print("world创建不成功");
                return;
            }
            DispatchServerService dispatchServerService = Application.getBean(DispatchServerService.class);
            DispatchServer dispatchServerMore = new DispatchServer();
            dispatchServerMore.setWorldId(worldServer.getId());
            dispatchServerMore.setServerId(worldServers.getServerId());
            dispatchServerMore.setServerType(worldServers.getServerType());
            dispatchServerMore.setMachinecode(worldServers.getMachinecode());
            dispatchServerMore.setDispatchServerId(dispatchServerId);
            dispatchServerMore.setIsDeploy(worldServers.getIsDeploy());
            dispatchServerMore.setOrders(orders);
            String dispatchResult = dispatchServerService.saveOrUpdateMoreDispatch(dispatchServerMore, creatrNumber);
            response.getWriter().print(dispatchResult);
            SystemLogService.worldServerLog(session, ":保存ID为" + worldServers.getId() + "的WORLD信息");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 编辑DISPATCH
     */
    @RequestMapping
    public ModelAndView changeServer(WorldServerModel worldServerModel, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("worldServer", worldServerService.getAll());
        modelAndView.addObject("server", serverService.getAll());
        SystemLogService.worldServerLog(session, ":编辑ID为" + worldServerModel.getWorldId() + "的WORLD信息");
        return modelAndView;
    }

    /**
     * 通过SCP部署服务
     */
    @RequestMapping
    public void deployServerByScp(HttpServletResponse response, HttpSession session, String worldIds, String jdbcType, String memory) {
        try {
            if (worldIds != null && !"".equals(worldIds)) {
                session.setAttribute("worldJdbcType", jdbcType);
                session.setAttribute("worldMemory", memory);
                worldServerService.deployServerByScp(session, worldIds, worldServerService);
                response.getWriter().write("true");
                SystemLogService.worldServerLog(session, ":部署ID为" + worldIds + "的服务成功");
            }
        } catch (Exception e) {
            SystemLogService.worldServerLog(session, ":部署ID为" + worldIds + "的服务成功");
            e.printStackTrace();
        }
    }

    /**
     * 更新Version配置文件
     */
    @RequestMapping
    public void synVersion(HttpSession session, HttpServletResponse response, String worldIds) {
        try {
            if (worldIds != null && !"".equals(worldIds)) {
                worldServerService.synVersion(session, worldIds, Global.CONFIG_WORLD_VERSION);
                response.getWriter().write("true");
                SystemLogService.worldServerLog(session, ":更新ID为" + worldIds + "的Version远程配置文件成功");
            }
        } catch (Exception e) {
            SystemLogService.worldServerLog(session, ":更新ID为" + worldIds + "的Version远程配置文件成功");
            e.printStackTrace();
        }
    }
}
