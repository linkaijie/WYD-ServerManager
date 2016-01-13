package com.zhwyd.server.config;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.wyd.empire.protocol.Protocol;
import com.wyd.net.ProtocolFactory;
public class ProtocolFactoryContextListener implements ServletContextListener {
    public static int num = 0; // 判断是否需要初始化加载限制ip登录参数

    public void contextDestroyed(ServletContextEvent arg0) {
    }

    public void contextInitialized(ServletContextEvent context) {
         ProtocolFactory.init(Protocol.class, "com.wyd.empire.protocol.data", "com.zhwyd.server.handler");
    }
}
