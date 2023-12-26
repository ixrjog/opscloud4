package com.baiyi.opscloud.workorder.model;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicketEntry;
import com.baiyi.opscloud.domain.notice.INoticeMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/26 3:20 PM
 * @Version 1.0
 */
public class TicketNoticeModel implements INoticeMessage {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApproveNoticeMessage implements Serializable, INoticeMessage {

        @Serial
        private static final long serialVersionUID = 4084295468928250597L;

        /**
         * ### No.${ticketId}
         * > ${workOrderName}
         * <p>
         * 您有**${createUser}**发起的工单需要审批:
         * <% for(ticketEntry in ticketEntities){ %>
         * + ${ticketEntry.name}  ${ticketEntry.comment}
         * <% } %>
         * [审批同意](${apiAgree})     [审批拒绝](${apiReject})
         * <p>
         * [点击查看(仅支持PC)](https://oc.chuanyinet.com/index.html#/workorder)
         */

        private Integer ticketId; // 工单ID
        private String workOrderName;  // 工单名称
        private String createUser; // 工单创建者显示名称
        private String apiAgree; // 同意
        private String apiReject; // 拒绝
        private List<WorkOrderTicketEntry> ticketEntities; // 工单条目

    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EndNoticeMessage implements Serializable, INoticeMessage {

        @Serial
        private static final long serialVersionUID = -4873558376848220544L;

        private Integer ticketId; // 工单ID
        private String workOrderName;  // 工单名称
        private String result;  // 执行结果

    }

}