package com.baiyi.opscloud.zabbix.helper;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixUserGroup;

/**
 * @Author baiyi
 * @Date 2021/8/12 9:24 上午
 * @Version 1.0
 */
public interface ZabbixGroupHelper {

    /**
     * 查询并创建用户组
     * @param zabbix
     * @param usergroupName
     * @return
     */
    ZabbixUserGroup.UserGroup getOrCreateUserGroup(ZabbixConfig.Zabbix zabbix, String usergroupName);

    /**
     * 查询并创建主机组
     * @param zabbix
     * @param hostgroupName
     * @return
     */
    ZabbixHostGroup.HostGroup getOrCreateHostGroup(ZabbixConfig.Zabbix zabbix, String hostgroupName);

}