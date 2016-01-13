package com.zhwyd.server.dao;
import java.util.List;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.controller.model.DispatchServerModel;
import com.zhwyd.server.web.page.Pager;
public interface DispatchServerDao extends DaoSupport<DispatchServer> {
    /**
     * 获取Dispatch服务列表
     * @param dispatchServerModel
     * @return
     */
    public Pager getDispatchServerList(DispatchServerModel dispatchServerModel);
    
    public DispatchServer getDispatchServerByNum(String machinecode, String number);
    
    public List<DispatchServer> getDispatchServerListByServerId(DispatchServer dispatchServer);
    
    public List<DispatchServer> getDispatchServerByServerTypeAndAreaId(int serverType, int machinecode);
    
    /**
     * 根据serverId获取dispatch列表
     * GROUP BY machinecode ORDER BY publicport DESC；
     * 根据machinecode分组，再根据publicport倒序排序；
     * @param serverId
     * @return
     */
    public List<DispatchServer> getDispatchServerListByTopPort(int serverId);
    
    public List<DispatchServer> getDispatchServerListByWorldId(int worldId);
}
