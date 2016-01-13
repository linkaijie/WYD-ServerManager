package com.zhwyd.server.dao.impl;
import java.util.List;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.bean.ServerType;
import com.zhwyd.server.controller.model.ServerModel;
import com.zhwyd.server.dao.ServerDao;
public class ServerDaoImpl extends DaoSupportImpl<Server> implements ServerDao {
    /**
     * 获取服务列表
     * author:LKJ 
     * 2014-9-4 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Server> getServerList(ServerModel serverModel) {
        StringBuffer hql = new StringBuffer();
        hql.append(" From Server ");
        hql.append(" Where 1 = 1 ");
        if (serverModel != null) {
            if (serverModel.getServerId() != null && serverModel.getServerId() > 0) {
                hql.append(" AND id = " + serverModel.getServerId());
            }
            if (serverModel.getUseType() != null && serverModel.getUseType() > 0) {
                hql.append(" AND useType = " + serverModel.getUseType());
            }
        }
        return (List<Server>) this.getList(hql.toString(), new Object[] {});
    }

    /**
     * 获取服务類型列表
     * author:LKJ 
     * 2014-9-4 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ServerType> getServerTypeList() {
        StringBuffer hql = new StringBuffer();
        hql.append(" From ServerType ");
        hql.append(" Where 1 = 1 ");
        return (List<ServerType>) this.getList(hql.toString(), new Object[] {});
    }
}
