package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostGroup;
import com.baiyi.opscloud.zabbix.http.ZabbixFilter;
import com.baiyi.opscloud.zabbix.http.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.http.ZabbixCommonRequest;
import com.baiyi.opscloud.zabbix.http.ZabbixCommonRequestBuilder;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.zabbix.handler.ZabbixHandler.ApiConstant.*;

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
        String CREATE_GROUP = "hostgroup.create";
    }

    private ZabbixCommonRequestBuilder queryRequestBuilder() {
        return ZabbixCommonRequestBuilder.builder()
                .method(Method.QUERY_GROUP);
    }

    public List<ZabbixHostGroup> listGroups(DsZabbixConfig.Zabbix zabbix) {
        ZabbixCommonRequest request = queryRequestBuilder()
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixHostGroup.class);
    }

    public List<ZabbixHostGroup> listGroupsByHost(DsZabbixConfig.Zabbix zabbix, ZabbixHost host) {
        ZabbixCommonRequest request = queryRequestBuilder()
                .paramEntry(HOST_IDS, host.getHostId())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixHostGroup.class);
    }

    public ZabbixHostGroup getGroupById(DsZabbixConfig.Zabbix zabbix, String groupId) {
        ZabbixCommonRequest request = queryRequestBuilder()
                .paramEntry(HOST_GROUP_IDS, groupId)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        List<ZabbixHostGroup> groups = ZabbixMapper.mapperList(data.get(RESULT), ZabbixHostGroup.class);
        if (CollectionUtils.isEmpty(groups))
            throw new RuntimeException("ZabbixHostGroup不存在");
        return groups.get(0);
    }

    public List<ZabbixHostGroup> listGroupByNames(DsZabbixConfig.Zabbix zabbix, List<String> names) {
        ZabbixFilter filter = ZabbixFilterBuilder.builder()
                .putEntry("name", names)
                .build();
        ZabbixCommonRequest request = queryRequestBuilder()
                .filter(filter)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixHostGroup.class);
    }

    public String createGroup(DsZabbixConfig.Zabbix zabbix, String name) {
        List<ZabbixHostGroup> groups = listGroupByNames(zabbix, Lists.newArrayList(name));
        if (!CollectionUtils.isEmpty(groups))
            return groups.get(0).getGroupId();
        ZabbixCommonRequest request = ZabbixCommonRequestBuilder.builder()
                .method(Method.CREATE_GROUP)
                .paramEntry("name", name)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        if (data.get(RESULT).get(HOST_GROUP_IDS).isEmpty()) {
            throw new RuntimeException("ZabbixHost创建失败");
        }
        return ZabbixMapper.mapperList(data.get(RESULT).get(HOST_GROUP_IDS), String.class).get(0);
    }

}
