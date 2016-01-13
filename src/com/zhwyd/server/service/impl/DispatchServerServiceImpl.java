package com.zhwyd.server.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import com.zhwyd.server.bean.BeanInterface;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.IpdServer;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.bean.WorldServer;
import com.zhwyd.server.bean.vo.BatchOpearVo;
import com.zhwyd.server.bean.vo.ChangeServerVo;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.common.util.CommonUtil;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.controller.model.DispatchServerModel;
import com.zhwyd.server.dao.DispatchServerDao;
import com.zhwyd.server.service.DispatchServerService;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.service.ServiceInterface;
import com.zhwyd.server.ssh.util.SshExecutorUtils;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
import com.zhwyd.server.thread.BatchCloseDispatchThread;
import com.zhwyd.server.thread.BatchCloseDispatchThreadNew;
import com.zhwyd.server.thread.BatchStartDispatchThread;
import com.zhwyd.server.web.page.Pager;
import net.sf.json.JSONObject;
public class DispatchServerServiceImpl extends ServiceSupportImpl<DispatchServer> implements DispatchServerService, ServiceInterface {
    protected DispatchServerDao dispatchServerDao;

    public void setDispatchServerDao(DispatchServerDao dispatchServerDao) {
        super.setDaoSupport(dispatchServerDao);
        this.dispatchServerDao = dispatchServerDao;
    }

    public Pager getDispatchServerList(DispatchServerModel dispatchServerModel) {
        return dispatchServerDao.getDispatchServerList(dispatchServerModel);
    }

    public List<DispatchServer> getDispatchServerListByWorldId(int worldId) {
        return dispatchServerDao.getDispatchServerListByWorldId(worldId);
    }

