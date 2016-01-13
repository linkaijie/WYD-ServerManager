package com.zhwyd.server.controller;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.zhwyd.server.bean.RedisConfig;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.controller.model.RedisConfigModel;
import com.zhwyd.server.service.RedisConfigService;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.service.impl.SystemLogService;
import com.zhwyd.server.web.page.Pager;
@Controller
public class RedisconfigController {
    @Autowired
    private ServerService      serverService;
    @Autowired
    private RedisConfigService redisConfigService;
    private RedisConfig        redisConfig;

    /**
     * 获取redis配置列表
     * @param redisConfigModel
     * @param session
     */
    @RequestMapping
    public ModelAndView redisConfigList(RedisConfigModel redisConfigModel, HttpSession session) throws Exception {
        Pager pager = redisConfigService.getRedisConfigList(redisConfigModel);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("model", redisConfigModel);
        modelAndView.addObject("server", serverService.getAll());
        modelAndView.addObject("serverMap", CacheService.getServerMap());
        modelAndView.addObject("areaMap", CacheService.getAreaMap());
        modelAndView.addObject("pager", pager);
        return modelAndView;
    }

    /**
     * 编辑redisConfig
     * 
     * @param redisConfigModel
     * @param session
     */
    @RequestMapping
    public ModelAndView redisConfigInput(RedisConfigModel redisConfigModel, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        if (redisConfigModel != null && redisConfigModel.getRedisConfigId() != null && redisConfigModel.getRedisConfigId() > 0) {
            redisConfig = redisConfigService.get(redisConfigModel.getRedisConfigId());
        } else {
            redisConfig = new RedisConfig();
        }
        modelAndView.addObject("redisConfig", redisConfig);
        modelAndView.addObject("server", serverService.getAll());
        modelAndView.addObject("serverMap", CacheService.getServerMap());
        modelAndView.addObject("areaList", CacheService.getAreaList());
        modelAndView.addObject("game", CacheService.getGameList());
        return modelAndView;
    }

    /**
     * 更新RedisConfig信息
     * 
     * @param redisConfig
     */
    @RequestMapping
    public String saveOrUpdate(RedisConfig redisConfigs) throws Exception {
        RedisConfig redisConfig = null;
        if (redisConfigs.getId() == null) {
            redisConfig = new RedisConfig();
        } else {
            redisConfig = redisConfigService.get(redisConfigs.getId());
        }
        BeanUtils.copyProperties(redisConfig, redisConfigs);// 将后面的复制到前面
        redisConfigService.saveOrUpdate(redisConfig);
        CacheService.initRedisConfig();
        return "redirect:redisConfigList.action";
    }

    /**
     * 删除 RedisConfig
     */
    @RequestMapping
    public void delete(HttpServletResponse response, RedisConfig redisConfig, HttpSession session) throws IOException {
        if (redisConfig.getId() != null) {
            redisConfig = redisConfigService.get(redisConfig.getId());
            redisConfigService.delete(redisConfig);
            CacheService.initRedisConfig();
            SystemLogService.redisLog(session, ":删除ID为" + redisConfig.getId() + "的redis信息");
            response.getWriter().write("success");
        }
    }

    /**
     * 操作redis服务
     */
    @RequestMapping
    public ModelAndView operateRedis(HttpServletResponse response, HttpSession session, String ids, String operateType) throws Exception {
        ModelAndView modelAndView = new ModelAndView(redisConfigService.getReturnType(operateType));
        if (!StringUtils.isEmpty(ids)) {
            SystemLogService.redisLog(session, "批量" + operateType + "id为：" + ids + "redis服务");
            redisConfigService.operateRedis(session, ids, operateType);
            response.getWriter().write("true");
            return null;
        }
        return modelAndView;
    }

    public RedisConfig getRedisConfig() {
        return redisConfig;
    }

    public void setRedisConfig(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }
}
