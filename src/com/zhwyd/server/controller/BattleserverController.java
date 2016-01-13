package com.zhwyd.server.controller;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.zhwyd.server.bean.BattleServer;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.controller.model.BattleServerModel;
import com.zhwyd.server.service.BattleServerService;
import com.zhwyd.server.service.ModelService;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.service.impl.SystemLogService;
import com.zhwyd.server.web.page.Pager;
@Controller
public class BattleserverController {
    @Autowired
    private ServerService       serverService;
    @Autowired
    private ModelService        modelService;
    @Autowired
    private BattleServerService battleServerService;
    private BattleServer        battleServer;

    /**
     * 获取对战服信息列表
     * @param battleServerModel
     * @param session
     */
    @RequestMapping
    public ModelAndView battleServerList(BattleServerModel battleServerModel, HttpSession session) throws Exception {
        Pager pager = battleServerService.getBattleServerList(battleServerModel);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("model", battleServerModel);
        modelAndView.addObject("server", serverService.getAll());
        modelAndView.addObject("serverMap", CacheService.getServerMap());
        modelAndView.addObject("areaMap", CacheService.getAreaMap());
        modelAndView.addObject("pager", pager);
        return modelAndView;
    }

    /**
     * 编辑battleServer
     * 
     * @param battleServerModel
     * @param session
     */
    @RequestMapping
    public ModelAndView battleServerInput(BattleServerModel battleServerModel, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        if (battleServerModel != null && battleServerModel.getBattleId() != null && battleServerModel.getBattleId() > 0) {
            battleServer = battleServerService.get(battleServerModel.getBattleId());
        } else {
            battleServer = new BattleServer();
        }
        modelAndView.addObject("battleServer", battleServer);
        modelAndView.addObject("model", modelService.getAll());
        modelAndView.addObject("server", serverService.getAll());
        modelAndView.addObject("serverMap", CacheService.getServerMap());
        modelAndView.addObject("areaList", CacheService.getAreaList());
        modelAndView.addObject("game", CacheService.getGameList());
        return modelAndView;
    }

    /**
     * 更新battleServer信息
     * 
     * @param battleServers
     */
    @RequestMapping
    public String saveOrUpdate(BattleServer battleServers) throws Exception {
        BattleServer battleServer = null;
        if (battleServers.getId() == null) {
            battleServer = new BattleServer();
        } else {
            battleServer = battleServerService.get(battleServers.getId());
        }
        BeanUtils.copyProperties(battleServer, battleServers);// 将后面的复制到前面
        battleServer.setUpdateTime(new Date());
        battleServerService.saveOrUpdate(battleServer);
        CacheService.initRedisConfig();
        return "redirect:battleServerList.action";
    }

    /**
     * 删除 BattleServer
     */
    @RequestMapping
    public void delete(HttpServletResponse response, BattleServer battleServer, HttpSession session) throws IOException {
        if (battleServer.getId() != null) {
            battleServer = battleServerService.get(battleServer.getId());
            battleServerService.delete(battleServer);
            CacheService.initRedisConfig();
            SystemLogService.redisLog(session, ":删除ID为" + battleServer.getId() + "的battleServer信息");
            response.getWriter().write("success");
        }
    }

    /**
     * 操作BattleServer服务
     */
    @RequestMapping
    public ModelAndView operateBattle(HttpServletResponse response, HttpSession session, String ids, String operateType, String sessionVoListName, String sessionBeanListName) throws Exception {
        ModelAndView modelAndView = new ModelAndView(battleServerService.getReturnType(operateType));
        if (!StringUtils.isEmpty(ids)) {
            Properties properties = new Properties();
            long currentTime = System.currentTimeMillis() / 1000;
            String sessionVoListNameTemp = currentTime + "Vo";
            String sessionBeanListNameTemp = currentTime + "Bean";
            properties.put("sessionVoListName", sessionVoListNameTemp);
            properties.put("sessionBeanListName", sessionBeanListNameTemp);
            properties.put("ids", ids);
            properties.put("operateType", operateType);
            battleServerService.operateBattle(session, properties);
            response.getWriter().write(new JSONObject().accumulate("result", "true").accumulate("sessionVoListName", sessionVoListNameTemp).accumulate("sessionBeanListName", sessionBeanListNameTemp).toString());
            SystemLogService.redisLog(session, "批量" + operateType + "id为：" + ids + "battle服务");
            return null;
        }
        modelAndView.addObject("sessionVoListName", sessionVoListName);
        modelAndView.addObject("sessionBeanListName", sessionBeanListName);
        modelAndView.addObject("beanInterfaceList", session.getAttribute(sessionBeanListName));
        return modelAndView;
    }

    public BattleServer getBattleServer() {
        return battleServer;
    }

    public void setBattleServer(BattleServer battleServer) {
        this.battleServer = battleServer;
    }
}
