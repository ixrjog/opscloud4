package com.baiyi.opscloud.builder;

import com.baiyi.opscloud.aliyun.ecs.base.AliyunInstanceTypeVO;
import com.baiyi.opscloud.bo.CloudInstanceTypeBO;
import com.baiyi.opscloud.common.base.CloudType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceType;

/**
 * @Author baiyi
 * @Date 2020/3/23 12:55 下午
 * @Version 1.0
 */
public class CloudInstanceTypeBuilder {

    public static OcCloudInstanceType build(AliyunInstanceTypeVO.InstanceType instanceType) {
        CloudInstanceTypeBO cloudInstanceTypeBO = CloudInstanceTypeBO.builder()
                .cloudType(CloudType.ALIYUN.getType())
                .instanceTypeFamily(instanceType.getInstanceTypeFamily())
                .instanceTypeId(instanceType.getInstanceTypeId())
                .cpuCoreCount(instanceType.getCpuCoreCount())
                .memorySize(instanceType.getMemorySize())
                .instanceFamilyLevel(instanceType.getInstanceFamilyLevel())
                .build();
        return covert(cloudInstanceTypeBO);
    }


    private static OcCloudInstanceType covert(CloudInstanceTypeBO cloudInstanceTypeBO) {
        return BeanCopierUtils.copyProperties(cloudInstanceTypeBO, OcCloudInstanceType.class);
    }
}
