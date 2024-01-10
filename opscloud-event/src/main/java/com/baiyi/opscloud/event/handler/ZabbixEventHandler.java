package com.baiyi.opscloud.event.handler;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.Event;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.param.event.EventParam;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.event.converter.EventConverter;
import com.baiyi.opscloud.event.enums.EventTypeEnum;
import com.baiyi.opscloud.event.handler.base.AbstractEventHandler;
import com.baiyi.opscloud.zabbix.constant.SeverityType;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5HostDriver;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5ProblemDriver;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5TriggerDriver;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixProblem;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixTrigger;
import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/10/9 3:13 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class ZabbixEventHandler extends AbstractEventHandler<ZabbixProblem.Problem> {

    @Resource
    private ZabbixV5TriggerDriver zabbixV5TriggerDatasource;

    @Resource
    private ZabbixV5ProblemDriver zabbixV5ProblemDatasource;

    @Resource
    private ZabbixV5HostDriver zabbixV5HostDatasource;

    private static final List<SeverityType> SEVERITY_TYPES = Lists.newArrayList(SeverityType.HIGH, SeverityType.DISASTER);

    protected ZabbixConfig getConfig(String instanceUuid) {
        DsInstanceContext context = buildDsInstanceContext(instanceUuid);
        return dsFactory.build(context.getDsConfig(), ZabbixConfig.class);
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
    protected Event toEvent(DatasourceInstance dsInstance, ZabbixProblem.Problem zabbixProblem) {
        ZabbixTrigger.Trigger trigger = zabbixV5TriggerDatasource.getById(getConfig(dsInstance.getUuid()).getZabbix(), zabbixProblem.getObjectid());
        return EventConverter.toEvent(dsInstance, zabbixProblem, trigger);
    }

    @Override
    protected List<ZabbixProblem.Problem> listeningEvents(DatasourceInstance dsInstance) {
        ZabbixConfig zabbixConfig = getConfig(dsInstance.getUuid());
        List<SeverityType> severityTypes;
        if (CollectionUtils.isEmpty(zabbixConfig.getZabbix().getSeverityTypes())) {
            severityTypes = SEVERITY_TYPES;
        } else {
            severityTypes = zabbixConfig.getZabbix().getSeverityTypes().stream().map(SeverityType::getByName).collect(Collectors.toList());
        }
        return zabbixV5ProblemDatasource.list(zabbixConfig.getZabbix(), severityTypes);
    }

    @Override
    protected ZabbixProblem.Problem getByEventId(String instanceUuid, String eventId) {
        return zabbixV5ProblemDatasource.getByEventId(getConfig(instanceUuid).getZabbix(), eventId);
    }

    @Override
    protected void recordEventBusiness(DatasourceInstance dsInstance, Event event) {
        ZabbixProblem.Problem problem = new GsonBuilder().create().fromJson(event.getEventMessage(), ZabbixProblem.Problem.class);
        ZabbixConfig config = getConfig(dsInstance.getUuid());
        ZabbixTrigger.Trigger trigger = zabbixV5TriggerDatasource.getById(config.getZabbix(), problem.getObjectid());
        if (trigger == null) {
            log.info("Zabbix trigger not exist: problemId={}, triggerId={}", problem.getEventid(), problem.getObjectid());
            return;
        }
        log.info("Zabbix trigger exist: problemId={}, triggerId={}", problem.getEventid(), problem.getObjectid());
        List<ZabbixHost.Host> hosts = trigger.getHosts();
        if (!CollectionUtils.isEmpty(hosts)) {
            hosts.forEach(h -> recordEventBusiness(config, event, h));
        }
    }

    private void recordEventBusiness(ZabbixConfig zabbixConfig, Event event, ZabbixHost.Host host) {
        ZabbixHost.Host zabbixHost = zabbixV5HostDatasource.getById(zabbixConfig.getZabbix(), host.getHostid());
        List<ZabbixHost.HostInterface> interfaces = zabbixHost.getInterfaces();
        Optional<ZabbixHost.HostInterface> optional = interfaces.stream().filter(i -> i.getType() == 1).findFirst();
        if (optional.isPresent()) {
            Server server = serverService.getByPrivateIp(optional.get().getIp());
            if (server != null) {
                ServerVO.Server iBusiness = BeanCopierUtil.copyProperties(server, ServerVO.Server.class);
                recordEventBusiness(event, iBusiness, iBusiness.getDisplayName());
            }
        }
    }

    @Override
    public EventTypeEnum getEventType() {
        return EventTypeEnum.ZABBIX_PROBLEM;
    }

}