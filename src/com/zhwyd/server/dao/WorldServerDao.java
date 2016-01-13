package com.zhwyd.server.dao;
import java.util.List;
import com.zhwyd.server.bean.WorldServer;
import com.zhwyd.server.controller.model.WorldServerModel;
import com.zhwyd.server.web.page.Pager;
public interface WorldServerDao extends DaoSupport<WorldServer> {
    /**
     * 获取WorldServer列表
     * @param worldServerModel
     * @return
     */
    public Pager getWorldServerList(WorldServerModel worldServerModel);
    
    public List<WorldServer> getWorldServerListByServerId(int serverId);
    
    public List<WorldServer> getWorldServerByServerTypeAndAreaId(int serverType, int machinecode);
}
