package com.baiyi.opscloud.zabbix.helper.impl;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.helper.ZabbixGroupHelper;
import com.baiyi.opscloud.zabbix.ZabbixUtil;
import com.baiyi.opscloud.zabbix.v5.drive.ZabbixV5HostGroupDrive;
import com.baiyi.opscloud.zabbix.v5.drive.ZabbixV5UserGroupDrive;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixUserGroup;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/12 9:24 上午
 * @Version 1.0
 */
@Component
public class ZabbixGroupHelperImpl implements ZabbixGroupHelper {

    @Resource
    private ZabbixV5UserGroupDrive zabbixV5UserGroupDatasource;

    @Resource
    private ZabbixV5HostGroupDrive zabbixV5HostGroupDatasource;

    /**
     * 查询并创建用户组
     *
     * @param zabbix
     * @param usergroupName
     * @return
     */
    @Override
    public ZabbixUserGroup.UserGroup getOrCreateUserGroup(ZabbixConfig.Zabbix zabbix, String usergroupName) {
        com.baiyi.opscloud.zabbix.v5.entity.ZabbixUserGroup.UserGroup userGroup = zabbixV5UserGroupDatasource.getByName(zabbix, usergroupName);
        if (userGroup != null) return userGroup;
        // 用户组不存在
        String hostgroup = ZabbixUtil.toHostgroupName(usergroupName);
        com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup.HostGroup hostGroup = getOrCreateHostGroup(zabbix, hostgroup);
        return zabbixV5UserGroupDatasource.create(zabbix, usergroupName, hostGroup);
    }

    @Override
    public com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup.HostGroup getOrCreateHostGroup(ZabbixConfig.Zabbix zabbix, String hostgroupName) {
        com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup.HostGroup hostGroup = zabbixV5HostGroupDatasource.getByName(zabbix, hostgroupName);
        if (hostGroup != null) return hostGroup;
        zabbixV5HostGroupDatasource.create(zabbix, hostgroupName);
        return zabbixV5HostGroupDatasource.getByName(zabbix, hostgroupName);
    }
}
