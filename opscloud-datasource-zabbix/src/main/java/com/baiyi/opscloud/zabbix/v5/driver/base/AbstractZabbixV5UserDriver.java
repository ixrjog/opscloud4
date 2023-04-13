package com.baiyi.opscloud.zabbix.v5.driver.base;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixUser;
import com.baiyi.opscloud.zabbix.v5.feign.ZabbixUserFeign;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/11/19 2:33 下午
 * @Version 1.0
 */
public abstract class AbstractZabbixV5UserDriver {

    @Resource
    protected SimpleZabbixAuth simpleZabbixAuth;

    private interface UserAPIMethod {
        String GET = "user.get";
        String CREATE = "user.create";
        String UPDATE = "user.update";
        String DELETE = "user.delete";
    }

    private ZabbixUserFeign buildFeign(ZabbixConfig.Zabbix config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(ZabbixUserFeign.class, config.getUrl());
    }

    protected ZabbixUser.QueryUserResponse queryHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DefaultRequest request) {
        ZabbixUserFeign zabbixAPI = buildFeign(config);
        request.setMethod(UserAPIMethod.GET);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.query(request);
    }

    protected ZabbixUser.UpdateUserResponse updateHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DefaultRequest request) {
        ZabbixUserFeign zabbixAPI = buildFeign(config);
        request.setMethod(UserAPIMethod.UPDATE);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.update(request);
    }

    protected ZabbixUser.CreateUserResponse createHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DefaultRequest request) {
        ZabbixUserFeign zabbixAPI = buildFeign(config);
        request.setMethod(UserAPIMethod.CREATE);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.create(request);
    }

    protected ZabbixUser.DeleteUserResponse deleteHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DeleteRequest request) {
        ZabbixUserFeign zabbixAPI = buildFeign(config);
        request.setMethod(UserAPIMethod.DELETE);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.delete(request);
    }

}
