package com.zhwyd.server.dao.impl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.zhwyd.server.bean.RedisConfig;
import com.zhwyd.server.controller.model.RedisConfigModel;
import com.zhwyd.server.dao.RedisConfigDao;
import com.zhwyd.server.web.page.Pager;
public class RedisConfigDaoImpl extends DaoSupportImpl<RedisConfig> implements RedisConfigDao {
    /**
     * 获取redis配置列表
     */
    public Pager getRedisConfigList(RedisConfigModel redisConfigModel) {
        Pager pager = redisConfigModel.getPager();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RedisConfig.class);
        if (redisConfigModel.getGameId() != null) {
            detachedCriteria.add(Restrictions.eq("gameId", redisConfigModel.getGameId()));
        }
        if (redisConfigModel.getRedisConfigId() != null) {
            detachedCriteria.add(Restrictions.eq("id", redisConfigModel.getRedisConfigId()));
        }
        if (redisConfigModel.getServerId() != null) {
            detachedCriteria.add(Restrictions.eq("serverId", redisConfigModel.getServerId()));
        }
        if (redisConfigModel.getAreaId() != null) {
            detachedCriteria.add(Restrictions.eq("areaId", redisConfigModel.getAreaId()));
        }
        return findByPager(pager, detachedCriteria);
    }
}
