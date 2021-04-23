package com.baiyi.opscloud.cloud.server.instance;

import com.baiyi.opscloud.aws.ec2.base.EC2InstanceType;
import com.baiyi.opscloud.aws.ec2.base.EC2Volume;
import com.baiyi.opscloud.common.cloud.BaseCloudServerInstance;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/13 12:57 下午
 * @Version 1.0
 */
@Builder
@Data
public class AwsEC2Instance implements BaseCloudServerInstance {

    private com.amazonaws.services.ec2.model.Instance instance;

    private List<EC2Volume> volumeList;

    private EC2InstanceType instanceType;

}
