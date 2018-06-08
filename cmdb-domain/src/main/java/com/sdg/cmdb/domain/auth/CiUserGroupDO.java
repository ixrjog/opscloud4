package com.sdg.cmdb.domain.auth;

import com.sdg.cmdb.domain.server.ServerGroupDO;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/8/17.
 */
public class CiUserGroupDO implements Serializable {
    private static final long serialVersionUID = 6454648940921876715L;

    private long id;

    private String groupName;

    private String content;

    private long serverGroupId;

    private int envType;

    private String gmtCreate;

    private String gmtModify;

    public CiUserGroupDO() {

    }

    public CiUserGroupDO(CiUserGroupVO ciUserGroupVO){
        this.id=ciUserGroupVO.getId();
        this.groupName = ciUserGroupVO.getGroupName();
        this.content = ciUserGroupVO.getContent();
        this.serverGroupId = ciUserGroupVO.getServerGroupDO().getId();
        this.envType = ciUserGroupVO.getEnvType();
    }


    public CiUserGroupDO(String groupName, ServerGroupDO serverGroupDO, int envType) {
        this.groupName = groupName;
        this.serverGroupId = serverGroupDO.getId();
        this.envType = envType;
    }

    public CiUserGroupDO(String groupName, int envType) {
        this.groupName = groupName;
        this.envType = envType;
    }

    @Override
    public String toString() {
        return "CiUserGroupDO{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", content='" + content + '\'' +
                ", serverGroupId=" + serverGroupId +
                ", envType=" + envType +
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getServerGroupId() {
        return serverGroupId;
    }

    public void setServerGroupId(long serverGroupId) {
        this.serverGroupId = serverGroupId;
    }

    public int getEnvType() {
        return envType;
    }

    public void setEnvType(int envType) {
        this.envType = envType;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
