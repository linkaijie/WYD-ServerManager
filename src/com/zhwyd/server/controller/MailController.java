package com.zhwyd.server.controller;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.zhwyd.server.bean.Mail;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.controller.model.BaseModel;
import com.zhwyd.server.service.GameService;
import com.zhwyd.server.service.MailService;
import com.zhwyd.server.service.ServerService;
import com.zhwyd.server.service.impl.SystemLogService;
import com.zhwyd.server.web.page.Pager;
@Controller
public class MailController {
    @Autowired
    private ServerService serverService;
    @Autowired
    private MailService   mailService;
    @Autowired
    private GameService   gameService;
    private Mail          mail;

    @RequestMapping
    public ModelAndView mailList(BaseModel baseModel, HttpSession session) throws Exception {
        List<Mail> modelList = mailService.getMailList((Integer) session.getAttribute(Global.GAME_ID));
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
     * 编辑Mail
     * 
     * @author:LKJ
     * @2014-11-24
     * @param models
     * @param session
     * @return
     */
    @RequestMapping
    public ModelAndView mailInput(Mail mails, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        if (mails != null && mails.getId() != null && mails.getId() > 0) {
            mail = mailService.get(mails.getId());
            modelAndView.addObject("mail", mail);
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
    public String saveOrUpdate(Mail mails) {
        if (mails.getId() == null) {
            mail = new Mail();
        } else {
            mail = mailService.get(mails.getId());
        }
        mail.setName(mails.getName());
        mail.setMail(mails.getMail());
        mail.setStatue(mails.getStatue());
        mailService.saveOrUpdate(mail);
        CacheService.initMail();
        return "redirect:mailList.action";
    }

    /**
     * 删除 Mail
     * 
     * @return
     * @throws IOException
     */
    @RequestMapping
    public void delete(HttpServletResponse response, Mail mails, HttpSession session) throws IOException {
        if (mails.getId() != null) {
            mail = mailService.get(mails.getId());
            mailService.delete(mail);
            CacheService.initMail();
            SystemLogService.serverManegeLog(session, ":删除ID为" + mails.getId() + "的Server信息");
            response.getWriter().write("success");
        }
        // return "redirect:accountServerList.action";
    }
}
