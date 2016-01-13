package com.zhwyd.server.thread;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.bean.WorldServer;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.service.DispatchServerService;
import com.zhwyd.server.service.WorldServerService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
public class BatchStartWorldAndDispatchThread implements Runnable {
    private int                 worldId;
    Map<Integer, List<Integer>> dispatchIdMap;
    private WorldServerService  worldServerService = null;
    private HttpSession         session;

    public BatchStartWorldAndDispatchThread(int accountId, HttpSession session, Map<Integer, List<Integer>> dispatchIdMap) {
        this.worldId = accountId;
        this.dispatchIdMap = dispatchIdMap;
        this.worldServerService = Application.getBean(WorldServerService.class);
        this.session = session;
    }

    @Override
    public void run() {
        try {
            WorldServer worldServer = worldServerService.get(worldId);
            StringBuilder stringBuilder = new StringBuilder();
            if (worldServer == null) {
                stringBuilder.append("<font color='red'>ID为：" + worldId + "的World不存在</font>");
                CacheService.updateSession(session, Global.BATCH_START_WORLD, stringBuilder);
                return;
            }
            stringBuilder.append("名称为" + worldServer.getName() + ",编号为: " + worldServer.getAreaid() + " ");
            if (worldServer.getIsDeploy() == CommanConstant.STATE_STOP) {
                stringBuilder.append("<font color='red'>World未部署</font>");
                CacheService.updateSession(session, Global.BATCH_START_WORLD, stringBuilder);
                return;
            }
            Server server = CacheService.getServerById(worldServer.getServerId());
            if (server == null) {
                stringBuilder.append("ID为" + worldServer.getServerId() + "的Server不存在");
                CacheService.updateSession(session, Global.BATCH_START_WORLD, stringBuilder);
                return;
            }
            // 开启World
            boolean startWorldResult = this.startWorld(server, worldServer, stringBuilder);
            // 先清空Dispatch的session信息(批量才需要，循环不需要)
            if (!startWorldResult) {
                CacheService.updateSession(session, Global.BATCH_START_WORLD, stringBuilder);
                return;
            }
            CacheService.updateFirstSession(session, Global.BATCH_START_WORLD, stringBuilder);
            System.out.println("<font color='green'>world启动成功</font>");
            // this.startDispatch(CacheService.getDispatchServerListByWorldId(worldId));// 顺序开启
            List<DispatchServer> dispatchServerList = CacheService.getDispatchServerListByWorldId(worldId);
            List<DispatchServer> openDispatchServerList = new ArrayList<DispatchServer>();
            if (dispatchServerList != null && dispatchServerList.size() > 0) {
                for (DispatchServer dispatchServer : dispatchServerList) {
                    if (dispatchServer.getIsDeploy() == CommanConstant.STATE_STOP) {
                        continue;
                    }
                    if (dispatchServer.getIsUse() == CommanConstant.STATE_STOP) {
                        continue;
                    }
                    openDispatchServerList.add(dispatchServer);
                }
                if (openDispatchServerList.size() > 0) {
                    this.batchStartDispatch(openDispatchServerList);// 批量开启
                }
            }
            CacheService.updateSecondSession(session, Global.BATCH_START_WORLD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启world服务
     */
    public boolean startWorld(Server server, WorldServer worldServer, StringBuilder stringBuilder) throws Exception {
        // 判断world是否已开启
        boolean result = SshxcuteUtils.progressIsExistNew(server, worldServer.getPath(), Global.WORLD_PID_FILE);
        boolean startResult = false;
        if (result) {
            stringBuilder.append("<font color='red'>World服务已启动，请关闭重新启动</font></br>");
            return startResult;
        }
        Object[] runResult = SshxcuteUtils.runExec(server, worldServer.getPath());
        // 启动服务出错，关闭服务
        if (runResult != null && runResult.length > 0 && (Integer) runResult[0] != CommanConstant.RESULT_TRUE_STATE) {
            stringBuilder.append(runResult[1].toString());
            SshxcuteUtils.killExec(server, worldServer.getPath());
            return startResult;
        }
        String successStr = Global.WORLD_SUCCESS_RESULT;
        for (int i = 0; i < 180; i++) {
            Boolean bResult = SshxcuteUtils.getBatchStdout(session, worldServer, worldServerService, successStr);
            if (bResult != null) {
                if (bResult) {
                    stringBuilder.append("<font color='green'>World服务启动成功</font></br>");
                    startResult = true;
                } else {
                    stringBuilder.append("<font color='red'>World服务启动失败</font></br>");
                    SshxcuteUtils.killExec(server, worldServer.getPath());
                }
                break;
            }
            Thread.sleep(3000l);
            if (i >= 179) {
                stringBuilder.append("<font color='red'>World服务启动超时</font></br>");
            }
        }
        return startResult;
    }

    /**
     * 循环开启Dispatch
     */
    public void startDispatch(List<DispatchServer> dispatchServerList) throws Exception {
        StringBuilder dispatchBuffer = new StringBuilder();
        Server server = null;
        for (DispatchServer dispatchServer : dispatchServerList) {
            // 当Dispatch未部署、不启用、已开启是直接跳过
            if (dispatchServer.getIsDeploy() == CommanConstant.STATE_STOP || dispatchServer.getIsUse() == CommanConstant.STATE_STOP || dispatchServer.getState() == CommanConstant.STATE_START) {
                continue;
            }
            System.out.println("dispatchId=" + dispatchServer.getId());
            dispatchBuffer.append("名称为" + dispatchServer.getName() + ",编号为: " + dispatchServer.getMachinecode() + "的");
            server = CacheService.getServerById(dispatchServer.getServerId());
            // 判断Dispatch是否已开启
            boolean result = SshxcuteUtils.progressIsExistNew(server, dispatchServer.getPath(), Global.DISPATCH_PID_FILE);
            if (result) {
                dispatchBuffer.append("<font color='red'>Dispatch服务已启动，请关闭重新启动</font></br>");
                CacheService.updateFirstSession(session, Global.BATCH_START_DISPATCH, dispatchBuffer);
                continue;
            }
            Object[] runResult = SshxcuteUtils.runExec(server, dispatchServer.getPath());
            // 启动服务出错
            if (runResult != null && runResult.length > 0 && (Integer) runResult[0] != CommanConstant.RESULT_TRUE_STATE) {
                dispatchBuffer.append(runResult[1].toString());
                SshxcuteUtils.killExec(server, dispatchServer.getPath());
                CacheService.updateFirstSession(session, Global.BATCH_START_DISPATCH, dispatchBuffer);
                continue;
            }
            String successStr = Global.DISPATCH_SUCCESS_RESULT;
            for (int i = 0; i < 180; i++) {
                Boolean bResult = SshxcuteUtils.getBatchStdout(session, dispatchServer, Application.getBean(DispatchServerService.class), successStr);
                if (bResult != null) {
                    if (bResult) {
                        dispatchBuffer.append("<font color='green'>Dispatch服务启动成功 </font></br>");
                    } else {
                        dispatchBuffer.append("<font color='red'>Dispatch服务启动失败</font></br>");
                        // 启动失败时关闭进程
                        SshxcuteUtils.killExec(server, dispatchServer.getPath());
                    }
                    break;
                }
                Thread.sleep(2000);
                if (i >= 179) {
                    dispatchBuffer.append("<font color='red'>Dispatch服务启动超时</font></br>");
                }
            }
            CacheService.updateFirstSession(session, Global.BATCH_START_DISPATCH, dispatchBuffer);
        }
    }

    /**
     * 批量开启Dispatch
     */
    public void batchStartDispatch(List<DispatchServer> dispatchServerList) throws Exception {
        if (dispatchServerList != null && dispatchServerList.size() > 0) {
            DispatchServerService dispatchServerService = Application.getBean(DispatchServerService.class);
            for (DispatchServer dispatchServer : dispatchServerList) {
                System.out.println("dispatchIdArray[i]=" + dispatchServer.getId());
                Application.getManager().getSimpleThreadPool().execute(this.createStartTask(dispatchServer.getId(), session, dispatchServerService));
            }
        }
        int i = 0;
        while (true) {
            Thread.sleep(2000);
            if (session.getAttribute("batchDispatchNum" + worldId) != null) {
                int batchDispatchNum = (Integer) session.getAttribute("batchDispatchNum" + worldId);
                if (batchDispatchNum == dispatchServerList.size()) {
                    break;
                }
            }
            if (i > 180) {
                break;
            }
            i++;
        }
    }

    /**
     * 创建新的开启任务
     */
    public Runnable createStartTask(int dispatchId, HttpSession session, DispatchServerService dispatchServerService) {
        return new BatchStartDispatchThreadV2(dispatchId, dispatchServerService, session);
    }
}