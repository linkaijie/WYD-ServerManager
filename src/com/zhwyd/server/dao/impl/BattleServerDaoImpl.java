package com.zhwyd.server.dao.impl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.zhwyd.server.bean.BattleServer;
import com.zhwyd.server.controller.model.BattleServerModel;
import com.zhwyd.server.dao.BattleServerDao;
import com.zhwyd.server.web.page.Pager;
public class BattleServerDaoImpl extends DaoSupportImpl<BattleServer> implements BattleServerDao {
    /**
     * 获取redis配置列表
     */
    public Pager getBattleServerList(BattleServerModel battleServerModel) {
        Pager pager = battleServerModel.getPager();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BattleServer.class);
        if (battleServerModel.getGameId() != null) {
            detachedCriteria.add(Restrictions.eq("gameId", battleServerModel.getGameId()));
        }
        if (battleServerModel.getBattleId() != null) {
            detachedCriteria.add(Restrictions.eq("id", battleServerModel.getBattleId()));
        }
        if (battleServerModel.getServerId() != null) {
            detachedCriteria.add(Restrictions.eq("serverId", battleServerModel.getServerId()));
        }
        return findByPager(pager, detachedCriteria);
    }
}
