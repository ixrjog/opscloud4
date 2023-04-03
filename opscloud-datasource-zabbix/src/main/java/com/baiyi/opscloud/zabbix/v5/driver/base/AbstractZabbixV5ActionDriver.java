package com.baiyi.opscloud.zabbix.v5.driver.base;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5HostGroupDriver;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5UserGroupDriver;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixAction;
import com.baiyi.opscloud.zabbix.v5.feign.ZabbixActionFeign;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import com.google.common.base.Joiner;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/11/19 4:14 下午
 * @Version 1.0
 */
public abstract class AbstractZabbixV5ActionDriver {

    public static final String ACTION_NAME_PREFIX = "Report problems to";

    @Resource
    protected SimpleZabbixAuth simpleZabbixAuth;

    @Resource
    protected ZabbixV5UserGroupDriver zabbixV5UserGroupDatasource;

    @Resource
    protected ZabbixV5HostGroupDriver zabbixV5HostGroupDatasource;

    private interface ActionAPIMethod {
        String GET = "action.get";
        String CREATE = "action.create";
        String DELETE = "action.delete";
    }

    private ZabbixActionFeign buildFeign(ZabbixConfig.Zabbix config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(ZabbixActionFeign.class, config.getUrl());
    }

    protected ZabbixAction.QueryActionResponse queryHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DefaultRequest request) {
        ZabbixActionFeign zabbixAPI = buildFeign(config);
        request.setMethod(ActionAPIMethod.GET);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.query(request);
    }

    protected ZabbixAction.CreateActionResponse createHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DefaultRequest request) {
        ZabbixActionFeign zabbixAPI = buildFeign(config);
        request.setMethod(ActionAPIMethod.CREATE);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.create(request);
    }

    protected ZabbixAction.DeleteActionResponse deleteHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DeleteRequest request) {
        ZabbixActionFeign zabbixAPI = buildFeign(config);
        request.setMethod(ActionAPIMethod.DELETE);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.delete(request);
    }

    public String buildActionName(String usergrpName) {
        return Joiner.on(" ").join(ACTION_NAME_PREFIX, usergrpName);
    }

}
