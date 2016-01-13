package com.zhwyd.server.controller;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.ServerType;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.controller.model.DispatchServerModel;
import com.zhwyd.server.service.DispatchServerService;
import com.zhwyd.server.service.GameService;
import com.zhwyd.server.service.IpdServerService;
import com.zhwyd.server.service.ModelService;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.service.WorldServerService;
import com.zhwyd.server.service.impl.SystemLogService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
import com.zhwyd.server.web.page.Pager;
@Controller
public class DispatchserverController {
    @SuppressWarnings("unused")
    private static final long     serialVersionUID = 1L;
    @Autowired
    private DispatchServerService dispatchServerService;
    @Autowired
    private ServerService         serverService;
    @Autowired
    private WorldServerService    worldServerService;
    @Autowired
    private IpdServerService      ipdServerService;
    @Autowired
    private ModelService          modelService;
    @Autowired
    private GameService           gameService;
    private DispatchServer        dispatchServer;
    Map<String, Object>           resultMap        = new HashMap<String, Object>();

    /**
     * 获取dispatchServer服务列表
     * 
     * @author:LKJ 2014-9-9
     */
    @RequestMapping
    public ModelAndView dispatchServerList(DispatchServerModel dispatchServerModel, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        dispatchServerModel.setGameId((Integer) session.getAttribute(Global.GAME_ID));
        Pager pager = dispatchServerService.getDispatchServerList(dispatchServerModel);
        modelAndView.addObject("model", dispatchServerModel);
        modelAndView.addObject("server", serverService.getAll());
        modelAndView.addObject("pager", pager);
        SystemLogService.dispatchServerLog(session, ":查看DISPATCH服务列表");
        return modelAndView;
    }

    /**
     * 编辑DISPATCH
     * 
     * @author:LKJ
     */
    @RequestMapping
    public ModelAndView dispatchServerInput(DispatchServerModel dispatchServerModel, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        if (dispatchServerModel != null && dispatchServerModel.getDispatchId() != null && dispatchServerModel.getDispatchId() > 0) {
            dispatchServer = dispatchServerService.get(dispatchServerModel.getDispatchId());
            modelAndView.addObject("dispatchServer", dispatchServer);
        }
        modelAndView.addObject("worldServer", worldServerService.getAll());
        modelAndView.addObject("ipdServer", ipdServerService.getAll());
        modelAndView.addObject("server", serverService.getAll());
        modelAndView.addObject("model", modelService.getAll());
        modelAndView.addObject("game", gameService.getAll());
        SystemLogService.dispatchServerLog(session, ":编辑ID为" + dispatchServerModel.getDispatchId() + "的DISPATCH信息");
        return modelAndView;
    }

    /**
     * 编辑DISPATCH
     * 
     * @author:LKJ
     */
    @RequestMapping
    public ModelAndView dispatchServerMoreInput(DispatchServerModel dispatchServerModel, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("worldServer", worldServerService.getAll());
        modelAndView.addObject("ipdServer", ipdServerService.getAll());
        modelAndView.addObject("server", serverService.getAll());
        modelAndView.addObject("model", modelService.getAll());
        modelAndView.addObject("game", gameService.getAll());
        SystemLogService.dispatchServerLog(session, ":编辑ID为" + dispatchServerModel.getDispatchId() + "的DISPATCH信息");
        return modelAndView;
    }

    /**
     * 编辑DISPATCH
     * 
     * @author:LKJ
     */
    @RequestMapping
    public ModelAndView dispatchServerMoreCreate(DispatchServerModel dispatchServerModel, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("worldServer", worldServerService.getAll());
        modelAndView.addObject("server", serverService.getAll());
        SystemLogService.dispatchServerLog(session, ":编辑ID为" + dispatchServerModel.getDispatchId() + "的DISPATCH信息");
        return modelAndView;
    }

