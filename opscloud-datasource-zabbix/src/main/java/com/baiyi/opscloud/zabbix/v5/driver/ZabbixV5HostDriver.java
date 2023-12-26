package com.baiyi.opscloud.zabbix.v5.driver;

import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.v5.driver.base.SimpleZabbixV5HostDriver;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixTrigger;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixRequestBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/18 1:47 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixV5HostDriver extends SimpleZabbixV5HostDriver {

    public List<ZabbixHost.Host> list(ZabbixConfig.Zabbix config) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("selectInterfaces", "extend")
                .build();
        ZabbixHost.QueryHostResponse response = queryHandle(config, request);
        return response.getResult();
    }

    public List<ZabbixHost.Host> getByGroup(ZabbixConfig.Zabbix config, ZabbixHostGroup.HostGroup hostGroup) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("selectInterfaces", "extend")
                .putParam("groupids", hostGroup.getGroupid())
                .build();
        ZabbixHost.QueryHostResponse response = queryHandle(config, request);
        return response.getResult();
    }

    public List<ZabbixHost.Host> listByTemplate(ZabbixConfig.Zabbix config, com.baiyi.opscloud.zabbix.v5.entity.ZabbixTemplate.Template template) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("selectInterfaces", "extend")
                .putParam("templateids", template.getTemplateid())
                .build();
        ZabbixHost.QueryHostResponse response = queryHandle(config, request);
        return response.getResult();
    }

    public List<ZabbixHost.Host> listByTrigger(ZabbixConfig.Zabbix config, ZabbixTrigger.Trigger trigger) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("selectInterfaces", "extend")
                .putParam("triggerids", trigger.getTriggerid())
                .build();
        ZabbixHost.QueryHostResponse response = queryHandle(config, request);
        return response.getResult();
    }

    @CacheEvict(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_host_hostid_' + #hostid")
    public void evictHostById(ZabbixConfig.Zabbix config, String hostid) {
        log.info("Evict cache Zabbix Host: hostid={}", hostid);
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_host_hostid_' + #hostid", unless = "#result == null")
    public ZabbixHost.Host getById(ZabbixConfig.Zabbix config, String hostid) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("selectInterfaces", "extend")
                .putParam("hostids", hostid)
                .build();
        ZabbixHost.QueryHostResponse response = queryHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult())) {
            return null;
        }
        return response.getResult().getFirst();
    }

    @CacheEvict(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_host_ip_' + #ip")
    public void evictHostByIp(ZabbixConfig.Zabbix config, String ip) {
        log.info("清除ZabbixHost缓存: ip={}", ip);
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_host_ip_' + #ip", unless = "#result == null")
    public ZabbixHost.Host getByIp(ZabbixConfig.Zabbix config, String ip) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .filter(ZabbixFilterBuilder.builder()
                        .putEntry("ip", ip)
                        .build())
                .build();
        ZabbixHost.QueryHostResponse response = queryHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult())) {
            return null;
        }
        return response.getResult().getFirst();
    }

    public void updateHost(ZabbixConfig.Zabbix config, ZabbixHost.Host host, ZabbixRequest.DefaultRequest request) {
        request.putParam("hostid", host.getHostid());
        ZabbixHost.UpdateHostResponse response = updateHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult().getHostids())) {
            log.error("Update Zabbix Host name error: hostName={}", host.getHost());
        }
    }

    @Deprecated
    public void updateHostName(ZabbixConfig.Zabbix config, ZabbixHost.Host host, String hostName) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("hostid", host.getHostid())
                .putParam("host", hostName)
                .build();
        ZabbixHost.UpdateHostResponse response = updateHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult().getHostids())) {
            log.error("Update Zabbix Host name error: hostName={}", hostName);
        }
    }

    /**
     * 更新主机代理
     *
     * @param config
     * @param host
     * @param proxyHostid
     */
    @Deprecated
    public void updateHostProxy(ZabbixConfig.Zabbix config, ZabbixHost.Host host, String proxyHostid) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("hostid", host.getHostid())
                .putParam("proxy_hostid", proxyHostid)
                .build();
        ZabbixHost.UpdateHostResponse response = updateHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult().getHostids())) {
            log.error("Update Zabbix Host name error: hostName={}, proxyHostid={}", host.getHost(), proxyHostid);
        }
    }

    public void deleteById(ZabbixConfig.Zabbix config, String hostId) {
        ZabbixHost.Host host = getById(config, hostId);
        if (host == null) {
            return;
        }
        delete(config, host);
    }

    private void delete(ZabbixConfig.Zabbix config, ZabbixHost.Host host) {
        ZabbixRequest.DeleteRequest request = ZabbixRequest.DeleteRequest.builder()
                .params(new String[]{host.getHostid()})
                .build();
        ZabbixHost.DeleteHostResponse response = deleteHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult().getHostids())) {
            log.error("Delete Zabbix Host error: hostid={}", host.getHostid());
        }
    }

}