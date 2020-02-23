package com.baiyi.opscloud.common.util;

/**
 * @Author baiyi
 * @Date 2020/1/7 1:52 下午
 * @Version 1.0
 */
public class SessionUtils {

    private static ThreadLocal<String> username = new ThreadLocal<>();

    private static ThreadLocal<String> token = new ThreadLocal<>();

    public static String getUsername() {
        return username.get();
    }

    public static void setUsername(String param) {
        username.set(param);
    }

    public static String getToken() {
        return token.get();
    }

    public static void setToken(String param) {
        token.set(param);
    }
}
