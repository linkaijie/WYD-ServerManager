package com.zhwyd.server.service.impl;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import com.zhwyd.server.bean.BeanInterface;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.IpdServer;
import com.zhwyd.server.bean.vo.BatchOpearVo;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.controller.model.IpdServerModel;
import com.zhwyd.server.dao.IpdServerDao;
import com.zhwyd.server.service.IpdServerService;
import com.zhwyd.server.service.ServiceInterface;
import com.zhwyd.server.ssh.util.SshExecutorUtils;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
import com.zhwyd.server.thread.BatchCloseIpdThread;
import com.zhwyd.server.thread.BatchStartIpdThread;
import com.zhwyd.server.web.page.Pager;
public class IpdServerServiceImpl extends ServiceSupportImpl<IpdServer> implements IpdServerService, ServiceInterface {
    protected IpdServerDao ipdServerDao;

    public void setIpdServerDao(IpdServerDao ipdServerDao) {
        super.setDaoSupport(ipdServerDao);
        this.ipdServerDao = ipdServerDao;
    }

    public Pager getIpdServerList(IpdServerModel ipdServerModel) {
        return ipdServerDao.getIpdServerList(ipdServerModel);
    }

    public String startServer(BeanInterface beanInterface) throws Exception {
        String string = "";
        string = SshxcuteUtils.startEachServer(beanInterface, Global.IPDMAIN_PID_FILE);
        return string;
    }

    public String killServer(BeanInterface beanInterface) throws Exception {
        String string = "";
        string = SshxcuteUtils.stopEachServer(beanInterface, this, Global.IPDMAIN_PID_FILE);
        return string;
    }

    /**
     * 查看运行日志 author:LKJ 2014-9-6
     * 
     * @param serverId
     * @throws Exception
     */
    public Map<String, Object> showStdout(BeanInterface beanInterface, HttpSession session) throws Exception {
        String successStr = Global.IPD_SUCCESS_RESULT;
        SshxcuteUtils.showStdout(beanInterface, this, session, successStr);
        return null;
    }

