package com.zhwyd.server.service;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.RedisConfig;
import com.zhwyd.server.controller.model.RedisConfigModel;
import com.zhwyd.server.web.page.Pager;
public interface RedisConfigService extends ServiceSupport<RedisConfig> {
    public Pager getRedisConfigList(RedisConfigModel redisConfigModel);
    
    /**
     * 获取页面跳转类型，用于页面跳转
     * @param operateType   操作类型
     * @return              页面跳转类型
     */
    public String getReturnType(String operateType);
    
    public void operateRedis(HttpSession session, String ids, String operateType) throws Exception;
}
