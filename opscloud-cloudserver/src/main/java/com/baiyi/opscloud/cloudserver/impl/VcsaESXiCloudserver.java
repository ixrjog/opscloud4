package com.baiyi.opscloud.cloudserver.impl;

import com.baiyi.opscloud.cloudserver.ICloudserver;
import com.baiyi.opscloud.cloudserver.builder.OcCloudserverBuilder;
import com.baiyi.opscloud.common.base.CloudserverType;
import com.baiyi.opscloud.domain.generator.OcCloudserver;
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
@Component("VcsaESXiCloudserver")
public class VcsaESXiCloudserver<T> extends BaseCloudserver<T> implements ICloudserver {

    @Resource
    private VcsaESXi vcsaESXi;

    @Override
    protected List<T> getInstanceList() {
        return (List<T>) vcsaESXi.getInstanceList();
    }

    @Override
    protected T getInstance(String regionId, String instanceId) {
        OcCloudserver ocCloudserver = ocCloudserverService.queryOcCloudserverByInstanceId(instanceId);
        return (T) vcsaESXi.getInstance(ocCloudserver.getServerName());
    }

    @Override
    protected int getCloudserverType() {
        return CloudserverType.ESXI.getType();
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
    protected OcCloudserver getCloudserver(T instance) {
        if (!(instance instanceof ESXiInstance)) return null;
        ESXiInstance i = (ESXiInstance) instance;
        return OcCloudserverBuilder.build(i, vcsaESXi.getZone());
    }

}
