package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "auth_role")
public class AuthRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 访问级别
     */
    @Column(name = "access_level")
    private Integer accessLevel;

    /**
     * 角色描述
     */
    private String comment;

    /**
     * 允许工作流申请
     */
    @Column(name = "allow_workorder")
    private Boolean allowWorkorder;

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
     * 获取访问级别
     *
     * @return access_level - 访问级别
     */
    public Integer getAccessLevel() {
        return accessLevel;
    }

    /**
     * 设置访问级别
     *
     * @param accessLevel 访问级别
     */
    public void setAccessLevel(Integer accessLevel) {
        this.accessLevel = accessLevel;
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
     * 获取允许工作流申请
     *
     * @return allow_workorder - 允许工作流申请
     */
    public Boolean getAllowWorkorder() {
        return allowWorkorder;
    }

    /**
     * 设置允许工作流申请
     *
     * @param allowWorkorder 允许工作流申请
     */
    public void setAllowWorkorder(Boolean allowWorkorder) {
        this.allowWorkorder = allowWorkorder;
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