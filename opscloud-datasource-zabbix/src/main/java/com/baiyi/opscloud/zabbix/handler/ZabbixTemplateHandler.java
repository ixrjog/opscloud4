package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.common.datasource.ZabbixDsInstanceConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.entry.ZabbixTemplate;
import com.baiyi.opscloud.zabbix.handler.base.BaseZabbixHandler;
import com.baiyi.opscloud.zabbix.handler.base.ZabbixServer;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequest;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.http.ZabbixFilter;
import com.baiyi.opscloud.zabbix.http.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.zabbix.handler.base.ZabbixServer.ApiConstant.*;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/1 2:50 下午
 * @Since 1.0
 */
@Slf4j
@Component
public class ZabbixTemplateHandler  extends BaseZabbixHandler<ZabbixTemplate> {

    @Resource
    private ZabbixServer zabbixServer;

    private interface Method {
        String QUERY_TEMPLATE = "template.get";
    }

    private SimpleZabbixRequestBuilder queryRequestBuilder() {
        return SimpleZabbixRequestBuilder.builder()
                .method(Method.QUERY_TEMPLATE);
    }

    public List<ZabbixTemplate> listAll(ZabbixDsInstanceConfig.Zabbix zabbix) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .build();
        JsonNode data = zabbixServer.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixTemplate.class);
    }

    @CacheEvict(cacheNames = CachingConfig.Repositories.ZABBIX, key = "#zabbix.url + '_template_hostid_' + #zabbixHost.hostid")
    public void evictHostTemplate(ZabbixDsInstanceConfig.Zabbix zabbix,ZabbixHost zabbixHost) {
        log.info("清除ZabbixHost模版缓存 : hostid = {}", zabbixHost.getHostid());
    }

    @Cacheable(cacheNames = CachingConfig.Repositories.ZABBIX, key = "#zabbix.url + '_template_hostid_' + #host.hostid", unless = "#result == null")
    public List<ZabbixTemplate> getByHost(ZabbixDsInstanceConfig.Zabbix zabbix, ZabbixHost host) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .paramEntry(HOST_IDS, host.getHostid())
                .build();
        JsonNode data = zabbixServer.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixTemplate.class);
    }

    public List<ZabbixTemplate> listTemplatesByGroup(ZabbixDsInstanceConfig.Zabbix zabbix, ZabbixHostGroup group) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .paramEntry(HOST_GROUP_IDS, group.getGroupid())
                .build();
        JsonNode data = zabbixServer.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixTemplate.class);
    }


    @Cacheable(cacheNames = CachingConfig.Repositories.ZABBIX, key = "#zabbix.url + '_template_id_' + #templateId", unless = "#result == null")
    public ZabbixTemplate getById(ZabbixDsInstanceConfig.Zabbix zabbix, String templateId) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .paramEntry(TEMPLATE_IDS, templateId)
                .build();
        JsonNode data = zabbixServer.call(zabbix, request);
        return mapperListGetOne(data.get(RESULT), ZabbixTemplate.class);
    }

    public List<ZabbixTemplate> listByNames(ZabbixDsInstanceConfig.Zabbix zabbix, List<String> names) {
        ZabbixFilter filter = ZabbixFilterBuilder.builder()
                .putEntry("host", names)
                .build();
        SimpleZabbixRequest request = queryRequestBuilder()
                .filter(filter)
                .build();
        JsonNode data = zabbixServer.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixTemplate.class);
    }

    @Cacheable(cacheNames = CachingConfig.Repositories.ZABBIX, key = "#zabbix.url + '_template_name_' + #templateName", unless = "#result == null")
    public ZabbixTemplate getByName(ZabbixDsInstanceConfig.Zabbix zabbix,String templateName) {
        ZabbixFilter filter = ZabbixFilterBuilder.builder()
                .putEntry("host", templateName)
                .build();
        SimpleZabbixRequest request = queryRequestBuilder()
                .filter(filter)
                .build();
        JsonNode data = zabbixServer.call(zabbix, request);
        return mapperListGetOne(data.get(RESULT), ZabbixTemplate.class);
    }
}
