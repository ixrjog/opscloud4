package com.baiyi.opscloud.workorder.processor.impl;

import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.ons.drive.AliyunOnsRocketMqGroupDrive;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsRocketMqGroup;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.VerifyTicketEntryException;
import com.baiyi.opscloud.workorder.processor.impl.extended.AbstractDatasourceAssetExtendedBaseTicketProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/1/9 2:38 PM
 * @Version 1.0
 */
@Slf4j
@Component
public class OnsGroupTicketProcessor extends AbstractDatasourceAssetExtendedBaseTicketProcessor<OnsRocketMqGroup.Group, AliyunConfig> {

    @Resource
    private AliyunOnsRocketMqGroupDrive aliyunOnsRocketMqGroupDrive;

    @Override
    protected void processHandle(WorkOrderTicketEntry ticketEntry, OnsRocketMqGroup.Group entry) throws TicketProcessException {
        AliyunConfig.Aliyun config = getDsConfig(ticketEntry, AliyunConfig.class).getAliyun();
        try {
            aliyunOnsRocketMqGroupDrive.createGroup(config.getRegionId(), config, entry);
            log.info("工单创建数据源实例资产: instanceUuid = {} , entry = {}", ticketEntry.getInstanceUuid(), entry);
        } catch (ClientException e) {
            throw new TicketProcessException("工单创建数据源实例资产失败: " + e.getMessage());
        }
    }

    @Override
    public void verify(WorkOrderTicketEntry ticketEntry) throws VerifyTicketEntryException {
        OnsRocketMqGroup.Group entry = this.toEntry(ticketEntry.getContent());
        if (StringUtils.isEmpty(entry.getGroupId()))
            throw new VerifyTicketEntryException("校验工单条目失败: 未指定GID名称!");
        if (!entry.getGroupId().startsWith("GID_"))
            throw new VerifyTicketEntryException("校验工单条目失败: GID名称必须为GID_!");
        if (!entry.getGroupId().matches("[0-9A-Z_]{7,64}"))
            throw new VerifyTicketEntryException("校验工单条目失败: GID名称不合规!");
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

}

