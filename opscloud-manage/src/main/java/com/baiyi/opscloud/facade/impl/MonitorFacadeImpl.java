package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.account.IAccount;
import com.baiyi.opscloud.account.factory.AccountFactory;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.decorator.monitor.MonitorHostDecorator;
import com.baiyi.opscloud.decorator.monitor.MonitorUserDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcAccount;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.param.monitor.MonitorHostParam;
import com.baiyi.opscloud.domain.param.monitor.MonitorUserParam;
import com.baiyi.opscloud.domain.vo.monitor.MonitorVO;
import com.baiyi.opscloud.facade.AccountFacade;
import com.baiyi.opscloud.facade.MonitorFacade;
import com.baiyi.opscloud.facade.ServerGroupFacade;
import com.baiyi.opscloud.server.IServer;
import com.baiyi.opscloud.server.factory.ServerFactory;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.server.ZabbixHostServer;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.common.base.Global.ASYNC_POOL_TASK_COMMON;

/**
 * @Author baiyi
 * @Date 2020/11/24 10:38 上午
 * @Version 1.0
 */
@Slf4j
@Service
public class MonitorFacadeImpl implements MonitorFacade {

    @Resource
    private OcServerService ocServerService;

    @Resource
    private MonitorHostDecorator monitorHostDecorator;

    @Resource
    private MonitorUserDecorator monitorUserDecorator;

    @Resource
    private ZabbixHostServer zabbixHostServer;

    @Resource
    private AccountFacade accountFacade;

    @Resource
    private ServerGroupFacade serverGroupFacade;


    @Override
    public DataTable<MonitorVO.Host> queryMonitorHostPage(MonitorHostParam.MonitorHostPageQuery pageQuery) {
        DataTable<OcServer> table = ocServerService.queryOcServerByMonitorParam(pageQuery);
        return convertHostTable(table);
    }

    @Override
    public BusinessWrapper<Boolean> massUpdateMonitorHostStatus(MonitorHostParam.MassUpdateMonitorHostStatus massUpdateMonitorHostStatus) {
        BusinessWrapper<Map<String, List<OcServer>>> wrapper = serverGroupFacade.queryServerGroupHostPattern(massUpdateMonitorHostStatus);
        if (!wrapper.isSuccess()) return new BusinessWrapper<>(wrapper.getCode(), wrapper.getDesc());
        Map<String, List<OcServer>> hostPatternMap = wrapper.getBody();
        if (hostPatternMap.containsKey(massUpdateMonitorHostStatus.getHostPattern())) {
            List<OcServer> servers = hostPatternMap.get(massUpdateMonitorHostStatus.getHostPattern());
            List<String> hostids = acqZabbixHostids(servers);
            return zabbixHostServer.massUpdateHostStatus(hostids, massUpdateMonitorHostStatus.getStatus());
        } else {
            return new BusinessWrapper<>(ErrorEnum.SERVER_GROUP_HOST_PATTERN_NOT_EXIST);
        }
    }

    private List<String> acqZabbixHostids(List<OcServer> servers) {
        List<String> hostids = Lists.newArrayList();
        servers.forEach(e -> {
            ZabbixHost zabbixHost = zabbixHostServer.getHost(e.getPrivateIp());
            if (!zabbixHost.isEmpty())
                hostids.add(zabbixHost.getHostid());
        });
        return hostids;
    }

    @Override
    @Async(ASYNC_POOL_TASK_COMMON)
    public void syncMonitorHostStatus() {
        ocServerService.queryAllOcServer().forEach(this::updateMonitorHostStatus);
    }

    @Override
    public DataTable<MonitorVO.User> queryMonitorUserPage(MonitorUserParam.MonitorUserPageQuery pageQuery) {
        DataTable<OcAccount> table = accountFacade.queryOcAccountByParam(pageQuery);
        return convertUserTable(table);
    }

    @Override
    @Async(ASYNC_POOL_TASK_COMMON)
    public void syncZabbixUsers() {
        IAccount iAccount = AccountFactory.getAccountByKey("ZabbixAccount");
        iAccount.async();
    }

    @Override
    public BusinessWrapper<Boolean> createMonitorHost(MonitorHostParam.CreateMonitorHost createMonitorHost) {
        OcServer ocServer = ocServerService.queryOcServerById(createMonitorHost.getServerId());
        IServer iServer = ServerFactory.getIServerByKey("ZabbixHost");
        BusinessWrapper<Boolean> result = iServer.create(ocServer);
        if (result.isSuccess()) {
            ocServer.setMonitorStatus(0);
            ocServerService.updateOcServer(ocServer);
            return BusinessWrapper.SUCCESS;
        }
        return new BusinessWrapper<>(ErrorEnum.SYSTEM_ERROR);
    }

    @Override
    public BusinessWrapper<Boolean> setMonitorHostStatus(int id) {
        OcServer ocServer = ocServerService.queryOcServerById(id);
        IServer iServer = ServerFactory.getIServerByKey("ZabbixHost");
        if (ocServer.getMonitorStatus() == -1)
            return new BusinessWrapper<>(ErrorEnum.ZABBIX_HOST_NOT_EXIST);
        return ocServer.getMonitorStatus() == 0 ? iServer.disable(ocServer) : iServer.enable(ocServer);
    }

    @Override
    public BusinessWrapper<Boolean> pushMonitorHost(MonitorHostParam.PushMonitorHost pushMonitorHost) {
        OcServer ocServer = ocServerService.queryOcServerById(pushMonitorHost.getServerId());
        IServer iServer = ServerFactory.getIServerByKey("ZabbixHost");
        return iServer.update(ocServer);
    }

    private void updateMonitorHostStatus(OcServer ocServer) {
        if (!ocServer.getIsActive()) return; // 无效主机
        ZabbixHost zabbixHost = zabbixHostServer.getHost(ocServer.getPrivateIp());
        log.info("更新主机监控状态: server={},ip={}", ocServer.getName(), ocServer.getPrivateIp());
        int status = -1;
        if (!zabbixHost.isEmpty())
            status = Integer.parseInt(zabbixHost.getStatus());
        if (status != ocServer.getMonitorStatus()) {
            ocServer.setMonitorStatus(status);
            ocServerService.updateOcServer(ocServer);
        }
    }

    private DataTable<MonitorVO.User> convertUserTable(DataTable<OcAccount> table) {
        List<MonitorVO.User> page = BeanCopierUtils.copyListProperties(table.getData(), MonitorVO.User.class);
        return new DataTable<>(page.stream().map(e -> monitorUserDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
    }

    private DataTable<MonitorVO.Host> convertHostTable(DataTable<OcServer> table) {
        List<MonitorVO.Host> page = BeanCopierUtils.copyListProperties(table.getData(), MonitorVO.Host.class);
        return new DataTable<>(page.stream().map(e -> monitorHostDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
    }

}
