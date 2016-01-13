package com.zhwyd.server.service;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import com.zhwyd.server.bean.BeanInterface;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.bean.WorldServer;
import com.zhwyd.server.bean.vo.BatchOpearVo;
import com.zhwyd.server.bean.vo.ChangeServerVo;
import com.zhwyd.server.controller.model.WorldServerModel;
import com.zhwyd.server.web.page.Pager;
public interface WorldServerService extends ServiceSupport<WorldServer>, ServiceInterface {
    /**
     * 获取WorldServer列表
     * 
     * @param worldServerModel
     * @return
     */
    public Pager getWorldServerList(WorldServerModel worldServerModel);

    /**
     * 更新或保存WorldServer
     * @param worldServers
     */
    public void saveOrUpdateWorld(WorldServer worldServers);

    /**
     * 查看运行日志 author:LKJ 2014-9-6
     * 
     * @param serverId
     * @throws Exception
     */
    public Map<String, Object> showStdout(BeanInterface beanInterface, HttpSession session) throws Exception;

    /**
     * 部署World服务 author:LKJ 2014-9-25
     * 
     * @param response
     * @param worldServerModel
     * @return
     * @throws Exception
     */
    public void deployServer(HttpServletResponse response, BeanInterface beanInterface, HttpSession session);

    /**
     * 更新World服务 author:LKJ 2014-9-25
     * 
     * @param response
     * @param worldServerModel
     * @return
     * @throws Exception
     */
    public void updateServer(HttpServletResponse response, BeanInterface beanInterface, HttpSession session) throws Exception;

    public void batchCloseWorld(String worldIds, HttpSession session, Map<Integer, List<Integer>> openDispatchIdMap) throws Exception;

    public void batchStartWorld(String worldIds, HttpSession session, Map<Integer, List<Integer>> openDispatchIdMap) throws Exception;

    public String killServer(BeanInterface beanInterface) throws Exception;

    /**
     * 开启world服务
     */
    public String startServer(BeanInterface beanInterface) throws Exception;

    public String getStdout(Server server, WorldServer worldServer, HttpSession session) throws Exception;

    /**
     * 根据worldIds获取所对应的ACCOUNT中未开启的accountId
     */
    public StringBuilder getAccountState(String worldIds);

    /**
     * 检测是否有关联
     */
    public boolean checkIsRelate(int worldId);

    public String getConfig(JSONObject jsonObject);

    /**
     * 批量关闭World
     * 
     * @author:LKJ 2014-12-17
     * @param accountIds
     * @return
     * @throws Exception
     */
    public void batchCloseWorldNew(String worldIds, HttpSession session, Map<Integer, List<Integer>> openDispatchIdMap) throws Exception;

    /**
     * 生成worldServer对象
     * @param mergeIds TODO
     */
    public String createWorldServer(WorldServer worldServers, String mergeIds);

    /**
     * 迁移当前机子的数据到其他机子
     */
    public String updateCfgToOtherServer(ChangeServerVo changeServerVo);

    /**
     * 检测Dispatch是否关闭完成
     * 
     * @author:LKJ
     * @2015-3-17
     * @param openDispatchIds   要关闭的Dispatch标识号
     * @param worldServer       世界服务
     * @param server
     * @param num               循环次数
     * @return 返回true时表示已开启的Dispatch服务已经关闭完成
     * @throws Exception
     */
    public boolean dispatchCloseIsSuccess(List<Integer> openDispatchIds, WorldServer worldServer, Server server, int num) throws Exception;

    /**
     * 開啟world服務
     * @param batchOpearVo  操作vo
     * @param worldServer   world服務
     * @param session       HttpSession
     * @param serverService TODO
     */
    public boolean startWorld(BatchOpearVo batchOpearVo, WorldServer worldServer, HttpSession session, ServerService serverService) throws Exception;

    /**
     * 關閉world
     * @param batchOpearVo          操作vo
     * @param dispatchServerIds     已開啟的dispatch服務Id
     * @param serverService TODO
     * @param worldId               worldID
     */
    public boolean closeWorld(BatchOpearVo batchOpearVo, WorldServer worldServer, List<Integer> dispatchServerIds, ServerService serverService) throws Exception;

    public void synVersion(HttpSession session, String ids, String fileName);
}
