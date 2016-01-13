package com.zhwyd.server.dao;
import com.zhwyd.server.bean.IpdServer;
import com.zhwyd.server.controller.model.IpdServerModel;
import com.zhwyd.server.web.page.Pager;
public interface IpdServerDao extends DaoSupport<IpdServer> {
    /**
     * 获取Ipd信息
     * @param ipdServerModel
     * @return
     */
    public Pager getIpdServerList(IpdServerModel ipdServerModel);
}
