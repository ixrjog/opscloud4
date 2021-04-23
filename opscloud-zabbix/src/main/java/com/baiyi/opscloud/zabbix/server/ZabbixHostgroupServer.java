package com.baiyi.opscloud.zabbix.server;

import com.baiyi.opscloud.zabbix.entry.ZabbixHostgroup;

/**
 * @Author baiyi
 * @Date 2020/1/8 1:06 下午
 * @Version 1.0
 */
public interface ZabbixHostgroupServer {

    // hostgroup
    ZabbixHostgroup createHostgroup(String hostgroup);

    ZabbixHostgroup getHostgroup(String hostgroup);
}
