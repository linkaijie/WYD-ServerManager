package com.zhwyd.server.controller;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.zhwyd.server.bean.Model;
import com.zhwyd.server.bean.vo.BatchOpearVo;
import com.zhwyd.server.bean.vo.ChangeServerVo;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.controller.model.BaseModel;
import com.zhwyd.server.controller.model.WorldServerModel;
import com.zhwyd.server.service.CommonService;
import com.zhwyd.server.service.ModelService;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.service.WorldServerService;
import com.zhwyd.server.service.impl.SystemLogService;
import com.zhwyd.server.web.page.Pager;
@Controller
public class CommonController {
    @Autowired
    private CommonService      commonService;
    @Autowired
    private WorldServerService worldServerService;
    @Autowired
    private ServerService      serverService;
    @Autowired
    private ModelService       modelService;

    /**
     * 获取批量更新列表
     * @param response
     * @param session
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @RequestMapping
    public void getBatchUpdateList(HttpServletResponse response, HttpSession session) throws IOException {
        response.getWriter().print(JSONArray.fromObject((List<BatchOpearVo>) session.getAttribute(Global.BATCH_UPDATEVOLIST)).toString());
    }

    /**
     * 获取批量同步配置文件列表
     * @param response
     * @param session
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @RequestMapping
    public void getBatchUpdateRemoteCfgList(HttpServletResponse response, HttpSession session) throws IOException {
        response.getWriter().print(JSONArray.fromObject((List<BatchOpearVo>) session.getAttribute(Global.BATCH_UPDATE_REMOTE_CFG)).toString());
    }

    /**
     * 获取迁服列表
     * @param response
     * @param session
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @RequestMapping
    public void getChangeServerList(HttpServletResponse response, HttpSession session) throws IOException {
        List<ChangeServerVo> changeServerVos = (List<ChangeServerVo>) session.getAttribute(Global.CHANGE_SERVER_VO_LIST);
        response.getWriter().print(JSONArray.fromObject(changeServerVos).toString());
    }

    /**
     * 获取更新并重启列表
     * @param response
     * @param session
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @RequestMapping
    public void getUpdateRestartList(HttpServletResponse response, HttpSession session) throws IOException {
        List<ChangeServerVo> updateRestartVos = (List<ChangeServerVo>) session.getAttribute(Global.UPDATE_RESTART_VO_LIST);
        response.getWriter().print(JSONArray.fromObject(updateRestartVos).toString());
    }

    @SuppressWarnings("unchecked")
    @RequestMapping
    public void getDeployServerList(HttpServletResponse response, HttpSession session) throws IOException {
        response.getWriter().print(JSONArray.fromObject((List<BatchOpearVo>) session.getAttribute(Global.BATCH_DEPLOYVOLIST)).toString());
    }

    /**
     * 跳转
     * @param baseModel
     * @param redirectType
     * @param session
     * @return
     */
    @RequestMapping
    public ModelAndView redirectAction(BaseModel baseModel, String redirectType, HttpSession session) {
        String redirect = "";
        if (redirectType == null) {
            return null;
        }
        redirect = commonService.getRedirect(baseModel, redirectType);
        if (StringUtils.isEmpty(redirect)) {
            return null;
        }
        return new ModelAndView(redirect);
    }

    /**
     * 获取批量开启world、dispatch列表
     * @param response
     * @param session
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @RequestMapping
    public void getBatchStartWorldAndDispatchList(HttpServletResponse response, HttpSession session) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.element("worldList", JSONArray.fromObject((List<BatchOpearVo>) session.getAttribute(Global.BATCH_START_WORLD)).toString());
        jsonObject.element("dispatchList", JSONArray.fromObject((List<BatchOpearVo>) session.getAttribute(Global.BATCH_START_DISPATCH)).toString());
        response.getWriter().print(jsonObject.toString());
    }

    /**
     * 获取批量关闭world、dispatch列表
     * @param response
     * @param session
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @RequestMapping
    public void getBatchCloseWorldAndDispatchList(HttpServletResponse response, HttpSession session) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.element("worldList", JSONArray.fromObject((List<BatchOpearVo>) session.getAttribute(Global.BATCH_CLOSE_WORLD)).toString());
        jsonObject.element("dispatchList", JSONArray.fromObject((List<BatchOpearVo>) session.getAttribute(Global.BATCH_CLOSE_DISPATCH)).toString());
        response.getWriter().print(jsonObject.toString());
    }

    /**
     * 更新前获取更新名称列表
     */
    @RequestMapping
    public void getFileNameList(HttpServletResponse response, HttpSession session, String beanType, String pathType) throws Exception {
        commonService.getFileNameList(response, session, beanType, pathType);
    }

    /**
     * 更新前获取更新名称列表
     */
    @RequestMapping
    public void getUpdateFileNameList(HttpServletResponse response, HttpSession session, int updateType) throws Exception {
        commonService.getUpdateFileNameList(response, session, updateType);
    }

    /**
     * 编辑DISPATCH
     */
    @RequestMapping
    public ModelAndView changeServerInput() {
        ModelAndView modelAndView = new ModelAndView("common/changeServer");
        modelAndView.addObject("worldServer", worldServerService.getAll());
        modelAndView.addObject("server", serverService.getAll());
        return modelAndView;
    }

    /**
     * 编辑迁服配置文件
     */
    @RequestMapping
    public ModelAndView changeServerCfg(HttpServletResponse response, String worldIds, int serverId, int dispatchServerId, HttpSession session) {
        try {
            response.getWriter().print(commonService.changeServerDBCfg(worldIds, serverId, dispatchServerId, session));
            SystemLogService.worldServerLog(session, ":编辑ID为" + worldIds + "的WORLD信息");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 迁服操作
     */
    @RequestMapping
    public ModelAndView changeServer(HttpServletResponse response, String worldIds, int serverId, int dispatchServerId, HttpSession session) {
        try {
            commonService.changeServer(session, worldIds, serverId, dispatchServerId);
            SystemLogService.serverManegeLog(session, ":迁移worldId为" + worldIds + "的服务");
            response.getWriter().print("true");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取WorldServer服务列表
     */
    @RequestMapping
    public ModelAndView worldServerList(WorldServerModel worldServerModel, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        Model model = modelService.getAll().get(0);
        Date updateDate = new Date();
        if (model != null) {
            if (model.getWorldUpdateTime() != null) {
                updateDate = model.getWorldUpdateTime();
                modelAndView.addObject("updateDate", updateDate);
            }
        }
        worldServerModel.setGameId((Integer) session.getAttribute(Global.GAME_ID));
        Pager pager = worldServerService.getWorldServerList(worldServerModel);
        modelAndView.addObject("model", worldServerModel);
        modelAndView.addObject("server", serverService.getAll());
        modelAndView.addObject("pager", pager);
        SystemLogService.worldServerLog(session, ":查看WORLD服务列表");
        return modelAndView;
    }

    /**
     * 获取操作列表
     * @param response
     * @param session
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @RequestMapping
    public void getOperateList(HttpServletResponse response, HttpSession session, String sessionType) throws IOException {
        response.getWriter().print(JSONArray.fromObject((List<BatchOpearVo>) session.getAttribute(sessionType)).toString());
    }

    /**
     * 根据名称销毁session值
     * @param session
     * @param sessionName session的名称
     */
    @RequestMapping
    public void destroySessionByName(HttpSession session, String sessionName) {
        System.out.println("sessionName="+sessionName);
        String[] sessionNames = sessionName.split(",");
        for(String name : sessionNames ){
            System.out.println("destroy="+session.getAttribute(name));
            session.removeAttribute(name);
        }
    }
}
