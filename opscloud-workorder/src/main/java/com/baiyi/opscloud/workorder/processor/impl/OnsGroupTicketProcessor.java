package com.baiyi.opscloud.workorder.processor.impl;

import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.ons.driver.AliyunOnsRocketMqGroupDriver;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsRocketMqGroup;
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

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/9 2:38 PM
 * @Version 1.0
 */
@Slf4j
@Component
public class OnsGroupTicketProcessor extends AbstractDsAssetExtendedBaseTicketProcessor<OnsRocketMqGroup.Group, AliyunConfig> {

    @Resource
    private AliyunOnsRocketMqGroupDriver aliyunOnsRocketMqGroupDrive;

    @Override
    protected void processHandle(WorkOrderTicketEntry ticketEntry, OnsRocketMqGroup.Group entry) throws TicketProcessException {
        AliyunConfig.Aliyun config = getDsConfig(ticketEntry, AliyunConfig.class).getAliyun();
        try {
            aliyunOnsRocketMqGroupDrive.createGroup(entry.getRegionId(), config, entry);
            log.info("工单创建数据源实例资产: instanceUuid = {} , entry = {}", ticketEntry.getInstanceUuid(), entry);
        } catch (ClientException e) {
            throw new TicketProcessException("工单创建数据源实例资产失败: " + e.getMessage());
        }
    }

    @Override
    public void verifyHandle(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {
        OnsRocketMqGroup.Group entry = this.toEntry(ticketEntry.getContent());
        if (StringUtils.isEmpty(entry.getGroupId()))
            throw new TicketVerifyException("校验工单条目失败: 未指定GID名称!");
        if (!entry.getGroupId().startsWith("GID_"))
            throw new TicketVerifyException("校验工单条目失败: GID名称必须为GID_!");
        if (!entry.getGroupId().matches("[0-9A-Z_]{7,64}"))
            throw new TicketVerifyException("校验工单条目失败: GID名称不合规!");
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .assetType(getAssetType())
                .instanceUuid(ticketEntry.getInstanceUuid())
                .name(entry.getGroupId())
                .build();
        List<DatasourceInstanceAsset> list = dsInstanceAssetService.queryAssetByAssetParam(asset);
        if (!CollectionUtils.isEmpty((list))) {
            if (list.stream().anyMatch(e -> e.getAssetId().equals(entry.getInstanceId()))) {
                throw new TicketVerifyException("校验工单条目失败: GID已存在改ONS实例中");
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
            OnsRocketMqGroup.Group group = aliyunOnsRocketMqGroupDrive.getGroup(entry.getRegionId(), config, entry.getInstanceId(), entry.getGroupId());
            pullAsset(ticketEntry, group);
        } catch (ClientException e) {
            throw new TicketProcessException("GID创建失败,GID= " + entry.getGroupId());
        }
    }

}
