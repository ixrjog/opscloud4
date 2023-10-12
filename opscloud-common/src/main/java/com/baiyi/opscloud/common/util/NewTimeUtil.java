package com.baiyi.opscloud.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2023/3/16 10:42
 * @Version 1.0
 */
public class NewTimeUtil {

    private NewTimeUtil() {
    }

    /**
     * 秒时间戳
     */
    public static final long SECOND_TIME = 1000L;

    /**
     * 分钟时间戳
     */
    public static final long MINUTE_TIME = 60 * SECOND_TIME;

    /**
     * 小时时间戳
     */
    public static final long HOUR_TIME = 60 * MINUTE_TIME;

    /**
     * 天时间戳
     */
    public static final long DAY_TIME = 24 * HOUR_TIME;

    public static final String ISO8601 = "yyyy-MM-dd'T'HH:mm.SSS'Z'";

    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String TIME = "HH:mm:ss";

    public static Date parse(String str, String fmt) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(fmt);
            return format.parse(str);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static Date parse(String str) {
        return parse(str, TIME_FORMAT);
    }

    public static String parse(Date date, String fmt) {
        SimpleDateFormat formatter = new SimpleDateFormat(fmt);
        return formatter.format(date);
    }

    public static String parse(Date date) {
        return parse(date, TIME_FORMAT);
    }



    /**
     * 获取当前时间
     *
     * @return
     */
    public static String nowDate() {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat(TIME_FORMAT);
        return formatter.format(new Date());
    }

    /**
     * 判断是否超时
     * @param startTime
     * @param timeout (毫秒)
     * @return
     */
    public static boolean isTimeout(long startTime, long timeout) {
        return (System.currentTimeMillis() - startTime) >= timeout;
    }

    public static String toRuntime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
        return formatter.format(time);
    }

    public static void sleep(long s) {
        try {
            TimeUnit.SECONDS.sleep(s);
        } catch (InterruptedException ignored) {
        }
    }

    public static void millisecondsSleep(long m) {
        try {
            TimeUnit.MILLISECONDS.sleep(m);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Calculate how many seconds
     * @param date
     * @return
     */
    public static long calculateHowManySecondsHavePassed(Date date) {
        long subTime = System.currentTimeMillis() - date.getTime();
        if (subTime < 0) {
            return 0;
        }
        return subTime / NewTimeUtil.SECOND_TIME;
    }

}