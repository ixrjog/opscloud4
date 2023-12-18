package com.baiyi.opscloud.domain.vo.workorder;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.base.ReadableTime;
import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author baiyi
 * @Date 2022/1/10 6:16 PM
 * @Version 1.0
 */
public class WorkOrderTicketVO {

    public interface ITicket {

        Integer getTicketId();

    }

    public interface ITicketEntries extends ITicket {

        String getWorkOrderKey();

        void setTicketEntries(List<Entry> ticketEntries);

    }

    public interface IApprover extends ITicket {

        Boolean getIsApprover();

        void setIsApprover(Boolean isApprover);

    }

    @Builder
    @Data
    @Schema
    public static class TicketView implements WorkOrderVO.IWorkOrder, ITicketEntries, Serializable {
        @Serial
        private static final long serialVersionUID = -5342262347843407536L;
        @Schema(description = "工单")
        private WorkOrderVO.WorkOrder workOrder;
        @Schema(description = "工单票据")
        private Ticket ticket;
        @Schema(description = "工单票据条目")
        private List<Entry> ticketEntries;
        @Schema(description = "工单创建用户")
        private UserVO.User createUser;
        @Schema(description = "工作流")
        private WorkflowVO.WorkflowView workflowView;
        @Schema(description = "工作流审批节点视图")
        private WorkOrderNodeVO.NodeView nodeView;

        @Override
        public Integer getTicketId() {
            if (this.ticket != null) {
                return this.ticket.getId();
            }
            return 0;
        }

        @Override
        public String getWorkOrderKey() {
            if (this.workOrder != null) {
                return this.workOrder.getWorkOrderKey();
            }
            return null;
        }

        @Override
        public Integer getWorkOrderId() {
            if (this.ticket != null) {
                return this.ticket.getWorkOrderId();
            }
            return 0;
        }

        public String getTicketPhase() {
            if (this.ticket != null) {
                return this.ticket.getTicketPhase();
            }
            return "NEW";
        }

        /**
         * Ticket Comment
         *
         * @return
         */
        public String getComment() {
            if (this.ticket != null) {
                return this.ticket.getComment();
            }
            return "";
        }

        public Boolean getIsApprover() {
            if (this.ticket != null) {
                return this.ticket.getIsApprover();
            }
            return false;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Schema
    public static class Ticket extends BaseVO implements WorkOrderVO.IWorkOrder, IApprover, ReadableTime.IAgo, Serializable {

        @Serial
        private static final long serialVersionUID = -3191271933875590264L;

        @Schema(description = "是否为审批人")
        private Boolean isApprover;

        @Override
        public Boolean getIsApprover() {
            return Objects.requireNonNullElse(this.isApprover, false);
        }

        private String ago;

        @Schema(description = "工单")
        private WorkOrderVO.WorkOrder workOrder;

        @Schema(description = "创建（申请）人")
        private UserVO.User createUser;

        private Integer id;
        private Integer workOrderId;
        private Integer userId;
        private String username;
        private Integer nodeId;
        private String ticketPhase;
        private Integer ticketStatus;
        private Boolean isActive;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date startTime;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date endTime;
        private String comment;

        /**
         * 前端选择用
         */
        private Boolean loading;

        @Override
        public Date getAgoTime() {
            return getStartTime();
        }

        @Override
        public Integer getTicketId() {
            return this.id;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    @Schema
    public static class Entry<T> extends BaseVO implements DsInstanceVO.IDsInstance, Serializable {

        @Serial
        private static final long serialVersionUID = 5462899820190005914L;

        private DsInstanceVO.Instance instance;

        private Integer id;
        private Integer workOrderTicketId;
        private String name;
        private String instanceUuid;
        private Integer businessType;
        private Integer businessId;
        @Builder.Default
        private Integer entryStatus = 0;
        private String entryKey;
        private T entry;
        private String role;
        private String comment;
        private String content;
        private String result;

    }

}