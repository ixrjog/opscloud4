package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.handler.base.BaseZabbixHandler;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequest;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.http.ZabbixFilter;
import com.baiyi.opscloud.zabbix.http.ZabbixFilterBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.zabbix.handler.base.ZabbixServer.ApiConstant.RESULT;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/1 2:12 下午
 * @Since 1.0
 */
@Slf4j
@Component
public class ZabbixHostGroupHandler extends BaseZabbixHandler<ZabbixHostGroup> {

    private interface HostGroupAPIMethod {
        String GET = "hostgroup.get";
        String CREATE = "hostgroup.create";
    }

    public List<ZabbixHostGroup> list(ZabbixConfig.Zabbix zabbix) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostGroupAPIMethod.GET)
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixHostGroup.class);
    }

    public List<ZabbixHostGroup> listByHost(ZabbixConfig.Zabbix zabbix, ZabbixHost host) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostGroupAPIMethod.GET)
                .paramEntry("hostids", host.getHostid())
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixHostGroup.class);
    }

    @Cacheable(cacheNames = CachingConfig.Repositories.ZABBIX, key = "#zabbix.url + '_hostgroup_groupid_' + #groupid", unless = "#result == null")
    public ZabbixHostGroup getById(ZabbixConfig.Zabbix zabbix, String groupid) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostGroupAPIMethod.GET)
                .paramEntry("groupids", groupid)
                .build();
        JsonNode data = call(zabbix, request);
        return mapperListGetOne(data.get(RESULT), ZabbixHostGroup.class);
    }

    @CacheEvict(cacheNames = CachingConfig.Repositories.ZABBIX, key = "#zabbix.url + '_hostgroup_name_' + #zabbixHostGroup.name")
    public void evictHostGroup(ZabbixConfig.Zabbix zabbix, ZabbixHostGroup zabbixHostGroup) {
        log.info("清除ZabbixHostGroup缓存 : name = {}", zabbixHostGroup.getName());
    }

    @Cacheable(cacheNames = CachingConfig.Repositories.ZABBIX, key = "#zabbix.url + '_hostgroup_name_' + #name", unless = "#result == null")
    public ZabbixHostGroup getByName(ZabbixConfig.Zabbix zabbix, String name) {
        ZabbixFilter filter = ZabbixFilterBuilder.builder()
                .putEntry("name", name)
                .build();
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostGroupAPIMethod.GET)
                .filter(filter)
                .build();
        JsonNode data = call(zabbix, request);
        return mapperListGetOne(data.get(RESULT), ZabbixHostGroup.class);
    }

    public void create(ZabbixConfig.Zabbix zabbix, String name) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostGroupAPIMethod.CREATE)
                .paramEntry("name", name)
                .build();
        JsonNode data = call(zabbix, request);
        if (data.get(RESULT).get("groupids").isEmpty()) {
            log.error("ZabbixHostGroup创建失败");
        }
    }

}
