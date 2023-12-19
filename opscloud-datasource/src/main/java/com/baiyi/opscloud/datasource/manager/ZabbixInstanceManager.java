package com.baiyi.opscloud.datasource.manager;

import com.baiyi.opscloud.common.constants.enums.ServerMonitorStatusEnum;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.ZabbixConfig;
import com.baiyi.opscloud.common.util.IPUtil;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.manager.base.BaseManager;
import com.baiyi.opscloud.domain.constants.TagConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.service.server.ServerService;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5HostDriver;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/5/9 10:06
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ZabbixInstanceManager extends BaseManager {

    private final DsConfigService dsConfigService;

    private final DsConfigManager dsConfigManager;

    private final ZabbixV5HostDriver zabbixV5HostDriver;

    private final ServerService serverService;

    /**
     * 过滤实例类型
     */
    private static final DsTypeEnum[] FILTER_INSTANCE_TYPES = {DsTypeEnum.ZABBIX};

    @Override
    protected DsTypeEnum[] getFilterInstanceTypes() {
        return FILTER_INSTANCE_TYPES;
    }

    @Override
    protected String getTag() {
        return TagConstants.SERVER.getTag();
    }

    public void updateServerMonitorStatus(List<Server> servers) {
        if (CollectionUtils.isEmpty(servers)) {
            return;
        }
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            return;
        }
        List<ZabbixConfig> zabbixConfigs = instances.stream().map(i -> {
            DatasourceConfig datasourceConfig = dsConfigService.getById(i.getConfigId());
            return dsConfigManager.build(datasourceConfig, ZabbixConfig.class);
        }).collect(Collectors.toList());
        servers.forEach(s -> updateServerMonitorStatus(s, zabbixConfigs));
    }

    private void updateServerMonitorStatus(Server server, List<ZabbixConfig> zabbixConfigs) {
        // 只遍历有效服务器
        if (!server.getIsActive()) {
            return;
        }
        Optional<ZabbixConfig> optionalZabbixConfig = zabbixConfigs.stream()
                .filter(c -> IPUtil.includeMasks(server.getPrivateIp(), c.getZabbix().getRegions()))
                .findFirst();
        // 未匹配路由规则
        if (optionalZabbixConfig.isEmpty()) {
            return;
        }
        ZabbixConfig zabbixConfig = optionalZabbixConfig.get();
        ZabbixHost.Host host = zabbixV5HostDriver.getByIp(zabbixConfig.getZabbix(), server.getPrivateIp());
        if (host == null) {
            updateServerMonitorStatus(server, ServerMonitorStatusEnum.NOT_CREATED);
        } else {
            updateServerMonitorStatus(server, host.getStatus() == ServerMonitorStatusEnum.MONITORED.getStatus() ?
                    ServerMonitorStatusEnum.MONITORED : ServerMonitorStatusEnum.UNMONITORED);
        }
    }

    private void updateServerMonitorStatus(Server server, ServerMonitorStatusEnum serverMonitorStatusEnum) {
        if (serverMonitorStatusEnum.getStatus() != server.getMonitorStatus()) {
            log.debug("Update server monitor status: ip={}, monitorStatus={}", server.getPrivateIp(), serverMonitorStatusEnum.getStatus());
            server.setMonitorStatus(serverMonitorStatusEnum.getStatus());
            serverService.update(server);
        }
    }

}
