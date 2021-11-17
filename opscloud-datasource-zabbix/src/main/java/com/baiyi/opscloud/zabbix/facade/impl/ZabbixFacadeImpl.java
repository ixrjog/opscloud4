package com.baiyi.opscloud.zabbix.facade.impl;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserGroup;
import com.baiyi.opscloud.zabbix.facade.ZabbixFacade;
import com.baiyi.opscloud.zabbix.datasource.ZabbixHostGroupDatasource;
import com.baiyi.opscloud.zabbix.datasource.ZabbixUserGroupDatasource;
import com.baiyi.opscloud.zabbix.util.ZabbixUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/12 9:24 上午
 * @Version 1.0
 */
@Component
public class ZabbixFacadeImpl implements ZabbixFacade {

    @Resource
    private ZabbixUserGroupDatasource zabbixUserGroupDatasource;

    @Resource
    private ZabbixHostGroupDatasource zabbixHostGroupDatasource;

    /**
     * 查询并创建用户组
     *
     * @param zabbix
     * @param usergroup
     * @return
     */
    @Override
    public ZabbixUserGroup getOrCreateUserGroup(ZabbixConfig.Zabbix zabbix, String usergroup) {
        ZabbixUserGroup zabbixUserGroup = zabbixUserGroupDatasource.getByName(zabbix, usergroup);
        if (zabbixUserGroup != null) return zabbixUserGroup;
        // 用户组不存在
        String hostgroup = ZabbixUtil.toHostgroupName(usergroup);
        ZabbixHostGroup zabbixHostGroup = getOrCreateHostGroup(zabbix, hostgroup);
        return zabbixUserGroupDatasource.create(zabbix, usergroup, zabbixHostGroup);
    }

    @Override
    public ZabbixHostGroup getOrCreateHostGroup(ZabbixConfig.Zabbix zabbix, String hostgroup) {
        ZabbixHostGroup zabbixHostGroup = zabbixHostGroupDatasource.getByName(zabbix, hostgroup);
        if (zabbixHostGroup != null) return zabbixHostGroup;
        zabbixHostGroupDatasource.create(zabbix, hostgroup);
        return zabbixHostGroupDatasource.getByName(zabbix, hostgroup);
    }
}
