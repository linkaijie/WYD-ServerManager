package com.zhwyd.server.handler.account;
import com.wyd.protocol.data.AbstractData;
import com.wyd.protocol.handler.IDataHandler;
import com.zhwyd.server.bean.User;
/**
 * 服务监控数据返回Handler
 * 
 * @author LKJ
 */
public class IMonitorDataHandler implements IDataHandler {
    @Override
    public void handle(AbstractData paramAbstractData) throws Exception {
    }

    public void handle(AbstractData paramAbstractData, User user) throws Exception {
    }
    
    public void handle(AbstractData paramAbstractData, String sessionId) throws Exception {
    }
}
