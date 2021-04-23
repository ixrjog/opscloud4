package com.baiyi.opscloud.zabbix.server.impl;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.zabbix.api.TriggerAPI;
import com.baiyi.opscloud.zabbix.base.SeverityType;
import com.baiyi.opscloud.zabbix.builder.ZabbixFilterBuilder;
import com.baiyi.opscloud.zabbix.builder.ZabbixRequestBuilder;
import com.baiyi.opscloud.zabbix.entry.ZabbixTrigger;
import com.baiyi.opscloud.zabbix.handler.ZabbixHandler;
import com.baiyi.opscloud.zabbix.http.ZabbixBaseRequest;
import com.baiyi.opscloud.zabbix.mapper.ZabbixTriggerMapper;
import com.baiyi.opscloud.zabbix.param.ZabbixFilter;
import com.baiyi.opscloud.zabbix.server.ZabbixProblemServer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/2/18 10:16 上午
 * @Version 1.0
 */
@Slf4j
@Component("ZabbixProblemServer")
public class ZabbixProblemServerImpl implements ZabbixProblemServer {

    @Resource
    private ZabbixHandler zabbixHandler;

    @Resource
    private RedisUtil redisUtil;

    // 600秒
    public static final Long CACHE_TIME = 600L;

    private static final String[] TRIGGER_OUTPUT = {"triggerid", "itemid", "description", "priority", "value", "lastchange", "hosts"};

    private static final int PROBLEM = 1;

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
    @Override
    public List<ZabbixTrigger> getTriggers(SeverityType severityType) {

        /**
         * (readonly 只读) Whether the trigger is in OK or problem state. 触发器是否处于正常或故障状态。
         Possible values are: 许可值为：
         0 - (default 默认) OK; 正常；
         1 - problem. 故障。
         */
        ZabbixFilter filter = ZabbixFilterBuilder.newBuilder()
                .putEntry("value", PROBLEM)
                .build();
        // https://www.zabbix.com/documentation/5.0/manual/api/reference/trigger/get
        ZabbixBaseRequest request = ZabbixRequestBuilder.newBuilder()
                .method(TriggerAPI.GET)
                .paramEntry("output", TRIGGER_OUTPUT)
                .paramEntryByFilter(filter)
                .paramEntry("active", 1) // 只返回属于受监控主机的启用的触发器（与上条意思差不多，至于什么区别，未测）
                .paramEntry("sortfield", "priority") // 排序
                .paramEntry("sortorder", "DESC") // 正排还是倒排
                .paramEntry("min_severity", severityType.getType()) // 大于等于给定的触发器级别，这里是大于等于严重
                .paramEntry("skipDependent", 1) // 跳过依赖于其他触发器的问题状态中的触发器。请注意，如果禁用了其他触发器，则会禁用其他触发器，禁用项目或禁用项目主机。
                .paramEntry("selectHosts", "hosts") // 在结果中返回关联的主机信息（意思就是显示出那台主机告警的）
                .paramEntry("monitored", 1) // 属于受监控主机的已启用触发器，并仅包含已启用的项目
                .paramEntry("only_true", 1) // 只返回最近处于问题状态的触发器（处于告警状态的主机）
                .paramEntry("expandDescription", 1) // 在触发器描述中展开宏（Expand macros in the name of the trigger.）
                .build();
        try {
            JsonNode jsonNode = zabbixHandler.api(request);
            return new ZabbixTriggerMapper().mapFromJson(jsonNode.get(ZabbixServerImpl.ZABBIX_RESULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public void cacheTriggers(List<ZabbixTrigger> triggers) {
        if (CollectionUtils.isEmpty(triggers))
            redisUtil.del(buildCacheKey());

        Map<String, List<ZabbixTrigger>> triggerMap = Maps.newHashMap();
        triggers.forEach(e -> e.getHosts().forEach(h -> {
            if (triggerMap.containsKey(h.getHostid())) {
                triggerMap.get(h.getHostid()).add(e);
            } else {
                triggerMap.put(h.getHostid(), Lists.newArrayList(e));
            }
        }));
        redisUtil.set(buildCacheKey(), triggerMap, CACHE_TIME);
    }

    @Override
    public Map<String, List<ZabbixTrigger>> getTriggerMapForCache() {
        return (Map<String, List<ZabbixTrigger>>) redisUtil.get(buildCacheKey());
    }


    // 触发器问题列表
    private String buildCacheKey() {
        return Joiner.on("_").join("zabbix", "trigger", "problem");
    }

}
