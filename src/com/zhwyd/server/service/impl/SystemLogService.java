package com.zhwyd.server.service.impl;
import java.util.Date;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import com.zhwyd.server.common.constant.Global;
/**
 * 系统日志服务
 * 
 * @author LKJ
 */
public class SystemLogService {
    private static Logger accountServerLog  = Logger.getLogger("accountServer");
    private static Logger ipdServerLog      = Logger.getLogger("ipdServer");
    private static Logger worldServerLog    = Logger.getLogger("worldServer");
    private static Logger dispatchServerLog = Logger.getLogger("dispatchServer");
    private static Logger serverManegeLog   = Logger.getLogger("serverManege");
    private static Logger batchAccountLog   = Logger.getLogger("batchAccount");
    private static Logger batchDispatchLog  = Logger.getLogger("batchDispatch");
    private static Logger batchIpdLog       = Logger.getLogger("batchIpd");
    private static Logger batchWorldLog     = Logger.getLogger("batchWorld");
    private static Logger monitorLog        = Logger.getLogger("monitor");
    private static Logger redisLog          = Logger.getLogger("redis");

    public static void accountServerLog(HttpSession session, String string) {
        StringBuilder sb = getLogHead(session);
        sb.append(string);
        accountServerLog.info(sb);
    }

    public static void ipdServerLog(HttpSession session, String string) {
        StringBuilder sb = getLogHead(session);
        sb.append(string);
        ipdServerLog.info(sb);
    }

    public static void worldServerLog(HttpSession session, String string) {
        StringBuilder sb = getLogHead(session);
        sb.append(string);
        worldServerLog.info(sb);
    }

    public static void dispatchServerLog(HttpSession session, String string) {
        StringBuilder sb = getLogHead(session);
        sb.append(string);
        dispatchServerLog.info(sb);
    }

    public static void serverManegeLog(HttpSession session, String string) {
        StringBuilder sb = getLogHead(session);
        sb.append(string);
        serverManegeLog.info(sb);
    }

    public static void batchAccountLog(HttpSession session, String string) {
        StringBuilder sb = getLogHead(session);
        sb.append(string);
        batchAccountLog.info(sb);
    }

    public static void batchDispatchLog(HttpSession session, String string) {
        StringBuilder sb = getLogHead(session);
        sb.append(string);
        batchDispatchLog.info(sb);
    }

    public static void batchIpdLog(HttpSession session, String string) {
        StringBuilder sb = getLogHead(session);
        sb.append(string);
        batchIpdLog.info(sb);
    }

    public static void batchWorldLog(HttpSession session, String string) {
        StringBuilder sb = getLogHead(session);
        sb.append(string);
        batchWorldLog.info(sb);
    }

    public static void monitorLog(CharSequence monitorInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        sb.append(monitorInfo);
        monitorLog.info(sb);
    }

    public static void redisLog(HttpSession session, String string) {
        StringBuilder sb = getLogHead(session);
        sb.append(string);
        redisLog.info(sb);
    }

    /**
     * 获取日志头部信息
     * 
     * @param logVersion
     *            版本号
     * @param player
     *            相关玩家
     * @return
     */
    private static StringBuilder getLogHead(HttpSession session) {
        StringBuilder sb = new StringBuilder();
        sb.append(session.getAttribute(Global.LOGIN_CURRENT_NAME)).append("|").append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return sb;
    }
}
