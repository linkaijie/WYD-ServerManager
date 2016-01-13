package com.zhwyd.server.dao;
import com.zhwyd.server.bean.AccountServer;
import com.zhwyd.server.controller.model.AccountServerModel;
import com.zhwyd.server.web.page.Pager;
public interface AccountServerDao extends DaoSupport<AccountServer> {
    /**
     * 获取账号服务对象
     * @param accountServerModel
     * @return
     */
    public Pager getAccountServerList(AccountServerModel accountServerModel);
}
