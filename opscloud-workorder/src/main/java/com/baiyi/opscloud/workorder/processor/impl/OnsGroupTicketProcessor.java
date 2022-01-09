package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.processor.impl.base.AbstractAssetPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/1/9 2:38 PM
 * @Version 1.0
 */
@Slf4j
@Component
public class OnsGroupTicketProcessor extends AbstractAssetPermission {

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, DatasourceInstanceAsset entry) throws TicketProcessException {
        User createUser = queryCreateUser(ticketEntry);

        try {

        } catch (Exception e) {
            throw new TicketProcessException("工单授权策略失败：" + e.getMessage());
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

}

