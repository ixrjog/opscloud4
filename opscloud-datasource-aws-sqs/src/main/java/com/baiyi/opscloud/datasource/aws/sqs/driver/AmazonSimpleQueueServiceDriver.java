package com.baiyi.opscloud.datasource.aws.sqs.driver;

import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.ListQueuesRequest;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.baiyi.opscloud.common.config.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.sqs.service.AmazonSQSService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/3/25 15:59
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonSimpleQueueServiceDriver {

    /**
     * 查询SQS信息
     *
     * @param config
     * @param regionId
     * @return
     */
    public List<String> listQueues(AwsConfig.Aws config, String regionId) {
        ListQueuesRequest request = new ListQueuesRequest();
        request.setMaxResults(1000);
        List<String> queues = Lists.newArrayList();
        while (true) {
            ListQueuesResult result = AmazonSQSService.buildAmazonSQS(config, regionId).listQueues(request);
            queues.addAll(result.getQueueUrls());
            if (StringUtils.isNotBlank(result.getNextToken())) {
                request.setNextToken(result.getNextToken());
            } else {
                break;
            }
        }
        return queues;
    }

    /**
     * 获取SQS属性
     *
     * @param config
     * @param regionId
     * @param queueUrl
     * @return
     */
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_1HOUR, key = "'accountId_' + #config.account.id + '_regionId' + #regionId + '_queueUrl_' + #queueUrl", unless = "#result == null")
    public Map<String, String> getQueueAttributes(AwsConfig.Aws config, String regionId, String queueUrl) {
        GetQueueAttributesRequest request = new GetQueueAttributesRequest();
        request.setAttributeNames(Lists.newArrayList("All"));
        request.setQueueUrl(queueUrl);
        GetQueueAttributesResult result = AmazonSQSService.buildAmazonSQS(config, regionId).getQueueAttributes(request);
        return result.getAttributes();
    }

}
