package com.baiyi.opscloud.zabbix.v5.driver;

import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.v5.driver.base.AbstractZabbixV5ProxyDriver;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixProxy;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixRequestBuilder;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @Author baiyi
 * @Date 2021/12/27 2:28 PM
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ZabbixV5ProxyDriver extends AbstractZabbixV5ProxyDriver {

    /**
     * 查询Zabbix代理
     *
     * @param config
     * @param hostname 代理的名称
     * @return
     */
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_2H, key = "#config.url + '_v5_proxy_name_' + #hostname", unless = "#result == null")
    public ZabbixProxy.Proxy getProxy(ZabbixConfig.Zabbix config, String hostname) {
        ZabbixRequest.Filter filter = ZabbixFilterBuilder.builder()
                .putEntry("host", hostname)
                .build();
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .filter(filter)
                .build();
        ZabbixProxy.QueryProxyResponse response = queryHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult())) {
            return null;
        }
        return response.getResult().getFirst();
    }

    /**
     * 更新主机的代理配置
     *
     * @param config
     * @param host
     * @param proxy
     */
    public void updateHostProxy(ZabbixConfig.Zabbix config, ZabbixHost.Host host, ZabbixProxy.Proxy proxy) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("proxyid", proxy.getProxyid())
                .putParam("hosts", Lists.newArrayList(host.getHostid()))
                .build();
        ZabbixProxy.UpdateProxyResponse response = updateHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult().getProxyids())) {
            log.error("Update Zabbix Host Proxy error: host={}, proxy={}", host.getHost(), proxy.getHost());
        }
    }

}