package com.zhwyd.server.monitor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.NameValuePair;
import com.zhwyd.server.bean.User;
import com.zhwyd.server.common.util.CryptionUtil;
import com.zhwyd.server.common.util.HttpClientUtil;
import com.zhwyd.server.config.Application;
/**
 * 向账号系统发送http的api类
 * 
 * @author rL.
 */
public class BathCreative {
    /**
     * 加密密钥
     */
    public static String secretKey = "pifnwkjdhn";
    /**
     * 应用id
     */
    public static String appId     = "gunsoul";
    /**
     * 请求地址
     */
//    public static String url       = "http://192.168.1.212:8080/account/api/user";//改账号服务器地址
    public static String url       = "http://183.57.16.34:8087/account/api/user";//改账号服务器地址
//    public static String url       = "http://180.150.188.84:8191/account/api/user";//改账号服务器地址
    
    /**
     * 保存文件路径
     */
    public static String filePath  = "d:" + File.separator + "registerData.txt";

    public static void main(String[] args) throws Exception {
        // User user = new User();
        // user.setUsername("testtest");
        // user.setPassword("000000");
        // System.out.println(register(user));
        // System.out.println(login(user));
    }

    /**
     * 检测注册名字的合法性
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public static JSONObject checkUsername(User user) throws Exception {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        JSONObject json = new JSONObject();
        json.element("type", "checkUsername");
        byte[] b = CryptionUtil.Encrypt(json.toString(), secretKey);
        list.add(new NameValuePair("username", user.getUsername()));
        list.add(new NameValuePair("appId", appId));
        list.add(new NameValuePair("data", CryptionUtil.byteToString(b)));
        return JSONObject.fromObject(HttpClientUtil.PostData(url, list));
    }

    /**
     * 检测注册名字的合法性
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public static JSONObject checkUsername(String username) throws Exception {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        JSONObject json = new JSONObject();
        json.element("type", "checkUsername");
        byte[] b = CryptionUtil.Encrypt(json.toString(), secretKey);
        list.add(new NameValuePair("username", username));
        list.add(new NameValuePair("appId", appId));
        list.add(new NameValuePair("data", CryptionUtil.byteToString(b)));
        return JSONObject.fromObject(HttpClientUtil.PostData(url, list));
    }

    /**
     * 注册账号
     * 
     * @param user
     * @throws Exception
     */
    public static JSONObject register(User user) throws Exception {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        JSONObject json = new JSONObject();
        json.element("type", "register");
        json.element("password", user.getPassword());
        json.element("token", "token");
        byte[] b = CryptionUtil.Encrypt(json.toString(), secretKey);
        list.add(new NameValuePair("username", user.getUsername()));
        list.add(new NameValuePair("appId", appId));
        list.add(new NameValuePair("data", CryptionUtil.byteToString(b)));
        String accountAddress = Application.getConfig("system", "accountAddress");
        return JSONObject.fromObject(HttpClientUtil.PostData(accountAddress, list));
    }

    public static JSONObject login(User user) throws Exception {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        JSONObject json = new JSONObject();
        json.element("type", "login");
        json.element("password", user.getPassword());
        byte[] b = CryptionUtil.Encrypt(json.toString(), secretKey);
        list.add(new NameValuePair("username", user.getUsername()));
        list.add(new NameValuePair("appId", appId));
        list.add(new NameValuePair("data", CryptionUtil.byteToString(b)));
        String accountAddress = Application.getConfig("system", "accountAddress");
        return JSONObject.fromObject(HttpClientUtil.PostData(accountAddress, list));
    }

    public static void bathRegister() throws Exception {
        for (int i = 1; i <= 100; i++) {
            User user = new User();
            // user.setUsername("不要打我的脸");
            user.setUsername("小小小小鸟" + i);
            user.setPassword("000000");
            JSONObject checkResultJson = checkUsername(user);
            if (checkResultJson.getInt("code") == 0) {
                System.out.println(user.getUsername() + "======" + checkResultJson.getString("message"));
                // continue;
                // 有一个不合法即全部不注册
                return;
            }
            // System.out.println(checkResultJson);
            // FileUtil.writeFile(filePath, resultJson.toString());
            // System.out.println(FileUtil.readFile(filePath));
            // String[] str = FileUtil.readFile(filePath).split("\r\n");
            // FileUtil.writeFile(filePath, register(user).toString());
        }
    }
}
