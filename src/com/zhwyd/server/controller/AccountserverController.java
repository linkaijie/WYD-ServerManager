package com.zhwyd.server.controller;
import java.io.IOException;
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
import com.zhwyd.server.bean.AccountServer;
import com.zhwyd.server.bean.JdbcUrl;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.controller.model.AccountServerModel;
import com.zhwyd.server.service.AccountServerService;
import com.zhwyd.server.service.GameService;
import com.zhwyd.server.service.JdbcUrlService;
import com.zhwyd.server.service.ModelService;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.service.impl.SystemLogService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
import com.zhwyd.server.sshxcute.UploadDataToServer;
import com.zhwyd.server.web.page.Pager;
@Controller
public class AccountserverController {
    @SuppressWarnings("unused")
    private static final long    serialVersionUID = 1L;
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
    private AccountServer        accountServer;
    @SuppressWarnings("unused")
    private Map<String, Object>  resultMap        = new HashMap<String, Object>();

    /**
     * 获取ACCOUNT列表
     * 
     * @author:LKJ 2014-11-24
     */
    @RequestMapping
    public ModelAndView accountServerList(AccountServerModel accountServerModel, HttpSession session) {
        session.setAttribute("stdout", "");
        ModelAndView modelAndView = new ModelAndView();
        accountServerModel.setGameId((Integer) session.getAttribute(Global.GAME_ID));
        Pager pager = accountServerService.getAccountServerList(accountServerModel);
        List<JdbcUrl> jdbcUrlList = jdbcUrlService.getJdbcUrlList(CommanConstant.TYPE_JDBC_ACCOUNT);
        modelAndView.addObject("jdbcUrlList", jdbcUrlList);
        modelAndView.addObject("model", accountServerModel);
        modelAndView.addObject("server", serverService.getAll());
        modelAndView.addObject("pager", pager);
        SystemLogService.accountServerLog(session, ":查看ACCOUNT服务列表");
        return modelAndView;
    }

    /**
     * 编辑ACCOUNT
     * 
     * @author:LKJ 2014-11-24
     */
    @RequestMapping
    public ModelAndView accountServerInput(AccountServerModel accountServerModel, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        if (accountServerModel != null && accountServerModel.getAccountId() != null && accountServerModel.getAccountId() > 0) {
            accountServer = accountServerService.get(accountServerModel.getAccountId());
            modelAndView.addObject("accountServer", accountServer);
        }
        modelAndView.addObject("server", serverService.getAll());
        modelAndView.addObject("model", modelService.getAll());
        modelAndView.addObject("game", gameService.getAll());
        SystemLogService.accountServerLog(session, ":编辑ID为" + accountServerModel.getAccountId() + "的ACCOUNT信息");
        return modelAndView;
    }

    /**
     * 启动account服务
     * 
     * @author:LKJ 2014-9-6
     */
    @RequestMapping
    public ModelAndView runAccount(HttpServletResponse response, AccountServerModel accountServerModel, HttpSession session) throws Exception {
        if (accountServerModel != null && accountServerModel.getAccountId() > 0) {
            AccountServer accountServer = accountServerService.get(accountServerModel.getAccountId());
            if (accountServer != null) {
                String startResult = accountServerService.startServer(accountServer);
                if (startResult.equals("true")) {
                    String successStr = Global.ACCOUNT_SUCCESS_RESULT;
                    SshxcuteUtils.checkStartServer(accountServer, accountServerService, session, successStr);
                }
                response.getWriter().write(startResult);
            } else {
                response.getWriter().write("ID为：" + accountServerModel.getAccountId() + "的ACCOUNT不存在");
            }
        }
        SystemLogService.accountServerLog(session, ":启动ID为" + accountServerModel.getAccountId() + "的ACCOUNT服务");
        return null;
    }

    /**
     * kill account服务
     * 
     * @author:LKJ 2014-9-6
     */
    @RequestMapping
    public void killAccount(HttpServletResponse response, AccountServerModel accountServerModel, HttpSession session) throws Exception {
        if (accountServerModel != null && accountServerModel.getAccountId() > 0) {
            AccountServer accountServer = accountServerService.get(accountServerModel.getAccountId());
            if (accountServer != null) {
                String killAccountResult = accountServerService.killServer(accountServer);
                response.getWriter().write(killAccountResult);
            } else {
                response.getWriter().write("ID为：" + accountServerModel.getAccountId() + "的ACCOUNT不存在");
            }
            SystemLogService.accountServerLog(session, ":停止ID为" + accountServerModel.getAccountId() + "的ACCOUNT服务");
        }
    }