    /**
     * 启动Dispatch服务
     * 
     * @author:LKJ 2014-9-6
     */
    @RequestMapping
    public ModelAndView runDispatch(HttpServletResponse response, DispatchServerModel dispatchServerModel, HttpSession session) throws Exception {
        if (dispatchServerModel != null && dispatchServerModel.getDispatchId() > 0) {
            DispatchServer dispatchServer = dispatchServerService.get(dispatchServerModel.getDispatchId());
            if (dispatchServer != null) {
                String string = dispatchServerService.startServer(dispatchServer);
                if (string.equals("true")) {
                    String successStr = Global.DISPATCH_SUCCESS_RESULT;
                    SshxcuteUtils.checkStartServer(dispatchServer, dispatchServerService, session, successStr);
                }
                response.getWriter().write(string);
            } else {
                response.getWriter().write("ID为：" + dispatchServerModel.getDispatchId() + "的DISPATCH不存在");
            }
        }
        SystemLogService.dispatchServerLog(session, ":启动ID为" + dispatchServerModel.getDispatchId() + "的DISPATCH信息");
        return null;
    }

    /**
     * kill Dispatch服务
     * 
     * @author:LKJ 2014-9-6
     */
    @RequestMapping
    public void killDispatch(HttpServletResponse response, DispatchServerModel dispatchServerModel, HttpSession session) throws Exception {
        if (dispatchServerModel != null && dispatchServerModel.getDispatchId() > 0) {
            DispatchServer dispatchServer = dispatchServerService.get(dispatchServerModel.getDispatchId());
            if (dispatchServer != null) {
                String string = dispatchServerService.killServer(dispatchServer);
                response.getWriter().write(string);
            } else {
                response.getWriter().write("ID为：" + dispatchServerModel.getDispatchId() + "的DISPATCH不存在");
            }
        }
        SystemLogService.dispatchServerLog(session, ":停止ID为" + dispatchServerModel.getDispatchId() + "的DISPATCH信息");
    }

