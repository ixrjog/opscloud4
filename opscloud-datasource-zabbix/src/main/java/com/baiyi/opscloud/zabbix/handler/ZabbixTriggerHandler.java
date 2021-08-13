package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixTrigger;
import com.baiyi.opscloud.zabbix.handler.base.ZabbixServer;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequest;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.zabbix.handler.base.ZabbixServer.ApiConstant.*;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/2 3:40 下午
 * @Since 1.0
 */

@Component
public class ZabbixTriggerHandler {

    @Resource
    private ZabbixServer zabbixHandler;

    private interface Method {
        String QUERY_TRIGGER = "trigger.get";
    }

    private SimpleZabbixRequestBuilder queryRequestBuilder() {
        return SimpleZabbixRequestBuilder.builder()
                .method(Method.QUERY_TRIGGER)
                // 只返回属于受监控主机的启用的触发器（与上条意思差不多，至于什么区别，未测）
                .paramEntry("active", 1)
                // 跳过依赖于其他触发器的问题状态中的触发器。请注意，如果禁用了其他触发器，则会禁用其他触发器，禁用项目或禁用项目主机。
                .paramEntry("skipDependent", 1)
                // 属于受监控主机的已启用触发器，并仅包含已启用的项目
                .paramEntry("monitored", 1)
                // 只返回最近处于问题状态的触发器（处于告警状态的主机）
                .paramEntry("only_true", 1)
                // 在触发器描述中展开宏（Expand macros in the name of the trigger.）
                .paramEntry("expandDescription", 1);
    }

    public List<ZabbixTrigger> listTriggers(DsZabbixConfig.Zabbix zabbix) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixTrigger.class);
    }

    public List<ZabbixTrigger> listTriggerByHost(DsZabbixConfig.Zabbix zabbix, ZabbixHost host) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .paramEntry(HOST_IDS, host.getHostid())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixTrigger.class);
    }

    public ZabbixTrigger getTriggerById(DsZabbixConfig.Zabbix zabbix, String triggerId) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .paramEntry(TRIGGER_IDS, triggerId)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        List<ZabbixTrigger> hosts = ZabbixMapper.mapperList(data.get(RESULT), ZabbixTrigger.class);
        if (CollectionUtils.isEmpty(hosts)) {
            throw new RuntimeException("ZabbixTrigger不存在");
        }
        return hosts.get(0);
    }
}
