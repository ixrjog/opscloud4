package com.baiyi.opscloud.cloud.server.impl;

import com.baiyi.opscloud.cloud.server.ICloudServer;
import com.baiyi.opscloud.cloud.server.builder.CloudServerBuilder;
import com.baiyi.opscloud.common.base.CloudServerType;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudServer;
import com.baiyi.opscloud.tencent.cloud.cvm.handler.TencentCloudCVMHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/20 5:00 下午
 * @Version 1.0
 */
@Slf4j
@Component("TencentCVMCloudServer")
public class TencentCVMCloudServer<T> extends BaseCloudServer<T> implements ICloudServer {

    @Resource
    private TencentCloudCVMHandler tencentCloudCVMHandler;

    @Override
    protected List<T> getInstanceList() {
        return getInstanceList(tencentCloudCVMHandler.getInstanceList());
    }

    @Override
    protected T getInstance(String regionId, String instanceId) {
        return (T) tencentCloudCVMHandler.getInstance(instanceId);
    }

    private List<T> getInstanceList(List<com.tencentcloudapi.cvm.v20170312.models.Instance> instanceList) {
        return (List<T>) instanceList;
    }

    @Override
    protected int getCloudServerType() {
        return CloudServerType.CVM.getType();
    }

    @Override
    protected String getInstanceId(T instance) throws Exception {
        if (!(instance instanceof com.tencentcloudapi.cvm.v20170312.models.Instance)) throw new Exception();
        com.tencentcloudapi.cvm.v20170312.models.Instance i = (com.tencentcloudapi.cvm.v20170312.models.Instance) instance;
        return i.getInstanceId();
    }

    @Override
    protected String getInstanceName(T instance) {
        if (!(instance instanceof com.tencentcloudapi.cvm.v20170312.models.Instance)) return null;
        com.tencentcloudapi.cvm.v20170312.models.Instance i = (com.tencentcloudapi.cvm.v20170312.models.Instance) instance;
        return i.getInstanceName();
    }


    @Override
    protected OcCloudServer getCloudServer(T instance) {
        if (!(instance instanceof com.tencentcloudapi.cvm.v20170312.models.Instance)) return null;
        com.tencentcloudapi.cvm.v20170312.models.Instance i = (com.tencentcloudapi.cvm.v20170312.models.Instance) instance;
        return CloudServerBuilder.build(i, getInstanceDetail(instance));
    }

    @Override
    protected BusinessWrapper<Boolean> power(OcCloudServer ocCloudserver, Boolean action) {
        return BusinessWrapper.SUCCESS;
    }

    protected int getPowerStatus(String regionId, String instanceId) {
        return 0;
    }


}
