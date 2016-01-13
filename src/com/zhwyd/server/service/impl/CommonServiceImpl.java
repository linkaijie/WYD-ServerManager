package com.zhwyd.server.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import com.zhwyd.server.bean.BeanInterface;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.bean.WorldServer;
import com.zhwyd.server.bean.vo.ChangeServerVo;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.common.util.CommonUtil;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.controller.model.BaseModel;
import com.zhwyd.server.service.CommonService;
import com.zhwyd.server.service.DispatchServerService;
import com.zhwyd.server.service.ServiceInterface;
import com.zhwyd.server.service.WorldServerService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
import com.zhwyd.server.thread.ChangeServerThread;
public class CommonServiceImpl implements CommonService {
    public String getRedirect(BaseModel baseModel, String redirectType) {
        String redirect = "";
        int pageNumber = 1;
        int pageSize = 1;
        if (baseModel != null && baseModel.getPager() != null) {
            pageNumber = baseModel.getPager().getPageNumber();
            pageSize = baseModel.getPager().getPageSize();
        }
        if (redirectType.equals("AccountServer")) {
            redirect = "redirect:/accountserver/accountServerList.action?pageNumber=" + pageNumber + "&pageSize=" + pageSize;
        } else if (redirectType.equals("IpdServer")) {
            redirect = "redirect:/ipdserver/ipdServerList.action?pageNumber=" + pageNumber + "&pageSize=" + pageSize;
        } else if (redirectType.equals("WorldServer")) {
            redirect = "redirect:/worldserver/worldServerList.action?pageNumber=" + pageNumber + "&pageSize=" + pageSize;
        } else if (redirectType.equals("DispatchServer")) {
            redirect = "redirect:/dispatchserver/dispatchServerList.action?pageNumber=" + pageNumber + "&pageSize=" + pageSize;
        } else if (redirectType.equals("batchStartServer")) {
            redirect = "redirect:/servermanage/batchStartLog.action?pageNumber=" + pageNumber + "&pageSize=" + pageSize;
        }
        return redirect;
    }

    /**
     * 更新前获取更新名称列表(單個服務更新時調用)
     */
    public List<String> getFileNameList(HttpServletResponse response, HttpSession session, String beanType, String pathType) throws Exception {
        session.removeAttribute(Global.UPDATE_FILE_NAME_LIST);
        List<String> updateFileNameList = CommonUtil.getFileNameList(beanType, pathType);
        session.setAttribute(Global.UPDATE_FILE_NAME_LIST, updateFileNameList);
        if (updateFileNameList != null && updateFileNameList.size() > 0) {
            // 返回到jsp显示
            response.getWriter().write(StringUtils.join(updateFileNameList, "\n"));
        } else {
            response.getWriter().write("notFile");
        }
        return updateFileNameList;
    }

    /**
     * 更新前获取更新名称列表(單個服務更新時調用)
     */
    public List<String> getUpdateFileNameList(HttpServletResponse response, HttpSession session, int updateType) throws Exception {
        String pathType = "update";
        String updateFileNameStr = "";
        List<String> updateFileNameList = null;
        if (updateType == CommanConstant.UPDATE_RESTARY_WORLD) {
            updateFileNameStr = this.getFileNameListStr(updateFileNameList, "WorldServer", pathType);
        } else if (updateType == CommanConstant.UPDATE_RESTARY_DISPATCH) {
            updateFileNameStr = this.getFileNameListStr(updateFileNameList, "DispatchServer", pathType);
        } else {
            String updateWorldFileNameStr = this.getFileNameListStr(updateFileNameList, "WorldServer", pathType);
            String updateDispatchFileNameStr = this.getFileNameListStr(updateFileNameList, "DispatchServer", pathType);
            updateFileNameStr = updateWorldFileNameStr +"\n"+ updateDispatchFileNameStr;
        }
        // 返回到jsp显示
        response.getWriter().write(updateFileNameStr);
        return updateFileNameList;
    }

    public String getFileNameListStr(List<String> updateFileNameList, String beanType, String pathType) throws Exception {
        String fileNameListStr = "";
        updateFileNameList = CommonUtil.getFileNameList(beanType, pathType);
        if (updateFileNameList.size() == 0) {
            updateFileNameList.add(0, "没有可更新文件，请检查");
        }
        updateFileNameList.add(0, beanType + "更新列表:");
        fileNameListStr = StringUtils.join(updateFileNameList, "\n");
        return fileNameListStr;
    }

