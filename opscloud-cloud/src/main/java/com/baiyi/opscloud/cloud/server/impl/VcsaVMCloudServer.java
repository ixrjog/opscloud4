package com.baiyi.opscloud.cloud.server.impl;

import com.baiyi.opscloud.cloud.server.ICloudServer;
import com.baiyi.opscloud.cloud.server.builder.CloudServerBuilder;
import com.baiyi.opscloud.common.base.CloudServerType;
import com.baiyi.opscloud.common.cloud.BaseCloudServerInstance;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudServer;
import com.baiyi.opscloud.vmware.vcsa.instance.VMInstance;
import com.baiyi.opscloud.vmware.vcsa.vm.VcsaVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/15 3:47 下午
 * @Version 1.0
 */
@Slf4j
@Component("VcsaVMCloudServer")
public class VcsaVMCloudServer<T extends BaseCloudServerInstance> extends BaseCloudServer<T> implements ICloudServer {

    @Resource
    private VcsaVM vcsaVM;

    @Override
    protected List<T> getInstanceList() {
        return (List<T>) vcsaVM.getInstanceList();
    }

    @Override
    protected T getInstance(String regionId, String instanceId) {
        OcCloudServer ocCloudserver = ocCloudServerService.queryOcCloudServerByInstanceId(instanceId);
        return (T) vcsaVM.getInstance(ocCloudserver.getServerName());
    }

    @Override
    protected int getCloudServerType() {
        return CloudServerType.VM.getType();
    }

    @Override
    protected String getInstanceId(T instance) throws Exception {
        if (!(instance instanceof VMInstance)) throw new Exception();
        VMInstance i = (VMInstance) instance;
        return i.getConfigInfoInstanceUuid();
    }

    @Override
    protected String getInstanceName(T instance) {
        if (!(instance instanceof VMInstance)) return null;
        VMInstance i = (VMInstance) instance;
        return i.getConfigInfoName();
    }

    @Override
    protected OcCloudServer getCloudServer(T instance) {
        if (!(instance instanceof VMInstance)) return null;
        VMInstance i = (VMInstance) instance;
        return CloudServerBuilder.build(i, vcsaVM.getZone());
    }

    @Override
    protected BusinessWrapper<Boolean> power(OcCloudServer ocCloudserver, Boolean action) {
        return vcsaVM.power(ocCloudserver.getInstanceName(), action);
    }

    protected int getPowerStatus(String regionId, String instanceId) {
        return -1;
    }

}
