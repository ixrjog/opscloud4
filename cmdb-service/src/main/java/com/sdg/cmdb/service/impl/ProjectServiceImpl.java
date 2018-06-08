package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.ProjectDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.projectManagement.*;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.service.EmailService;
import com.sdg.cmdb.service.ProjectService;
import com.sdg.cmdb.util.SessionUtils;
import com.sdg.cmdb.util.TimeUtils;
import com.sdg.cmdb.util.TimeViewUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectDao projectDao;

    @Resource
    private UserDao userDao;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private EmailService emailService;

    @Override
    public TableVO<List<ProjectManagementVO>> queryProjectManagementPage(String projectName,
                                                                         int projectType,
                                                                         int status,
                                                                         String leaderUsername, int page, int length) {
        long size = projectDao.getProjectManagementSize(projectName, projectType, status, leaderUsername);
        List<ProjectManagementDO> list = projectDao.getProjectManagementPage(projectName, projectType, status, leaderUsername, page * length, length);
        List<ProjectManagementVO> voList = new ArrayList<>();
        for (ProjectManagementDO projectManagementDO : list) {
            ProjectManagementVO pmVO = new ProjectManagementVO(projectManagementDO);
            invokeProjectManagementVO(pmVO);
            //invokeLeaderUser(pmVO);
            //invokeProjectProperty(pmVO);
            voList.add(pmVO);
        }
        return new TableVO<>(size, voList);
    }


    @Override
    public TableVO<List<ProjectManagementVO>> queryHeartbeatPage(String projectName,
                                                                 int projectType, int status,
                                                                 String leaderUsername, int page, int length) {
        String username = SessionUtils.getUsername();
        UserDO userDO = userDao.getUserByName(username);
        long size = projectDao.getProjectHeartbeatSize(userDO.getId(), projectName, projectType, status, leaderUsername);
        List<ProjectManagementDO> list = projectDao.getProjectHeartbeatPage(userDO.getId(), projectName, projectType, status, leaderUsername, page * length, length);
        List<ProjectManagementVO> voList = new ArrayList<>();
        for (ProjectManagementDO projectManagementDO : list) {
            ProjectManagementVO pmVO = new ProjectManagementVO(projectManagementDO);
            invokeProjectManagementVO(pmVO);
            //invokeLeaderUser(pmVO);
            //invokeProjectProperty(pmVO);
            voList.add(pmVO);
        }
        return new TableVO<>(size, voList);
    }

    @Override
    public ProjectManagementVO getProjectManagement(long id) {
        ProjectManagementDO pmDO = projectDao.getProjectManagementById(id);
        ProjectManagementVO pmVO = new ProjectManagementVO(pmDO);
        invokeProjectProperty(pmVO);
        return pmVO;
    }

    @Override
    public  BusinessWrapper<Boolean> delProjectManagement(long pmId){
        try{
            List<ProjectPropertyDO> list = projectDao.getProjectPropertyByPmIdAndType(pmId,-1);
            for(ProjectPropertyDO ppDO:list)
                projectDao.delProjectProperty(ppDO.getId());
            projectDao.delProjectManagement(pmId);
            return new  BusinessWrapper<Boolean>(true);
        }catch (Exception e){
            e.printStackTrace();
            return new  BusinessWrapper<Boolean>(false);
        }
    }


    private void invokeProjectManagementVO(ProjectManagementVO pmVO) {
        invokeLeaderUser(pmVO);
        invokeProjectProperty(pmVO);
        invokeHeartbeat(pmVO);
        invokeTimeView(pmVO);
    }

    private void invokeLeaderUser(ProjectManagementVO pmVO) {
        UserDO userDO = userDao.getUserById(pmVO.getLeaderUserId());
        if (userDO == null)
            userDO = new UserDO(pmVO.getLeaderUsername());
        pmVO.setLeaderUser(userDO);
    }

    private void invokeProjectProperty(ProjectManagementVO pmVO) {
        List<ProjectPropertyDO> userList = projectDao.getProjectPropertyByPmIdAndType(pmVO.getId(), ProjectPropertyDO.PropertyTypeEnum.user.getCode());
        List<ProjectPropertyVO> users = new ArrayList<>();
        for (ProjectPropertyDO ppDO : userList) {
            UserDO userDO = userDao.getUserById(ppDO.getPropertyValue());
            if (userDO != null)
                users.add(new ProjectPropertyVO(ppDO, userDO));
        }

        List<ProjectPropertyDO> serveGroupList = projectDao.getProjectPropertyByPmIdAndType(pmVO.getId(), ProjectPropertyDO.PropertyTypeEnum.serverGroup.getCode());
        List<ProjectPropertyVO> serverGroups = new ArrayList<>();
        for (ProjectPropertyDO ppDO : serveGroupList) {
            ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupById(ppDO.getPropertyValue());
            if (serverGroupDO != null)
                serverGroups.add(new ProjectPropertyVO(ppDO, serverGroupDO));
        }
        pmVO.setUsers(users);
        pmVO.setServerGroups(serverGroups);
    }

    private void invokeHeartbeat(ProjectManagementVO pmVO) {
        List<ProjectHeartbeatDO> heartbeatDOList = projectDao.getProjectHeartbeatByPmId(pmVO.getId(), 5);
        List<ProjectHeartbeatVO> heartbeats = new ArrayList<>();
        for (ProjectHeartbeatDO projectHeartbeatDO : heartbeatDOList) {
            UserDO userDO = userDao.getUserById(projectHeartbeatDO.getUserId());
            if (userDO == null) {
                userDO = new UserDO(projectHeartbeatDO.getUsername());
            }
            ProjectHeartbeatVO projectHeartbeatVO = new ProjectHeartbeatVO(projectHeartbeatDO, userDO);
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
                Date d = format.parse(projectHeartbeatDO.getGmtCreate());
                projectHeartbeatVO.setTimeView(TimeViewUtils.format(d));
            } catch (Exception e) {
                e.printStackTrace();
            }
            heartbeats.add(projectHeartbeatVO);
        }
        pmVO.setHeartbeats(heartbeats);
        // 更新项目状态并插入心跳时间差
        updateHeartbeatStatus(pmVO);
    }

    /**
     * 计算心跳时间差
     *
     * @param pmVO
     */
    private void invokeHeartbeatDateDiff(ProjectManagementVO pmVO) {
        List<ProjectHeartbeatDO> heartbeatDOList = projectDao.getProjectHeartbeatByPmId(pmVO.getId(), 1);
        if (heartbeatDOList.size() == 0) {
            pmVO.setNeedHeartbeat(true);
        }
        for (ProjectHeartbeatDO projectHeartbeatDO : heartbeatDOList) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
                Date date = format.parse(projectHeartbeatDO.getGmtCreate());
                int dateDiff = TimeUtils.calculateDateDiff4Day(date, new Date());
                pmVO.setDateDiff(dateDiff);
                if (dateDiff >= (pmVO.getTtl() - 5) && pmVO.getStatus() != ProjectManagementDO.ProjectStatusEnum.death.getCode()) {
                    pmVO.setNeedHeartbeat(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
                pmVO.setDateDiff(0);
            }
        }
    }

    /**
     * 更新项目状态
     *
     * @param pmVO
     * @return
     */
    public boolean updateHeartbeatStatus(ProjectManagementVO pmVO) {
        // 死亡的项目直接返回
        if (pmVO.getStatus() == ProjectManagementDO.ProjectStatusEnum.death.getCode())
            return false;
        invokeHeartbeatDateDiff(pmVO);
        //  丢失状态
        if (pmVO.getDateDiff() >= (pmVO.getTtl() + 5)) {
            updateHeartbeatStatus(pmVO, ProjectManagementDO.ProjectStatusEnum.lose.getCode());
            return true;
        }
        //  可更新状态
        if (pmVO.getDateDiff() == -1 || pmVO.getDateDiff() >= (pmVO.getTtl() - 5)) {
            updateHeartbeatStatus(pmVO, ProjectManagementDO.ProjectStatusEnum.renew.getCode());
            return true;
        }
        //  正常状态
        if (pmVO.getDateDiff() < (pmVO.getTtl() - 5)) {
            updateHeartbeatStatus(pmVO, ProjectManagementDO.ProjectStatusEnum.normal.getCode());
            return true;
        }
        return false;
    }


    private void updateHeartbeatStatus(ProjectManagementVO pmVO, int status) {
        if (status != pmVO.getStatus()) {
            pmVO.setStatus(status);
            projectDao.updateProjectManagementStatus(pmVO);
        }
    }


    private void invokeTimeView(ProjectManagementVO pmVO) {
        if (StringUtils.isEmpty(pmVO.getBeginTime())) return;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
            Date beginDate = format.parse(pmVO.getBeginTime());
            SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
            String dateString = format2.format(beginDate);
            pmVO.setTimeView(dateString + "<" + TimeViewUtils.format(beginDate) + ">");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ProjectManagementVO saveProjectManagement(ProjectManagementVO pmVO) {
        ProjectManagementDO pmDO = new ProjectManagementDO(pmVO);
        try {
            if (pmDO.getId() == 0) {
                projectDao.addProjectManagement(pmDO);
            } else {
                projectDao.updateProjectManagement(pmDO);
            }
            return getProjectManagement(pmDO.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return new ProjectManagementVO();
        }
    }

    @Override
    public BusinessWrapper<Boolean> addUser(long pmId, long userId) {
        // 不添加项目负责人为用户
        ProjectManagementDO pmDO = projectDao.getProjectManagementById(pmId);
        if (pmDO.getLeaderUserId() == userId) return new BusinessWrapper<Boolean>(false);

        ProjectPropertyDO ppDO = new ProjectPropertyDO(pmId, ProjectPropertyDO.PropertyTypeEnum.user.getCode(), userId);
        try {
            projectDao.addProjectProperty(ppDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delUser(long id) {
        try {
            projectDao.delProjectProperty(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> addServerGroup(long pmId, long serverGroupId) {
        ProjectPropertyDO ppDO = new ProjectPropertyDO(pmId, ProjectPropertyDO.PropertyTypeEnum.serverGroup.getCode(), serverGroupId);
        try {
            projectDao.addProjectProperty(ppDO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delServerGroup(long id) {
        try {
            projectDao.delProjectProperty(id);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }


    @Override
    public BusinessWrapper<Boolean> saveHeartbeat(long pmId, int status) {
        ProjectManagementDO pmDO = projectDao.getProjectManagementById(pmId);
        UserDO userDO = userDao.getUserByName(SessionUtils.getUsername());
        ProjectHeartbeatDO phDO = new ProjectHeartbeatDO(pmDO, userDO, status);
        ProjectManagementVO pmVO = new ProjectManagementVO(pmDO, status);
        try {
            projectDao.addProjectHeartbeat(phDO);
            projectDao.updateProjectManagementStatus(pmVO);
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            return new BusinessWrapper<Boolean>(false);
        }
    }

    @Override
    public void task() {
        HashMap<String, List<ProjectManagementVO>> map = new HashMap<>();
        List<ProjectManagementDO> list = projectDao.getProjectManagementAll();
        for (ProjectManagementDO pmDO : list) {
            ProjectManagementVO pmVO = new ProjectManagementVO(pmDO);
            updateHeartbeatStatus(pmVO);
            List<UserDO> users = acqUsers(pmVO);
            setMap(map, users, pmVO);
            sendMsg(map);
        }
    }

    private void sendMsg(HashMap<String, List<ProjectManagementVO>> map) {
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String username = (String) entry.getKey();
            List<ProjectManagementVO> list = (List<ProjectManagementVO>) entry.getValue();
            UserDO userDO = userDao.getUserByName(username);
            emailService.doSendProjectHeartbeatNotify(userDO, list);
        }
    }


    private void setMap(HashMap<String, List<ProjectManagementVO>> map, List<UserDO> users, ProjectManagementVO pmVO) {
        for (UserDO userDO : users) {
            String username = userDO.getUsername();
            List<ProjectManagementVO> list = new ArrayList<>();
            if (map.containsKey(username)) {
                list = map.get(userDO.getUsername());
            }
            list.add(pmVO);
            map.put(username, list);
        }
    }

    /**
     * 获取ProjectManagementVO中的所有用户（包括项目负责人）
     *
     * @param pmVO
     * @return
     */
    private List<UserDO> acqUsers(ProjectManagementVO pmVO) {
        List<UserDO> users = new ArrayList<>();
        users.add(userDao.getUserById(pmVO.getLeaderUserId()));
        List<ProjectPropertyDO> ppDOList = projectDao.getProjectPropertyByPmIdAndType(pmVO.getId(), ProjectPropertyDO.PropertyTypeEnum.user.getCode());
        for (ProjectPropertyDO ppDO : ppDOList) {
            users.add(userDao.getUserById(ppDO.getPropertyValue()));
        }
        return users;
    }

}
