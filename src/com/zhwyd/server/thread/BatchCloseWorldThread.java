package com.zhwyd.server.thread;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.bean.WorldServer;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.common.util.CommonUtil;
import com.zhwyd.server.service.WorldServerService;
import com.zhwyd.server.service.impl.SystemLogService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
public class BatchCloseWorldThread implements Runnable {
    private int                 worldId;
    Map<Integer, List<Integer>> dispatchIdMap;
    private WorldServerService  worldServerService = null;
    private HttpSession         session;

    public BatchCloseWorldThread(int worldId, WorldServerService worldServerService, HttpSession session, Map<Integer, List<Integer>> dispatchIdMap) {
        this.worldId = worldId;
        this.dispatchIdMap = dispatchIdMap;
        this.worldServerService = worldServerService;
        this.session = session;
    }

    @Override
    public void run() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            boolean closeState = false;
            WorldServer worldServer = worldServerService.get(worldId);
            if (worldServer == null) {
                stringBuilder.append("ID为：" + worldId + "的World不存在");
                this.updateSession(stringBuilder);
                return;
            }
            if (worldServer.getIsDeploy() == CommanConstant.STATE_STOP) {
                stringBuilder.append("ID为" + worldId + "World未部署");
                this.updateSession(stringBuilder);
                return;
            }
            stringBuilder.append("名称" + worldServer.getName() + ",ID:" + worldServer.getId() + " ");
            Server server = CacheService.getServerById(worldServer.getServerId());
            if (server == null) {
                stringBuilder.append("ID为" + worldServer.getServerId() + "的Server不存在");
                this.updateSession(stringBuilder);
                return;
            }
            List<DispatchServer> dispatchServerList = CacheService.getDispatchServerListByWorldId(worldId);
            // 检测Dispatch是否已经关闭
            for (DispatchServer dispatchServer : dispatchServerList) {
                if (dispatchServer.getIsDeploy() == CommanConstant.STATE_STOP || dispatchServer.getIsUse() == CommanConstant.STATE_STOP) {// 未部署或未启用
                    continue;
                }
                closeState = SshxcuteUtils.progressIsExistNew(server, dispatchServer.getPath(), Global.DISPATCH_PID_FILE);
                // 开启状态(如果其中有一个未关闭，则跳出循环，不予关闭该world服务)
                if (closeState) {
                    stringBuilder.append("<font color='red'>ID为：" + dispatchServer.getId() + "名称为：" + dispatchServer.getName() + " 的</font>");
                    break;
                }
            }
            // 检测Dispatch服务运行状态，true表示Dispatch在运行中
            if (closeState) {
                stringBuilder.append("<font color='red'>Dispatch关闭未完成，请关闭后再试!</font>");
                this.updateSession(stringBuilder);
                return;
            }
            if (dispatchIdMap != null) {
                // 检测Dispatch是否关闭完成
                List<Integer> openDispatchIds = dispatchIdMap.get(worldId);
                boolean dispatchCloseIsSuccess = false;
                if (openDispatchIds != null && openDispatchIds.size() > 0) {
                    dispatchCloseIsSuccess = this.dispatchCloseIsSuccess(openDispatchIds, worldServer, server, CommanConstant.STATE_STOP);
                    // 检测Dispatch服务是否关闭完成，若未完成则不关闭world服务
                    if (!dispatchCloseIsSuccess) {
                        stringBuilder.append("<font color='red'>Dispatch关闭未完成，请关闭后再试!</font>");
                        this.updateSession(stringBuilder);
                        return;
                    }
                }
            }
            Object[] killResult = SshxcuteUtils.killExec(server, worldServer.getPath());
            if (killResult != null && killResult.length > 0 && (Integer) killResult[0] != CommanConstant.RESULT_TRUE_STATE) {
                stringBuilder.append(killResult[1].toString());
                this.updateSession(stringBuilder);
                return;
            }
            Thread.sleep(3000l);
            boolean result = SshxcuteUtils.progressIsExistNew(server, worldServer.getPath(), Global.WORLD_PID_FILE);
            if (!result) {
                worldServer.setState(CommanConstant.STATE_STOP);
                worldServerService.update(worldServer);
                CacheService.getWorldServerById(worldServer.getId()).setState(CommanConstant.STATE_STOP);
                stringBuilder.append("关闭成功");
            } else {
                stringBuilder.append("<font color='red'>关闭失败</font>");
            }
            this.updateSession(stringBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stringBuilder = null;
        }
    }

    /**
     * 检测Dispatch是否关闭完成
     * 
     * @author:LKJ
     * @2015-3-17
     * @param openDispatchIds
     *            要关闭的Dispatch标识号
     * @param worldServer
     *            世界服务
     * @param server
     * @param num
     *            循环次数
     * @return 返回true时表示已开启的Dispatch服务已经关闭完成
     * @throws Exception
     */
    private boolean dispatchCloseIsSuccess(List<Integer> openDispatchIds, WorldServer worldServer, Server server, int num) throws Exception {
        if (openDispatchIds.size() == 0) {
            return false;
        }
        boolean dispatchCloseIsSuccess = false;
        boolean successResult = false;
        int number = 0;
        if (num > 0) {
            number = num;
        }
        if (number > 30) {
            return false;
        }
        Object[] stdResult = SshxcuteUtils.showStdoutLast(server, worldServer.getPath());
        for (int dispatchId : openDispatchIds) {
            if (stdResult != null && stdResult.length > 0 && stdResult[1] != null) {
                successResult = CommonUtil.Pattern(Global.DISPATCH_SUCCESS_CLOSE_RESULT.replace("{0}", String.valueOf(dispatchId)), stdResult[1].toString());
                if (!successResult) {
                    Thread.sleep(4000);
                    ++number;
                    this.dispatchCloseIsSuccess(openDispatchIds, worldServer, server, number);
                }
            } else {
                Thread.sleep(4000);
                ++number;
                this.dispatchCloseIsSuccess(openDispatchIds, worldServer, server, number);
            }
        }
        dispatchCloseIsSuccess = true;
        return dispatchCloseIsSuccess;
    }

    /**
     * 记录session数据
     */
    public void updateSession(StringBuilder stringBuilder) {
        StringBuffer batchWorld = null;
        synchronized (this.session) {
            if (session.getAttribute("batchWorld") != null) {
                batchWorld = (StringBuffer) session.getAttribute("batchWorld");
                batchWorld.append(stringBuilder + "</br>");
            } else {
                batchWorld = new StringBuffer();
                batchWorld.append(stringBuilder + "</br>");
            }
            int batchWorldNum = 1;
            if (session.getAttribute("batchWorldNum") != null) {
                batchWorldNum = (Integer) session.getAttribute("batchWorldNum") + 1;
            }
            System.out.println("batchWorldNum=" + batchWorldNum);
            session.setAttribute("batchWorldNum", batchWorldNum);
            System.out.println("batchWorld=" + batchWorld);
            session.setAttribute("batchWorld", batchWorld);
        }
        SystemLogService.batchWorldLog(session, stringBuilder.toString());
    }
}
