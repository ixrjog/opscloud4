package com.baiyi.opscloud.datasource.business.account.impl.base;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.datasource.business.account.impl.ZabbixAccountHandler;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.zabbix.helper.ZabbixGroupHelper;
import com.baiyi.opscloud.zabbix.ZabbixUtil;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixUserGroup;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/8/24 9:56 上午
 * @Version 1.0
 */
public abstract class AbstractZabbixAccountHandler extends AbstractAccountHandler {

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private ZabbixGroupHelper zabbixFacade;

    protected static ThreadLocal<ZabbixConfig.Zabbix> configContext = new ThreadLocal<>();

    @Override
    protected void initialConfig(DatasourceConfig dsConfig) {
        configContext.set(dsConfigManager.build(dsConfig, ZabbixConfig.class).getZabbix());
    }

    protected List<Map<String, String>> getUsrgrps(ZabbixConfig.Zabbix zabbix, User user) {
        List<Map<String, String>> userGroups = toUsrgrps(zabbix, queryUserPermission(user, BusinessTypeEnum.SERVERGROUP.getType()));
        if (CollectionUtils.isEmpty(userGroups)) {
            userGroups.add(buildUsrgrp(zabbixFacade.getOrCreateUserGroup(zabbix, ZabbixAccountHandler.ZABBIX_DEFAULT_USERGROUP).getUsrgrpid()));
        }
        return userGroups;
    }

    protected List<Map<String, String>> toUsrgrps(ZabbixConfig.Zabbix zabbix, List<UserPermission> userPermissions) {
        return userPermissions.stream().map(e -> {
            ServerGroup serverGroup = serverGroupService.getById(e.getBusinessId());
            String usergroupName = ZabbixUtil.toUsergrpName(serverGroup.getName());
            ZabbixUserGroup.UserGroup userGroup = zabbixFacade.getOrCreateUserGroup(zabbix, usergroupName);
            return buildUsrgrp(userGroup.getUsrgrpid());
        }).collect(Collectors.toList());
    }

    protected Map<String, String> buildUsrgrp(String usrgrpId) {
        Map<String, String> usrgrp = Maps.newHashMap();
        usrgrp.put("usrgrpid", usrgrpId);
        return usrgrp;
    }

    @Override
    protected int getBusinessResourceType() {
        return BusinessTypeEnum.SERVERGROUP.getType();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ZABBIX.getName();
    }
}
