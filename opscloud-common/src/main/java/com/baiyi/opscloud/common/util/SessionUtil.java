package com.baiyi.opscloud.common.util;

import com.baiyi.opscloud.domain.generator.opscloud.UserToken;

/**
 * @Author baiyi
 * @Date 2020/1/7 1:52 下午
 * @Version 1.0
 */
public class SessionUtil {

    private static final ThreadLocal<String> username = new ThreadLocal<>();

    private static final ThreadLocal<String> token = new ThreadLocal<>();

    private static final ThreadLocal<Integer> userId = new ThreadLocal<>();

    private static final ThreadLocal<Boolean> isAdmin = new ThreadLocal<>();

    public static void setUserToken(UserToken userToken) {
        username.set(userToken.getUsername());
        token.set(userToken.getToken());
    }

    public static Integer getUserId() {
        return userId.get();
    }

    public static void setUserId(Integer param) {
        userId.set(param);
    }

    public static void setIsAdmin(Boolean param) {
        isAdmin.set(param);
    }

    public static Boolean getIsAdmin() {
        return isAdmin.get() == null ? false : isAdmin.get();
    }

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
