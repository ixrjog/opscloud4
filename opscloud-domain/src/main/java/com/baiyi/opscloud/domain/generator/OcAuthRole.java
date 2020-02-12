package com.baiyi.opscloud.domain.generator;

import java.util.Date;
import javax.persistence.*;

@Table(name = "oc_auth_role")
public class OcAuthRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 角色描述
     */
    private String comment;

    private Boolean workflow;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
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
     * 获取角色名称
     *
     * @return role_name - 角色名称
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 设置角色名称
     *
     * @param roleName 角色名称
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * 获取角色描述
     *
     * @return comment - 角色描述
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置角色描述
     *
     * @param comment 角色描述
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return workflow
     */
    public Boolean getWorkflow() {
        return workflow;
    }

    /**
     * @param workflow
     */
    public void setWorkflow(Boolean workflow) {
        this.workflow = workflow;
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