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
import com.zhwyd.server.bean.IpdServer;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.controller.model.IpdServerModel;
import com.zhwyd.server.service.GameService;
import com.zhwyd.server.service.IpdServerService;
import com.zhwyd.server.service.ModelService;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.service.impl.SystemLogService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
import com.zhwyd.server.web.page.Pager;
@Controller
public class IpdserverController {
    @SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;
    @Autowired
    private IpdServerService  ipdServerService;
    @Autowired
    private ServerService     serverService;
    @Autowired
    private ModelService      modelService;
    @Autowired
    private GameService       gameService;
    Map<String, Object>       resultMap        = new HashMap<String, Object>();
    private IpdServer         ipdServer;

    /**
     * 获取IPD列表
     * 
     * @author:LKJ 2014-11-24
     */
    @RequestMapping
    public ModelAndView ipdServerList(IpdServerModel ipdServerModel, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        ipdServerModel.setGameId((Integer) session.getAttribute(Global.GAME_ID));
        Pager pager = ipdServerService.getIpdServerList(ipdServerModel);
        modelAndView.addObject("model", ipdServerModel);
        modelAndView.addObject("server", serverService.getAll());
        modelAndView.addObject("pager", pager);
        SystemLogService.ipdServerLog(session, ":查看IPD服务列表");
        return modelAndView;
    }

    /**
     * 编辑IPD
     * 
     * @author:LKJ 2014-11-24
     */
    @RequestMapping
    public ModelAndView ipdServerInput(IpdServerModel ipdServerModel, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        if (ipdServerModel != null && ipdServerModel.getIpdId() != null && ipdServerModel.getIpdId() > 0) {
            ipdServer = ipdServerService.get(ipdServerModel.getIpdId());
            modelAndView.addObject("ipdServer", ipdServer);
        }
        modelAndView.addObject("server", serverService.getAll());
        modelAndView.addObject("model", modelService.getAll());
        modelAndView.addObject("game", gameService.getAll());
        SystemLogService.ipdServerLog(session, ":编辑ID为" + ipdServerModel.getIpdId() + "的IPD信息");
        return modelAndView;
    }

    /**
     * 启动ipd服务
     * 
     * @author:LKJ 2014-9-6
     */
    @RequestMapping
    public ModelAndView runIpd(HttpServletResponse response, IpdServerModel ipdServerModel, HttpSession session) throws Exception {
        if (ipdServerModel != null && ipdServerModel.getIpdId() > 0) {
            IpdServer ipdServer = ipdServerService.get(ipdServerModel.getIpdId());
            if (ipdServer != null) {
                String ipdStartResult = ipdServerService.startServer(ipdServer);
                if (ipdStartResult.equals("true")) {
                    String successStr = Global.IPD_SUCCESS_RESULT;
                    SshxcuteUtils.checkStartServer(ipdServer, ipdServerService, session, successStr);
                }
                response.getWriter().write(ipdStartResult);
            } else {
                response.getWriter().write("ID为：" + ipdServerModel.getIpdId() + "的IPD不存在");
            }
        }
        SystemLogService.ipdServerLog(session, ":启动ID为" + ipdServerModel.getIpdId() + "的IPD信息");
        return null;
    }

    /**
     * kill ipd服务
     * 
     * @author:LKJ 2014-9-6
     */
    @RequestMapping
    public void killIpd(HttpServletResponse response, IpdServerModel ipdServerModel, HttpSession session) throws Exception {
        if (ipdServerModel != null && ipdServerModel.getIpdId() > 0) {
            IpdServer ipdServer = ipdServerService.get(ipdServerModel.getIpdId());
            if (ipdServer != null) {
                String killIpdResult = ipdServerService.killServer(ipdServer);
                response.getWriter().write(killIpdResult);
            } else {
                response.getWriter().write("ID为：" + ipdServerModel.getIpdId() + "的IPD不存在");
            }
        }
        SystemLogService.ipdServerLog(session, ":停止ID为" + ipdServerModel.getIpdId() + "的IPD信息");
    }

