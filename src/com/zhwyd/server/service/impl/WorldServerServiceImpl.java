package com.zhwyd.server.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import com.zhwyd.server.bean.AccountServer;
import com.zhwyd.server.bean.BeanInterface;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.bean.WorldServer;
import com.zhwyd.server.bean.vo.BatchOpearVo;
import com.zhwyd.server.bean.vo.ChangeServerVo;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.common.util.CommonUtil;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.controller.model.WorldServerModel;
import com.zhwyd.server.dao.WorldServerDao;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.service.ServiceInterface;
import com.zhwyd.server.service.WorldServerService;
import com.zhwyd.server.ssh.util.SshExecutorUtils;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
import com.zhwyd.server.thread.BatchCloseWorldThread;
import com.zhwyd.server.thread.BatchCloseWorldThreadNew;
import com.zhwyd.server.thread.BatchStartWorldThread;
import com.zhwyd.server.web.page.Pager;
public class WorldServerServiceImpl extends ServiceSupportImpl<WorldServer> implements WorldServerService, ServiceInterface {
    protected WorldServerDao worldServerDao;

    public void setWorldServerDao(WorldServerDao worldServerDao) {
        super.setDaoSupport(worldServerDao);
        this.worldServerDao = worldServerDao;
    }

    public Pager getWorldServerList(WorldServerModel worldServerModel) {
        return worldServerDao.getWorldServerList(worldServerModel);
    }

