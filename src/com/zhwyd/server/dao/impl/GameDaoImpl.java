package com.zhwyd.server.dao.impl;
import java.util.List;
import java.util.Vector;
import com.zhwyd.server.bean.Area;
import com.zhwyd.server.bean.Game;
import com.zhwyd.server.dao.GameDao;
public class GameDaoImpl extends DaoSupportImpl<Game> implements GameDao {
    /**
     * 获取游戏对象
     * author:LKJ 
     * 2014-9-4 
     * @param gameId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Game> getGameList(Integer gameId) {
        StringBuffer hql = new StringBuffer();
        List<Object> values = new Vector<Object>();
        hql.append(" From Game ");
        hql.append(" Where 1 = 1 ");
        // hql.append(" AND gameId = ? ");
        // values.add(gameId);
        this.getList(hql.toString(), values.toArray());
        return (List<Game>) this.getList(hql.toString(), values.toArray());
    }

    /**
     * 获取地区列表
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Area> getAreaList() {
        StringBuffer hql = new StringBuffer();
        List<Object> values = new Vector<Object>();
        hql.append(" From Area ");
        hql.append(" Where 1 = 1 ");
        this.getList(hql.toString(), values.toArray());
        return (List<Area>) this.getList(hql.toString(), values.toArray());
    }
}
