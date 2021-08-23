package com.baiyi.opscloud.zabbix.facade.impl;

import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserGroup;
import com.baiyi.opscloud.zabbix.facade.ZabbixFacade;
import com.baiyi.opscloud.zabbix.handler.ZabbixHostGroupHandler;
import com.baiyi.opscloud.zabbix.handler.ZabbixUserGroupHandler;
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
    private ZabbixUserGroupHandler zabbixUserGroupHandler;

    @Resource
    private ZabbixHostGroupHandler zabbixHostGroupHandler;

    /**
     * 查询并创建用户组
     *
     * @param zabbix
     * @param usergroup
     * @return
     */
    @Override
    public ZabbixUserGroup getOrCreateUserGroup(DsZabbixConfig.Zabbix zabbix, String usergroup) {
        ZabbixUserGroup zabbixUserGroup = zabbixUserGroupHandler.getByName(zabbix, usergroup);
        if (zabbixUserGroup != null) return zabbixUserGroup;
        // 用户组不存在
        String hostgroup = ZabbixUtil.toHostgroupName(usergroup);
        ZabbixHostGroup zabbixHostGroup = getOrCreateHostGroup(zabbix, hostgroup);
        return zabbixUserGroupHandler.create(zabbix, usergroup, zabbixHostGroup);
    }

    @Override
    public ZabbixHostGroup getOrCreateHostGroup(DsZabbixConfig.Zabbix zabbix, String hostgroup) {
        ZabbixHostGroup zabbixHostGroup = zabbixHostGroupHandler.getByName(zabbix, hostgroup);
        if (zabbixHostGroup != null) return zabbixHostGroup;
        return zabbixHostGroupHandler.create(zabbix, hostgroup);
    }
}
