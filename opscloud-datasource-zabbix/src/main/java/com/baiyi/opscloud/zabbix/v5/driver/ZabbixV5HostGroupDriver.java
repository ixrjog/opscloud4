package com.baiyi.opscloud.zabbix.v5.driver;

import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.v5.driver.base.SimpleZabbixAuth;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.v5.feign.ZabbixHostGroupFeign;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixRequestBuilder;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/19 11:32 上午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ZabbixV5HostGroupDriver {

    private final SimpleZabbixAuth simpleZabbixAuth;

    private interface HostGroupAPIMethod {
        String GET = "hostgroup.get";
        String CREATE = "hostgroup.create";
    }

    private ZabbixHostGroupFeign buildFeign(ZabbixConfig.Zabbix config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(ZabbixHostGroupFeign.class, config.getUrl());
    }

    private ZabbixHostGroup.QueryHostGroupResponse queryHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DefaultRequest request) {
        ZabbixHostGroupFeign zabbixAPI = buildFeign(config);
        request.setMethod(HostGroupAPIMethod.GET);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.query(request);
    }

    private ZabbixHostGroup.CreateHostGroupResponse createHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DefaultRequest request) {
        ZabbixHostGroupFeign zabbixAPI = buildFeign(config);
        request.setMethod(HostGroupAPIMethod.CREATE);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.create(request);
    }

    public List<ZabbixHostGroup.HostGroup> list(ZabbixConfig.Zabbix config) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .build();
        ZabbixHostGroup.QueryHostGroupResponse response = queryHandle(config, request);
        return response.getResult();
    }

    public List<ZabbixHostGroup.HostGroup> listByHost(ZabbixConfig.Zabbix config, ZabbixHost.Host host) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("hostids", host.getHostid())
                .build();
        ZabbixHostGroup.QueryHostGroupResponse response = queryHandle(config, request);
        return response.getResult();
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_hostgroup_groupid_' + #groupid", unless = "#result == null")
    public ZabbixHostGroup.HostGroup getById(ZabbixConfig.Zabbix config, String groupid) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("groupids", groupid)
                .build();
        ZabbixHostGroup.QueryHostGroupResponse response = queryHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult())) {
            return null;
        }
        return response.getResult().getFirst();
    }

    @CacheEvict(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_hostgroup_name_' + #hostGroup.name")
    public void evictHostGroup(ZabbixConfig.Zabbix config, ZabbixHostGroup.HostGroup hostGroup) {
        log.info("Evict cache Zabbix HostGroup: name={}", hostGroup.getName());
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_hostgroup_name_' + #name", unless = "#result == null")
    public ZabbixHostGroup.HostGroup getByName(ZabbixConfig.Zabbix config, String name) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .filter(ZabbixFilterBuilder.builder()
                        .putEntry("name", name)
                        .build())
                .build();
        ZabbixHostGroup.QueryHostGroupResponse response = queryHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult())) {
            return null;
        }
        return response.getResult().getFirst();
    }

    public void create(ZabbixConfig.Zabbix config, String name) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("name", name)
                .build();
        ZabbixHostGroup.CreateHostGroupResponse response = createHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult().getGroupids())) {
            log.error("Create Zabbix HostGroup error: name={}", name);
        }
    }

}