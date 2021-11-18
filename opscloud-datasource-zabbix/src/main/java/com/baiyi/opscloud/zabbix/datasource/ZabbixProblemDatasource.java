package com.baiyi.opscloud.zabbix.datasource;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.datasource.base.ZabbixServer;
import com.baiyi.opscloud.zabbix.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.entity.ZabbixProblem;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequest;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.mapper.ZabbixMapper;
import com.baiyi.opscloud.zabbix.param.base.SeverityType;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.zabbix.datasource.base.ZabbixServer.ApiConstant.HOST_IDS;
import static com.baiyi.opscloud.zabbix.datasource.base.ZabbixServer.ApiConstant.RESULT;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/2 7:00 下午
 * @Since 1.0
 */

@Component
@RequiredArgsConstructor
public class ZabbixProblemDatasource {

    private final ZabbixServer zabbixServer;

    private interface ProblemAPIMethod {
        String GET = "problem.get";
    }

    public List<ZabbixProblem> list(ZabbixConfig.Zabbix zabbix, List<SeverityType> severityTypes) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(ProblemAPIMethod.GET)
                /**
                 * true - 仅返回被抑制问题;
                 * false - 返回问题在正常状态。
                 */
                /**
                 * 只返回给定事件严重程度的问题。仅当对象是触发器时才应用。
                 */
                .putParam("severities", severityTypes.stream().map(SeverityType::getType).collect(Collectors.toList()))
                .putParam("recent", "true")
                .build();
        JsonNode data = zabbixServer.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixProblem.class);
    }

    public List<ZabbixProblem> listByHost(ZabbixConfig.Zabbix zabbix, ZabbixHost host) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(ProblemAPIMethod.GET)
                .putParam(HOST_IDS, host.getHostid())
                .build();
        JsonNode data = zabbixServer.call(zabbix, request);
        return ZabbixMapper.mapperList(data.get(RESULT), ZabbixProblem.class);
    }

    public ZabbixProblem getByEventId(ZabbixConfig.Zabbix zabbix, String eventId) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(ProblemAPIMethod.GET)
                .putParam("eventids", eventId)
                .build();
        JsonNode data = zabbixServer.call(zabbix, request);
        List<ZabbixProblem> hosts = ZabbixMapper.mapperList(data.get(RESULT), ZabbixProblem.class);
        if (CollectionUtils.isEmpty(hosts))
            throw new RuntimeException("ZabbixProblem不存在");
        return hosts.get(0);
    }

    public ZabbixProblem getByTriggerId(ZabbixConfig.Zabbix zabbix, String triggerId) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(ProblemAPIMethod.GET)
                .putParam("selectAcknowledges", "extend")
                .putParam("objectids", triggerId)
                .putParam("recent", "true")
                .putParam("sortfield", Lists.newArrayList("eventid"))
                .putParam("sortorder", "DESC")
                .build();
        JsonNode data = zabbixServer.call(zabbix, request);
        List<ZabbixProblem> hosts = ZabbixMapper.mapperList(data.get(RESULT), ZabbixProblem.class);
        if (CollectionUtils.isEmpty(hosts))
            throw new RuntimeException("ZabbixProblem不存在");
        return hosts.get(0);
    }
}
