package com.zhwyd.server.service;
import java.util.Properties;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.BattleServer;
import com.zhwyd.server.controller.model.BattleServerModel;
import com.zhwyd.server.web.page.Pager;
public interface BattleServerService extends ServiceSupport<BattleServer> {
    public Pager getBattleServerList(BattleServerModel battleServerModel);
    
    /**
     * 获取页面跳转类型，用于页面跳转
     * @param operateType   操作类型
     * @return              页面跳转类型
     */
    public String getReturnType(String operateType);
    
    /**
     * 操作battle
     * @param session
     * @param properties
     * @param battleBeanInterfaceList
     * @throws Exception
     */
    public void operateBattle(HttpSession session, Properties properties) throws Exception;
}
