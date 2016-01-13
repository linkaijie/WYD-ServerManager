package com.zhwyd.server.service;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.bean.WorldServer;
import com.zhwyd.server.controller.model.WorldServerModel;
import com.zhwyd.server.web.page.Pager;
public interface ServerManageService extends BaseService {
    /**
     * 获取模板对象
     * 
     * @param serverManageModel
     * @return
     * @throws Exception
     */
    public Pager getServerManageModelList(WorldServerModel worldServerModel) throws Exception;

    /**
     * 检查world服务是否启动成功
     */
    public boolean startWorld(Server server, WorldServer worldServer, StringBuffer stringBuffer, HttpSession session) throws Exception;

    /**
     * 开启Dispatch服务
     */
    public void startDispatch(Server server, int worldId, StringBuffer stringBuffer, HttpSession session) throws Exception;

    /**
     * 开启服务
     */
    public void startServer(WorldServerService worldServerService, Integer worldId, HttpSession session, StringBuffer stringBuffer) throws Exception;

    /**
     * 关闭服务
     */
    public void closeServer(WorldServerService worldServerService, Integer worldId, HttpSession session, StringBuffer stringBuffer) throws Exception;

    /**
     * 根据worldIds和type获取Dispatch ID串
     */
    public String getDispatchIds(HttpSession session, String worldIds, int type, Map<Integer, List<Integer>> openDispatchIdMap, Map<WorldServer, List<DispatchServer>> batchWorldAndDispatchMap);

    /**
     * 批量开启Dispatch
     */
    public void batchStartDispatch(String dispatchIds, HttpSession session) throws Exception;

    /**
     * 批量开启World
     */
    public void batchStartWorld(String worldIds, HttpSession session, Map<Integer, List<Integer>> openDispatchIdMap) throws Exception;

    /**
     * 批量关闭World
     */
    public void batchCloseWorld(String worldIds, HttpSession session, Map<Integer, List<Integer>> openDispatchIdMap) throws Exception;

    /**
     * 批量关闭Dispatch
     */
    public void batchCloseDispatch(String dispatchIds, HttpSession session) throws Exception;
    
    /**
     * 批量启动World和Dispatch
     * @author:LKJ 2014-12-17
     */
    public void batchStartWorldAndDispatch(String worldIds, HttpSession session, Map<Integer, List<Integer>> dispatchIdMap) throws Exception;
    
    /**
     * 更新并重啟
     */
    public void updateAndRestart(HttpSession session, Properties properties) throws Exception;
}
