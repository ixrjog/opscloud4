package com.sdg.cmdb.domain.auth;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/8/18.
 */
public class CiUserDO implements Serializable {
    private static final long serialVersionUID = -6446462809771256606L;

    private long id;


    private long userId;

    // ci usergroup
    private long usergroupId;

    private String gmtCreate;

    private String gmtModify;

    public CiUserDO(){

    }

    public CiUserDO(long userId, long usergroupId) {
        this.userId = userId;
        this.usergroupId = usergroupId;
    }


    @Override
    public String toString() {
        return "CiUserDO{" +
                "id=" + id +
                ", userId=" + userId +
                ", usergroupId=" + usergroupId +
                ", gmtModify='" + gmtModify + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
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
