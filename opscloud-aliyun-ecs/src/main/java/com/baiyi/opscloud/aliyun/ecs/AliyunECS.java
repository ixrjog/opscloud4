package com.baiyi.opscloud.aliyun.ecs;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.opscloud.aliyun.ecs.base.ECSDisk;
import com.baiyi.opscloud.domain.BusinessWrapper;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/10 6:48 下午
 * @Version 1.0
 */
public interface AliyunECS {

    List<DescribeInstancesResponse.Instance> getInstanceList();

    DescribeInstancesResponse.Instance getInstance(String regionId, String instanceId);

    List<ECSDisk> getDiskList(String regionId, String instanceId);

    String getRenewAttribute(String regionId, String instanceId);

    BusinessWrapper<Boolean> power(String regionId, String instanceId, Boolean action);

    boolean delete(String regionId, String instanceId);

    void modifyInstanceChargeType(String regionId, String instanceId, String chargeType);

    boolean modifyInstanceName(String regionId, String instanceId, String instanceName);

}
