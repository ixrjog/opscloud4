package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.http.ZabbixRequest;
import com.baiyi.opscloud.zabbix.http.ZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/1 2:12 下午
 * @Since 1.0
 */
@Component
public class ZabbixHostGroupHandler {

    @Resource
    private ZabbixHandler zabbixHandler;

    private interface Method {
        String QUERY_GROUP = "hostgroup.get";
    }

    public List<ZabbixHostGroup> listGroups(DsZabbixConfig.Zabbix zabbix) {
        ZabbixRequest request = ZabbixRequestBuilder.builder()
                .method(Method.QUERY_GROUP)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get("result"), ZabbixHostGroup.class);
    }

    public List<ZabbixHostGroup> listGroupsByHost(DsZabbixConfig.Zabbix zabbix, ZabbixHost host) {
        ZabbixRequest request = ZabbixRequestBuilder.builder()
                .method(Method.QUERY_GROUP)
                .paramEntry("hostids", host.getHostId())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get("result"), ZabbixHostGroup.class);
    }

    public ZabbixHostGroup getGroupById(DsZabbixConfig.Zabbix zabbix, String groupId) {
        ZabbixRequest request = ZabbixRequestBuilder.builder()
                .method(Method.QUERY_GROUP)
                .paramEntry("groupids", groupId)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        List<ZabbixHostGroup> groups = ZabbixMapper.mapperList(data.get("result"), ZabbixHostGroup.class);
        if (CollectionUtils.isEmpty(groups))
            throw new RuntimeException("ZabbixHostGroup不存在");
        return groups.get(0);
    }
}
