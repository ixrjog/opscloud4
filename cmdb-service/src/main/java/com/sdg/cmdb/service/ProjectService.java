package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.projectManagement.ProjectManagementVO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ProjectService {

    TableVO<List<ProjectManagementVO>> queryProjectManagementPage(String projectName,
                                                                  int projectType,
                                                                  int status,
                                                                  String leaderUsername, int page, int length);


    ProjectManagementVO getProjectManagement(long id);

    BusinessWrapper<Boolean> delProjectManagement(long pmId);

    /**
     * 保存指定的PM信息
     *
     * @param pmVO
     * @return
     */
    ProjectManagementVO saveProjectManagement(ProjectManagementVO pmVO);


    BusinessWrapper<Boolean> addUser(long pmId, long userId);

    BusinessWrapper<Boolean> delUser(long id);

    BusinessWrapper<Boolean> addServerGroup(long pmId, long serverGroupId);

    BusinessWrapper<Boolean> delServerGroup(long id);


    /**
     * 心跳详情页
     *
     * @param projectName
     * @param projectType
     * @param projectStatus
     * @param leaderUsername
     * @param page
     * @param length
     * @return
     */
    TableVO<List<ProjectManagementVO>> queryHeartbeatPage(String projectName,
                                                          int projectType, int projectStatus,
                                                          String leaderUsername, int page, int length);

    /**
     * 心跳
     * @param pmId
     * @param status
     * @return
     */
    BusinessWrapper<Boolean> saveHeartbeat(long pmId, int status);

    /**
     * 定时任务
     */
    void task();

}
