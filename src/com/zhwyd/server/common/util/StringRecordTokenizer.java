package com.zhwyd.server.common.util;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;
import org.apache.commons.lang3.time.DateUtils;
public class StringRecordTokenizer {
    private final String SEPARATOR = Pattern.quote("|");
    private String[]     tokens;

    public StringRecordTokenizer(String str) {
        tokens = str.split(SEPARATOR);
    }

    public String getString(int i) {
        return tokens[i].replaceAll("^.+=:", "");
    }

    public Integer getInteger(int i) {
        return Integer.valueOf(getString(i));
    }

    public Date getDate(int i) {
        return getDate(i, "yyyy-MM-dd HH:mm:ss");
    }

    public Date getDate(int i, String format) {
        try {
            return DateUtils.parseDate(getString(i), format);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
