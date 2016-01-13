package com.zhwyd.server.net;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import com.wyd.net.ProtocolFactory;
import com.wyd.protocol.data.AbstractData;
import com.zhwyd.server.bean.User;
import com.zhwyd.server.common.util.SessionMap;
import com.zhwyd.server.handler.account.IMonitorDataHandler;
/**
 * 原始的会话处理
 */
public class OriginalSessionHandler extends IoHandlerAdapter {
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        // User currentUser = (User) session.getAttribute("currentUser");
        long sessionId = session.getId();
        System.out.println("sessionId=" + sessionId);
        User currentUser = SessionMap.getInstance().getUserBySessionId(String.valueOf(sessionId));
        if (currentUser == null) {
            return;
        }
        throw new Exception(cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        // User currentUser = (User) session.getAttribute("currentUser");
        long sessionId = session.getId();
        // System.out.println("sessionId=" + sessionId);
        User currentUser = SessionMap.getInstance().getUserBySessionId(String.valueOf(sessionId));
        AbstractData data = (AbstractData) message;
        if (data == null) {
            MonitorConnector.log.error("get a NULL data!");
        } else {
            // System.out.println("className=" + message.getClass().getName());
            // System.out.println("handlerClass=" + data.getClass());
            IMonitorDataHandler handler = (IMonitorDataHandler) ProtocolFactory.getDataHandler(data);
            // String type = message.getClass().getName().substring(message.getClass().getName().lastIndexOf(".") + 1);
            // IMonitorDataHandler handler = (IMonitorDataHandler) HandlerFactory.getHandler(type);
            if (handler != null) {
                handler.handle(data, currentUser);
            }
            // 测试
            // if (handler != null) {
            // handler.handle(data, String.valueOf(sessionId));
            // }
        }
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
    }

    /*
     * sessionOpened()方法是在 TCP 连接 建立之后，接收到数据之前发送
     */
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
    }
}
