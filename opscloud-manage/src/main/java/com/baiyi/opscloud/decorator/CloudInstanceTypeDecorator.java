package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.aliyun.ecs.AliyunInstance;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudInstanceTypeVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/3/23 2:04 下午
 * @Version 1.0
 */
@Component
public class CloudInstanceTypeDecorator {

    @Resource
    private AliyunInstance aliyunInstance;

    public OcCloudInstanceTypeVO.CloudInstanceType decorator(OcCloudInstanceTypeVO.CloudInstanceType cloudInstanceType,String regionId, Integer extend) {
        if (extend != null && extend == 1) {
            Map<String, Set<String>> map = aliyunInstance.getInstanceTypeZoneMap(regionId);
            if(map.containsKey(cloudInstanceType.getInstanceTypeId()))
                cloudInstanceType.setZones(map.get(cloudInstanceType.getInstanceTypeId()));
        }
        return cloudInstanceType;
    }
}
