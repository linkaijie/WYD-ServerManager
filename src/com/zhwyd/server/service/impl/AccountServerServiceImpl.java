package com.zhwyd.server.service.impl;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import com.zhwyd.server.bean.AccountServer;
import com.zhwyd.server.bean.BeanInterface;
import com.zhwyd.server.bean.WorldServer;
import com.zhwyd.server.bean.vo.BatchOpearVo;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.common.util.CommonUtil;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.controller.model.AccountServerModel;
import com.zhwyd.server.dao.AccountServerDao;
import com.zhwyd.server.service.AccountServerService;
import com.zhwyd.server.service.ServiceInterface;
import com.zhwyd.server.ssh.util.SshExecutorUtils;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
import com.zhwyd.server.thread.BatchCloseAccountThread;
import com.zhwyd.server.thread.BatchStartAccountThread;
import com.zhwyd.server.web.page.Pager;
public class AccountServerServiceImpl extends ServiceSupportImpl<AccountServer> implements AccountServerService, ServiceInterface {
    protected AccountServerDao accountServerDao;

    public void setAccountServerDao(AccountServerDao accountServerDao) {
        super.setDaoSupport(accountServerDao);
        this.accountServerDao = accountServerDao;
    }

    public Pager getAccountServerList(AccountServerModel accountServerModel) {
        return accountServerDao.getAccountServerList(accountServerModel);
    }

