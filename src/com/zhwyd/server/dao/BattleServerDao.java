package com.zhwyd.server.dao;
import com.zhwyd.server.bean.BattleServer;
import com.zhwyd.server.controller.model.BattleServerModel;
import com.zhwyd.server.web.page.Pager;
public interface BattleServerDao extends DaoSupport<BattleServer> {
    /**
     * 获取战斗服列表
     */
    public Pager getBattleServerList(BattleServerModel battleServerModel);
}
