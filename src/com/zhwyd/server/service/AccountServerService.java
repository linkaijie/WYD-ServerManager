package com.zhwyd.server.service;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import com.zhwyd.server.bean.AccountServer;
import com.zhwyd.server.bean.BeanInterface;
import com.zhwyd.server.controller.model.AccountServerModel;
import com.zhwyd.server.web.page.Pager;
public interface AccountServerService extends ServiceSupport<AccountServer>, ServiceInterface {
    /**
     * 获取账号服务对象
     */
    public Pager getAccountServerList(AccountServerModel accountServerModel);

    /**
     * 部署ACCOUNT服务 author:LKJ 2014-11-27
     */
    public void deployServer(HttpServletResponse response, BeanInterface beanInterface, HttpSession session);

    /**
     * 更新ACCOUNT服务 author:LKJ 2014-11-27
     */
    public void updateServer(HttpServletResponse response, BeanInterface beanInterface, HttpSession session) throws Exception;

    public Map<String, Object> showStdout(BeanInterface beanInterface, HttpSession session) throws Exception;

    public void batchCloseAccount(String accountIds, HttpSession session) throws Exception;

    public void batchStartAccount(String accountIds, HttpSession session) throws Exception;

    public String startServer(BeanInterface beanInterface) throws Exception;

    public String killServer(BeanInterface beanInterface) throws Exception;

    public boolean checkIsRelate(int accountId);

    public void updateSession(String accountIds, HttpSession session) throws Exception;

    /**
     * 同步配置信息
     */
    public void synchronizeConfig(String accountIds);
    
    public String getConfig(JSONObject jsonObject);
}
