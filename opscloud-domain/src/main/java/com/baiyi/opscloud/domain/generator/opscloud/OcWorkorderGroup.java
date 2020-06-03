package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_workorder_group")
public class OcWorkorderGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 工单组名称
     */
    private String name;

    /**
     * 工单组类型
     */
    @Column(name = "workorder_type")
    private Integer workorderType;

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
     * 获取工单组名称
     *
     * @return name - 工单组名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置工单组名称
     *
     * @param name 工单组名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取工单组类型
     *
     * @return workorder_type - 工单组类型
     */
    public Integer getWorkorderType() {
        return workorderType;
    }

    /**
     * 设置工单组类型
     *
     * @param workorderType 工单组类型
     */
    public void setWorkorderType(Integer workorderType) {
        this.workorderType = workorderType;
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