package com.baiyi.opscloud.datasource.aws.ec2.driver;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.*;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.datasource.aws.ec2.entity.Ec2Instance;
import com.baiyi.opscloud.datasource.aws.ec2.helper.AmazonEc2InstanceTypeHelper;
import com.baiyi.opscloud.datasource.aws.ec2.model.InstanceModel;
import com.baiyi.opscloud.datasource.aws.ec2.service.AmazonEC2Service;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/1/21 10:19 AM
 * @Version 1.0
 */
@Slf4j
@Component
public class AmazonEc2Driver {

    @Resource
    private AmazonEc2InstanceTypeHelper amazonEc2InstanceTypeHelper;

    public List<Ec2Instance.Instance> listInstances(String regionId, AwsConfig.Aws aws) {
        Map<String, InstanceModel.EC2InstanceType> instanceTypeMap = Maps.newHashMap();
        try {
            instanceTypeMap.putAll(amazonEc2InstanceTypeHelper.getAmazonEc2InstanceTypeMap(aws));
        } catch (Exception e) {
            log.error("查询AmazonEC2实例类型错误: {}", e.getMessage());
        }
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        List<Ec2Instance.Instance> instanceList = Lists.newArrayList();
        while (true) {
            DescribeInstancesResult response = buildAmazonEC2(aws, regionId).describeInstances(request);
            response.getReservations().forEach(e -> e.getInstances().forEach(i -> {
                // 获取不到IP
                // 销毁状态 i.getState().getCode() == 48
                if (StringUtils.isEmpty(i.getPrivateIpAddress())) {
                    return;
                }
                final Gson builder = new GsonBuilder()
                        .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (jsonElement, type, context) -> new Date(jsonElement.getAsJsonPrimitive().getAsLong())).create();
                Ec2Instance.Instance instance = builder.fromJson(JSONUtil.writeValueAsString(i), Ec2Instance.Instance.class);
                instance.setRegionId(regionId);
                if (instanceTypeMap.containsKey(instance.getInstanceType())) {
                    instance.setEc2InstanceType(instanceTypeMap.get(instance.getInstanceType()));
                }
                instanceList.add(instance);
            }));
            request.setNextToken(response.getNextToken());
            if (response.getNextToken() == null) {
                return instanceList;
            }
        }
    }

    private AmazonEC2 buildAmazonEC2(AwsConfig.Aws aws, String regionId) {
        return AmazonEC2Service.buildAmazonEC2(aws, regionId);
    }

    public void createTags(AwsConfig.Aws aws, String regionId, List<String> resources, List<Tag> tags) {
        CreateTagsRequest request = new CreateTagsRequest(resources, tags);
        try {
            buildAmazonEC2(aws, regionId).createTags(request);
        } catch (Exception e) {
            log.error("CreateTags 错误: {}", e.getMessage());
        }
    }

    public List<Instance> listByInstanceIds(AwsConfig.Aws aws, String regionId, List<String> instanceIds) {
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        request.setInstanceIds(instanceIds);
        List<Instance> instanceList = Lists.newArrayList();
        while (true) {
            DescribeInstancesResult response = buildAmazonEC2(aws, regionId).describeInstances(request);
            response.getReservations().forEach(e -> instanceList.addAll(e.getInstances()));
            request.setNextToken(response.getNextToken());
            if (response.getNextToken() == null) {
                return instanceList;
            }
        }
    }

}
