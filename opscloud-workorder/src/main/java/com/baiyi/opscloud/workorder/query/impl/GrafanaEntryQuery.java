package com.baiyi.opscloud.workorder.query.impl;

import com.baiyi.opscloud.workorder.constants.WorkOrderKeyConstants;
import com.baiyi.opscloud.workorder.query.impl.extended.UserGroupExtendedTicketEntryQuery;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/3/22 10:01
 * @Version 1.0
 */
@Component
public class GrafanaEntryQuery extends UserGroupExtendedTicketEntryQuery {

    @Override
    public String getKey() {
        return WorkOrderKeyConstants.GRAFANA.name();
    }

}
