package com.sdg.cmdb.domain.projectManagement;

import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;

import java.io.Serializable;

public class ProjectPropertyVO implements Serializable {

    private long id;

    // ProjectManagementDO id
    private long pmId;

    // 0 userId
    // 1 serverGroupId
    private int propertyType;

    private long propertyValue;

    private UserDO userDO;


    private ServerGroupDO serverGroupDO;


    public ProjectPropertyVO() {

    }

    public ProjectPropertyVO(ProjectPropertyDO projectPropertyDO, UserDO userDO) {
        this.id = projectPropertyDO.getId();
        this.pmId = projectPropertyDO.getPmId();
        this.propertyType = projectPropertyDO.getPropertyType();
        this.propertyValue = projectPropertyDO.getPropertyValue();
        this.userDO = userDO;
    }

    public ProjectPropertyVO(ProjectPropertyDO projectPropertyDO, ServerGroupDO serverGroupDO) {
        this.id = projectPropertyDO.getId();
        this.pmId = projectPropertyDO.getPmId();
        this.propertyType = projectPropertyDO.getPropertyType();
        this.propertyValue = projectPropertyDO.getPropertyValue();
        this.serverGroupDO = serverGroupDO;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPmId() {
        return pmId;
    }

    public void setPmId(long pmId) {
        this.pmId = pmId;
    }

    public int getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(int propertyType) {
        this.propertyType = propertyType;
    }

    public long getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(long propertyValue) {
        this.propertyValue = propertyValue;
    }

    public UserDO getUserDO() {
        return userDO;
    }

    public void setUserDO(UserDO userDO) {
        this.userDO = userDO;
    }

    public ServerGroupDO getServerGroupDO() {
        return serverGroupDO;
    }

    public void setServerGroupDO(ServerGroupDO serverGroupDO) {
        this.serverGroupDO = serverGroupDO;
    }
}
