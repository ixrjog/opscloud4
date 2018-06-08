package com.sdg.cmdb.domain.auth;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zxxiao on 16/9/22.
 */
public class ResourceVO implements Serializable {
    private static final long serialVersionUID = 2765072859958562102L;

    private long id;

    private String resourceName;

    private String resourceDesc;

    private int needAuth;

    private List<ResourceGroupDO> groupDOList;

    private String gmtCreate;

    private String gmtModify;

    public ResourceVO() {
    }

    public ResourceVO(ResourceDO resourceDO, List<ResourceGroupDO> groupDOList) {
        this.id = resourceDO.getId();
        this.resourceName = resourceDO.getResourceName();
        this.resourceDesc = resourceDO.getResourceDesc();
        this.needAuth = resourceDO.getNeedAuth();
        this.groupDOList = groupDOList;
        this.gmtCreate = resourceDO.getGmtCreate();
        this.gmtModify = resourceDO.getGmtModify();
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

    public List<ResourceGroupDO> getGroupDOList() {
        return groupDOList;
    }

    public void setGroupDOList(List<ResourceGroupDO> groupDOList) {
        this.groupDOList = groupDOList;
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
        return "ResourceVO{" +
                "id=" + id +
                ", resourceName='" + resourceName + '\'' +
                ", resourceDesc='" + resourceDesc + '\'' +
                ", needAuth=" + needAuth +
                ", groupDOList=" + groupDOList +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
