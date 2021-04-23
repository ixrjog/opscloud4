package com.baiyi.opscloud.zabbix.server.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.zabbix.api.HostgroupAPI;
import com.baiyi.opscloud.zabbix.builder.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.builder.ZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostgroup;
import com.baiyi.opscloud.zabbix.handler.ZabbixHandler;
import com.baiyi.opscloud.zabbix.http.ZabbixBaseRequest;
import com.baiyi.opscloud.zabbix.mapper.ZabbixHostgroupMapper;
import com.baiyi.opscloud.zabbix.mapper.ZabbixIdsMapper;
import com.baiyi.opscloud.zabbix.param.ZabbixFilter;
import com.baiyi.opscloud.zabbix.param.ZabbixHostgroupParam;
import com.baiyi.opscloud.zabbix.server.ZabbixHostgroupServer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/8 1:06 下午
 * @Version 1.0
 */
@Component("ZabbixHostgroupServer")
public class ZabbixHostgroupServerImpl implements ZabbixHostgroupServer {

    @Resource
    private ZabbixHandler zabbixHandler;

    @Override
    public ZabbixHostgroup createHostgroup(String hostgroup) {
        ZabbixHostgroup zabbixHostgroup = getHostgroup(hostgroup);
        if (zabbixHostgroup != null) return zabbixHostgroup;
        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(HostgroupAPI.CREATE)
                .paramEntry("name", hostgroup)
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            ZabbixHostgroupParam.Hostgroup hg =  ZabbixHostgroupParam.Hostgroup.builder()
                    .groupid(new ZabbixIdsMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT).get("groupids")).get(0))
                    .name(hostgroup)
                    .build();
            return BeanCopierUtils.copyProperties(hg, ZabbixHostgroup.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ZabbixHostgroup getHostgroup(String hostgroup) {
        ZabbixFilter filter = ZabbixFilterBuilder.newBuilder()
                .putEntry("name", hostgroup)
                .build();

        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(HostgroupAPI.GET)
                .paramEntryByFilter(filter)
                .paramEntry("output", "extend")
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixHostgroupMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT)).get(0);
        } catch (Exception e) {
           return null;
        }
    }


}
