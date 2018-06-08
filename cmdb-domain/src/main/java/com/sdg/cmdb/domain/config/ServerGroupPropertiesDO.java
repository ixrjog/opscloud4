package com.sdg.cmdb.domain.config;

import java.io.Serializable;

/**
 * Created by zxxiao on 2016/10/21.
 */
public class ServerGroupPropertiesDO implements Serializable {
    private static final long serialVersionUID = -6577131273984933361L;

    private long id;

    /**
     * 服务器组Id
     */
    private Long groupId;

    /**
     * 服务器Id
     */
    private Long serverId;

    /**
     * 属性id
     */
    private Long propertyId;

    /**
     * 属性值
     */
    private String propertyValue;

    /**
     * 属性组id
     */
    private Long propertyGroupId;

    private String gmtCreate;

    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public Long getPropertyGroupId() {
        return propertyGroupId;
    }

    public void setPropertyGroupId(Long propertyGroupId) {
        this.propertyGroupId = propertyGroupId;
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
        return "ServerGroupPropertiesDO{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", serverId=" + serverId +
                ", propertyId=" + propertyId +
                ", propertyValue='" + propertyValue + '\'' +
                ", propertyGroupId=" + propertyGroupId +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
