package com.baiyi.opscloud.datasource.account.impl;

import com.baiyi.opscloud.common.datasource.ZabbixDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.account.convert.AccountConvert;
import com.baiyi.opscloud.datasource.account.impl.base.AbstractAccountProvider;
import com.baiyi.opscloud.datasource.account.util.ZabbixMediaUtil;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.business.BaseBusiness;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import com.baiyi.opscloud.zabbix.entry.ZabbixUserGroup;
import com.baiyi.opscloud.zabbix.facade.ZabbixFacade;
import com.baiyi.opscloud.zabbix.handler.ZabbixUserHandler;
import com.baiyi.opscloud.zabbix.util.ZabbixUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/8/11 4:31 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixAccountProvider extends AbstractAccountProvider<DsZabbixConfig.Zabbix> {

    @Resource
    private ZabbixUserHandler zabbixUserHandler;

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private ZabbixFacade zabbixFacade;

    public static final String ZABBIX_DEFAULT_USERGROUP = "users_default";

    @Override
    protected void doCreate(DsZabbixConfig.Zabbix zabbix, User user) {
        zabbixUserHandler.create(zabbix, AccountConvert.toZabbixUser(user), ZabbixMediaUtil.buildMedias(user), getUsrgrps(zabbix, user));
        log.info("创建Zabbix用户: url= {} , username = {}", zabbix.getUrl(), user.getUsername());
    }

    @Override
    protected void doUpdate(DsZabbixConfig.Zabbix zabbix, User user) {
        ZabbixUser zabbixUser = zabbixUserHandler.getByUsername(zabbix, user.getUsername());
        if (zabbixUser == null) return; // 用户不存在
        ZabbixUser updateUser = AccountConvert.toZabbixUser(user);
        updateUser.setUserid(zabbixUser.getUserid());
        zabbixUserHandler.update(zabbix, updateUser, ZabbixMediaUtil.buildMedias(user), getUsrgrps(zabbix, user));
        log.info("更新Zabbix用户: url= {} , username = {}", zabbix.getUrl(), user.getUsername());
    }

    @Override
    protected void doDelete(DsZabbixConfig.Zabbix zabbix, User user) {
        zabbixUserHandler.delete(zabbix, user.getUsername());
        log.info("删除Zabbix用户: url= {} , username = {}", zabbix.getUrl(), user.getUsername());
    }

    @Override
    protected void doGrant(DsZabbixConfig.Zabbix zabbix, User user, BaseBusiness.IBusiness businessResource) {

    }

    @Override
    protected void doRevoke(DsZabbixConfig.Zabbix zabbix, User user, BaseBusiness.IBusiness businessResource) {

    }

    @Override
    protected int getBusinessResourceType() {
        return BusinessTypeEnum.SERVERGROUP.getType();
    }

    private ServerGroup getBusinessResource(int businessId) {
        return serverGroupService.getById(businessId);
    }

    @Override
    protected DsZabbixConfig.Zabbix buildConfig(DatasourceConfig dsConfig) {
        return dsConfigFactory.build(dsConfig, ZabbixDsInstanceConfig.class).getZabbix();
    }

    private List<Map<String, String>> getUsrgrps(DsZabbixConfig.Zabbix zabbix, User user) {
        List<Map<String, String>> userGroups = toUsrgrps(zabbix, queryUserPermission(user, BusinessTypeEnum.SERVERGROUP.getType()));
        if (CollectionUtils.isEmpty(userGroups))
            userGroups.add(buildUsrgrp(zabbixFacade.getOrCreateUserGroup(zabbix, ZABBIX_DEFAULT_USERGROUP).getUsrgrpid()));
        return userGroups;
    }

    private List<Map<String, String>> toUsrgrps(DsZabbixConfig.Zabbix zabbix, List<UserPermission> userPermissions) {
        return userPermissions.stream().map(e -> {
            ServerGroup serverGroup = serverGroupService.getById(e.getBusinessId());
            String usergroupName = ZabbixUtil.toUsergrpName(serverGroup.getName());
            ZabbixUserGroup zabbixUserGroup = zabbixFacade.getOrCreateUserGroup(zabbix, usergroupName);
            return buildUsrgrp(zabbixUserGroup.getUsrgrpid());
        }).collect(Collectors.toList());
    }

    private Map<String, String> buildUsrgrp(String usrgrpId) {
        Map<String, String> usrgrp = Maps.newHashMap();
        usrgrp.put("usrgrpid", usrgrpId);
        return usrgrp;
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ZABBIX.getName();
    }
}
