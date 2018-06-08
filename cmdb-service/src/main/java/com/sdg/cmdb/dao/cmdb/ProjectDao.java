package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.projectManagement.ProjectHeartbeatDO;
import com.sdg.cmdb.domain.projectManagement.ProjectManagementDO;
import com.sdg.cmdb.domain.projectManagement.ProjectManagementVO;
import com.sdg.cmdb.domain.projectManagement.ProjectPropertyDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ProjectDao {

    /**
     * 获取项目管理数目
     *
     * @param projectName
     * @param projectType
     * @param leaderUsername
     * @return
     */
    long getProjectManagementSize(
            @Param("projectName") String projectName,
            @Param("projectType") int projectType,
            @Param("status") int status,
            @Param("leaderUsername") String leaderUsername);


    /**
     * 获取项目管理详情
     *
     * @param projectName
     * @param projectType
     * @param leaderUsername
     * @param pageStart
     * @param length
     * @return
     */
    List<ProjectManagementDO> getProjectManagementPage(
            @Param("projectName") String projectName,
            @Param("projectType") int projectType,
            @Param("status") int status,
            @Param("leaderUsername") String leaderUsername,
            @Param("pageStart") long pageStart, @Param("length") int length);

    ProjectManagementDO getProjectManagementById( @Param("id") long id);

    int addProjectManagement(ProjectManagementDO pmDO);

    int updateProjectManagement(ProjectManagementDO pmDO);

    int updateProjectManagementStatus(ProjectManagementVO pmVO);

    int delProjectManagement(@Param("id") long id);

    /**
     * 按pmId&type查询项目管理属性
     *
     * @param pmId
     * @param propertyType
     * @return
     */
    List<ProjectPropertyDO> getProjectPropertyByPmIdAndType(@Param("pmId") long pmId, @Param("propertyType") int propertyType);

    int addProjectProperty(ProjectPropertyDO projectPropertyDO);

    int delProjectProperty(@Param("id") long id);

    List<ProjectHeartbeatDO> getProjectHeartbeatByPmId(@Param("pmId") long pmId,@Param("cnt") long cnt);

    int addProjectHeartbeat(ProjectHeartbeatDO projectHeartbeatDO);

    long getProjectHeartbeatSize(
            @Param("userId") long userId,
            @Param("projectName") String projectName,
            @Param("projectType") int projectType,
            @Param("status") int status,
            @Param("leaderUsername") String leaderUsername);

    List<ProjectManagementDO> getProjectHeartbeatPage(
            @Param("userId") long userId,
            @Param("projectName") String projectName,
            @Param("projectType") int projectType,
            @Param("status") int status,
            @Param("leaderUsername") String leaderUsername,
            @Param("pageStart") long pageStart, @Param("length") int length);

    /**
     * 查询所有非终止的项目
     * @return
     */
    List<ProjectManagementDO> getProjectManagementAll();

}
