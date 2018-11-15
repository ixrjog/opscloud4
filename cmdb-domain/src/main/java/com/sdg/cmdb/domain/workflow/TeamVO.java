package com.sdg.cmdb.domain.workflow;

import com.sdg.cmdb.domain.auth.UserDO;

import java.io.Serializable;
import java.util.List;

public class TeamVO extends TeamDO implements Serializable {
    private static final long serialVersionUID = 7348364045181679481L;

    private UserDO leader;

    private List<TeamuserVO> teamusers;

    public TeamVO(TeamDO teamDO,List<TeamuserVO> teamusers) {
        setId(teamDO.getId());
        setTeamName(teamDO.getTeamName());
        setContent(teamDO.getContent());
        setTeamType(teamDO.getTeamType());
        setTeamleaderUserId(teamDO.getTeamleaderUserId());
        setTeamleaderUsername(teamDO.getTeamleaderUsername());
        setGmtCreate(teamDO.getGmtCreate());
        setGmtModify(teamDO.getGmtModify());
    }

    public TeamVO() {

    }

    public UserDO getLeader() {
        return leader;
    }

    public void setLeader(UserDO leader) {
        this.leader = leader;
    }

    public List<TeamuserVO> getTeamusers() {
        return teamusers;
    }

    public void setTeamusers(List<TeamuserVO> teamusers) {
        this.teamusers = teamusers;
    }
}
