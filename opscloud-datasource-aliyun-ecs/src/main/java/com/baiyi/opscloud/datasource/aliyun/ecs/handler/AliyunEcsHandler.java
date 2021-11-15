package com.baiyi.opscloud.datasource.aliyun.ecs.handler;

import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.core.handler.AliyunHandler;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.datasource.aliyun.core.common.BaseAliyunHandler.Query.PAGE_SIZE;

/**
 * @Author baiyi
 * @Date 2021/6/18 9:46 上午
 * @Version 1.0
 */
@Component
public class AliyunEcsHandler {

    @Resource
    private AliyunHandler aliyunHandler;

    public List<DescribeInstancesResponse.Instance> listInstances(String regionId, AliyunConfig.Aliyun aliyun) {
        List<DescribeInstancesResponse.Instance> instanceList = Lists.newArrayList();
        String nextToken;
        try {
            DescribeInstancesRequest describe = new DescribeInstancesRequest();
            do {
                DescribeInstancesResponse response = aliyunHandler.getAcsResponse(regionId, aliyun, describe);
                instanceList.addAll(response.getInstances());
                nextToken = response.getNextToken();
                describe.setNextToken(nextToken);
            } while (Strings.isNotBlank(nextToken));
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return instanceList;
    }

    public List<DescribeImagesResponse.Image> listImages(String regionId, AliyunConfig.Aliyun aliyun) {
        List<DescribeImagesResponse.Image> images = Lists.newArrayList();
        try {
            DescribeImagesRequest describe = new DescribeImagesRequest();
            describe.setSysRegionId(regionId);
            describe.setPageSize(PAGE_SIZE);
            /**
             * system：阿里云提供的公共镜像。
             * self：您创建的自定义镜像。
             * others：其他阿里云用户共享给您的镜像。
             * marketplace：镜像市场提供的镜像
             */
            describe.setImageOwnerAlias("self");
            int size = PAGE_SIZE;
            int pageNumber = 1;
            // 循环取值
            while (PAGE_SIZE <= size) {
                describe.setPageNumber(pageNumber);
                DescribeImagesResponse response = aliyunHandler.getAcsResponse(regionId, aliyun, describe);
                images.addAll(response.getImages());
                size = response.getImages().size();
                pageNumber++;
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return images;
    }

    public List<DescribeSecurityGroupsResponse.SecurityGroup> listSecurityGroups(String regionId, AliyunConfig.Aliyun aliyun, DatasourceInstanceAsset asset) {
        List<DescribeSecurityGroupsResponse.SecurityGroup> securityGroups = Lists.newArrayList();
        try {
            DescribeSecurityGroupsRequest describe = new DescribeSecurityGroupsRequest();
            describe.setSysRegionId(regionId);
            describe.setPageSize(PAGE_SIZE);
            describe.setVpcId(asset.getAssetId());
            int size = PAGE_SIZE;
            int pageNumber = 1;
            while (PAGE_SIZE <= size) {
                describe.setPageNumber(pageNumber);
                DescribeSecurityGroupsResponse response = aliyunHandler.getAcsResponse(regionId, aliyun, describe);
                securityGroups.addAll(response.getSecurityGroups());
                size = response.getSecurityGroups().size();
                pageNumber++;
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return securityGroups;

    }


}
