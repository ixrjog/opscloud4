package com.baiyi.opscloud.datasource.account.impl;

import com.baiyi.opscloud.common.datasource.ZabbixDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.account.convert.AccountConvert;
import com.baiyi.opscloud.datasource.account.impl.base.BaseAccountProvider;
import com.baiyi.opscloud.zabbix.util.ZabbixMediaUtil;
import com.baiyi.opscloud.zabbix.util.ZabbixUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserGroup;
import com.baiyi.opscloud.zabbix.handler.ZabbixUserGroupHandler;
import com.baiyi.opscloud.zabbix.handler.ZabbixUserHandler;
import com.google.common.collect.Maps;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/8/11 4:31 下午
 * @Version 1.0
 */
public class ZabbixAccountProvider extends BaseAccountProvider<DsZabbixConfig.Zabbix> {

    @Resource
    private ZabbixUserHandler zabbixUserHandler;

    @Resource
    private ZabbixUserGroupHandler zabbixUserGroupHandler;

    @Resource
    private ServerGroupService serverGroupService;

    public static final String ZABBIX_DEFAULT_USERGROUP = "users_default";

    @Override
    protected void doCreate(DsZabbixConfig.Zabbix zabbix, User user) {
        zabbixUserHandler.createUser(zabbix, AccountConvert.toZabbixUser(user), ZabbixMediaUtil.buildMedias(user), getUsrgrps(zabbix, user));
    }

    @Override
    protected void doUpdate(DsZabbixConfig.Zabbix zabbix, User user) {
        // personRepo.update(ldap, AccountConvert.toLdapPerson(user));
    }

    @Override
    protected void doDelete(DsZabbixConfig.Zabbix zabbix, User user) {
        // personRepo.delete(ldap, user.getUsername());
    }

    @Override
    protected DsZabbixConfig.Zabbix buildConfig(DatasourceConfig dsConfig) {
        return dsConfigFactory.build(dsConfig, ZabbixDsInstanceConfig.class).getZabbix();
    }

    private List<Map<String, String>> getUsrgrps(DsZabbixConfig.Zabbix zabbix, User user) {
        List<Map<String, String>> userGroups = queryUserPermission(user, BusinessTypeEnum.SERVERGROUP.getType()).stream().map(e -> {
            ServerGroup serverGroup = serverGroupService.getById(e.getBusinessId());
            String usergrpName = ZabbixUtil.toUsergrpName(serverGroup.getName());
            // 此处要判断是否为空
            ZabbixUserGroup userGroup = zabbixUserGroupHandler.getGroupByName(zabbix, usergrpName);
            Map<String, String> usrgrp = Maps.newHashMap();
            usrgrp.put("usrgrpid", userGroup.getUserGroupId());
            return usrgrp;
        }).collect(Collectors.toList());
        Map<String, String> defUsrgrp = Maps.newHashMap();
        defUsrgrp.put("usrgrpid", zabbixUserGroupHandler.getGroupByName(zabbix, ZABBIX_DEFAULT_USERGROUP).getUserGroupId());
        userGroups.add(defUsrgrp);
        return userGroups;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.LDAP.getName();
    }
}
