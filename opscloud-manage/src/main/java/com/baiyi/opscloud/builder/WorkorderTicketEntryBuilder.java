package com.baiyi.opscloud.builder;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.bo.WorkorderTicketEntryBO;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketEntry;
import com.baiyi.opscloud.domain.param.workorder.WorkorderTicketEntryParam;

/**
 * @Author baiyi
 * @Date 2020/4/28 12:53 下午
 * @Version 1.0
 */
public class WorkorderTicketEntryBuilder {

    public static OcWorkorderTicketEntry build(WorkorderTicketEntryParam.TicketEntry ticketEntry, String name) {
        WorkorderTicketEntryBO workorderTicketEntryBO = WorkorderTicketEntryBO.builder()
                .workorderTicketId(ticketEntry.getTicketId())
                .name(name)
                .entryKey(ticketEntry.getEntryKey())
                .entryDetail(JSON.toJSONString(ticketEntry.getTicketEntry()))
                .build();
        return covert(workorderTicketEntryBO);
    }

    private static OcWorkorderTicketEntry covert(WorkorderTicketEntryBO workorderTicketEntryBO) {
        return BeanCopierUtils.copyProperties(workorderTicketEntryBO, OcWorkorderTicketEntry.class);
    }
}
