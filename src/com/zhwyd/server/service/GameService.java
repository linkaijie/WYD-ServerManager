package com.zhwyd.server.service;
import java.util.List;
import com.zhwyd.server.bean.Area;
import com.zhwyd.server.bean.Game;
public interface GameService extends ServiceSupport<Game> {
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
     */
    public List<Area> getAreaList();
}
