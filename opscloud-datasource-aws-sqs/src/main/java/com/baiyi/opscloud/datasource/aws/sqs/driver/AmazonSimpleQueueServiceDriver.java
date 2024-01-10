package com.baiyi.opscloud.datasource.aws.sqs.driver;

import com.amazonaws.services.sqs.model.*;
import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.sqs.service.AmazonSQSService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
     * https://docs.aws.amazon.com/AWSSimpleQueueService/latest/APIReference/API_CreateQueue.html
     *
     * @param config
     * @param regionId
     * @param queueName
     * @param attributes queueUrl
     * @return
     */
    public String createQueue(AwsConfig.Aws config, String regionId, String queueName, Map<String, String> attributes) {
        CreateQueueRequest request = new CreateQueueRequest();
        request.setQueueName(queueName);
        if (!CollectionUtils.isEmpty(attributes)) {
            request.setAttributes(attributes);
        }
        CreateQueueResult result = AmazonSQSService.buildAmazonSQS(config, regionId).createQueue(request);
        return result.getQueueUrl();
    }

    /**
     * 查询Queue list信息
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
     * 通过QueueName 查询 QueueUrl
     *
     * @param config
     * @param regionId
     * @param queueName
     * @return
     */
    public String getQueue(AwsConfig.Aws config, String regionId, String queueName) {
        try {
            GetQueueUrlRequest request = new GetQueueUrlRequest();
            request.setQueueName(queueName);
            GetQueueUrlResult result = AmazonSQSService.buildAmazonSQS(config, regionId).getQueueUrl(request);
            return result.getQueueUrl();
        } catch (QueueDoesNotExistException e) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 获取Queue属性
     *
     * @param config
     * @param regionId
     * @param queueUrl
     * @return
     */
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1H, key = "'accountId_' + #config.account.id + '_regionId' + #regionId + '_queueUrl_' + #queueUrl", unless = "#result == null")
    public Map<String, String> getQueueAttributes(AwsConfig.Aws config, String regionId, String queueUrl) {
        GetQueueAttributesRequest request = new GetQueueAttributesRequest();
        request.setAttributeNames(Lists.newArrayList("All"));
        request.setQueueUrl(queueUrl);
        GetQueueAttributesResult result = AmazonSQSService.buildAmazonSQS(config, regionId).getQueueAttributes(request);
        return result.getAttributes();
    }

    @CacheEvict(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1H, key = "'accountId_' + #config.account.id + '_regionId' + #regionId + '_queueUrl_' + #queueUrl")
    public void evictWrap(AwsConfig.Aws config, String regionId, String queueUrl) {
    }

    public void setQueueAttributes(AwsConfig.Aws config, String regionId, String queueUrl, Map<String, String> attributes) {
        SetQueueAttributesRequest request = new SetQueueAttributesRequest();
        request.setQueueUrl(queueUrl);
        request.setAttributes(attributes);
        AmazonSQSService.buildAmazonSQS(config, regionId).setQueueAttributes(request);
        ((AmazonSimpleQueueServiceDriver) AopContext.currentProxy()).evictWrap(config, regionId, queueUrl);
        ((AmazonSimpleQueueServiceDriver) AopContext.currentProxy()).getQueueAttributes(config, regionId, queueUrl);
    }

}
