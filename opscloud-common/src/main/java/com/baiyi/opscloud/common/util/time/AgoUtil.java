package com.baiyi.opscloud.common.util.time;

import com.baiyi.opscloud.domain.vo.base.ReadableTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/5/7 2:19 下午
 * @Version 1.0
 */
public class AgoUtil {

    private AgoUtil() {
    }

    private static final long ONE_MINUTE = 60 * 1000L;
    private static final long ONE_HOUR = ONE_MINUTE * 60;
    private static final long ONE_DAY = ONE_HOUR * 24;
    private static final long ONE_WEEK = ONE_DAY * 7;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "个月前";
    private static final String ONE_YEAR_AGO = "年前";
    private static final String YESTERDAY = "昨天";

    public static String format(String gmtDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
        try {
            Date d = format.parse(gmtDate);
            return format(d);
        } catch (Exception e) {
            return gmtDate;
        }
    }

    public static void wrap(ReadableTime.IAgo iAgo) {
        if (iAgo.getAgoTime() == null) {
            return;
        }
        iAgo.setAgo(format(iAgo.getAgoTime()));
    }

    public static String format(Date date) {
        long delta = System.currentTimeMillis() - date.getTime();
        if (delta < ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return YESTERDAY;
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }

}