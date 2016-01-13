package com.zhwyd.server.handler.error;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.wyd.empire.protocol.data.error.ProtocolError;
import com.wyd.protocol.data.AbstractData;
import com.zhwyd.server.bean.User;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.common.util.SessionMap;
import com.zhwyd.server.handler.account.IMonitorDataHandler;
/**
 * 获取错误信息
 * 
 * @author rL.
 */
public class ProtocolErrorHandler extends IMonitorDataHandler {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(ProtocolErrorHandler.class);
    @Autowired
    private HttpSession         session;
    /** 需要进行错误处理的信息 */
    private static List<String> errorList;
    static {
        errorList = new ArrayList<String>();
        // 如果是体力不足导致的闯关错误
        // errorList.add("您的体力不足");
        // 如果是连接超时导致的错误
        // errorList.add("连接超时，请稍后再试");
        // 如果是创建角色发生的错误
        errorList.add("存在同名角色");
        // 发生此错误时，当前线程并没有wait,因为这错误暂时不处理，丢到角色创建报错时再处理
        errorList.add("获取随机名称失败\t");
        errorList.add("角色名出现非法字符");
    }

    public void handle(AbstractData data, User user) throws Exception {
        ProtocolError result = (ProtocolError) data;
        // 错误处理,如果有进行错误处理则返回
        if (this.dealError(user, result.getCause())) {
            return;
        }
        String errorMsg = "用户名:" + user.getUsername() + "   提交错误类型:" + result.getSubmitErrorType() + "   错误信息:" + result.getCause();
        System.out.println(errorMsg);
        // 把错误写进login_error.log日志里面
        logger.error(errorMsg);
//        SessionMap.getInstance().getMap().get(user).put(Global.PROTOCOL_ERROR, result);
        SessionMap.getInstance().getMap().get(user.getUsername()).put(Global.PROTOCOL_ERROR, result);
    }

    public void handle(AbstractData data, String sessionId) throws Exception {
        ProtocolError result = (ProtocolError) data;
        // 错误处理,如果有进行错误处理则返回
        if (this.dealError(sessionId, result.getCause())) {
            return;
        }
        String errorMsg = "用户名:" + sessionId + "   提交错误类型:" + result.getSubmitErrorType() + "   错误信息:" + result.getCause();
        System.out.println(errorMsg);
        logger.error(errorMsg);
        SessionMap.getInstance().getNewMap().get(sessionId).put(Global.PROTOCOL_ERROR, result);
    }

    /**
     * 处理错误
     * 
     * @param user
     *            发生错误的玩家
     * @param causeMsg
     *            错误信息
     * @return
     */
    private boolean dealError(User user, String causeMsg) {
        for (String errorMsg : errorList) {
            // 如果发生的错误存在于可处理错误列表中
            if (errorMsg.equals(causeMsg)) {
                synchronized (user) {
                    // 通知主流程说有错误发生
                    user.setHasError(true);
                    user.notify();
                    return true;
                }
            }
        }
        // 如果错误没用在方法中被处理
        return false;
    }

    /**
     * 处理错误
     * 
     * @param sessionId
     *            发生错误的sessionId
     * @param causeMsg
     *            错误信息
     * @return
     */
    private boolean dealError(String sessionId, String causeMsg) {
        for (String errorMsg : errorList) {
            // 如果发生的错误存在于可处理错误列表中
            if (errorMsg.equals(causeMsg)) {
                session.setAttribute("error" + sessionId, "true");
                return true;
            }
        }
        // 如果错误没用在方法中被处理
        return false;
    }
}
