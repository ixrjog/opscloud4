package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/23 17:07
 * @Version 1.0
 */
@Component
public class ApolloReleaseApplicationFaultEmergencyEntryQuery extends ApolloReleaseApplicationEntryQuery {

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.APOLLO_RELEASE_FE.name();
    }

}