    public void saveOrUpdateWorld(WorldServer worldServers) {
        try {
            WorldServer worldServer = null;
            if (worldServers.getId() == null) {
                worldServer = new WorldServer();
            } else {
                worldServer = this.get(worldServers.getId());
            }
            worldServer.setGameId(worldServers.getGameId());
            worldServer.setServerId(worldServers.getServerId());
            worldServer.setAccountId(worldServers.getAccountId());
            worldServer.setModelPath(worldServers.getModelPath());
            worldServer.setUpdatePath(worldServers.getUpdatePath());
            worldServer.setName(worldServers.getName());
            worldServer.setLocalip(worldServers.getLocalip());
            worldServer.setPort(worldServers.getPort());
            worldServer.setAdminip(worldServers.getAdminip());
            worldServer.setAdminport(worldServers.getAdminport());
            worldServer.setHttpip(worldServers.getHttpip());
            worldServer.setPublicip(worldServers.getPublicip());
            worldServer.setHttpport(worldServers.getHttpport());
            worldServer.setAuthip(worldServers.getAuthip());
            worldServer.setAuthport(worldServers.getAuthport());
            worldServer.setServerpassword(worldServers.getServerpassword());
            worldServer.setAreaid(worldServers.getAreaid());
            worldServer.setMachinecode(worldServers.getMachinecode());
            worldServer.setServerType(worldServers.getServerType());
            worldServer.setDid(worldServers.getDid());
            worldServer.setPath(worldServers.getPath());
            worldServer.setState(worldServers.getState());
            worldServer.setIsDeploy(worldServers.getIsDeploy());
            worldServer.setOrders(worldServers.getOrders());
            this.saveOrUpdate(worldServer);
            CacheService.initWorldServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看运行日志 author:LKJ 2014-9-6
     * 
     * @param serverId
     * @throws Exception
     */
    public Map<String, Object> showStdout(BeanInterface beanInterface, HttpSession session) throws Exception {
        String successStr = Global.WORLD_SUCCESS_RESULT;
        SshxcuteUtils.showStdout(beanInterface, this, session, successStr);
        return null;
    }

    /**
     * 部署World服务
     * 
     * @throws Exception
     */
    public void deployServer(HttpServletResponse response, BeanInterface beanInterface, HttpSession session) {
        try {
            SshExecutorUtils.deployEachServer(response, beanInterface, this, Global.CONFIG_WORLD_PROPERTIES, session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新World服务
     * 
     * @throws Exception
     */
    public void updateServer(HttpServletResponse response, BeanInterface beanInterface, HttpSession session) throws Exception {
        String updateResult = "";
        WorldServer worldServer = (WorldServer) beanInterface;
        updateResult = SshExecutorUtils.updateEachServer(worldServer, session);
        if (updateResult.equals("success")) {
            System.out.println("更新成功~~！");
            worldServer.setUpdateTime(new Date());
            this.merge(worldServer);
            response.getWriter().write("success");
        } else {
            response.getWriter().write(updateResult);
        }
    }

    /**
     * 获取通配信息
     * 
     * @param worldServerModel
     * @return
     */
    @Override
    public String getWildcard(int worldId) {
        StringBuffer sb = new StringBuffer();
        WorldServer worldServer = CacheService.getWorldServerById(worldId);
        if (worldServer != null && worldServer.getId() > 0) {
            sb.append(worldServer.getLocalip()).append(",").append(worldServer.getPort()).append(",").append(worldServer.getAdminip()).append(",").append(worldServer.getAdminport()).append(",").append(worldServer.getHttpip()).append(",").append(worldServer.getPublicip()).append(",").append(worldServer.getHttpport()).append(",").append(worldServer.getAuthip()).append(",").append(worldServer.getAuthport()).append(",").append(worldServer.getAreaid()).append(",").append(worldServer.getMachinecode()).append(",").append(worldServer.getServerType()).append(",").append(worldServer.getBattleip()).append(",").append(worldServer.getBattleport()).append(",");
        }
        return sb.toString();
    }

    /**
     * 批量关闭World author:LKJ 2014-12-17
     * 
     * @param accountIds
     * @return
     * @throws Exception
     */
    public void batchCloseWorld(String worldIds, HttpSession session, Map<Integer, List<Integer>> openDispatchIdMap) throws Exception {
        System.out.println("worldIds=" + worldIds);
        if (!StringUtils.isEmpty(worldIds)) {
            WorldServerService worldServerService = Application.getBean(WorldServerService.class);
            String[] worldIdArray = worldIds.split(",");
            for (int i = 0; i < worldIdArray.length; i++) {
                System.out.println("worldIdArray[i]=" + worldIdArray[i]);
                Application.getManager().getSimpleThreadPool().execute(this.createCloseTask(Integer.valueOf(worldIdArray[i]), session, worldServerService, openDispatchIdMap));
            }
        }
    }

    /**
     * 创建新的批量关闭任务
     */
    private Runnable createCloseTask(int worldId, HttpSession session, WorldServerService worldServerService, Map<Integer, List<Integer>> openDispatchIdMap) {
        return new BatchCloseWorldThread(worldId, worldServerService, session, openDispatchIdMap);
    }

    /**
     * 批量关闭World
     * 
     * @author:LKJ 2014-12-17
     */
    @Override
    public void batchStartWorld(String worldIds, HttpSession session, Map<Integer, List<Integer>> openDispatchIdMap) throws Exception {
        if (!StringUtils.isEmpty(worldIds)) {
            WorldServerService worldServerService = Application.getBean(WorldServerService.class);
            String[] worldIdArray = worldIds.split(",");
            for (int i = 0; i < worldIdArray.length; i++) {
                System.out.println("worldIdArray[i]=" + worldIdArray[i]);
                Application.getManager().getSimpleThreadPool().execute(this.createStartTask(Integer.valueOf(worldIdArray[i]), session, worldServerService, openDispatchIdMap));
            }
        }
    }

    /**
     * 创建新的启动任务
     */
    private Runnable createStartTask(int worldId, HttpSession session, WorldServerService worldServerService, Map<Integer, List<Integer>> openDispatchIdMap) {
        return new BatchStartWorldThread(worldId, worldServerService, session, openDispatchIdMap);
    }

    /**
     * 开启world服务
     */
    public String startServer(BeanInterface beanInterface) throws Exception {
        System.out.println("开启world服务");
        StringBuffer stringBuffer = new StringBuffer();
        WorldServer worldServer = (WorldServer) beanInterface;
        if (worldServer.getIsDeploy() == CommanConstant.STATE_START) {
            int accountState = CacheService.getAccountServerById(worldServer.getAccountId()).getState();
            if (accountState == CommanConstant.STATE_START) {
                String startResult = SshxcuteUtils.startEachServer(worldServer, Global.WORLD_PID_FILE);
                stringBuffer.append(startResult);
            } else {
                stringBuffer.append("ID为：" + worldServer.getAccountId() + "ACCOUNT服务未启动，请先启动ACCOUNT！");
            }
        } else {
            stringBuffer.append("ID为：" + worldServer.getId() + "worldServer未部署");
        }
        return stringBuffer.toString();
    }

    /**
     * 关闭服务
     */
    public String killServer(BeanInterface beanInterface) throws Exception {
        String string = "";
        if (beanInterface.getIsDeploy() == CommanConstant.STATE_START) {
            string = SshxcuteUtils.stopEachServer(beanInterface, this, Global.WORLD_PID_FILE);
        } else {
            string = "ID为：" + beanInterface.getId() + "World未部署 </br>";
        }
        return string;
    }

    /**
     * 获取运行结果信息
     * 
     * @author:LKJ
     */
    public String getStdout(Server server, WorldServer worldServer, HttpSession session) throws Exception {
        boolean successResult = false;
        boolean exceptionResult = false;
        String result = "";
        Object[] stdResult = SshxcuteUtils.showStdout(server, worldServer.getPath());
        String successStr = Global.WORLD_SUCCESS_RESULT;
        String exceptionStr = "Exception";
        if (stdResult != null && stdResult.length > 0 && stdResult[1] != null) {
            successResult = CommonUtil.Pattern(successStr, stdResult[1].toString());
            exceptionResult = CommonUtil.Pattern(exceptionStr, stdResult[1].toString());
        }
        if (exceptionResult) {
            result = "false";
            SystemLogService.worldServerLog(session, ":" + worldServer.getId() + "," + stdResult[1].toString());
        } else if (successResult) {
            result = "true";
            CacheService.getWorldServerById(worldServer.getId()).setState(CommanConstant.STATE_START);
            worldServer.setState(CommanConstant.STATE_START);
            Application.getBean(WorldServerService.class).update(worldServer);
        }
        return result;
    }

    /**
     * 根据worldIds获取所对应的ACCOUNT中未开启的accountId
     */
    public StringBuilder getAccountState(String worldIds) {
        StringBuilder sb = new StringBuilder();
        try {
            String[] worldIdArray = worldIds.split(",");
            for (int i = 0; i < worldIdArray.length; i++) {
                AccountServer accountServer = CacheService.getAccountServerById(CacheService.getWorldServerById(Integer.valueOf(worldIdArray[i])).getAccountId());
                // 检测ACCOUNT服务是否开启
                if (accountServer.getState() == CommanConstant.STATE_STOP) {
                    sb.append(accountServer.getId()).append(",");
                }
            }
        } catch (Exception e) {
            sb.append(e.getMessage());
            e.printStackTrace();
        }
        return sb;
    }

    @Override
    public void updateState(int id, int state, int type) {
        WorldServer worldServer = this.get(id);
        if (type == 0) {// 0为更新运行状态
            CacheService.getWorldServerById(id).setState(state);
            worldServer.setState(state);
        } else if (type == 1) {// 1为更新部署状态
            CacheService.getWorldServerById(id).setIsDeploy(state);
            worldServer.setIsDeploy(state);
        }
        this.update(worldServer);
    }

    @Override
    public void writeBatchLog(HttpSession session, String logContent) {
        SystemLogService.batchWorldLog(session, logContent);
    }

    /**
     * 检测是否有关联
     */
    public boolean checkIsRelate(int worldId) {
        boolean checkResult = false;
        for (DispatchServer dispatchServer : CacheService.getDispatchServerAllList()) {
            if (dispatchServer.getWorldId() == worldId) {
                checkResult = true;
            }
        }
        return checkResult;
    }

    /**
     * 同步配置信息
     */
    @Override
    public void synchronizeConfig(String worldIds) {
        String[] ids = worldIds.split(",");
        for (int i = 0; i < ids.length; i++) {
            Map<String, String> configMap = SshxcuteUtils.getConfigMap(CacheService.getWorldServerById(Integer.valueOf(ids[i])), Global.CONFIG_WORLD_PROPERTIES);
            this.updateWorldByMap(Integer.valueOf(ids[i]), configMap);
        }
    }

    /**
     * 根据配置信息修改DB及缓存
     */
    private void updateWorldByMap(int dispatchId, Map<String, String> configMap) {
        WorldServer worldServer = this.get(dispatchId);
        worldServer.setLocalip(configMap.get("localip"));
        worldServer.setPort(Integer.valueOf(configMap.get("port")));
        worldServer.setAdminip(configMap.get("adminip"));
        worldServer.setAdminport(Integer.valueOf(configMap.get("adminport")));
        worldServer.setHttpip(configMap.get("httpip"));
        worldServer.setPublicip(configMap.get("publicip"));
        worldServer.setHttpip(configMap.get("httpip"));
        worldServer.setHttpport(Integer.valueOf(configMap.get("httpport")));
        worldServer.setAuthip(configMap.get("authip"));
        worldServer.setAuthport(Integer.valueOf(configMap.get("authport")));
        worldServer.setServerpassword(configMap.get("serverpassword"));
        worldServer.setAreaid(configMap.get("areaid"));
        worldServer.setMachinecode(Integer.valueOf(configMap.get("machinecode")));
        worldServer.setServerType(Integer.valueOf(configMap.get("serverType")));
        this.update(worldServer);
        configMap = null;
    }

    public String getConfig(JSONObject jsonObject) {
        String areaId = jsonObject.getString("areaId");
        WorldServer worldServer = worldServerDao.get("areaid", areaId);
        JSONObject json = new JSONObject();
        json.element("port", worldServer.getPort());
        json.element("adminport", worldServer.getAdminport());
        json.element("publicip", worldServer.getPublicip());
        json.element("httpport", worldServer.getHttpport());
        json.element("authport", worldServer.getAuthport());
        json.element("areaid", worldServer.getAreaid());
        json.element("machinecode", worldServer.getMachinecode());
        json.element("serverType", worldServer.getServerType());
        return json.toString();
    }

    @Override
    public BeanInterface getBean(Integer id) {
        return worldServerDao.get(id);
    }

    /**
     * 批量关闭World
     */
    public void batchCloseWorldNew(String worldIds, HttpSession session, Map<Integer, List<Integer>> openDispatchIdMap) throws Exception {
        System.out.println("worldIds=" + worldIds);
        if (!StringUtils.isEmpty(worldIds)) {
            WorldServerService worldServerService = Application.getBean(WorldServerService.class);
            String[] worldIdArray = worldIds.split(",");
            for (int i = 0; i < worldIdArray.length; i++) {
                System.out.println("worldIdArray[i]=" + worldIdArray[i]);
                Application.getManager().getSimpleThreadPool().execute(this.createCloseTaskNew(Integer.valueOf(worldIdArray[i]), session, worldServerService, openDispatchIdMap));
            }
        }
    }

    /**
     * 创建新的批量关闭任务
     */
    private Runnable createCloseTaskNew(int worldId, HttpSession session, WorldServerService worldServerService, Map<Integer, List<Integer>> openDispatchIdMap) {
        return new BatchCloseWorldThreadNew(worldId, worldServerService, session, openDispatchIdMap);
    }

    /**
     * 生成worldServer对象
     */
    public String createWorldServer(WorldServer worldServers, String mergeIds) {
        String createResult = "";
        try {
            String cnType = Application.getConfig("system", "cnType");// 分区前缀类型
            int serverId = worldServers.getServerId();// 服务器Id（哪台机子）
            Server server = CacheService.getServerById(serverId);
            int machinecode = worldServers.getMachinecode();// 分区号
            String publicip = server.getServerName().trim();// 公网ip
            String battleip = worldServers.getBattleip();// 对战服ip
            int battleport = worldServers.getBattleport();// 对战服端口
            String areaId = cnType + "_" + machinecode;// 加前缀的分编号，如“CN_1”
            int serverType = worldServers.getServerType();
            String path = getWorldPath(worldServers, cnType);
            String name = getWorldName(worldServers, cnType);
            List<WorldServer> worldServerListCheck = worldServerDao.getWorldServerByServerTypeAndAreaId(serverType, machinecode);
            if (worldServerListCheck != null && worldServerListCheck.size() > 0) {
                createResult = "存在相同類型和分區號的記錄";
                return createResult;
            }
            WorldServer worldServer = null;
            List<WorldServer> worldServerList = worldServerDao.getWorldServerListByServerId(worldServers.getServerId());
            if (worldServerList != null && worldServerList.size() > 0) {
                worldServer = new WorldServer();
                WorldServer worldServerTemp = worldServerList.get(0);
                BeanUtils.copyProperties(worldServer, worldServerTemp);
                if (worldServerDao.get(new String[] { "port", "serverId"}, new Object[] { worldServerTemp.getPort() + 1, serverId}) != null) {
                    createResult = "存在相同的port，生成失败";
                    return createResult;
                }
                worldServer.setId(null);
                worldServer.setPort(worldServerTemp.getPort() + 1);
                worldServer.setAdminport(worldServerTemp.getAdminport() + 1);
                worldServer.setHttpport(worldServerTemp.getHttpport() + 1);
                worldServer.setAuthport(worldServerTemp.getAuthport());
                worldServer.setUpdateTime(null);
                worldServer.setState(0);
                worldServer.setIsDeploy(1);
                worldServer.setMergeIds("");
            } else {
                worldServer = this.getDefaultWorld();
            }
            worldServer.setServerId(serverId);
            worldServer.setAccountId(CacheService.getAccountServerByServerId(serverId).getId());
            worldServer.setName(name);
            worldServer.setPublicip(publicip);
            worldServer.setAreaid(areaId);
            worldServer.setMachinecode(machinecode);
            worldServer.setBattleport(battleport);
            worldServer.setBattleip(battleip);
            worldServer.setPath(path);
            worldServer.setServerType(worldServers.getServerType());
            worldServer.setOrders(this.getMaxOrderByType(serverType, worldServers.getOrders()) + 1);
            if (worldServers.getIsDeploy() != null) {
                worldServer.setIsDeploy(worldServers.getIsDeploy());
            }
            // 合服區
            if (!StringUtils.isEmpty(mergeIds)) {
                // String mergeIds = "";
                StringBuilder sb = new StringBuilder();
                String[] mergeIdArray = mergeIds.split(",");
                // if (mergeIdArray.length == 2) {
                for (String id : mergeIdArray) {
                    if (!StringUtils.isNumeric(id)) {
                        createResult = "合服id串格式錯誤";
                        return createResult;
                    }
                    String mergeId = cnType + "_" + id;
                    sb.append(mergeId).append(",");
                }
                mergeIds = sb.substring(0, sb.lastIndexOf(","));
                // } else if (mergeIdArray.length == 1) {
                // mergeIds = cnType + "_" + mergeIdArray[0];
                // } else {
                // createResult = "名稱格式錯誤";
                // return createResult;
                // }
                worldServer.setMergeIds(mergeIds);
            }
            this.saveOrUpdate(worldServer);
            CacheService.initWorldServer();
            createResult = "true";
        } catch (Exception e) {
            e.printStackTrace();
            createResult = "遇到未知错误";
        }
        return createResult;
    }

    public String getWorldPath(WorldServer worldServer, String cnType) {
        int machinecode = worldServer.getMachinecode();// 填
        int serverType = worldServer.getServerType();
        String path = "";
        if (serverType == 3 && cnType.equals("CN")) {
            path = "/data/apps/gunsoul/worldServer_" + serverType + "_" + (machinecode - 10000);
        } else {
            path = "/data/apps/gunsoul/worldServer_" + serverType + "_" + machinecode;
        }
        return path;
    }

    public String getWorldName(WorldServer worldServer, String cnType) {
        int machinecode = worldServer.getMachinecode();// 填
        int num = machinecode / 10000 * 10000;
        int areaNum = machinecode - num;
        String name = "";
        if (worldServer.getServerType() == 3 && cnType.equals("CN")) {
            name = "硬核" + areaNum + "区 " + worldServer.getName();
        } else {
            name = machinecode + "区 " + worldServer.getName();
        }
        return name;
    }

    /**
     * 默认world
     * 
     * @return
     */
    public WorldServer getDefaultWorld() {
        WorldServer worldServer = new WorldServer();
        worldServer.setModelPath(Global.WORLD_MODEL_PATH);
        worldServer.setUpdatePath(Global.WORLD_UPDATE_PATH);
        worldServer.setGameId(1);
        worldServer.setLocalip("0.0.0.0");
        worldServer.setPort(6860);
        worldServer.setAdminip("0.0.0.0");
        worldServer.setAdminport(28960);
        worldServer.setHttpip("0.0.0.0");
        worldServer.setHttpport(6660);
        worldServer.setAuthip("127.0.0.1");
        worldServer.setAuthport(6100);
        worldServer.setServerpassword("gunsoul");
        worldServer.setState(0);
        worldServer.setIsDeploy(1);
        return worldServer;
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
        String sql = "select world.orders from world_server as world where {0} order by world.orders desc";
        String condition = "";
        if (num > 0) {
            order = num;
            condition = "orders >= " + num + " and orders < " + (num + 10000);
        } else {
            order = 0;
            condition = "orders < " + 10000;
        }
        sql = sql.replace("{0}", condition);
        List list = worldServerDao.createSqlQuery(sql).list();
        if (list != null && list.size() > 0) {
            order = (int) list.get(0);
        }
        return order;
    }

    /**
     * 迁移当前机子的数据到其他机子
     */
    public String updateCfgToOtherServer(ChangeServerVo changeServerVo) {
        String result = "success";
        try {
            WorldServer worldServer = CacheService.getWorldServerById(changeServerVo.getId());
            changeServerVo.setRemark("修改" + worldServer.getName() + "数据库配置中");
            if (changeServerVo.getServerId() == worldServer.getServerId()) {
                changeServerVo.setRemark("服务已在当前服务器，请确认目标服务器是否正确");
                result = "服务已在当前服务器，请确认目标服务器是否正确";
                return result;
            }
            int serverId = changeServerVo.getServerId();// 迁移目标serverId
            Server server = CacheService.getServerById(serverId);
            String publicip = server.getServerName().trim();// 目标机子公共ip
            // 获取排好序的world列表（port倒序）
            List<WorldServer> worldServerList = worldServerDao.getWorldServerListByServerId(serverId);
            if (worldServerList != null && worldServerList.size() > 0) {
                WorldServer worldServerTemp = worldServerList.get(0);// 获取port最大的WorldServer
                // 判断目标机子上是否已经存在改端口
                if (worldServerDao.get(new String[] { "port", "serverId"}, new Object[] { worldServerTemp.getPort() + 1, serverId}) != null) {
                    result = "world存在相同的port，生成失败";
                    System.out.println("world存在相同的port，生成失败");
                    return result;
                }
                worldServer.setPort(worldServerTemp.getPort() + 1);
                worldServer.setAdminport(worldServerTemp.getAdminport() + 1);
                worldServer.setHttpport(worldServerTemp.getHttpport() + 1);
            } else {// 如果该机子没有记录，则设置初设值
                worldServer.setPort(6860);
                worldServer.setAdminport(28960);
                worldServer.setHttpport(6660);
            }
            worldServer.setServerId(serverId);
            worldServer.setAccountId(CacheService.getAccountServerByServerId(serverId).getId());
            worldServer.setPublicip(publicip);
            this.saveOrUpdate(worldServer);
            CacheService.initWorldServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 同步远程配置
     */
    public boolean synCfgByDeploy(BeanInterface beanInterface, BatchOpearVo batchOpearVo, HttpSession session) throws Exception {
        boolean result = false;
        WorldServer worldServer = (WorldServer) beanInterface;
        String wildcards = this.getWildcard(worldServer.getId());
        batchOpearVo.setRemark("正在同步：" + Global.CONFIG_WORLD_PROPERTIES + "配置");
        // 更新world配置文件
        Object[] updateResult = SshxcuteUtils.synWildcard(beanInterface, wildcards, Global.CONFIG_WORLD_PROPERTIES);
        if (updateResult == null || updateResult.length <= 0 || (Integer) updateResult[0] != CommanConstant.RESULT_TRUE_STATE) {
            batchOpearVo.isFail("更新" + Global.CONFIG_WORLD_PROPERTIES + "失败");// 失敗
            return result;
        }
        // 更新jdbc配置
        batchOpearVo.setRemark("正在同步：" + Global.CONFIG_WORLD_JDBC + "配置");
        // 获取jdbc路径
        String jdbcPath = CommonUtil.getJdbcPath(session.getAttribute("worldJdbcType").toString());
        // 同步jdbc配置文件
        Object[] updateJdbcResult = SshxcuteUtils.synWildcard(beanInterface, jdbcPath, Global.CONFIG_WORLD_JDBC);
        if (updateJdbcResult == null || updateJdbcResult.length <= 0 || (Integer) updateJdbcResult[0] != CommanConstant.RESULT_TRUE_STATE) {
            batchOpearVo.isFail("更新" + Global.CONFIG_WORLD_JDBC + "失败");// 失敗
            return result;
        }
        // 更新run.sh配置
        batchOpearVo.setRemark("正在同步：" + Global.CONFIG_WORLD_RUN + "配置");
        // 获取run
        String run = "";
        if ("1".equals(session.getAttribute("worldMemory").toString())) {
            run = Global.RUN_1G;
        } else if ("2".equals(session.getAttribute("worldMemory").toString())) {
            run = Global.RUN_2G;
        } else if ("4".equals(session.getAttribute("worldMemory").toString())) {
            run = Global.RUN_4G;
        } else if ("6".equals(session.getAttribute("worldMemory").toString())) {
            run = Global.RUN_6G;
        }
        // 同步run.sh配置文件
        Object[] updateRunResult = SshxcuteUtils.synWildcard(beanInterface, run, Global.CONFIG_WORLD_RUN);
        if (updateRunResult == null || updateRunResult.length <= 0 || (Integer) updateRunResult[0] != CommanConstant.RESULT_TRUE_STATE) {
            batchOpearVo.isFail("更新" + Global.CONFIG_WORLD_RUN + "失败");// 失敗
            return result;
        }
        batchOpearVo.setRemark("正在同步：" + Global.CONFIG_WORLD_VERSION + "配置");
        // 更新version.xml文件
        Object[] updateVersionResult = SshxcuteUtils.synWildcard(beanInterface, worldServer.getMachinecode().toString(), Global.CONFIG_WORLD_VERSION);
        if (updateVersionResult == null || updateVersionResult.length <= 0 || (Integer) updateVersionResult[0] != CommanConstant.RESULT_TRUE_STATE) {
            batchOpearVo.isFail("更新" + Global.CONFIG_WORLD_VERSION + "失败");// 失敗
            return result;
        } else {
            result = true;
        }
        return result;
    }

    public void synVersion(HttpSession session, String ids, String fileName) {
        String[] beanIds = ids.split(",");
        session.removeAttribute(Global.BATCH_UPDATE_REMOTE_CFG);
        session.removeAttribute(Global.UPDATE_CFG_BEAN_LIST);
        List<BeanInterface> beanInterfaceList = new ArrayList<BeanInterface>();
        List<BatchOpearVo> batchOpearVoList = new ArrayList<BatchOpearVo>();
        BatchOpearVo batchOpearVo = null;
        for (int i = 0; i < beanIds.length; i++) {
            batchOpearVo = new BatchOpearVo();
            batchOpearVo.setId(Integer.valueOf(beanIds[i]));
            batchOpearVo.setRemark("开始更新");
            System.out.println("更新" + beanIds[i]);
            // 更新文件操作
            Application.getManager().getSimpleThreadPool().execute(this.createSynVersionTask(session, this.get(Integer.valueOf(beanIds[i])), batchOpearVo, fileName));
            beanInterfaceList.add(this.get(Integer.valueOf(beanIds[i])));
            batchOpearVoList.add(batchOpearVo);
        }
        session.setAttribute(Global.BATCH_UPDATE_REMOTE_CFG, batchOpearVoList);
        session.setAttribute(Global.UPDATE_CFG_BEAN_LIST, beanInterfaceList);
    }

    /**
     * 创建同步version文件任务
     */
    private Runnable createSynVersionTask(HttpSession session, WorldServer worldServer, BatchOpearVo batchOpearVo, String fileName) {
        return new BatchSynVersionThread(session, worldServer, batchOpearVo, fileName);
    }
    /**
     * 更新并重啟內部線程類
     */
    public class BatchSynVersionThread implements Runnable {
        HttpSession  session;
        BatchOpearVo batchOpearVo;
        WorldServer  worldServer;
        String       fileName;

        public BatchSynVersionThread(HttpSession session, WorldServer worldServer, BatchOpearVo batchOpearVo, String fileName) {
            this.session = session;
            this.worldServer = worldServer;
            this.batchOpearVo = batchOpearVo;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            try {
                batchOpearVo.setRemark("正在同步：" + Global.CONFIG_WORLD_VERSION + "配置");
                // 更新version.xml文件
                Object[] updateVersionResult = SshxcuteUtils.synWildcard(worldServer, worldServer.getMachinecode().toString(), fileName);
                if (updateVersionResult == null || updateVersionResult.length <= 0 || (Integer) updateVersionResult[0] != CommanConstant.RESULT_TRUE_STATE) {
                    batchOpearVo.isFail("更新" + Global.CONFIG_WORLD_VERSION + "失败");// 失敗
                } else {
                    batchOpearVo.isSUCCESS("更新" + Global.CONFIG_WORLD_VERSION + "成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检测Dispatch是否关闭完成
     * 
     * @author:LKJ @2015-3-17
     * @param openDispatchIds 要关闭的Dispatch标识号
     * @param worldServer 世界服务
     * @param server
     * @param num 循环次数
     * @return 返回true时表示已开启的Dispatch服务已经关闭完成
     * @throws Exception
     */
    public boolean dispatchCloseIsSuccess(List<Integer> openDispatchIds, WorldServer worldServer, Server server, int num) throws Exception {
        if (openDispatchIds.size() == 0) {
            return false;
        }
        // System.out.println("=====openDispatchIds.Size====="+openDispatchIds.size());
        // System.out.println("=====openDispatchIds1====="+openDispatchIds.get(0));
        // System.out.println("=====openDispatchIds2====="+openDispatchIds.get(openDispatchIds.size()-1));
        // boolean dispatchCloseIsSuccess = false;
        // boolean successResult = false;
        // int number = 0;
        // if (num > 0) {
        // number = num;
        // }
        // Object[] stdResult = SshxcuteUtils.showStdoutLast(server, worldServer.getPath());
        // System.out.println("=====openDispatchIds3====="+openDispatchIds.get(0));
        // for (int i = 0; i < openDispatchIds.size(); i++) {
        // System.out.println("=====openDispatchIdsssss"+i+"====="+openDispatchIds.get(i));
        // }
        // for (int dispatchId : openDispatchIds) {
        // if (number > 100) {
        // return false;
        // }
        // System.out.println("========================检测Dispatch是否关闭完成number=" + number);
        // if (stdResult != null && stdResult.length > 0 && stdResult[1] != null) {
        // successResult = CommonUtil.Pattern(Global.DISPATCH_SUCCESS_CLOSE_RESULT.replace("{0}", String.valueOf(dispatchId)), stdResult[1].toString());
        // if (!successResult) {
        // Thread.sleep(1000);
        // ++number;
        // this.dispatchCloseIsSuccess(openDispatchIds, worldServer, server, number);
        // }
        // } else {
        // Thread.sleep(1000);
        // ++number;
        // this.dispatchCloseIsSuccess(openDispatchIds, worldServer, server, number);
        // }
        // }
        boolean dispatchCloseIsSuccess = false;
        int i = 1;
        while (true) {
            if (i == 1) {
                // dispatch关闭时会停500毫秒
                Thread.sleep(600L);
            } else {
                Thread.sleep(500L);
            }
            Object[] stdResult = SshxcuteUtils.showStdoutLast(server, worldServer.getPath());
            if (stdResult != null && stdResult.length > 0 && stdResult[1] != null) {
                if (CommonUtil.Pattern(Global.ALL_DISPATCH_SUCCESS_CLOSE_RESULT, stdResult[1].toString())) {
                    System.out.println("检测All DispatchServer Is Closed成功");
                    dispatchCloseIsSuccess = true;
                    break;
                }
            }
            System.out.println("=====================检测All DispatchServer Is Closed：" + i);
            // 如果两分钟内world还没有关闭完成，则退出检测
            if (i >= 240) {
                break;
            }
            i++;
        }
        return dispatchCloseIsSuccess;
    }

    /**
     * 開啟world服務
     * 
     * @param batchOpearVo 操作vo
     * @param worldServer world服務
     * @param session HttpSession
     */
    public boolean startWorld(BatchOpearVo batchOpearVo, WorldServer worldServer, HttpSession session, ServerService serverService) throws Exception {
        boolean startResult = false;
        batchOpearVo.setRemark(worldServer.getName() + "正在开启");
        WorldServerService worldServerService = Application.getBean(WorldServerService.class);
        Server server = serverService.get(worldServer.getServerId());
        // 判断world是否已开启
        boolean result = SshxcuteUtils.progressIsExistNew(server, worldServer.getPath(), Global.WORLD_PID_FILE);
        if (result) {
            batchOpearVo.isFail(worldServer.getName() + "服务已启动，请关闭重启");
            return startResult;
        }
        Object[] runResult = SshxcuteUtils.runExec(server, worldServer.getPath());
        // 启动服务出错，关闭服务
        if (runResult != null && runResult.length > 0 && (Integer) runResult[0] != CommanConstant.RESULT_TRUE_STATE) {
            batchOpearVo.isFail(worldServer.getName() + "world启动出错");
            SshxcuteUtils.killExec(server, worldServer.getPath());
            return startResult;
        }
        int i = 0;
        while (true) {
            if (i == 0) {
                Thread.sleep(4000L);
            } else {
                Thread.sleep(500L);
            }
            if (i >= 120) {
                batchOpearVo.isFail(worldServer.getName() + "world启动超时");
                break;
            }
            Boolean bResult = SshxcuteUtils.getBatchStdout(session, worldServer, worldServerService, Global.WORLD_SUCCESS_RESULT);
            if (bResult != null) {
                if (bResult) {
                    batchOpearVo.isSUCCESS(worldServer.getName() + "world启动成功");
                    startResult = true;
                } else {
                    batchOpearVo.isFail(worldServer.getName() + "world启动失败");
                    SshxcuteUtils.killExec(server, worldServer.getPath());
                }
                break;
            }
            System.out.println("============check start world " + worldServer.getName() + " => " + i);
            i++;
        }
        return startResult;
    }

    /**
     * 關閉world
     * 
     * @param batchOpearVo 操作vo
     * @param dispatchServerIds 已開啟的dispatch服務Id
     * @param worldId worldID
     */
    public boolean closeWorld(BatchOpearVo batchOpearVo, WorldServer worldServer, List<Integer> dispatchServerIds, ServerService serverService) throws Exception {
        boolean closeResult = false;
        batchOpearVo.setRemark(worldServer.getName() + "正在关闭");
        WorldServerService worldServerService = Application.getBean(WorldServerService.class);
        Server server = serverService.get(worldServer.getServerId());
        // worldServer已关闭的情况
        if (worldServer.getState() == CommanConstant.STATE_STOP) {
            boolean result = SshxcuteUtils.progressIsExistNew(server, worldServer.getPath(), Global.WORLD_PID_FILE);
            if (!result) {
                return true;
            }
        }
        if (dispatchServerIds != null && dispatchServerIds.size() > 0) {
            // System.out.println("=====dispatchServerIds=====" + dispatchServerIds.toArray().toString());
            boolean dispatchCloseIsSuccess = worldServerService.dispatchCloseIsSuccess(dispatchServerIds, worldServer, server, CommanConstant.STATE_STOP);
            // 检测Dispatch服务是否关闭完成，若未完成则不关闭world服务
            if (!dispatchCloseIsSuccess) {
                batchOpearVo.isFail("分发服关闭未完成，请关闭再试");
                return closeResult;
            }
        }
        Object[] killResult = SshxcuteUtils.killExec(server, worldServer.getPath());
        if (killResult != null && killResult.length > 0 && (Integer) killResult[0] != CommanConstant.RESULT_TRUE_STATE) {
            batchOpearVo.isFail(worldServer.getName() + "world关闭出错");
            return closeResult;
        }
        worldServer.setState(CommanConstant.STATE_STOP);
        worldServerService.update(worldServer);
        CacheService.getWorldServerById(worldServer.getId()).setState(CommanConstant.STATE_STOP);
        batchOpearVo.isSUCCESS(worldServer.getName() + "world关闭成功");
        closeResult = true;
        return closeResult;
    }

    @Override
    public void initCache() {
        CacheService.initWorldServer();
    }
}
