package com.baiyi.opscloud.cloudserver.impl;

import com.baiyi.opscloud.cloudserver.Cloudserver;
import com.baiyi.opscloud.cloudserver.instance.ZabbixHostInstance;
import com.baiyi.opscloud.zabbix.entry.ZabbixHostInterface;
import com.baiyi.opscloud.zabbix.server.ZabbixHostServer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/1/9 3:43 下午
 * @Version 1.0
 */
@Component("ZabbixHostCloudserver")
public class ZabbixHostCloudserver<T> extends Cloudserver<T> {

    @Resource
    private ZabbixHostServer zabbixHostServer;

    @Override
    protected List<T> getInstanceList() {
        List<com.baiyi.opscloud.zabbix.entry.ZabbixHost> hostList = zabbixHostServer.getHostList();
        if (!CollectionUtils.isEmpty(hostList))
            return getInstanceList(hostList);
        return Collections.emptyList();
    }

    private List<T> getInstanceList(List<com.baiyi.opscloud.zabbix.entry.ZabbixHost> hostList) {
        return hostList.stream().map(e -> {
            List<ZabbixHostInterface> interfaceList = zabbixHostServer.getHostInterfaceList(e.getHostid());
            return (T) new ZabbixHostInstance(e, interfaceList);
        }).collect(Collectors.toList());
    }


}
