package com.baiyi.opscloud.zabbix.facade;

import com.baiyi.opscloud.common.datasource.ZabbixDsInstanceConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserGroup;

/**
 * @Author baiyi
 * @Date 2021/8/12 9:24 上午
 * @Version 1.0
 */
public interface ZabbixFacade {

    /**
     * 查询并创建用户组
     * @param zabbix
     * @param usergroup
     * @return
     */
    ZabbixUserGroup getOrCreateUserGroup(ZabbixDsInstanceConfig.Zabbix zabbix, String usergroup);

    /**
     * 查询并创建主机组
     * @param zabbix
     * @param hostgroup
     * @return
     */
    ZabbixHostGroup getOrCreateHostGroup(ZabbixDsInstanceConfig.Zabbix zabbix, String hostgroup);
}
