package com.baiyi.caesar.common.util;

/**
 * @Author baiyi
 * @Date 2020/12/21 4:51 下午
 * @Version 1.0
 */
public class GitlabTokenUtil {

    private static ThreadLocal<String> token = new ThreadLocal<>();

    public static String getToken() {
        return token.get();
    }

    public static void setToken(String param) {
        token.set(param);
    }
}
