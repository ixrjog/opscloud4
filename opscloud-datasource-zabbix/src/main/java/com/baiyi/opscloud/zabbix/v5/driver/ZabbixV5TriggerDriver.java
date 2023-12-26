package com.baiyi.opscloud.zabbix.v5.driver;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.v5.driver.base.SimpleZabbixAuth;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixTrigger;
import com.baiyi.opscloud.zabbix.v5.feign.ZabbixTriggerFeign;
import com.baiyi.opscloud.zabbix.v5.request.ZabbixRequest;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.v5.request.builder.ZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.constant.SeverityType;
import feign.Feign;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/19 11:09 上午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ZabbixV5TriggerDriver {

    protected final SimpleZabbixAuth simpleZabbixAuth;

    private static final int PROBLEM = 1;

    private static final String[] TRIGGER_OUTPUT = {"triggerid", "itemid", "description", "priority", "value", "lastchange", "hosts"};

    private interface TriggerAPIMethod {
        String GET = "trigger.get";
    }

    private ZabbixTriggerFeign buildFeign(ZabbixConfig.Zabbix config) {
        return Feign.builder()
                .retryer(new Retryer.Default(3000, 3000, 3))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(ZabbixTriggerFeign.class, config.getUrl());
    }

    private ZabbixTrigger.QueryTriggerResponse queryHandle(ZabbixConfig.Zabbix config, ZabbixRequest.DefaultRequest request) {
        ZabbixTriggerFeign zabbixAPI = buildFeign(config);
        request.setMethod(TriggerAPIMethod.GET);
        request.setAuth(simpleZabbixAuth.getAuth(config));
        return zabbixAPI.query(request);
    }

    /**
     * Severity of the trigger.
     * Possible values are:
     * 0 - (default) not classified;
     * 1 - information;
     * 2 - warning;
     * 3 - average;
     * 4 - high;
     * 5 - disaster.
     *
     * @param severityType
     */
    public List<ZabbixTrigger.Trigger> getBySeverityType(ZabbixConfig.Zabbix config, SeverityType severityType) {
        /*
         * filter
         * (readonly 只读) Whether the trigger is in OK or problem state. 触发器是否处于正常或故障状态。
         Possible values are: 许可值为：
         0 - (default 默认) OK; 正常；
         1 - problem. 故障。
         */
        // https://www.zabbix.com/documentation/5.0/manual/api/reference/trigger/get
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                .putParam("output", TRIGGER_OUTPUT)
                .putParam("selectFunctions", "extend")
                .filter(ZabbixFilterBuilder.builder()
                        .putEntry("value", PROBLEM)
                        .build())
                // 只返回属于受监控主机的启用的触发器（与上条意思差不多，至于什么区别，未测）
                .putParam("active", 1)
                // 排序
                .putParam("sortfield", "priority")
                // 正排还是倒排
                .putParam("sortorder", "DESC")
                // 大于等于给定的触发器级别，这里是大于等于严重
                .putParam("min_severity", severityType.getType())
                // 跳过依赖于其他触发器的问题状态中的触发器。请注意，如果禁用了其他触发器，则会禁用其他触发器，禁用项目或禁用项目主机。
                .putParam("skipDependent", 1)
                // 在结果中返回关联的主机信息（意思就是显示出那台主机告警的）
                .putParam("selectHosts", "hosts")
                // 属于受监控主机的已启用触发器，并仅包含已启用的项目
                .putParam("monitored", 1)
                // 只返回最近处于问题状态的触发器（处于告警状态的主机）
                .putParam("only_true", 1)
                // 在触发器描述中展开宏（Expand macros in the name of the trigger.）
                .putParam("expandDescription", 1)
                .build();
        ZabbixTrigger.QueryTriggerResponse response = queryHandle(config, request);
        return response.getResult();
    }

    public List<ZabbixTrigger.Trigger> list(ZabbixConfig.Zabbix config) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                // 只返回属于受监控主机的启用的触发器（与上条意思差不多，至于什么区别，未测）
                .putParam("active", 1)
                // 跳过依赖于其他触发器的问题状态中的触发器。请注意，如果禁用了其他触发器，则会禁用其他触发器，禁用项目或禁用项目主机。
                .putParam("skipDependent", 1)
                // 属于受监控主机的已启用触发器，并仅包含已启用的项目
                .putParam("monitored", 1)
                // 只返回最近处于问题状态的触发器（处于告警状态的主机）
                .putParam("only_true", 1)
                // 在触发器描述中展开宏（Expand macros in the name of the trigger.）
                .putParam("expandDescription", 1)
                .build();
        ZabbixTrigger.QueryTriggerResponse response = queryHandle(config, request);
        return response.getResult();
    }

    public List<ZabbixTrigger.Trigger> listByHost(ZabbixConfig.Zabbix config, ZabbixHost.Host host) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                // 只返回属于受监控主机的启用的触发器（与上条意思差不多，至于什么区别，未测）
                .putParam("active", 1)
                // 跳过依赖于其他触发器的问题状态中的触发器。请注意，如果禁用了其他触发器，则会禁用其他触发器，禁用项目或禁用项目主机。
                .putParam("skipDependent", 1)
                // 属于受监控主机的已启用触发器，并仅包含已启用的项目
                .putParam("monitored", 1)
                // 只返回最近处于问题状态的触发器（处于告警状态的主机）
                .putParam("only_true", 1)
                // 在触发器描述中展开宏（Expand macros in the name of the trigger.）
                .putParam("expandDescription", 1)
                .putParam("hostids", host.getHostid())
                .build();
        ZabbixTrigger.QueryTriggerResponse response = queryHandle(config, request);
        return response.getResult();
    }

    public ZabbixTrigger.Trigger getById(ZabbixConfig.Zabbix config, String triggerId) {
        ZabbixRequest.DefaultRequest request = ZabbixRequestBuilder.builder()
                // 只返回属于受监控主机的启用的触发器（与上条意思差不多，至于什么区别，未测）
                .putParam("active", 1)
                // 跳过依赖于其他触发器的问题状态中的触发器。请注意，如果禁用了其他触发器，则会禁用其他触发器，禁用项目或禁用项目主机。
                .putParam("skipDependent", 1)
                // 属于受监控主机的已启用触发器，并仅包含已启用的项目
                .putParam("monitored", 1)
                // 只返回最近处于问题状态的触发器（处于告警状态的主机）
                .putParam("only_true", 1)
                // 在结果中返回关联的主机信息（意思就是显示出那台主机告警的）
                .putParam("selectHosts", "hosts")
                // 在触发器描述中展开宏（Expand macros in the name of the trigger.）
                .putParam("expandDescription", 1)
                .putParam("triggerids", triggerId)
                .build();
        ZabbixTrigger.QueryTriggerResponse response = queryHandle(config, request);
        if (CollectionUtils.isEmpty(response.getResult())) {
            return null;
        }
        return response.getResult().getFirst();
    }

}