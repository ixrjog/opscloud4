package com.baiyi.opscloud.cloud.server.instance;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.opscloud.aliyun.ecs.base.ECSDisk;
import com.baiyi.opscloud.common.cloud.BaseCloudServerInstance;
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
public class AliyunECSInstance implements BaseCloudServerInstance {

    private DescribeInstancesResponse.Instance instance;

    private List<ECSDisk> diskList;

    private String renewalStatus;
}
