package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/23 17:08
 * @Version 1.0
 */
@Component
public class ApplicationDeployFaultEmergencyEntryQuery extends ApplicationDeployEntryQuery {

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.APPLICATION_DEPLOY_FE.name();
    }

}
