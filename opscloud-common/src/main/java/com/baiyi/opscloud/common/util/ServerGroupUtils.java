package com.baiyi.opscloud.common.util;

import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;

/**
 * @Author baiyi
 * @Date 2021/2/25 9:41 上午
 * @Version 1.0
 */
public class ServerGroupUtils {

    private ServerGroupUtils(){}

    private final static String NAME_PREFIX = "group_";

    public static String getShortName(OcServerGroup ocServerGroup){
        return getShortName(ocServerGroup.getName());
    }

    public static String getShortName(String serverGroupName){
        return serverGroupName.replace(NAME_PREFIX, "");
    }
}
