package com.baiyi.opscloud.datasource.account.impl.base;

import com.baiyi.opscloud.common.datasource.ZabbixDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserGroup;
import com.baiyi.opscloud.zabbix.facade.ZabbixFacade;
import com.baiyi.opscloud.zabbix.util.ZabbixUtil;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.datasource.account.impl.ZabbixAccountProvider.ZABBIX_DEFAULT_USERGROUP;

/**
 * @Author baiyi
 * @Date 2021/8/24 9:56 上午
 * @Version 1.0
 */
public abstract class BaseZabbixAccountProvider extends AbstractAccountProvider<DsZabbixConfig.Zabbix> {

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private ZabbixFacade zabbixFacade;

    protected List<Map<String, String>> getUsrgrps(DsZabbixConfig.Zabbix zabbix, User user) {
        List<Map<String, String>> userGroups = toUsrgrps(zabbix, queryUserPermission(user, BusinessTypeEnum.SERVERGROUP.getType()));
        if (CollectionUtils.isEmpty(userGroups))
            userGroups.add(buildUsrgrp(zabbixFacade.getOrCreateUserGroup(zabbix, ZABBIX_DEFAULT_USERGROUP).getUsrgrpid()));
        return userGroups;
    }

    protected List<Map<String, String>> toUsrgrps(DsZabbixConfig.Zabbix zabbix, List<UserPermission> userPermissions) {
        return userPermissions.stream().map(e -> {
            ServerGroup serverGroup = serverGroupService.getById(e.getBusinessId());
            String usergroupName = ZabbixUtil.toUsergrpName(serverGroup.getName());
            ZabbixUserGroup zabbixUserGroup = zabbixFacade.getOrCreateUserGroup(zabbix, usergroupName);
            return buildUsrgrp(zabbixUserGroup.getUsrgrpid());
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
    protected DsZabbixConfig.Zabbix buildConfig(DatasourceConfig dsConfig) {
        return dsConfigFactory.build(dsConfig, ZabbixDsInstanceConfig.class).getZabbix();
    }


    @Override
    public String getInstanceType() {
        return DsTypeEnum.ZABBIX.getName();
    }
}
