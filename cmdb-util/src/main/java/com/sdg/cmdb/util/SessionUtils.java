package com.sdg.cmdb.util;

/**
 * Created by zxxiao on 16/9/28.
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
