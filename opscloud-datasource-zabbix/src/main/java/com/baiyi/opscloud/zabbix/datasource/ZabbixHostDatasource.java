package com.baiyi.opscloud.zabbix.datasource;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.common.config.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.entity.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.entity.ZabbixTemplate;
import com.baiyi.opscloud.zabbix.entity.ZabbixTrigger;
import com.baiyi.opscloud.zabbix.datasource.base.BaseZabbixDatasource;
import com.baiyi.opscloud.zabbix.http.*;
import com.baiyi.opscloud.zabbix.param.ZabbixHostParam;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.zabbix.datasource.base.ZabbixServer.ApiConstant.RESULT;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/1 2:07 下午
 * @Since 1.0
 */
@Slf4j
@Component
public class ZabbixHostDatasource extends BaseZabbixDatasource<ZabbixHost> {

    public interface HostAPIMethod {
        String GET = "host.get";
        String CREATE = "host.create";
        String UPDATE = "host.update";
        String DELETE = "host.delete";
    }

    public List<ZabbixHost> list(ZabbixConfig.Zabbix zabbix) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.GET)
                .putParam("selectInterfaces", "extend")
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixHost.class);
    }

    public List<ZabbixHost> getByGroup(ZabbixConfig.Zabbix zabbix, ZabbixHostGroup group) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.GET)
                .putParam("selectInterfaces", "extend")
                .putParam("groupids", group.getGroupid())
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixHost.class);
    }

    public List<ZabbixHost> listByTemplate(ZabbixConfig.Zabbix zabbix, ZabbixTemplate template) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.GET)
                .putParam("selectInterfaces", "extend")
                .putParam("templateids", template.getTemplateid())
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixHost.class);
    }

    public List<ZabbixHost> listByTrigger(ZabbixConfig.Zabbix zabbix, ZabbixTrigger trigger) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.GET)
                .putParam("selectInterfaces", "extend")
                .putParam("triggerids", trigger.getTriggerid())
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixHost.class);
    }

    @CacheEvict(cacheNames = CachingConfiguration.Repositories.ZABBIX, key = "#zabbix.url + '_host_hostid_' + #hostid")
    public void evictHostById(ZabbixConfig.Zabbix zabbix, String hostid) {
        log.info("清除ZabbixHost缓存 : hostid = {}", hostid);
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.ZABBIX, key = "#zabbix.url + '_host_hostid_' + #hostid", unless = "#result == null")
    public ZabbixHost getById(ZabbixConfig.Zabbix zabbix, String hostid) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.GET)
                .putParam("selectInterfaces", "extend")
                .putParam("hostids", hostid)
                .build();
        JsonNode data = call(zabbix, request);
        return mapperListGetOne(data.get(RESULT), ZabbixHost.class);
    }

    @CacheEvict(cacheNames = CachingConfiguration.Repositories.ZABBIX, key = "#zabbix.url + '_host_ip_' + #ip")
    public void evictHostByIp(ZabbixConfig.Zabbix zabbix, String ip) {
        log.info("清除ZabbixHost缓存 : ip = {}", ip);
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.ZABBIX, key = "#zabbix.url + '_host_ip_' + #ip", unless = "#result == null")
    public ZabbixHost getByIp(ZabbixConfig.Zabbix zabbix, String ip) {
        ZabbixFilter filter = ZabbixFilterBuilder.builder()
                .putEntry("ip", ip)
                .build();
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.GET)
                .filter(filter)
                .build();
        JsonNode data = call(zabbix, request);
        return mapperListGetOne(data.get(RESULT), ZabbixHost.class);
    }

    public void updateHostName(ZabbixConfig.Zabbix zabbix, ZabbixHost zabbixHost, String hostName) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.UPDATE)
                .putParam("hostid", zabbixHost.getHostid())
                .putParam("host", hostName)
                .build();
        JsonNode data = call(zabbix, request);
        if (data.get(RESULT).get("hostids").isEmpty()) {
            log.error("更新ZabbixHost主机名称失败: hostName = {}", hostName);
        } else {
            log.info("更新ZabbixHost主机名称: hostName = {}", hostName);
        }
    }

    public void updateHostTemplates(ZabbixConfig.Zabbix zabbix, ZabbixHost zabbixHost, List<ZabbixTemplate> zabbixTemplates) {
        if (CollectionUtils.isEmpty(zabbixTemplates)) return;
        List<ZabbixHostParam.Template> templatesParams = toTemplateParams(zabbixTemplates);
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.UPDATE)
                .putParam("hostid", zabbixHost.getHostid())
                .putParamSkipEmpty("templates", templatesParams)
                .build();
        JsonNode data = call(zabbix, request);
        if (data.get(RESULT).get("hostids").isEmpty()) {
            log.error("更新ZabbixHost主机模版失败: templates = {}", JSON.toJSONString(templatesParams));
        } else {
            log.info("更新ZabbixHost主机模版: templates = {}", JSON.toJSONString(templatesParams));
        }
    }

    private List<ZabbixHostParam.Template> toTemplateParams(List<ZabbixTemplate> zabbixTemplates) {
        return zabbixTemplates.stream().map(e -> ZabbixHostParam.Template.builder().templateid(e.getTemplateid()).build())
                .collect(Collectors.toList());
    }

    public void clearHostTemplates(ZabbixConfig.Zabbix zabbix, ZabbixHost zabbixHost, List<ZabbixTemplate> zabbixTemplates) {
        if (CollectionUtils.isEmpty(zabbixTemplates)) return;
        List<ZabbixHostParam.Template> templatesParams = toTemplateParams(zabbixTemplates);
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.UPDATE)
                .putParam("hostid", zabbixHost.getHostid())
                .putParamSkipEmpty("templates_clear", templatesParams)
                .build();
        JsonNode data = call(zabbix, request);
        if (data.get(RESULT).get("hostids").isEmpty()) {
            log.error("清除ZabbixHost主机模版失败: templates = {}", JSON.toJSONString(templatesParams));
        } else {
            log.info("清除ZabbixHost主机模版: templates = {}", JSON.toJSONString(templatesParams));
        }
    }

    public void deleteById(ZabbixConfig.Zabbix zabbix, String hostId) {
        ZabbixHost host = getById(zabbix, hostId);
        if (host == null) {
            return;
        }
        delete(zabbix, host);
    }

    private void delete(ZabbixConfig.Zabbix zabbix, ZabbixHost zabbixHost) {
        ZabbixDeleteRequest request = ZabbixDeleteRequest.builder()
                .method(HostAPIMethod.DELETE)
                .params(new String[]{zabbixHost.getHostid()})
                .build();
        JsonNode data = call(zabbix, request);
        if (data.get(RESULT).get("hostids").isEmpty()) {
            log.error("删除ZabbixHost主机失败: hostid = {}", zabbixHost.getHostid());
        } else {
            log.info("清除ZabbixHost主机: hostid = {}", zabbixHost.getHostid());
        }
    }

}
