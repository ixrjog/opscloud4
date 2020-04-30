package com.baiyi.opscloud.builder;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.bo.WorkorderTicketEntryBO;
import com.baiyi.opscloud.common.base.WorkorderKey;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketEntry;
import com.baiyi.opscloud.domain.param.workorder.WorkorderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.server.OcServerGroupVO;
import com.baiyi.opscloud.domain.vo.workorder.OcWorkorderTicketEntryVO;
import com.baiyi.opscloud.factory.ticket.entry.ServerGroupEntry;

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

    public static OcWorkorderTicketEntryVO.Entry build(int ticketId, OcServerGroup ocServerGroup) {
        ServerGroupEntry entry = ServerGroupEntry.builder()
                .serverGroup(BeanCopierUtils.copyProperties(ocServerGroup, OcServerGroupVO.ServerGroup.class))
                .build();

        WorkorderTicketEntryBO workorderTicketEntryBO = WorkorderTicketEntryBO.builder()
                .workorderTicketId(ticketId)
                .name(ocServerGroup.getName())
                .businessId(ocServerGroup.getId())
                .entryKey(WorkorderKey.SERVER_GROUP.getKey())
                .comment(ocServerGroup.getComment())
                .ticketEntry(entry)
                .entryDetail(JSON.toJSONString(entry))
                .build();
        return covertVO(workorderTicketEntryBO);
    }

    private static OcWorkorderTicketEntryVO.Entry covertVO(WorkorderTicketEntryBO workorderTicketEntryBO) {
        return BeanCopierUtils.copyProperties(workorderTicketEntryBO, OcWorkorderTicketEntryVO.Entry.class);
    }

    private static OcWorkorderTicketEntry covert(WorkorderTicketEntryBO workorderTicketEntryBO) {
        return BeanCopierUtils.copyProperties(workorderTicketEntryBO, OcWorkorderTicketEntry.class);
    }
}
