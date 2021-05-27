package com.baiyi.caesar.common.util;

import com.baiyi.caesar.domain.generator.caesar.UserToken;

/**
 * @Author baiyi
 * @Date 2020/1/7 1:52 下午
 * @Version 1.0
 */
public class SessionUtil {

    private static ThreadLocal<String> username = new ThreadLocal<>();

    private static ThreadLocal<String> token = new ThreadLocal<>();

    public static void setUserToken(UserToken userToken){
        username.set(userToken.getUsername());
        token.set(userToken.getToken());
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
