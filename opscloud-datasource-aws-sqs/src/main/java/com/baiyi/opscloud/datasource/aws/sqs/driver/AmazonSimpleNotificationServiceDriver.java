package com.baiyi.opscloud.datasource.aws.sqs.driver;

import com.amazonaws.services.sns.model.*;
import com.baiyi.opscloud.common.config.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.sqs.service.AmazonSNSService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/3/28 18:01
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonSimpleNotificationServiceDriver {


    /**
     * https://docs.aws.amazon.com/sns/latest/api/API_CreateTopic.html
     * 创建SNS主题
     *
     * @param config
     * @param regionId
     * @param topic
     * @param attributes
     * @return
     */
    public String createTopic(AwsConfig.Aws config, String regionId, String topic, Map<String, String> attributes) {
        CreateTopicRequest request = new CreateTopicRequest();
        request.setName(topic);
        if (!CollectionUtils.isEmpty(attributes))
            request.setAttributes(attributes);
        CreateTopicResult result = AmazonSNSService.buildAmazonSNS(config, regionId).createTopic(request);
        return result.getTopicArn();
    }

    /**
     * 查询Topic列表
     *
     * @param config
     * @param regionId
     * @return
     */
    public List<Topic> listTopics(AwsConfig.Aws config, String regionId) {
        ListTopicsRequest request = new ListTopicsRequest();
        List<Topic> topics = Lists.newArrayList();
        while (true) {
            ListTopicsResult result = AmazonSNSService.buildAmazonSNS(config, regionId).listTopics(request);
            topics.addAll(result.getTopics());
            if (StringUtils.isNotBlank(result.getNextToken())) {
                request.setNextToken(result.getNextToken());
            } else {
                break;
            }
        }
        return topics;
    }

    /**
     * 获取Topic属性
     *
     * @param config
     * @param regionId
     * @param topicArn
     * @return
     */
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_1HOUR, key = "'accountId_' + #config.account.id + '_regionId' + #regionId + '_topicArn_' + #topicArn", unless = "#result == null")
    public Map<String, String> getTopicAttributes(AwsConfig.Aws config, String regionId, String topicArn) {
        GetTopicAttributesRequest request = new GetTopicAttributesRequest();
        request.setTopicArn(topicArn);
        GetTopicAttributesResult result = AmazonSNSService.buildAmazonSNS(config, regionId).getTopicAttributes(request);
        return result.getAttributes();
    }

    /**
     * 查询Subscription列表
     *
     * @param config
     * @param regionId
     * @return
     */
    public List<Subscription> listSubscriptions(AwsConfig.Aws config, String regionId) {
        ListSubscriptionsRequest request = new ListSubscriptionsRequest();
        List<Subscription> subscriptions = Lists.newArrayList();
        while (true) {
            ListSubscriptionsResult result = AmazonSNSService.buildAmazonSNS(config, regionId).listSubscriptions(request);
            subscriptions.addAll(result.getSubscriptions());
            if (StringUtils.isNotBlank(result.getNextToken())) {
                request.setNextToken(result.getNextToken());
            } else {
                break;
            }
        }
        return subscriptions;
    }

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_1HOUR, key = "'accountId_' + #config.account.id + '_regionId' + #regionId + '_subscriptionArn_' + #subscriptionArn", unless = "#result == null")
    public Map<String, String> getSubscriptionAttributes(AwsConfig.Aws config, String regionId, String subscriptionArn) {
        GetSubscriptionAttributesRequest request = new GetSubscriptionAttributesRequest();
        request.setSubscriptionArn(subscriptionArn);
        GetSubscriptionAttributesResult result = AmazonSNSService.buildAmazonSNS(config, regionId).getSubscriptionAttributes(request);
        return result.getAttributes();
    }

    public String subscribe(AwsConfig.Aws config, String regionId, String topicArn, String protocol, String endpoint) {
        SubscribeRequest request = new SubscribeRequest();
        request.setTopicArn(topicArn);
        request.setProtocol(protocol);
        request.setEndpoint(endpoint);
        SubscribeResult result = AmazonSNSService.buildAmazonSNS(config, regionId).subscribe(request);
        return result.getSubscriptionArn();
    }

}
