package com.zhwyd.server.dao.impl;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.zhwyd.server.bean.DispatchServer;
import com.zhwyd.server.controller.model.DispatchServerModel;
import com.zhwyd.server.dao.DispatchServerDao;
import com.zhwyd.server.web.page.Pager;
public class DispatchServerDaoImpl extends DaoSupportImpl<DispatchServer> implements DispatchServerDao {
    public Pager getDispatchServerList(DispatchServerModel dispatchServerModel) {
        Pager pager = dispatchServerModel.getPager();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DispatchServer.class);
        if (dispatchServerModel.getGameId() != null) {
            detachedCriteria.add(Restrictions.eq("gameId", dispatchServerModel.getGameId()));
        }
        if (dispatchServerModel.getDispatchId() != null) {
            detachedCriteria.add(Restrictions.eq("id", dispatchServerModel.getDispatchId()));
        }
        if (dispatchServerModel.getServerId() != null) {
            detachedCriteria.add(Restrictions.eq("serverId", dispatchServerModel.getServerId()));
        }
        
        if (dispatchServerModel.getEndAreaId() != null) {
            detachedCriteria.add(Restrictions.le("machinecode", dispatchServerModel.getEndAreaId()));
        }
        if (dispatchServerModel.getStartAreaId() != null) {
            if (dispatchServerModel.getEndAreaId() != null) {
                detachedCriteria.add(Restrictions.ge("machinecode", dispatchServerModel.getStartAreaId()));
            } else {
                detachedCriteria.add(Restrictions.eq("machinecode", dispatchServerModel.getStartAreaId()));
            }
        }
        
        detachedCriteria.addOrder(Order.asc("orders"));
        return findByPager(pager, detachedCriteria);
    }
    
    public DispatchServer getDispatchServerByNum(String machinecode, String number) {
        return this.get(new String[]{"machinecode", "number"}, new String[]{machinecode, number});
    }
    
    @SuppressWarnings("unchecked")
    public List<DispatchServer> getDispatchServerListByServerId(DispatchServer dispatchServer) {
        StringBuffer hql = new StringBuffer();
        hql.append("FROM DispatchServer WHERE 1=1 ");
        hql.append(" AND serverId = ? ");
        hql.append(" order by machinecode desc, orders ");
        return (List<DispatchServer>) getList(hql, new Object[] { dispatchServer.getServerId()});
    }
    
    @SuppressWarnings("unchecked")
    public List<DispatchServer> getDispatchServerByServerTypeAndAreaId(int serverType, int machinecode) {
        StringBuffer hql = new StringBuffer();
        hql.append("FROM DispatchServer WHERE 1=1 ");
        hql.append(" AND serverType = ? ");
        hql.append(" AND machinecode = ? ");
        return (List<DispatchServer>) getList(hql, new Object[] { serverType, machinecode});
    }
    
    @SuppressWarnings("unchecked")
    public List<DispatchServer> getDispatchServerListByTopPort(int serverId) {
        StringBuffer hql = new StringBuffer();
        hql.append("FROM DispatchServer WHERE 1=1 ");
        hql.append(" AND serverId = ? ");
        hql.append(" GROUP BY machinecode ORDER BY publicport DESC ");
        return (List<DispatchServer>) getList(hql, new Object[] { serverId});
    }
    
    @SuppressWarnings("unchecked")
    public List<DispatchServer> getDispatchServerListByWorldId(int worldId) {
        StringBuffer hql = new StringBuffer();
        hql.append("FROM DispatchServer WHERE 1=1 ");
        hql.append(" AND worldId = ? ");
        return (List<DispatchServer>) getList(hql, new Object[] { worldId});
    }
}
