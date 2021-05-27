package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;

@Table(name = "auth_resource")
public class AuthResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资源组id
     */
    @Column(name = "group_id")
    private Integer groupId;

    /**
     * 资源名称
     */
    @Column(name = "resource_name")
    private String resourceName;

    private String comment;

    /**
     * 是否鉴权
     */
    @Column(name = "need_auth")
    private Boolean needAuth;

    /**
     * 用户界面
     */
    private Boolean ui;

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
     * 获取资源组id
     *
     * @return group_id - 资源组id
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * 设置资源组id
     *
     * @param groupId 资源组id
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * 获取资源名称
     *
     * @return resource_name - 资源名称
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * 设置资源名称
     *
     * @param resourceName 资源名称
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    /**
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 获取是否鉴权
     *
     * @return need_auth - 是否鉴权
     */
    public Boolean getNeedAuth() {
        return needAuth;
    }

    /**
     * 设置是否鉴权
     *
     * @param needAuth 是否鉴权
     */
    public void setNeedAuth(Boolean needAuth) {
        this.needAuth = needAuth;
    }

    /**
     * 获取用户界面
     *
     * @return ui - 用户界面
     */
    public Boolean getUi() {
        return ui;
    }

    /**
     * 设置用户界面
     *
     * @param ui 用户界面
     */
    public void setUi(Boolean ui) {
        this.ui = ui;
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