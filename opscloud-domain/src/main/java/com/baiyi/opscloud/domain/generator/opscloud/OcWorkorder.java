package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_workorder")
public class OcWorkorder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 工单名称
     */
    private String name;

    /**
     * 工单key
     */
    @Column(name = "workorder_key")
    private String workorderKey;

    /**
     * 帮助文档id
     */
    @Column(name = "readme_id")
    private Integer readmeId;

    /**
     * 工单组id
     */
    @Column(name = "workorder_group_id")
    private Integer workorderGroupId;

    /**
     * 审批类型
     */
    @Column(name = "approval_type")
    private Integer approvalType;

    /**
     * 是否需要组织架构审批
     */
    @Column(name = "org_approval")
    private Boolean orgApproval;

    /**
     * 审批组id
     */
    @Column(name = "approval_group_id")
    private Integer approvalGroupId;

    /**
     * 模式 0 自动 1 手动
     */
    @Column(name = "workorder_mode")
    private Integer workorderMode;

    /**
     * 状态 0 正常 1 开发 2 停用
     */
    @Column(name = "workorder_status")
    private Integer workorderStatus;

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
     * 获取工单名称
     *
     * @return name - 工单名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置工单名称
     *
     * @param name 工单名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取工单key
     *
     * @return workorder_key - 工单key
     */
    public String getWorkorderKey() {
        return workorderKey;
    }

    /**
     * 设置工单key
     *
     * @param workorderKey 工单key
     */
    public void setWorkorderKey(String workorderKey) {
        this.workorderKey = workorderKey;
    }

    /**
     * 获取帮助文档id
     *
     * @return readme_id - 帮助文档id
     */
    public Integer getReadmeId() {
        return readmeId;
    }

    /**
     * 设置帮助文档id
     *
     * @param readmeId 帮助文档id
     */
    public void setReadmeId(Integer readmeId) {
        this.readmeId = readmeId;
    }

    /**
     * 获取工单组id
     *
     * @return workorder_group_id - 工单组id
     */
    public Integer getWorkorderGroupId() {
        return workorderGroupId;
    }

    /**
     * 设置工单组id
     *
     * @param workorderGroupId 工单组id
     */
    public void setWorkorderGroupId(Integer workorderGroupId) {
        this.workorderGroupId = workorderGroupId;
    }

    /**
     * 获取审批类型
     *
     * @return approval_type - 审批类型
     */
    public Integer getApprovalType() {
        return approvalType;
    }

    /**
     * 设置审批类型
     *
     * @param approvalType 审批类型
     */
    public void setApprovalType(Integer approvalType) {
        this.approvalType = approvalType;
    }

    /**
     * 获取是否需要组织架构审批
     *
     * @return org_approval - 是否需要组织架构审批
     */
    public Boolean getOrgApproval() {
        return orgApproval;
    }

    /**
     * 设置是否需要组织架构审批
     *
     * @param orgApproval 是否需要组织架构审批
     */
    public void setOrgApproval(Boolean orgApproval) {
        this.orgApproval = orgApproval;
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
     * 获取模式 0 自动 1 手动
     *
     * @return workorder_mode - 模式 0 自动 1 手动
     */
    public Integer getWorkorderMode() {
        return workorderMode;
    }

    /**
     * 设置模式 0 自动 1 手动
     *
     * @param workorderMode 模式 0 自动 1 手动
     */
    public void setWorkorderMode(Integer workorderMode) {
        this.workorderMode = workorderMode;
    }

    /**
     * 获取状态 0 正常 1 开发 2 停用
     *
     * @return workorder_status - 状态 0 正常 1 开发 2 停用
     */
    public Integer getWorkorderStatus() {
        return workorderStatus;
    }

    /**
     * 设置状态 0 正常 1 开发 2 停用
     *
     * @param workorderStatus 状态 0 正常 1 开发 2 停用
     */
    public void setWorkorderStatus(Integer workorderStatus) {
        this.workorderStatus = workorderStatus;
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