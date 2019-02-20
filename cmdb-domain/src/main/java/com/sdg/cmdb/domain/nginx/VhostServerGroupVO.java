package com.sdg.cmdb.domain.nginx;

import com.sdg.cmdb.domain.server.ServerGroupDO;

import java.io.Serializable;

public class VhostServerGroupVO extends VhostServerGroupDO implements Serializable {
    private static final long serialVersionUID = -3404774307802371511L;

    private ServerGroupDO serverGroupDO;

    /**
     *     private long id;

     private long vhostId;

     private long serverGroupId;

     private String groupName;

     private String gmtCreate;

     private String gmtModify;
     */

    public VhostServerGroupVO(VhostServerGroupDO vhostServerGroupDO,ServerGroupDO serverGroupDO){
        setId(vhostServerGroupDO.getId());
        setVhostId(vhostServerGroupDO.getVhostId());
        setServerGroupId(vhostServerGroupDO.getServerGroupId());
        setGroupName(vhostServerGroupDO.getGroupName());
        setGmtCreate(vhostServerGroupDO.getGmtCreate());
        setGmtModify(vhostServerGroupDO.getGmtModify());
        this.serverGroupDO = serverGroupDO;

    }

    public VhostServerGroupVO(){

    }

    public ServerGroupDO getServerGroupDO() {
        return serverGroupDO;
    }

    public void setServerGroupDO(ServerGroupDO serverGroupDO) {
        this.serverGroupDO = serverGroupDO;
    }
}
