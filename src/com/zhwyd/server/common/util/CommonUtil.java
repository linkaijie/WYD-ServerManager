package com.zhwyd.server.common.util;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import com.zhwyd.server.bean.BeanInterface;
import com.zhwyd.server.bean.JdbcUrl;
import com.zhwyd.server.cache.CacheService;
import com.zhwyd.server.common.constant.CommanConstant;
import com.zhwyd.server.common.constant.Global;
import com.zhwyd.server.config.Application;
import com.zhwyd.server.service.JdbcUrlService;
import com.zhwyd.server.ssh.util.SshxcuteUtils;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
public class CommonUtil {
    /**
     * 正则表达式比较字符串
     * @throws RowsExceededException
     */
    public static boolean Pattern(String regstr, String comparestr) throws RowsExceededException, WriteException {
        boolean result = false;
        Pattern p = Pattern.compile(regstr);
        Matcher m = p.matcher(comparestr);
        result = m.find();
        return result;
    }

    /**
     * 獲取被更新文件的MD5值
     * @param beanInterface
     * @return
     */
    public static Properties getFilesMD5(HttpSession session, BeanInterface beanInterface, List<String> updateFileNameList) throws Exception {
        Properties properties = new Properties();
        if (updateFileNameList != null && updateFileNameList.size() > 0) {
            String updatePath = beanInterface.getPath();
            StringBuffer sb = new StringBuffer();
            for (String fileName : updateFileNameList) {
                sb.append(updatePath + "/" + fileName).append(" ");// 拼接名稱路徑
            }
            Object[] fileMD5 = null;
            if (sb.length() > 0) {
                fileMD5 = SshxcuteUtils.getFileMD5(CacheService.getServerById(beanInterface.getServerId()), sb.toString());
            }
            if (fileMD5 != null && fileMD5.length > 0 && (Integer) fileMD5[0] == CommanConstant.RESULT_TRUE_STATE) {
                String[] fileMD5s = fileMD5[1].toString().trim().split("\n");
                for (int i = 0; i < updateFileNameList.size(); i++) {
                    properties.put(updateFileNameList.get(i), fileMD5s[i]);
                    // System.out.println(updateFileNameList.get(i) + "=" + properties.getProperty(updateFileNameList.get(i)));
                }
            }
        }
        return properties;
    }

