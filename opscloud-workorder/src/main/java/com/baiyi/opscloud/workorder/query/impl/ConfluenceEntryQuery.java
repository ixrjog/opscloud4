package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.query.impl.extended.BaseUserGroupExtendedTicketEntryQuery;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/1/11 5:39 PM
 * @Version 1.0
 */
@Component
public class ConfluenceEntryQuery extends BaseUserGroupExtendedTicketEntryQuery {

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.CONFLUENCE.name();
    }

}