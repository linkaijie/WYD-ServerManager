package com.zhwyd.server.common.util;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;
public class PropertiesUtil {
    public static String getFileIO(String name, String fileURL) {
        Properties prop = new Properties();
        InputStream in = PropertiesUtil.class.getResourceAsStream(fileURL);
        try {
            prop.load(in);
            return prop.getProperty(name);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 修改properties文件 author:LKJ 2014-10-8
     * 
     * @param key
     * @param value
     * @param fileURL
     * @param tempURL
     * @throws Exception
     */
    public static void writeData(String keys, String values, String fileURL, String tempURL) throws Exception {
        Properties prop = new Properties();
        InputStream fis = null;
        OutputStream fos = null;
        try {
            File file = new File(fileURL);
            // File tempFile = file.createTempFile("configWorldTemp", "properties");
            File tempFile = new File(tempURL);
            System.out.println("name= " + tempFile.getName());
            if (!tempFile.exists()) tempFile.createNewFile();
            fis = new FileInputStream(file);
            prop.load(fis);
            fis.close();// 一定要在修改值之前关闭fis
            fos = new FileOutputStream(tempFile);
            String[] key = keys.split(",");
            String[] value = values.split(",");
            if (key != null && key.length != value.length) {
                throw new Exception();
            }
            for (int i = 0; i < key.length; i++) {
                prop.setProperty(key[i], value[i]);
            }
            prop.store(fos, "Update '" + key + "' value");
            fos.close();
        } catch (IOException e) {
            System.err.println("Visit " + fileURL + " for updating " + values + " value error");
        } finally {
            try {
                fos.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // ConfigUtil.getFileIO("name", "gxyTest.properties");
        // System.out.println("path="+path);
        PropertiesUtil.writeData("machinename", "测试", "D:/test/ServerDeploy/worldServer/configWorld.properties", "/D:/myEclipse2013/gunsoulBI/Server/webroot/WEB-INF/classes/configWorld.properties");
        // System.out.println(PropertiesUtil.getFileIO("name", "gxyTest.properties"));
    }

    // 根据key读取value
    public static String readValue(String filePath, String key) {
        Properties props = new Properties();
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(filePath));
            props.load(in);
            String value = props.getProperty(key);
            // System.out.println(key + value);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 读取properties的全部信息
    @SuppressWarnings({ "rawtypes", "unused"})
    public static void readProperties(String filePath) {
        Properties props = new Properties();
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(filePath));
            props.load(in);
            Enumeration en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String Property = props.getProperty(key);
                // System.out.println(key + Property);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 写入properties信息
    public static void writeProperties(String filePath, String parameterName, String parameterValue) {
        Properties prop = new Properties();
        InputStream fis = null;
        OutputStream fos = null;
        try {
            fis = new FileInputStream(filePath);
            // 从输入流中读取属性列表（键和元素对）
            prop.load(fis);
            // 调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            fos = new FileOutputStream(filePath);
            prop.setProperty(parameterName, parameterValue);
            // 以适合使用 load 方法加载到 Properties 表中的格式，
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流
            // prop.store(fos, "Update '" + parameterName + "' value");
            prop.store(fos, null);
        } catch (IOException e) {
            // System.err.println("Visit " + filePath + " for updating " + parameterName + " value error");
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
