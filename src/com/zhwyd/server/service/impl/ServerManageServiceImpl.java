package com.zhwyd.server.service.impl;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import com.zhwyd.server.bean.AccountServer;
import com.zhwyd.server.bean.BeanInterface;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.IpdServer;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.bean.WorldServer;
import com.zhwyd.server.bean.vo.BatchOpearVo;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.common.util.CommonUtil;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.controller.model.ServerManageModel;
import com.zhwyd.server.controller.model.WorldServerModel;
import com.zhwyd.server.service.DispatchServerService;
import com.zhwyd.server.service.ServerManageService;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.service.ServiceInterface;
import com.zhwyd.server.service.WorldServerService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
import com.zhwyd.server.thread.BatchStartWorldAndDispatchThreadNew;
import com.zhwyd.server.web.page.Pager;
public class ServerManageServiceImpl extends BaseServiceImpl implements ServerManageService {
    /**
     * 获取总管理列表
     * 
     * @author:LKJ @2014-9-4
     */
    @SuppressWarnings("unchecked")
    public Pager getServerManageModelList(WorldServerModel worldServerModel) throws Exception {
        List<ServerManageModel> serverManageModelList = new ArrayList<ServerManageModel>();
        ServerManageModel server = null;
        AccountServer accountServer = null;
        IpdServer ipdServer = null;
        List<DispatchServer> dispatchServerList = null;
        Pager pager = Application.getBean(WorldServerService.class).getWorldServerList(worldServerModel);
        List<WorldServer> worldServerList = (List<WorldServer>) pager.getList();
        if (worldServerList.size() > 0) {
            Collections.sort(worldServerList, new Comparator<WorldServer>() {
                public int compare(WorldServer worldServer1, WorldServer worldServer2) {
                    return worldServer1.getOrders().compareTo(worldServer2.getOrders());
                }
            });
        }
        for (WorldServer worldServerTemp : worldServerList) {
            server = new ServerManageModel();
            accountServer = CacheService.getAccountServerById(worldServerTemp.getAccountId());
            server.setAreaId(worldServerTemp.getAreaid());
            server.setAccountId(accountServer.getId());
            server.setAccountName(accountServer.getName());
            server.setAccountState(accountServer.getState());
            server.setWorldId(worldServerTemp.getId());
            server.setWorldName(worldServerTemp.getName());
            server.setWorldState(worldServerTemp.getState());
            dispatchServerList = CacheService.getDispatchServerListByWorldId(worldServerTemp.getId());
            int ipdId = 0;
            if (dispatchServerList != null && dispatchServerList.size() > 0) {
                // if (dispatchServerList.size() > 3) {
                // throw new Exception("ID为：" + worldServerTemp.getId() + ",名称为：" + worldServerTemp.getName() + "的WorldServer所关联的Dispatch数量大于3个！");
                // }
                int i = 1;
                for (DispatchServer dispatchServerTemp : dispatchServerList) {
                    switch (i) {
                    case 1:
                        ipdId = dispatchServerTemp.getIpdId();
                        server.setDispatchOneId(dispatchServerTemp.getId());
                        server.setDispatchOneName(dispatchServerTemp.getName());
                        server.setDispatchOneState(dispatchServerTemp.getState());
                        break;
                    case 2:
                        server.setDispatchTwoId(dispatchServerTemp.getId());
                        server.setDispatchTwoName(dispatchServerTemp.getName());
                        server.setDispatchTwoState(dispatchServerTemp.getState());
                        break;
                    case 3:
                        server.setDispatchThreeId(dispatchServerTemp.getId());
                        server.setDispatchThreeName(dispatchServerTemp.getName());
                        server.setDispatchThreeState(dispatchServerTemp.getState());
                        break;
                    case 4:
                        server.setDispatchFourId(dispatchServerTemp.getId());
                        server.setDispatchFourName(dispatchServerTemp.getName());
                        server.setDispatchFourState(dispatchServerTemp.getState());
                        break;
                    case 5:
                        server.setDispatchFiveId(dispatchServerTemp.getId());
                        server.setDispatchFiveName(dispatchServerTemp.getName());
                        server.setDispatchFiveState(dispatchServerTemp.getState());
                        break;
                    default:
                        break;
                    }
                    i++;
                }
            }
            if (ipdId > 0) {
                ipdServer = CacheService.getIpdServerById(ipdId);
                server.setIpdId(ipdServer.getId());
                server.setIpdName(ipdServer.getName());
                server.setIpdState(ipdServer.getState());
            }
            serverManageModelList.add(server);
        }
        worldServerList = null;
        pager.setList(serverManageModelList);
        return pager;
        // return getPagerByList(serverManageModelList, worldServerModel.getPager());
    }

