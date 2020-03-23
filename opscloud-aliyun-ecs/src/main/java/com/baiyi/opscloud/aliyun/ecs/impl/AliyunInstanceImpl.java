package com.baiyi.opscloud.aliyun.ecs.impl;

import com.aliyuncs.ecs.model.v20140526.DescribeInstanceTypesResponse;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.ecs.AliyunInstance;
import com.baiyi.opscloud.aliyun.ecs.base.AliyunInstanceTypeVO;
import com.baiyi.opscloud.aliyun.ecs.handler.AliyunInstanceHandler;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/3/20 1:12 下午
 * @Version 1.0
 */
@Component
public class AliyunInstanceImpl implements AliyunInstance {
    @Resource
    private AliyunCore aliyunCore;

    @Resource
    private AliyunInstanceHandler aliyunInstanceHandler;


    /**
     * key : instanceType = ecs.t1.xsmall
     *
     * @return
     */
    @Cacheable(cacheNames = "instanceTypeContext", key = "#root.targetClass")
    @Override
    public Map<String, AliyunInstanceTypeVO.InstanceType> getInstanceTypeMap() {
        List<DescribeInstanceTypesResponse.InstanceType> data = aliyunInstanceHandler.getInstanceTypeList(aliyunCore.getAccount().getRegionId());
        //instanceTypeList .stream().map(e -> BeanCopierUtils.copyProperties(e, AliyunInstanceTypeVO.InstanceType.class)).collect(Collectors.toList());
        return data.stream().collect(Collectors.toMap(DescribeInstanceTypesResponse.InstanceType::getInstanceTypeId
                , a -> BeanCopierUtils.copyProperties(a, AliyunInstanceTypeVO.InstanceType.class), (k1, k2) -> k1));
    }
}
