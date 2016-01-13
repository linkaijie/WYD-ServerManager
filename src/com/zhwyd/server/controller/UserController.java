package com.zhwyd.server.controller;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.zhwyd.server.bean.ServerType;
import com.zhwyd.server.bean.UserTable;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.controller.model.UserModel;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.service.UserTableServices;
import com.zhwyd.server.web.page.Pager;
@Controller
public class UserController {
    @Autowired
    private UserTableServices userTableServices;
    private UserTable         userTable;

    @RequestMapping
    public String login(HttpServletRequest request, HttpSession session) {
        Principal loginUser = request.getUserPrincipal();
        String userName = "";
        userName = loginUser.getName();
        UserTable user = userTableServices.findByUserName(userName);
        session.setAttribute(Global.LOGIN_CURRENT_USER, user);
        session.setAttribute(Global.LOGIN_CURRENT_NAME, userName);
        this.setSession(session);
        return "redirect:/index.jsp";
    }

    /**
     * @return
     * @throws Exception
     */
    @RequestMapping
    public ModelAndView userList(UserModel userModel, HttpServletRequest request, HttpSession session) throws Exception {
        Pager pager = userModel.getPager();
        String userName = request.getParameter("userName");
        if (StringUtils.hasText(userName)) {
            request.setAttribute("userName", userName);
        }
        List<UserTable> userTableList = (List<UserTable>) userTableServices.getUserTableList(pager);
        pager.setList(userTableList);
        pager.setTotalCount(userTableList.size());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("pager", pager);
        return modelAndView;
    }

    /**
     * 跳转修改密码页
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping
    public ModelAndView userPwdForm(HttpServletRequest request, HttpSession session) throws Exception {
        UserTable userTable = this.getCurrentUser(request, session);
        request.setAttribute("id", userTable.getId());
        return new ModelAndView();
    }

    /**
     * 获取当前用户 author:LKJ 2014-11-27
     * 
     * @param request
     * @param session
     * @return
     */
    public UserTable getCurrentUser(HttpServletRequest request, HttpSession session) {
        return getCurrentLoginUser(request, session);
    }

    /**
     * 获取当前登录用户 author:LKJ 2014-11-27
     * 
     * @param request
     * @param session
     * @return
     */
    private UserTable getCurrentLoginUser(HttpServletRequest request, HttpSession session) {
        Principal loginUser = request.getUserPrincipal();
        String userName = "";
        userName = loginUser.getName();
        UserTable user = userTableServices.findByUserName(userName);
        return user;
    }

    /**
     * 用户信息编辑 author:LKJ 2014-11-27
     * 
     * @param userId
     * @return
     */
    @RequestMapping
    public ModelAndView userForm(Integer userId) {
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    /**
     * 保存用户信息 author:LKJ 2014-11-27
     * 
     * @param newUserTable
     * @return
     */
    @RequestMapping
    public String saveOrUpdate(UserTable newUserTable) {
        if (newUserTable.getId() != null && newUserTable.getId() > 0) {
            userTable = userTableServices.get(newUserTable.getId());
            userTable.setUpdateDate(new Date());
        } else {
            userTable = new UserTable();
            userTable.setCreateDate(new Date());
        }
        userTable.setNickName(newUserTable.getNickName());
        userTable.setUserName(newUserTable.getUserName());
        userTable.setPwd(DigestUtils.md5Hex(newUserTable.getPwd()));
        userTableServices.saveUser(userTable);
        return "redirect:userList.action";
    }

    /**
     * 修改密码
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping
    public String savePwd(HttpServletRequest request, HttpSession session, HttpServletResponse response, UserTable newUserTable) {
        try {
            userTable = getCurrentUser(request, session);
            String tempPwd = DigestUtils.md5Hex(newUserTable.getPwd());
            userTable.setPwd(tempPwd);
            userTableServices.update(userTable);
            response.getWriter().write("修改成功");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除用户 author:LKJ 2014-11-27
     * 
     * @param request
     * @param session
     * @param response
     * @param userId
     * @return
     */
    @RequestMapping
    public String deleteUser(HttpServletRequest request, HttpSession session, HttpServletResponse response, int userId) {
        userTable = userTableServices.getUser(userId);
        userTableServices.deleteUser(userTable);
        return "redirect:userList.action";
    }

    public void setSession(HttpSession session) {
        // 設置serverType列表
        ServerService serverService = Application.getBean(ServerService.class);
        List<ServerType> serverTypeList = serverService.getServerTypeList();
        Map<Integer, ServerType> serverTypeMap = new HashMap<Integer, ServerType>();
        for (ServerType serverType : serverTypeList) {
            serverTypeMap.put(serverType.getId(), serverType);
        }
        session.setAttribute("serverTypeList", serverTypeList);
        session.setAttribute("serverTypeMap", serverTypeMap);
    }
}
