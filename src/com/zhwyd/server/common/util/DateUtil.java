package com.zhwyd.server.common.util;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
/**
 * 处理日期基类
 */
public class DateUtil {
    public final static String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String DEFAULT_DAY_FORMAT  = "yyyy-MM-dd";
    public final static String DEFAULT_ZONE_ID     = "Asia/Hong_Kong";

    /**
     * 获取当日减一个月的日期
     * 
     * @return
     */
    public static String getLastMonthDay() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        return df.format(c.getTime()); // 如果起如日期为空，默认为今天
    }

    /**
     * 将String 类型字段转成 Date
     * 
     * @param strDate
     *            日期字符串
     * @return
     */
    public static Date parseDate(String strDate) {
        try {
            if (strDate == null || strDate.length() < 1) return null;
            try {
                return DateUtils.parseDate(strDate, DEFAULT_TIME_FORMAT);
            } catch (ParseException ex) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将Date类型字段转成String
     * 
     * @param date
     *            日期date
     * @return
     */
    public static String format(Date date) {
        if (date == null) return null;
        return DateFormatUtils.format(date, DEFAULT_DAY_FORMAT);
    }

    /**
     * 获得某月的第一天yyyy-MM-dd
     * 
     * @param date
     *            日期date
     * @return
     */
    public static String getFirstDayOfMonth(Date date) {
        if (date == null) return null;
        Date firstOfDay = DateUtils.setDays(date, 1);
        return DateFormatUtils.format(firstOfDay, DEFAULT_DAY_FORMAT);
    }

    /**
     * 获得某月的最后一天yyyy-MM-dd
     * 
     * @param date
     *            日期date
     * @return
     */
    public static String getLastDayOfMonth(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return DateFormatUtils.format(calendar.getTime(), DEFAULT_DAY_FORMAT);
    }

    /**
     * 获得周一的日期
     * 
     * @param date
     *            日期date
     * @return
     */
    public static String getFirstDayOfWeek(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return DateFormatUtils.format(calendar.getTime(), DEFAULT_DAY_FORMAT);
    }

    /**
     * 获得周日的日期
     * 
     * @param date
     *            日期date
     * @return
     */
    public static String getLastDayOfWeek(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        calendar.add(Calendar.DATE, 7 - dayOfWeek);
        return DateFormatUtils.format(calendar.getTime(), DEFAULT_DAY_FORMAT);
    }

    /**
     * 時間戳轉Date
     * 
     * @param date
     *            日期date
     * @return
     * @throws ParseException
     */
    public static Date getDateByTime(Long time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = format.format(time);
        Date date = format.parse(d);
        return date;
    }
}
