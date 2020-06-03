package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_workorder_ticket")
public class OcWorkorderTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 工单id
     */
    @Column(name = "workorder_id")
    private Integer workorderId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 说明
     */
    private String comment;

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
     * 工单状态
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

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 用户详情
     */
    @Column(name = "user_detail")
    private String userDetail;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取工单id
     *
     * @return workorder_id - 工单id
     */
    public Integer getWorkorderId() {
        return workorderId;
    }

    /**
     * 设置工单id
     *
     * @param workorderId 工单id
     */
    public void setWorkorderId(Integer workorderId) {
        this.workorderId = workorderId;
    }

    /**
     * 获取用户id
     *
     * @return user_id - 用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取说明
     *
     * @return comment - 说明
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置说明
     *
     * @param comment 说明
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 获取工单流程id
     *
     * @return flow_id - 工单流程id
     */
    public Integer getFlowId() {
        return flowId;
    }

    /**
     * 设置工单流程id
     *
     * @param flowId 工单流程id
     */
    public void setFlowId(Integer flowId) {
        this.flowId = flowId;
    }

    /**
     * 获取工单阶段
     *
     * @return ticket_phase - 工单阶段
     */
    public String getTicketPhase() {
        return ticketPhase;
    }

    /**
     * 设置工单阶段
     *
     * @param ticketPhase 工单阶段
     */
    public void setTicketPhase(String ticketPhase) {
        this.ticketPhase = ticketPhase;
    }

    /**
     * 获取工单状态
     *
     * @return ticket_status - 工单状态
     */
    public Integer getTicketStatus() {
        return ticketStatus;
    }

    /**
     * 设置工单状态
     *
     * @param ticketStatus 工单状态
     */
    public void setTicketStatus(Integer ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    /**
     * 获取申请开始时间
     *
     * @return start_time - 申请开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置申请开始时间
     *
     * @param startTime 申请开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取结束开始时间
     *
     * @return end_time - 结束开始时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置结束开始时间
     *
     * @param endTime 结束开始时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取用户详情
     *
     * @return user_detail - 用户详情
     */
    public String getUserDetail() {
        return userDetail;
    }

    /**
     * 设置用户详情
     *
     * @param userDetail 用户详情
     */
    public void setUserDetail(String userDetail) {
        this.userDetail = userDetail;
    }
}