package com.baiyi.opscloud.cloud.server.decorator;

import com.baiyi.opscloud.cloud.server.instance.ZabbixHostInstance;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.server.ZabbixHostServer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/13 1:27 下午
 * @Version 1.0
 */
@Component("ZabbixHostDecorator")
public class ZabbixHostDecorator {

    @Resource
    private ZabbixHostServer zabbixHostServer;

    public ZabbixHostInstance decorator(ZabbixHost host){
        return ZabbixHostInstance.builder()
                .host(host)
                .interfaceList(zabbixHostServer.getHostInterfaces(host.getHostid()))
                .build();
    }

}
