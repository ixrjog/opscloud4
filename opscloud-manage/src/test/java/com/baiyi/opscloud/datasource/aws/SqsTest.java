package com.baiyi.opscloud.datasource.aws;

import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.base.BaseAwsTest;
import com.baiyi.opscloud.datasource.aws.sqs.driver.AmazonSimpleQueueServiceDriver;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/3/25 16:28
 * @Version 1.0
 */
public class SqsTest extends BaseAwsTest {

    @Resource
    private AmazonSimpleQueueServiceDriver amazonSQSDriver;

    @Test
    void listQueuesTest() {
        // "eu-west-1" ap-east-1
        AwsConfig.Aws awsConfig = getConfig().getAws();
        List<String> queues = amazonSQSDriver.listQueues(awsConfig, "eu-west-1");
        print("size = " + queues.size());
        queues.forEach(this::print);
    }

}