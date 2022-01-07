package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.processor.impl.base.asset.AssetPermissionExtendedAbstractTicketProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 阿里云ONS-Topic权限申请工单票据处理
 * @Author baiyi
 * @Date 2022/1/7 6:21 PM
 * @Version 1.0
 */
@Slf4j
@Component
public class OnsTopicTicketProcessor extends AssetPermissionExtendedAbstractTicketProcessor {

    @Resource
    private 

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, DatasourceInstanceAsset entry) throws TicketProcessException {
        User applicantUser = queryApplicant(ticketEntry);

        try {

        } catch (Exception e) {
            throw new TicketProcessException("工单授权策略失败：" + e.getMessage());
        }
    }

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.ONS_ROCKETMQ_TOPIC.name();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.ALIYUN.name();
    }

}

