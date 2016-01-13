package com.zhwyd.server.net;
import org.apache.commons.lang3.RandomStringUtils;
import com.wyd.empire.protocol.data.account.GetRandomNameOk;
import com.wyd.empire.protocol.data.account.GetRoleActorListOk;
import com.wyd.empire.protocol.data.account.LoginOk;
import com.wyd.empire.protocol.data.account.RoleActorLoginOk;
import com.zhwyd.server.bean.User;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.common.util.SessionMap;
/**
 * 此处与GetOkData的区别是，此类会改变玩家面板状态
 * @author rL.
 *
 */
public class GetOkData {
    // ==================================登录流程==============================================
    public static LoginOk getLoginOk(User user) {
        LoginOk loginOk = (LoginOk) SessionMap.getInstance().getMsgByUser(user, Global.LOGIN_OK);
        SessionMap.getInstance().removeByKey(user, Global.LOGIN_OK);
        return loginOk;
    }

    public static GetRandomNameOk getRandomNameOk(User user) {
        GetRandomNameOk getRandomNameOk = (GetRandomNameOk) SessionMap.getInstance().getMsgByUser(user, Global.GET_RANDOM_NAME_OK);
        SessionMap.getInstance().removeByKey(user, Global.GET_RANDOM_NAME_OK);
        return getRandomNameOk;
    }

    /**
     * 本地生成一个随机名称
     * @param user
     * @return
     */
    public static GetRandomNameOk getRandomNameOkByLocal(User user, int length) {
        GetRandomNameOk getRandomNameOk = new GetRandomNameOk();
        // 根据字母表随机生成长度为8的字符
        getRandomNameOk.setName(RandomStringUtils.randomAlphabetic(length));
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
