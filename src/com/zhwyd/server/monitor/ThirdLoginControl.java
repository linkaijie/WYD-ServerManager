package com.zhwyd.server.monitor;
import net.sf.json.JSONObject;
import com.wyd.empire.protocol.Protocol;
import com.wyd.net.ProtocolFactory;
import com.zhwyd.server.bean.User;
public class ThirdLoginControl {
    private static String  address = "192.168.3.137";
    private static Integer port    = 19990;

    public static void main(String[] args) throws Exception {
        ProtocolFactory.init(Protocol.class, "com.wyd.empire.protocol.data", "com.zhwyd.server.handler");
        String uuid = "";
        User user = new User();
        user.setUsername("ewrwexzTest7");
        user.setPassword("000000");
        // 第三方登录
        JSONObject loginJson = JSONObject.fromObject(BathCreative.login(user));// 返回uuid和sessionId
        uuid = loginJson.getString("uuid");
        System.out.println(loginJson.toString());
        if (loginJson.get("code").toString().equals("0")) {
            JSONObject registerJson = BathCreative.register(user);
            // code返回0代表不成功,不成功则返回错误信息
            if ("0".equals(registerJson.getString("code"))) {
                String resultMsg = user.getUsername() + "," + registerJson.getString("message");
                System.out.println(resultMsg);
                return;
            }
            uuid = registerJson.getString("uuid");
        }
        // 第三方登录后返回uuid
        user.setUuid(uuid);
        Monitor monitor = new Monitor(address, port, user);
        monitor.thirdLoginProcess(0);
    }
}
