package com.baiyi.opscloud.common.util;

import org.slf4j.helpers.MessageFormatter;

/**
 * @Author baiyi
 * @Date 2023/7/14 13:23
 * @Version 1.0
 */
public class StringFormatter {

    public static String arrayFormat(String str, Object... args) {
        return MessageFormatter.arrayFormat(str, args).getMessage();
    }

    public static String format(String str, Object arg) {
        return MessageFormatter.format(str, arg).getMessage();
    }

}
