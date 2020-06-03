package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_workorder_ticket_flow")
public class OcWorkorderTicketFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 工单id
     */
    @Column(name = "ticket_id")
    private Integer ticketId;

    /**
     * (责任人)用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * (责任人)用户名
     */
    private String username;

    /**
     * 工单流程名称
     */
    @Column(name = "flow_name")
    private String flowName;

    /**
     * 审批类型 0:org  1:审批组
     */
    @Column(name = "approval_type")
    private Integer approvalType;

    /**
     * 父流程id
     */
    @Column(name = "flow_parent_id")
    private Integer flowParentId;

    /**
     * 审批组id
     */
    @Column(name = "approval_group_id")
    private Integer approvalGroupId;

    /**
     * 审批状态 1 同意 0 拒绝 -1 未操作
     */
    @Column(name = "approval_status")
    private Integer approvalStatus;

    /**
     * 说明
     */
    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

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
     * @return ticket_id - 工单id
     */
    public Integer getTicketId() {
        return ticketId;
    }

    /**
     * 设置工单id
     *
     * @param ticketId 工单id
     */
    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    /**
     * 获取(责任人)用户id
     *
     * @return user_id - (责任人)用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置(责任人)用户id
     *
     * @param userId (责任人)用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取(责任人)用户名
     *
     * @return username - (责任人)用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置(责任人)用户名
     *
     * @param username (责任人)用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取工单流程名称
     *
     * @return flow_name - 工单流程名称
     */
    public String getFlowName() {
        return flowName;
    }

    /**
     * 设置工单流程名称
     *
     * @param flowName 工单流程名称
     */
    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    /**
     * 获取审批类型 0:org  1:审批组
     *
     * @return approval_type - 审批类型 0:org  1:审批组
     */
    public Integer getApprovalType() {
        return approvalType;
    }

    /**
     * 设置审批类型 0:org  1:审批组
     *
     * @param approvalType 审批类型 0:org  1:审批组
     */
    public void setApprovalType(Integer approvalType) {
        this.approvalType = approvalType;
    }

    /**
     * 获取父流程id
     *
     * @return flow_parent_id - 父流程id
     */
    public Integer getFlowParentId() {
        return flowParentId;
    }

    /**
     * 设置父流程id
     *
     * @param flowParentId 父流程id
     */
    public void setFlowParentId(Integer flowParentId) {
        this.flowParentId = flowParentId;
    }

    /**
     * 获取审批组id
     *
     * @return approval_group_id - 审批组id
     */
    public Integer getApprovalGroupId() {
        return approvalGroupId;
    }

    /**
     * 设置审批组id
     *
     * @param approvalGroupId 审批组id
     */
    public void setApprovalGroupId(Integer approvalGroupId) {
        this.approvalGroupId = approvalGroupId;
    }

    /**
     * 获取审批状态 1 同意 0 拒绝 -1 未操作
     *
     * @return approval_status - 审批状态 1 同意 0 拒绝 -1 未操作
     */
    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    /**
     * 设置审批状态 1 同意 0 拒绝 -1 未操作
     *
     * @param approvalStatus 审批状态 1 同意 0 拒绝 -1 未操作
     */
    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
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
}