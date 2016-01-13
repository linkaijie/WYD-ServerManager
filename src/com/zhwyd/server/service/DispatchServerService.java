package com.zhwyd.server.service;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import com.zhwyd.server.bean.BeanInterface;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.bean.vo.BatchOpearVo;
import com.zhwyd.server.bean.vo.ChangeServerVo;
import com.zhwyd.server.controller.model.DispatchServerModel;
import com.zhwyd.server.web.page.Pager;
public interface DispatchServerService extends ServiceSupport<DispatchServer>, ServiceInterface {
    /**
     * 获取Dispatch服务列表
     * @param dispatchServerModel
     * @return
     */
    public Pager getDispatchServerList(DispatchServerModel dispatchServerModel);
    
    public List<DispatchServer> getDispatchServerListByWorldId(int worldId);

    /**
     * 查看运行日志
     */
    public Map<String, Object> showStdout(BeanInterface beanInterface, HttpSession session) throws Exception;

    /**
     * 部署Dispatch服务
     */
    public void deployServer(HttpServletResponse response, BeanInterface beanInterface, HttpSession session);

    /**
     * 更新Dispatch服务 
     */
    public void updateServer(HttpServletResponse response, BeanInterface beanInterface, HttpSession session) throws Exception;

    /**
     * 批量关闭Dispatch服务
     */
    public void batchCloseDispatch(String dispatchIds, HttpSession session) throws Exception;

    /**
     * 批量开启Dispatch服务
     */
    public void batchStartDispatch(String dispatchIds, HttpSession session) throws Exception;

    /**
     * 关闭Dispatch服务
     */
    public String killServer(BeanInterface beanInterface) throws Exception;

    /**
     * 开启Dispatch服务
     */
    public String startServer(BeanInterface beanInterface) throws Exception;

    /**
     * 获取运行结果信息 
     */
    public String getStdout(Server server, DispatchServer dispatchServer) throws Exception;

    /**
     * 根据dispatchIds获取未开启的ipd主键
     */
    public StringBuilder getIpdState(String dispatchIds);

    /**
     * 根据dispatchIds获取未开启的world主键
     */
    public StringBuilder getWorldState(String dispatchIds);
    
    /**
     * 同步配置信息
     */
    public void synchronizeConfig(String dispatchIds);
    
    public String getConfig(JSONObject jsonObject);
    
    /**
     * 根据类型获取最大排序
     * @param serverType
     * @return
     */
//    public int getMaxOrderByType(String serverType);
    
    public void batchCloseDispatchNew(String dispatchIds, HttpSession session) throws Exception;
    
    public void saveOrUpdateMore(DispatchServer dispatchServers, Integer creatrNumber, String serverTypes);
    
    public void saveOrUpdateDispatch(DispatchServer dispatchServers);
    
    public String saveOrUpdateMoreDispatch(DispatchServer dispatchServerMore, Integer creatrNumber);
    
    public String updateCfgToOtherServer(ChangeServerVo changeServerVo);
    
    /**
     * 关闭dispatch（内部多线类）
     * @param batchOpearVo          操作vo
     * @param worldId               worldID
     * @param dispatchServerIds     已開啟的dispatch服務Id
     * @param serverService TODO
     * @param dispatchServerService TODO
     */
    public boolean closeDispatchInsideThread(HttpSession session, BatchOpearVo batchOpearVo, int worldId, List<Integer> dispatchServerIds, ServerService serverService, DispatchServerService dispatchServerService) throws Exception;
    
    /**
     * 关闭dispatch(单个循环)
     * @param batchOpearVo          操作vo
     * @param worldId               worldID
     * @param dispatchServerIds     已開啟的dispatch服務Id
     */
    public boolean closeDispatch(BatchOpearVo batchOpearVo, int worldId, List<Integer> dispatchServerIds) throws Exception;
    
    /**
     * 開啟dispatch
     * @param batchOpearVo      開啟dispatch
     * @param dispatchServerId  dispatchServerId
     * @param session           HttpSession
     */
    public boolean startDispatch(BatchOpearVo batchOpearVo, Integer dispatchServerId, HttpSession session) throws Exception;
    
    /**
     * 关闭dispatch(多线程内部类)
     * @param batchOpearVo          操作vo
     * @param worldId               worldID
     * @param serverService TODO
     * @param dispatchServerIds     已開啟的dispatch服務Id
     */
    public boolean startDispatchInsideThread(HttpSession session, BatchOpearVo batchOpearVo, int worldId, ServerService serverService) throws Exception;
}
