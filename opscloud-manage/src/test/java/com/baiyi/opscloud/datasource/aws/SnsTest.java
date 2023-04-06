package com.baiyi.opscloud.datasource.aws;

import com.amazonaws.services.sns.model.NotFoundException;
import com.amazonaws.services.sns.model.Subscription;
import com.amazonaws.services.sns.model.Topic;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.base.BaseAwsTest;
import com.baiyi.opscloud.datasource.aws.sqs.driver.AmazonSimpleNotificationServiceDriver;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/3/28 18:16
 * @Version 1.0
 */
@Slf4j
public class SnsTest extends BaseAwsTest {

    @Resource
    private AmazonSimpleNotificationServiceDriver amazonSNSDriver;

    @Test
    void listTopicsTest() {
        AwsConfig.Aws awsConfig = getConfigById(23).getAws();
        List<Topic> topics = amazonSNSDriver.listTopics(awsConfig, "eu-west-1");
        print("size = " + topics.size());
        topics.forEach(this::print);
    }

    /**
     * {"topicArn":"arn:aws:sns:eu-west-1:502076313352:transsnet_close_account_canary_topic"}
     * {"topicArn":"arn:aws:sns:eu-west-1:502076313352:transsnet_close_account_perf_topic"}
     * {"topicArn":"arn:aws:sns:eu-west-1:502076313352:transsnet_close_account_prod_sx_topic"}
     * {"topicArn":"arn:aws:sns:eu-west-1:502076313352:transsnet_close_account_prod_topic"}
     */

    @Test
    void getTopicAttributesTest() {
        AwsConfig.Aws awsConfig = getConfigById(23).getAws();
        String topicArn = "arn:aws:sns:eu-west-1:502076313352:transsnet_close_account_canary_topic";
        print(amazonSNSDriver.getTopicAttributes(awsConfig, "eu-west-1", topicArn));
    }

    @Test
    void listSubscriptionsTest() {
        AwsConfig.Aws awsConfig = getConfigById(23).getAws();
        List<Subscription> subscriptions = amazonSNSDriver.listSubscriptions(awsConfig, "eu-west-1");
        print("size = " + subscriptions.size());
        subscriptions.forEach(this::print);
    }

    /**
     * {
     *     "subscriptionArn":"arn:aws:sns:eu-west-1:502076313352:transsnet_close_account_perf_topic:01ec9677-aac8-4f1c-a4ca-90eae6b3defe",
     *     "owner":"502076313352",
     *     "protocol":"sqs",
     *     "endpoint":"arn:aws:sqs:eu-west-1:502076313352:transsnet_close_account_perf_queue",
     *     "topicArn":"arn:aws:sns:eu-west-1:502076313352:transsnet_close_account_perf_topic"
     * }
     */


    /**
     * {
     * "Owner":"502076313352",
     * "RawMessageDelivery":"false",
     * "TopicArn":"arn:aws:sns:eu-west-1:502076313352:transsnet_close_account_perf_topic",
     * "Endpoint":"arn:aws:sqs:eu-west-1:502076313352:transsnet_close_account_perf_queue",
     * "Protocol":"sqs",
     * "PendingConfirmation":"false",
     * "ConfirmationWasAuthenticated":"true",
     * "SubscriptionArn":"arn:aws:sns:eu-west-1:502076313352:transsnet_close_account_perf_topic:01ec9677-aac8-4f1c-a4ca-90eae6b3defe"
     * }
     */
    @Test
    void getSubscriptionAttributesTest() {
        AwsConfig.Aws awsConfig = getConfigById(23).getAws();
        String subscriptionArn = "arn:aws:sns:eu-west-1:502076313352:transsnet_close_account_perf_topic:01ec9677-aac8-4f1c-a4ca-90eae6b3defe";
        print(amazonSNSDriver.getSubscriptionAttributes(awsConfig, "eu-west-1", subscriptionArn));
    }


    @Test
    void createSnsTopic() {
        AwsConfig.Aws awsConfig = getConfigById(23).getAws();
        List<DatasourceInstanceAsset> topicList = dsInstanceAssetService.listByInstanceAssetType(DEFAULT_DSINSTANCE_UUID, DsAssetTypeConstants.SNS_TOPIC.name());
        List<DatasourceInstanceAsset> prodTopic = topicList.stream()
                .filter(asset -> asset.getName().endsWith("_prod_topic"))
                .collect(Collectors.toList());
        prodTopic.forEach(asset -> {
            Map<String, String> attributes = amazonSNSDriver.getTopicAttributes(awsConfig, asset.getRegionId(), asset.getAssetKey());
            if (CollectionUtils.isEmpty(attributes)) {
                log.error("创建异常", asset.getName());
            } else {
                String newTopicArn = asset.getAssetKey().replace("_prod_topic", "_pre_topic");
                try {
                    amazonSNSDriver.getTopicAttributes(awsConfig, asset.getRegionId(), newTopicArn);
                    print(newTopicArn);
                } catch (NotFoundException e) {
                    Map<String, String> newAttributes = Maps.newHashMap();
                    newAttributes.put("DisplayName", StringUtils.isBlank(attributes.get("DisplayName")) ? "-" : attributes.get("DisplayName"));
                    String newTopic = asset.getName().replace("_prod_topic", "_pre_topic");
                    amazonSNSDriver.createTopic(awsConfig, asset.getRegionId(), newTopic, newAttributes);
                }
            }
        });
    }

    @Test
    void createSubscriptions() {
        AwsConfig.Aws awsConfig = getConfigById(23).getAws();
        List<DatasourceInstanceAsset> subList = dsInstanceAssetService.listByInstanceAssetType(DEFAULT_DSINSTANCE_UUID, DsAssetTypeConstants.SNS_SUBSCRIPTION.name());
        List<DatasourceInstanceAsset> prodSub = subList.stream()
                .filter(asset -> asset.getName().endsWith("_prod_topic"))
                .collect(Collectors.toList());
        prodSub.forEach(asset -> {
            try {
                String topicArn = asset.getAssetKey2().replace("_prod_topic", "_pre_topic");
                String protocol = "sqs";
                String endpoint = asset.getAssetKey().replace("_prod_queue", "_pre_queue");
                String queueName = asset.getAssetKey().split(":")[5].replace("_prod_queue", "_pre_queue");
                amazonSNSDriver.subscribe(awsConfig, asset.getRegionId(), topicArn, protocol, endpoint, queueName);
            } catch (Exception e) {
                log.error(asset.getName() + "=====", asset.getAssetKey());
            }
        });
    }
}