    /**
     * 查看IPD操作日志
     * 
     * @author:LKJ 2014-9-6
     */
    @RequestMapping
    public ModelAndView showStdout(IpdServerModel ipdServerModel, HttpSession session) throws Exception {
        session.setAttribute("stdout", "");
        if (ipdServerModel != null && ipdServerModel.getIpdId() > 0) {
            ipdServer = ipdServerService.get(ipdServerModel.getIpdId());
            if (ipdServer != null) {
                resultMap = ipdServerService.showStdout(ipdServer, session);
            }
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("ipdId", ipdServerModel.getIpdId());
        return modelAndView;
    }

    /**
     * 部署IPD服务
     * 
     * @author:LKJ 2014-9-25
     */
    @RequestMapping
    public ModelAndView deployIpd(HttpServletResponse response, IpdServerModel ipdServerModel, HttpSession session) throws Exception {
        if (ipdServerModel != null && ipdServerModel.getIpdId() > 0) {
            ipdServer = ipdServerService.get(ipdServerModel.getIpdId());
            if (ipdServer != null) {
                ipdServerService.deployServer(response, ipdServer, session);
            }
        }
        SystemLogService.ipdServerLog(session, ":部署ID为" + ipdServerModel.getIpdId() + "的IPD信息");
        return null;
    }

    @RequestMapping
    public ModelAndView showDeployResult(HttpServletResponse response, HttpSession session, String ipdId) {
        return new ModelAndView();
    }

    /**
     * 更新IPD服务
     * 
     * @author:LKJ 2014-9-25
     */
    @RequestMapping
    public ModelAndView updateIpd(HttpServletResponse response, IpdServerModel ipdServerModel, HttpSession session) throws Exception {
        if (ipdServerModel != null && ipdServerModel.getIpdId() > 0) {
            ipdServer = ipdServerService.get(ipdServerModel.getIpdId());
            if (ipdServer != null) {
                ipdServerService.updateServer(response, ipdServer, session);
            }
        }
        SystemLogService.ipdServerLog(session, ":更新ID为" + ipdServerModel.getIpdId() + "的IPD信息");
        return null;
    }

    /**
     * 更新IPDSERVER信息
     */
    @RequestMapping
    public String saveOrUpdate(IpdServer ipdServers, HttpSession session) {
        if (ipdServers.getId() == null) {
            ipdServer = new IpdServer();
        } else {
            ipdServer = ipdServerService.get(ipdServers.getId());
        }
        ipdServer.setServerId(ipdServers.getServerId());
        ipdServer.setGameId(ipdServers.getGameId());
        ipdServer.setName(ipdServers.getName());
        ipdServer.setLocalip(ipdServers.getLocalip());
        ipdServer.setPort(ipdServers.getPort());
        ipdServer.setHttp(ipdServers.getHttp());
        ipdServer.setPath(ipdServers.getPath());
        ipdServer.setState(ipdServers.getState());
        ipdServer.setIsDeploy(ipdServers.getIsDeploy());
        ipdServer.setModelPath(ipdServers.getModelPath());
        ipdServer.setUpdatePath(ipdServers.getUpdatePath());
        ipdServer.setIsMain(ipdServers.getIsMain());
        ipdServerService.saveOrUpdate(ipdServer);
        CacheService.initIpdServer();
        SystemLogService.ipdServerLog(session, ":保存ID为" + ipdServers.getId() + "的IPD信息");
        return "redirect:ipdServerList.action";
    }

    /**
     * 删除IpdServer
     * 
     * @author:LKJ 2014-11-28
     */
    @RequestMapping
    public void delete(HttpServletResponse response, IpdServer ipdServers, HttpSession session) throws IOException {
        if (ipdServers.getId() != null) {
            if (ipdServerService.checkIsRelate(ipdServers.getId())) {
                response.getWriter().write(Global.IS_RELATE);
            } else {
                IpdServer ipdServer = ipdServerService.get(ipdServers.getId());
                ipdServerService.delete(ipdServer);
                CacheService.initIpdServer();
                SystemLogService.ipdServerLog(session, ":删除ID为" + ipdServers.getId() + "的IPD信息");
                response.getWriter().write("success");
            }
        }
    }

    /**
     * 批量关闭IPD
     * 
     * @author:LKJ 2014-12-17
     */
    @RequestMapping
    public ModelAndView batchCloseIpd(HttpServletResponse response, String ipdIds, HttpSession session) throws Exception {
        if (!StringUtils.isEmpty(ipdIds)) {
            session.removeAttribute("batchIpd");
            session.removeAttribute("batchIpdNum");
            ipdServerService.batchCloseIpd(ipdIds, session);
            response.getWriter().write("true");
            SystemLogService.ipdServerLog(session, "批量关闭ID为：" + ipdIds + "的IPD服务");
            ipdServerService.updateSession(ipdIds, session);
            return null;
        }
        return new ModelAndView();
    }

    /**
     * 批量关闭IPD
     * 
     * @author:LKJ 2014-12-17
     */
    @RequestMapping
    public ModelAndView batchStartIpd(HttpServletResponse response, String ipdIds, HttpSession session) throws Exception {
        if (!StringUtils.isEmpty(ipdIds)) {
            session.removeAttribute("batchIpd");
            session.removeAttribute("batchIpdNum");
            ipdServerService.batchStartIpd(ipdIds, session);
            response.getWriter().write("true");
            SystemLogService.ipdServerLog(session, "批量开启ID为：" + ipdIds + "的IPD服务");
            ipdServerService.updateSession(ipdIds, session);
            return null;
        }
        return new ModelAndView();
    }

    /**
     * 同步world配置文件
     */
    @RequestMapping
    public void synchronizeConfig(HttpServletResponse response, HttpSession session, String ipdIds) {
        try {
            if (!StringUtils.isEmpty(ipdIds)) {
                ipdServerService.synchronizeConfig(ipdIds);
                CacheService.initIpdServer();
                response.getWriter().write("true");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通关SCP更新IPD服务
     * 
     * @author:LKJ 2014-9-25
     */
    @RequestMapping
    public void updateServerByScp(HttpServletResponse response, String ipdIds, HttpSession session, String beanType, String pathType) throws Exception {
        try {
            if (ipdIds != null && !"".equals(ipdIds)) {
                ipdServer = CacheService.getIpdServerAllList().get(0);
                boolean synResult = SshxcuteUtils.updateServerByScp(null, null, ipdServer.getUpdatePath(), ipdServer.getModelPath(), CommanConstant.STATE_UPLOAD_ALL);
                if (synResult) {
                    // 获取本地文件包MD5值
                    SshxcuteUtils.getJarMD5(session, beanType, pathType);
                    ipdServerService.updateServerByScp(session, ipdIds, ipdServerService);
                    response.getWriter().write("true");
                    CacheService.initIpdServer();
                    SystemLogService.ipdServerLog(session, ":更新ID为" + ipdIds + "的IPD服务");
                } else {
                    response.getWriter().write("从update同步IPD服务到model文件夹失败，请检查");
                }
            }
        } catch (Exception e) {
            SystemLogService.ipdServerLog(session, ":更新ID为" + ipdIds + "的IPD服务出错");
            e.printStackTrace();
        }
    }

    /**
     * 上传文件夹下所有内容到指定位置（未使用）
     */
    @RequestMapping
    public ModelAndView uploadAllDataToServer(HttpServletResponse response, IpdServerModel ipdServerModel, HttpSession session) throws Exception {
        if (ipdServerModel != null && ipdServerModel.getIpdId() > 0) {
            ipdServer = ipdServerService.get(ipdServerModel.getIpdId());
            if (ipdServer != null) {
                SshxcuteUtils.uploadAllDataToServer(ipdServer);
            }
        }
        SystemLogService.ipdServerLog(session, ":部署ID为" + ipdServerModel.getIpdId() + "的IPD信息");
        return null;
    }

    /**
     * 更新远程配置文件
     */
    @RequestMapping
    public void updateRemoteConfig(HttpServletResponse response, String ipdIds, HttpSession session) {
        try {
            if (ipdIds != null && !"".equals(ipdIds)) {
                ipdServerService.updateRemoteCfg(ipdIds, Global.CONFIG_IPD_PROPERTIES, ipdServerService, session);
                response.getWriter().write("true");
                SystemLogService.ipdServerLog(session, ":更新ID为" + ipdIds + "的远程配置文件成功");
            }
        } catch (Exception e) {
            SystemLogService.ipdServerLog(session, ":更新ID为" + ipdIds + "的远程配置文件成功");
            e.printStackTrace();
        }
    }
}