    /**
     * 部署IPD服务 author:LKJ 2014-9-25
     * 
     * @throws Exception
     */
    public void deployServer(HttpServletResponse response, BeanInterface beanInterface, HttpSession session) {
        try {
            SshExecutorUtils.deployEachServer(response, beanInterface, this, Global.CONFIG_IPD_PROPERTIES, session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 部署IPD服务 author:LKJ 2014-9-25
     * 
     * @throws Exception
     */
    public void updateServer(HttpServletResponse response, BeanInterface beanInterface, HttpSession session) throws Exception {
        String updateResult = "";
        IpdServer ipdServer = (IpdServer) beanInterface;
        updateResult = SshExecutorUtils.updateEachServer(ipdServer, session);
        if (updateResult.equals("success")) {
            System.out.println("更新成功~~！");
            ipdServer.setUpdateTime(new Date());
            this.merge(ipdServer);
            response.getWriter().write("success");
        } else {
            response.getWriter().write(updateResult);
        }
    }

    /**
     * 获取通配信息 author:LKJ 2014-9-28
     * 
     * @return
     */
    @Override
    public String getWildcard(int ipdId) {
        StringBuffer sb = new StringBuffer();
        IpdServer ipdServer = CacheService.getIpdServerById(ipdId);
        if (ipdServer != null && ipdServer.getId() > 0) {
            sb.append(ipdServer.getLocalip()).append(",").append(ipdServer.getPort()).append(",").append(ipdServer.getHttp());
        }
        return sb.toString();
    }

    /**
     * 批量关闭Ipd author:LKJ 2014-12-17
     * 
     * @throws Exception
     */
    public void batchCloseIpd(String ipdIds, HttpSession session) throws Exception {
        System.out.println("ipdIds=" + ipdIds);
        if (!StringUtils.isEmpty(ipdIds)) {
            IpdServerService dIpdServerService = Application.getBean(IpdServerService.class);
            String[] ipdIdArray = ipdIds.split(",");
            for (int i = 0; i < ipdIdArray.length; i++) {
                System.out.println("ipdIdArray[i]=" + ipdIdArray[i]);
                Application.getManager().getSimpleThreadPool().execute(this.createCloseTask(Integer.valueOf(ipdIdArray[i]), session, dIpdServerService));
            }
        }
    }

    /**
     * 创建新的关闭Ipd任务
     * 
     * @param ipdId
     * @return 赠送任务
     */
    private Runnable createCloseTask(int ipdId, HttpSession session, IpdServerService dIpdServerService) {
        return new BatchCloseIpdThread(ipdId, dIpdServerService, session);
    }

    /**
     * 批量关闭ACCOUNT author:LKJ 2014-12-17
     * 
     * @param accountIds
     * @throws Exception
     */
    public void batchStartIpd(String ipdIds, HttpSession session) throws Exception {
        if (!StringUtils.isEmpty(ipdIds)) {
            IpdServerService dIpdServerService = Application.getBean(IpdServerService.class);
            String[] ipdIdArray = ipdIds.split(",");
            for (int i = 0; i < ipdIdArray.length; i++) {
                System.out.println("ipdIdArray[i]=" + ipdIdArray[i]);
                Application.getManager().getSimpleThreadPool().execute(this.createStartTask(Integer.valueOf(ipdIdArray[i]), session, dIpdServerService));
            }
        }
    }

    /**
     * 创建新的赠送任务
     * 
     * @return 赠送任务
     */
    private Runnable createStartTask(int ipdIds, HttpSession session, IpdServerService dIpdServerService) {
        return new BatchStartIpdThread(ipdIds, dIpdServerService, session);
    }

    @Override
    public void updateState(int id, int state, int type) {
        IpdServer ipdServer = this.get(id);
        if (type == 0) {// 0：更新运行状态
            CacheService.getIpdServerById(id).setState(state);
            ipdServer.setState(state);
        } else if (type == 1) {// 1：更新部署状态
            CacheService.getIpdServerById(id).setIsDeploy(state);
            ipdServer.setIsDeploy(state);
        }
        this.update(ipdServer);
    }

    @Override
    public void writeBatchLog(HttpSession session, String logContent) {
        SystemLogService.batchIpdLog(session, logContent);
    }

    /**
     * 检测是否有关联
     */
    public boolean checkIsRelate(int ipdId) {
        boolean checkResult = false;
        for (DispatchServer dispatchServer : CacheService.getDispatchServerAllList()) {
            if (dispatchServer.getIpdId() == ipdId) {
                checkResult = true;
            }
        }
        return checkResult;
    }

    /**
     * 修改session数据
     */
    public void updateSession(String ipdIds, HttpSession session) throws Exception {
        while (true) {
            int i = 0;
            if (session.getAttribute("batchIpdNum") != null) {
                Thread.sleep(2000);
                int batchIpdNum = (Integer) session.getAttribute("batchIpdNum");
                if (batchIpdNum == ipdIds.split(",").length) {
                    break;
                }
            }
            if (i > 180) {
                break;
            }
            i++;
        }
        if (session.getAttribute("batchIpd") != null) {
            StringBuffer batchIpd = (StringBuffer) session.getAttribute("batchIpd");
            session.setAttribute("batchIpd", batchIpd.append("<font color='green'>批量操作成功</font></br>"));
        }
    }

    /**
     * 同步配置信息
     */
    @Override
    public void synchronizeConfig(String ipdIds) {
        String[] ids = ipdIds.split(",");
        for (int i = 0; i < ids.length; i++) {
            Map<String, String> configMap = SshxcuteUtils.getConfigMap(CacheService.getIpdServerById(Integer.valueOf(ids[i])), Global.CONFIG_IPD_PROPERTIES);
            this.updateIpdByMap(Integer.valueOf(ids[i]), configMap);
        }
    }

    /**
     * 根据配置信息修改DB及缓存
     */
    private void updateIpdByMap(int ipdIds, Map<String, String> configMap) {
        IpdServer ipdServer = this.get(ipdIds);
        ipdServer.setLocalip(configMap.get("localip"));
        ipdServer.setPort(Integer.valueOf(configMap.get("port")));
        ipdServer.setHttp(Integer.valueOf(configMap.get("http")));
        this.update(ipdServer);
        configMap = null;
    }

    @Override
    public BeanInterface getBean(Integer id) {
        return ipdServerDao.get(id);
    }
    
    public boolean synCfgByDeploy(BeanInterface beanInterface, BatchOpearVo batchOpearVo, HttpSession session) throws Exception {
        boolean result = false;
        IpdServer ipdServer = (IpdServer) beanInterface;
        String wildcards = this.getWildcard(ipdServer.getId());
        Object[] updateResult = SshxcuteUtils.synWildcard(beanInterface, wildcards, Global.CONFIG_IPD_PROPERTIES);
        if (updateResult == null || updateResult.length <= 0 || (Integer) updateResult[0] != CommanConstant.RESULT_TRUE_STATE) {
            batchOpearVo.isFail("更新" + Global.CONFIG_IPD_PROPERTIES + "失败");// 失敗
            return result;
        } else {
            result = true;
        }
        return result;
    }

    @Override
    public void initCache() {
        // TODO Auto-generated method stub
        CacheService.initIpdServer();
    }
}
