package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.datasource.aws.sqs.driver.AmazonSimpleQueueServiceDriver;
import com.baiyi.opscloud.datasource.aws.sqs.entity.SimpleQueueService;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractDsAssetExtendedBaseTicketProcessor;
import com.baiyi.opscloud.workorder.validator.QueueValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
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

    @Resource
    private QueueValidator queueValidator;

    @Override
    protected void processHandle(WorkOrderTicketEntry ticketEntry, SimpleQueueService.Queue entry) throws TicketProcessException {
        AwsConfig.Aws config = getDsConfig(ticketEntry, AwsConfig.class).getAws();
        try {
            String name = amazonSqsDriver.createQueue(config, entry.getRegionId(), entry.getQueueName(), entry.getAttributes());
            log.info("工单创建数据源实例资产: instanceUuid={}, entry={}", ticketEntry.getInstanceUuid(), entry);
            if (StringUtils.isBlank(name)) {
                throw new TicketProcessException("工单创建数据源实例资产失败！");
            }
        } catch (Exception e) {
            throw new TicketProcessException("工单创建数据源实例资产失败: {}", e.getMessage());
        }
    }

    @Override
    public void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
        SimpleQueueService.Queue entry = this.toEntry(ticketEntry.getContent());
        if (StringUtils.isEmpty(entry.getQueueName())) {
            throw new TicketVerifyException("校验工单条目失败: 未指定SQS名称！");
        }
        if (!entry.getQueueName().matches("[0-9a-z_]{7,80}")) {
            throw new TicketVerifyException("校验工单条目失败: SQS名称不合规！");
        }

        if (!entry.getQueueName().startsWith("transsnet_")) {
            throw new TicketVerifyException("校验工单条目失败: SQS名称必须以 transsnet_ 开始！");
        }

        switch (entry.getEnvName()) {
            case "dev":
                if (!entry.getQueueName().endsWith("_dev_queue")) {
                    throw new TicketVerifyException("校验工单条目失败: 开发环境SQS名称必须以 _dev_queue 结尾！");
                }
                break;
            case "daily":
                if (!entry.getQueueName().endsWith("_test_queue")) {
                    throw new TicketVerifyException("校验工单条目失败: 日常环境SQS名称必须以 _test_queue 结尾！");
                }
                break;
            case "frankfurt-daily":
                if (!entry.getQueueName().endsWith("_daily_queue")) {
                    throw new TicketVerifyException("校验工单条目失败: 日常环境SQS名称必须以 _daily_queue 结尾！");
                }
                break;
            case "gray":
                if (!entry.getQueueName().endsWith("_canary_queue")) {
                    throw new TicketVerifyException("校验工单条目失败: 灰度环境SQS名称必须以 _canary_queue 结尾！");
                }
                break;
            case "pre":
                if (!entry.getQueueName().endsWith("_pre_queue")) {
                    throw new TicketVerifyException("校验工单条目失败: 预发环境SQS名称必须以 _pre_queue 结尾！");
                }
                break;
            case "prod":
                if (!entry.getQueueName().endsWith("_prod_queue")) {
                    throw new TicketVerifyException("校验工单条目失败: 生产环境SQS名称必须以 _prod_queue 结尾！");
                }
                break;
            default:
                throw new TicketVerifyException("校验工单条目失败: 该环境不支持新建SQS！");
        }
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .assetType(getAssetType())
                .instanceUuid(ticketEntry.getInstanceUuid())
                .name(entry.getQueueName())
                .regionId(entry.getRegionId())
                .build();
        List<DatasourceInstanceAsset> list = dsInstanceAssetService.queryAssetByAssetParam(asset);
        if (!CollectionUtils.isEmpty(list)) {
            if (list.stream().anyMatch(e -> e.getName().equals(entry.getQueueName()))) {
                throw new TicketVerifyException("校验工单条目失败: 该地域SQS已存在！");
            }
        }
        queueValidator.verify(ticketEntry.getProperties());
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
            if (StringUtils.isBlank(queueUrl)) {
                throw new TicketProcessException("SQS创建失败: 工单创建数据源实例资产失败！");
            }
            Map<String, String> attributes = amazonSqsDriver.getQueueAttributes(config, entry.getRegionId(), entry.getQueueName());
            SimpleQueueService.Queue queue = SimpleQueueService.Queue.builder()
                    .queueUrl(queueUrl)
                    .regionId(entry.getRegionId())
                    .envName(entry.getEnvName())
                    .attributes(attributes)
                    .build();
            pullAsset(ticketEntry, queue);
            ticketEntry.setContent(JSONUtil.writeValueAsString(queue));
            ticketEntryService.update(ticketEntry);
        } catch (Exception e) {
            throw new TicketProcessException("SQS创建失败: queueName={}", entry.getQueueName());
        }
    }

}