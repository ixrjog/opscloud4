package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_workorder_approval_group")
public class OcWorkorderApprovalGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 工单名称
     */
    private String name;

    /**
     * 流程类型
     */
    @Column(name = "flow_type")
    private Integer flowType;

    @Column(name = "group_type")
    private Integer groupType;

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
     * 获取流程类型
     *
     * @return flow_type - 流程类型
     */
    public Integer getFlowType() {
        return flowType;
    }

    /**
     * 设置流程类型
     *
     * @param flowType 流程类型
     */
    public void setFlowType(Integer flowType) {
        this.flowType = flowType;
    }

    /**
     * @return group_type
     */
    public Integer getGroupType() {
        return groupType;
    }

    /**
     * @param groupType
     */
    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
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