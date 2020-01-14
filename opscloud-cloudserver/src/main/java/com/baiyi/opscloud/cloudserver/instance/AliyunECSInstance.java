package com.baiyi.opscloud.cloudserver.instance;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.opscloud.aliyun.ecs.base.ECSDisk;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/13 6:53 下午
 * @Version 1.0
 */
@Builder
@Data
public class AliyunECSInstance {

    private DescribeInstancesResponse.Instance instance;

    private List<ECSDisk> diskList;

    private String renewalStatus;
}