    /**
     * 更新數據庫配置文件（只修改DB信息，不同步文件）
     */
    public String changeServerDBCfg(String ids, int serverId, int dispatchServerId, HttpSession session) throws Exception {
        String changeResult = "success";
        String[] beanIds = ids.split(",");
        session.removeAttribute(Global.BATCH_UPDATE_REMOTE_CFG);
        session.removeAttribute(Global.UPDATE_CFG_BEAN_LIST);
        List<BeanInterface> beanInterfaceList = new ArrayList<BeanInterface>();
        for (int i = 0; i < beanIds.length; i++) {
            WorldServerService worldServerService = Application.getBean(WorldServerService.class);
            DispatchServerService dispatchServerService = Application.getBean(DispatchServerService.class);
            // 修改world配置文件
            ChangeServerVo changeServerVo = new ChangeServerVo();
            changeServerVo.setId(Integer.valueOf(beanIds[i]));
            changeServerVo.setServerId(serverId);
            changeServerVo.setDispatchServerId(dispatchServerId);
            changeResult = worldServerService.updateCfgToOtherServer(changeServerVo);// 修改world信息
            // 修改world信息失敗
            if (!changeResult.equals("success")) {
                System.out.println(changeResult);
                return changeResult;
            }
            changeResult = dispatchServerService.updateCfgToOtherServer(changeServerVo);
            // 修改dispatch信息失敗
            if (!changeResult.equals("success")) {
                System.out.println("修改dispatch信息失敗");
                return changeResult;
            }
        }
        session.setAttribute(Global.UPDATE_CFG_BEAN_LIST, beanInterfaceList);
        return changeResult;
    }

    /**
     * 獲取遷服bean列表（暫時沒作用）
     */
    public Map<WorldServer, List<DispatchServer>> getChangeServerBeanList(String[] ids) {
        Map<WorldServer, List<DispatchServer>> batchWorldAndDispatchMap = new HashMap<WorldServer, List<DispatchServer>>();
        List<DispatchServer> batchDispatchServerList = null;
        List<DispatchServer> dispatchServerList = null;
        // String[] ids = beanIds.split(",");
        for (int i = 0; i < ids.length; i++) {
            int worldId = Integer.valueOf(ids[i]);
            batchDispatchServerList = new ArrayList<DispatchServer>();
            batchWorldAndDispatchMap.put(CacheService.getWorldServerById(worldId), batchDispatchServerList);// world為key，dispatch列表為value
            dispatchServerList = CacheService.getDispatchServerListByWorldId(worldId);// 根據worldId獲取dispatch列表
            for (DispatchServer dispatchServer : dispatchServerList) {
                batchDispatchServerList.add(dispatchServer);
            }
        }
        return batchWorldAndDispatchMap;
    }