    /**
     * 比較MD5值 
     * @param session           HttpSession
     * @param beanInterface     bean
     * @param properties        本地文件MD5信息
     * @throws Exception 
     */
    public static boolean compareMD5(HttpSession session, BeanInterface beanInterface, Properties properties, List<String> updateFileNameList) throws Exception {
        boolean result = true;
        // 獲取被更新文件的MD5值
        Properties updateProperties = getFilesMD5(session, beanInterface, updateFileNameList);
        Iterator<Entry<Object, Object>> it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Object, Object> entry = it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            // 比較本地與遠程的MD5值是否相等
            if (!updateProperties.getProperty(key.toString()).equals(value.toString())) {
                // batchOpearVo.isFail(key + "MD5不相等");
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * 更新前获取更新名称列表
     * @param   beanType    bean类型(AccountServer、IpdServer、WorldServer、DispatchServer)
     * @param   pathType    update或model
     */
    public static List<String> getFileNameList(String beanType, String pathType) throws Exception {
        List<String> updateFileNameList = new ArrayList<String>();
        // 更新模板路径
        String path = getPath(beanType, pathType);
        if (!StringUtils.isEmpty(path)) {
            Object[] result = SshxcuteUtils.getFileNameList(path);
            if (result != null && result.length > 0 && (Integer) result[0] == CommanConstant.RESULT_TRUE_STATE) {
                System.out.println("result=" + result[1].toString().trim());
                String[] fileNames = result[1].toString().trim().split("\n");
                if (!(fileNames.length == 1 && StringUtils.isEmpty(fileNames[0]))) {
                    for (int i = 0; i < fileNames.length; i++) {
                        updateFileNameList.add(fileNames[i].replace("\n", "").replace("\r", ""));
                    }
                }
            }
        }
        return updateFileNameList;
    }

    /**
     * 获取路径
     * 
     * @param beanType  bean类型
     * @param pathType  update或model
     * @return
     */
    public static String getPath(String beanType, String pathType) {
        if (StringUtils.isEmpty(beanType) || StringUtils.isEmpty(pathType)) {
            return null;
        }
        String path = "";
        BeanInterface beanInterface = null;
        if (beanType.equals("AccountServer")) {
            beanInterface = CacheService.getAccountServerAllList().get(0);
        } else if (beanType.equals("IpdServer")) {
            beanInterface = CacheService.getIpdServerAllList().get(0);
        } else if (beanType.equals("WorldServer")) {
            beanInterface = CacheService.getWorldServerAllList().get(0);
        } else if (beanType.equals("DispatchServer")) {
            beanInterface = CacheService.getDispatchServerAllList().get(0);
        } else if (beanType.equals("serverLib")) {
            beanInterface = CacheService.getServerAllList().get(0);
        } else if (beanType.equals("lib")) {
            beanInterface = CacheService.getServerAllList().get(0);
        }
        if (beanInterface != null) {
            if (beanType.equals("lib")) {// 特殊处理
                if (pathType.equals("update")) {
                    path = "/data/apps/model/update/lib";
                } else if (pathType.equals("model")) {
                    path = "/data/apps/model/model/lib";
                }
            } else if (beanType.equals("serverLib")) {// 特殊处理
                if (pathType.equals("update")) {
                    path = "/data/apps/model/update/serverLib";
                } else if (pathType.equals("model")) {
                    path = "/data/apps/model/model/serverLib";
                }
            } else {
                if (pathType.equals("update")) {
                    path = beanInterface.getUpdatePath();
                } else if (pathType.equals("model")) {
                    path = beanInterface.getModelPath();
                }
            }
        }
        return path;
    }

    /**
     * 获取jdbc地址(无用)
     * @param type
     * @return
     */
    public static String getJdbcPathOld(String type) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        String path = "";
        if (type.equals("JDBC_BEFORE_36")) {
            path = Global.JDBC_BEFORE_36;
        } else if (type.equals("JDBC_AFTER_37")) {
            path = Global.JDBC_AFTER_37;
        } else if (type.equals("JDBC_AFTER_161")) {
            path = Global.JDBC_AFTER_161;
        } else if (type.equals("JDBC_HD")) {
            path = Global.JDBC_HD;
        } else if (type.equals("JDBC_6")) {
            path = Global.JDBC_6;
        } else if (type.equals("JDBC_IOS_WORLD1")) {
            path = Global.JDBC_IOS_WORLD1;
        } else if (type.equals("JDBC_TW_TEST")) {
            path = Global.JDBC_TW_TEST;
        } else if (type.equals("JDBC_TW_TEST_ACCOUNT")) {
            path = Global.JDBC_TW_TEST_ACCOUNT;
        } else if (type.equals("JDBC_TX_ACCOUNT")) {
            path = Global.JDBC_TX_ACCOUNT;
        } else if (type.equals("JDBC_6_ACCOUNT")) {
            path = Global.JDBC_6_ACCOUNT;
        } else if (type.equals("JDBC_IOS_ACCOUNT")) {
            path = Global.JDBC_IOS_ACCOUNT;
        } else if (type.equals("JDBC_VN_TEST")) {
            path = Global.JDBC_VN_TEST;
        } else if (type.equals("JDBC_VN_TEST_ACCOUNT")) {
            path = Global.JDBC_VN_TEST_ACCOUNT;
        }
        return path;
    }

    /**
     * 获取jdbc地址
     * @param type
     * @return
     */
    public static String getJdbcPath(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        String path = "";
        List<JdbcUrl> jdbcUrlList = Application.getBean(JdbcUrlService.class).getJdbcUrlList(CommanConstant.TYPE_JDBC_ALL);
        if (jdbcUrlList != null && jdbcUrlList.size() > 0) {
            for (JdbcUrl jdbcUrl : jdbcUrlList) {
                if (jdbcUrl.getJdbcKey().equals(key)) {
                    path = jdbcUrl.getJdbcValue();
                }
            }
        }
        return path;
    }

    public static void outputApiResult(HttpServletResponse response, String result) throws IOException {
        writeResult(response, result);
    }

    /**
     * 输出结果
     * 
     * @param response      HttpServletResponse
     * @param res           结果串
     * @throws IOException
     */
    public static void writeResult(HttpServletResponse response, CharSequence res) throws IOException {
        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(res.toString());
        out.flush();
        out.close();
        res = null;
    }

    public static Properties getBeanAndVoName() {
        Properties properties = new Properties();
        long currentTime = System.currentTimeMillis() / 1000;
        String sessionVoListName = currentTime + "Vo";
        String sessionBeanListName = currentTime + "Bean";
        properties.put("sessionVoListName", sessionVoListName);
        properties.put("sessionBeanListName", sessionBeanListName);
        return properties;
    }
}
