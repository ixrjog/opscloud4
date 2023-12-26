package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.datasource.aws.sqs.driver.AmazonSimpleNotificationServiceDriver;
import com.baiyi.opscloud.datasource.aws.sqs.entity.SimpleNotificationService;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractDsAssetExtendedBaseTicketProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @Author 修远
 * @Date 2022/4/6 4:37 PM
 * @Since 1.0
 */
@Slf4j
@Component
public class SnsSubscriptionTicketProcessor extends AbstractDsAssetExtendedBaseTicketProcessor<SimpleNotificationService.Subscription, AwsConfig> {

    @Resource
    private AmazonSimpleNotificationServiceDriver amazonSnsDriver;

    @Override
    protected void processHandle(WorkOrderTicketEntry ticketEntry, SimpleNotificationService.Subscription entry) throws TicketProcessException {
        AwsConfig.Aws config = getDsConfig(ticketEntry, AwsConfig.class).getAws();
        try {
            String subscriptionArn = amazonSnsDriver.subscribe(config, entry.getRegionId(), entry.getTopicArn(), entry.getProtocol(), entry.getEndpoint(), entry.getQueueName());
            log.info("工单创建数据源实例资产: instanceUuid={}, entry={}", ticketEntry.getInstanceUuid(), entry);
            if (StringUtils.isBlank(subscriptionArn)) {
                throw new TicketProcessException("工单创建数据源实例资产失败！");
            }
            SimpleNotificationService.Subscription subscription = SimpleNotificationService.Subscription.builder()
                    .subscriptionArn(subscriptionArn)
                    .topicArn(entry.getTopicArn())
                    .protocol(entry.getProtocol())
                    .endpoint(entry.getEndpoint())
                    .regionId(entry.getRegionId())
                    .envName(entry.getEnvName())
                    .build();
            pullAsset(ticketEntry, subscription);
            ticketEntry.setContent(JSONUtil.writeValueAsString(subscription));
            ticketEntryService.update(ticketEntry);
        } catch (Exception e) {
            throw new TicketProcessException("工单创建数据源实例资产失败: {}", e.getMessage());
        }
    }

    @Override
    public void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
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