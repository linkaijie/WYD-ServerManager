package com.zhwyd.server.controller;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.util.CryptionUtil;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.controller.model.ServerModel;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.service.impl.SystemLogService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
import com.zhwyd.server.sshxcute.pool.ConnectionPoolManager;
import com.zhwyd.server.web.page.Pager;
@Controller
public class ServerController {
    @Autowired
    private ServerService serverService;
    private Server        server;

    @RequestMapping
    public ModelAndView serverList(ServerModel serverModel) throws Exception {
        List<Server> serverList = serverService.getServerList(serverModel);
        Pager pager = serverModel.getPager();
        pager.setList(serverList);
        pager.setTotalCount(serverList.size());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("server", serverService.getAll());
        modelAndView.addObject("model", serverModel);
        return modelAndView;
    }

    /**
     * 编辑Server
     */
    @RequestMapping
    public ModelAndView serverInput(Server servers, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        if (servers != null && servers.getId() != null && servers.getId() > 0) {
            server = serverService.get(servers.getId());
            String psw = CryptionUtil.getDecryptString(server.getSshPwd(), Application.getConfig("system", "key"));
            server.setSshPwd(psw);
            modelAndView.addObject("server", server);
        }
        return modelAndView;
    }

    /**
     * 更新Server信息
     */
    @RequestMapping
    public String saveOrUpdate(HttpServletRequest request, Server servers) {
        try {
            if (servers.getId() == null) {
                server = new Server();
                server.setCreateTime(new Date());
            } else {
                server = serverService.get(servers.getId());
                server.setUpdateTime(new Date());
            }
            server.setServerName(servers.getServerName());
            server.setServerIp(servers.getServerIp());
            server.setServerPort(servers.getServerPort());
            server.setHttpPort(servers.getHttpPort());
            server.setSshName(servers.getSshName());
            String psw = CryptionUtil.getEncryptString(servers.getSshPwd(), Application.getConfig("system", "key"));
            server.setSshPwd(psw);
            server.setSshUrl(servers.getSshUrl());
            server.setUseType(servers.getUseType());
            server.setRemark(servers.getRemark());
            serverService.saveOrUpdate(server);
            CacheService.initServer();
            if (servers.getId() == null) {
                // 授权
                Object[] copyResult = SshxcuteUtils.copySshRsa(request, servers);
                if (copyResult == null || copyResult.length <= 0 || (Integer) copyResult[0] != CommanConstant.RESULT_TRUE_STATE) {
                    throw new Exception("授权失败");
                } else {
                    server.setRsaTime(new Date());
                    serverService.update(server);
                }
                // 创建连接池连接
                ConnectionPoolManager.getInstance().newPool(server);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:serverList.action";
    }

    /**
     * 删除SERVER
     */
    @RequestMapping
    public void delete(HttpServletResponse response, Server servers, HttpSession session) throws IOException {
        if (servers.getId() != null) {
            server = serverService.get(servers.getId());
            serverService.delete(servers);
            CacheService.initServer();
            SystemLogService.serverManegeLog(session, ":删除ID为" + servers.getId() + "的Server信息");
            response.getWriter().write("success");
        }
        // return "redirect:accountServerList.action";
    }

    /**
     * 添加公钥
     */
    @RequestMapping
    public void copySshRsa(HttpServletRequest request, HttpServletResponse response, HttpSession session, Server servers) {
        try {
            if (servers != null && servers.getId() > 0) {
                server = CacheService.getServerById(servers.getId());
                Object[] copyResult = SshxcuteUtils.copySshRsa(request, server);
                if (copyResult == null || copyResult.length <= 0 || (Integer) copyResult[0] != CommanConstant.RESULT_TRUE_STATE) {
                    response.getWriter().write(copyResult[1].toString());
                } else {
                    response.getWriter().write("success");
                    server = serverService.get(servers.getId());
                    server.setRsaTime(new Date());
                    serverService.update(server);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * 通关SCP更新IPD服务
    */
    @RequestMapping
    public void updateServerByScp(HttpServletResponse response, String ids, HttpSession session, String beanType, String pathType, Integer type) throws Exception {
        try {
            if (ids != null && !"".equals(ids)) {
                server = CacheService.getServerAllList().get(0);
                server.setType(type);
                // 更新前将update文件夹里的文件拷到model中
                boolean synResult = SshxcuteUtils.updateServerByScp(null, null, server.getUpdatePath(), server.getModelPath(), CommanConstant.STATE_UPLOAD_ALL);
                if (synResult) {
                    // 获取本地文件包MD5值
                    SshxcuteUtils.getJarMD5(session, beanType, pathType);
                    serverService.updateServerByScp(session, ids, type);
                    response.getWriter().write("true");
                    CacheService.initServer();
                    SystemLogService.serverManegeLog(session, ":更新ID为" + ids + "的serverLib服务");
                } else {
                    response.getWriter().write("从update同步Server服务到model文件夹失败，请检查");
                }
            }
        } catch (Exception e) {
            SystemLogService.serverManegeLog(session, ":更新ID为" + ids + "的serverLib服务出错");
            e.printStackTrace();
        }
    }

    /**
     * 编辑Server
     */
    @RequestMapping
    public ModelAndView commandInput() {
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    /**
     * 通关SCP更新IPD服务
     */
    @RequestMapping
    public void runCommand(HttpServletResponse response, String ids, HttpSession session, String command) throws Exception {
        try {
            if (ids != null && !"".equals(ids) && !StringUtils.isEmpty(command)) {
                serverService.runCommand(session, ids, command);
                response.getWriter().write("true");
                CacheService.initServer();
                SystemLogService.serverManegeLog(session, ":更新ID为" + ids + "的serverLib服务");
            }
        } catch (Exception e) {
            SystemLogService.serverManegeLog(session, ":更新ID为" + ids + "的serverLib服务出错");
            e.printStackTrace();
        }
    }
}
