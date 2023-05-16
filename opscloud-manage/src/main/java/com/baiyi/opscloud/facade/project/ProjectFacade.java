package com.baiyi.opscloud.facade.project;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.project.ProjectParam;
import com.baiyi.opscloud.domain.vo.project.ProjectResourceVO;
import com.baiyi.opscloud.domain.vo.project.ProjectVO;

/**
 * @Author 修远
 * @Date 2023/5/15 5:44 PM
 * @Since 1.0
 */
public interface ProjectFacade {

    DataTable<ProjectVO.Project> queryProjectPage(ProjectParam.ProjectPageQuery pageQuery);

    ProjectVO.Project getProjectById(Integer id);

    void addProject(ProjectVO.Project project);

    void updateProject(ProjectVO.Project project);

    void deleteProject(Integer id);

    void bindProjectResource(ProjectResourceVO.Resource resource);

    void unbindProjectResource(Integer id);

}
