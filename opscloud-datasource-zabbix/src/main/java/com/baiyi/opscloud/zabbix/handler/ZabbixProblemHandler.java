package com.baiyi.opscloud.zabbix.handler;

import com.baiyi.opscloud.common.datasource.config.DsZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixProblem;
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
 * @Date 2021/7/2 7:00 下午
 * @Since 1.0
 */

@Component
public class ZabbixProblemHandler {

    @Resource
    private ZabbixServer zabbixHandler;

    private interface Method {
        String QUERY_PROBLEM = "problem.get";
    }

    private SimpleZabbixRequestBuilder queryRequestBuilder() {
        return SimpleZabbixRequestBuilder.builder()
                .method(Method.QUERY_PROBLEM);
    }

    public List<ZabbixProblem> listProblems(DsZabbixConfig.Zabbix zabbix) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixProblem.class);
    }

    public List<ZabbixProblem> listProblemsByHost(DsZabbixConfig.Zabbix zabbix, ZabbixHost host) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .paramEntry(HOST_IDS, host.getHostId())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixProblem.class);
    }

    public ZabbixProblem getProblemById(DsZabbixConfig.Zabbix zabbix, String eventId) {
        SimpleZabbixRequest request = queryRequestBuilder()
                .paramEntry(EVENT_IDS, eventId)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        List<ZabbixProblem> hosts = ZabbixMapper.mapperList(data.get(RESULT), ZabbixProblem.class);
        if (CollectionUtils.isEmpty(hosts))
            throw new RuntimeException("ZabbixProblem不存在");
        return hosts.get(0);
    }
}
