package com.zhwyd.server.monitor;
import java.net.InetSocketAddress;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.wyd.empire.protocol.data.account.CreateRoleActor;
import com.wyd.empire.protocol.data.account.GetRandomName;
import com.wyd.empire.protocol.data.account.GetRandomNameOk;
import com.wyd.empire.protocol.data.account.GetRoleActorList;
import com.wyd.empire.protocol.data.account.GetRoleActorListOk;
import com.wyd.empire.protocol.data.account.LoginOk;
import com.wyd.empire.protocol.data.account.RoleActorLogin;
import com.wyd.empire.protocol.data.account.RoleActorLoginOk;
import com.wyd.empire.protocol.data.account.ThirdLogin;
import com.zhwyd.server.bean.User;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.common.util.SessionMap;
import com.zhwyd.server.net.AdminConnector;
import com.zhwyd.server.net.GetOkData;
import com.zhwyd.server.net.GetSendData;
public class Monitor {
    @SuppressWarnings("unused")
    private static final Logger logger  = Logger.getLogger(Monitor.class);
    /** 服务器地址 */
    private String              address = "192.168.3.137";
    /** 端口号 */
    private Integer             port    = 19990;
    /** 连接实例 */
    private AdminConnector      connector;
    /** 唯一标示一个玩家 */
    private String              sessionId;
    /** 唯一标示一个玩家 */
    private String              serviceId;
    /** 一个任务连接对应一个玩家 */
    private User                user;
    // @Autowired
    // private HttpSession session;

    public Monitor() {
    }

    public Monitor(String address, Integer port, User user) {
        this.address = address;
        this.port = port;
        this.user = user;
        this.sessionId = UUID.randomUUID().toString();
        this.connector = this.initOneConnector();
    }

    public Monitor(String serviceId, String address, Integer port, User user) {
        this.serviceId = serviceId;
        this.address = address;
        this.port = port;
        this.user = user;
        this.sessionId = UUID.randomUUID().toString();
        this.connector = this.initOneConnector();
    }

    /**
     * 发送第三方登录协议
     */
    @SuppressWarnings("unused")
    public String thirdLoginProcess(int number) throws Exception {
        // 向服务端发起连接
        int connectNum = 0;
        while (!this.connector.isConnected()) {
            String connectResult = this.connector.connect();
            connectNum++;
            // 建立连接5次不成功视为失败
            if (connectNum >= 2) {
                System.out.println("connectResult=" + connectResult);
                return Global.CONNECT_FAIL_TWO_TIME + connectResult;
            }
        }
        // ThirdLogin thirdLogin = GetSendData.getThirdLogin(user);
        ThirdLogin thirdLogin = GetSendData.getThirdLoginAndServiceId(user, serviceId);
        connector.send(thirdLogin);
        LoginOk loginOk = GetSendData.getLoginOk(user);
        // LoginOk loginOk = GetSendData.getLoginOk(String.valueOf(connector.getSession().getId()));
        // System.out.println("MonitorloginOk=" + loginOk);
        // if (protocolErrorThrow(String.valueOf(connector.getSession().getId()))) {
        if (protocolErrorThrow()) {
            return Global.THIRD_LOGIN_ERROR;
        }
        int i = 0;
        // 进行第三方登录5次不成功是为失败
        if (number > 0 && number <= 2) {
            i = number;
        } else if (number > 2) {
            return Global.LOGIN_TWO_TIMES_ERROR;
        }
        if (loginOk == null) {
            // 返回错误协议，发送错误信息
            if (protocolErrorThrow()) {
                return Global.THIRD_LOGIN_ERROR;
            }
            i++;
            this.thirdLoginProcess(i);
        }
        if (loginOk != null && this.hasNoRole(loginOk)) {
            // 获取随机姓名
            GetRandomName randomName = GetSendData.getRondomName();
            this.connector.send(randomName);
            // 重新返回新的随机名称
            GetRandomNameOk randomNameOk = GetSendData.getRandomNameOk(user);
            if (protocolErrorThrow()) {
                // if (protocolErrorThrow(String.valueOf(connector.getSession().getId()))) {
                return Global.RANDOM_NAME_ERROR;
            }
            if (randomNameOk == null) {
                i++;
                this.thirdLoginProcess(i);
            }
            // 创建新角色
            CreateRoleActor createRoleActor = GetSendData.getCreateRoleActor(randomNameOk);
            this.connector.send(createRoleActor);
            if (protocolErrorThrow()) {
                // if (protocolErrorThrow(String.valueOf(connector.getSession().getId()))) {
                return Global.ROLE_ACTOR_ERROR;
            }
            // 如果创建角色失败,则重新获取随机名字,创建角色,直到角色创建成功为止
            i = 0;
            while (this.user.hasError()) {
                // while ("true".equals(session.getAttribute("error"+connector.getSession().getId()))) {
                if (i > 2) {
                    return Global.ROLE_ACTOR_TWO_TIMES_ERROR;
                }
                // 重新获取角色名称
                randomName = GetSendData.getRondomName();
                this.connector.send(randomName);
                // 重新返回新的随机名称
                randomNameOk = GetOkData.getRandomNameOk(user);
                if (protocolErrorThrow()) {
                    // if (protocolErrorThrow(String.valueOf(connector.getSession().getId()))) {
                    return Global.RANDOM_NAME_ERROR;
                }
                // 重新创建角色
                createRoleActor = GetSendData.getCreateRoleActor(randomNameOk);
                this.connector.send(createRoleActor);
                i++;
                if (protocolErrorThrow()) {
                    // if (protocolErrorThrow(String.valueOf(connector.getSession().getId()))) {
                    return Global.ROLE_ACTOR_ERROR;
                }
            }
            // 获取角色列表
            GetRoleActorList roleActorList = GetSendData.getRoleActorList();
            this.connector.send(roleActorList);
            GetRoleActorListOk getRoleActorListOk = GetSendData.getRoleActorListOk(user);
            if (protocolErrorThrow()) {
                // if (protocolErrorThrow(String.valueOf(connector.getSession().getId()))) {
                return Global.ACTOR_LIST_ERROR;
            }
            // 角色登录
            RoleActorLogin roleActorLogin = GetSendData.getRoleActorLogin(getRoleActorListOk);
            this.connector.send(roleActorLogin);
            RoleActorLoginOk roleActorLoginOk = GetSendData.getRoleActorLoginOk(user);
            if (protocolErrorThrow()) {
                // if (protocolErrorThrow(String.valueOf(connector.getSession().getId()))) {
                return Global.ACTOR_LOGIN_ERROR;
            }
        }
        connector.close();
        return "";
    }

