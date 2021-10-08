package com.baiyi.opscloud.datasource.util;

import com.baiyi.opscloud.datasource.util.enums.TimeZoneEnum;
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

    public static Date toGmtDate(String time, TimeZoneEnum timeZoneEnum) {
        if (StringUtils.isEmpty(time)) return new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(timeZoneEnum.getFormat());
        formatter.setTimeZone(TimeZone.getTimeZone(timeZoneEnum.name()));
        try {
            return formatter.parse(time);
        } catch (ParseException e) {
            return new Date();
        }
    }

}
