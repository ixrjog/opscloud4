package com.baiyi.opscloud.zabbix.v5.driver.base;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixProxy;
import com.baiyi.opscloud.zabbix.v5.feign.ZabbixProxyFeign;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/12/27 3:58 PM
 * @Version 1.0
 */
public abstract class AbstractZabbixV5ProxyDriver {

    public interface ProxyAPIMethod {
        String GET = "proxy.get";
        String UPDATE = "proxy.update";
    }

    @Resource
    private SimpleZabbixAuth simpleZabbixAuth;

    private ZabbixProxyFeign buildFeign(ZabbixConfig.Zabbix config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(ZabbixProxyFeign.class, config.getUrl());
    }

    protected ZabbixProxy.QueryProxyResponse queryHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DefaultRequest request) {
        ZabbixProxyFeign zabbixAPI = buildFeign(config);
        request.setMethod(ProxyAPIMethod.GET);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.query(request);
    }

    protected ZabbixProxy.UpdateProxyResponse updateHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DefaultRequest request) {
        ZabbixProxyFeign zabbixAPI = buildFeign(config);
        request.setMethod(ProxyAPIMethod.UPDATE);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.update(request);
    }

}
