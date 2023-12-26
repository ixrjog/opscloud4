package com.baiyi.opscloud.zabbix.v5.driver;

import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.v5.driver.base.SimpleZabbixAuth;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixTemplate;
import com.baiyi.opscloud.zabbix.v5.feign.ZabbixTemplateFeign;
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
 * @Date 2021/11/19 10:28 上午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ZabbixV5TemplateDriver {

    protected final SimpleZabbixAuth simpleZabbixAuth;

    public interface TemplateAPIMethod {
        String GET = "template.get";
    }

    private ZabbixTemplateFeign buildFeign(ZabbixConfig.Zabbix config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(ZabbixTemplateFeign.class, config.getUrl());
    }

    private ZabbixTemplate.QueryTemplateResponse queryHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DefaultRequest request) {
        ZabbixTemplateFeign zabbixAPI = buildFeign(config);
        request.setMethod(TemplateAPIMethod.GET);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.query(request);
    }

    public List<ZabbixTemplate.Template> list(ZabbixConfig.Zabbix config) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .build();
        ZabbixTemplate.QueryTemplateResponse response = queryHandle(config, request);
        return response.getResult();
    }

    @CacheEvict(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_template_hostid_' + #host.hostid")
    public void evictHostTemplate(ZabbixConfig.Zabbix config, ZabbixHost.Host host) {
        log.info("Evict cache Zabbix Host Template: hostid={}", host.getHostid());
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_template_hostid_' + #host.hostid", unless = "#result == null")
    public List<ZabbixTemplate.Template> getByHost(ZabbixConfig.Zabbix config, ZabbixHost.Host host) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("hostids", host.getHostid())
                .build();
        ZabbixTemplate.QueryTemplateResponse response = queryHandle(config, request);
        return response.getResult();
    }

    public List<ZabbixTemplate.Template> listTemplatesByGroup(ZabbixConfig.Zabbix config, ZabbixHostGroup.HostGroup group) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("groupids", group.getGroupid())
                .build();
        ZabbixTemplate.QueryTemplateResponse response = queryHandle(config, request);
        return response.getResult();
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_template_id_' + #templateId", unless = "#result == null")
    public ZabbixTemplate.Template getById(ZabbixConfig.Zabbix config, String templateId) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("templateids", templateId)
                .build();
        ZabbixTemplate.QueryTemplateResponse response = queryHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult())) {
            return null;
        }
        return response.getResult().getFirst();
    }

    public List<ZabbixTemplate.Template> listByNames(ZabbixConfig.Zabbix config, List<String> names) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .filter(ZabbixFilterBuilder.builder()
                        .putEntry("host", names)
                        .build())
                .build();
        ZabbixTemplate.QueryTemplateResponse response = queryHandle(config, request);
        return response.getResult();
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, key = "#config.url + '_v5_template_name_' + #templateName", unless = "#result == null")
    public ZabbixTemplate.Template getByName(ZabbixConfig.Zabbix config, String templateName) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .filter(ZabbixFilterBuilder.builder()
                        .putEntry("host", templateName)
                        .build())
                .build();
        ZabbixTemplate.QueryTemplateResponse response = queryHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult())) {
            return null;
        }
        return response.getResult().getFirst();
    }

}