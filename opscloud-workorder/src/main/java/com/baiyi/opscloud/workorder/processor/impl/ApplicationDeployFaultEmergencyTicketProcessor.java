package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.model.WorkOrderToken;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.entry.ApplicationDeployEntry;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/23 16:52
 * @Version 1.0
 */
@Component
public class ApplicationDeployFaultEmergencyTicketProcessor extends ApplicationDeployTicketProcessor {

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.APPLICATION_DEPLOY_FE.name();
    }

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, ApplicationDeployEntry.LeoBuildVersion entry) throws TicketProcessException {
        int buildId = entry.getId();
        WorkOrderToken.LeoDeployToken token = WorkOrderToken.LeoDeployToken.builder()
                .key(buildId)
                .applicationId(entry.getApplicationId())
                .faultEmergency(true)
                .build();
        // 设置令牌，@LeoDeployInterceptor 拦截器注解中使用
        workOrderLeoDeployHolder.setToken(token);
        // 多次工单申请也只记录最后一次工单ID
        LeoBuild saveLeoBuild = LeoBuild.builder()
                .id(buildId)
                .ticketId(ticketEntry.getWorkOrderTicketId())
                .build();
        leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
    }

}