package com.zhwyd.server.service;
import java.util.List;
import javax.servlet.http.HttpSession;
import com.zhwyd.server.bean.Server;
import com.zhwyd.server.bean.ServerType;
import com.zhwyd.server.controller.model.ServerModel;
public interface ServerService extends ServiceSupport<Server> {
    /**
     * 获取服务列表
     * author:LKJ 
     * 2014-9-4 
     * @param serverMode TODO
     * @return
     */
    public List<Server> getServerList(ServerModel serverModel);

    /**
     * 通关SCP更新服务
     */
    public void updateServerByScp(HttpSession session, String ids, int type) throws Exception;

    /**
     * 通关SCP更新服务
     */
    public void runCommand(HttpSession session, String ids, String command) throws Exception;

    /**
     * 获取服务類型列表
     * author:LKJ 
     * 2014-9-4 
     * @return
     */
    public List<ServerType> getServerTypeList();
}
