package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixProblem;
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
 * @Date 2021/7/2 7:00 下午
 * @Since 1.0
 */

@Component
public class ZabbixProblemHandler {

    @Resource
    private ZabbixHandler zabbixHandler;

    private interface Method {
        String QUERY_PROBLEM = "problem.get";
    }

    public List<ZabbixProblem> listProblems(DsZabbixConfig.Zabbix zabbix) {
        ZabbixRequest request = ZabbixRequestBuilder.builder()
                .method(Method.QUERY_PROBLEM)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get("result"), ZabbixProblem.class);
    }

    public List<ZabbixProblem> listProblemsByHost(DsZabbixConfig.Zabbix zabbix, ZabbixHost host) {
        ZabbixRequest request = ZabbixRequestBuilder.builder()
                .method(Method.QUERY_PROBLEM)
                .paramEntry("hostids", host.getHostId())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get("result"), ZabbixProblem.class);
    }

    public ZabbixProblem getProblemById(DsZabbixConfig.Zabbix zabbix, String eventId) {
        ZabbixRequest request = ZabbixRequestBuilder.builder()
                .method(Method.QUERY_PROBLEM)
                .paramEntry("eventids", eventId)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        List<ZabbixProblem> hosts = ZabbixMapper.mapperList(data.get("result"), ZabbixProblem.class);
        if (CollectionUtils.isEmpty(hosts))
            throw new RuntimeException("ZabbixProblem不存在");
        return hosts.get(0);
    }
}
