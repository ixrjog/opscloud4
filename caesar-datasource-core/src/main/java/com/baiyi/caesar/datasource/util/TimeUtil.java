package com.baiyi.caesar.datasource.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/6/18 3:50 下午
 * @Version 1.0
 */
public class TimeUtil {

    public interface Format {
        String UTC = "yyyy-MM-dd'T'HH:mm'Z'";
    }

    public static Date toGmtDate(String time,String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        try {
            return format.parse(time);
        } catch (ParseException e) {
            return new Date();
        }
    }
}
