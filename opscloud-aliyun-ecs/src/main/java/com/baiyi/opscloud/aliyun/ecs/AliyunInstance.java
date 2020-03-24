package com.baiyi.opscloud.aliyun.ecs;

import com.baiyi.opscloud.aliyun.ecs.base.AliyunInstanceTypeVO;

import java.util.Map;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/3/20 1:12 下午
 * @Version 1.0
 */
public interface AliyunInstance {

    Map<String, AliyunInstanceTypeVO.InstanceType> getInstanceTypeMap();

    Map<String, Set<String>> getInstanceTypeZoneMap(String regionId);
}
