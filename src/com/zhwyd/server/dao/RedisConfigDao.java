package com.zhwyd.server.dao;
import com.zhwyd.server.bean.RedisConfig;
import com.zhwyd.server.controller.model.RedisConfigModel;
import com.zhwyd.server.web.page.Pager;
public interface RedisConfigDao extends DaoSupport<RedisConfig> {
    /**
     * 获取redis配置列表
     */
    public Pager getRedisConfigList(RedisConfigModel redisConfigModel);
}
