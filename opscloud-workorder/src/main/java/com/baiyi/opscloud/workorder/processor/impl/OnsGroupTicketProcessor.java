package com.baiyi.opscloud.workorder.processor.impl;

import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.ons.driver.AliyunOnsRocketMqGroupDriver;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsRocketMqGroup;
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
 * @Author baiyi
 * @Date 2022/1/9 2:38 PM
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OnsGroupTicketProcessor extends AbstractDsAssetExtendedBaseTicketProcessor<OnsRocketMqGroup.Group, AliyunConfig> {

    private final AliyunOnsRocketMqGroupDriver aliyunOnsRocketMqGroupDrive;

    @Override
    protected void processHandle(WorkOrderTicketEntry ticketEntry, OnsRocketMqGroup.Group entry) throws TicketProcessException {
        AliyunConfig.Aliyun config = getDsConfig(ticketEntry, AliyunConfig.class).getAliyun();
        try {
            aliyunOnsRocketMqGroupDrive.createGroup(entry.getRegionId(), config, entry);
            log.info("工单创建数据源实例资产: instanceUuid={}, entry={}", ticketEntry.getInstanceUuid(), entry);
        } catch (ClientException e) {
            throw new TicketProcessException("工单创建数据源实例资产失败: {}", e.getMessage());
        }
    }

    @Override
    public void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
        OnsRocketMqGroup.Group entry = this.toEntry(ticketEntry.getContent());
        if (StringUtils.isEmpty(entry.getGroupId())) {
            throw new TicketVerifyException("校验工单条目失败: 未指定GID名称!");
        }
        if (!entry.getGroupId().startsWith("GID_")) {
            throw new TicketVerifyException("校验工单条目失败: GID名称必须以 GID_ 开头！");
        }
        if (!entry.getGroupId().matches("[0-9A-Z_]{7,64}")) {
            throw new TicketVerifyException("校验工单条目失败: GID名称不合规！");
        }
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .assetType(getAssetType())
                .instanceUuid(ticketEntry.getInstanceUuid())
                .name(entry.getGroupId())
                .build();
        List<DatasourceInstanceAsset> list = dsInstanceAssetService.queryAssetByAssetParam(asset);
        if (!CollectionUtils.isEmpty((list))) {
            if (list.stream().anyMatch(e -> !e.getKind().equals(entry.getGroupType()))) {
                throw new TicketVerifyException("校验工单条目失败: GID类型与其他环境不一致，请选择 {}！", list.getFirst().getKind());
            }
            if (list.stream().anyMatch(e -> e.getAssetId().equals(entry.getInstanceId()))) {
                throw new TicketVerifyException("校验工单条目失败: GID已存在改ONS实例中！");
            }
        }
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.ONS_ROCKETMQ_GROUP.name();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

    @Override
    protected Class<OnsRocketMqGroup.Group> getEntryClassT() {
        return OnsRocketMqGroup.Group.class;
    }

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, OnsRocketMqGroup.Group entry) throws TicketProcessException {
        processHandle(ticketEntry, entry);
        AliyunConfig.Aliyun config = getDsConfig(ticketEntry, AliyunConfig.class).getAliyun();
        try {
            OnsRocketMqGroup.Group group = aliyunOnsRocketMqGroupDrive.getGroup(entry.getRegionId(), config, entry.getInstanceId(), entry.getGroupId(), entry.getGroupType());
            pullAsset(ticketEntry, group);
            DatasourceInstanceAsset queryAssetAsset = dsInstanceAssetService.getByUniqueKey(DatasourceInstanceAsset.builder()
                    .instanceUuid(ticketEntry.getInstanceUuid())
                    .assetId(group.getInstanceId())
                    .assetType(DsAssetTypeConstants.ONS_ROCKETMQ_GROUP.name())
                    .assetKey(group.getGroupId())
                    .build());
            DatasourceInstanceAsset asset = dsInstanceAssetService.getByUniqueKey(queryAssetAsset);
            DatasourceInstanceAsset queryParentAssetAsset = dsInstanceAssetService.getByUniqueKey(DatasourceInstanceAsset.builder()
                    .instanceUuid(ticketEntry.getInstanceUuid())
                    .assetId(group.getInstanceId())
                    .assetType(DsAssetTypeConstants.ONS_ROCKETMQ_INSTANCE.name())
                    .assetKey(group.getInstanceId())
                    .build());
            DatasourceInstanceAsset parentAsset = dsInstanceAssetService.getByUniqueKey(queryParentAssetAsset);
            asset.setParentId(parentAsset.getId());
            dsInstanceAssetService.update(asset);
        } catch (ClientException e) {
            throw new TicketProcessException("GID创建失败: GID={}", entry.getGroupId());
        }
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ONS_ROCKETMQ_GROUP.name();
    }

}