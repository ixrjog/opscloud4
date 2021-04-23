package com.baiyi.opscloud.common.util;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/1/7 1:52 下午
 * @Version 1.0
 */
@Slf4j
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
     * 判断是否超时
     *
     * @param startTime
     * @return
     */
    public static boolean checkTimeout(Long startTime, Long timeout) {
        return (new Date().getTime() - startTime) >= timeout;
    }


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

    /**
     * 计算时间经过了多少分钟
     *
     * @param date
     * @return
     */
    public static int calculateDateAgoMinute(Date date) {
        long subTime = new Date().getTime() - date.getTime();
        if (subTime < 0) {
            throw new RuntimeException("计算时间有误!");
        }
        Long diff = subTime / minuteTime;
        return diff.intValue();
    }


    /**
     * 计算两个时间的时间戳差
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static Long calculateDateDiff4Day(String startTime, String endTime) {
        try {
            long time = TimeUtils.dateToStamp2(endTime) - TimeUtils.dateToStamp2(startTime);
            if (time < 0) {
                throw new RuntimeException("开始时间不能大于结束时间");
            }
            return time;
        } catch (ParseException e) {
            log.error(e.getMessage());
            return 0L;
        }
    }


    /**
     * 计算时间已经过多少天
     *
     * @param fromDate
     * @return
     */
    public static int calculateDateDiff4Day(String fromDate) {
        try {
            long subTime = new Date().getTime() - dateToStamp(fromDate);
            if (subTime < 0)
                return 0;
            Long diff = subTime / dayTime;
            return diff.intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 计算时间是否过期
     *
     * @param expired
     * @return
     */
    public static boolean calculateDateExpired(String expired) {
        try {
            long subTime = dateToStamp(expired) - new Date().getTime();
            return subTime <= 0;
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean calculateDateExpired(Date date) {
        try {
            long subTime = date.getTime() - new Date().getTime();
            return subTime <= 0;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 将时间转换为时间戳
     */
    public static long dateToStamp(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        return date.getTime();
    }

    public static long dateToStamp2(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = simpleDateFormat.parse(s);
        return date.getTime();
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

    public static String gmtNowDate() {
        long time = new Date().getTime();
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = formatter.format(time - TimeUtils.hourTime * 8);
        return now;
    }

    /**
     * 获取用于名称的当前时间
     *
     * @return eg: 20181110_003738
     */
    public static String nowDateName() {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
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
            return true;
        } else {
            return false;
        }
    }

    /**
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

    /**
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

    /**
     * 将时间戳转换为时间去零到分钟
     *
     * @param s
     * @return
     */
    public static String stampToDateMin(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long lt = new Long(s);
        Date date = new Date(lt);
        return simpleDateFormat.format(date);
    }

    /**
     * 计算到期天数
     *
     * @param expiredTime "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static long expiredDay(String expiredTime) {
        try {
            long et = dateToStamp(expiredTime);
            Date d = new Date();
            return (et - d.getTime()) / TimeUtils.dayTime;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 计算未来时间
     *
     * @param day 往后几天
     * @return "yyyy-MM-dd HH:mm:ss"
     */
    public static String futureTime(int day) {
        long now = new Date().getTime();
        now = now + dayTime * day;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(now);
    }

    public static long futureTimeStamp(int day) {
        long now = new Date().getTime();
        now += dayTime * day;
        return now;
    }

    /**
     * 是否为今天
     *
     * @param date yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static boolean isToday(String date) {
        try {
            long stamp = dateToStamp(date);
            Date date1 = new Date(stamp);
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            if (fmt.format(date).toString().equals(fmt.format(new Date()).toString())) { //格式化为相同格式
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    public static Date gmtToDate(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(time);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static Date acqGmtDate(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        try {
            return format.parse(time);
        } catch (ParseException e) {
            return new Date();
        }
    }

    //转换时间
    public static String acqGmtTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        try {
            Date date = format.parse(time);
            SimpleDateFormat fmt;
            fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return fmt.format(date);
        } catch (ParseException e) {
            return "";
        }
    }

    //转换时间 2016-11-19T02:36:13Z
    public static String acqGmtTimePlus(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date date = format.parse(time);
            SimpleDateFormat fmt;
            fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return fmt.format(date);
        } catch (ParseException e) {
            return "";
        }
    }

    public static String dateToStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static Boolean isTimeOut(Date startTime, Integer minute) {
        return (startTime.getTime() + (minute * minuteTime)) < new Date().getTime();
    }

    public static String whatWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        //设置中国周
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);
        return Joiner.on("-").join(year, week);
    }

    public static Date acqGmtDateV2(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
            return format.parse(time);
        } catch (ParseException e) {
            return new Date();
        }
    }
}
