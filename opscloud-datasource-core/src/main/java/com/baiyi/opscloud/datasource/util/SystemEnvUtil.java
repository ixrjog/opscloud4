package com.baiyi.opscloud.datasource.util;

/**
 * @Author baiyi
 * @Date 2021/6/25 5:30 下午
 * @Version 1.0
 */
public class SystemEnvUtil {

    private SystemEnvUtil() {
    }

    public static String renderEnvHome(String str) {
        return str.replace("${HOME}", System.getenv("HOME"));
    }
}
