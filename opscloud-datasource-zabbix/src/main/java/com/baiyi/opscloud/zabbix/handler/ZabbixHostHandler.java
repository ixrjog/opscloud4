package com.baiyi.opscloud.zabbix.handler;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.entry.ZabbixTemplate;
import com.baiyi.opscloud.zabbix.entry.ZabbixTrigger;
import com.baiyi.opscloud.zabbix.handler.base.BaseZabbixHandler;
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

import static com.baiyi.opscloud.zabbix.handler.base.ZabbixServer.ApiConstant.*;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/1 2:07 下午
 * @Since 1.0
 */
@Slf4j
@Component
public class ZabbixHostHandler extends BaseZabbixHandler<ZabbixHost> {

    public interface HostAPIMethod {
        String GET = "host.get";
        String CREATE = "host.create";
        String UPDATE = "host.update";
        String DELETE = "host.delete";
    }

    public List<ZabbixHost> list(DsZabbixConfig.Zabbix zabbix) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.GET)
                .paramEntry("selectInterfaces", "extend")
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixHost.class);
    }

    public List<ZabbixHost> getByGroup(DsZabbixConfig.Zabbix zabbix, ZabbixHostGroup group) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.GET)
                .paramEntry("selectInterfaces", "extend")
                .paramEntry("groupids", group.getGroupid())
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixHost.class);
    }

    public List<ZabbixHost> listByTemplate(DsZabbixConfig.Zabbix zabbix, ZabbixTemplate template) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.GET)
                .paramEntry("selectInterfaces", "extend")
                .paramEntry("templateids", template.getTemplateid())
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixHost.class);
    }

    public List<ZabbixHost> listByTrigger(DsZabbixConfig.Zabbix zabbix, ZabbixTrigger trigger) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.GET)
                .paramEntry("selectInterfaces", "extend")
                .paramEntry("triggerids", trigger.getTriggerid())
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixHost.class);
    }

    @CacheEvict(cacheNames = CachingConfig.Repositories.ZABBIX, key = "'host_hostid_' + #hostid")
    public void evictHostById(String hostid) {
        log.info("清除ZabbixHost缓存 : hostid = {}", hostid);
    }

    @Cacheable(cacheNames = CachingConfig.Repositories.ZABBIX, key = "'host_hostid_' + #hostid", unless = "#result == null")
    public ZabbixHost getById(DsZabbixConfig.Zabbix zabbix, String hostid) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.GET)
                .paramEntry("selectInterfaces", "extend")
                .paramEntry("hostids", hostid)
                .build();
        JsonNode data = call(zabbix, request);
        return mapperListGetOne(data.get(RESULT), ZabbixHost.class);
    }

//    public List<ZabbixHost> listByNames(DsZabbixConfig.Zabbix zabbix, List<String> names) {
//        ZabbixFilter filter = ZabbixFilterBuilder.builder()
//                .putEntry("host", names)
//                .build();
//        JsonNode data = queryHostByFilter(zabbix, filter);
//        return mapperList(data.get(RESULT), ZabbixHost.class);
//    }

//    public ZabbixHost getByName(DsZabbixConfig.Zabbix zabbix, String name) {
//        ZabbixFilter filter = ZabbixFilterBuilder.builder()
//                .putEntry("host", name)
//                .build();
//        JsonNode data = queryHostByFilter(zabbix, filter);
//        return mapperOne(data.get(RESULT), ZabbixHost.class);
//    }

    @CacheEvict(cacheNames = CachingConfig.Repositories.ZABBIX, key = "'host_ip_' + #ip")
    public void evictHostByIp(String ip) {
        log.info("清除ZabbixHost缓存 : ip = {}", ip);
    }

    @Cacheable(cacheNames = CachingConfig.Repositories.ZABBIX, key = "'host_ip_' + #ip", unless = "#result == null")
    public ZabbixHost getByIp(DsZabbixConfig.Zabbix zabbix, String ip) {
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


    private JsonNode queryHostByFilter(DsZabbixConfig.Zabbix zabbix, ZabbixFilter filter) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.GET)
                .paramEntry("selectInterfaces", "extend")
                .filter(filter)
                .build();
        return call(zabbix, request);
    }

//    public String createHost(DsZabbixConfig.Zabbix zabbix, Server server, Map<String, Object> paramMap) {
//        ZabbixHost host = getByName(zabbix, ServerUtil.toServerName(server));
//        if (host != null) {
//            return host.getHostid();
//        }
//        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
//                .method(HostAPIMethod.CREATE)
//                .paramEntry("host", ServerUtil.toServerName(server))
//                .paramEntry(paramMap)
//                .build();
//        JsonNode data = call(zabbix, request);
//        if (data.get(RESULT).get(HOST_IDS).isEmpty()) {
//            throw new RuntimeException("ZabbixHost创建失败");
//        }
//        return ZabbixMapper.mapperList(data.get(RESULT).get(HOST_IDS), String.class).get(0);
//    }

    public void updateHostName(DsZabbixConfig.Zabbix zabbix, ZabbixHost zabbixHost, String hostName) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.UPDATE)
                .paramEntry("hostid", zabbixHost.getHostid())
                .paramEntry("host", hostName)
                .build();
        JsonNode data = call(zabbix, request);
        if (data.get(RESULT).get("hostids").isEmpty()) {
            log.error("更新ZabbixHost主机名称失败: hostName = {}", hostName);
        } else {
            log.info("更新ZabbixHost主机名称: hostName = {}", hostName);
        }
    }

    public void updateHostTemplates(DsZabbixConfig.Zabbix zabbix, ZabbixHost zabbixHost, List<ZabbixTemplate> zabbixTemplates) {
        if (CollectionUtils.isEmpty(zabbixTemplates)) return;
        List<ZabbixHostParam.Template> templatesParams = toTemplateParams(zabbixTemplates);
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.UPDATE)
                .paramEntry("hostid", zabbixHost.getHostid())
                .paramEntrySkipEmpty("templates", templatesParams)
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

    public void clearHostTemplates(DsZabbixConfig.Zabbix zabbix, ZabbixHost zabbixHost, List<ZabbixTemplate> zabbixTemplates) {
        if (CollectionUtils.isEmpty(zabbixTemplates)) return;
        List<ZabbixHostParam.Template> templatesParams = toTemplateParams(zabbixTemplates);
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostAPIMethod.UPDATE)
                .paramEntry("hostid", zabbixHost.getHostid())
                .paramEntrySkipEmpty("templates_clear", templatesParams)
                .build();
        JsonNode data = call(zabbix, request);
        if (data.get(RESULT).get("hostids").isEmpty()) {
            log.error("清除ZabbixHost主机模版失败: templates = {}", JSON.toJSONString(templatesParams));
        } else {
            log.info("清除ZabbixHost主机模版: templates = {}", JSON.toJSONString(templatesParams));
        }
    }

    public void deleteById(DsZabbixConfig.Zabbix zabbix, String hostId) {
        ZabbixHost host = getById(zabbix, hostId);
        if (host == null) {
            return;
        }
        delete(zabbix, host);
    }

//    public void deleteByName(DsZabbixConfig.Zabbix zabbix, String name) {
//        ZabbixHost host = getByName(zabbix, name);
//        if (host == null) {
//            return;
//        }
//        delete(zabbix, host);
//    }

    private void delete(DsZabbixConfig.Zabbix zabbix, ZabbixHost zabbixHost) {
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