    /**
     * 查看account操作日志
     * 
     * @author:LKJ 2014-9-6
     */
    @RequestMapping
    public ModelAndView showStdout(AccountServerModel accountServerModel, HttpSession session) throws Exception {
        session.setAttribute("stdout", "");
        if (accountServerModel != null && accountServerModel.getAccountId() > 0) {
            accountServer = accountServerService.get(accountServerModel.getAccountId());
            if (accountServer != null) {
                resultMap = accountServerService.showStdout(accountServer, session);
            }
        }
        // return new ModelAndView("redirect:../stdoutlog.jsp");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("accountId", accountServerModel.getAccountId());
        return modelAndView;
    }

    /**
     * 部署ACCOUNT服务
     * 
     * @author:LKJ 2014-9-25
     */
    @RequestMapping
    public ModelAndView deployAccount(HttpServletResponse response, AccountServerModel accountServerModel, HttpSession session) throws Exception {
        if (accountServerModel != null && accountServerModel.getAccountId() > 0) {
            AccountServer accountServer = accountServerService.get(accountServerModel.getAccountId());
            if (accountServer != null) {
                accountServerService.deployServer(response, accountServer, session);
            }
        }
        SystemLogService.accountServerLog(session, ":部署ID为" + accountServerModel.getAccountId() + "的ACCOUNT服务");
        return null;
    }

    /**
     * 上传文件夹下所有内容到指定位置（未使用）
     */
    @RequestMapping
    public ModelAndView uploadAllDataToServer(HttpServletResponse response, AccountServerModel accountServerModel, HttpSession session) throws Exception {
        if (accountServerModel != null && accountServerModel.getAccountId() > 0) {
            AccountServer accountServer = accountServerService.get(accountServerModel.getAccountId());
            if (accountServer != null) {
                SshxcuteUtils.uploadAllDataToServer(accountServer);
            }
        }
        SystemLogService.accountServerLog(session, ":部署ID为" + accountServerModel.getAccountId() + "的ACCOUNT服务");
        return null;
    }

    @RequestMapping
    public ModelAndView showDeployResult(HttpServletResponse response, HttpSession session, String accountId) {
        return new ModelAndView();
    }

    /**
     * 更新ACCOUNT服务（停用）
     * 
     * @author:LKJ 2014-9-25
     */
    @RequestMapping
    public ModelAndView updateAccount(HttpServletResponse response, AccountServerModel accountServerModel, HttpSession session) throws Exception {
        if (accountServerModel != null && accountServerModel.getAccountId() > 0) {
            AccountServer accountServer = accountServerService.get(accountServerModel.getAccountId());
            if (accountServer != null) {
                accountServerService.updateServer(response, accountServer, session);
            }
        }
        SystemLogService.accountServerLog(session, ":更新ID为" + accountServerModel.getAccountId() + "的ACCOUNT服务");
        return null;
    }

    /**
     * 通关SCP更新ACCOUNT服务
     * 
     * @author:LKJ 2014-9-25
     */
    @RequestMapping
    public void updateServerByScp(HttpServletResponse response, String accountIds, HttpSession session, String beanType, String pathType) throws Exception {
        try {
            if (accountIds != null && !"".equals(accountIds)) {
                accountServer = CacheService.getAccountServerAllList().get(0);
                // 更新前将update文件夹里的文件拷到model中
                boolean synResult = SshxcuteUtils.updateServerByScp(null, null, accountServer.getUpdatePath(), accountServer.getModelPath(), CommanConstant.STATE_UPLOAD_ALL);
                if (synResult) {
                    // 获取本地文件包MD5值
                    SshxcuteUtils.getJarMD5(session, beanType, pathType);
                    accountServerService.updateServerByScp(session, accountIds, accountServerService);
                    response.getWriter().write("true");
                    CacheService.initAccountServer();
                    SystemLogService.accountServerLog(session, ":更新ID为" + accountIds + "的ACCOUNT服务");
                } else {
                    response.getWriter().write("从update同步Account服务到model文件夹失败，请检查");
                }
            }
        } catch (Exception e) {
            SystemLogService.accountServerLog(session, ":更新ID为" + accountIds + "的ACCOUNT服务出错");
            e.printStackTrace();
        }
    }

    /**
     * 编辑ACCOUNTSERVER信息
     */
    @RequestMapping
    public String saveOrUpdate(AccountServer accountServers, HttpSession session) {
        if (accountServers.getId() == null) {
            accountServer = new AccountServer();
        } else {
            accountServer = accountServerService.get(accountServers.getId());
        }
        accountServer.setGameId(accountServers.getGameId());
        accountServer.setServerId(accountServers.getServerId());
        accountServer.setName(accountServers.getName());
        accountServer.setServerip(accountServers.getServerip());
        accountServer.setPort(accountServers.getPort());
        accountServer.setPath(accountServers.getPath());
        accountServer.setState(accountServers.getState());
        accountServer.setIsDeploy(accountServers.getIsDeploy());
        accountServer.setModelPath(accountServers.getModelPath());
        accountServer.setUpdatePath(accountServers.getUpdatePath());
        accountServerService.saveOrUpdate(accountServer);
        CacheService.initAccountServer();
        SystemLogService.accountServerLog(session, ":编辑ID为" + accountServers.getId() + "的ACCOUNT信息");
        return "redirect:accountServerList.action";
    }

