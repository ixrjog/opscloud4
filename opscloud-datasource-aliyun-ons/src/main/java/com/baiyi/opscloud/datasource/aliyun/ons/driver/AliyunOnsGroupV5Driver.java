package com.baiyi.opscloud.datasource.aliyun.ons.driver;

import com.aliyun.rocketmq20220801.Client;
import com.aliyun.rocketmq20220801.models.CreateConsumerGroupRequest;
import com.aliyun.rocketmq20220801.models.GetConsumerGroupResponse;
import com.aliyun.rocketmq20220801.models.ListConsumerGroupsRequest;
import com.aliyun.rocketmq20220801.models.ListConsumerGroupsResponseBody;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.ons.client.AliyunOnsV5Client;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsGroupV5;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.datasource.aliyun.core.SimpleAliyunClient.Query.PAGE_SIZE;

/**
 * @Author 修远
 * @Date 2023/9/11 10:50 PM
 * @Since 1.0
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunOnsGroupV5Driver {

    public List<OnsGroupV5.Group> listGroup(String regionId, AliyunConfig.Aliyun aliyun, String instanceId) throws Exception {
        List<ListConsumerGroupsResponseBody.ListConsumerGroupsResponseBodyDataList> groupList = Lists.newArrayList();
        int pageNumber = 1;
        ListConsumerGroupsRequest request = new ListConsumerGroupsRequest()
                .setPageSize(PAGE_SIZE);
        Client client = AliyunOnsV5Client.buildClient(regionId, aliyun);
        ListConsumerGroupsResponseBody.ListConsumerGroupsResponseBodyData response;
        do {
            request.setPageNumber(pageNumber);
            response = client.listConsumerGroups(instanceId, request).getBody().getData();
            groupList.addAll(response.getList());
            pageNumber++;
        } while (groupList.size() < response.getTotalCount());
        return BeanCopierUtil.copyListProperties(groupList, OnsGroupV5.Group.class);
    }

    public OnsGroupV5.Group getGroup(String regionId, AliyunConfig.Aliyun aliyun, String instanceId, String groupId) throws Exception {
        Client client = AliyunOnsV5Client.buildClient(regionId, aliyun);
        GetConsumerGroupResponse response = client.getConsumerGroup(instanceId, groupId);
        return BeanCopierUtil.copyProperties(response.getBody().getData(), OnsGroupV5.Group.class);

    }

    /**
     * https://help.aliyun.com/zh/apsaramq-for-rocketmq/cloud-message-queue-rocketmq-5-x-series/developer-reference/api-rocketmq-2022-08-01-createconsumergroup?spm=a2c4g.11186623.0.0.7ee7c4b4JkXAgI
     */
    public void createGroup(String regionId, AliyunConfig.Aliyun aliyun, OnsGroupV5.Group group) throws Exception {
        CreateConsumerGroupRequest.CreateConsumerGroupRequestConsumeRetryPolicy policy = new CreateConsumerGroupRequest.CreateConsumerGroupRequestConsumeRetryPolicy()
                .setRetryPolicy(group.getConsumeRetryPolicy().getRetryPolicy())
                .setMaxRetryTimes(group.getConsumeRetryPolicy().getMaxRetryTimes())
                .setDeadLetterTargetTopic(group.getConsumeRetryPolicy().getDeadLetterTargetTopic());
        CreateConsumerGroupRequest request = new CreateConsumerGroupRequest()
                .setRemark(group.getRemark())
                .setDeliveryOrderType(group.getDeliveryOrderType())
                .setConsumeRetryPolicy(policy);
        Client client = AliyunOnsV5Client.buildClient(regionId, aliyun);
        client.createConsumerGroup(group.getInstanceId(), group.getConsumerGroupId(), request);
    }

}