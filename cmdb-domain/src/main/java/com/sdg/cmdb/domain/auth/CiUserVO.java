package com.sdg.cmdb.domain.auth;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/8/21.
 */
public class CiUserVO implements Serializable {
    private static final long serialVersionUID = -5713152787698185954L;


    private long id;


    private long userId;

    // ci usergroup
    private long usergroupId;

    private String gmtCreate;

    private String gmtModify;


    private String serverGroupName;

    private int groupType;

    private CiUserGroupDO ciUserGroupDO;

    public CiUserVO(){

    }

    public CiUserVO(CiUserDO ciUserDO, CiUserGroupDO ciUserGroupDO) {
        this.id=ciUserDO.getId();
        this.userId=ciUserDO.getUserId();
        this.usergroupId=ciUserDO.getUsergroupId();
        this.gmtModify=ciUserDO.getGmtModify();
        this.gmtCreate=ciUserDO.getGmtCreate();
        this.ciUserGroupDO = ciUserGroupDO;
    }


    public String getServerGroupName() {
        return serverGroupName;
    }

    public void setServerGroupName(String serverGroupName) {
        this.serverGroupName = serverGroupName;
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public CiUserGroupDO getCiUserGroupDO() {
        return ciUserGroupDO;
    }

    public void setCiUserGroupDO(CiUserGroupDO ciUserGroupDO) {
        this.ciUserGroupDO = ciUserGroupDO;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUsergroupId() {
        return usergroupId;
    }

    public void setUsergroupId(long usergroupId) {
        this.usergroupId = usergroupId;
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
}
