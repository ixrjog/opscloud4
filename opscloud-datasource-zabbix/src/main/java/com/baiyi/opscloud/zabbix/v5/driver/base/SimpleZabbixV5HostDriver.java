package com.baiyi.opscloud.zabbix.v5.driver.base;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.v5.feign.ZabbixHostFeign;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/11/19 10:09 上午
 * @Version 1.0
 */
@Component
public class SimpleZabbixV5HostDriver {

    @Resource
    private SimpleZabbixAuth simpleZabbixAuth;

    public interface HostAPIMethod {
        String GET = "host.get";
        String CREATE = "host.create";
        String UPDATE = "host.update";
        String DELETE = "host.delete";
    }

    private ZabbixHostFeign buildFeign(ZabbixConfig.Zabbix config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options())
                .target(ZabbixHostFeign.class, config.getUrl());
    }

    public ZabbixHost.CreateHostResponse createHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DefaultRequest request) {
        ZabbixHostFeign zabbixAPI = buildFeign(config);
        request.setMethod(HostAPIMethod.CREATE);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.create(request);
    }

    protected ZabbixHost.QueryHostResponse queryHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DefaultRequest request) {
        ZabbixHostFeign zabbixAPI = buildFeign(config);
        request.setMethod(HostAPIMethod.GET);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.query(request);
    }

    protected ZabbixHost.UpdateHostResponse updateHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DefaultRequest request) {
        ZabbixHostFeign zabbixAPI = buildFeign(config);
        request.setMethod(HostAPIMethod.UPDATE);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.update(request);
    }

    protected ZabbixHost.DeleteHostResponse deleteHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DeleteRequest request) {
        ZabbixHostFeign zabbixAPI = buildFeign(config);
        request.setMethod(HostAPIMethod.DELETE);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.delete(request);
    }

}
