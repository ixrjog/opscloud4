package com.baiyi.opscloud.workorder.processor.impl;

import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.ons.constants.OnsMessageTypeConstants;
import com.baiyi.opscloud.datasource.aliyun.ons.driver.AliyunOnsRocketMqTopicDriver;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsRocketMqTopic;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractDsAssetExtendedBaseTicketProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 阿里云ONS-Topic权限申请工单票据处理
 *
 * @Author baiyi
 * @Date 2022/1/7 6:21 PM
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OnsTopicTicketProcessor extends AbstractDsAssetExtendedBaseTicketProcessor<OnsRocketMqTopic.Topic, AliyunConfig> {

    private final AliyunOnsRocketMqTopicDriver aliyunOnsRocketMqTopicDrive;

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.ONS_ROCKETMQ_TOPIC.name();
    }

    @Override
    protected void processHandle(WorkOrderTicketEntry ticketEntry, OnsRocketMqTopic.Topic entry) throws TicketProcessException {
        AliyunConfig.Aliyun config = getDsConfig(ticketEntry, AliyunConfig.class).getAliyun();
        try {
            aliyunOnsRocketMqTopicDrive.createTopic(entry.getRegionId(), config, entry);
            log.info("工单创建数据源实例资产: instanceUuid={}, entry={}", ticketEntry.getInstanceUuid(), entry);
        } catch (ClientException e) {
            throw new TicketProcessException("工单创建数据源实例资产失败: {}", e.getMessage());
        }
    }

    @Override
    public void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
        OnsRocketMqTopic.Topic entry = this.toEntry(ticketEntry.getContent());
        if (StringUtils.isEmpty(entry.getTopic())) {
            throw new TicketVerifyException("校验工单条目失败: 未指定Topic名称！");
        }
        if (!entry.getTopic().startsWith("TOPIC_")) {
            throw new TicketVerifyException("校验工单条目失败: Topic名称必须以TOPIC_开头！");
        }
        if (!entry.getTopic().matches("[0-9A-Z_]{9,64}")) {
            throw new TicketVerifyException("校验工单条目失败: Topic名称不合规！");
        }
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .assetType(getAssetType())
                .instanceUuid(ticketEntry.getInstanceUuid())
                .name(entry.getTopic())
                .build();
        List<DatasourceInstanceAsset> list = dsInstanceAssetService.queryAssetByAssetParam(asset);
        if (!CollectionUtils.isEmpty(list)) {
            if (list.stream().anyMatch(e -> !e.getKind().equals(OnsMessageTypeConstants.getDesc(entry.getMessageType())))) {
                throw new TicketVerifyException("校验工单条目失败: Topic类型与其他环境不一致，请选择 {}", list.getFirst().getKind());
            }
            if (list.stream().anyMatch(e -> e.getAssetId().equals(entry.getInstanceId()))) {
                throw new TicketVerifyException("校验工单条目失败: Topic已存在改ONS实例中！");
            }
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

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, OnsRocketMqTopic.Topic entry) throws TicketProcessException {
        processHandle(ticketEntry, entry);
        AliyunConfig.Aliyun config = getDsConfig(ticketEntry, AliyunConfig.class).getAliyun();
        try {
            OnsRocketMqTopic.Topic topic = aliyunOnsRocketMqTopicDrive.getTopic(entry.getRegionId(), config, entry.getInstanceId(), entry.getTopic());
            pullAsset(ticketEntry, topic);
            DatasourceInstanceAsset queryAssetAsset = dsInstanceAssetService.getByUniqueKey(DatasourceInstanceAsset.builder()
                    .instanceUuid(ticketEntry.getInstanceUuid())
                    .assetId(topic.getInstanceId())
                    .assetType(DsAssetTypeConstants.ONS_ROCKETMQ_TOPIC.name())
                    .assetKey(topic.getTopic())
                    .build());
            DatasourceInstanceAsset asset = dsInstanceAssetService.getByUniqueKey(queryAssetAsset);
            DatasourceInstanceAsset queryParentAssetAsset = dsInstanceAssetService.getByUniqueKey(DatasourceInstanceAsset.builder()
                    .instanceUuid(ticketEntry.getInstanceUuid())
                    .assetId(topic.getInstanceId())
                    .assetType(DsAssetTypeConstants.ONS_ROCKETMQ_INSTANCE.name())
                    .assetKey(topic.getInstanceId())
                    .build());
            DatasourceInstanceAsset parentAsset = dsInstanceAssetService.getByUniqueKey(queryParentAssetAsset);
            asset.setParentId(parentAsset.getId());
            dsInstanceAssetService.update(asset);
        } catch (ClientException e) {
            throw new TicketProcessException("Topic创建失败: Topic={}", entry.getTopic());
        }
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ONS_ROCKETMQ_TOPIC.name();
    }

}