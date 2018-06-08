package com.sdg.cmdb.domain.todo;

import com.sdg.cmdb.domain.auth.RoleDO;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/10/10.
 */
public class TodoConfigVO implements Serializable {
    private static final long serialVersionUID = 6619241630513766191L;

    private long id;

    private String configName;

    private TodoConfigDO parentConfigDO;

    private int configStatus;

    private RoleDO roleDO;

    private String gmtCreate;

    private String gmtModify;

    public TodoConfigVO() {
    }

    public TodoConfigVO(TodoConfigDO configDO, TodoConfigDO parentConfigDO, RoleDO roleDO) {
        this.id = configDO.getId();
        this.configName = configDO.getConfigName();
        this.parentConfigDO = parentConfigDO;
        this.configStatus = configDO.getConfigStatus();
        this.roleDO = roleDO;
        this.gmtCreate = configDO.getGmtCreate();
        this.gmtModify = configDO.getGmtModify();
    }

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

    public TodoConfigDO getParentConfigDO() {
        return parentConfigDO;
    }

    public void setParentConfigDO(TodoConfigDO parentConfigDO) {
        this.parentConfigDO = parentConfigDO;
    }

    public int getConfigStatus() {
        return configStatus;
    }

    public void setConfigStatus(int configStatus) {
        this.configStatus = configStatus;
    }

    public RoleDO getRoleDO() {
        return roleDO;
    }

    public void setRoleDO(RoleDO roleDO) {
        this.roleDO = roleDO;
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
        return "TodoConfigVO{" +
                "id=" + id +
                ", configName='" + configName + '\'' +
                ", parentConfigDO=" + parentConfigDO +
                ", configStatus=" + configStatus +
                ", roleDO=" + roleDO +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
