package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.query.impl.extended.UserGroupExtendedTicketEntryQuery;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/3/11 14:39
 * @Version 1.0
 */
@Component
public class ApolloEntryQuery extends UserGroupExtendedTicketEntryQuery {

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.APOLLO.name();
    }

}