    /**
     * 删除ACCOUNT
     */
    @RequestMapping
    public void delete(HttpServletResponse response, AccountServer accountServers, HttpSession session) throws IOException {
        if (accountServers.getId() != null) {
            if (accountServerService.checkIsRelate(accountServers.getId())) {
                response.getWriter().write(Global.IS_RELATE);
            } else {
                accountServer = accountServerService.get(accountServers.getId());
                accountServerService.delete(accountServer);
                CacheService.initAccountServer();
                SystemLogService.accountServerLog(session, ":删除ID为" + accountServers.getId() + "的ACCOUNT信息");
                response.getWriter().write("success");
            }
        }
        // return "redirect:accountServerList.action";
    }

    /**
     * 批量开启ACCOUNT
     * 
     * @author:LKJ 2014-12-17
     */
    @RequestMapping
    public ModelAndView batchStartAccount(HttpServletResponse response, String accountIds, HttpSession session) throws Exception {
        if (!StringUtils.isEmpty(accountIds)) {
            session.removeAttribute("batchAccount");
            session.removeAttribute("batchAccountNum");
            response.getWriter().write("true");
            accountServerService.batchStartAccount(accountIds, session);
            SystemLogService.accountServerLog(session, "批量开启ID为：" + accountIds + "的ACCOUNT服务");
            accountServerService.updateSession(accountIds, session);
            return null;
        }
        return new ModelAndView();
    }

    /**
     * 批量关闭ACCOUNT
     */
    @RequestMapping
    public ModelAndView batchCloseAccount(HttpServletResponse response, String accountIds, HttpSession session) throws Exception {
        if (!StringUtils.isEmpty(accountIds)) {
            session.removeAttribute("batchAccount");
            session.removeAttribute("batchAccountNum");
            accountServerService.batchCloseAccount(accountIds, session);
            response.getWriter().write("true");
            SystemLogService.accountServerLog(session, "批量关闭ID为：" + accountIds + "的ACCOUNT服务");
            accountServerService.updateSession(accountIds, session);
            return null;
        }
        return new ModelAndView();
    }

    /**
     * 同步ACCOUNT配置文件
     */
    @RequestMapping
    public void synchronizeConfig(HttpServletResponse response, HttpSession session, String accountIds) {
        try {
            if (!StringUtils.isEmpty(accountIds)) {
                accountServerService.synchronizeConfig(accountIds);
                CacheService.initAccountServer();
                response.getWriter().write("true");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件或文件夹到指定目录
     */
    @RequestMapping
    public void uploadDataToServer(HttpServletResponse response, HttpSession session, String serverId, String localPath, String toPath) {
        try {
            if (!StringUtils.isEmpty(serverId)) {
                UploadDataToServer.upload(serverId, localPath, toPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新远程配置文件
     */
    @RequestMapping
    public void updateRemoteConfig(HttpServletResponse response, String accountIds, HttpSession session) {
        try {
            if (accountIds != null && !"".equals(accountIds)) {
                accountServerService.updateRemoteCfg(accountIds, Global.CONFIG_ACCOUNT_PROPERTIES, accountServerService, session);
                response.getWriter().write("true");
                SystemLogService.accountServerLog(session, ":更新ID为" + accountIds + "的远程配置文件成功");
            }
        } catch (Exception e) {
            SystemLogService.accountServerLog(session, ":更新ID为" + accountIds + "的远程配置文件成功");
            e.printStackTrace();
        }
    }

    /**
     * 通过SCP部署服务
     */
    @RequestMapping
    public void deployServerByScp(HttpServletResponse response, HttpSession session, String accountIds, String jdbcType) {
        try {
            if (accountIds != null && !"".equals(accountIds)) {
                session.setAttribute("accountJdbcType", jdbcType);
                accountServerService.deployServerByScp(session, accountIds, accountServerService);
                response.getWriter().write("true");
                SystemLogService.accountServerLog(session, ":部署ID为" + accountIds + "的服务成功");
            }
        } catch (Exception e) {
            SystemLogService.accountServerLog(session, ":部署ID为" + accountIds + "的服务成功");
            e.printStackTrace();
        }
    }
}