    public boolean protocolErrorThrow() {
        // if (SessionMap.getInstance().getMap().get(user).get(Global.PROTOCOL_ERROR) != null) {
        // SessionMap.getInstance().removeByKey(user, Global.PROTOCOL_ERROR);
        if (SessionMap.getInstance().getMap().get(user.getUsername()).get(Global.PROTOCOL_ERROR) != null) {
            SessionMap.getInstance().removeByKey(user, Global.PROTOCOL_ERROR);
            connector.close();
            return true;
        } else {
            return false;
        }
    }

    public boolean protocolErrorThrow(String sessionId) {
        if (SessionMap.getInstance().getNewMap().get(sessionId).get(Global.PROTOCOL_ERROR) != null) {
            SessionMap.getInstance().removeByKey(sessionId, Global.PROTOCOL_ERROR);
            connector.close();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否有角色
     */
    private boolean hasNoRole(LoginOk loginOk) {
        return loginOk == null || loginOk.getPlayerNames() == null || loginOk.getPlayerNames().length == 0 || StringUtils.isBlank(loginOk.getPlayerNames()[0]) || loginOk.getServiceIds() == null || loginOk.getServiceIds().length == 0;
    }

    /**
     * 判断是否连接
     */
    public boolean isConnected() {
        if (this.connector == null || !this.connector.isConnected()) {
            return false;
        }
        return true;
    }

    /**
     * 关闭连接
     */
    public void closeConnector() {
        if (this.connector != null) {
            this.connector = null;
        }
    }

    /**
     * 初始化连接
     */
    private AdminConnector initOneConnector() {
        // 向账号系统发送http请求,获取uuid
        AdminConnector connector = null;
        try {
            // 第三方登录后返回uuid
            connector = new AdminConnector(this.sessionId, new InetSocketAddress(this.address, this.port), false, this.user);
            // 根据sessionId绑定一个user,一个user对应唯一个userMap
            // SessionMap.getInstance().bind(this.sessionId, user);
            // 向服务端发起连接
            if (!connector.isConnected()) {
                connector.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connector;
    }
}