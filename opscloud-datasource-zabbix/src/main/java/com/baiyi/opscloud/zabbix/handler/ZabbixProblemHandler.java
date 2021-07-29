package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixProblem;
import com.baiyi.opscloud.zabbix.http.ZabbixCommonRequest;
import com.baiyi.opscloud.zabbix.http.ZabbixCommonRequestBuilder;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.zabbix.handler.ZabbixHandler.ApiConstant.*;

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
        ZabbixCommonRequest request = ZabbixCommonRequestBuilder.builder()
                .method(Method.QUERY_PROBLEM)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixProblem.class);
    }

    public List<ZabbixProblem> listProblemsByHost(DsZabbixConfig.Zabbix zabbix, ZabbixHost host) {
        ZabbixCommonRequest request = ZabbixCommonRequestBuilder.builder()
                .method(Method.QUERY_PROBLEM)
                .paramEntry(HOST_IDS, host.getHostId())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixProblem.class);
    }

    public ZabbixProblem getProblemById(DsZabbixConfig.Zabbix zabbix, String eventId) {
        ZabbixCommonRequest request = ZabbixCommonRequestBuilder.builder()
                .method(Method.QUERY_PROBLEM)
                .paramEntry(EVENT_IDS, eventId)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        List<ZabbixProblem> hosts = ZabbixMapper.mapperList(data.get(RESULT), ZabbixProblem.class);
        if (CollectionUtils.isEmpty(hosts))
            throw new RuntimeException("ZabbixProblem不存在");
        return hosts.get(0);
    }
}
