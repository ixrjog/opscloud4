package com.baiyi.opscloud.zabbix.v5.drive;

import com.baiyi.opscloud.common.config.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.zabbix.v5.drive.base.AbstractZabbixV5HostDrive;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixTrigger;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.v5.param.ZabbixHostParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/11/18 1:47 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixV5HostDrive extends AbstractZabbixV5HostDrive {

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

    @CacheEvict(cacheNames = CachingConfiguration.Repositories.ZABBIX, key = "#config.url + '_v5_host_hostid_' + #hostid")
    public void evictHostById(ZabbixConfig.Zabbix config, String hostid) {
        log.info("清除ZabbixHost缓存 : hostid = {}", hostid);
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.ZABBIX, key = "#config.url + '_v5_host_hostid_' + #hostid", unless = "#result == null")
    public ZabbixHost.Host getById(ZabbixConfig.Zabbix config, String hostid) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("selectInterfaces", "extend")
                .putParam("hostids", hostid)
                .build();
        ZabbixHost.QueryHostResponse response = queryHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult()))
            return null;
        return response.getResult().get(0);
    }

    @CacheEvict(cacheNames = CachingConfiguration.Repositories.ZABBIX, key = "#config.url + '_v5_host_ip_' + #ip")
    public void evictHostByIp(ZabbixConfig.Zabbix config, String ip) {
        log.info("清除ZabbixHost缓存 : ip = {}", ip);
    }


    @Cacheable(cacheNames = CachingConfiguration.Repositories.ZABBIX, key = "#config.url + '_v5_host_ip_' + #ip", unless = "#result == null")
    public ZabbixHost.Host getByIp(ZabbixConfig.Zabbix config, String ip) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .filter(ZabbixFilterBuilder.builder()
                        .putEntry("ip", ip)
                        .build())
                .build();
        ZabbixHost.QueryHostResponse response = queryHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult()))
            return null;
        return response.getResult().get(0);
    }

    public void updateHostName(ZabbixConfig.Zabbix config, ZabbixHost.Host host, String hostName) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("hostid", host.getHostid())
                .putParam("host", hostName)
                .build();
        ZabbixHost.UpdateHostResponse response = updateHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult().getHostids())) {
            log.error("更新ZabbixHost主机名称失败: hostName = {}", hostName);
        }
    }

    public void updateHostTemplates(ZabbixConfig.Zabbix config, ZabbixHost.Host host, List<com.baiyi.opscloud.zabbix.v5.entity.ZabbixTemplate.Template> templates) {
        if (CollectionUtils.isEmpty(templates)) return;
        List<ZabbixHostParam.Template> templatesParams = toTemplateParams(templates);
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("hostid", host.getHostid())
                .putParamSkipEmpty("templates", templatesParams)
                .build();
        ZabbixHost.UpdateHostResponse response = updateHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult().getHostids())) {
            log.error("更新ZabbixHost主机模版失败: templates = {}", JSONUtil.writeValueAsString(templatesParams));
        }
    }

    public void clearHostTemplates(ZabbixConfig.Zabbix config, ZabbixHost.Host host, List<com.baiyi.opscloud.zabbix.v5.entity.ZabbixTemplate.Template> templates) {
        if (CollectionUtils.isEmpty(templates)) return;
        List<ZabbixHostParam.Template> templatesParams = toTemplateParams(templates);
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("hostid", host.getHostid())
                .putParamSkipEmpty("templates_clear", templatesParams)
                .build();

        ZabbixHost.UpdateHostResponse response = updateHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult().getHostids())) {
            log.error("清除ZabbixHost主机模版失败: templates = {}", JSONUtil.writeValueAsString(templatesParams));
        }
    }

    private List<ZabbixHostParam.Template> toTemplateParams(List<com.baiyi.opscloud.zabbix.v5.entity.ZabbixTemplate.Template> templates) {
        return templates.stream().map(e -> ZabbixHostParam.Template.builder().templateid(e.getTemplateid()).build())
                .collect(Collectors.toList());
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
            log.error("删除ZabbixHost主机失败: hostid = {}", host.getHostid());
        }
    }

}
