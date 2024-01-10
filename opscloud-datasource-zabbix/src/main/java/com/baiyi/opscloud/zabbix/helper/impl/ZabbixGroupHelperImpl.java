package com.baiyi.opscloud.zabbix.helper.impl;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.ZabbixUtil;
import com.baiyi.opscloud.zabbix.helper.ZabbixGroupHelper;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5HostGroupDriver;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5UserGroupDriver;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixUserGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/8/12 9:24 上午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ZabbixGroupHelperImpl implements ZabbixGroupHelper {

    private final ZabbixV5UserGroupDriver zabbixV5UserGroupDrive;

    private final ZabbixV5HostGroupDriver zabbixV5HostGroupDrive;

    /**
     * 查询并创建用户组
     *
     * @param zabbix
     * @param usergroupName
     * @return
     */
    @Override
    public ZabbixUserGroup.UserGroup getOrCreateUserGroup(ZabbixConfig.Zabbix zabbix, String usergroupName) {
        com.baiyi.opscloud.zabbix.v5.entity.ZabbixUserGroup.UserGroup userGroup = zabbixV5UserGroupDrive.getByName(zabbix, usergroupName);
        if (userGroup != null) {
            return userGroup;
        }
        // 用户组不存在
        String hostgroup = ZabbixUtil.toHostgroupName(usergroupName);
        com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup.HostGroup hostGroup = getOrCreateHostGroup(zabbix, hostgroup);
        return zabbixV5UserGroupDrive.create(zabbix, usergroupName, hostGroup);
    }

    @Override
    public com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup.HostGroup getOrCreateHostGroup(ZabbixConfig.Zabbix zabbix, String hostgroupName) {
        com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup.HostGroup hostGroup = zabbixV5HostGroupDrive.getByName(zabbix, hostgroupName);
        if (hostGroup != null) {
            return hostGroup;
        }
        zabbixV5HostGroupDrive.create(zabbix, hostgroupName);
        return zabbixV5HostGroupDrive.getByName(zabbix, hostgroupName);
    }

}