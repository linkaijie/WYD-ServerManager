package com.zhwyd.server.dao;
import java.util.List;
import com.zhwyd.server.bean.Area;
import com.zhwyd.server.bean.Game;
public interface GameDao extends DaoSupport<Game> {
    /**
     * 获取游戏对象
     * author:LKJ 
     * 2014-9-4 
     * @param gameId
     * @return
     */
    public List<Game> getGameList(Integer gameId);

    /**
     * 获取地区列表
     * @return
     */
    public List<Area> getAreaList();
}
