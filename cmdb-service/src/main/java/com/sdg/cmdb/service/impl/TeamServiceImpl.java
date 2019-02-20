package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.TeamDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.RoleDO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.workflow.TeamDO;
import com.sdg.cmdb.domain.workflow.TeamVO;
import com.sdg.cmdb.domain.workflow.TeamuserDO;
import com.sdg.cmdb.domain.workflow.TeamuserVO;
import com.sdg.cmdb.service.AuthService;
import com.sdg.cmdb.service.TeamService;
import com.sdg.cmdb.util.SessionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    @Resource
    private TeamDao teamDao;

    @Resource
    private UserDao userDao;

    @Resource
    private AuthService authService;

    @Override
    public TableVO<List<TeamVO>> getTeamPage(String teamName, String teamleaderUsername, int teamType, int page, int length) {
        long size = teamDao.getTeamSize(teamName, teamType, teamleaderUsername);
        List<TeamDO> list = teamDao.getTeamPage(teamName, teamType, teamleaderUsername, page * length, length);
        List<TeamVO> voList = new ArrayList<>();
        for (TeamDO teamDO : list)
            voList.add(getTeam(teamDO.getId()));
        return new TableVO<>(size, voList);
    }

    @Override
    public TeamVO getTeam(long id) {
        TeamDO teamDO = teamDao.getTeam(id);
        UserDO leader = userDao.getUserById(teamDO.getTeamleaderUserId());
        TeamVO teamVO = new TeamVO(teamDO, leader, getTeamuserByTeamId(teamDO.getId()));
        return teamVO;
    }


    /**
     * 获取team成员信息
     *
     * @param id
     * @return
     */
    private List<TeamuserVO> getTeamuserByTeamId(long id) {
        List<TeamuserDO> list = teamDao.queryTeamuserByTeamId(id);
        List<TeamuserVO> voList = new ArrayList<>();
        for (TeamuserDO teamuserDO : list) {
            UserDO userDO = userDao.getUserById(teamuserDO.getUserId());
            if (userDO == null)
                userDO = new UserDO(teamuserDO.getUsername());
            TeamuserVO teamuserVO = new TeamuserVO(teamuserDO, userDO);
            voList.add(teamuserVO);
        }
        return voList;

    }

    @Override
    public BusinessWrapper<Boolean> saveTeam(TeamDO teamDO) {
        try {
            if (teamDO.getId() == 0) {
                teamDao.addTeam(teamDO);
            } else {
                teamDao.updateTeam(teamDO);
            }
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delTeam(long id) {
        try {
            teamDao.delTeam(id);
            List<TeamuserDO> list = teamDao.queryTeamuserByTeamId(id);
            for (TeamuserDO teamuserDO : list)
                teamDao.delTeamuser(teamuserDO.getId());
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> saveTeamuser(TeamuserDO teamuserDO) {
        if(! checkAuth(teamuserDO)) return new BusinessWrapper<Boolean>(true);

        try {
            if (teamuserDO.getId() == 0) {
                teamDao.addTeamuser(teamuserDO);
            } else {
                teamDao.updateTeamuser(teamuserDO);
            }
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delTeamuser(long id) {
        if(! checkAuth(id)) return new BusinessWrapper<Boolean>(true);
        try {
            teamDao.delTeamuser(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    private boolean checkAuth(long teamuserId) {
        TeamuserDO teamuserDO = teamDao.getTeamuser(teamuserId);
        return checkAuth(teamuserDO);
    }

    private boolean checkAuth(TeamuserDO teamuserDO) {
        // 判断是否为管理员操作
        String sessionUsername = SessionUtils.getUsername();
        if (authService.isRole(sessionUsername, RoleDO.roleAdmin)) return true;
        // 判断是否为TL操作
        long teamId = teamuserDO.getTeamId();
        TeamDO teamDO = teamDao.getTeam(teamId);
        long userId = teamDO.getTeamleaderUserId();
        UserDO userDO = userDao.getUserByName(sessionUsername);
        if (userDO.getId() == userId) return true;
        return false;
    }


}
