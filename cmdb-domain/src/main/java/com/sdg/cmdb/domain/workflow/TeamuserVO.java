package com.sdg.cmdb.domain.workflow;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.auth.UserDO;
import lombok.Data;

import java.io.Serializable;

@Data
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
