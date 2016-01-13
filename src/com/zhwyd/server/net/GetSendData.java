package com.zhwyd.server.net;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import com.wyd.empire.protocol.data.account.CreateRoleActor;
import com.wyd.empire.protocol.data.account.GetRandomName;
import com.wyd.empire.protocol.data.account.GetRandomNameOk;
import com.wyd.empire.protocol.data.account.GetRoleActorList;
import com.wyd.empire.protocol.data.account.GetRoleActorListOk;
import com.wyd.empire.protocol.data.account.LoginAgain;
import com.wyd.empire.protocol.data.account.LoginOk;
import com.wyd.empire.protocol.data.account.RoleActorLogin;
import com.wyd.empire.protocol.data.account.RoleActorLoginOk;
import com.wyd.empire.protocol.data.account.ThirdLogin;
import com.wyd.empire.protocol.data.system.GetResource;
import com.zhwyd.server.bean.User;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.common.util.SessionMap;
/**
 * 获取发送数据
 * 
 * @author rL.
 */
public class GetSendData {
    /**
     * 根据用户信息获取第三方登录信息
     * 
     * @param user
     * @return
     */
    public static ThirdLogin getThirdLogin(User user) {
        ThirdLogin thirdLogin = new ThirdLogin();
        thirdLogin.setAccountName(user.getUsername());
        thirdLogin.setAccountPwd(user.getPassword());
        thirdLogin.setUuid(user.getUuid());
        thirdLogin.setModel("iOS");
        thirdLogin.setVersion("2.0.0");
        thirdLogin.setToken("");
        thirdLogin.setChannel(1058);
        return thirdLogin;
    }
    
    /**
     * 根据用户信息获取第三方登录信息
     * 
     * @param user
     * @return
     */
    public static ThirdLogin getThirdLoginAndServiceId(User user, String serviceId) {
        ThirdLogin thirdLogin = new ThirdLogin();
        thirdLogin.setAccountName(user.getUsername());
        thirdLogin.setAccountPwd(user.getPassword());
        thirdLogin.setUuid(user.getUuid());
        thirdLogin.setModel("iOS");
        thirdLogin.setVersion("2.0.0");
        thirdLogin.setToken("");
        thirdLogin.setChannel(1058);
        thirdLogin.setChioceAreaId(serviceId);
        thirdLogin.setIdfa("");
        return thirdLogin;
    }

    /**
     * 根据用户信息获取重新登录信息
     * 
     * @param user
     * @return
     */
    public static LoginAgain getLoginAgain(User user) {
        LoginAgain loginAgain = new LoginAgain();
        loginAgain.setAccountName(user.getUsername());
        loginAgain.setAccountPwd(user.getPassword());
        loginAgain.setPlayerName(user.getPlayerName());
        loginAgain.setModel("iOS");
        loginAgain.setVersion("2.0.0");
        loginAgain.setChannel(1058);
        loginAgain.setUuid(user.getUuid());
        return loginAgain;
    }

    /**
     * 获取角色选择列表
     * 
     * @return
     */
    public static GetRoleActorList getRoleActorList() {
        return new GetRoleActorList();
    }

    /**
     * 获取随机名称
     */
    public static GetRandomName getRondomName() {
        GetRandomName randomName = new GetRandomName();
        randomName.setSex(0);
        return randomName;
    }

    /**
     * 根据随机名称获取角色创建列表
     * 
     * @param getRandomNameOk
     * @return
     * @throws Exception
     */
    public static CreateRoleActor getCreateRoleActor(GetRandomNameOk getRandomNameOk) {
        CreateRoleActor createRoleActor = new CreateRoleActor();
        if (getRandomNameOk == null) {
            getRandomNameOk = new GetRandomNameOk();
        }
        // 如果发生错误:"获取随机名称失败",则先不处理，将name设置为空字符串,防止出现空指针错误，在创建角色时再处理
        if (StringUtils.isBlank(getRandomNameOk.getName())) {
            createRoleActor.setPlayerName("错误的名字");
        } else {
            createRoleActor.setPlayerName(getRandomNameOk.getName());
        }
        createRoleActor.setPlayerSex(0);
        createRoleActor.setPlayerHeadIcon(0);
        createRoleActor.setDevice("iOS");
        createRoleActor.setArea("");
        return createRoleActor;
    }

    /**
     * 根据角色列表获取角色登录信息
     */
    public static RoleActorLogin getRoleActorLogin(GetRoleActorListOk getRoleActorListOk) throws Exception {
        RoleActorLogin roleActorLogin = new RoleActorLogin();
        roleActorLogin.setPlayerName(getRoleActorListOk.getPlayerName()[0]);
        return roleActorLogin;
    }

    /**
     * 从服务器获取资源
     */
    public static GetResource getResource(Integer resourceType) {
        GetResource getResource = new GetResource();
        getResource.setResourceType(resourceType);
        getResource.setLastUpdateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return getResource;
    }

    public static LoginOk getLoginOk(User user) {
        LoginOk loginOk = (LoginOk) SessionMap.getInstance().getMsgByUser(user, Global.LOGIN_OK);
        SessionMap.getInstance().removeByKey(user, Global.LOGIN_OK);
        return loginOk;
    }

    // new
    public static LoginOk getLoginOk(String sessionId) {
        LoginOk loginOk = (LoginOk) SessionMap.getInstance().getMsgByUser(sessionId, Global.LOGIN_OK);
        SessionMap.getInstance().removeByKey(sessionId, Global.LOGIN_OK);
        return loginOk;
    }

    public static GetRandomNameOk getRandomNameOk(User user) {
        GetRandomNameOk getRandomNameOk = (GetRandomNameOk) SessionMap.getInstance().getMsgByUser(user, Global.GET_RANDOM_NAME_OK);
        SessionMap.getInstance().removeByKey(user, Global.GET_RANDOM_NAME_OK);
        return getRandomNameOk;
    }

    public static GetRoleActorListOk getRoleActorListOk(User user) {
        GetRoleActorListOk getRoleActorListOk = (GetRoleActorListOk) SessionMap.getInstance().getMsgByUser(user, Global.GET_ROLE_ACTOR_LIST_OK);
        SessionMap.getInstance().removeByKey(user, Global.GET_ROLE_ACTOR_LIST_OK);
        return getRoleActorListOk;
    }

    public static RoleActorLoginOk getRoleActorLoginOk(User user) {
        RoleActorLoginOk roleActorLoginOk = (RoleActorLoginOk) SessionMap.getInstance().getMsgByUser(user, Global.ROlE_ACTOR_LOGIN_OK);
        SessionMap.getInstance().removeByKey(user, Global.ROlE_ACTOR_LOGIN_OK);
        return roleActorLoginOk;
    }
}
