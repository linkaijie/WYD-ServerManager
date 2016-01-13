package com.zhwyd.server.handler.factory;
import java.util.HashMap;
import java.util.Map;
import com.wyd.protocol.handler.IDataHandler;
import com.zhwyd.server.handler.account.GetRandomNameOkHandler;
import com.zhwyd.server.handler.account.GetRoleActorListOkHandler;
import com.zhwyd.server.handler.account.LoginOkHandler;
import com.zhwyd.server.handler.account.RoleActorLoginOkHandler;
import com.zhwyd.server.handler.error.ProtocolErrorHandler;
public class HandlerFactory {
    private static final Map<String, Class<? extends IDataHandler>> handlerMap = new HashMap<String, Class<? extends IDataHandler>>();
//    static {
//        handlerMap.put("LoginOk", LoginOkHandler.class);
//        handlerMap.put("GetRandomNameOk", GetRandomNameOkHandler.class);
//        handlerMap.put("GetRoleActorListOk", GetRoleActorListOkHandler.class);
//        handlerMap.put("RoleActorLoginOk", RoleActorLoginOkHandler.class);
//        handlerMap.put("ProtocolError", ProtocolErrorHandler.class);
//    }
    
    public static void initHandler() {
        handlerMap.put("LoginOk", LoginOkHandler.class);
        handlerMap.put("GetRandomNameOk", GetRandomNameOkHandler.class);
        handlerMap.put("GetRoleActorListOk", GetRoleActorListOkHandler.class);
        handlerMap.put("RoleActorLoginOk", RoleActorLoginOkHandler.class);
        handlerMap.put("ProtocolError", ProtocolErrorHandler.class);
    }

    public static IDataHandler getHandler(String type) {
        Class<? extends IDataHandler> typeClass = handlerMap.get(type);
        if (typeClass != null) {
            try {
                return typeClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("system error", e);
            }
        } else {
            return null;
        }
    }
}
