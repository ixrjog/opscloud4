package com.baiyi.opscloud.aws.ec2.impl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.*;
import com.baiyi.opscloud.aws.core.AwsCore;
import com.baiyi.opscloud.aws.ec2.AwsEC2;
import com.baiyi.opscloud.aws.ec2.base.EC2InstanceType;
import com.baiyi.opscloud.aws.ec2.base.EC2Volume;
import com.baiyi.opscloud.aws.ec2.context.EC2InstanceTypeContext;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/1/13 9:53 上午
 * @Version 1.0
 */
@Component("AwsEC2")
public class AwsEC2Impl implements AwsEC2, InitializingBean {

    @Resource
    private AwsCore awsCore;

    @Resource
    private EC2InstanceTypeContext ec2InstanceTypeContext;

    private AmazonEC2 ec2;

    @Override
    public List<Instance> getInstanceList() {
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        List<Instance> instanceList = Lists.newArrayList();
        while (true) {
            DescribeInstancesResult response = ec2.describeInstances(request);
            response.getReservations().forEach(e -> instanceList.addAll(e.getInstances()));
            request.setNextToken(response.getNextToken());
            if (response.getNextToken() == null) return instanceList;
        }

    }

    @Override
    public Instance getInstance(String instanceId) {
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        Collection<String> instanceIds = Lists.newArrayList();
        instanceIds.add(instanceId);
        request.setInstanceIds(instanceIds);
        try {
            DescribeInstancesResult response = ec2.describeInstances(request);
            return response.getReservations().get(0).getInstances().get(0);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public EC2InstanceType getInstanceType(String instanceType) {
        try {
            return ec2InstanceTypeContext.getInstanceTypeContext().get(instanceType);
        } catch (Exception e) {
            e.printStackTrace();
            return new EC2InstanceType();
        }
    }

    @Override
    public List<Region> getRegionList() {
        DescribeRegionsResult regions_response = ec2.describeRegions();
        return regions_response.getRegions();
    }

    @Override
    public List<EC2Volume> getVolumeList(Instance instance) {
        try {
            List<String> volumeIds = instance.getBlockDeviceMappings().stream().map(e -> e.getEbs().getVolumeId()).collect(Collectors.toList());
            DescribeVolumesRequest request = new DescribeVolumesRequest();
            request.setVolumeIds(volumeIds);
            DescribeVolumesResult result = ec2.describeVolumes(request);
            return result.getVolumes().stream().map(e -> new EC2Volume(e, instance.getRootDeviceName())).collect(Collectors.toList());
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            AWSCredentials credentials = awsCore.getAWSCredentials();
            if (credentials == null) return;
            ec2 = AmazonEC2ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(awsCore.getApRegionId())
                    .build();
        } catch (Exception e) {
        }
    }
}
