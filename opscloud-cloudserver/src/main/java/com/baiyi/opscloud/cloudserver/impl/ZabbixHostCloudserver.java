package com.baiyi.opscloud.cloudserver.impl;

import com.baiyi.opscloud.cloudserver.ICloudserver;
import com.baiyi.opscloud.cloudserver.base.CloudserverType;
import com.baiyi.opscloud.cloudserver.builder.OcCloudserverBuilder;
import com.baiyi.opscloud.cloudserver.decorator.ZabbixHostDecorator;
import com.baiyi.opscloud.cloudserver.instance.ZabbixHostInstance;
import com.baiyi.opscloud.domain.generator.OcCloudserver;
import com.baiyi.opscloud.zabbix.config.ZabbixConfig;
import com.baiyi.opscloud.zabbix.entry.ZabbixHost;
import com.baiyi.opscloud.zabbix.server.ZabbixHostServer;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Component("ZabbixHostCloudserver")
public class ZabbixHostCloudserver<T> extends BaseCloudserver<T> implements ICloudserver {

    @Resource
    private ZabbixHostServer zabbixHostServer;

    @Resource
    private ZabbixConfig zabbixConfig;

    @Resource
    private ZabbixHostDecorator zabbixHostDecorator;

    @Override
    protected List<T> getInstanceList() {
        List<ZabbixHost> hostList = zabbixHostServer.getHostList();
        if (!CollectionUtils.isEmpty(hostList))
            return getInstanceList(hostList);
        return Collections.emptyList();
    }

    @Override
    protected T getInstance(String regionId, String instanceId) {
        return (T) zabbixHostDecorator.decorator(zabbixHostServer.getHostByHostid(instanceId));
    }

    private List<T> getInstanceList(List<ZabbixHost> hostList) {
        return hostList.stream().map(e -> (T) zabbixHostDecorator.decorator(e)).collect(Collectors.toList());
    }

    @Override
    protected int getCloudserverType() {
        return CloudserverType.ZH.getType();
    }

    @Override
    protected String getInstanceId(T instance) throws Exception {
        if (!(instance instanceof ZabbixHostInstance)) throw new Exception();
        ZabbixHostInstance i = (ZabbixHostInstance) instance;
        return i.getHost().getHostid();
    }

    @Override
    protected String getInstanceName(T instance) {
        if (!(instance instanceof ZabbixHostInstance)) return null;
        ZabbixHostInstance i = (ZabbixHostInstance) instance;
        return i.getHost().getName();
    }

    @Override
    protected OcCloudserver getCloudserver(T instance) {
        if (!(instance instanceof ZabbixHostInstance)) return null;
        ZabbixHostInstance i = (ZabbixHostInstance) instance;
        return OcCloudserverBuilder.buildOcCloudserver(i, getInstanceDetail(instance), zabbixConfig.getZone());
    }

}
