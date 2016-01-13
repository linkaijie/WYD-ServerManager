package com.zhwyd.server.dao.impl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.zhwyd.server.bean.IpdServer;
import com.zhwyd.server.controller.model.IpdServerModel;
import com.zhwyd.server.dao.IpdServerDao;
import com.zhwyd.server.web.page.Pager;
public class IpdServerDaoImpl extends DaoSupportImpl<IpdServer> implements IpdServerDao {
    public Pager getIpdServerList(IpdServerModel ipdServerModel){
        Pager pager = ipdServerModel.getPager();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(IpdServer.class);
        if (ipdServerModel.getGameId() != null) {
            detachedCriteria.add(Restrictions.eq("gameId", ipdServerModel.getGameId()));
        }
        if (ipdServerModel.getIpdId() != null) {
            detachedCriteria.add(Restrictions.eq("id", ipdServerModel.getIpdId()));
        }
        if (ipdServerModel.getServerId() != null) {
            detachedCriteria.add(Restrictions.eq("serverId", ipdServerModel.getServerId()));
        }
        return findByPager(pager, detachedCriteria);
    }
}
