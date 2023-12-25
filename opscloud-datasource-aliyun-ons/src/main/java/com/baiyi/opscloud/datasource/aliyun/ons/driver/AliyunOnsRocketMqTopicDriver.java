package com.baiyi.opscloud.datasource.aliyun.ons.driver;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ons.model.v20190214.OnsTopicCreateRequest;
import com.aliyuncs.ons.model.v20190214.OnsTopicCreateResponse;
import com.aliyuncs.ons.model.v20190214.OnsTopicListRequest;
import com.aliyuncs.ons.model.v20190214.OnsTopicListResponse;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsRocketMqTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/9/30 3:10 下午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunOnsRocketMqTopicDriver {

    private final AliyunClient aliyunClient;

    public static final String QUERY_ALL_TOPIC = StringUtils.EMPTY;

    public List<OnsRocketMqTopic.Topic> listTopic(String regionId, AliyunConfig.Aliyun aliyun, String instanceId) throws ClientException {
        return listTopic(regionId, aliyun, instanceId, QUERY_ALL_TOPIC);
    }

    public OnsRocketMqTopic.Topic getTopic(String regionId, AliyunConfig.Aliyun aliyun, String instanceId, String topic) throws ClientException {
        List<OnsRocketMqTopic.Topic> list = listTopic(regionId, aliyun, instanceId, topic);
        return CollectionUtils.isEmpty(list) ? null : list.getFirst();
    }

    /**
     * OnsRocketMqTopic
     * OnsTopicListResponse.PublishInfoDo
     * <p>
     * https://help.aliyun.com/document_detail/29590.html
     *
     * @param regionId
     * @param aliyun
     * @param instanceId 必选参数
     * @param topic
     * @return
     */
    public List<OnsRocketMqTopic.Topic> listTopic(String regionId, AliyunConfig.Aliyun aliyun, String instanceId, String topic) throws ClientException {
        OnsTopicListRequest request = new OnsTopicListRequest();
        request.setInstanceId(instanceId);
        if (StringUtils.isNotBlank(topic)) {
            request.setTopic(topic);
        }
        OnsTopicListResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        if (response == null || CollectionUtils.isEmpty(response.getData())) {
            return Collections.emptyList();
        }
        return response.getData().stream().map(e -> {
            OnsRocketMqTopic.Topic t = BeanCopierUtil.copyProperties(e, OnsRocketMqTopic.Topic.class);
            t.setRegionId(regionId);
            return t;
        }).collect(Collectors.toList());
    }

    /**
     * https://help.aliyun.com/document_detail/29591.html
     * 创建Topic
     *
     * @param regionId
     * @param aliyun
     * @param topic
     * @throws ClientException
     */
    public void createTopic(String regionId, AliyunConfig.Aliyun aliyun, OnsRocketMqTopic.Topic topic) throws ClientException {
        OnsTopicCreateRequest request = new OnsTopicCreateRequest();
        request.setInstanceId(topic.getInstanceId());
        request.setTopic(topic.getTopic());
        request.setRemark(topic.getRemark());
        request.setMessageType(topic.getMessageType());
        OnsTopicCreateResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        log.info("创建阿里云ONS-Topic: requestId={}, topic={}", response.getRequestId(), topic);
    }

}