    /**
     * 开启服务(停用)
     */
    public void startServer(WorldServerService worldServerService, Integer worldId, HttpSession session, StringBuffer stringBuffer) throws Exception {
        System.out.println("开启server");
        session.removeAttribute("startServerInfo");
        session.setAttribute("startServerInfo", stringBuffer + "</br>");
        WorldServer worldServer = worldServerService.get(worldId);
        if (worldServer == null) {
            stringBuffer.append("</br>ID为：" + worldId + "的World不存在");
            return;
        }
        Server server = CacheService.getServerById(worldServer.getServerId());
        if (server == null) {
            stringBuffer.append("</br>ID为：" + worldServer.getServerId() + "的Server不存在");
            return;
        }
        String startWorldResult = worldServerService.startServer(worldServer);
        System.out.println("startWorldResult=" + startWorldResult);
        if (startWorldResult.equals("true")) {
            if (this.startWorld(server, worldServer, stringBuffer, session)) {
                this.startDispatch(server, worldId, stringBuffer, session);
                System.out.println("启动完成");
                stringBuffer.append("</br><font color='red'>启动完成</font>");
            }
        }
        session.setAttribute("startServerInfo", stringBuffer + "</br>");
    }

    /**
     * 关闭服务(停用)
     */
    public void closeServer(WorldServerService worldServerService, Integer worldId, HttpSession session, StringBuffer stringBuffer) throws Exception {
        List<Integer> openDispatchIds = new ArrayList<Integer>();
        session.removeAttribute("closeServerInfo");
        session.setAttribute("closeServerInfo", stringBuffer + "</br>");
        List<DispatchServer> dispatchServerList = CacheService.getDispatchServerListByWorldId(worldId);
        for (DispatchServer dispatchServer : dispatchServerList) {
            if (dispatchServer.getIsDeploy() == CommanConstant.STATE_START && dispatchServer.getState() == CommanConstant.STATE_START) {// 服务已部署并开启中
                stringBuffer.append(Application.getBean(DispatchServerService.class).killServer(dispatchServer));
                session.setAttribute("closeServerInfo", stringBuffer + "</br>");
                openDispatchIds.add(dispatchServer.getDispatchServerId());
            }
        }
        // 检测Dispatch是否已经关闭
        boolean closeState = this.getCloseState(dispatchServerList, 0);
        System.out.println("closeState=" + closeState);
        if (!closeState) {
            System.out.println("closeState=" + closeState);
            WorldServer worldServer = worldServerService.get(worldId);
            Server server = CacheService.getServerById(worldServer.getServerId());
            boolean dispatchCloseIsSuccess = this.DispatchCloseIsSuccess(openDispatchIds, worldServer, server, 0);
            System.out.println("dispatchCloseIsSuccess=" + dispatchCloseIsSuccess);
            if (dispatchCloseIsSuccess) {// 当Dispatch服务完全关闭是才开始关闭world服务
                stringBuffer.append(Application.getBean(WorldServerService.class).killServer(worldServer));
                stringBuffer.append("<font red='greed'>关闭完成</font>");
            } else {
                stringBuffer.append("<font red='red'>Dispatch关闭未完成，请重新检测</font>");
            }
        } else {
            stringBuffer.append("<font red='red'>Dispatch关闭未完成，请重新检测</font>");
        }
        session.setAttribute("closeServerInfo", stringBuffer + "</br>");
    }

