package com.zhwyd.server.handler.account;
import java.util.concurrent.ConcurrentHashMap;
import com.wyd.empire.protocol.data.account.LoginOk;
import com.wyd.protocol.data.AbstractData;
import com.zhwyd.server.bean.User;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.common.util.SessionMap;
/**
 * 获取第三方登录成功信息
 * @author LKJ
 *
 */
public class LoginOkHandler extends IMonitorDataHandler {
    public void handle(AbstractData data, User user) throws Exception {
        LoginOk result = (LoginOk) data;
        //将结果put进该玩家所属的Map中
//        if (SessionMap.getInstance().getMap().get(user) !=null && SessionMap.getInstance().getMap().get(user).size()>0) {
//            SessionMap.getInstance().getMap().get(user).put(Global.LOGIN_OK, result); 
//        } else {
//            SessionMap.getInstance().getMap().put(user, new ConcurrentHashMap<String, AbstractData>());
//            SessionMap.getInstance().getMap().get(user).put(Global.LOGIN_OK, result); 
//        }
        if (SessionMap.getInstance().getMap().get(user.getUsername()) !=null && SessionMap.getInstance().getMap().get(user.getUsername()).size()>0) {
            SessionMap.getInstance().getMap().get(user.getUsername()).put(Global.LOGIN_OK, result); 
        } else {
            SessionMap.getInstance().getMap().put(user.getUsername(), new ConcurrentHashMap<String, AbstractData>());
            SessionMap.getInstance().getMap().get(user.getUsername()).put(Global.LOGIN_OK, result); 
        }
        //直到登录成功,hasError为true主线程会一直尝试重新登录
        synchronized (user) {
            user.setHasError(false);
            user.notify();
        }
    }
    
    public void handle(AbstractData data, String sessionId) throws Exception {
        LoginOk result = (LoginOk) data;
        //将结果put进该玩家所属的Map中
        if (SessionMap.getInstance().getNewMap().get(sessionId) !=null && SessionMap.getInstance().getNewMap().get(sessionId).size()>0) {
            SessionMap.getInstance().getNewMap().get(sessionId).put(Global.LOGIN_OK, result); 
        } else {
            SessionMap.getInstance().getNewMap().put(sessionId, new ConcurrentHashMap<String, AbstractData>());
            SessionMap.getInstance().getNewMap().get(sessionId).put(Global.LOGIN_OK, result); 
        }
    }
}
