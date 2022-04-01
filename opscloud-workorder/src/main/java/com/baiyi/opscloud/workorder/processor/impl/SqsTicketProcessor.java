package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.datasource.aws.sqs.driver.AmazonSimpleQueueServiceDriver;
import com.baiyi.opscloud.datasource.aws.sqs.entity.SimpleQueueService;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractDsAssetExtendedBaseTicketProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author 修远
 * @Date 2022/3/30 11:33 AM
 * @Since 1.0
 */
@Slf4j
@Component
public class SqsTicketProcessor extends AbstractDsAssetExtendedBaseTicketProcessor<SimpleQueueService.Queue, AwsConfig> {

    @Resource
    private AmazonSimpleQueueServiceDriver amazonSqsDriver;

    @Override
    protected void processHandle(WorkOrderTicketEntry ticketEntry, SimpleQueueService.Queue entry) throws TicketProcessException {
        AwsConfig.Aws config = getDsConfig(ticketEntry, AwsConfig.class).getAws();
        try {
            String name = amazonSqsDriver.createQueue(config, entry.getRegionId(), entry.getQueueName(), entry.getAttributes());
            log.info("工单创建数据源实例资产: instanceUuid = {} , entry = {}", ticketEntry.getInstanceUuid(), entry);
            if (StringUtils.isBlank(name))
                throw new TicketProcessException("工单创建数据源实例资产失败");
        } catch (Exception e) {
            throw new TicketProcessException("工单创建数据源实例资产失败: " + e.getMessage());
        }
    }

    @Override
    public void verifyHandle(WorkOrderTicketEntry ticketEntry) throws TicketVerifyException {
        SimpleQueueService.Queue entry = this.toEntry(ticketEntry.getContent());
        if (StringUtils.isEmpty(entry.getQueueName()))
            throw new TicketVerifyException("校验工单条目失败: 未指定SQS名称!");
        if (!entry.getQueueName().matches("[0-9a-z_]{7,80}"))
            throw new TicketVerifyException("校验工单条目失败: SQS名称不合规!");
        switch (entry.getRegionId()) {
            case "ap-northeast-2":
                if (!entry.getQueueName().endsWith("_dev_queue"))
                    throw new TicketVerifyException("开发环境SQS名称必须以 _dev_queue 结尾");
                break;
            case "ap-east-1":
                if (!entry.getQueueName().endsWith("_test_queue"))
                    throw new TicketVerifyException("测试环境SQS名称必须以 _test_queue 结尾");
                break;
            case "eu-west-1":
                if (!entry.getQueueName().endsWith("_canary_queue")
                        || entry.getQueueName().endsWith("_prod_queue"))
                    throw new TicketVerifyException("灰度、生产SQS名称必须以 _canary_queue 或 _prod_queue 结尾");
                break;
            default:
                throw new TicketVerifyException("该可用区下不支持新建SQS");
        }
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .assetType(getAssetType())
                .instanceUuid(ticketEntry.getInstanceUuid())
                .name(entry.getQueueName())
                .regionId(entry.getRegionId())
                .build();
        List<DatasourceInstanceAsset> list = dsInstanceAssetService.queryAssetByAssetParam(asset);
        if (CollectionUtils.isNotEmpty(list)) {
            if (list.stream().anyMatch(e -> e.getName().equals(entry.getQueueName()))) {
                throw new TicketVerifyException("校验工单条目失败: 该地域SQS已存在");
            }
        }
    }

    private void propertyCheck(Map<String, String> properties, String key, Integer min, Integer max, String errMsg) throws TicketVerifyException {
        try {
            if (properties.containsKey(key)) {
                int value = Integer.parseInt(properties.get(key));
                if (value < min || value > max)
                    throw new TicketVerifyException(errMsg);
            }
        } catch (NumberFormatException e) {
            throw new TicketVerifyException(key + "只能为整数");
        }

    }

    @Override
    protected void verifyHandle(Map<String, String> properties) throws TicketVerifyException {
        propertyCheck(properties, "DelaySeconds", 0, 900, "交付延迟时间应介于 0 秒至 15 分钟之间");
        propertyCheck(properties, "MaximumMessageSize", 1024, 262144, "最大消息大小应介于 1 KB 和 256 KB之间");
        propertyCheck(properties, "MessageRetentionPeriod", 60, 1209600, "消息保留周期应介于 1 分钟至 14 天之间");
        propertyCheck(properties, "ReceiveMessageWaitTimeSeconds", 0, 20, "接收消息等待时间应介于 0 至 20 秒之间");
        propertyCheck(properties, "VisibilityTimeout", 0, 43200, "可见性超时时间应介于 0 秒至 12 小时之间");
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.SQS.name();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.AWS.name();
    }

    @Override
    protected Class<SimpleQueueService.Queue> getEntryClassT() {
        return SimpleQueueService.Queue.class;
    }

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, SimpleQueueService.Queue entry) throws TicketProcessException {
        processHandle(ticketEntry, entry);
        AwsConfig.Aws config = getDsConfig(ticketEntry, AwsConfig.class).getAws();
        try {
            String queueUrl = amazonSqsDriver.getQueue(config, entry.getRegionId(), entry.getQueueName());
            if (StringUtils.isBlank(queueUrl))
                throw new TicketProcessException("工单创建数据源实例资产失败");
            Map<String, String> attributes = amazonSqsDriver.getQueueAttributes(config, entry.getRegionId(), entry.getQueueName());
            pullAsset(ticketEntry, SimpleQueueService.Queue.builder()
                    .queueUrl(queueUrl)
                    .regionId(entry.getRegionId())
                    .attributes(attributes)
                    .build());
        } catch (Exception e) {
            throw new TicketProcessException("SQS创建失败,name = " + entry.getQueueName());
        }
    }
}