    /**
     * 获取Dispatch关闭状态(停用)
     * 
     * @author:LKJ @2015-3-17
     * @param dispatchServerList Dispatch列表
     * @param num 循环次数
     * @return 返回true时表示Dispatch未正常关闭
     * @throws Exception
     */
    private boolean getCloseState(List<DispatchServer> dispatchServerList, int num) throws Exception {
        System.out.println("获取Dispatch关闭状态");
        if (dispatchServerList == null || dispatchServerList.size() == 0) {
            return true;
        }
        int number = 0;
        boolean closeState = false;
        Server server = null;
        if (num > 0) {
            number = num;
        }
        if (number > 120) {// true代表Dispatch服务还在运行
            return true;
        }
        System.out.println("number=" + number);
        for (DispatchServer dispatchServer : dispatchServerList) {
            if (dispatchServer.getIsDeploy() == CommanConstant.STATE_STOP) {// 未部署
                continue;
            }
            if (dispatchServer.getIsUse() == CommanConstant.STATE_STOP) {// 未启用
                continue;
            }
            // if (dispatchServer.getState() == CommanConstant.STATE_STOP) {// 未启动
            // continue;
            // }
            server = CacheService.getServerById(dispatchServer.getServerId());
            closeState = SshxcuteUtils.progressIsExistNew(server, dispatchServer.getPath(), Global.DISPATCH_PID_FILE);
            System.out.println("closeState=" + closeState);
            // 开启状态
            if (closeState) {
                Thread.sleep(2000);
                ++number;
                this.getCloseState(dispatchServerList, number);
            }
        }
        return closeState;
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
    private boolean DispatchCloseIsSuccess(List<Integer> openDispatchIds, WorldServer worldServer, Server server, int num) throws Exception {
        System.out.println("检测Dispatch是否关闭完成");
        if (openDispatchIds.size() == 0) {
            return false;
        }
        boolean dispatchCloseIsSuccess = false;
        boolean successResult = false;
        int number = 0;
        if (num > 0) {
            number = num;
        }
        if (number > 60) {
            return false;
        }
        System.out.println("number=" + number);
        Object[] stdResult = SshxcuteUtils.showStdoutLast(server, worldServer.getPath());
        for (int dispatchId : openDispatchIds) {
            if (stdResult != null && stdResult.length > 0 && (Integer) stdResult[0] == CommanConstant.RESULT_TRUE_STATE) {
                successResult = CommonUtil.Pattern(Global.DISPATCH_SUCCESS_CLOSE_RESULT.replace("{0}", String.valueOf(dispatchId)), stdResult[1].toString());
                if (!successResult) {
                    Thread.sleep(2000);
                    ++number;
                    this.DispatchCloseIsSuccess(openDispatchIds, worldServer, server, number);
                }
            } else {
                Thread.sleep(2000);
                ++number;
                this.DispatchCloseIsSuccess(openDispatchIds, worldServer, server, number);
            }
        }
        dispatchCloseIsSuccess = true;
        return dispatchCloseIsSuccess;
    }

    /**
     * 检查world服务是否启动成功
     */
    public boolean startWorld(Server server, WorldServer worldServer, StringBuffer stringBuffer, HttpSession session) throws Exception {
        System.out.println("检查world");
        boolean resutl = false;
        String worldStdout = "";
        WorldServerService worldServerService = Application.getBean(WorldServerService.class);
        int i = 0;
        while (true) {
            worldStdout = worldServerService.getStdout(server, worldServer, session);
            System.out.println("worldStdout=" + worldStdout);
            if (!StringUtils.isEmpty(worldStdout) && (worldStdout.equals("true") || worldStdout.equals("false"))) {
                if (worldStdout.equals("true")) {
                    stringBuffer.append("ID为：" + worldServer.getId() + "的WorldServer开启成功！");
                    resutl = true;
                } else if (worldStdout.equals("false")) {
                    stringBuffer.append("===========World服务启动失败===========");
                    SshxcuteUtils.killExec(server, worldServer.getPath());
                }
                break;
            }
            Thread.sleep(3000);
            if (i >= 120) {
                stringBuffer.append("===========World服务启动超时===========");
                System.out.println("i=" + i);
                break;
            }
            i++;
            System.out.println("检查world i=" + i);
        }
        session.setAttribute("startServerInfo", stringBuffer + "</br>");
        return resutl;
    }

    /**
     * 开启Dispatch服务(停用)
     */
    public void startDispatch(Server server, int worldId, StringBuffer stringBuffer, HttpSession session) throws Exception {
        System.out.println("开启Dispatch");
        List<DispatchServer> dispatchServerList = CacheService.getDispatchServerListByWorldId(worldId);
        DispatchServerService dispatchServerService = Application.getBean(DispatchServerService.class);
        for (DispatchServer dispatchServer : dispatchServerList) {
            if (dispatchServer.getIsDeploy() != CommanConstant.STATE_START) {
                stringBuffer.append("ID为：" + dispatchServer.getId() + ",名称为：" + dispatchServer.getName() + "的Dispatch未部署</br>");
                continue;
            }
            if (dispatchServer.getState() != CommanConstant.STATE_STOP) {
                stringBuffer.append("ID为：" + dispatchServer.getId() + ",名称为：" + dispatchServer.getName() + "的Dispatch已开启</br>");
                continue;
            }
            if (dispatchServer.getIsUse() == CommanConstant.STATE_STOP) {
                stringBuffer.append("ID为：" + dispatchServer.getId() + ",名称为：" + dispatchServer.getName() + "的Dispatch处于禁用状态，若要启动该Dispatch，请修改其启用状态</br>");
                continue;
            }
            String startDispatchResult = dispatchServerService.startServer(dispatchServer);
            stringBuffer.append(startDispatchResult + "</br>");
            if (startDispatchResult.equals("true")) {
                String dispatchStdout = "";
                int i = 0;
                while (true) {
                    dispatchStdout = dispatchServerService.getStdout(server, dispatchServer);
                    if (!StringUtils.isEmpty(dispatchStdout) && (dispatchStdout.equals("true") || dispatchStdout.equals("false"))) {
                        System.out.println("进来");
                        System.out.println("dispatchStdout=" + dispatchStdout);
                        if (dispatchStdout.equals("true")) {
                            stringBuffer.append("ID为：" + dispatchServer.getId() + "的DIspatch服务启动成功！</br>");
                        } else if (dispatchStdout.equals("false")) {
                            stringBuffer.append("===========DIspatch服务启动失败===========</br>");
                            SshxcuteUtils.killExec(server, dispatchServer.getPath());
                        }
                        break;
                    }
                    Thread.sleep(2500);
                    if (i == 120) {
                        stringBuffer.append("===========DIspatch服务启动超时===========</br>");
                        break;
                    }
                    i++;
                }
            }
            session.setAttribute("startServerInfo", stringBuffer + "</br>");
        }
    }

    /**
     * 根据worldIds和type获取Dispatch的ID串
     */
    public String getDispatchIds(HttpSession session, String worldIds, int type, Map<Integer, List<Integer>> openDispatchIdMap, Map<WorldServer, List<DispatchServer>> batchWorldAndDispatchMap) {
        session.removeAttribute(Global.BATCH_DISPATCH_NUM + type);
        StringBuilder dispatchIds = new StringBuilder();
        String[] worldIdArray = worldIds.split(",");
        List<DispatchServer> dispatchServerList = null;
        List<Integer> dispatchIdList = null;// 開啟時為dispatchServerId，關閉時為id
        // 界面显示需要
        List<DispatchServer> batchDispatchServerList = null;
        int batchDispatchNum = 0;
        for (int i = 0; i < worldIdArray.length; i++) {
            int worldId = Integer.valueOf(worldIdArray[i]);
            batchDispatchServerList = new ArrayList<DispatchServer>();
            batchWorldAndDispatchMap.put(CacheService.getWorldServerById(worldId), batchDispatchServerList);// world為key，dispatch列表為value
            if (!openDispatchIdMap.containsKey(worldId)) {
                dispatchIdList = new ArrayList<Integer>();
                openDispatchIdMap.put(worldId, dispatchIdList);
            } else {
                dispatchIdList = openDispatchIdMap.get(worldId);
            }
            dispatchServerList = CacheService.getDispatchServerListByWorldId(worldId);// 根據worldId獲取dispatch列表
            for (DispatchServer dispatchServer : dispatchServerList) {
                if (dispatchServer.getIsDeploy() == CommanConstant.STATE_STOP) {// 未部署
                    continue;
                }
                if (dispatchServer.getIsUse() == CommanConstant.STATE_STOP) {// 未启用
                    continue;
                }
                if (dispatchServer.getState() == type) {// 1为开启，0为关闭
                    dispatchIds.append(dispatchServer.getId() + ",");
                    if (CommanConstant.STATE_STOP == type) {// 开启操作需要获取已关闭的id
                        dispatchIdList.add(dispatchServer.getId());
                    } else if (CommanConstant.STATE_START == type) {// 关闭操作需要获取已开启dispatch的dispatchServerId
                        dispatchIdList.add(dispatchServer.getDispatchServerId());
                    }
                    batchDispatchServerList.add(dispatchServer);
                    batchDispatchNum++;
                }
            }
        }
        session.setAttribute(Global.BATCH_DISPATCH_NUM + type, batchDispatchNum);// 将batchDispatchNum存进session供界面的ajax使用；type：1为开启，0为关闭
        if (!StringUtils.isEmpty(dispatchIds)) {
            return dispatchIds.toString().substring(0, dispatchIds.lastIndexOf(","));
        } else {
            return "";
        }
    }

    /**
     * 批量开启World
     */
    public void batchStartWorld(String worldIds, HttpSession session, Map<Integer, List<Integer>> openDispatchIdMap) throws Exception {
        Application.getBean(WorldServerService.class).batchStartWorld(worldIds, session, openDispatchIdMap);
        int i = 0;
        while (true) {
            Thread.sleep(3000);
            if (session.getAttribute("batchWorldNum") != null) {
                int batchWorldNum = (Integer) session.getAttribute("batchWorldNum");
                if (batchWorldNum == worldIds.split(",").length) {
                    break;
                }
            }
            if (i > 120) {
                break;
            }
            i++;
        }
    }

    /**
     * 批量开启Dispatch
     */
    public void batchStartDispatch(String dispatchIds, HttpSession session) throws Exception {
        int i = 0;
        Application.getBean(DispatchServerService.class).batchStartDispatch(dispatchIds, session);
        while (true) {
            Thread.sleep(3000);
            if (session.getAttribute("batchDispatchNum") != null) {
                int batchDispatchNum = (Integer) session.getAttribute("batchDispatchNum");
                if (batchDispatchNum == dispatchIds.split(",").length) {
                    break;
                }
            }
            if (i > 120) {
                break;
            }
            i++;
        }
    }

    /**
     * 批量关闭World
     */
    public void batchCloseWorld(String worldIds, HttpSession session, Map<Integer, List<Integer>> openDispatchIdMap) throws Exception {
        Application.getBean(WorldServerService.class).batchCloseWorldNew(worldIds, session, openDispatchIdMap);
    }

    /**
     * 批量关闭Dispatch
     */
    public void batchCloseDispatch(String dispatchIds, HttpSession session) throws Exception {
        Application.getBean(DispatchServerService.class).batchCloseDispatchNew(dispatchIds, session);
    }

    /**
     * 批量开启World
     */
    public void batchStartServer(String worldIds, HttpSession session, Map<Integer, List<Integer>> openDispatchIdMap) throws Exception {
        Application.getBean(WorldServerService.class).batchStartWorld(worldIds, session, openDispatchIdMap);
        int i = 0;
        while (true) {
            Thread.sleep(3000);
            if (session.getAttribute("batchWorldNum") != null) {
                int batchWorldNum = (Integer) session.getAttribute("batchWorldNum");
                if (batchWorldNum == worldIds.split(",").length) {
                    break;
                }
            }
            if (i > 120) {
                break;
            }
            i++;
        }
    }

    /**
     * 批量启动World和Dispatch
     * 
     * @author:LKJ 2014-12-17
     */
    public void batchStartWorldAndDispatch(String worldIds, HttpSession session, Map<Integer, List<Integer>> dispatchIdMap) throws Exception {
        if (!StringUtils.isEmpty(worldIds)) {
            WorldServerService worldServerService = Application.getBean(WorldServerService.class);
            String[] worldIdArray = worldIds.split(",");
            for (int i = 0; i < worldIdArray.length; i++) {
                System.out.println("worldIdArray[i]=" + worldIdArray[i]);
                Application.getManager().getSimpleThreadPool().execute(this.createStartTask(Integer.valueOf(worldIdArray[i]), session, worldServerService, dispatchIdMap));
            }
        }
    }

    /**
     * 创建新的启动任务
     */
    private Runnable createStartTask(int worldId, HttpSession session, WorldServerService worldServerService, Map<Integer, List<Integer>> dispatchIdMap) {
        return new BatchStartWorldAndDispatchThreadNew(worldId, session, dispatchIdMap, worldServerService);
    }

    // ================================================更新并重啟start=============================================
    /**
     * 更新并重啟
     */
    public void updateAndRestart(HttpSession session, Properties properties) throws Exception {
        String worldIds = properties.get("worldIds").toString();// 操作world串
        int updateType = Integer.valueOf(properties.get("updateType").toString());// 操作类型
        if (!StringUtils.isEmpty(worldIds)) {
            String sessionVoListName = properties.getProperty("sessionVoListName");// 操作服务列名称
            String sessionBeanListName = properties.getProperty("sessionBeanListName");// 操作时状态列表名称
            List<BeanInterface> beanInterfaceList = new ArrayList<BeanInterface>();
            List<BatchOpearVo> updateRestartVoList = new ArrayList<BatchOpearVo>();
            String[] ids = worldIds.split(",");
            for (int i = 0; i < ids.length; i++) {
                beanInterfaceList.add(CacheService.getWorldServerById(Integer.valueOf(ids[i])));
            }
            session.setAttribute(sessionVoListName, updateRestartVoList);
            session.setAttribute(sessionBeanListName, beanInterfaceList);
            Application.getManager().getSimpleThreadPool().execute(this.createUpdateAndRestartTask(session, updateRestartVoList, worldIds, updateType));
        }
    }

    /**
     * 创建新的更新并重啟任务
     */
    private Runnable createUpdateAndRestartTask(HttpSession session, List<BatchOpearVo> updateRestartVoList, String worldIds, int updateType) {
        return new updateAndRestartThread(session, updateRestartVoList, worldIds, updateType);
    }
    /**
     * 更新并重啟內部線程類
     */
    public class updateAndRestartThread implements Runnable {
        int                updateType;
        String             worldIds;
        HttpSession        session;
        List<BatchOpearVo> updateRestartVoList;

        public updateAndRestartThread(HttpSession session, List<BatchOpearVo> updateRestartVoList, String worldIds, int updateType) {
            this.session = session;
            this.worldIds = worldIds;
            this.updateType = updateType;
            this.updateRestartVoList = updateRestartVoList;
        }

        @Override
        public void run() {
            try {
                doUpdateAndRestart(session, updateRestartVoList, worldIds, updateType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 更新并重啟服務
     * 
     * @param session
     * @param worldIds 要更新的worldId串
     * @param updateType 更新類型（1：只更新world；2：只更新dispatch；3：更新world和dispatch；4：只重启dispatch，不更新；5：重启world和dispatch，不更新）
     * @throws Exception
     */
    public void doUpdateAndRestart(HttpSession session, List<BatchOpearVo> updateRestartVoList, String worldIds, int updateType) {
        try {
            // 设置正在操作（监控用）
            CacheService.setIsOpareting(true);
            session.removeAttribute(Global.UPDATE_RESTART_VO_LIST);
            session.removeAttribute("updateRestartError");
            session.setAttribute(Global.UPDATE_RESTART_VO_LIST, updateRestartVoList);
            String[] worldIdArray = worldIds.split(",");
            WorldServerService worldServerService = Application.getBean(WorldServerService.class);
            DispatchServerService dispatchServerService = Application.getBean(DispatchServerService.class);
            ServerService serverService = Application.getBean(ServerService.class);
            // 更新前获取更新名称列表和本地文件的MD5值
            List<String> worldUpdateFileList = null;
            Properties worldFilesMD5 = null;
            List<String> dispatchUpdateFileList = null;
            Properties dispatchFilesMD5 = null;
            if (updateType != CommanConstant.RESTARY_DISPATCH && updateType != CommanConstant.RESTARY_WORLD_DISPATCH) {
                if (updateType != CommanConstant.UPDATE_RESTARY_DISPATCH) {// updateType為2時，只操作dispatch，不用操作world
                    worldUpdateFileList = CommonUtil.getFileNameList("WorldServer", "update");
                    session.setAttribute(Global.UPDATE_FILE_NAME_LIST, worldUpdateFileList);
                    worldFilesMD5 = SshxcuteUtils.getJarMD5(session, "WorldServer", "update");
                }
                if (updateType != CommanConstant.UPDATE_RESTARY_WORLD) {// updateType為1時，只操作world，不用操作dispatch
                    dispatchUpdateFileList = CommonUtil.getFileNameList("DispatchServer", "update");
                    session.setAttribute(Global.UPDATE_FILE_NAME_LIST, dispatchUpdateFileList);
                    dispatchFilesMD5 = SshxcuteUtils.getJarMD5(session, "DispatchServer", "update");
                }
            }
            // 开始更新重启流程
            for (int i = 0; i < worldIdArray.length; i++) {
                List<Integer> dispatchServerIds = new ArrayList<Integer>();
                int worldId = Integer.valueOf(worldIdArray[i]);
                BatchOpearVo batchOpearVo = new BatchOpearVo();
                batchOpearVo.setId(worldId);
                batchOpearVo.setRemark("准备成功");
                updateRestartVoList.add(batchOpearVo);
                WorldServer worldServer = worldServerService.get(worldId);
                if (worldServer == null && updateType != CommanConstant.UPDATE_RESTARY_DISPATCH) {
                    batchOpearVo.isFail("ID為" + worldId + "的world不存在");
                    return;
                }
                long startTime = System.currentTimeMillis();
                System.out.println("=============开始关闭dispatch");
                // 關閉
                boolean isCloseDispatch = dispatchServerService.closeDispatchInsideThread(session, batchOpearVo, worldId, dispatchServerIds, serverService, dispatchServerService);
                // 关dispatch
                if (!isCloseDispatch) {
                    return;
                }
                long l1 = System.currentTimeMillis();
                System.out.println("=============结束关闭dispatch" + " Use Time: " + (l1 - startTime));
                // updateType為2時，只操作dispatch，不用操作world
                if (updateType != CommanConstant.UPDATE_RESTARY_DISPATCH && updateType != CommanConstant.RESTARY_DISPATCH) {
                    System.out.println("=============开始关闭World");
                    long l2 = System.currentTimeMillis();
                    // 關閉
                    boolean isCloseWorld = worldServerService.closeWorld(batchOpearVo, worldServer, dispatchServerIds, serverService);
                    // 如果关world不成功返回
                    if (!isCloseWorld) {
                        return;
                    }
                    long l3 = System.currentTimeMillis();
                    System.out.println("=============结束关闭World" + " Use Time: " + (l3 - l2));
                    System.out.println("=============开始更新World");
                    if (updateType != CommanConstant.RESTARY_WORLD_DISPATCH) {
                        // 更新world
                        boolean isScpFile = this.scpUpdateFile(batchOpearVo, worldId, worldServerService, session, worldUpdateFileList, worldFilesMD5);
                        if (!isScpFile) {
                            return;
                        }
                    }
                    long l4 = System.currentTimeMillis();
                    System.out.println("=============结束更新World" + " Use Time: " + (l4 - l3));
                }
                // updateType為1時，只操作world，不用操作dispatch
                if (updateType != CommanConstant.UPDATE_RESTARY_WORLD && updateType != CommanConstant.RESTARY_DISPATCH && updateType != CommanConstant.RESTARY_WORLD_DISPATCH) {
                    System.out.println("=============开始更新Dispatch");
                    long l5 = System.currentTimeMillis();
                    // 更新dispatch
                    for (DispatchServer dispatchServer : CacheService.getDispatchServerListByWorldId(worldId)) {
                        boolean isScpFile = this.scpUpdateFile(batchOpearVo, dispatchServer.getId(), dispatchServerService, session, dispatchUpdateFileList, dispatchFilesMD5);
                        if (!isScpFile) {
                            return;
                        }
                    }
                    long l6 = System.currentTimeMillis();
                    System.out.println("=============结束更新Dispatch" + " Use Time: " + (l6 - l5));
                }
                // updateType為2時，只操作dispatch，不用操作world
                if (updateType != CommanConstant.UPDATE_RESTARY_DISPATCH && updateType != CommanConstant.RESTARY_DISPATCH) {
                    System.out.println("=============开始开启World");
                    long l7 = System.currentTimeMillis();
                    // 开启world
                    boolean isOpenWorld = worldServerService.startWorld(batchOpearVo, worldServer, session, serverService);
                    if (!isOpenWorld) {
                        return;
                    }
                    long l8 = System.currentTimeMillis();
                    System.out.println("=============结束开启World" + " Use Time: " + (l8 - l7));
                }
                System.out.println("=============开始开启dispatchServer=");
                long l9 = System.currentTimeMillis();
                boolean isOpenDispatch = dispatchServerService.startDispatchInsideThread(session, batchOpearVo, worldId, serverService);
                if (!isOpenDispatch) {
                    return;
                }
                long endTime = System.currentTimeMillis();
                System.out.println("=============结束开启dispatchServer" + " Use Time: " + (endTime - l9));
                // System.out.println("'" + worldServer.getName() + "'操作所用时间：" + (endTime - startTime) + "毫秒");
                System.out.println("'" + worldServer.getName() + "'操作所用时间：" + ((endTime - startTime) / 1000.0) + "秒");
                if (updateType != CommanConstant.RESTARY_DISPATCH && updateType != CommanConstant.RESTARY_WORLD_DISPATCH) {
                    batchOpearVo.isSUCCESS("更新并重启成功");
                } else {
                    batchOpearVo.isSUCCESS("重启成功");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CacheService.setIsOpareting(false);// 设置操作结束（监控用）
        }
    }

    /**
     * scp更新服務
     * 
     * @param batchOpearVo 操作vo
     * @param id 要更新的服務id
     * @param serviceInterface 服務對於的service
     * @param updateFileNameList 更新名称列表
     * @param properties 本地文件MD5信息
     * @param session HttpSession
     */
    public boolean scpUpdateFile(BatchOpearVo batchOpearVo, int id, ServiceInterface serviceInterface, HttpSession session, List<String> updateFileNameList, Properties properties) throws Exception {
        boolean scpResult = false;
        BeanInterface beanInterface = serviceInterface.getBean(id);
        try {
            String classSuperName = beanInterface.getClass().getSuperclass().getName();
            batchOpearVo.setRemark(beanInterface.getName() + "正在更新");
            // 本地文件MD5信息
            beanInterface.setUpdateTime(new Date());
            // SCP 操作
            scpResult = SshxcuteUtils.updateServersByScp(beanInterface, null, null, CommanConstant.STATE_UPLOAD_ALL);
            if (scpResult) {
                // 獲取被更新文件的MD5值
                scpResult = CommonUtil.compareMD5(session, beanInterface, properties, updateFileNameList);
            } else {
                batchOpearVo.isFail(beanInterface.getName() + "更新失败");
            }
            if (scpResult) {
                Method method = serviceInterface.getMethod(serviceInterface, "", "merge", classSuperName);// 获取方法
                method.invoke(serviceInterface, beanInterface);// 调用方法
                batchOpearVo.isSUCCESS(beanInterface.getName() + "更新成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            batchOpearVo.isFail(beanInterface.getName() + "更新失败");
        }
        return scpResult;
    }// ================================================更新并重啟end=============================================
}
