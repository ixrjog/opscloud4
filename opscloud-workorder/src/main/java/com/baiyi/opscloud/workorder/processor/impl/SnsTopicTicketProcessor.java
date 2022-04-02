package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.sqs.driver.AmazonSimpleNotificationServiceDriver;
import com.baiyi.opscloud.datasource.aws.sqs.entity.SimpleNotificationService;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractDsAssetExtendedBaseTicketProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author 修远
 * @Date 2022/4/1 6:05 PM
 * @Since 1.0
 */
@Slf4j
@Component
public class SnsTopicTicketProcessor extends AbstractDsAssetExtendedBaseTicketProcessor<SimpleNotificationService.Topic, AwsConfig> {

    @Resource
    private AmazonSimpleNotificationServiceDriver amazonSnsDriver;

    @Override
    protected void processHandle(WorkOrderTicketEntry ticketEntry, SimpleNotificationService.Topic entry) throws TicketProcessException {
        AwsConfig.Aws config = getDsConfig(ticketEntry, AwsConfig.class).getAws();
        try {
            String name = amazonSnsDriver.createTopic(config, entry.getRegionId(), entry.getTopic(), entry.getAttributes());
            log.info("工单创建数据源实例资产: instanceUuid = {} , entry = {}", ticketEntry.getInstanceUuid(), entry);
            if (StringUtils.isBlank(name))
                throw new TicketProcessException("工单创建数据源实例资产失败");
        } catch (Exception e) {
            throw new TicketProcessException("工单创建数据源实例资产失败: " + e.getMessage());
        }
    }

    @Override
    public void verifyHandle(WorkOrderTicketEntry ticketEntry) throws TicketVerifyException {
        SimpleNotificationService.Topic entry = this.toEntry(ticketEntry.getContent());
        if (StringUtils.isEmpty(entry.getTopic()))
            throw new TicketVerifyException("校验工单条目失败: 未指定SNS主题名称!");
        if (!entry.getTopic().matches("[0-9a-z_]{7,256}"))
            throw new TicketVerifyException("校验工单条目失败: SNS主题名称不合规!");
        switch (entry.getRegionId()) {
            case "ap-northeast-2":
                if (!entry.getTopic().endsWith("_dev_topic"))
                    throw new TicketVerifyException("校验工单条目失败: 开发环境SNS主题名称必须以 _dev_topic 结尾！");
                break;
            case "ap-east-1":
                if (!entry.getTopic().endsWith("_test_topic"))
                    throw new TicketVerifyException("校验工单条目失败: 测试环境SNS主题名称必须以 _test_topic 结尾！");
                break;
            case "eu-west-1":
                if (!entry.getTopic().endsWith("_canary_topic")
                        || entry.getTopic().endsWith("_prod_topic"))
                    throw new TicketVerifyException("校验工单条目失败: 灰度、生产SNS主题名称必须以 _canary_topic 或 _prod_topic 结尾！");
                break;
            default:
                throw new TicketVerifyException("校验工单条目失败: 该可用区下不支持新建SNS主题！");
        }
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .assetType(getAssetType())
                .instanceUuid(ticketEntry.getInstanceUuid())
                .name(entry.getTopic())
                .regionId(entry.getRegionId())
                .build();
        List<DatasourceInstanceAsset> list = dsInstanceAssetService.queryAssetByAssetParam(asset);
        if (!CollectionUtils.isEmpty(list)) {
            if (list.stream().anyMatch(e -> e.getName().equals(entry.getTopic()))) {
                throw new TicketVerifyException("校验工单条目失败: 该地域SQS已存在！");
            }
        }
    }

    private void propertyCheck(Map<String, String> properties, String key, Integer min, Integer max, String errMsg) throws TicketVerifyException {
        try {
            if (properties.containsKey(key)) {
                int value = Integer.parseInt(properties.get(key));
                if (value < min || value > max)
                    throw new TicketVerifyException("校验工单条目失败: " + errMsg);
            }
        } catch (NumberFormatException e) {
            throw new TicketVerifyException("校验工单条目失败: " + key + "只能为整数！");
        }
    }

    @Override
    protected void verifyHandle(Map<String, String> properties) throws TicketVerifyException {
        propertyCheck(properties, "DelaySeconds", 0, 900, "交付延迟时间应介于 0 秒至 15 分钟之间！");
        propertyCheck(properties, "MaximumMessageSize", 1024, 262144, "最大消息大小应介于 1 KB 和 256 KB之间！");
        propertyCheck(properties, "MessageRetentionPeriod", 60, 1209600, "消息保留周期应介于 1 分钟至 14 天之间！");
        propertyCheck(properties, "ReceiveMessageWaitTimeSeconds", 0, 20, "接收消息等待时间应介于 0 至 20 秒之间！");
        propertyCheck(properties, "VisibilityTimeout", 0, 43200, "可见性超时时间应介于 0 秒至 12 小时之间！");
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.SNS_TOPIC.name();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.AWS.name();
    }

    @Override
    protected Class<SimpleNotificationService.Topic> getEntryClassT() {
        return SimpleNotificationService.Topic.class;
    }

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, SimpleNotificationService.Topic entry) throws TicketProcessException {
        processHandle(ticketEntry, entry);
        AwsConfig.Aws config = getDsConfig(ticketEntry, AwsConfig.class).getAws();
        try {
            Map<String, String> attributes = amazonSnsDriver.getTopicAttributes(config, entry.getRegionId(), entry.getTopicArn());
            if (CollectionUtils.isEmpty(attributes))
                throw new TicketProcessException("SNS主题创建失败: 工单创建数据源实例资产失败");
            pullAsset(ticketEntry, SimpleNotificationService.Topic.builder()
                    .topicArn(entry.getTopicArn())
                    .regionId(entry.getRegionId())
                    .attributes(attributes)
                    .build());
        } catch (Exception e) {
            throw new TicketProcessException("SNS主题创建失败: topic = " + entry.getTopic());
        }
    }

}