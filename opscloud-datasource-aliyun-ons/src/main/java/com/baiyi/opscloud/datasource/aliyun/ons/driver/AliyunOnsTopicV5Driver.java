package com.baiyi.opscloud.datasource.aliyun.ons.driver;

import com.aliyun.rocketmq20220801.Client;
import com.aliyun.rocketmq20220801.models.CreateTopicRequest;
import com.aliyun.rocketmq20220801.models.GetTopicResponse;
import com.aliyun.rocketmq20220801.models.ListTopicsRequest;
import com.aliyun.rocketmq20220801.models.ListTopicsResponseBody;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.ons.client.AliyunOnsV5Client;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsTopicV5;
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
public class AliyunOnsTopicV5Driver {

    public List<OnsTopicV5.Topic> listTopic(String regionId, AliyunConfig.Aliyun aliyun, String instanceId) throws Exception {
        List<ListTopicsResponseBody.ListTopicsResponseBodyDataList> topicList = Lists.newArrayList();
        int pageNumber = 1;
        ListTopicsRequest request = new ListTopicsRequest()
                .setPageSize(PAGE_SIZE);
        Client client = AliyunOnsV5Client.buildClient(regionId, aliyun);
        ListTopicsResponseBody.ListTopicsResponseBodyData response;
        do {
            request.setPageNumber(pageNumber);
            response = client.listTopics(instanceId, request).getBody().getData();
            topicList.addAll(response.getList());
            pageNumber++;
        } while (topicList.size() < response.getTotalCount());
        return BeanCopierUtil.copyListProperties(topicList, OnsTopicV5.Topic.class);
    }

    public OnsTopicV5.Topic getTopic(String regionId, AliyunConfig.Aliyun aliyun, String instanceId, String topicName) throws Exception {
        Client client = AliyunOnsV5Client.buildClient(regionId, aliyun);
        GetTopicResponse response = client.getTopic(instanceId, topicName);
        return BeanCopierUtil.copyProperties(response.getBody().getData(), OnsTopicV5.Topic.class);
    }

    public void createTopic(String regionId, AliyunConfig.Aliyun aliyun, OnsTopicV5.Topic topic) throws Exception {
        CreateTopicRequest request = new CreateTopicRequest()
                .setMessageType(topic.getMessageType())
                .setRemark(topic.getRemark());
        Client client = AliyunOnsV5Client.buildClient(regionId, aliyun);
        client.createTopic(topic.getInstanceId(), topic.getTopicName(), request);
    }

}