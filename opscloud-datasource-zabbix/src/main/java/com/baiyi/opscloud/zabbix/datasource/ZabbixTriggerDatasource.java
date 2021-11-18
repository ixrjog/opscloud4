package com.baiyi.opscloud.zabbix.datasource;

import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.zabbix.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.entity.ZabbixTrigger;
import com.baiyi.opscloud.zabbix.datasource.base.BaseZabbixDatasource;
import com.baiyi.opscloud.zabbix.datasource.base.ZabbixServer;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequest;
import com.baiyi.opscloud.zabbix.http.SimpleZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.http.ZabbixFilter;
import com.baiyi.opscloud.zabbix.http.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.param.base.SeverityType;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.zabbix.datasource.base.ZabbixServer.ApiConstant.HOST_IDS;
import static com.baiyi.opscloud.zabbix.datasource.base.ZabbixServer.ApiConstant.RESULT;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/7/2 3:40 下午
 * @Since 1.0
 */

@Component
public class ZabbixTriggerDatasource extends BaseZabbixDatasource<ZabbixTrigger> {

    @Resource
    private ZabbixServer zabbixHandler;

    private static final int PROBLEM = 1;

    private static final String[] TRIGGER_OUTPUT = {"triggerid", "itemid", "description", "priority", "value", "lastchange", "hosts"};

    private interface TriggerAPIMethod {
        String GET = "trigger.get";
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
    public List<ZabbixTrigger> getBySeverityType(ZabbixConfig.Zabbix zabbix, SeverityType severityType) {

        /**
         * (readonly 只读) Whether the trigger is in OK or problem state. 触发器是否处于正常或故障状态。
         Possible values are: 许可值为：
         0 - (default 默认) OK; 正常；
         1 - problem. 故障。
         */
        ZabbixFilter filter = ZabbixFilterBuilder.builder()
                .putEntry("value", PROBLEM)
                .build();
        // https://www.zabbix.com/documentation/5.0/manual/api/reference/trigger/get
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(TriggerAPIMethod.GET)
                .putParam("output", TRIGGER_OUTPUT)
                .putParam("selectFunctions", "extend")
                .filter(filter)
                .putParam("active", 1) // 只返回属于受监控主机的启用的触发器（与上条意思差不多，至于什么区别，未测）
                .putParam("sortfield", "priority") // 排序
                .putParam("sortorder", "DESC") // 正排还是倒排
                .putParam("min_severity", severityType.getType()) // 大于等于给定的触发器级别，这里是大于等于严重
                .putParam("skipDependent", 1) // 跳过依赖于其他触发器的问题状态中的触发器。请注意，如果禁用了其他触发器，则会禁用其他触发器，禁用项目或禁用项目主机。
                .putParam("selectHosts", "hosts") // 在结果中返回关联的主机信息（意思就是显示出那台主机告警的）
                .putParam("monitored", 1) // 属于受监控主机的已启用触发器，并仅包含已启用的项目
                .putParam("only_true", 1) // 只返回最近处于问题状态的触发器（处于告警状态的主机）
                .putParam("expandDescription", 1) // 在触发器描述中展开宏（Expand macros in the name of the trigger.）
                .build();

        JsonNode data = zabbixHandler.call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixTrigger.class);
    }

    public List<ZabbixTrigger> list(ZabbixConfig.Zabbix zabbix) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(TriggerAPIMethod.GET)
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
        JsonNode data = zabbixHandler.call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixTrigger.class);
    }

    public List<ZabbixTrigger> listByHost(ZabbixConfig.Zabbix zabbix, ZabbixHost host) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(TriggerAPIMethod.GET)
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
                .putParam(HOST_IDS, host.getHostid())
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return mapperList(data.get(RESULT), ZabbixTrigger.class);
    }

    public ZabbixTrigger getById(ZabbixConfig.Zabbix zabbix, String triggerId) {
        SimpleZabbixRequest request = SimpleZabbixRequestBuilder.builder()
                .method(TriggerAPIMethod.GET)
                // 只返回属于受监控主机的启用的触发器（与上条意思差不多，至于什么区别，未测）
                .putParam("active", 1)
                // 跳过依赖于其他触发器的问题状态中的触发器。请注意，如果禁用了其他触发器，则会禁用其他触发器，禁用项目或禁用项目主机。
                .putParam("skipDependent", 1)
                // 属于受监控主机的已启用触发器，并仅包含已启用的项目
                .putParam("monitored", 1)
                // 只返回最近处于问题状态的触发器（处于告警状态的主机）
                .putParam("only_true", 1)
                .putParam("selectHosts", "hosts") // 在结果中返回关联的主机信息（意思就是显示出那台主机告警的）
                // 在触发器描述中展开宏（Expand macros in the name of the trigger.）
                .putParam("expandDescription", 1)
                .putParam("triggerids", triggerId)
                .build();
        JsonNode data = zabbixHandler.call(zabbix, request);
        return mapperListGetOne(data.get(RESULT), ZabbixTrigger.class);
    }
}
