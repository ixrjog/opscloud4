package com.baiyi.opscloud.workorder.processor.impl;

import com.baiyi.opscloud.common.helper.WorkOrderLeoDeployHelper;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.model.WorkOrderLeoDeployToken;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.entry.ApplicationDeployEntry;
import com.baiyi.opscloud.workorder.exception.TicketProcessException;
import com.baiyi.opscloud.workorder.exception.TicketVerifyException;
import com.baiyi.opscloud.workorder.processor.impl.base.BaseTicketProcessor;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/5/11 10:58
 * @Version 1.0
 */
@Component
public class ApplicationDeployTicketProcessor extends BaseTicketProcessor<ApplicationDeployEntry.LeoBuildVersion> {

    @Resource
    private WorkOrderLeoDeployHelper workOrderLeoDeployHelper;

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.APPLICATION_DEPLOY.name();
    }

    @Override
    protected void process(WorkOrderTicketEntry ticketEntry, ApplicationDeployEntry.LeoBuildVersion entry) throws TicketProcessException {
        WorkOrderLeoDeployToken token = WorkOrderLeoDeployToken.builder()
                .buildId(entry.getId())
                .applicationId(entry.getApplicationId())
                .build();
        // 设置令牌，@LeoDeployInterceptor 拦截器注解中使用
        workOrderLeoDeployHelper.set(token);
    }

    @Override
    protected Class<ApplicationDeployEntry.LeoBuildVersion> getEntryClassT() {
        return ApplicationDeployEntry.LeoBuildVersion.class;
    }

    @Override
    protected void handleVerify(WorkOrderTicketEntryParam.TicketEntry ticketEntry) throws TicketVerifyException {

    }

}
