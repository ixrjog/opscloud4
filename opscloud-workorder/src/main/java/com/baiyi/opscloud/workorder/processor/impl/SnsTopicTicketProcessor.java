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
import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
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
            String topicArn = amazonSnsDriver.createTopic(config, entry.getRegionId(), entry.getTopic(), entry.getAttributes());
            log.info("工单创建数据源实例资产: instanceUuid={}, entry={}", ticketEntry.getInstanceUuid(), entry);
            if (StringUtils.isBlank(topicArn)) {
                throw new TicketProcessException("工单创建数据源实例资产失败！");
            }
            Map<String, String> attributes = amazonSnsDriver.getTopicAttributes(config, entry.getRegionId(), topicArn);
            if (CollectionUtils.isEmpty(attributes)) {
                throw new TicketProcessException("SNS主题创建失败: 工单创建数据源实例资产失败！");
            }
            SimpleNotificationService.Topic topic = SimpleNotificationService.Topic.builder()
                    .topicArn(topicArn)
                    .regionId(entry.getRegionId())
                    .attributes(attributes)
                    .envName(entry.getEnvName())
                    .build();
            pullAsset(ticketEntry, topic);
            ticketEntry.setContent(JSONUtil.writeValueAsString(topic));
            ticketEntryService.update(ticketEntry);
        } catch (Exception e) {
            throw new TicketProcessException("工单创建数据源实例资产失败: {}", e.getMessage());
        }
    }

    @Override
    public void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
        SimpleNotificationService.Topic entry = this.toEntry(ticketEntry.getContent());
        String topic = entry.getTopic();
        if (StringUtils.isEmpty(topic)) {
            throw new TicketVerifyException("校验工单条目失败: 未指定SNS主题名称!");
        }
        if (topic.endsWith(".fifo")) {
            if (!"true".equals(ticketEntry.getProperties().get("FifoTopic"))) {
                throw new TicketVerifyException("校验工单条目失败: .fifo 结尾必须为 FIFO 主题");
            }
            List<String> strings = Splitter.on(".fifo").splitToList(topic);
            topic = strings.getFirst();
        } else {
            if ("true".equals(ticketEntry.getProperties().get("FifoTopic"))) {
                throw new TicketVerifyException("校验工单条目失败: FIFO 主题名称必须以 .fifo 结尾！");
            }
        }
        if (!topic.startsWith("transsnet_")) {
            throw new TicketVerifyException("校验工单条目失败: SNS主题名称必须以 transsnet_ 开始！");
        }

        if (!topic.matches("[0-9a-z_]{7,256}")) {
            throw new TicketVerifyException("校验工单条目失败: SNS主题名称不合规！");
        }
        switch (entry.getEnvName()) {
            case "dev":
                if (!topic.endsWith("_dev_topic")) {
                    throw new TicketVerifyException("校验工单条目失败: 开发环境SNS主题名称必须以 _dev_topic 结尾！");
                }
                break;
            case "daily":
                if (!topic.endsWith("_test_topic")) {
                    throw new TicketVerifyException("校验工单条目失败: 测试环境SNS主题名称必须以 _test_topic 结尾！");
                }
                break;
            case "frankfurt-daily":
                if (!topic.endsWith("_daily_topic")) {
                    throw new TicketVerifyException("校验工单条目失败: 日常环境SNS主题名称必须以 _daily_topic 结尾！");
                }
                break;
            case "gray":
                if (!topic.endsWith("_canary_topic")) {
                    throw new TicketVerifyException("校验工单条目失败: 灰度环境SNS主题名称必须以 _canary_topic 结尾！");
                }
                break;
            case "prod":
                if (!topic.endsWith("_prod_topic")) {
                    throw new TicketVerifyException("校验工单条目失败: 生产环境SNS主题名称必须以 _prod_topic 结尾！");
                }
                break;
            case "pre":
                if (!topic.endsWith("_pre_topic")) {
                    throw new TicketVerifyException("校验工单条目失败: 预发环境SNS主题名称必须以 _pre_topic 结尾！");
                }
                break;
            default:
                throw new TicketVerifyException("校验工单条目失败: 该环境不支持新建SNS主题！");
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
    }

}