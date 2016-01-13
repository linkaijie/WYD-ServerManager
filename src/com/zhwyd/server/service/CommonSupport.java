package com.zhwyd.server.service;
import javax.servlet.http.HttpSession;
public interface CommonSupport {
    /**
     * 通关SCP更新服务
     */
    public void updateServerByScp(HttpSession session, String ids, ServiceInterface serviceInterface) throws Exception;

    /**
     * 更新远程配置文件
     */
    public void updateRemoteCfg(String ids, String fileName, ServiceInterface serviceInterface, HttpSession session) throws Exception;

    /**
     * 通关SCP部署服务
     */
    public void deployServerByScp(HttpSession session, String ids, ServiceInterface serviceInterface) throws Exception;
}
