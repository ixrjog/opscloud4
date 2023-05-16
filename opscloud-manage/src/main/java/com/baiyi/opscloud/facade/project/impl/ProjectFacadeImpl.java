package com.baiyi.opscloud.facade.project.impl;

import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.FunctionUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Project;
import com.baiyi.opscloud.domain.generator.opscloud.ProjectResource;
import com.baiyi.opscloud.domain.param.project.ProjectParam;
import com.baiyi.opscloud.domain.vo.project.ProjectResourceVO;
import com.baiyi.opscloud.domain.vo.project.ProjectVO;
import com.baiyi.opscloud.facade.project.ProjectFacade;
import com.baiyi.opscloud.service.project.ProjectResourceService;
import com.baiyi.opscloud.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @Author 修远
 * @Date 2023/5/15 5:44 PM
 * @Since 1.0
 */

@Component
@RequiredArgsConstructor
@BusinessType(BusinessTypeEnum.PROJECT)
public class ProjectFacadeImpl implements ProjectFacade {

    private final ProjectService projectService;

    private final ProjectResourceService projectResourceService;

    @Override
    public DataTable<ProjectVO.Project> queryProjectPage(ProjectParam.ProjectPageQuery pageQuery) {
        DataTable<Project> dataTable = projectService.queryPageByParam(pageQuery);
        return null;
    }

    @Override
    public ProjectVO.Project getProjectById(Integer id) {
        Project project = projectService.getById(id);
        return null;
    }

    @Override
    public void addProject(ProjectVO.Project project) {
        FunctionUtil.isTure(projectService.getByKey(project.getProjectKey()) != null)
                .throwBaseException(new OCException(ErrorEnum.PROJECT_ALREADY_EXIST));
        Project newProject = BeanCopierUtil.copyProperties(project, Project.class);
        FunctionUtil.trueFunction(StringUtils.isNotBlank(project.getProjectKey()))
                .trueHandle(() ->
                        newProject.setProjectKey(project.getProjectKey().replaceAll(" ", "").toUpperCase()));
        projectService.add(newProject);
    }

    @Override
    public void updateProject(ProjectVO.Project project) {
        FunctionUtil.isTure(projectService.getByKey(project.getProjectKey()) != null)
                .throwBaseException(new OCException(ErrorEnum.PROJECT_NOT_EXIST));
        projectService.update(BeanCopierUtil.copyProperties(project, Project.class));
    }

    @Override
    public void deleteProject(Integer id) {
        FunctionUtil.isTure(!CollectionUtils.isEmpty(projectResourceService.queryByApplication(id)))
                .throwBaseException(new OCException(ErrorEnum.APPLICATION_RES_IS_NOT_EMPTY));
        projectService.deleteById(id);
    }

    @Override
    public void bindProjectResource(ProjectResourceVO.Resource resource) {
        FunctionUtil.isTure(projectResourceService.getByUniqueKey(resource.getProjectId(), resource.getBusinessType(), resource.getBusinessId()) != null)
                .throwBaseException(new OCException(ErrorEnum.PROJECT_RES_ALREADY_EXIST));
        ProjectResource res = BeanCopierUtil.copyProperties(resource, ProjectResource.class);
        projectResourceService.add(res);
    }

    @Override
    public void unbindProjectResource(Integer id) {
        projectResourceService.deleteById(id);
    }
}
