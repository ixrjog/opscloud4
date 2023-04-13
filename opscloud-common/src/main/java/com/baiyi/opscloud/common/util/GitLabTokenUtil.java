package com.baiyi.opscloud.common.util;

/**
 * @Author baiyi
 * @Date 2020/12/21 4:51 下午
 * @Version 1.0
 */
public class GitLabTokenUtil {

    private static final ThreadLocal<String> TOKEN = new ThreadLocal<>();

    public static String getToken() {
        return TOKEN.get();
    }

    public static void setToken(String param) {
        TOKEN.set(param);
    }

}
