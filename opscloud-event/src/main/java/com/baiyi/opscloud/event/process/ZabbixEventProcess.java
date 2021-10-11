package com.baiyi.opscloud.event.process;

import com.baiyi.opscloud.common.datasource.ZabbixDsInstanceConfig;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.Event;
import com.baiyi.opscloud.event.convert.EventConvert;
import com.baiyi.opscloud.event.enums.EventTypeEnum;
import com.baiyi.opscloud.event.process.base.AbstractEventProcess;
import com.baiyi.opscloud.zabbix.entry.ZabbixProblem;
import com.baiyi.opscloud.zabbix.entry.ZabbixTrigger;
import com.baiyi.opscloud.zabbix.handler.ZabbixProblemHandler;
import com.baiyi.opscloud.zabbix.handler.ZabbixTriggerHandler;
import com.baiyi.opscloud.zabbix.param.base.SeverityType;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/10/9 3:13 下午
 * @Version 1.0
 */
@Component
public class ZabbixEventProcess extends AbstractEventProcess<ZabbixProblem> {

    @Resource
    private ZabbixTriggerHandler zabbixTriggerHandler;

    @Resource
    private ZabbixProblemHandler zabbixProblemHandler;

    private static final List<SeverityType> severityTypes = Lists.newArrayList(SeverityType.HIGH, SeverityType.DISASTER);

    protected ZabbixDsInstanceConfig getConfig(String instanceUuid) {
        DsInstanceContext context = buildDsInstanceContext(instanceUuid);
        return dsFactory.build(context.getDsConfig(), ZabbixDsInstanceConfig.class);
    }

    @Override
    protected DsTypeEnum getDsInstanceType() {
        return DsTypeEnum.ZABBIX;
    }

    @Override
    protected Event toEvent(DatasourceInstance dsInstance, ZabbixProblem zabbixProblem) {
        ZabbixTrigger zabbixTrigger = zabbixTriggerHandler.getById(getConfig(dsInstance.getUuid()).getZabbix(), zabbixProblem.getObjectid());
        return EventConvert.toEvent(dsInstance, zabbixProblem, zabbixTrigger);
    }

    @Override
    protected List<ZabbixProblem> listeningEvents(DatasourceInstance dsInstance) {
        return zabbixProblemHandler.list(getConfig(dsInstance.getUuid()).getZabbix(), severityTypes);
    }

    @Override
    protected ZabbixProblem getByEventId(String instanceUuid, String eventId) {
        return zabbixProblemHandler.getByEventId(getConfig(instanceUuid).getZabbix(), eventId);
    }

    @Override
    public EventTypeEnum getEventType() {
        return EventTypeEnum.ZABBIX_PROBLEM;
    }

}
