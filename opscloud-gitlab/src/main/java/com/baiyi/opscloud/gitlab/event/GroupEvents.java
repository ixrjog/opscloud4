package com.baiyi.opscloud.gitlab.event;

/**
 * @Author baiyi
 * @Date 2020/1/14 4:24 下午
 * @Version 1.0
 */
public class GroupEvents {

    public static final String[] GROUP_EVENTS = {
            "group_create",
            "group_destroy",
            "group_rename",
            "user_add_to_group",
            "user_remove_from_group"};
}
