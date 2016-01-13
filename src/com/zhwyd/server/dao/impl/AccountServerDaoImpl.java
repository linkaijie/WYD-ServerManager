package com.zhwyd.server.dao.impl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.zhwyd.server.bean.AccountServer;
import com.zhwyd.server.controller.model.AccountServerModel;
import com.zhwyd.server.dao.AccountServerDao;
import com.zhwyd.server.web.page.Pager;
public class AccountServerDaoImpl extends DaoSupportImpl<AccountServer> implements AccountServerDao {
    public Pager getAccountServerList(AccountServerModel accountServerModel) {
        Pager pager = accountServerModel.getPager();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AccountServer.class);
        if (accountServerModel.getGameId() != null) {
            detachedCriteria.add(Restrictions.eq("gameId", accountServerModel.getGameId()));
        }
        if (accountServerModel.getAccountId() != null) {
            detachedCriteria.add(Restrictions.eq("id", accountServerModel.getAccountId()));
        }
        if (accountServerModel.getServerId() != null) {
            detachedCriteria.add(Restrictions.eq("serverId", accountServerModel.getServerId()));
        }
        return findByPager(pager, detachedCriteria);
    }
}
