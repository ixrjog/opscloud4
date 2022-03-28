package com.baiyi.opscloud.datasource.aws;

import com.amazonaws.services.sns.model.Topic;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.base.BaseAwsTest;
import com.baiyi.opscloud.datasource.aws.sqs.driver.AmazonSimpleNotificationServiceDriver;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/3/28 18:16
 * @Version 1.0
 */
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

}
