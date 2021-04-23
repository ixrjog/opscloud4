package com.baiyi.opscloud.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/1/8 4:32 下午
 * @Version 1.0
 */
public class ZabbixUtils {

    public static final String HOSTId = "hostid";

    /**
     * 服务器组名称转换为 zabbix 用户组名称
     *
     * @param serverGroupName
     * @return
     */
    public static String convertUsergrpName(String serverGroupName) {
        return serverGroupName.replace("group_", "users_");
    }


    public static String convertHostgroupName(String usergrpName) {
        return usergrpName.replace("users_", "group_");
    }

    /**
     * {
     * "jsonrpc": "2.0",
     * "method": "host.massupdate",
     * "params": {
     * "hosts": [
     * {
     * "hostid": "69665"
     * },
     * {
     * "hostid": "69666"
     * }
     * ],
     * "status": 0
     * },
     * "auth": "038e1d7b1735c6a5436ee9eae095879e",
     * "id": 1
     * }
     *
     * @param hostids
     * @return
     */
    public static List<Map<String, String>> convertHosts(List<String> hostids) {
        List<Map<String, String>> hosts = Lists.newArrayList();
        hostids.forEach(id -> {
            Map<String, String> hostid = Maps.newHashMap();
            hostid.put(HOSTId, id);
            hosts.add(hostid);
        });
        return hosts;
    }
}
