package com.baiyi.opscloud.zabbix.v5.driver;

import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.v5.param.ZabbixHostParam;
import com.baiyi.opscloud.zabbix.v5.driver.base.SimpleZabbixV5HostDriver;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixRequestBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/19 10:03 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixV5HostTagDriver extends SimpleZabbixV5HostDriver {

    @CacheEvict(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_host_tag_hostid' + #host.hostid")
    public void evictHostTag(ZabbixConfig.Zabbix config, ZabbixHost.Host host) {
        log.info("Evict cache Zabbix Host Tag: hostid={}", host.getHostid());
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_host_tag_hostid' + #host.hostid", unless = "#result == null")
    public ZabbixHost.Host getHostTag(ZabbixConfig.Zabbix config, ZabbixHost.Host host) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("output", new String[]{"name"})
                .putParam("hostids", host.getHostid())
                .putParam("selectTags", new String[]{"tag", "value"})
                .build();
        ZabbixHost.QueryHostResponse response = queryHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult())) {
            return null;
        }
        return response.getResult().getFirst();
    }

    public void updateHostTags(ZabbixConfig.Zabbix config, ZabbixHost.Host host, List<ZabbixHostParam.Tag> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            return;
        }
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("hostid", host.getHostid())
                .putParam("tags", tags)
                .build();
        ZabbixHost.UpdateHostResponse response = updateHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult().getHostids())) {
            log.error("Update Zabbix Host Tag error: hostid={}", host.getHostid());
        }
    }

}