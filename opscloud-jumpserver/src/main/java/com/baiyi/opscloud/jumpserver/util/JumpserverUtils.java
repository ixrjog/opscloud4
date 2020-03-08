package com.baiyi.opscloud.jumpserver.util;

/**
 * @Author baiyi
 * @Date 2020/3/8 5:03 下午
 * @Version 1.0
 */
public class JumpserverUtils {

    // 用户组前缀
    public static final String USERGROUP_PREFIX = "usergroup_";
    public static final String SERVERGROUP_PREFIX = "group_";

    public static String toUsergroupName(String serverGroupName){
        return serverGroupName.replace(SERVERGROUP_PREFIX, USERGROUP_PREFIX);
    }
}
