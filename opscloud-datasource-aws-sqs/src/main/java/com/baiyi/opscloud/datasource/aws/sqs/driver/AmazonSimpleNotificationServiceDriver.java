package com.baiyi.opscloud.datasource.aws.sqs.driver;

import com.amazonaws.services.sns.model.*;
import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.common.util.JacksonUtil;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamPolicyDocument;
import com.baiyi.opscloud.datasource.aws.sqs.service.AmazonSNSService;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
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

    private final AmazonSimpleQueueServiceDriver amazonSqsDriver;

    private static final String DEFAULT_VERSION = "2012-10-17";
    private static final String DEFAULT_ID = "SQSDefaultPolicy";
    private static final String DEFAULT_SID = "topic-subscription";
    private static final String DEFAULT_EFFECT = "Allow";
    private static final String DEFAULT_ACTION = "SQS:SendMessage";

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
        if (!CollectionUtils.isEmpty(attributes)) {
            request.setAttributes(attributes);
        }
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
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1H, key = "'accountId_' + #config.account.id + '_regionId' + #regionId + '_topicArn_' + #topicArn", unless = "#result == null")
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

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1H, key = "'accountId_' + #config.account.id + '_regionId' + #regionId + '_subscriptionArn_' + #subscriptionArn", unless = "#result == null")
    public Map<String, String> getSubscriptionAttributes(AwsConfig.Aws config, String regionId, String subscriptionArn) {
        GetSubscriptionAttributesRequest request = new GetSubscriptionAttributesRequest();
        request.setSubscriptionArn(subscriptionArn);
        GetSubscriptionAttributesResult result = AmazonSNSService.buildAmazonSNS(config, regionId).getSubscriptionAttributes(request);
        return result.getAttributes();
    }

    public String subscribe(AwsConfig.Aws config, String regionId, String topicArn, String protocol, String endpoint, String queueName) {
        String subscriptionArn = subscribe(config, regionId, topicArn, protocol, endpoint);
        String queueUrl = amazonSqsDriver.getQueue(config, regionId, queueName);
        Map<String, String> attributes = amazonSqsDriver.getQueueAttributes(config, regionId, queueUrl);
        String policyDoc = getPolicyDoc(attributes, topicArn, endpoint);
        Map<String, String> newPolicy = new ImmutableMap.Builder<String, String>()
                .put("Policy", policyDoc)
                .build();
        amazonSqsDriver.setQueueAttributes(config, regionId, queueUrl, newPolicy);
        return subscriptionArn;
    }

    private String subscribe(AwsConfig.Aws config, String regionId, String topicArn, String protocol, String endpoint) {
        SubscribeRequest request = new SubscribeRequest();
        request.setTopicArn(topicArn);
        request.setProtocol(protocol);
        request.setEndpoint(endpoint);
        SubscribeResult result = AmazonSNSService.buildAmazonSNS(config, regionId).subscribe(request);
        return result.getSubscriptionArn();
    }

    private String getPolicyDoc(Map<String, String> attributes, String topicArn, String endpoint) {
        if (StringUtils.isNotBlank(attributes.get("Policy"))) {
            IamPolicyDocument policyDocument = JSONUtil.readValue(attributes.get("Policy"), IamPolicyDocument.class);
            try {
                assert policyDocument != null;
                List<IamPolicyDocument.Statement> statementList = policyDocument.getStatement();
                if (statementList.stream().noneMatch(e -> e.getSid().equals(Joiner.on("-").join(DEFAULT_SID, topicArn)))) {
                    statementList.add(buildStatement(topicArn, endpoint));
                    policyDocument.setStatement(statementList);
                }
            } catch (NullPointerException e) {
                policyDocument.setStatement(Lists.newArrayList(buildStatement(topicArn, endpoint)));
            }
            return JSONUtil.writeValueAsString(policyDocument);
        } else {
            IamPolicyDocument policyDocument = IamPolicyDocument.builder()
                    .version(DEFAULT_VERSION)
                    .id(Joiner.on("/").join(endpoint, DEFAULT_ID))
                    .statement(Lists.newArrayList(buildStatement(topicArn, endpoint)))
                    .build();
            return JacksonUtil.toJSONString(policyDocument);
        }
    }

    private IamPolicyDocument.Statement buildStatement(String topicArn, String endpoint) {
        Map<String, String> principal = new ImmutableMap.Builder<String, String>()
                .put("AWS", "*")
                .build();
        Map<String, String> arnLike = new ImmutableMap.Builder<String, String>()
                .put("aws:SourceArn", topicArn)
                .build();
        IamPolicyDocument.Condition condition = IamPolicyDocument.Condition.builder()
                .arnLike(arnLike)
                .build();
        return IamPolicyDocument.Statement.builder()
                .sid(Joiner.on("-").join(DEFAULT_SID, topicArn))
                .effect(DEFAULT_EFFECT)
                .principal(principal)
                .action(DEFAULT_ACTION)
                .resource(endpoint)
                .condition(condition)
                .build();
    }

}
