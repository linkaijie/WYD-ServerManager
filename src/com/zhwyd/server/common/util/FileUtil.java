package com.zhwyd.server.common.util;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class FileUtil {
    /**
     * 像指定路径文件写入内容
     * @param path      指定路径
     * @param content   写入内容
     * @param append   是否以追加形式
     */
    public static void writeFile(String path, String content, boolean append) {
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(path, append);
            writer.write(content);
            writer.write("\r\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 像指定路径文件写入内容
     * @param path      指定路径
     * @param content   写入内容
     */
    public static void writeFile(String path, String content) {
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(path, true);
            writer.write(content);
            writer.write("\r\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取指定路径的内容
     * @param path
     * @return
     */
    public static String readFile(String path) {
        StringBuffer result = new StringBuffer();
        try {
            BufferedReader read = new BufferedReader(new FileReader(path));
            String line = null;
            while ((line = read.readLine()) != null) {
                result.append(line);
                result.append("\r\n");
            }
            read.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
