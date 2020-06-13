package com.baiyi.opscloud.common.util;

/**
 * @Author baiyi
 * @Date 2020/1/8 4:32 下午
 * @Version 1.0
 */
public class ZabbixUtils {

    /**
     * 服务器组名称转换为 zabbix 用户组名称
     * @param serverGroupName
     * @return
     */
    public static String convertUsergrpName(String serverGroupName) {
        return serverGroupName.replace("group_", "users_");
    }


    public static String convertHostgroupName(String usergrpName) {
        return usergrpName.replace( "users_","group_");
    }
}