    /**
     * 迁移文件、日志
     */
    public boolean scpFileToRemote(ChangeServerVo changeServerVo, HttpSession session) {
        boolean result = true;
        try {
            changeServerVo.setRemark("迁移world、dispatch中");
            int id = changeServerVo.getId();
            int serverId = changeServerVo.getServerId();
            WorldServer worldServer = CacheService.getWorldServerById(id);
            Server localServer = CacheService.getServerById(worldServer.getServerId());// 本地Server对象
            Server remoteServer = CacheService.getServerById(serverId);// 远程Server对象
            String worldPath = worldServer.getPath();// worldServer全路徑
            String worldPathBefore = worldPath.substring(0, worldPath.lastIndexOf("/"));// world文件名前面的路“/data/apps/gunsoul”
            String worldPathafter = worldPath.substring(worldPath.lastIndexOf("/"));// world文件名“worldServer_2_2”
            List<DispatchServer> dispatchServerList = CacheService.getDispatchServerListByWorldId(id);
            String[] scpPaths;
            if (dispatchServerList != null && dispatchServerList.size() > 0) {
                scpPaths = new String[dispatchServerList.size() + 1];
                for (int i = 0; i < dispatchServerList.size(); i++) {
                    scpPaths[i] = dispatchServerList.get(i).getPath();
                }
                scpPaths[dispatchServerList.size()] = worldPath;
            } else {
                scpPaths = new String[] { worldPath};
            }
            String shPath = "/data/apps/gunsoul/sh";// 脚本存放路径
            // 迁移world、dispatch
            Object[] scpWorldResult = SshxcuteUtils.sshScpMore(session, localServer, remoteServer, scpPaths, worldPathBefore, shPath);
            if (scpWorldResult != null && scpWorldResult.length > 0 && (Integer) scpWorldResult[0] != CommanConstant.RESULT_TRUE_STATE) {
                changeServerVo.isFail("迁移world出错");
                result = false;
                return result;
            }
            changeServerVo.setRemark("迁移log文件中");
            // 迁移log文件
            String localLogPath = worldPathBefore + "/logfile" + worldPathafter;
            String remoteLogPath = worldPathBefore + "/logfile";
            // scp -r /data/apps/gunsoul/logfile/worldServer developer@192.168.1.6:/data/apps/test/logfile/
            Object[] scpDispatchResult = SshxcuteUtils.sshScp(session, localServer, remoteServer, localLogPath, remoteLogPath, shPath);
            if (scpDispatchResult != null && scpDispatchResult.length > 0 && (Integer) scpDispatchResult[0] != CommanConstant.RESULT_TRUE_STATE) {
                changeServerVo.isFail("迁移dispatch出错");
                result = false;
                return result;
            } else {
                changeServerVo.setRemark("迁移成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * 更新數據庫配置文件（只修改DB信息，不同步文件,單個文件）
     */
    public boolean changeOneServerDBCfg(ChangeServerVo changeServerVo, HttpSession session) {
        boolean result = false;
        String changeResult = "success";
        try {
            changeServerVo.setRemark("修改数据库配置中");
            WorldServerService worldServerService = Application.getBean(WorldServerService.class);
            DispatchServerService dispatchServerService = Application.getBean(DispatchServerService.class);
            // 修改world配置文件
            changeResult = worldServerService.updateCfgToOtherServer(changeServerVo);// 修改world信息
            // 修改world信息失敗
            if (!changeResult.equals("success")) {
                changeServerVo.setRemark(changeResult);
                System.out.println(changeResult);
                return result;
            } else {
                changeServerVo.setRemark("worldDB配置修改成功");
            }
            changeResult = dispatchServerService.updateCfgToOtherServer(changeServerVo);
            // 修改dispatch信息失敗
            if (!changeResult.equals("success")) {
                changeServerVo.setRemark(changeResult);
                System.out.println("修改dispatchDB信息失敗");
                return result;
            } else {
                result = true;
                changeServerVo.setRemark("dispatchDB配置修改成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            changeServerVo.setRemark("修改DB配置未知錯誤");
        }
        return result;
    }

    /**
     * 同步远程配置文件
     */
    public boolean synRemoteCfg(ChangeServerVo changeServerVo, HttpSession session) throws Exception {
        changeServerVo.setRemark("同步遠程配置文件中");
        boolean result = true;
        int worldId = changeServerVo.getId();
        WorldServer worldServer = CacheService.getWorldServerById(worldId);
        List<DispatchServer> dispatchServerList = CacheService.getDispatchServerListByWorldId(worldId);
        // 同步world
        String filePath = session.getServletContext().getRealPath("cfgmodel");
        // filePath = "/data/apps/gunsoul_test/conf";// /////////////////////////改回
        String localWorldPath = filePath + "/" + Global.CONFIG_WORLD_PROPERTIES;
        // 同步world服务
        changeServerVo.setRemark("同步" + worldServer.getName() + "配置");
        result = this.scpAndSynCfg(worldServer, Application.getBean(WorldServerService.class), Global.CONFIG_WORLD_PROPERTIES, localWorldPath);
        if (!result) {
            changeServerVo.isFail("同步" + worldServer.getName() + "遠程配置文件錯誤");
            return result;
        }
        // 同步dispatch服务
        if (dispatchServerList != null) {
            for (DispatchServer dispatchServer : dispatchServerList) {
                changeServerVo.setRemark("同步" + dispatchServer.getName() + "配置");
                String localDispatchPath = filePath + "/" + Global.CONFIG_DISPATCH_PROPERTIES;
                result = this.scpAndSynCfg(dispatchServer, Application.getBean(DispatchServerService.class), Global.CONFIG_DISPATCH_PROPERTIES, localDispatchPath);
                if (!result) {
                    changeServerVo.isFail("同步" + dispatchServer.getName() + "遠程配置文件錯誤");
                    return result;
                }
            }
        }
        return result;
    }

    public boolean scpAndSynCfg(BeanInterface beanInterface, ServiceInterface serviceInterface, String fileName, String localFilePath) throws Exception {
        boolean result = false;
        // SCP 操作
        boolean scpResult = SshxcuteUtils.updateServersByScp(beanInterface, localFilePath, null, CommanConstant.STATE_UPLOAD_SINGLE);
        if (scpResult) {
            // 同步操作
            Object[] updateResult = SshxcuteUtils.updateRemoteConfig(beanInterface, serviceInterface, fileName);
            if (updateResult == null || updateResult.length <= 0 || (Integer) updateResult[0] == CommanConstant.RESULT_TRUE_STATE) {
                System.out.println("更新成功");
                result = true;
            }
        }
        return result;
    }

    /**
     * 遷服
     */
    public void changeServer(HttpSession session, String worldIds, Integer serverId, int dispatchServerId) throws Exception {
        session.removeAttribute(Global.CHANGE_SERVER_BEAN_LIST);
        String[] ids = worldIds.split(",");
        List<BeanInterface> beanInterfaceList = new ArrayList<BeanInterface>();
        for (int i = 0; i < ids.length; i++) {
            beanInterfaceList.add(CacheService.getWorldServerById(Integer.valueOf(ids[i])));
        }
        session.setAttribute(Global.CHANGE_SERVER_BEAN_LIST, beanInterfaceList);
        Application.getManager().getSimpleThreadPool().execute(this.createchangeServerTask(session, worldIds, serverId, dispatchServerId));
    }

    /**
     * 创建新的批量更新任务
     */
    private Runnable createchangeServerTask(HttpSession session, String worldIds, Integer serverId, int dispatchServerId) {
        return new ChangeServerThread(session, worldIds, serverId, dispatchServerId);
    }
}
