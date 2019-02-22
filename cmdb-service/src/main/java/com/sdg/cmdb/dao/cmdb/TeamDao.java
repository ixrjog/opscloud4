package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.workflow.TeamDO;
import com.sdg.cmdb.domain.workflow.TeamuserDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TeamDao {

    long getTeamSize(
            @Param("teamName") String teamName,
            @Param("teamType") int teamType,
            @Param("teamleaderUsername") String teamleaderUsername);

    List<TeamDO> getTeamPage(
            @Param("teamName") String teamName,
            @Param("teamType") int teamType,
            @Param("teamleaderUsername") String teamleaderUsername,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    int updateTeam(TeamDO teamDO);

    int delTeam(@Param("id") long id);

    int addTeam(TeamDO teamDO);

    TeamDO getTeam(@Param("id") long id);

    TeamDO queryTeamByLeaderUserId(@Param("teamleaderUserId") long teamleaderUserId);


    List<TeamuserDO> queryTeamuserByTeamId(
            @Param("teamId") long teamId);

    TeamuserDO getTeamuser(@Param("id") long id);

    TeamuserDO queryTeamuserByUserId(@Param("userId") long userId);

    int addTeamuser(TeamuserDO teamuserDO);

    int updateTeamuser(TeamuserDO teamuserDO);

    int delTeamuser(@Param("id") long id);

}
