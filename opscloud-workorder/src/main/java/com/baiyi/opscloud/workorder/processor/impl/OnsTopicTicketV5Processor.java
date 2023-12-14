package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.ons.driver.AliyunOnsTopicV5Driver;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsTopicV5;
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
 * @Author 修远
 * @Date 2023/9/12 8:09 PM
 * @Since 1.0
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class OnsTopicTicketV5Processor extends AbstractDsAssetExtendedBaseTicketProcessor<OnsTopicV5.Topic, AliyunConfig> {

    private final AliyunOnsTopicV5Driver aliyunOnsTopicV5Driver;

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.ONS5_TOPIC.name();
    }

    @Override
    protected void processHandle(WorkOrderTicketEntry ticketEntry, OnsTopicV5.Topic entry) throws TicketProcessException {
        AliyunConfig.Aliyun config = getDsConfig(ticketEntry, AliyunConfig.class).getAliyun();
        try {
            aliyunOnsTopicV5Driver.createTopic(entry.getRegionId(), config, entry);
            log.info("工单创建数据源实例资产: instanceUuid={}, entry={}", ticketEntry.getInstanceUuid(), entry);
        } catch (Exception e) {
            throw new TicketProcessException("工单创建数据源实例资产失败: {}", e.getMessage());
        }
    }

    @Override
    public void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
        OnsTopicV5.Topic entry = this.toEntry(ticketEntry.getContent());
        if (StringUtils.isEmpty(entry.getTopicName())) {
            throw new TicketVerifyException("校验工单条目失败: 未指定Topic名称！");
        }
        if (!entry.getTopicName().startsWith("TOPIC_")) {
            throw new TicketVerifyException("校验工单条目失败: Topic名称必须以TOPIC_开头！");
        }
        if (!entry.getTopicName().matches("[0-9A-Z_]{9,60}")) {
            throw new TicketVerifyException("校验工单条目失败: Topic名称不合规！");
        }
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .assetType(getAssetType())
                .instanceUuid(ticketEntry.getInstanceUuid())
                .name(entry.getTopicName())
                .build();
        List<DatasourceInstanceAsset> list = dsInstanceAssetService.queryAssetByAssetParam(asset);
        if (!CollectionUtils.isEmpty(list)) {
            if (list.stream().anyMatch(e -> !e.getKind().equals(entry.getMessageType()))) {
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
    protected Class<OnsTopicV5.Topic> getEntryClassT() {
        return OnsTopicV5.Topic.class;
    }

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, OnsTopicV5.Topic entry) throws TicketProcessException {
        processHandle(ticketEntry, entry);
        AliyunConfig.Aliyun config = getDsConfig(ticketEntry, AliyunConfig.class).getAliyun();
        try {
            OnsTopicV5.Topic topic = aliyunOnsTopicV5Driver.getTopic(entry.getRegionId(), config, entry.getInstanceId(), entry.getTopicName());
            pullAsset(ticketEntry, topic);
            DatasourceInstanceAsset queryAssetAsset = dsInstanceAssetService.getByUniqueKey(DatasourceInstanceAsset.builder()
                    .instanceUuid(ticketEntry.getInstanceUuid())
                    .assetId(topic.getInstanceId())
                    .assetType(DsAssetTypeConstants.ONS5_TOPIC.name())
                    .assetKey(topic.getTopicName())
                    .build());
            DatasourceInstanceAsset asset = dsInstanceAssetService.getByUniqueKey(queryAssetAsset);
            DatasourceInstanceAsset queryParentAssetAsset = dsInstanceAssetService.getByUniqueKey(DatasourceInstanceAsset.builder()
                    .instanceUuid(ticketEntry.getInstanceUuid())
                    .assetId(topic.getInstanceId())
                    .assetType(DsAssetTypeConstants.ONS5_INSTANCE.name())
                    .assetKey(topic.getInstanceId())
                    .build());
            DatasourceInstanceAsset parentAsset = dsInstanceAssetService.getByUniqueKey(queryParentAssetAsset);
            asset.setParentId(parentAsset.getId());
            dsInstanceAssetService.update(asset);
        } catch (Exception e) {
            throw new TicketProcessException("Topic创建失败: Topic={}", entry.getTopicName());
        }
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ONS5_TOPIC.name();
    }

}