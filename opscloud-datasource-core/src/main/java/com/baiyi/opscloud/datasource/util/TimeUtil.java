package com.baiyi.opscloud.datasource.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @Author baiyi
 * @Date 2021/6/18 3:50 下午
 * @Version 1.0
 */
public class TimeUtil {

    public interface Format {
        String UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    }

    public static Date toGmtDate(String time, String dateFormat) {
        if (StringUtils.isEmpty(time)) return new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return formatter.parse(time);
        } catch (ParseException e) {
            return new Date();
        }
    }
}
