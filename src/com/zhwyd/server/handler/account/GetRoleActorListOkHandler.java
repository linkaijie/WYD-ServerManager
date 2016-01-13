package com.zhwyd.server.handler.account;
import com.wyd.empire.protocol.data.account.GetRoleActorListOk;
import com.wyd.protocol.data.AbstractData;
import com.zhwyd.server.bean.User;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.common.util.SessionMap;
/**
 * 获取角色列表
 * @author LKJ
 *
 */
public class GetRoleActorListOkHandler extends IMonitorDataHandler {
    public void handle(AbstractData data, User user) throws Exception {
        GetRoleActorListOk result = (GetRoleActorListOk) data;
//        SessionMap.getInstance().getMap().get(user).put(Global.GET_ROLE_ACTOR_LIST_OK, result);
        SessionMap.getInstance().getMap().get(user.getUsername()).put(Global.GET_ROLE_ACTOR_LIST_OK, result);
        // 直到创建角色成功,hasError为true主线程会一直尝试重新创建角色
        synchronized (user) {
            user.setHasError(false);
            user.notify();
        }
    }
    
    public void handle(AbstractData data, String sessionId) throws Exception {
        GetRoleActorListOk result = (GetRoleActorListOk) data;
        SessionMap.getInstance().getNewMap().get(sessionId).put(Global.GET_ROLE_ACTOR_LIST_OK, result);
    }
}
