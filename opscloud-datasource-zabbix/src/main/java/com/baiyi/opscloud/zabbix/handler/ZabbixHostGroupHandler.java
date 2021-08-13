package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.handler.base.BaseZabbixHandler;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequest;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.http.ZabbixFilter;
import com.baiyi.opscloud.zabbix.http.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.zabbix.handler.base.ZabbixServer.ApiConstant.RESULT;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/1 2:12 下午
 * @Since 1.0
 */
@Component
public class ZabbixHostGroupHandler extends BaseZabbixHandler<ZabbixHostGroup> {

    private interface HostGroupAPIMethod {
        String GET = "hostgroup.get";
        String CREATE = "hostgroup.create";
    }

    public List<ZabbixHostGroup> list(DsZabbixConfig.Zabbix zabbix) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostGroupAPIMethod.GET)
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixHostGroup.class);
    }

    public List<ZabbixHostGroup> listByHost(DsZabbixConfig.Zabbix zabbix, ZabbixHost host) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostGroupAPIMethod.GET)
                .paramEntry("hostids", host.getHostid())
                .build();
        JsonNode data = call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixHostGroup.class);
    }

    public ZabbixHostGroup getById(DsZabbixConfig.Zabbix zabbix, String groupId) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostGroupAPIMethod.GET)
                .paramEntry("groupids", groupId)
                .build();
        JsonNode data = call(zabbix, request);
        return   mapperListGetOne(data.get(RESULT), ZabbixHostGroup.class);
    }

    public ZabbixHostGroup getByName(DsZabbixConfig.Zabbix zabbix, String name) {
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

    public ZabbixHostGroup create(DsZabbixConfig.Zabbix zabbix, String name) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(HostGroupAPIMethod.CREATE)
                .paramEntry("name", name)
                .build();
        JsonNode data = call(zabbix, request);
        if (data.get(RESULT).get("groupids").isEmpty()) {
            throw new RuntimeException("ZabbixHost创建失败");
        }
        String groupid = ZabbixMapper.mapperList(data.get(RESULT).get("groupids"), String.class).get(0);
        return getById(zabbix,groupid);
    }

}
