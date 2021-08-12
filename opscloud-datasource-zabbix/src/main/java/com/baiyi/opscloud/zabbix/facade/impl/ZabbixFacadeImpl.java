package com.baiyi.opscloud.zabbix.facade.impl;

import com.baiyi.opscloud.zabbix.facade.ZabbixFacade;
import com.baiyi.opscloud.zabbix.handler.ZabbixUserGroupHandler;
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


    /**
     * 查询并创建用户组
     *
     * @param zabbix
     * @param usergroup
     * @return
     */
//    public ZabbixUserGroup getOrCreateGroup(DsZabbixConfig.Zabbix zabbix, String usergroup) {
//        ZabbixUserGroup zabbixUserGroup = zabbixUserGroupHandler.getGroupByName(zabbix, usergroup);
//        if (zabbixUserGroup != null) return zabbixUserGroup;
//        // 用户组不存在
//        String hostgroup = ZabbixUtil.toHostgroupName(usergroup);
//
//    }
}
