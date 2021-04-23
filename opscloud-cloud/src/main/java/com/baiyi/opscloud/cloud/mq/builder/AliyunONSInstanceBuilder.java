package com.baiyi.opscloud.cloud.mq.builder;

import com.aliyuncs.ons.model.v20190214.OnsInstanceInServiceListResponse;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsInstance;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/5 3:05 下午
 * @Since 1.0
 */
public class AliyunONSInstanceBuilder {

    public static OcAliyunOnsInstance build(OnsInstanceInServiceListResponse.InstanceVO instance, String regionId) {
        OcAliyunOnsInstance ocAliyunOnsInstance = new OcAliyunOnsInstance();
        ocAliyunOnsInstance.setInstanceId(instance.getInstanceId());
        ocAliyunOnsInstance.setInstanceName(instance.getInstanceName());
        ocAliyunOnsInstance.setIndependentNaming(instance.getIndependentNaming());
        ocAliyunOnsInstance.setInstanceStatus(instance.getInstanceStatus());
        ocAliyunOnsInstance.setInstanceType(instance.getInstanceType());
        ocAliyunOnsInstance.setRegionId(regionId);
        ocAliyunOnsInstance.setReleaseTime(instance.getReleaseTime());
        return ocAliyunOnsInstance;
    }
}
