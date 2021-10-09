package com.baiyi.opscloud.event.process;

import com.baiyi.opscloud.common.datasource.ZabbixDsInstanceConfig;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.Event;
import com.baiyi.opscloud.event.convert.EventConvert;
import com.baiyi.opscloud.event.enums.EventTypeEnum;
import com.baiyi.opscloud.zabbix.entry.ZabbixTrigger;
import com.baiyi.opscloud.zabbix.handler.ZabbixTriggerHandler;
import com.baiyi.opscloud.zabbix.param.base.SeverityType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/10/9 3:13 下午
 * @Version 1.0
 */
@Component
public class ZabbixEventProcess extends AbstractEventProcess<ZabbixTrigger> {

    @Resource
    private ZabbixTriggerHandler zabbixTriggerHandler;

    private static final DsTypeEnum instanceType = DsTypeEnum.ZABBIX;

    protected ZabbixDsInstanceConfig getConfig(DatasourceInstance dsInstance) {
        DatasourceConfig datasourceConfig = dsConfigService.getById(dsInstance.getId());
        return dsFactory.build(datasourceConfig, ZabbixDsInstanceConfig.class);
    }

    @Override
    protected DsTypeEnum getDsInstanceType() {
        return instanceType;
    }

    @Override
    public Event toEvent(DatasourceInstance dsInstance, ZabbixTrigger zabbixTrigger) {
        return EventConvert.toEvent(dsInstance, zabbixTrigger);
    }

    @Override
    protected List<ZabbixTrigger> listeningEvents(DatasourceInstance dsInstance) {
        return zabbixTriggerHandler.getTriggersBySeverityType(getConfig(dsInstance).getZabbix(), SeverityType.AVERAGE);
    }

    @Override
    public EventTypeEnum getEventType() {
        return EventTypeEnum.ZABBIX_TRIGGER;
    }

}
