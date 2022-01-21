package com.baiyi.opscloud.datasource.aws.ec2.drive;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.ec2.service.AmazonEC2Service;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/21 10:19 AM
 * @Version 1.0
 */
@Component
public class AmazonEc2Drive {

    public List<Instance> listInstances(AwsConfig.Aws aws) {
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        List<Instance> instanceList = Lists.newArrayList();
        while (true) {
            DescribeInstancesResult response = buildAmazonEC2(aws).describeInstances(request);
            response.getReservations().forEach(e -> instanceList.addAll(e.getInstances()));
            request.setNextToken(response.getNextToken());
            if (response.getNextToken() == null) return instanceList;
        }
    }

    private AmazonEC2 buildAmazonEC2(AwsConfig.Aws aws) {
        return AmazonEC2Service.buildAmazonEC2(aws);
    }

}
