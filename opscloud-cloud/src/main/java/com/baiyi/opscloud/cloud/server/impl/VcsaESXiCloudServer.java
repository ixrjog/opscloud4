package com.baiyi.opscloud.cloud.server.impl;

import com.baiyi.opscloud.cloud.server.ICloudServer;
import com.baiyi.opscloud.cloud.server.builder.CloudServerBuilder;
import com.baiyi.opscloud.common.base.CloudServerType;
import com.baiyi.opscloud.common.cloud.BaseCloudServerInstance;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudServer;
import com.baiyi.opscloud.vmware.vcsa.esxi.VcsaESXi;
import com.baiyi.opscloud.vmware.vcsa.instance.ESXiInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/15 2:10 下午
 * @Version 1.0
 */
@Slf4j
@Component("VcsaESXiCloudServer")
public class VcsaESXiCloudServer<T extends BaseCloudServerInstance> extends BaseCloudServer<T> implements ICloudServer {

    @Resource
    private VcsaESXi vcsaESXi;

    @Override
    protected List<T> getInstanceList() {
        return (List<T>) vcsaESXi.getInstanceList();
    }

    @Override
    protected T getInstance(String regionId, String instanceId) {
        OcCloudServer ocCloudserver = ocCloudServerService.queryOcCloudServerByInstanceId(instanceId);
        return (T) vcsaESXi.getInstance(ocCloudserver.getServerName());
    }

    @Override
    protected int getCloudServerType() {
        return CloudServerType.ESXI.getType();
    }

    @Override
    protected String getInstanceId(T instance) throws Exception {
        if (!(instance instanceof ESXiInstance)) throw new Exception();
        ESXiInstance i = (ESXiInstance) instance;
        return i.getHostHardwareInfo().systemInfo.uuid;
    }

    @Override
    protected String getInstanceName(T instance) {
        if (!(instance instanceof ESXiInstance)) return null;
        ESXiInstance i = (ESXiInstance) instance;
        return i.getHostSummary().config.name;
    }

    @Override
    protected OcCloudServer getCloudServer(T instance) {
        if (!(instance instanceof ESXiInstance)) return null;
        ESXiInstance i = (ESXiInstance) instance;
        return CloudServerBuilder.build(i, vcsaESXi.getZone());
    }

    protected int getPowerStatus(String regionId, String instanceId) {
        return -1;
    }

}
