package com.zhwyd.server.common.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 命令行输入工具类
 * @author rL.
 *
 */
public class InputData {
    private BufferedReader buf = null;

    public InputData() {
        this.buf = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * 从控制台获取数字
     * @param info 提示信息
     * @param err  错误信息
     * @return 用户输入的数字
     */
    public int getInt(String info, String err) {
        int i = 0;
        boolean flag = true;
        // 直到用户输入的是数字才跳出循环
        while (flag) {
            String str = this.getString(info);
            // 如果是数字
            if (str.matches("\\d+")) {
                i = Integer.parseInt(str);
                flag = false;
            } else {
                // 输出错误信息
                System.out.print(err);
            }
        }
        return i;
    }

    /**
     * 从控制台获取一行字符串
     * @param info 提示信息
     * @return
     */
    public String getString(String info) {
        String str = null;
        System.out.print(info);
        try {
            str = this.buf.readLine();
        } catch (IOException e) {
        }
        return str;
    }

    /**
     * @param info
     * @param err
     * @return
     */
    public Date getDate(String info, String err) {
        Date date = null;
        boolean flag = true;
        // 直到用户输入的是日期格式才跳出循环
        while (flag) {
            String str = this.getString(info);
            // 日期格式为yyyy-MM-dd
            if (str.matches("\\d{4}-\\d{2}-\\d{2}")) {
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
                    flag = false;
                } catch (ParseException e) {
                }
            } else {
                // 输出错误信息
                System.out.print(err);
            }
        }
        return date;
    }
}
