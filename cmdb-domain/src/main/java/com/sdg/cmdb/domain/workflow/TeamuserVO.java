package com.sdg.cmdb.domain.workflow;

import com.sdg.cmdb.domain.auth.UserDO;

import java.io.Serializable;

public class TeamuserVO extends TeamuserDO implements Serializable {
    private static final long serialVersionUID = -6782378737766312096L;

    private UserDO userDO;

    public TeamuserVO(TeamuserDO teamuserDO,UserDO userDO){
        setId(teamuserDO.getId());
        setTeamId(teamuserDO.getTeamId());
        setUsername(teamuserDO.getUsername());
        setUserId(teamuserDO.getUserId());
        setTeamRole(teamuserDO.getTeamRole());
        this.userDO =userDO;
    }

    public TeamuserVO(){

    }

    public UserDO getUserDO() {
        return userDO;
    }

    public void setUserDO(UserDO userDO) {
        this.userDO = userDO;
    }
}
