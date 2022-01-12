package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "work_order_ticket")
public class WorkOrderTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 工单ID
     */
    @Column(name = "work_order_id")
    private Integer workOrderId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 工单流程id
     */
    @Column(name = "flow_id")
    private Integer flowId;

    /**
     * 工单阶段
     */
    @Column(name = "ticket_phase")
    private String ticketPhase;

    /**
     * 工单状态 0 正常  1 结束（成功） 2结束（失败）
     */
    @Column(name = "ticket_status")
    private Integer ticketStatus;

    /**
     * 申请开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 结束开始时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 说明
     */
    private String comment;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}