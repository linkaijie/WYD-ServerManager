package com.zhwyd.server.dao;
import java.util.List;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.bean.ServerType;
import com.zhwyd.server.controller.model.ServerModel;
public interface ServerDao extends DaoSupport<Server> {
    /**
     * 获取服务列表
     * author:LKJ 
     * 2014-9-4 
     * @param serverMode TODO
     * @return
     */
    public List<Server> getServerList(ServerModel serverModel);

    /**
     * 获取服务類型列表
     * author:LKJ 
     * 2014-9-4 
     * @return
     */
    public List<ServerType> getServerTypeList();
}
