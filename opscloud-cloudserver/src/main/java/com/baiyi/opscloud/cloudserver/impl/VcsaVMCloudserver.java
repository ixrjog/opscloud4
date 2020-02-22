package com.baiyi.opscloud.cloudserver.impl;

import com.baiyi.opscloud.cloudserver.ICloudserver;
import com.baiyi.opscloud.cloudserver.builder.OcCloudserverBuilder;
import com.baiyi.opscloud.common.base.CloudserverType;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.OcCloudserver;
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
@Component("VcsaVMCloudserver")
public class VcsaVMCloudserver<T> extends BaseCloudserver<T> implements ICloudserver {

    @Resource
    private VcsaVM vcsaVM;

    @Override
    protected List<T> getInstanceList() {
        return (List<T>) vcsaVM.getInstanceList();
    }

    @Override
    protected T getInstance(String regionId, String instanceId) {
        OcCloudserver ocCloudserver = ocCloudserverService.queryOcCloudserverByInstanceId(instanceId);
        return (T) vcsaVM.getInstance(ocCloudserver.getServerName());
    }

    @Override
    protected int getCloudserverType() {
        return CloudserverType.VM.getType();
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
    protected OcCloudserver getCloudserver(T instance) {
        if (!(instance instanceof VMInstance)) return null;
        VMInstance i = (VMInstance) instance;
        return OcCloudserverBuilder.build(i, vcsaVM.getZone());
    }

    @Override
    protected BusinessWrapper<Boolean> power(OcCloudserver ocCloudserver, Boolean action){
        return vcsaVM.power(ocCloudserver.getInstanceName(),action);
    }

}
