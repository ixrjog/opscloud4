package com.baiyi.opscloud.datasource.zabbix.v5;

import com.baiyi.opscloud.datasource.server.impl.ZabbixHostServerProvider;
import com.baiyi.opscloud.datasource.zabbix.base.BaseZabbixTest;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.server.ServerService;
import com.baiyi.opscloud.zabbix.v5.drive.ZabbixV5HostDrive;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixHost;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/22 10:14 上午
 * @Version 1.0
 */
@Slf4j
public class ZabbixHostTest extends BaseZabbixTest {

    @Resource
    private ZabbixV5HostDrive zabbixV5HostDrive;

    @Resource
    private ZabbixHostServerProvider zabbixHostServerProvider;

    @Resource
    private DsInstanceService dsInstanceService;

    @Resource
    private ServerService serverService;

    @Test
    void careateHostTest() {
        DatasourceInstance datasourceInstance = dsInstanceService.getById(8);
        ServerParam.ServerPageQuery pageQuery = ServerParam.ServerPageQuery.builder()
                .build();
        pageQuery.setPage(1);
        pageQuery.setLength(1000);
        DataTable<Server> dataTable = serverService.queryServerPage(pageQuery);
        for (Server server : dataTable.getData()) {
            zabbixHostServerProvider.update(datasourceInstance, server);
            log.info("创建Zabbix主机: name = {} , envType = {}", server.getName(), server.getEnvType());
        }
    }

    @Test
    void listHostTest() {
        List<ZabbixHost.Host> hosts = zabbixV5HostDrive.list(getConfig().getZabbix());
        print(hosts);
    }

    @Test
    void getByIpTest() {
        ZabbixHost.Host host = zabbixV5HostDrive.getByIp(getConfig().getZabbix(), "172.19.0.129");
        print(host);
    }

    @Test
    void getByIpTest2() {
        ZabbixHost.Host host1 = zabbixV5HostDrive.getByIp(getConfig().getZabbix(), "172.19.0.129");
        ZabbixHost.Host host2 = zabbixV5HostDrive.getById(getConfig().getZabbix(), host1.getHostid());
        print(host2);
    }

    @Test
    void updateHostNameTest() {
        ZabbixHost.Host host = zabbixV5HostDrive.getByIp(getConfig().getZabbix(), "172.19.0.129");
        zabbixV5HostDrive.updateHostName(getConfig().getZabbix(), host, "opscloud4-1");
    }

}
