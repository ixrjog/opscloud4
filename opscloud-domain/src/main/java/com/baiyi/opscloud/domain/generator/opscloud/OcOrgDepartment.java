package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_org_department")
public class OcOrgDepartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 父部门id,根部门id=1
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 是否隐藏部门
     */
    @Column(name = "dept_hiding")
    private Integer deptHiding;

    /**
     * 部门类型
     */
    @Column(name = "dept_type")
    private Integer deptType;

    @Column(name = "dept_order")
    private Integer deptOrder;

    /**
     * 创建时间
     */
    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 中文说明
     */
    private String comment;

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
     * 获取部门名称
     *
     * @return name - 部门名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置部门名称
     *
     * @param name 部门名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取父部门id,根部门id=1
     *
     * @return parent_id - 父部门id,根部门id=1
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * 设置父部门id,根部门id=1
     *
     * @param parentId 父部门id,根部门id=1
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取是否隐藏部门
     *
     * @return dept_hiding - 是否隐藏部门
     */
    public Integer getDeptHiding() {
        return deptHiding;
    }

    /**
     * 设置是否隐藏部门
     *
     * @param deptHiding 是否隐藏部门
     */
    public void setDeptHiding(Integer deptHiding) {
        this.deptHiding = deptHiding;
    }

    /**
     * 获取部门类型
     *
     * @return dept_type - 部门类型
     */
    public Integer getDeptType() {
        return deptType;
    }

    /**
     * 设置部门类型
     *
     * @param deptType 部门类型
     */
    public void setDeptType(Integer deptType) {
        this.deptType = deptType;
    }

    /**
     * @return dept_order
     */
    public Integer getDeptOrder() {
        return deptOrder;
    }

    /**
     * @param deptOrder
     */
    public void setDeptOrder(Integer deptOrder) {
        this.deptOrder = deptOrder;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取中文说明
     *
     * @return comment - 中文说明
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置中文说明
     *
     * @param comment 中文说明
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}