    /**
     * 部署ACCOUNT服务
     * 
     * @author:LKJ 2014-9-25
     * @throws Exception
     */
    public void deployServer(HttpServletResponse response, BeanInterface beanInterface, HttpSession session) {
        try {
            SshExecutorUtils.deployEachServer(response, beanInterface, this, Global.CONFIG_ACCOUNT_PROPERTIES, session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新ACCOUNT服务
     * 
     * @author:LKJ 2014-9-25
     * @throws Exception
     */
    public void updateServer(HttpServletResponse response, BeanInterface beanInterface, HttpSession session) throws Exception {
        String updateResult = "";
        AccountServer accountServer = (AccountServer) beanInterface;
        updateResult = SshExecutorUtils.updateEachServer(accountServer, session);
        if (updateResult.equals("success")) {
            System.out.println("更新成功~~！");
            accountServer.setUpdateTime(new Date());
            this.merge(accountServer);
            response.getWriter().write("success");
        } else {
            response.getWriter().write(updateResult);
        }
    }

    /**
     * 获取通配信息
     * 
     * @author:LKJ 2014-9-28
     * @param accountServerModel
     * @return
     */
    @Override
    public String getWildcard(int accountId) {
        StringBuffer sb = new StringBuffer();
        AccountServer accountServer = CacheService.getAccountServerById(accountId);
        if (accountServer != null && accountServer.getId() > 0) {
            sb.append(accountServer.getServerip()).append(",").append(accountServer.getPort());
        }
        return sb.toString();
    }

    @Override
    public String startServer(BeanInterface beanInterface) throws Exception {
        return SshxcuteUtils.startEachServer(beanInterface, Global.ACCOUNT_PID_FILE);
    }

    @Override
    public String killServer(BeanInterface beanInterface) throws Exception {
        return SshxcuteUtils.stopEachServer(beanInterface, this, Global.ACCOUNT_PID_FILE);
    }

    /**
     * 查看运行日志
     * 
     * @author:LKJ 2014-9-6
     * @param serverId
     * @throws Exception
     */
    public Map<String, Object> showStdout(BeanInterface beanInterface, HttpSession session) throws Exception {
        String successStr = Global.ACCOUNT_SUCCESS_RESULT;
        SshxcuteUtils.showStdout(beanInterface, this, session, successStr);
        return null;
    }

    /**
     * 批量关闭ACCOUNT
     * 
     * @author:LKJ 2014-12-17
     * @throws Exception
     */
    public void batchCloseAccount(String accountIds, HttpSession session) throws Exception {
        if (!StringUtils.isEmpty(accountIds)) {
            String[] accountIdArray = accountIds.split(",");
            AccountServerService accountServerService = Application.getBean(AccountServerService.class);
            for (int i = 0; i < accountIdArray.length; i++) {
                System.out.println("accountIdArray[i]=" + accountIdArray[i]);
                Application.getManager().getSimpleThreadPool().execute(this.createCloseTask(Integer.valueOf(accountIdArray[i]), session, accountServerService));
            }
        }
    }

    /**
     * 创建新的关闭任务
     * 
     * @return 关闭任务
     */
    private Runnable createCloseTask(int accountId, HttpSession session, AccountServerService accountServerService) {
        return new BatchCloseAccountThread(accountId, accountServerService, session);
    }

    /**
     * 批量开启ACCOUNT
     * 
     * @author:LKJ 2014-12-17
     * @throws Exception
     */
    public void batchStartAccount(String accountIds, HttpSession session) throws Exception {
        if (!StringUtils.isEmpty(accountIds)) {
            String[] accountIdArray = accountIds.split(",");
            AccountServerService accountServerService = Application.getBean(AccountServerService.class);
            for (int i = 0; i < accountIdArray.length; i++) {
                System.out.println("accountIdArray[i]=" + accountIdArray[i]);
                Application.getManager().getSimpleThreadPool().execute(this.createStartTask(Integer.valueOf(accountIdArray[i]), session, accountServerService));
            }
        }
    }

    /**
     * 创建新的开启任务
     * 
     * @return 开启任务
     */
    private Runnable createStartTask(int accountId, HttpSession session, AccountServerService accountServerService) {
        return new BatchStartAccountThread(accountId, accountServerService, session);
    }

    /**
     * 修改状态
     * 
     * @param id
     *            服务ID
     * @param state
     *            服务状态
     * @param type
     *            修改类型 0：运行状态 1：部署状态
     */
    @Override
    public void updateState(int id, int state, int type) {
        AccountServer accountServer = this.get(id);
        if (type == CommanConstant.RUN_STATE) {
            CacheService.getAccountServerById(id).setState(state);
            accountServer.setState(state);
        } else if (type == CommanConstant.DEPLOY_STATE) {
            CacheService.getAccountServerById(id).setIsDeploy(state);
            accountServer.setIsDeploy(state);
        }
        this.update(accountServer);
    }

    /**
     * 写入日志
     */
    @Override
    public void writeBatchLog(HttpSession session, String logContent) {
        SystemLogService.batchAccountLog(session, logContent);
    }

    /**
     * 检测是否有关联
     */
    public boolean checkIsRelate(int accountId) {
        boolean checkResult = false;
        for (WorldServer worldServer : CacheService.getWorldServerAllList()) {
            if (worldServer.getAccountId() == accountId) {
                checkResult = true;
            }
        }
        return checkResult;
    }

    /**
     * 修改session数据
     */
    public void updateSession(String accountIds, HttpSession session) throws Exception {
        while (true) {
            int i = 0;
            if (session.getAttribute("batchAccountNum") != null) {
                Thread.sleep(2000);
                int batchAccountNum = (Integer) session.getAttribute("batchAccountNum");
                if (batchAccountNum == accountIds.split(",").length) {
                    break;
                }
            }
            if (i > 180) {
                break;
            }
            i++;
        }
        if (session.getAttribute("batchAccount") != null) {
            StringBuffer batchAccount = (StringBuffer) session.getAttribute("batchAccount");
            session.setAttribute("batchAccount", batchAccount.append("<font color='green'>批量操作成功</font></br>"));
        }
    }

    /**
     * 同步配置信息
     */
    @Override
    public void synchronizeConfig(String accountIds) {
        String[] ids = accountIds.split(",");
        for (int i = 0; i < ids.length; i++) {
            Map<String, String> configMap = SshxcuteUtils.getConfigMap(CacheService.getAccountServerById(Integer.valueOf(ids[i])), Global.CONFIG_ACCOUNT_PROPERTIES);
            this.updateAccountByMap(Integer.valueOf(ids[i]), configMap);
        }
    }

    /**
     * 工具配置信息修改DB及缓存
     */
    private void updateAccountByMap(int accountId, Map<String, String> configMap) {
        AccountServer accountServer = this.get(accountId);
        accountServer.setServerip(configMap.get("serverip"));
        accountServer.setPort(Integer.valueOf(configMap.get("port")));
        this.update(accountServer);
        configMap = null;
    }

    // /**
    // * 通过Scp更新ACCOUNT服务
    // *
    // * @author:LKJ 2014-9-25
    // * @throws Exception
    // */
    // public void updateServerByScp(HttpServletResponse response, String ids) throws Exception {
    // String[] accountIds = ids.split(",");
    // AccountServer accountServer = null;
    // for (int i = 0; i < accountIds.length; i++) {
    // accountServer = this.get(Integer.valueOf(accountIds[i]));
    // boolean result = SshxcuteUtils.updateServersByScp(response, accountServer);
    // this.merge(accountServer);
    // if (!result) {
    // return;
    // }
    // }
    // response.getWriter().write("true");
    // }
    public String getConfig(JSONObject jsonObject) {
        return "";
    }

    @Override
    public BeanInterface getBean(Integer id) {
        return accountServerDao.get(id);
    }

    public boolean synCfgByDeploy(BeanInterface beanInterface, BatchOpearVo batchOpearVo, HttpSession session) throws Exception {
        boolean result = false;
        AccountServer accountServer = (AccountServer) beanInterface;
        batchOpearVo.setRemark("正在同步：" + Global.CONFIG_ACCOUNT_PROPERTIES + "配置");
        // 更新account配置文件
        String wildcards = this.getWildcard(accountServer.getId());
        Object[] updateResult = SshxcuteUtils.synWildcard(beanInterface, wildcards, Global.CONFIG_ACCOUNT_PROPERTIES);
        if (updateResult == null || updateResult.length <= 0 || (Integer) updateResult[0] != CommanConstant.RESULT_TRUE_STATE) {
            batchOpearVo.isFail("更新" + Global.CONFIG_ACCOUNT_PROPERTIES + "失败");// 失敗
            return result;
        }
        // 更新jdbc配置
        batchOpearVo.setRemark("正在同步：" + Global.CONFIG_ACCOUNT_JDBC + "配置");
        // 获取jdbc路径
        String jdbcPath = CommonUtil.getJdbcPath(session.getAttribute("accountJdbcType").toString());
        // 同步jdbc配置文件
        Object[] updateJdbcResult = SshxcuteUtils.synWildcard(beanInterface, jdbcPath, Global.CONFIG_ACCOUNT_JDBC);
        if (updateJdbcResult == null || updateJdbcResult.length <= 0 || (Integer) updateJdbcResult[0] != CommanConstant.RESULT_TRUE_STATE) {
            batchOpearVo.isFail("更新" + Global.CONFIG_ACCOUNT_JDBC + "失败");// 失敗
            return result;
        }else {
            result = true;
        }
        return result;
    }

    @Override
    public void initCache() {
        // TODO Auto-generated method stub
        CacheService.initAccountServer();
    }
}
