package com.baiyi.opscloud.event.process;

import com.baiyi.opscloud.common.datasource.ZabbixDsInstanceConfig;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.model.DsInstanceContext;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.Event;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.param.event.EventParam;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.event.convert.EventConvert;
import com.baiyi.opscloud.event.enums.EventTypeEnum;
import com.baiyi.opscloud.event.process.base.AbstractEventProcess;
import com.baiyi.opscloud.facade.server.SimpleServerNameFacade;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostInterface;
import com.baiyi.opscloud.zabbix.entry.ZabbixProblem;
import com.baiyi.opscloud.zabbix.entry.ZabbixTrigger;
import com.baiyi.opscloud.zabbix.handler.ZabbixHostHandler;
import com.baiyi.opscloud.zabbix.handler.ZabbixProblemHandler;
import com.baiyi.opscloud.zabbix.handler.ZabbixTriggerHandler;
import com.baiyi.opscloud.zabbix.param.base.SeverityType;
import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2021/10/9 3:13 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixEventProcess extends AbstractEventProcess<ZabbixProblem> {

    @Resource
    private ZabbixTriggerHandler zabbixTriggerHandler;

    @Resource
    private ZabbixProblemHandler zabbixProblemHandler;

    @Resource
    private ZabbixHostHandler zabbixHostHandler;

    private static final List<SeverityType> severityTypes = Lists.newArrayList(SeverityType.HIGH, SeverityType.DISASTER);

    protected ZabbixDsInstanceConfig getConfig(String instanceUuid) {
        DsInstanceContext context = buildDsInstanceContext(instanceUuid);
        return dsFactory.build(context.getDsConfig(), ZabbixDsInstanceConfig.class);
    }

    @Override
    public DataTable<Event> listEvent(EventParam.UserPermissionEventPageQuery pageQuery) {
        return eventService.queryUserPermissionServerEventByParam(pageQuery);
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
    protected void recordEventBusiness(DatasourceInstance dsInstance, Event event) {
        ZabbixProblem problem = new GsonBuilder().create().fromJson(event.getEventMessage(), ZabbixProblem.class);
        ZabbixDsInstanceConfig config = getConfig(dsInstance.getUuid());
        ZabbixTrigger trigger = zabbixTriggerHandler.getById(config.getZabbix(), problem.getObjectid());
        if (trigger == null) {
            log.info("Zabbix Trigger 不存在: problemId = {}, triggerId = {}", problem.getEventid(), problem.getObjectid());
            return;
        }
        log.info("Zabbix Trigger 存在: problemId = {}, triggerId = {}", problem.getEventid(), problem.getObjectid());
        List<ZabbixHost> hosts = trigger.getHosts();
        if (!CollectionUtils.isEmpty(hosts)) {
            for (ZabbixHost h : hosts) {
                ZabbixHost zabbixHost = zabbixHostHandler.getById(config.getZabbix(), h.getHostid());
                List<ZabbixHostInterface> interfaces = zabbixHost.getInterfaces();
                // filter type = agent interface
                Optional<ZabbixHostInterface> optional = interfaces.stream().filter(i -> i.getType() == 1).findFirst();
                if (optional.isPresent()) {
                    Server server = serverService.getByPrivateIp(optional.get().getIp());
                    if (server != null) {
                        ServerVO.Server iBusiness = BeanCopierUtil.copyProperties(server, ServerVO.Server.class);
                        recordEventBusiness(event, iBusiness, SimpleServerNameFacade.toServerName(iBusiness));
                    }
                }
            }
        }
    }

    @Override
    public EventTypeEnum getEventType() {
        return EventTypeEnum.ZABBIX_PROBLEM;
    }

}
