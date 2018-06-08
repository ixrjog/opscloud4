package com.sdg.cmdb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zxxiao on 2016/10/20.
 */
public class TimeUtils {

    /**
     * 天时间戳
     */
    public static long dayTime = 24 * 60 * 60 * 1000;

    /**
     * 小时时间戳
     */
    public static long hourTime = 3600000;

    /**
     * 分钟时间戳
     */
    public static long minuteTime = 60000;

    /**
     * 秒时间戳
     */
    public static long secondTime = 1000;

    /**
     * 字符串时间格式
     */
    public static String timeFormat = "yyyy-MM-dd HH:mm:ss";

    /**
     * 计算2个日期时间差
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    public static String calculateDateDifference(Date fromDate, Date toDate) {
        long subTime = toDate.getTime() - fromDate.getTime();
        if (subTime < 0) {
            throw new RuntimeException("计算时间有误!");
        }
        StringBuffer buffer = new StringBuffer();
        long day = subTime / dayTime;
        if (day != 0) {
            buffer.append(day);
            buffer.append("天");
        }
        long hour = subTime % dayTime / hourTime;
        if (hour != 0) {
            buffer.append(hour);
            buffer.append("小时");
        }
        long minute = subTime % dayTime % hourTime / minuteTime;
        if (minute != 0) {
            buffer.append(minute);
            buffer.append("分钟");
        }
        if (day == 0 && hour == 0 && minute == 0) {
            buffer.append("0分钟");
        }

        return buffer.toString();
    }

    /**
     * 计算2个日期时间差
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    public static int calculateDateDiff4Day(Date fromDate, Date toDate) {
        long subTime = toDate.getTime() - fromDate.getTime();
        if (subTime < 0) {
            throw new RuntimeException("计算时间有误!");
        }

        Long diff = subTime / dayTime;
        return diff.intValue();
    }

    /*
    * 将时间转换为时间戳
    */
    public static long dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        return ts;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String nowDate() {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = formatter.format(new Date());
        return now;
    }


    /**
     * 判断时间是否过了stamp
     *
     * @param d     "yyyy-MM-dd HH:mm:ss"
     * @param stamp
     * @return
     * @throws ParseException
     */
    public static boolean timeLapse(String d, long stamp) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(TimeUtils.timeFormat);
        Date date = sdf.parse(d);
        long subTime = new Date().getTime() - date.getTime();
        if (subTime >= stamp) {
            return false;
        } else {
            return true;
        }
    }


    /*
    * 将时间戳(秒)转换为时间
    */
    public static String stampSecToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = (new Long(s)) * 1000;
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间戳转换为时间
    */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
