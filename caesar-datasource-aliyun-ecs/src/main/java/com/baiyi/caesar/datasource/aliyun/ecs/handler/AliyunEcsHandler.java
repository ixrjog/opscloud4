package com.baiyi.caesar.datasource.aliyun.ecs.handler;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.caesar.common.datasource.config.AliyunDsConfig;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.caesar.datasource.aliyun.ecs.common.BaseAliyunHandler.Query.INSTANCE_PAGE_SIZE;

/**
 * @Author baiyi
 * @Date 2021/6/18 9:46 上午
 * @Version 1.0
 */
@Component
public class AliyunEcsHandler {

    @Resource
    private AliyunInstanceHandler aliyunInstanceHandler;

    public List<DescribeInstancesResponse.Instance> listInstance(String regionId, AliyunDsConfig.aliyun aliyun) {
        List<DescribeInstancesResponse.Instance> instanceList = Lists.newArrayList();
        try {
            DescribeInstancesRequest describe = new DescribeInstancesRequest();
            describe.setPageSize(INSTANCE_PAGE_SIZE);
            int size = INSTANCE_PAGE_SIZE;
            int pageNumber = 1;
            // 循环取值
            while (INSTANCE_PAGE_SIZE <= size) {
                describe.setPageNumber(pageNumber);
                DescribeInstancesResponse response = aliyunInstanceHandler.getInstancesResponse(regionId, aliyun, describe);
                instanceList.addAll(response.getInstances());
                size = response.getInstances() != null ? response.getInstances().size() : 0;
                pageNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instanceList;
    }
}
