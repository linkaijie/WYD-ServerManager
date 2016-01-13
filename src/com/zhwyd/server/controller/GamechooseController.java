package com.zhwyd.server.controller;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.controller.model.BaseModel;
@Controller
public class GamechooseController {
    
    @RequestMapping
    public ModelAndView chooseGame(BaseModel baseModel, HttpSession session) {
    	session.setAttribute(Global.GAME_ID,baseModel.getGameId());
        return new ModelAndView("redirect:/accountserver/accountServerList.action");
    }
}
