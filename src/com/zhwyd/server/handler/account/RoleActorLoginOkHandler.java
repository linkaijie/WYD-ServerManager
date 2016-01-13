package com.zhwyd.server.handler.account;
import com.wyd.empire.protocol.data.account.RoleActorLoginOk;
import com.wyd.protocol.data.AbstractData;
import com.zhwyd.server.bean.User;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.common.util.SessionMap;
/**
 * 获取角色登录成功返回信息
 * 
 * @author LKJ
 */
public class RoleActorLoginOkHandler extends IMonitorDataHandler {
    public void handle(AbstractData data, User user) throws Exception {
        RoleActorLoginOk result = (RoleActorLoginOk) data;
//        SessionMap.getInstance().getMap().get(user).put(Global.ROlE_ACTOR_LOGIN_OK, result);
        SessionMap.getInstance().getMap().get(user.getUsername()).put(Global.ROlE_ACTOR_LOGIN_OK, result);
    }

    public void handle(AbstractData data, String sessionId) throws Exception {
        RoleActorLoginOk result = (RoleActorLoginOk) data;
        SessionMap.getInstance().getNewMap().get(sessionId).put(Global.ROlE_ACTOR_LOGIN_OK, result);
    }
}
