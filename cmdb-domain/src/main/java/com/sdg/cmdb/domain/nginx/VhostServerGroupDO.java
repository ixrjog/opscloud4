package com.sdg.cmdb.domain.nginx;

import com.sdg.cmdb.domain.server.ServerGroupDO;

import java.io.Serializable;

public class VhostServerGroupDO implements Serializable {
    private static final long serialVersionUID = 1179967746466481502L;

    public VhostServerGroupDO(){

    }


    public VhostServerGroupDO(long vhostId, ServerGroupDO serverGroupDO){
        this.vhostId = vhostId;
        this.serverGroupId = serverGroupDO.getId();
        this.groupName = serverGroupDO.getName();
    }


    private long id;

    private long vhostId;

    private long serverGroupId;

    private String groupName;

    private String gmtCreate;

    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVhostId() {
        return vhostId;
    }

    public void setVhostId(long vhostId) {
        this.vhostId = vhostId;
    }

    public long getServerGroupId() {
        return serverGroupId;
    }

    public void setServerGroupId(long serverGroupId) {
        this.serverGroupId = serverGroupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
