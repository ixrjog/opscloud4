package com.baiyi.opscloud.workorder.processor.impl;

import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.ons.drive.AliyunOnsRocketMqTopicDrive;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsRocketMqTopic;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractDatasourceAssetExtendedBaseTicketProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 阿里云ONS-Topic权限申请工单票据处理
 *
 * @Author baiyi
 * @Date 2022/1/7 6:21 PM
 * @Version 1.0
 */
@Slf4j
@Component
public class OnsTopicTicketProcessor extends AbstractDatasourceAssetExtendedBaseTicketProcessor<OnsRocketMqTopic.Topic, AliyunConfig> {

    @Resource
    private AliyunOnsRocketMqTopicDrive aliyunOnsRocketMqTopicDrive;

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.ONS_ROCKETMQ_TOPIC.name();
    }

    @Override
    protected void processHandle(WorkOrderTicketEntry ticketEntry, OnsRocketMqTopic.Topic entry) throws TicketProcessException {
        AliyunConfig.Aliyun config = getDsConfig(ticketEntry, AliyunConfig.class).getAliyun();
        try {
            aliyunOnsRocketMqTopicDrive.createTopic(config.getRegionId(), config, entry);
            log.info("工单创建数据源实例资产: instanceUuid = {} , entry = {}", ticketEntry.getInstanceUuid(), entry);
        } catch (ClientException e) {
            throw new TicketProcessException("工单创建数据源实例资产失败: " + e.getMessage());
        }
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    protected Class<OnsRocketMqTopic.Topic> getEntryClassT() {
        return OnsRocketMqTopic.Topic.class;
    }

}

