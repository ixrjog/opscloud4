package com.baiyi.opscloud.zabbix.v5.driver;

import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.v5.driver.base.AbstractZabbixV5UserGroupDriver;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixUser;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixUserGroup;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixRequestBuilder;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/11/19 3:50 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixV5UserGroupDriver extends AbstractZabbixV5UserGroupDriver {

    public List<ZabbixUserGroup.UserGroup> list(ZabbixConfig.Zabbix config) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .build();
        ZabbixUserGroup.QueryUserGroupResponse response = queryHandle(config, request);
        return response.getResult();
    }

    public List<ZabbixUserGroup.UserGroup> listByUser(ZabbixConfig.Zabbix config, ZabbixUser.User user) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("userids", user.getUserid())
                .build();
        ZabbixUserGroup.QueryUserGroupResponse response = queryHandle(config, request);
        return response.getResult();
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_usergroup_usrgrpid_' + #usrgrpid", unless = "#result == null")
    public ZabbixUserGroup.UserGroup getById(ZabbixConfig.Zabbix config, String usrgrpid) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("usrgrpids", usrgrpid)
                .build();
        ZabbixUserGroup.QueryUserGroupResponse response = queryHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult())) {
            return null;
        }
        return response.getResult().getFirst();
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_usergroup_name_' + #usergroup", unless = "#result == null")
    public ZabbixUserGroup.UserGroup getByName(ZabbixConfig.Zabbix config, String usergroup) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("status", 0)
                .filter(ZabbixFilterBuilder.builder()
                        .putEntry("name", usergroup)
                        .build())
                .build();
        ZabbixUserGroup.QueryUserGroupResponse response = queryHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult())) {
            return null;
        }
        return response.getResult().getFirst();
    }

    public ZabbixUserGroup.UserGroup create(ZabbixConfig.Zabbix config, String usergroup, ZabbixHostGroup.HostGroup hostGroup) {
        // 创建用户组
        Map<String, String> rights = Maps.newHashMap();
        /*
         * Possible values:
         0 - access denied;
         2 - read-only access;
         3 - read-write access.
         */
        rights.put("permission", "2");
        rights.put("id", hostGroup.getGroupid());
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("name", usergroup)
                .putParam("rights", rights)
                .build();
        ZabbixUserGroup.CreateUserGroupResponse response = createHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult().getUsrgrpids())) {
            return null;
        }
        return getById(config, response.getResult().getUsrgrpids().getFirst());
    }

}