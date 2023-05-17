package com.baiyi.opscloud.facade.project;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.project.ProjectParam;
import com.baiyi.opscloud.domain.param.project.ProjectResourceParam;
import com.baiyi.opscloud.domain.vo.project.ProjectVO;

/**
 * @Author 修远
 * @Date 2023/5/15 5:44 PM
 * @Since 1.0
 */
public interface ProjectFacade extends IProjectResource {

    DataTable<ProjectVO.Project> queryProjectPage(ProjectParam.ProjectPageQuery pageQuery);

    ProjectVO.Project getProjectById(Integer id);

    void addProject(ProjectParam.AddProject project);

    void updateProject(ProjectParam.UpdateProject project);

    void deleteProject(Integer id);

    void bindResource(ProjectResourceParam.Resource resource);

    void unbindResource(Integer id);

}
