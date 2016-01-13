package com.zhwyd.server.handler.account;
import com.wyd.empire.protocol.data.account.GetRandomNameOk;
import com.wyd.protocol.data.AbstractData;
import com.zhwyd.server.bean.User;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.common.util.SessionMap;
/**
 * 获取随机名称
 * @author LKJ
 *
 */
public class GetRandomNameOkHandler extends IMonitorDataHandler {
    public void handle(AbstractData data, User user) throws Exception {
        GetRandomNameOk result = (GetRandomNameOk) data;
//        SessionMap.getInstance().getMap().get(user).put(Global.GET_RANDOM_NAME_OK, result);
        SessionMap.getInstance().getMap().get(user.getUsername()).put(Global.GET_RANDOM_NAME_OK, result);
    }
    
    public void handle(AbstractData data, String sessionId) throws Exception {
        GetRandomNameOk result = (GetRandomNameOk) data;
        SessionMap.getInstance().getNewMap().get(sessionId).put(Global.GET_RANDOM_NAME_OK, result);
    }
}
