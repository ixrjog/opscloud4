package com.sdg.cmdb.domain.todo;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/10/9.
 */
public class TodoConfigDO implements Serializable {
    private static final long serialVersionUID = 5427411491545874232L;

    private long id;

    private String configName;

    private long parentId;

    private int configStatus;

    private long roleId;

    private String gmtCreate;

    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public int getConfigStatus() {
        return configStatus;
    }

    public void setConfigStatus(int configStatus) {
        this.configStatus = configStatus;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
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
        return "TodoConfigDO{" +
                "id=" + id +
                ", configName='" + configName + '\'' +
                ", parentId=" + parentId +
                ", configStatus=" + configStatus +
                ", roleId=" + roleId +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
