package com.baiyi.opscloud.common.util;

import org.springframework.util.Assert;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author 修远
 * @Date 2021/6/28 10:10 上午
 * @Since 1.0
 */
public class StringToDurationUtil {

    private static final Pattern ISO8601 = Pattern.compile("^[\\+\\-]?P.*$");
    private static final Pattern SIMPLE = Pattern.compile("^([\\+\\-]?\\d+)([a-zA-Z]{0,2})$");
    private static final Map<String, ChronoUnit> UNITS;

    private StringToDurationUtil() {
    }

    static {
        UNITS = Map.of("us", ChronoUnit.MICROS, "ns", ChronoUnit.NANOS, "ms", ChronoUnit.MILLIS, "s", ChronoUnit.SECONDS, "m", ChronoUnit.MINUTES, "h", ChronoUnit.HOURS, "d", ChronoUnit.DAYS, "", ChronoUnit.MILLIS);
    }

    public static Duration parse(String source) {
        try {
            if (ISO8601.matcher(source).matches()) {
                return Duration.parse(source);
            } else {
                Matcher matcher = SIMPLE.matcher(source);
                Assert.state(matcher.matches(), "'" + source + "' is not a valid duration");
                long amount = Long.parseLong(matcher.group(1));
                ChronoUnit unit = getUnit(matcher.group(2));
                return Duration.of(amount, unit);
            }
        } catch (Exception var6) {
            throw new IllegalStateException("'" + source + "' is not a valid duration", var6);
        }
    }

    private static ChronoUnit getUnit(String value) {
        ChronoUnit unit = UNITS.get(value.toLowerCase());
        Assert.state(unit != null, "Unknown unit '" + value + "'");
        return unit;
    }

}