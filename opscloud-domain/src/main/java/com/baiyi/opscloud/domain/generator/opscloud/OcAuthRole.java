package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "access_level")
    private Integer accessLevel;

    /**
     * 角色描述
     */
    private String comment;

    /**
     * 在工单中
     */
    @Column(name = "in_workorder")
    private Integer inWorkorder;

    /**
     * api允许访问的资源路径(用于公共接口2次鉴权)
     */
    @Column(name = "resource_name")
    private String resourceName;

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

    public Integer getAccessLevel() {
        return accessLevel;
    }

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

    public Integer getInWorkorder() {
        return inWorkorder;
    }

    public void setInWorkorder(Integer inWorkorder) {
        this.inWorkorder = inWorkorder;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
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