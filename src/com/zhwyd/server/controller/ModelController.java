package com.zhwyd.server.controller;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.zhwyd.server.bean.Model;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.controller.model.BaseModel;
import com.zhwyd.server.service.GameService;
import com.zhwyd.server.service.ModelService;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.service.impl.SystemLogService;
import com.zhwyd.server.web.page.Pager;
@Controller
public class ModelController {
    @Autowired
    private ServerService serverService;
    @Autowired
    private ModelService  modelService;
    @Autowired
    private GameService   gameService;
    private Model         model;

    @RequestMapping
    public ModelAndView modelList(BaseModel baseModel, HttpSession session) throws Exception {
        List<Model> modelList = modelService.getModelList((Integer) session.getAttribute(Global.GAME_ID));
        // System.out.println("size=" + modelList.size());
        Pager pager = baseModel.getPager();
        pager.setList(modelList);
        pager.setTotalCount(modelList.size());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("modelList", modelList);
        modelAndView.addObject("pager", pager);
        return modelAndView;
    }

    /**
     * 编辑Model
     * 
     * @author:LKJ
     * @2014-11-24
     * @param models
     * @param session
     * @return
     */
    @RequestMapping
    public ModelAndView modelInput(Model models, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        if (models != null && models.getId() != null && models.getId() > 0) {
            model = modelService.get(models.getId());
            modelAndView.addObject("model", model);
        }
        modelAndView.addObject("server", serverService.getAll());
        modelAndView.addObject("game", gameService.getAll());
        return modelAndView;
    }

    /**
     * 更新Model信息
     * 
     * @param models
     * @return
     */
    @RequestMapping
    public String saveOrUpdate(Model models) {
        if (models.getId() == null) {
            model = new Model();
            model.setCreateTime(new Date());
        } else {
            model = modelService.get(models.getId());
            model.setUpdateTime(new Date());
        }
        model.setName(models.getName());
        model.setGameId(models.getGameId());
        model.setServerId(models.getServerId());
        model.setAccountPath(models.getAccountPath());
        model.setIpdmainPath(models.getIpdmainPath());
        model.setWorldPath(models.getWorldPath());
        model.setDispatchPatch(models.getDispatchPatch());
        modelService.saveOrUpdate(model);
        CacheService.initModel();
        return "redirect:modelList.action";
    }

    /**
     * 更新Model信息
     * 
     * @param models
     * @return
     * @throws IOException
     */
    @RequestMapping
    public String UpdateModelDate(HttpServletResponse response, String updateModeTime, String updateType) throws Exception {
        if (updateType != null) {
            model = modelService.getAll().get(0);
            model.setUpdateTime(new Date());
            Date date = com.zhwyd.server.common.util.DateUtil.getDateByTime(Long.valueOf(updateModeTime));
            if (updateType.equals("world")) {
                model.setWorldUpdateTime(date);
            } else if (updateType.equals("dispatch")) {
                model.setDispatchUpdateTime(date);
            } else if (updateType.equals("ipd")) {
                model.setIpdUpdateTime(date);
            } else if (updateType.equals("account")) {
                model.setAccountUpdateTime(date);
            }
            modelService.saveOrUpdate(model);
            CacheService.initModel();
            response.getWriter().write("true");
        }
        return null;
    }

    /**
     * 删除SERVER
     * 
     * @return
     * @throws IOException
     */
    @RequestMapping
    public void delete(HttpServletResponse response, Model models, HttpSession session) throws IOException {
        if (models.getId() != null) {
            model = modelService.get(models.getId());
            modelService.delete(models);
            CacheService.initModel();
            SystemLogService.serverManegeLog(session, ":删除ID为" + models.getId() + "的Server信息");
            response.getWriter().write("success");
        }
        // return "redirect:accountServerList.action";
    }
}
