package com.baiyi.opscloud.zabbix.v5.driver.base;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixUserGroup;
import com.baiyi.opscloud.zabbix.v5.feign.ZabbixUserGroupFeign;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/11/19 3:51 下午
 * @Version 1.0
 */
public abstract class AbstractZabbixV5UserGroupDriver {

    @Resource
    protected SimpleZabbixAuth simpleZabbixAuth;

    private interface UserGroupAPIMethod {
        String GET = "usergroup.get";
        String CREATE = "usergroup.create";
    }

    private ZabbixUserGroupFeign buildFeign(ZabbixConfig.Zabbix config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(ZabbixUserGroupFeign.class, config.getUrl());
    }

    protected ZabbixUserGroup.QueryUserGroupResponse queryHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DefaultRequest request) {
        ZabbixUserGroupFeign zabbixAPI = buildFeign(config);
        request.setMethod(UserGroupAPIMethod.GET);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.query(request);
    }

    protected ZabbixUserGroup.CreateUserGroupResponse createHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DefaultRequest request) {
        ZabbixUserGroupFeign zabbixAPI = buildFeign(config);
        request.setMethod(UserGroupAPIMethod.CREATE);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.create(request);
    }

}
