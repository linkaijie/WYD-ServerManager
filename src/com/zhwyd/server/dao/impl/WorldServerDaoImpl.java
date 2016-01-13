package com.zhwyd.server.dao.impl;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.zhwyd.server.bean.WorldServer;
import com.zhwyd.server.controller.model.WorldServerModel;
import com.zhwyd.server.dao.WorldServerDao;
import com.zhwyd.server.web.page.Pager;
public class WorldServerDaoImpl extends DaoSupportImpl<WorldServer> implements WorldServerDao {
    public Pager getWorldServerList(WorldServerModel worldServerModel) {
        Pager pager = worldServerModel.getPager();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(WorldServer.class);
        if (worldServerModel.getGameId() != null) {
            detachedCriteria.add(Restrictions.eq("gameId", worldServerModel.getGameId()));
        }
        if (worldServerModel.getWorldId() != null) {
            detachedCriteria.add(Restrictions.eq("id", worldServerModel.getWorldId()));
        }
        if (worldServerModel.getServerId() != null) {
            detachedCriteria.add(Restrictions.eq("serverId", worldServerModel.getServerId()));
        }
        if (worldServerModel.getServerType() != null) {
            detachedCriteria.add(Restrictions.eq("serverType", worldServerModel.getServerType()));
        }
        if (worldServerModel.getPublicIp() != null && worldServerModel.getPublicIp() != "") {
            detachedCriteria.add(Restrictions.eq("publicip", worldServerModel.getPublicIp()));
        }
        if (worldServerModel.getStatus() != null) {
            detachedCriteria.add(Restrictions.eq("state", worldServerModel.getStatus()));
        }
        if (!StringUtils.isEmpty(worldServerModel.getAreaIds())) {
            String[] areaIds = worldServerModel.getAreaIds().replace("，", ",").split(",");
            Integer[] areaIdsInt = new Integer[areaIds.length];
            for(int i = 0; i<areaIds.length; i++){
                areaIdsInt[i] = Integer.valueOf(areaIds[i]);
            }
            detachedCriteria.add(Restrictions.in("machinecode", areaIdsInt));
        }
        if (worldServerModel.getEndAreaId() != null) {
            detachedCriteria.add(Restrictions.le("machinecode", worldServerModel.getEndAreaId()));
        }
        if (worldServerModel.getStartAreaId() != null) {
            if (worldServerModel.getEndAreaId() != null) {
                detachedCriteria.add(Restrictions.ge("machinecode", worldServerModel.getStartAreaId()));
            } else {
                detachedCriteria.add(Restrictions.eq("machinecode", worldServerModel.getStartAreaId()));
            }
        }
        detachedCriteria.addOrder(Order.asc("orders"));
        return findByPager(pager, detachedCriteria);
    }
    
    @SuppressWarnings("unchecked")
    public List<WorldServer> getWorldServerListByServerId(int serverId) {
        StringBuffer hql = new StringBuffer();
        hql.append("FROM WorldServer WHERE 1=1 ");
        hql.append(" AND serverId = ? ");
        hql.append(" order by port desc ");
        return (List<WorldServer>) getList(hql, new Object[] { serverId});
    }
    
    @SuppressWarnings("unchecked")
    public List<WorldServer> getWorldServerByServerTypeAndAreaId(int serverType, int machinecode) {
        StringBuffer hql = new StringBuffer();
        hql.append("FROM WorldServer WHERE 1=1 ");
        hql.append(" AND serverType = ? ");
        hql.append(" AND machinecode = ? ");
        return (List<WorldServer>) getList(hql, new Object[] { serverType, machinecode});
    }
}
