package com.baiyi.opscloud.datasource.aws;

import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.datasource.aws.base.BaseAwsTest;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamPolicyDocument;
import com.baiyi.opscloud.datasource.aws.sqs.driver.AmazonSimpleQueueServiceDriver;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/3/25 16:28
 * @Version 1.0
 */
public class SqsTest extends BaseAwsTest {

    /**
     * https://sqs.eu-west-1.amazonaws.com/502076313352/transsnet_verify_realName_result_loan_canary_queue
     * https://sqs.eu-west-1.amazonaws.com/502076313352/transsnet_verify_realName_result_loan_perf_queue
     * https://sqs.eu-west-1.amazonaws.com/502076313352/transsnet_verify_realName_result_loan_prod_queue
     * https://sqs.eu-west-1.amazonaws.com/502076313352/transsnet_virtualbank_autowithdraw_process_canary_queue
     * https://sqs.eu-west-1.amazonaws.com/502076313352/transsnet_virtualbank_autowithdraw_process_canary_sx_queue
     * https://sqs.eu-west-1.amazonaws.com/502076313352/transsnet_virtualbank_autowithdraw_process_perf_queue
     * https://sqs.eu-west-1.amazonaws.com/502076313352/transsnet_virtualbank_autowithdraw_process_prod_queue
     * https://sqs.eu-west-1.amazonaws.com/502076313352/transsnet_virtualbank_autowithdraw_process_prod_sx_queue
     * https://sqs.eu-west-1.amazonaws.com/502076313352/transsnet_virtualbank_autowithdraw_schedule_tigger_canary_queue
     * https://sqs.eu-west-1.amazonaws.com/502076313352/transsnet_virtualbank_autowithdraw_schedule_tigger_canary_sx_queue
     * https://sqs.eu-west-1.amazonaws.com/502076313352/transsnet_virtualbank_autowithdraw_schedule_tigger_perf_queue
     * https://sqs.eu-west-1.amazonaws.com/502076313352/transsnet_virtualbank_autowithdraw_schedule_tigger_prod_queue
     * https://sqs.eu-west-1.amazonaws.com/502076313352/transsnet_virtualbank_autowithdraw_schedule_tigger_prod_sx_queue
     */

    @Resource
    private AmazonSimpleQueueServiceDriver amazonSQSDriver;

    @Test
    void listQueuesTest() {
        // "eu-west-1" ap-east-1
        AwsConfig.Aws awsConfig = getConfigById(23).getAws();
        List<String> queues = amazonSQSDriver.listQueues(awsConfig, "eu-west-1");
        print("size = " + queues.size());
        queues.forEach(this::print);
    }

    @Test
    void getQueueTest() {
        AwsConfig.Aws awsConfig = getConfigById(23).getAws();
        String url = amazonSQSDriver.getQueue(awsConfig, "ap-east-1", "newedge_bill_notify_process_test_queue");
        print(url);
    }

    @Test
    void getQueueAttributesTest() {
        AwsConfig.Aws awsConfig = getConfigById(23).getAws();
        String queueUrl = "https://sqs.ap-east-1.amazonaws.com/502076313352/chenzaifang_20220401_test_queue";
        Map<String, String> map = amazonSQSDriver.getQueueAttributes(awsConfig, "eu-west-1", queueUrl);
        if (StringUtils.isNotBlank(map.get("Policy"))) {
            IamPolicyDocument policyDocument = JSONUtil.readValue(map.get("Policy"), IamPolicyDocument.class);
            print(policyDocument);
        }
        print(map);
    }

    @Test
    void dddd() throws MalformedURLException {
        String queueUrl = "https://sqs.eu-west-1.amazonaws.com/502076313352/transsnet_virtualbank_autowithdraw_schedule_tigger_prod_sx_queue";
        URL url = new URL(queueUrl);
        print(url);
    }

}