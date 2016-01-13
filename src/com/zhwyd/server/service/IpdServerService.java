package com.zhwyd.server.service;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.BeanInterface;
import com.zhwyd.server.bean.IpdServer;
import com.zhwyd.server.controller.model.IpdServerModel;
import com.zhwyd.server.web.page.Pager;
public interface IpdServerService extends ServiceSupport<IpdServer>, ServiceInterface {
    /**
     * 获取Ipd服务列表
     * @param ipdServerModel
     * @return
     */
    public Pager getIpdServerList(IpdServerModel ipdServerModel);

    /**
     * 查看运行日志 author:LKJ 2014-9-6
     * 
     * @param serverId
     * @throws Exception
     */
    public Map<String, Object> showStdout(BeanInterface beanInterface, HttpSession session) throws Exception;

    /**
     * 部署IPD服务 author:LKJ 2014-9-25
     * 
     * @param response
     * @param session TODO
     * @param accountServerModel
     * @return
     * @throws Exception
     */
    public void deployServer(HttpServletResponse response, BeanInterface beanInterface, HttpSession session);

    /**
     * 部署IPD服务 author:LKJ 2014-9-25
     * 
     * @param response
     * @param accountServerModel
     * @return
     * @throws Exception
     */
    public void updateServer(HttpServletResponse response, BeanInterface beanInterface, HttpSession session) throws Exception;

    /**
     * 批量关闭Ipd author:LKJ 2014-12-17
     * 
     * @param accountIds
     * @return
     * @throws Exception
     */
    public void batchCloseIpd(String ipdIds, HttpSession session) throws Exception;

    /**
     * 批量关闭Ipd author:LKJ 2014-12-17
     * 
     * @param accountIds
     * @return
     * @throws Exception
     */
    public void batchStartIpd(String ipdIds, HttpSession session) throws Exception;

    public String startServer(BeanInterface beanInterface) throws Exception;

    public String killServer(BeanInterface beanInterface) throws Exception;
    
    /**
     * 检测是否有关联
     */
    public boolean checkIsRelate(int ipdId) ;
    
    /**
     * 修改session数据
     */
    public void updateSession(String ipdIds, HttpSession session) throws Exception;
}
