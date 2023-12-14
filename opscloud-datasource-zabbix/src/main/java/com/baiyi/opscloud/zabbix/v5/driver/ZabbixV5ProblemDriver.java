package com.baiyi.opscloud.zabbix.v5.driver;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.constant.SeverityType;
import com.baiyi.opscloud.zabbix.v5.driver.base.SimpleZabbixAuth;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixProblem;
import com.baiyi.opscloud.zabbix.v5.feign.ZabbixProblemFeign;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixRequestBuilder;
import com.google.common.collect.Lists;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/11/19 4:38 下午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ZabbixV5ProblemDriver {

    protected final SimpleZabbixAuth simpleZabbixAuth;

    private interface ProblemAPIMethod {
        String GET = "problem.get";
    }

    private ZabbixProblemFeign buildFeign(ZabbixConfig.Zabbix config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(ZabbixProblemFeign.class, config.getUrl());
    }

    protected ZabbixProblem.QueryProblemResponse queryHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DefaultRequest request) {
        ZabbixProblemFeign zabbixAPI = buildFeign(config);
        request.setMethod(ProblemAPIMethod.GET);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.query(request);
    }

    public List<ZabbixProblem.Problem> list(ZabbixConfig.Zabbix config, List<SeverityType> severityTypes) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                /*
                 * true - 仅返回被抑制问题;
                 * false - 返回问题在正常状态。
                 */
                /*
                 * 只返回给定事件严重程度的问题。仅当对象是触发器时才应用。
                 */
                .putParam("severities", severityTypes.stream().map(SeverityType::getType).collect(Collectors.toList()))
                .putParam("recent", "true")
                .build();
        ZabbixProblem.QueryProblemResponse response = queryHandle(config, request);
        return response.getResult();
    }

    public List<ZabbixProblem.Problem> listByHost(ZabbixConfig.Zabbix config, ZabbixHost.Host host) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("hostids", host.getHostid())
                .build();
        ZabbixProblem.QueryProblemResponse response = queryHandle(config, request);
        return response.getResult();
    }

    public ZabbixProblem.Problem getByEventId(ZabbixConfig.Zabbix config, String eventId) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("eventids", eventId)
                .build();
        ZabbixProblem.QueryProblemResponse response = queryHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult())) {
            throw new RuntimeException("ZabbixProblem不存在");
        }
        return response.getResult().getFirst();
    }

    public ZabbixProblem.Problem getByTriggerId(ZabbixConfig.Zabbix config, String triggerId) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("selectAcknowledges", "extend")
                .putParam("objectids", triggerId)
                .putParam("recent", "true")
                .putParam("sortfield", Lists.newArrayList("eventid"))
                .putParam("sortorder", "DESC")
                .build();
        ZabbixProblem.QueryProblemResponse response = queryHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult())) {
            throw new RuntimeException("ZabbixProblem不存在");
        }
        return response.getResult().getFirst();
    }

}