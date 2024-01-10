package com.baiyi.opscloud.common.holder;

import com.baiyi.opscloud.domain.generator.opscloud.UserToken;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author baiyi
 * @Date 2020/1/7 1:52 下午
 * @Version 1.0
 */
public class SessionHolder {

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
        return isAdmin.get() != null && isAdmin.get();
    }

    public static String getUsername() {
        return username.get();
    }

    public static boolean equalsUsername(String name) {
        if (StringUtils.isEmpty(name)) {
            return false;
        }
        return name.equals(username.get());
    }

    public static boolean isEmpty() {
        return StringUtils.isEmpty(username.get());
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