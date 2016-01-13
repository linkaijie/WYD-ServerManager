package com.zhwyd.server.service.impl;
import java.util.List;
import com.zhwyd.server.bean.Area;
import com.zhwyd.server.bean.Game;
import com.zhwyd.server.dao.GameDao;
import com.zhwyd.server.service.GameService;
public class GameServiceImpl extends ServiceSupportImpl<Game> implements GameService {
    protected GameDao gameDao;

    public void setGameDao(GameDao gameDao) {
        super.setDaoSupport(gameDao);
        this.gameDao = gameDao;
    }

    /**
     * 获游戏列表
     * author:LKJ 
     * 2014-9-4 
     * @param gameId
     * @return
     */
    public List<Game> getGameList(Integer gameId) {
        List<Game> gameList = gameDao.getGameList(gameId);
        return gameList;
    }

    /**
     * 获取地区列表
     */
    public List<Area> getAreaList() {
        List<Area> areaList = gameDao.getAreaList();
        return areaList;
    }
}