    public void saveOrUpdateDispatch(DispatchServer dispatchServers) {
        try {
            DispatchServer dispatchServer = null;
            if (dispatchServers.getId() == null) {
                dispatchServer = new DispatchServer();
            } else {
                dispatchServer = this.get(dispatchServers.getId());
            }
            dispatchServer.setGameId(dispatchServers.getGameId());
            dispatchServer.setServerId(dispatchServers.getServerId());
            dispatchServer.setName(dispatchServers.getName());
            dispatchServer.setWorldId(dispatchServers.getWorldId());
            dispatchServer.setIpdId(dispatchServers.getIpdId());
            dispatchServer.setWorldip(dispatchServers.getWorldip());
            dispatchServer.setWorldport(dispatchServers.getWorldport());
            dispatchServer.setLocalip(dispatchServers.getLocalip());
            dispatchServer.setPort(dispatchServers.getPort());
            dispatchServer.setServertype(dispatchServers.getServertype());
            dispatchServer.setPublicserver(dispatchServers.getPublicserver());
            dispatchServer.setPublicport(dispatchServers.getPublicport());
            dispatchServer.setDispatchServerId(dispatchServers.getDispatchServerId());
            dispatchServer.setServerarea(dispatchServers.getServerarea());
            dispatchServer.setMachinecode(dispatchServers.getMachinecode());
            dispatchServer.setCompanycode(dispatchServers.getCompanycode());
            dispatchServer.setDispatcherserver(dispatchServers.getDispatcherserver());
            dispatchServer.setDispatcherport(dispatchServers.getDispatcherport());
            dispatchServer.setServerarea(dispatchServers.getServerarea());
            dispatchServer.setState(dispatchServers.getState());
            dispatchServer.setIsDeploy(dispatchServers.getIsDeploy());
            dispatchServer.setPath(dispatchServers.getPath());
            dispatchServer.setModelPath(dispatchServers.getModelPath());
            dispatchServer.setUpdatePath(dispatchServers.getUpdatePath());
            dispatchServer.setIsUse(dispatchServers.getIsUse());
            dispatchServer.setOrders(dispatchServers.getOrders());
            this.saveOrUpdate(dispatchServer);
            CacheService.initDispatchServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启Dispatch服务
     */
    public String startServer(BeanInterface beanInterface) throws Exception {
        String string = "";
        DispatchServer dispatchServer = (DispatchServer) beanInterface;
        int worldtState = CacheService.getWorldServerById(dispatchServer.getWorldId()).getState();
        int ipdmainState = CacheService.getIpdServerById(dispatchServer.getIpdId()).getState();
        if (dispatchServer.getIsUse() != CommanConstant.STATE_START) {
            string = "该Dispatch未启用，若要开启该Dispatch请先修改其启用状态！";
        }
        if (worldtState == CommanConstant.STATE_START) {
            if (ipdmainState == CommanConstant.STATE_START) {
                string = SshxcuteUtils.startEachServer(dispatchServer, Global.DISPATCH_PID_FILE);
            } else {
                string = "IPD服务未启动，请先启动IPD！";
            }
        } else {
            string = "WORLD服务未启动，请先启动WORLD！";
        }
        return string;
    }

    /**
     * 关闭Dispatch服务
     */
    public String killServer(BeanInterface beanInterface) throws Exception {
        String string = "";
        string = SshxcuteUtils.stopEachServer(beanInterface, this, Global.DISPATCH_PID_FILE);
        return string;
    }

    /**
     * 查看运行日志
     */
    public Map<String, Object> showStdout(BeanInterface beanInterface, HttpSession session) throws Exception {
        String successStr = Global.DISPATCH_SUCCESS_RESULT;
        SshxcuteUtils.showStdout(beanInterface, this, session, successStr);
        return null;
    }

    /**
     * 部署Dispatch服务 author:LKJ 2014-9-25
     * 
     * @param response
     * @param dispatchServerModel
     * @return
     * @throws Exception
     */
    public void deployServer(HttpServletResponse response, BeanInterface beanInterface, HttpSession session) {
        try {
            SshExecutorUtils.deployEachServer(response, beanInterface, this, Global.CONFIG_DISPATCH_PROPERTIES, session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新Dispatch服务 author:LKJ 2014-9-25
     * 
     * @throws Exception
     */
    public void updateServer(HttpServletResponse response, BeanInterface beanInterface, HttpSession session) throws Exception {
        String updateResult = "";
        DispatchServer dispatchServer = (DispatchServer) beanInterface;
        updateResult = SshExecutorUtils.updateEachServer(dispatchServer, session);
        if (updateResult.equals("success")) {
            System.out.println("更新成功~~！");
            dispatchServer.setUpdateTime(new Date());
            this.merge(dispatchServer);
            response.getWriter().write("success");
        } else {
            response.getWriter().write(updateResult);
        }
    }

    /**
     * 获取通配信息
     * 
     * @param dispatchId
     * @return
     */
    public String getWildcard(int dispatchId) {
        StringBuffer sb = new StringBuffer();
        DispatchServer dispatchServer = this.get(dispatchId);
        if (dispatchServer != null && dispatchServer.getId() > 0) {
            sb.append(dispatchServer.getWorldip()).append(",").append(dispatchServer.getWorldport()).append(",").append(dispatchServer.getLocalip()).append(",").append(dispatchServer.getPort()).append(",").append(dispatchServer.getPublicserver()).append(",").append(dispatchServer.getPublicport()).append(",").append(dispatchServer.getDispatchServerId()).append(",").append(dispatchServer.getMachinecode()).append(",").append(dispatchServer.getDispatcherserver()).append(",").append(dispatchServer.getDispatcherport()).append(",");
        }
        return sb.toString();
    }

    /**
     * 批量关闭Dispatch服务
     */
    public void batchCloseDispatch(String dispatchIds, HttpSession session) throws Exception {
        if (!StringUtils.isEmpty(dispatchIds)) {
            String[] dispatchIdArray = dispatchIds.split(",");
            DispatchServerService dispatchServerService = Application.getBean(DispatchServerService.class);
            for (int i = 0; i < dispatchIdArray.length; i++) {
                System.out.println("dispatchIdArray[i]=" + dispatchIdArray[i]);
                Application.getManager().getSimpleThreadPool().execute(this.createCloseTask(Integer.valueOf(dispatchIdArray[i]), session, dispatchServerService));
            }
        }
    }

    /**
     * 创建新的批量关闭任务
     */
    private Runnable createCloseTask(int dispatchIds, HttpSession session, DispatchServerService dispatchServerService) {
        return new BatchCloseDispatchThread(dispatchIds, dispatchServerService, session);
    }

    /**
     * 批量开启Dispatch服务
     */
    public void batchStartDispatch(String dispatchIds, HttpSession session) throws Exception {
        if (!StringUtils.isEmpty(dispatchIds)) {
            DispatchServerService dispatchServerService = Application.getBean(DispatchServerService.class);
            String[] dispatchIdArray = dispatchIds.split(",");
            for (int i = 0; i < dispatchIdArray.length; i++) {
                System.out.println("dispatchIdArray[i]=" + dispatchIdArray[i]);
                Application.getManager().getSimpleThreadPool().execute(this.createStartTask(Integer.valueOf(dispatchIdArray[i]), session, dispatchServerService));
            }
        }
    }

    /**
     * 批量开启Dispatch服务
     */
    public void batchStartDispatchByList(List<Integer> dispatchIdList, HttpSession session) throws Exception {
        if (dispatchIdList != null && dispatchIdList.size() > 0) {
            DispatchServerService dispatchServerService = Application.getBean(DispatchServerService.class);
            for (int i = 0; i < dispatchIdList.size(); i++) {
                System.out.println("dispatchIdArray[i]=" + dispatchIdList.get(i));
                Application.getManager().getSimpleThreadPool().execute(this.createStartTask(dispatchIdList.get(i), session, dispatchServerService));
            }
        }
    }

    /**
     * 创建新的开启任务
     */
    public Runnable createStartTask(int dispatchId, HttpSession session, DispatchServerService dispatchServerService) {
        return new BatchStartDispatchThread(dispatchId, dispatchServerService, session);
    }

    /**
     * 获取运行结果信息
     */
    public String getStdout(Server server, DispatchServer dispatchServer) throws Exception {
        boolean successResult = false;
        boolean exceptionResult = false;
        String result = "";
        Object[] stdResult = SshxcuteUtils.showStdout(server, dispatchServer.getPath());
        String successStr = Global.DISPATCH_SUCCESS_RESULT;
        String exceptionStr = "Exception";
        if (stdResult != null && stdResult.length > 0) {
            successResult = CommonUtil.Pattern(successStr, stdResult[1].toString());
            exceptionResult = CommonUtil.Pattern(exceptionStr, stdResult[1].toString());
        }
        System.out.println("successResult=" + successResult + ",exceptionResult=" + exceptionResult);
        if (exceptionResult) {
            result = "false";
        } else if (successResult) {
            result = "true";
            CacheService.getDispatchServerById(dispatchServer.getId()).setState(CommanConstant.STATE_START);
            dispatchServer.setState(CommanConstant.STATE_START);
            Application.getBean(DispatchServerService.class).update(dispatchServer);
        }
        return result;
    }

    /**
     * 根据dispatchIds获取未开启的ipd主键
     */
    public StringBuilder getIpdState(String dispatchIds) {
        StringBuilder ipdIds = new StringBuilder();
        if (!StringUtils.isEmpty(dispatchIds)) {
            String[] dispatchIdArray = dispatchIds.split(",");
            for (int i = 0; i < dispatchIdArray.length; i++) {
                IpdServer ipdServer = CacheService.getIpdServerById(CacheService.getDispatchServerById(Integer.valueOf(dispatchIdArray[i])).getIpdId());
                // 获取未开启的IPD服务
                if (ipdServer.getState() == CommanConstant.STATE_STOP) {
                    ipdIds.append(ipdServer.getId()).append(",");
                }
            }
        }
        return ipdIds;
    }

    /**
     * 根据dispatchIds获取未开启的world主键
     */
    public StringBuilder getWorldState(String dispatchIds) {
        StringBuilder sb = new StringBuilder();
        String[] dispatchIdArray = dispatchIds.split(",");
        for (int i = 0; i < dispatchIdArray.length; i++) {
            WorldServer worldServer = CacheService.getWorldServerById(CacheService.getDispatchServerById(Integer.valueOf(dispatchIdArray[i])).getWorldId());
            if (worldServer.getState() == 0) {
                sb.append(worldServer.getId()).append(",");
            }
        }
        return sb;
    }

    /**
     * 修改运行/部署状态（0：运行；1：部署）
     */
    @Override
    public void updateState(int id, int state, int type) {
        DispatchServer dispatchServer = this.get(id);
        if (type == 0) {
            CacheService.getDispatchServerById(id).setState(state);
            dispatchServer.setState(state);
        } else if (type == 1) {
            CacheService.getDispatchServerById(id).setIsDeploy(state);
            dispatchServer.setIsDeploy(state);
        }
        this.update(dispatchServer);
    }

    @Override
    public void writeBatchLog(HttpSession session, String logContent) {
        SystemLogService.batchDispatchLog(session, logContent);
    }

    /**
     * 同步配置信息
     */
    @Override
    public void synchronizeConfig(String dispatchIds) {
        String[] ids = dispatchIds.split(",");
        for (int i = 0; i < ids.length; i++) {
            Map<String, String> configMap = SshxcuteUtils.getConfigMap(CacheService.getDispatchServerById(Integer.valueOf(ids[i])), Global.CONFIG_DISPATCH_PROPERTIES);
            this.updateDispatchByMap(Integer.valueOf(ids[i]), configMap);
        }
    }

    /**
     * 工具配置信息修改DB及缓存
     */
    private void updateDispatchByMap(int dispatchId, Map<String, String> configMap) {
        DispatchServer dispatchServer = this.get(dispatchId);
        dispatchServer.setWorldip(configMap.get("worldip"));
        dispatchServer.setWorldport(Integer.valueOf(configMap.get("worldport")));
        dispatchServer.setLocalip(configMap.get("localip"));
        dispatchServer.setPort(Integer.valueOf(configMap.get("port")));
        dispatchServer.setServertype(configMap.get("servertype"));
        dispatchServer.setPublicserver(configMap.get("publicserver"));
        dispatchServer.setPublicport(Integer.valueOf(configMap.get("publicport")));
        dispatchServer.setDispatchServerId(Integer.valueOf(configMap.get("id")));
        dispatchServer.setServerarea(configMap.get("serverarea"));
        System.out.println("machinecode=" + Integer.valueOf(configMap.get("machinecode")));
        dispatchServer.setMachinecode(Integer.valueOf(configMap.get("machinecode")));
        System.out.println("companycode=" + Integer.valueOf(configMap.get("companycode")));
        dispatchServer.setCompanycode(Integer.valueOf(configMap.get("companycode")));
        dispatchServer.setDispatcherserver(configMap.get("dispatcherserver"));
        dispatchServer.setDispatcherport(Integer.valueOf(configMap.get("dispatcherport")));
        this.update(dispatchServer);
        configMap = null;
    }

    public String getConfig(JSONObject jsonObject) {
        int machinecode = jsonObject.getInt("machinecode");
        int number = jsonObject.getInt("number");
        DispatchServer dispatchServer = dispatchServerDao.get(new String[] { "machinecode", "number"}, new Object[] { machinecode, number});
        JSONObject json = new JSONObject();
        json.element("worldport", dispatchServer.getWorldport());
        json.element("port", dispatchServer.getPort());
        json.element("publicserver", dispatchServer.getPublicserver());
        json.element("publicport", dispatchServer.getPublicport());
        json.element("id", dispatchServer.getDispatchServerId());
        json.element("machinecode", dispatchServer.getMachinecode());
        json.element("dispatcherserver", dispatchServer.getDispatcherserver());
        json.element("dispatcherport", dispatchServer.getDispatcherport());
        return json.toString();
    }

    /**
     * 根据类型获取最大排序
     * 
     * @param serverType
     * @return
     */
    @SuppressWarnings("rawtypes")
    public int getMaxOrderByType(int serverType, int orders) {
        int order = 0;
        int num = orders;
        String sql = "select dispatch.orders from dispatch_server as dispatch where {0} order by dispatch.orders desc";
        String condition = "";
        if (num > 0) {
            order = num;
            condition = " orders >= " + num + " and orders < " + (num + 10000);
        } else {
            order = 0;
            condition = " orders < " + 10000;
        }
        sql = sql.replace("{0}", condition);
        List list = dispatchServerDao.createSqlQuery(sql).list();
        if (list != null && list.size() > 0) {
            order = (int) list.get(0);
        }
        return order;
    }

    @Override
    public BeanInterface getBean(Integer id) {
        return dispatchServerDao.get(id);
    }

    /**
     * 批量关闭Dispatch服务
     */
    public void batchCloseDispatchNew(String dispatchIds, HttpSession session) throws Exception {
        if (!StringUtils.isEmpty(dispatchIds)) {
            String[] dispatchIdArray = dispatchIds.split(",");
            session.setAttribute("dispatchIdsNum", dispatchIdArray.length);
            DispatchServerService dispatchServerService = Application.getBean(DispatchServerService.class);
            for (int i = 0; i < dispatchIdArray.length; i++) {
                System.out.println("dispatchIdArray[i]=" + dispatchIdArray[i]);
                Application.getManager().getSimpleThreadPool().execute(this.createCloseTaskNew(Integer.valueOf(dispatchIdArray[i]), session, dispatchServerService));
            }
        }
    }

    /**
     * 创建新的批量关闭任务
     */
    private Runnable createCloseTaskNew(int dispatchIds, HttpSession session, DispatchServerService dispatchServerService) {
        return new BatchCloseDispatchThreadNew(dispatchIds, dispatchServerService, session);
    }

    /**
     * 批量生成dispatch
     */
    public void saveOrUpdateMore(DispatchServer dispatchServers, Integer creatrNumber, String serverTypes) {
        try {
            for (int i = 1; i <= creatrNumber; i++) {
                DispatchServer dispatchServerSave = new DispatchServer();
                String name = "";
                String path = "";
                int number = i;
                if (i == 1) {
                    name = dispatchServers.getName() + i;
                    path = dispatchServers.getPath() + i;
                } else if (i > 1 && i == creatrNumber) {
                    name = dispatchServers.getName() + "副";
                    path = dispatchServers.getPath() + "bak";
                    number = 0;
                } else {
                    name = dispatchServers.getName() + i;
                    path = dispatchServers.getPath() + i;
                }
                dispatchServerSave.setGameId(dispatchServers.getGameId());
                dispatchServerSave.setServerId(dispatchServers.getServerId());
                dispatchServerSave.setName(name);
                dispatchServerSave.setWorldId(dispatchServers.getWorldId());
                dispatchServerSave.setIpdId(dispatchServers.getIpdId());
                dispatchServerSave.setState(dispatchServers.getState());
                dispatchServerSave.setIsDeploy(dispatchServers.getIsDeploy());
                dispatchServerSave.setPath(path);
                dispatchServerSave.setModelPath(dispatchServers.getModelPath());
                dispatchServerSave.setUpdatePath(dispatchServers.getUpdatePath());
                dispatchServerSave.setIsUse(dispatchServers.getIsUse());
                dispatchServerSave.setOrders(dispatchServers.getOrders());
                dispatchServerSave.setNumber(number);
                int orders = this.getMaxOrderByType(Integer.valueOf(serverTypes), dispatchServers.getOrders());
                dispatchServerSave.setOrders(orders + 1);
                this.saveOrUpdate(dispatchServerSave);
            }
            CacheService.initDispatchServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量生成dispatch
     */
    public String saveOrUpdateMoreDispatchOld(DispatchServer dispatchServerMore, Integer creatrNumber) {
        String result = "";
        try {
            DispatchServer dispatchServer = null;
            String cnType = Application.getConfig("system", "cnType");
            int serverId = dispatchServerMore.getServerId();
            Server server = CacheService.getServerById(serverId);
            int machinecode = dispatchServerMore.getMachinecode();// 填
            int num = machinecode / 10000 * 10000;// 整万，用于被machinecode减，得到分区号
            int areaNum = machinecode - num;// 分区号
            String publicserver = server.getServerName().trim();// 填
            int port = 0;
            int dispatchId = 0;
            String name = "{0}服分发服{1}";
            String path = "/data/apps/gunsoul/dispatchServer_{0}_{1}_{2}";
            List<DispatchServer> DispatchServerListCheck = dispatchServerDao.getDispatchServerByServerTypeAndAreaId(dispatchServerMore.getServerType(), machinecode);
            if (DispatchServerListCheck != null && DispatchServerListCheck.size() > 0) {
                result = "存在相同類型和分區號的記錄";
                return result;
            }
            List<DispatchServer> dispatchServersList = dispatchServerDao.getDispatchServerListByTopPort(dispatchServerMore.getServerId());
            if (dispatchServersList != null && dispatchServersList.size() > 0) {
                dispatchServer = dispatchServersList.get(0);
                port = dispatchServer.getPort() + 10;
                dispatchId = dispatchServer.getDispatchServerId() + 100;
            } else {
                dispatchServer = this.getDefaultDispatch();
                port = dispatchServer.getPort();
                dispatchId = Integer.valueOf(dispatchServerMore.getDispatchServerId() + "0001");// 页面输入，只输入机器号,后面自动拼接
            }
            String nameTemp = "";
            int portTemp = 0;
            int dispatchServerIdTemp = 0;
            int number;
            String dispatchserverTemp = "";
            String pathTemp = "";
            for (int i = 1; i <= creatrNumber; i++) {
                DispatchServer dispatchServerSave = new DispatchServer();
                BeanUtils.copyProperties(dispatchServerSave, this.getDefaultDispatch());
                if (i > 1 && i == creatrNumber) {
                    if (dispatchServerMore.getServerType() == 3 && cnType.equals("CN")) {
                        nameTemp = name.replace("{0}", "硬核" + String.valueOf(areaNum)).replace("{1}", "副");
                        pathTemp = path.replace("{0}", dispatchServerMore.getServerType().toString()).replace("{1}", String.valueOf(areaNum)).replace("{2}", "bak");
                    } else {
                        nameTemp = name.replace("{0}", String.valueOf(machinecode)).replace("{1}", "副");
                        pathTemp = path.replace("{0}", dispatchServerMore.getServerType().toString()).replace("{1}", String.valueOf(machinecode)).replace("{2}", "bak");
                    }
                    portTemp = port - 1;
                    dispatchServerIdTemp = dispatchId - 1;
                    number = 0;
                    if (cnType.equals("CN")) {
                        if (dispatchServerMore.getServerType() == 1) {// 国内IOS副IPD
                            dispatchserverTemp = "10.46.64.112";
                        } else {// 天象副IPD
                            dispatchserverTemp = "10.10.40.139";
                        }
                    } else if (cnType.equals("TW")) {// 台湾副IPD
                        dispatchserverTemp = "10.0.0.185";
                    } else if (cnType.equals("VN")) {
                        dispatchserverTemp = "125.212.244.5";// 越南测试
                    }
                } else {
                    if (dispatchServerMore.getServerType() == 3 && cnType.equals("CN")) {
                        nameTemp = name.replace("{0}", "硬核" + String.valueOf(areaNum)).replace("{1}", String.valueOf(i));
                        pathTemp = path.replace("{0}", dispatchServerMore.getServerType().toString()).replace("{1}", String.valueOf(areaNum)).replace("{2}", String.valueOf(i));
                    } else {
                        nameTemp = name.replace("{0}", String.valueOf(machinecode)).replace("{1}", String.valueOf(i));
                        pathTemp = path.replace("{0}", dispatchServerMore.getServerType().toString()).replace("{1}", String.valueOf(machinecode)).replace("{2}", String.valueOf(i));
                    }
                    portTemp = port + i - 1;
                    dispatchServerIdTemp = dispatchId + i - 1;
                    number = i;
                    if (cnType.equals("CN")) {
                        if (dispatchServerMore.getServerType() == 1) {// 国服IOS IPD
                            dispatchserverTemp = "10.46.67.222";
                        } else {// 天象IPD
                            dispatchserverTemp = "10.10.2.142";
                        }
                    } else if (cnType.equals("TW")) {// 台湾IPD
                        dispatchserverTemp = "10.0.0.181";
                    } else if (cnType.equals("VN")) {
                        dispatchserverTemp = "125.212.244.5";// 越南测试
                    }
                }
                DispatchServer dispatchServerTemp = dispatchServerDao.get(new String[] { "port", "serverId"}, new Object[] { portTemp, serverId});
                if (dispatchServerTemp != null) {
                    result = "存在相同的port，生成失败";
                    return result;
                }
                dispatchServerSave.setName(nameTemp);
                dispatchServerSave.setServerId(serverId);
                dispatchServerSave.setWorldId(dispatchServerMore.getWorldId());
                dispatchServerSave.setWorldport(CacheService.getWorldServerById(dispatchServerMore.getWorldId()).getPort());
                dispatchServerSave.setPort(portTemp);
                dispatchServerSave.setPublicport(portTemp);
                dispatchServerSave.setPublicserver(publicserver);
                dispatchServerSave.setMachinecode(machinecode);
                dispatchServerSave.setDispatchServerId(dispatchServerIdTemp);
                dispatchServerSave.setDispatcherserver(dispatchserverTemp);
                dispatchServerSave.setNumber(number);
                dispatchServerSave.setPath(pathTemp);
                if (dispatchServerMore.getIsDeploy() != null) {
                    dispatchServerSave.setIsDeploy(dispatchServerMore.getIsDeploy());
                }
                dispatchServerSave.setServerType(dispatchServerMore.getServerType());
                int orders = this.getMaxOrderByType(dispatchServerMore.getServerType(), dispatchServerMore.getOrders());
                dispatchServerSave.setOrders(orders + 1);
                this.saveOrUpdate(dispatchServerSave);
            }
            CacheService.initDispatchServer();
            result = "true";
        } catch (Exception e) {
            e.printStackTrace();
            result = "创建dispatch遇到未知错误";
        }
        return result;
    }

    /**
     * 批量生成dispatch
     */
    public String saveOrUpdateMoreDispatch(DispatchServer dispatchServerMore, Integer creatrNumber) {
        String result = "";
        try {
            DispatchServer dispatchServer = null;
            String cnType = Application.getConfig("system", "cnType");// 分区前缀类型
            String ipdIpMain = Application.getConfig("system", "ipdIpMain");// 主ipdIp
            String ipdIpBak = Application.getConfig("system", "ipdIpBak");// 副ipdIp
            int serverId = dispatchServerMore.getServerId();// 服务器Id（哪台机子）
            Server server = CacheService.getServerById(serverId);
            int machinecode = dispatchServerMore.getMachinecode();// 分区号
            int num = machinecode / 10000 * 10000;// 整万，用于被machinecode减，得到分区号
            int areaNum = machinecode - num;// 分区号
            String publicserver = server.getServerName().trim();// 公网Ip地址
            int port = 0;
            int dispatchId = 0;
            String name = "{0}服分发服{1}";
            String path = "/data/apps/gunsoul/dispatchServer_{0}_{1}_{2}";
            List<DispatchServer> DispatchServerListCheck = dispatchServerDao.getDispatchServerByServerTypeAndAreaId(dispatchServerMore.getServerType(), machinecode);
            if (DispatchServerListCheck != null && DispatchServerListCheck.size() > 0) {
                result = "存在相同類型和分區號的記錄";
                return result;
            }
            List<DispatchServer> dispatchServersList = dispatchServerDao.getDispatchServerListByTopPort(dispatchServerMore.getServerId());
            if (dispatchServersList != null && dispatchServersList.size() > 0) {
                dispatchServer = dispatchServersList.get(0);
                port = dispatchServer.getPort() + 10;
                dispatchId = dispatchServer.getDispatchServerId() + 100;
            } else {
                dispatchServer = this.getDefaultDispatch();
                port = dispatchServer.getPort();
                dispatchId = Integer.valueOf(dispatchServerMore.getDispatchServerId() + "0001");// 页面输入，只输入机器号,后面自动拼接
            }
            String nameTemp = "";// 名称
            int portTemp = 0;// 端口
            int dispatchServerIdTemp = 0;// dispatchServerId
            int number;
            String dispatchserverTemp = "";// ipd IP
            String pathTemp = "";
            for (int i = 1; i <= creatrNumber; i++) {
                DispatchServer dispatchServerSave = new DispatchServer();
                BeanUtils.copyProperties(dispatchServerSave, this.getDefaultDispatch());
                if (i > 1 && i == creatrNumber) {// 副IPD
                    if (dispatchServerMore.getServerType() == 3 && cnType.equals("CN")) {
                        nameTemp = name.replace("{0}", "硬核" + String.valueOf(areaNum)).replace("{1}", "副");
                        pathTemp = path.replace("{0}", dispatchServerMore.getServerType().toString()).replace("{1}", String.valueOf(areaNum)).replace("{2}", "bak");
                    } else {
                        nameTemp = name.replace("{0}", String.valueOf(machinecode)).replace("{1}", "副");
                        pathTemp = path.replace("{0}", dispatchServerMore.getServerType().toString()).replace("{1}", String.valueOf(machinecode)).replace("{2}", "bak");
                    }
                    portTemp = port - 1;
                    dispatchServerIdTemp = dispatchId - 1;
                    number = 0;
                    dispatchserverTemp = ipdIpBak;// 设置副IPD的ip地址
                } else {
                    if (dispatchServerMore.getServerType() == 3 && cnType.equals("CN")) {
                        nameTemp = name.replace("{0}", "硬核" + String.valueOf(areaNum)).replace("{1}", String.valueOf(i));
                        pathTemp = path.replace("{0}", dispatchServerMore.getServerType().toString()).replace("{1}", String.valueOf(areaNum)).replace("{2}", String.valueOf(i));
                    } else {
                        nameTemp = name.replace("{0}", String.valueOf(machinecode)).replace("{1}", String.valueOf(i));
                        pathTemp = path.replace("{0}", dispatchServerMore.getServerType().toString()).replace("{1}", String.valueOf(machinecode)).replace("{2}", String.valueOf(i));
                    }
                    portTemp = port + i - 1;
                    dispatchServerIdTemp = dispatchId + i - 1;
                    number = i;
                    dispatchserverTemp = ipdIpMain;// 设置主IPD的ip地址
                }
                DispatchServer dispatchServerTemp = dispatchServerDao.get(new String[] { "port", "serverId"}, new Object[] { portTemp, serverId});
                if (dispatchServerTemp != null) {
                    result = "存在相同的port，生成失败";
                    return result;
                }
                dispatchServerSave.setName(nameTemp);
                dispatchServerSave.setServerId(serverId);
                dispatchServerSave.setWorldId(dispatchServerMore.getWorldId());
                dispatchServerSave.setWorldport(CacheService.getWorldServerById(dispatchServerMore.getWorldId()).getPort());
                dispatchServerSave.setPort(portTemp);
                dispatchServerSave.setPublicport(portTemp);
                dispatchServerSave.setPublicserver(publicserver);
                dispatchServerSave.setMachinecode(machinecode);
                dispatchServerSave.setDispatchServerId(dispatchServerIdTemp);
                dispatchServerSave.setDispatcherserver(dispatchserverTemp);
                dispatchServerSave.setNumber(number);
                dispatchServerSave.setPath(pathTemp);
                if (dispatchServerMore.getIsDeploy() != null) {
                    dispatchServerSave.setIsDeploy(dispatchServerMore.getIsDeploy());
                }
                dispatchServerSave.setServerType(dispatchServerMore.getServerType());
                int orders = this.getMaxOrderByType(dispatchServerMore.getServerType(), dispatchServerMore.getOrders());
                dispatchServerSave.setOrders(orders + 1);
                this.saveOrUpdate(dispatchServerSave);
            }
            CacheService.initDispatchServer();
            result = "true";
        } catch (Exception e) {
            e.printStackTrace();
            result = "创建dispatch遇到未知错误";
        }
        return result;
    }

    public DispatchServer getDefaultDispatch() {
        String cnType = Application.getConfig("system", "cnType");
        DispatchServer dispatchServer = new DispatchServer();
        dispatchServer.setGameId(1);
        dispatchServer.setIpdId(CacheService.getIpdServerMain().getId());
        dispatchServer.setWorldip("127.0.0.1");
        dispatchServer.setWorldport(6860);
        dispatchServer.setLocalip("0.0.0.0");
        dispatchServer.setPort(19901);
        dispatchServer.setServertype("socket");
        dispatchServer.setPublicport(19901);
        dispatchServer.setServerarea(cnType);
        dispatchServer.setCompanycode(0);// 一般不会变
        dispatchServer.setDispatcherport(6886);// 一般不会变
        dispatchServer.setIsUse(1);
        dispatchServer.setState(0);
        dispatchServer.setIsDeploy(1);
        return dispatchServer;
    }

    /**
     * 批量生成dispatch
     */
    public String updateCfgToOtherServer(ChangeServerVo changeServerVo) {
        String result = "success";
        try {
            List<DispatchServer> dispatchServerList = CacheService.getDispatchServerListByWorldId(changeServerVo.getId());
            WorldServer worldServer = CacheService.getWorldServerById(changeServerVo.getId());
            DispatchServer dispatchServerTemp = null;
            int serverId = worldServer.getServerId();// 目标机子serverId
            String publicserver = worldServer.getPublicip();// 目标机子外部IP
            // 根據serverId獲取改機子上的dispatch列表（排好序）
            List<DispatchServer> dispatchServersList = dispatchServerDao.getDispatchServerListByTopPort(changeServerVo.getServerId());
            int portTemp = 0;
            int dispatchServerIdTemp = 0;
            // 获取端口和dispatchServerId
            if (dispatchServersList != null && dispatchServersList.size() > 0) {
                dispatchServerTemp = dispatchServersList.get(0);
                portTemp = dispatchServerTemp.getPort() + 10;
                dispatchServerIdTemp = dispatchServerTemp.getDispatchServerId() + 100;
            } else {
                dispatchServerTemp = this.getDefaultDispatch();
                portTemp = dispatchServerTemp.getPort();
                dispatchServerIdTemp = Integer.valueOf(changeServerVo.getDispatchServerId() + "0001");// 页面输入，只输入机器号,后面自动拼接
            }
            int i = 1;
            int port = 0;
            int dispatchServerId = 0;
            for (DispatchServer dispatchServer : dispatchServerList) {
                changeServerVo.setRemark("修改" + dispatchServer.getName() + "数据库配置中");
                if (i > 1 && i == dispatchServerList.size() && dispatchServer.getPath().contains("bak")) {// 副dispatch
                    port = portTemp - 1;
                    dispatchServerId = dispatchServerIdTemp - 1;
                } else {
                    port = portTemp + i - 1;
                    dispatchServerId = dispatchServerIdTemp + i - 1;
                }
                DispatchServer dispatchServerCheck = dispatchServerDao.get(new String[] { "port", "serverId"}, new Object[] { port, serverId});
                if (dispatchServerCheck != null) {
                    changeServerVo.setRemark("存在相同的port，生成失败");
                    result = "存在相同的port，生成失败";
                    return result;
                }
                dispatchServer.setServerId(serverId);
                dispatchServer.setPort(port);
                dispatchServer.setPublicport(port);
                dispatchServer.setPublicserver(publicserver);
                dispatchServer.setWorldport(worldServer.getPort());
                dispatchServer.setDispatchServerId(dispatchServerId);
                this.saveOrUpdate(dispatchServer);
                i++;
            }
            CacheService.initDispatchServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean synCfgByDeploy(BeanInterface beanInterface, BatchOpearVo batchOpearVo, HttpSession session) throws Exception {
        boolean result = false;
        DispatchServer dispatchServer = (DispatchServer) beanInterface;
        String wildcards = this.getWildcard(dispatchServer.getId());
        batchOpearVo.setRemark("正在同步" + Global.CONFIG_DISPATCH_PROPERTIES + "配置");
        Object[] updateResult = SshxcuteUtils.synWildcard(beanInterface, wildcards, Global.CONFIG_DISPATCH_PROPERTIES);
        if (updateResult == null || updateResult.length <= 0 || (Integer) updateResult[0] != CommanConstant.RESULT_TRUE_STATE) {
            batchOpearVo.isFail("更新" + Global.CONFIG_DISPATCH_PROPERTIES + "失败");// 失敗
            return result;
        } else {
            result = true;
        }
        return result;
    }

    /**
     * 关闭dispatch(单个循环)
     * 
     * @param batchOpearVo 操作vo
     * @param worldId worldID
     * @param dispatchServerIds 已開啟的dispatch服務Id
     */
    public boolean closeDispatch(BatchOpearVo batchOpearVo, int worldId, List<Integer> dispatchServerIds) throws Exception {
        boolean closeResult = true;
        List<DispatchServer> dispatchServerList = CacheService.getDispatchServerListByWorldId(worldId);
        for (DispatchServer dispatchServer : dispatchServerList) {
            batchOpearVo.setRemark(dispatchServer.getName() + "正在关闭");
            Server server = CacheService.getServerById(dispatchServer.getServerId());
            if (dispatchServer.getIsDeploy() == CommanConstant.STATE_STOP || dispatchServer.getIsUse() == CommanConstant.STATE_STOP) {// 未部署或未启用
                continue;
            }
            if (dispatchServer.getState() == CommanConstant.STATE_START) {
                // 关闭Dispatch服务
                Object[] killResult = SshxcuteUtils.killExec(server, dispatchServer.getPath());
                if (killResult != null && killResult.length > 0 && (Integer) killResult[0] != CommanConstant.RESULT_TRUE_STATE) {
                    batchOpearVo.isFail(dispatchServer.getName() + "关闭出错");
                    return false;
                }
                boolean result = SshxcuteUtils.progressIsExistNew(server, dispatchServer.getPath(), Global.DISPATCH_PID_FILE);
                if (!result) {
                    dispatchServer.setState(CommanConstant.STATE_STOP);
                    Application.getBean(DispatchServerService.class).update(dispatchServer);// 更新数据库
                    CacheService.getDispatchServerById(dispatchServer.getId()).setState(CommanConstant.STATE_STOP);// 更新缓存
                    batchOpearVo.isSUCCESS(dispatchServer.getName() + "关闭成功");
                    dispatchServerIds.add(dispatchServer.getDispatchServerId());
                    closeResult = true;
                } else {
                    batchOpearVo.isFail(dispatchServer.getName() + "关闭出错");
                    return false;
                }
            }
        }
        return closeResult;
    }

    // ==================================关闭dispatch线程类start====================================
    /**
     * 关闭dispatch
     * 
     * @param batchOpearVo 操作vo
     * @param worldId worldID
     * @param dispatchServerIds 已開啟的dispatch服務Id
     */
    public boolean closeDispatchInsideThread(HttpSession session, BatchOpearVo batchOpearVo, int worldId, List<Integer> dispatchServerIds, ServerService serverService, DispatchServerService dispatchServerService) throws Exception {
        boolean closeResult = false;
        List<DispatchServer> dispatchServerList = dispatchServerService.getDispatchServerListByWorldId(worldId);
        // 设置需要处理的dispatch数量
        batchOpearVo.setNum(dispatchServerList.size());
        // 循环处理所有dispatch
        for (DispatchServer dispatchServer : dispatchServerList) {
            CloseDispatchThread closeDispatchThread = new CloseDispatchThread(dispatchServer, dispatchServerIds, batchOpearVo, serverService);
            new Thread(closeDispatchThread).start();
        }
        // 等待直到所有dispatch关闭完成后会唤醒此线程
        synchronized (batchOpearVo) {
            batchOpearVo.wait();
        }
        // 如果dispatch处理不成功
        if (batchOpearVo.getStatus() != null && !batchOpearVo.getStatus()) {
            session.setAttribute("updateRestartError", batchOpearVo.getRemark());
        } else {
            closeResult = true;
        }
        return closeResult;
    }
    // ==================================关闭dispatch线程类start====================================
    /**
     * 关闭dispatch內部線程類
     */
    public class CloseDispatchThread implements Runnable {
        private DispatchServer dispatchServer    = null;
        private List<Integer>  dispatchServerIds = null;
        private BatchOpearVo   batchOpearVo      = null;
        private ServerService  serverService     = null;

        public CloseDispatchThread(DispatchServer dispatchServer, List<Integer> dispatchServerIds, BatchOpearVo batchOpearVo, ServerService serverService) {
            this.dispatchServer = dispatchServer;
            this.batchOpearVo = batchOpearVo;
            this.dispatchServerIds = dispatchServerIds;
            this.serverService = serverService;
        }

        @Override
        public void run() {
            try {
                if (batchOpearVo.getStatus() == null || batchOpearVo.getStatus()) {
                    batchOpearVo.setRemark(dispatchServer.getName() + "正在关闭");
                }
                Server server = serverService.get(dispatchServer.getServerId());
                // 未部署或未启用
                if (dispatchServer.getIsDeploy() == CommanConstant.STATE_STOP || dispatchServer.getIsUse() == CommanConstant.STATE_STOP) {
                    return;
                }
                if (dispatchServer.getState() == CommanConstant.STATE_START) {
                    // 关闭Dispatch服务
                    Object[] killResult = SshxcuteUtils.killExec(server, dispatchServer.getPath());
                    if (killResult != null && killResult.length > 0 && (Integer) killResult[0] != CommanConstant.RESULT_TRUE_STATE) {
                        batchOpearVo.isFail(dispatchServer.getName() + "关闭出错");
                        return;
                    }
                    dispatchServer.setState(CommanConstant.STATE_STOP);
                    // 更新数据库
                    Application.getBean(DispatchServerService.class).update(dispatchServer);
                    // 更新缓存
                    CacheService.getDispatchServerById(dispatchServer.getId()).setState(CommanConstant.STATE_STOP);
                    // System.out.println("=================dispatchServerIds.add==============" + dispatchServer.getDispatchServerId());
                    dispatchServerIds.add(dispatchServer.getDispatchServerId());
                    // System.out.println("=================dispatchServerIds.add==============" + dispatchServer.getDispatchServerId());
                    // System.out.println("=================dispatchServerIds.contains==============" + dispatchServerIds.contains(dispatchServer.getDispatchServerId()));
                    if (batchOpearVo.getStatus() == null || batchOpearVo.getStatus() != false) {
                        batchOpearVo.isSUCCESS(dispatchServer.getName() + "关闭成功");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                batchOpearVo.isFail(dispatchServer.getName() + "关闭出错");
            } finally {
                // 更新处理数量
                batchOpearVo.updateNum();
            }
        }
    }
    // ==================================关闭dispatch线程类end====================================

    /**
     * 開啟dispatch(单个循环开启)
     * 
     * @param batchOpearVo 開啟dispatch
     * @param dispatchServerId dispatchServerId
     * @param session HttpSession
     */
    public boolean startDispatch(BatchOpearVo batchOpearVo, Integer worldId, HttpSession session) throws Exception {
        boolean startResult = true;
        for (DispatchServer dispatchServer : CacheService.getDispatchServerListByWorldId(worldId)) {
            if (dispatchServer.getIsDeploy() == CommanConstant.STATE_STOP || dispatchServer.getIsUse() == CommanConstant.STATE_STOP || dispatchServer.getState() == CommanConstant.STATE_START) {// 未部署或未启用或已开启
                continue;
            }
            DispatchServerService dispatchServerService = Application.getBean(DispatchServerService.class);
            batchOpearVo.setRemark(dispatchServer.getName() + "正在开启");
            Server server = CacheService.getServerById(dispatchServer.getServerId());
            boolean result = SshxcuteUtils.progressIsExistNew(server, dispatchServer.getPath(), Global.DISPATCH_PID_FILE);
            if (result) {
                batchOpearVo.isFail(dispatchServer.getName() + "服务已启动，请关闭重启");
                return false;
            }
            // 对应的world服务未启动
            if (CacheService.getWorldServerById(dispatchServer.getWorldId()).getState() != CommanConstant.STATE_START) {
                batchOpearVo.isFail(CacheService.getWorldServerById(dispatchServer.getWorldId()).getName() + "未启动");
                return false;
            }
            Object[] runResult = SshxcuteUtils.runExec(server, dispatchServer.getPath());
            // 启动服务出错
            if (runResult != null && runResult.length > 0 && (Integer) runResult[0] != CommanConstant.RESULT_TRUE_STATE) {
                batchOpearVo.isFail(dispatchServer.getName() + "启动出错");
                SshxcuteUtils.killExec(server, dispatchServer.getPath());
                return false;
            }
            int i = 0;
            while (true) {
                // 如果两分钟内检测不到结果，则返回失败结果
                if (i >= 120) {
                    batchOpearVo.isFail(dispatchServer.getName() + "启动超时");
                    startResult = false;
                    break;
                }
                // 先休眠1秒再测试是否启动成功
                Thread.sleep(1000L);
                // 获取启动日志并分析结果
                Boolean bStartDispatchResult = SshxcuteUtils.getBatchStdout(session, dispatchServer, dispatchServerService, Global.DISPATCH_SUCCESS_RESULT);
                // 如果启动有返回结果
                if (bStartDispatchResult != null) {
                    if (bStartDispatchResult) {
                        batchOpearVo.isSUCCESS(dispatchServer.getName() + "启动成功");
                        startResult = true;
                    } else {
                        batchOpearVo.isFail(dispatchServer.getName() + "启动失败 ");
                        SshxcuteUtils.killExec(server, dispatchServer.getPath());
                    }
                    break;
                }
                System.out.println("============check start dispatch " + dispatchServer.getName() + " => " + i);
                i++;
            }
        }
        return startResult;
    }

    // ==================================开启dispatch线程类start====================================
    /**
     * 关闭dispatch(多线程内部类)
     * 
     * @param batchOpearVo 操作vo
     * @param worldId worldID
     * @param dispatchServerIds 已開啟的dispatch服務Id
     */
    public boolean startDispatchInsideThread(HttpSession session, BatchOpearVo batchOpearVo, int worldId, ServerService serverService) throws Exception {
        boolean closeResult = false;
        List<DispatchServer> dispatchServerList = CacheService.getDispatchServerListByWorldId(worldId);
        // 设置开启dispatch数量
        batchOpearVo.setNum(dispatchServerList.size());
        // 循环启动dispatch线程
        for (DispatchServer dispatchServer : dispatchServerList) {
            StartDispatchThread startDispatchThread = new StartDispatchThread(session, dispatchServer, batchOpearVo, serverService);
            new Thread(startDispatchThread).start();
        }
        // 等待直到所有dispatch启动完成
        synchronized (batchOpearVo) {
            batchOpearVo.wait();
        }
        // 如果启动失败
        if (batchOpearVo.getStatus() != null && !batchOpearVo.getStatus()) {
            session.setAttribute("updateRestartError", batchOpearVo.getRemark());
        } else {
            closeResult = true;
        }
        return closeResult;
    }
    /**
     * 关闭dispatch內部線程類
     */
    public class StartDispatchThread implements Runnable {
        HttpSession    session;
        DispatchServer dispatchServer;
        BatchOpearVo   batchOpearVo;
        ServerService  serverService;

        public StartDispatchThread(HttpSession session, DispatchServer dispatchServer, BatchOpearVo batchOpearVo, ServerService serverService) {
            this.session = session;
            this.dispatchServer = dispatchServer;
            this.batchOpearVo = batchOpearVo;
            this.serverService = serverService;
        }

        @Override
        public void run() {
            try {
                if (batchOpearVo.getStatus() == null || batchOpearVo.getStatus() != false) {
                    batchOpearVo.setRemark(dispatchServer.getName() + "正在开启");
                }
                Server server = CacheService.getServerById(dispatchServer.getServerId());
                // 未部署或未启用
                if (dispatchServer.getIsDeploy() == CommanConstant.STATE_STOP || dispatchServer.getIsUse() == CommanConstant.STATE_STOP) {
                    return;
                }
                if (dispatchServer.getState() == CommanConstant.STATE_STOP) {
                    // 关闭Dispatch服务
                    Object[] runResult = SshxcuteUtils.runExec(server, dispatchServer.getPath());
                    // 启动服务出错
                    if (runResult != null && runResult.length > 0 && (Integer) runResult[0] != CommanConstant.RESULT_TRUE_STATE) {
                        batchOpearVo.isFail(dispatchServer.getName() + "启动出错");
                        SshxcuteUtils.killExec(server, dispatchServer.getPath());
                        return;
                    }
                    int i = 0;
                    while (true) {
                        if (i == 0) {
                            Thread.sleep(1200l);
                        } else {
                            Thread.sleep(1000l);
                        }
                        // 如果2分钟内没有结果，则返回失败
                        if (i >= 120) {
                            batchOpearVo.isFail(dispatchServer.getName() + "启动超时");
                            break;
                        }
                        // 获取输出结果
                        Boolean bResult = SshxcuteUtils.getBatchStdout(session, dispatchServer, Application.getBean(DispatchServerService.class), Global.DISPATCH_SUCCESS_RESULT);
                        if (bResult != null) {
                            if (bResult) {
                                batchOpearVo.isSUCCESS(dispatchServer.getName() + "启动成功");
                            } else {
                                batchOpearVo.isFail(dispatchServer.getName() + "启动失败 ");
                                SshxcuteUtils.killExec(server, dispatchServer.getPath());
                            }
                            break;
                        }
                        System.out.println("============check start dispatch " + dispatchServer.getName() + " => " + i);
                        i++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                batchOpearVo.isFail(dispatchServer.getName() + "开启出错");
            } finally {
                batchOpearVo.updateNum();
            }
        }
    }
    // ==================================开启dispatch线程类end====================================

    @Override
    public void initCache() {
        // TODO Auto-generated method stub
        CacheService.initDispatchServer();
    }
}
