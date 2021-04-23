package com.baiyi.opscloud.bo.workorder;

import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicketEntry;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/18 3:54 下午
 * @Since 1.0
 */
public class WorkorderTicketFlowMsgBO {

    @Data
    @Builder
    public static class DingtalkMsg {
        private Integer ticketId;
        private String workorderName;
        private String displayName;
        private List<OcWorkorderTicketEntry> ticketEntryList;
    }
}
