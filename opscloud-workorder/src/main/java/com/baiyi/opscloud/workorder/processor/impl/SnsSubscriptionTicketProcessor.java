package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.common.util.JacksonUtil;
import com.baiyi.opscloud.datasource.aws.iam.entity.IamPolicyDocument;
import com.baiyi.opscloud.datasource.aws.sqs.driver.AmazonSimpleNotificationServiceDriver;
import com.baiyi.opscloud.datasource.aws.sqs.driver.AmazonSimpleQueueServiceDriver;
import com.baiyi.opscloud.datasource.aws.sqs.entity.SimpleNotificationService;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractDsAssetExtendedBaseTicketProcessor;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author 修远
 * @Date 2022/4/6 4:37 PM
 * @Since 1.0
 */
@Slf4j
@Component
public class SnsSubscriptionTicketProcessor extends AbstractDsAssetExtendedBaseTicketProcessor<SimpleNotificationService.Subscription, AwsConfig> {

    private static final String DEFAULT_VERSION = "2012-10-17";

    private static final String DEFAULT_ID = "SQSDefaultPolicy";

    private static final String DEFAULT_SID = "topic-subscription";

    private static final String DEFAULT_EFFECT = "Allow";

    private static final String DEFAULT_ACTION = "SQS:SendMessage";

    @Resource
    private AmazonSimpleNotificationServiceDriver amazonSnsDriver;

    @Resource
    private AmazonSimpleQueueServiceDriver amazonSqsDriver;

    @Override
    protected void processHandle(WorkOrderTicketEntry ticketEntry, SimpleNotificationService.Subscription entry) throws TicketProcessException {
        AwsConfig.Aws config = getDsConfig(ticketEntry, AwsConfig.class).getAws();
        try {
            String subscriptionArn = subscribe(config, entry.getRegionId(), entry.getTopicArn(), entry.getProtocol(), entry.getEndpoint(), entry.getQueueName());
            log.info("工单创建数据源实例资产: instanceUuid = {} , entry = {}", ticketEntry.getInstanceUuid(), entry);
            if (StringUtils.isBlank(subscriptionArn))
                throw new TicketProcessException("工单创建数据源实例资产失败");
            SimpleNotificationService.Subscription subscription = SimpleNotificationService.Subscription.builder()
                    .subscriptionArn(subscriptionArn)
                    .topicArn(entry.getTopicArn())
                    .protocol(entry.getProtocol())
                    .endpoint(entry.getEndpoint())
                    .regionId(entry.getRegionId())
                    .build();
            pullAsset(ticketEntry, subscription);
            ticketEntry.setContent(JSONUtil.writeValueAsString(subscription));
            ticketEntryService.update(ticketEntry);
        } catch (Exception e) {
            throw new TicketProcessException("工单创建数据源实例资产失败: " + e.getMessage());
        }
    }

    private String subscribe(AwsConfig.Aws config, String regionId, String topicArn, String protocol, String endpoint, String queueName) {
        String subscriptionArn = amazonSnsDriver.subscribe(config, regionId, topicArn, protocol, endpoint);
        String queueUrl = amazonSqsDriver.getQueue(config, regionId, queueName);
        Map<String, String> attributes = amazonSqsDriver.getQueueAttributes(config, regionId, queueUrl);
        String policyDoc = getPolicyDoc(attributes, topicArn, endpoint);
        Map<String, String> newPolicy = new ImmutableMap.Builder<String, String>()
                .put("Policy", policyDoc)
                .build();
        amazonSqsDriver.setQueueAttributes(config, regionId, queueUrl, newPolicy);
        return subscriptionArn;
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
                    .Statement(Lists.newArrayList(buildStatement(topicArn, endpoint)))
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

    @Override
    public void verifyHandle(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
        SimpleNotificationService.Subscription entry = this.toEntry(ticketEntry.getContent());
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .assetType(getAssetType())
                .instanceUuid(ticketEntry.getInstanceUuid())
                .assetKey(entry.getEndpoint())
                .assetKey2(entry.getTopicArn())
                .regionId(entry.getRegionId())
                .build();
        List<DatasourceInstanceAsset> list = dsInstanceAssetService.queryAssetByAssetParam(asset);
        if (!CollectionUtils.isEmpty(list)) {
            throw new TicketVerifyException("校验工单条目失败: 订阅关系已存在！");
        }
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.SNS_SUBSCRIPTION.name();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.AWS.name();
    }

    @Override
    protected Class<SimpleNotificationService.Subscription> getEntryClassT() {
        return SimpleNotificationService.Subscription.class;
    }

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, SimpleNotificationService.Subscription entry) throws TicketProcessException {
        processHandle(ticketEntry, entry);
    }

}
