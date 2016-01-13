package com.zhwyd.server.thread;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.bean.WorldServer;
import com.zhwyd.server.bean.vo.BatchOpearVo;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.service.DispatchServerService;
import com.zhwyd.server.service.WorldServerService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
public class BatchStartWorldAndDispatchThreadNew implements Runnable {
    private int                 worldId;
    Map<Integer, List<Integer>> dispatchIdMap;
    private WorldServerService  worldServerService = null;
    private HttpSession         session;

    public BatchStartWorldAndDispatchThreadNew(int accountId, HttpSession session, Map<Integer, List<Integer>> dispatchIdMap, WorldServerService worldServerService) {
        this.worldId = accountId;
        this.dispatchIdMap = dispatchIdMap;
        this.worldServerService = worldServerService;
        this.session = session;
    }

    @Override
    public void run() {
        try {
            BatchOpearVo batchWorldVo = new BatchOpearVo();
            WorldServer worldServer = worldServerService.get(worldId);
            batchWorldVo.setId(worldId);
            StringBuilder stringBuilder = new StringBuilder();
            if (worldServer == null) {
                batchWorldVo.isNotExist("worldServer");
                CacheService.updateSession(session, batchWorldVo, Global.BATCH_START_WORLD);
                return;
            }
            if (worldServer.getIsDeploy() == CommanConstant.STATE_STOP) {
                batchWorldVo.isFail("未部署");
                CacheService.updateSession(session, batchWorldVo, Global.BATCH_START_WORLD);
                return;
            }
            Server server = CacheService.getServerById(worldServer.getServerId());
            if (server == null) {
                batchWorldVo.isFail("ID为" + worldServer.getServerId() + "的Server不存在");
                CacheService.updateSession(session, batchWorldVo, Global.BATCH_START_WORLD);
                return;
            }
            // 开启World
            boolean startWorldResult = this.startWorld(server, worldServer, stringBuilder);
            // 先清空Dispatch的session信息(批量才需要，循环不需要)
            if (!startWorldResult) {
                batchWorldVo.isFail("启动出错");
                CacheService.updateSession(session, batchWorldVo, Global.BATCH_START_WORLD);
                return;
            } else {
                batchWorldVo.isSUCCESS(stringBuilder.toString());
                CacheService.updateSession(session, batchWorldVo, Global.BATCH_START_WORLD);
            }
            List<Integer> dispatchIdList = dispatchIdMap.get(worldId);
            this.batchStartDispatch(dispatchIdList);// 批量开启dispatch
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
            stringBuilder.append("服务已启动，请关闭重启");
            return startResult;
        }
        Object[] runResult = SshxcuteUtils.runExec(server, worldServer.getPath());
        // 启动服务出错，关闭服务
        if (runResult != null && runResult.length > 0 && (Integer) runResult[0] != CommanConstant.RESULT_TRUE_STATE) {
            stringBuilder.append("启动出错");
            SshxcuteUtils.killExec(server, worldServer.getPath());
            return startResult;
        }
        for (int i = 0; i < 300; i++) {
            Boolean bResult = SshxcuteUtils.getBatchStdout(session, worldServer, worldServerService, Global.WORLD_SUCCESS_RESULT);
            if (bResult != null) {
                if (bResult) {
                    stringBuilder.append("启动成功");
                    startResult = true;
                } else {
                    stringBuilder.append("启动失败");
                    SshxcuteUtils.killExec(server, worldServer.getPath());
                }
                break;
            }
            Thread.sleep(1000l);
            if (i >= 299) {
                stringBuilder.append("启动超时");
            }
        }
        return startResult;
    }

    /**
     * 批量开启Dispatch
     */
    public void batchStartDispatch(List<Integer> dispatchIdList) throws Exception {
        if (dispatchIdList != null && dispatchIdList.size() > 0) {
            DispatchServerService dispatchServerService = Application.getBean(DispatchServerService.class);
            for (int id : dispatchIdList) {
                Application.getManager().getSimpleThreadPool().execute(this.createStartTask(id, session, dispatchServerService));
            }
        }
    }

    /**
     * 创建新的开启任务
     */
    public Runnable createStartTask(int dispatchId, HttpSession session, DispatchServerService dispatchServerService) {
        return new BatchStartDispatchThreadNew(dispatchId, dispatchServerService, session);
    }
}