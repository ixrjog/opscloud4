package com.sdg.cmdb.domain.workflow;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.auth.UserDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TeamVO extends TeamDO implements Serializable {
    private static final long serialVersionUID = 7348364045181679481L;

    private UserDO leader;

    private List<TeamuserVO> teamusers;

    public TeamVO(TeamDO teamDO, UserDO leader, List<TeamuserVO> teamusers) {
        setId(teamDO.getId());
        setTeamName(teamDO.getTeamName());
        setContent(teamDO.getContent());
        setTeamType(teamDO.getTeamType());
        setTeamleaderUserId(teamDO.getTeamleaderUserId());
        setTeamleaderUsername(teamDO.getTeamleaderUsername());
        setGmtCreate(teamDO.getGmtCreate());
        setGmtModify(teamDO.getGmtModify());
        this.leader = leader;
        this.teamusers = teamusers;
    }

    public TeamVO() {

    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
