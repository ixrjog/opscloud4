package com.sdg.cmdb.domain.auth;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/19.
 */
public class ResourceDO implements Serializable {
    private static final long serialVersionUID = 6562546998177350617L;

    private long id;

    private String resourceName;

    private String resourceDesc;

    /*
    是否需要鉴权。0：需要；1：不需要
     */
    private int needAuth;

    private long groupId;

    private String gmtCreate;

    private String gmtModify;

    public ResourceDO() {
    }

    public ResourceDO(ResourceVO resourceVO) {
        this.id = resourceVO.getId();
        this.resourceName = resourceVO.getResourceName();
        this.resourceDesc = resourceVO.getResourceDesc();
        this.needAuth = resourceVO.getNeedAuth();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceDesc() {
        return resourceDesc;
    }

    public void setResourceDesc(String resourceDesc) {
        this.resourceDesc = resourceDesc;
    }

    public int getNeedAuth() {
        return needAuth;
    }

    public void setNeedAuth(int needAuth) {
        this.needAuth = needAuth;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    @Override
    public String toString() {
        return "ResourceDO{" +
                "id=" + id +
                ", resourceName='" + resourceName + '\'' +
                ", resourceDesc='" + resourceDesc + '\'' +
                ", needAuth=" + needAuth +
                ", groupId=" + groupId +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
