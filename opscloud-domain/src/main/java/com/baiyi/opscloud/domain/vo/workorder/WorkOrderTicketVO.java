package com.baiyi.opscloud.domain.vo.workorder;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/10 6:16 PM
 * @Version 1.0
 */
public class WorkOrderTicketVO {

    public interface IWorkOrder {

        Integer getWorkOrderId();

        void setWorkOrder(WorkOrderVO.WorkOrder workOrder);
    }

    public interface ITicketEntries {

        Integer getTicketId();

        void setTicketEntries(List<Entry> ticketEntries);
    }

    @Builder
    @Data
    @ApiModel
    public static class TicketView implements IWorkOrder,ITicketEntries, Serializable {
        private static final long serialVersionUID = -5342262347843407536L;
        @ApiModelProperty(value = "工单")
        private WorkOrderVO.WorkOrder workOrder;
        @ApiModelProperty(value = "工单票据")
        private Ticket workOrderTicket;
        @ApiModelProperty(value = "工单票据条目")
        private List<Entry> ticketEntries;
        @ApiModelProperty(value = "工单创建用户")
        private UserVO.User createUser;
        @ApiModelProperty(value = "工作流")
        private WorkflowVO.WorkflowView workflow;

        @Override
        public Integer getTicketId() {
            if (this.workOrderTicket != null)
                return this.workOrderTicket.getId();
            return 0;
        }

        @Override
        public Integer getWorkOrderId() {
            if (this.workOrderTicket != null)
                return this.workOrderTicket.getWorkOrderId();
            return 0;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @ApiModel
    public static class Ticket extends BaseVO implements Serializable {

        private static final long serialVersionUID = -3191271933875590264L;
        private Integer id;
        private Integer workOrderId;
        private Integer userId;
        private String username;
        private Integer flowId;
        private String ticketPhase;
        private Integer ticketStatus;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date startTime;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date endTime;
        private String comment;

    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @ApiModel
    public static class Entry<T> extends BaseVO implements Serializable {

        private static final long serialVersionUID = 5462899820190005914L;
        private Integer id;
        private Integer workOrderTicketId;
        private String name;
        private String instanceUuid;
        private Integer businessType;
        private Integer businessId;
        private Integer entryStatus;
        private String entryKey;
        private T entry;
        private String role;
        private String comment;
        private String content;
        private String result;

    }

}
