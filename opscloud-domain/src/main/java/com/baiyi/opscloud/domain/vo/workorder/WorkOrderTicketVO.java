package com.baiyi.opscloud.domain.vo.workorder;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.base.ShowTime;
import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/10 6:16 PM
 * @Version 1.0
 */
public class WorkOrderTicketVO {

    public interface ITicketEntries {

        Integer getTicketId();

        String getWorkOrderKey();

        void setTicketEntries(List<Entry> ticketEntries);
    }

    @Builder
    @Data
    @ApiModel
    public static class TicketView implements WorkOrderVO.IWorkOrder, ITicketEntries, Serializable {
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
        private WorkflowVO.WorkflowView workflowView;
        @ApiModelProperty(value = "工作流审批节点视图")
        private WorkOrderNodeVO.NodeView nodeView;

        @Override
        public Integer getTicketId() {
            if (this.workOrderTicket != null)
                return this.workOrderTicket.getId();
            return 0;
        }

        @Override
        public String getWorkOrderKey() {
            if (this.workOrder != null)
                return this.workOrder.getWorkOrderKey();
            return null;
        }

        @Override
        public Integer getWorkOrderId() {
            if (this.workOrderTicket != null)
                return this.workOrderTicket.getWorkOrderId();
            return 0;
        }

        /**
         * Ticket Comment
         *
         * @return
         */
        public String getComment() {
            if (this.workOrderTicket != null)
                return this.workOrderTicket.getComment();
            return "";
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @ApiModel
    public static class Ticket extends BaseVO implements WorkOrderVO.IWorkOrder, ShowTime.IAgo, Serializable {

        private static final long serialVersionUID = -3191271933875590264L;

        private String ago;

        @ApiModelProperty(value = "工单")
        private WorkOrderVO.WorkOrder workOrder;

        @ApiModelProperty(value = "创建（申请）人")
        private UserVO.User createUser;

        private Integer id;
        private Integer workOrderId;
        private Integer userId;
        private String username;
        private Integer nodeId;
        private String ticketPhase;
        private Integer ticketStatus;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date startTime;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date endTime;
        private String comment;

        @Override
        public Date getAgoTime() {
            return getStartTime();
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @ApiModel
    public static class Entry<T> extends BaseVO implements DsInstanceVO.IDsInstance, Serializable {

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
