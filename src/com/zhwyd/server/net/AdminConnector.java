package com.zhwyd.server.net;
import java.net.InetSocketAddress;
import com.wyd.protocol.data.AbstractData;
import com.zhwyd.server.bean.User;
public class AdminConnector extends MonitorConnector {
    public AdminConnector(String id, InetSocketAddress address, boolean needSecureAuth) {
        super(id, address, needSecureAuth);
        // 设置断开不需要重新连接
        this.setNeedRetry(false);
    }

    public AdminConnector(String id, InetSocketAddress address, boolean needSecureAuth, User user) {
        super(id, address, needSecureAuth, user);
        // 设置断开不需要重新连接
        this.setNeedRetry(false);
    }

    @Override
    protected void sendSecureAuthMessage() {
    }

    @Override
    protected void processLogin(AbstractData paramAbstractData) throws Exception {
    }
}
