package com.baiyi.opscloud.aws.ec2;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Region;
import com.baiyi.opscloud.aws.ec2.base.EC2InstanceType;
import com.baiyi.opscloud.aws.ec2.base.EC2Volume;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/13 9:52 上午
 * @Version 1.0
 */

public interface AwsEC2 {

    List<Region> getRegionList();

    EC2InstanceType getInstanceType(String instanceType);

    List<Instance> getInstanceList();

    Instance getInstance(String instanceId);

    List<EC2Volume> getVolumeList(Instance instance);
}
