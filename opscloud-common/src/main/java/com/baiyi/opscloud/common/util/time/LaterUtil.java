package com.baiyi.opscloud.common.util.time;

import com.baiyi.opscloud.domain.vo.base.ReadableTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2021/8/5 10:16 上午
 * @Version 1.0
 */
public class LaterUtil {

    private LaterUtil() {
    }

    private static final long ONE_MINUTE = 60 * 1000L;
    private static final long ONE_HOUR = ONE_MINUTE * 60;
    private static final long ONE_DAY = ONE_HOUR * 24;
    private static final long ONE_WEEK = ONE_DAY * 7;

    private static final String ONE_SECOND_LATER = "秒后";
    private static final String ONE_MINUTE_LATER = "分钟后";
    private static final String ONE_HOUR_LATER = "小时后";
    private static final String ONE_DAY_LATER = "天后";
    private static final String ONE_MONTH_LATER = "个月后";
    private static final String ONE_YEAR_LATER = "年后";
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

    public static void wrap(ReadableTime.ILater iLater) {
        if (iLater.getExpiredTime() == null) {
            return;
        }
        iLater.setLater(format(iLater.getExpiredTime()));
    }

    public static String format(Date date) {
        long delta = date.getTime() - System.currentTimeMillis();
        if (delta < ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_LATER;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_LATER;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_LATER;
        }
        if (delta < 48L * ONE_HOUR) {
            return YESTERDAY;
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_LATER;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_LATER;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_LATER;
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