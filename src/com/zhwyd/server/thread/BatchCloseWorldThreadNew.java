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
import com.zhwyd.server.service.WorldServerService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
public class BatchCloseWorldThreadNew implements Runnable {
    private int                 worldId;
    Map<Integer, List<Integer>> dispatchIdMap;
    private WorldServerService  worldServerService = null;
    private HttpSession         session;

    public BatchCloseWorldThreadNew(int worldId, WorldServerService worldServerService, HttpSession session, Map<Integer, List<Integer>> dispatchIdMap) {
        this.worldId = worldId;
        this.dispatchIdMap = dispatchIdMap;
        this.worldServerService = worldServerService;
        this.session = session;
    }

    @Override
    public void run() {
        BatchOpearVo batchWorldVo = new BatchOpearVo();
        // 所有dispatch关闭成功时再开始执行world任务
        if (session.getAttribute("dispatchIdsNum") != null) {
            int i = 0;
            while (true) {
                try {
                    Thread.sleep(1000);
                    if (session.getAttribute("batchDispatchNum") != null) {
                        // batchDispatchNum为批量关闭dispatch的数量，在关闭dispatch时记录
                        // dispatchIdsNum为需要关闭的dispatch数量，在新建关闭线程类时记录
                        System.out.println("batchDispatchNum=" + session.getAttribute("batchDispatchNum"));
                        System.out.println("dispatchIdsNum=" + session.getAttribute("dispatchIdsNum"));
                        int batchDispatchNum = (Integer) session.getAttribute("batchDispatchNum");
                        if (batchDispatchNum == (Integer) session.getAttribute("dispatchIdsNum")) {
                            break;
                        }
                        if (i > 240) {
                            batchWorldVo.isFail("dispatch關閉未完成，等待超时");
                            CacheService.updateSession(session, batchWorldVo, Global.BATCH_CLOSE_WORLD);
                            return;
                        }
                        System.out.println("i=" + i);
                        i++;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            batchWorldVo.setId(worldId);
            WorldServer worldServer = worldServerService.get(worldId);
            if (worldServer == null) {
                batchWorldVo.isNotExist("worldServer");
                CacheService.updateSession(session, batchWorldVo, Global.BATCH_CLOSE_WORLD);
                return;
            }
            if (worldServer.getIsDeploy() == CommanConstant.STATE_STOP) {
                batchWorldVo.isFail("未部署");
                CacheService.updateSession(session, batchWorldVo, Global.BATCH_CLOSE_WORLD);
                return;
            }
            Server server = CacheService.getServerById(worldServer.getServerId());
            if (server == null) {
                batchWorldVo.isFail("ID为" + worldServer.getServerId() + "的Server不存在");
                CacheService.updateSession(session, batchWorldVo, Global.BATCH_CLOSE_WORLD);
                return;
            }
            if (dispatchIdMap != null) {
                // 检测Dispatch是否关闭完成
                List<Integer> openDispatchIds = dispatchIdMap.get(worldId);
                boolean dispatchCloseIsSuccess = false;
                if (openDispatchIds != null && openDispatchIds.size() > 0) {
                    dispatchCloseIsSuccess = worldServerService.dispatchCloseIsSuccess(openDispatchIds, worldServer, server, CommanConstant.STATE_STOP);
                    // 检测Dispatch服务是否关闭完成，若未完成则不关闭world服务
                    if (!dispatchCloseIsSuccess) {
                        batchWorldVo.isFail("分发服关闭未完成，请关闭再试");
                        CacheService.updateSession(session, batchWorldVo, Global.BATCH_CLOSE_WORLD);
                        return;
                    }
                }
            }
            Object[] killResult = SshxcuteUtils.killExec(server, worldServer.getPath());
            if (killResult != null && killResult.length > 0 && (Integer) killResult[0] != CommanConstant.RESULT_TRUE_STATE) {
                batchWorldVo.isFail("关闭出错");
                CacheService.updateSession(session, batchWorldVo, Global.BATCH_CLOSE_WORLD);
                return;
            }
            // boolean result = SshxcuteUtils.progressIsExistNew(server, worldServer.getPath(), Global.WORLD_PID_FILE);// 检测关闭是否成功，可不测
            // if (!result) {
            worldServer.setState(CommanConstant.STATE_STOP);
            worldServerService.update(worldServer);
            CacheService.getWorldServerById(worldServer.getId()).setState(CommanConstant.STATE_STOP);
            batchWorldVo.isSUCCESS("关闭成功");
            CacheService.updateSession(session, batchWorldVo, Global.BATCH_CLOSE_WORLD);
            // } else {
            // batchWorldVo.isFail("关闭出错");
            // CacheService.updateSession(session, batchWorldVo, Global.BATCH_CLOSE_WORLD);
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