    /**
     * 查看dispatch操作日志
     * 
     * @author:LKJ 2014-9-6
     */
    @RequestMapping
    public ModelAndView showStdout(DispatchServerModel dispatchServerModel, HttpSession session) throws Exception {
        session.setAttribute("stdout", "");
        if (dispatchServerModel != null && dispatchServerModel.getDispatchId() > 0) {
            dispatchServer = dispatchServerService.get(dispatchServerModel.getDispatchId());
            if (dispatchServer != null) {
                resultMap = dispatchServerService.showStdout(dispatchServer, session);
            }
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("dispatchId", dispatchServerModel.getDispatchId());
        return modelAndView;
    }

    /**
     * 部署Dispatch服务
     * 
     * @author:LKJ 2014-9-25
     */
    @RequestMapping
    public ModelAndView deployDispatch(HttpServletResponse response, DispatchServerModel dispatchServerModel, HttpSession session) throws Exception {
        if (dispatchServerModel != null && dispatchServerModel.getDispatchId() > 0) {
            dispatchServer = dispatchServerService.get(dispatchServerModel.getDispatchId());
            if (dispatchServer != null) {
                dispatchServerService.deployServer(response, dispatchServer, session);
            }
        }
        SystemLogService.dispatchServerLog(session, ":部署ID为" + dispatchServerModel.getDispatchId() + "的DISPATCH信息");
        return null;
    }

    @RequestMapping
    public ModelAndView showDeployResult(HttpServletResponse response, HttpSession session, String dispatchId) {
        return new ModelAndView();
    }

    /**
     * 更新Dispatch服务
     * 
     * @author:LKJ 2014-9-25
     */
    @RequestMapping
    public ModelAndView updateDispatch(HttpServletResponse response, DispatchServerModel dispatchServerModel, HttpSession session) throws Exception {
        if (dispatchServerModel != null && dispatchServerModel.getDispatchId() > 0) {
            dispatchServer = dispatchServerService.get(dispatchServerModel.getDispatchId());
            if (dispatchServer != null) {
                dispatchServerService.updateServer(response, dispatchServer, session);
            }
        }
        SystemLogService.dispatchServerLog(session, ":更新ID为" + dispatchServerModel.getDispatchId() + "的DISPATCH信息");
        return null;
    }

    /**
     * 更新DispatchServer信息
     */
    @RequestMapping
    public String saveOrUpdate(DispatchServer dispatchServers, HttpSession session) {
        dispatchServerService.saveOrUpdateDispatch(dispatchServers);
        SystemLogService.dispatchServerLog(session, ":保存ID为" + dispatchServers.getId() + "的DISPATCH信息");
        return "redirect:dispatchServerList.action";
    }

    /**
     * 更新DispatchServer信息
     */
    @SuppressWarnings("unchecked")
    @RequestMapping
    public String saveOrUpdateMore(DispatchServer dispatchServers, Integer creatrNumber, String serverTypes, HttpSession session) {
        Map<Integer, ServerType> serverTypeMap = (Map<Integer, ServerType>) session.getAttribute("serverTypeMap");
        int orders = serverTypeMap.get(serverTypes).getOrders();
        dispatchServers.setOrders(orders);
        dispatchServerService.saveOrUpdateMore(dispatchServers, creatrNumber, serverTypes);
        SystemLogService.dispatchServerLog(session, ":保存ID为" + dispatchServers.getId() + "的DISPATCH信息");
        return "redirect:dispatchServerList.action";
    }

    /**
     * 删除DISPATCH
     * 
     * @author:LKJ 2014-11-28
     */
    @RequestMapping
    public String delete(DispatchServer dispatchServers, HttpSession session) {
        if (dispatchServers.getId() != null) {
            dispatchServer = dispatchServerService.get(dispatchServers.getId());
            dispatchServerService.delete(dispatchServer);
            CacheService.initDispatchServer();
            SystemLogService.dispatchServerLog(session, ":删除ID为" + dispatchServers.getId() + "的DISPATCH信息");
        }
        return "redirect:dispatchServerList.action";
    }

    /**
     * 批量关闭ACCOUNT
     * 
     * @author:LKJ 2014-12-17
     */
    @RequestMapping
    public ModelAndView batchCloseDispatch(HttpServletResponse response, String dispatchIds, HttpSession session) throws Exception {
        if (!StringUtils.isEmpty(dispatchIds)) {
            session.removeAttribute("batchDispatch");
            session.removeAttribute("batchDispatchNum");
            dispatchServerService.batchCloseDispatch(dispatchIds, session);
            response.getWriter().write("true");
            SystemLogService.dispatchServerLog(session, "批量关闭ID为：" + dispatchIds + "的Dispatch服务");
            return null;
        }
        return new ModelAndView();
    }

    /**
     * 批量开启ACCOUNT
     * 
     * @author:LKJ 2014-12-17
     */
    @RequestMapping
    public ModelAndView batchStartDispatch(HttpServletResponse response, String dispatchIds, HttpSession session) throws Exception {
        if (!StringUtils.isEmpty(dispatchIds)) {
            StringBuilder ipdState = dispatchServerService.getIpdState(dispatchIds);
            StringBuilder worldState = dispatchServerService.getWorldState(dispatchIds);
            System.out.println("dispatchIds=" + dispatchIds);
            if ((ipdState != null && ipdState.length() > 0)) {
                response.getWriter().write("ID为：" + ipdState + "的IPD未启动，请启动完再重试");
            } else if (worldState != null && worldState.length() > 0) {
                response.getWriter().write("ID为：" + worldState + "的WORLD未启动，请启动完再重试");
            } else {
                session.removeAttribute("batchDispatch");
                session.removeAttribute("batchDispatchNum");
                dispatchServerService.batchStartDispatch(dispatchIds, session);
                response.getWriter().write("true");
                SystemLogService.dispatchServerLog(session, "批量开启ID为：" + dispatchIds + "的Dispatch服务");
                return null;
            }
        }
        return new ModelAndView();
    }

    /**
     * 同步Dispatch配置文件
     */
    @RequestMapping
    public void synchronizeConfig(HttpServletResponse response, HttpSession session, String dispatchIds) {
        try {
            if (!StringUtils.isEmpty(dispatchIds)) {
                dispatchServerService.synchronizeConfig(dispatchIds);
                CacheService.initDispatchServer();
                response.getWriter().write("true");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通关SCP更新Dispatch服务
     * 
     * @author:LKJ 2014-9-25
     */
    @RequestMapping
    public void updateServerByScp(HttpServletResponse response, String dispatchIds, HttpSession session, String beanType, String pathType) throws Exception {
        try {
            if (dispatchIds != null && !"".equals(dispatchIds)) {
                dispatchServer = CacheService.getDispatchServerAllList().get(0);
                // 更新前将update文件夹里的文件拷到model中
                boolean synResult = SshxcuteUtils.updateServerByScp(null, null, dispatchServer.getUpdatePath(), dispatchServer.getModelPath(), CommanConstant.STATE_UPLOAD_ALL);
                if (synResult) {
                    // 获取本地文件包MD5值
                    SshxcuteUtils.getJarMD5(session, beanType, pathType);
                    dispatchServerService.updateServerByScp(session, dispatchIds, dispatchServerService);
                    response.getWriter().write("true");
                    CacheService.initDispatchServer();
                    SystemLogService.dispatchServerLog(session, ":更新ID为" + dispatchIds + "的Dispatch服务");
                } else {
                    response.getWriter().write("从update同步Dispatch服务到model文件夹失败，请检查");
                }
            }
        } catch (Exception e) {
            SystemLogService.dispatchServerLog(session, ":更新ID为" + dispatchIds + "的Dispatch服务出错");
            e.printStackTrace();
        }
    }

    /**
     * 上传文件夹下所有内容到指定位置（未使用）
     */
    @RequestMapping
    public ModelAndView uploadAllDataToServer(HttpServletResponse response, DispatchServerModel dispatchServerModel, HttpSession session) throws Exception {
        if (dispatchServerModel != null && dispatchServerModel.getDispatchId() > 0) {
            dispatchServer = dispatchServerService.get(dispatchServerModel.getDispatchId());
            if (dispatchServer != null) {
                SshxcuteUtils.uploadAllDataToServer(dispatchServer);
            }
        }
        SystemLogService.dispatchServerLog(session, ":部署ID为" + dispatchServerModel.getDispatchId() + "的DISPATCH信息");
        return null;
    }

    /**
     * 更新远程配置文件
     */
    @RequestMapping
    public void updateRemoteConfig(HttpServletResponse response, String dispatchIds, HttpSession session) {
        try {
            if (dispatchIds != null && !"".equals(dispatchIds)) {
                dispatchServerService.updateRemoteCfg(dispatchIds, Global.CONFIG_DISPATCH_PROPERTIES, dispatchServerService, session);
                response.getWriter().write("true");
                SystemLogService.dispatchServerLog(session, ":更新ID为" + dispatchIds + "的远程配置文件成功");
            }
        } catch (Exception e) {
            SystemLogService.dispatchServerLog(session, ":更新ID为" + dispatchIds + "的远程配置文件成功");
            e.printStackTrace();
        }
    }

    /**
     * 更新远程配置文件
     */
    @RequestMapping
    public void saveOrUpdateMoreDispatch(HttpServletResponse response, DispatchServer dispatchServer, HttpSession session, Integer creatrNumber) {
        try {
            String result = dispatchServerService.saveOrUpdateMoreDispatch(dispatchServer, creatrNumber);
            response.getWriter().print(result);
            SystemLogService.dispatchServerLog(session, ":保存ID为" + dispatchServer.getId() + "的WORLD信息");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过SCP部署服务
     */
    @RequestMapping
    public void deployServerByScp(HttpServletResponse response, HttpSession session, String dispatchIds) {
        try {
            if (dispatchIds != null && !"".equals(dispatchIds)) {
                dispatchServerService.deployServerByScp(session, dispatchIds, dispatchServerService);
                response.getWriter().write("true");
                SystemLogService.worldServerLog(session, ":部署ID为" + dispatchIds + "的服务成功");
            }
        } catch (Exception e) {
            SystemLogService.dispatchServerLog(session, ":部署ID为" + dispatchIds + "的服务成功");
            e.printStackTrace();
        }
    }
}
