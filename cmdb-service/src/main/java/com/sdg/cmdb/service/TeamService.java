package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.workflow.TeamDO;
import com.sdg.cmdb.domain.workflow.TeamVO;
import com.sdg.cmdb.domain.workflow.TeamuserDO;

import java.util.List;

public interface TeamService {

    TableVO<List<TeamVO>> getTeamPage(String teamName, String teamleaderUsername, int teamType, int page, int length);

    TeamVO getTeam(long id);

    BusinessWrapper<Boolean> saveTeam(TeamDO teamDO);

    BusinessWrapper<Boolean> delTeam(long id);


    BusinessWrapper<Boolean> saveTeamuser(TeamuserDO teamuserDO);

    BusinessWrapper<Boolean> delTeamuser(long id);


}
