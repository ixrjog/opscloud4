package com.baiyi.opscloud.gitlab.event;

/**
 * @Author baiyi
 * @Date 2020/1/14 4:25 下午
 * @Version 1.0
 */
public class UserEvents {
    public static final String[] USER_EVENTS = {
            "user_add_to_team",
            "user_remove_from_team",
            "user_create",
            "user_destroy",
            "user_failed_login",
            "user_rename"};
}
