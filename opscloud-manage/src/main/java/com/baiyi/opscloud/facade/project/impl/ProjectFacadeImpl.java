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
import com.baiyi.opscloud.domain.param.project.ProjectResourceParam;
import com.baiyi.opscloud.domain.vo.project.ProjectVO;
import com.baiyi.opscloud.facade.project.ProjectFacade;
import com.baiyi.opscloud.service.project.ProjectResourceService;
import com.baiyi.opscloud.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author 修远
 * @Date 2023/5/15 5:44 PM
 * @Since 1.0
 */
@Slf4j
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
    public void addProject(ProjectParam.AddProject project) {
        project.setProjectKey(project.getProjectKey().replaceAll(" ", "").toUpperCase());
        if (StringUtils.isBlank(project.getProjectKey())) {
            throw new OCException(ErrorEnum.PROJECT_KEY_CANNOT_BE_EMPTY);
        }
        FunctionUtil.isTure(StringUtils.isBlank(project.getProjectKey()))
                .throwBaseException(new OCException(ErrorEnum.PROJECT_KEY_CANNOT_BE_EMPTY));
        FunctionUtil.isTure(projectService.getByKey(project.getProjectKey()) != null)
                .throwBaseException(new OCException(ErrorEnum.PROJECT_ALREADY_EXIST));
        Project newProject = BeanCopierUtil.copyProperties(project, Project.class);
        projectService.add(newProject);
    }

    @Override
    public void updateProject(ProjectParam.UpdateProject project) {
        projectService.updateByPrimaryKeySelective(BeanCopierUtil.copyProperties(project, Project.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProjectAndUnbindAllResource(Integer projectId) {
        // 解除所有资源
        List<ProjectResource> resources = projectResourceService.queryByProjectId(projectId);
        for (ProjectResource resource : resources) {
            this.unbindResource(resource.getId());
        }
        // 再删除项目
        projectService.deleteById(projectId);
    }

    @Override
    public void deleteProject(Integer projectId) {
        FunctionUtil.isTure(!CollectionUtils.isEmpty(projectResourceService.queryByProjectId(projectId)))
                .throwBaseException(new OCException(ErrorEnum.PROJECT_RES_IS_NOT_EMPTY));
        projectService.deleteById(projectId);
    }

    @Override
    public void bindResource(ProjectResourceParam.Resource resource) {
        FunctionUtil.isTure(projectResourceService.getByProjectResource(resource) != null)
                .throwBaseException(new OCException(ErrorEnum.PROJECT_RES_ALREADY_EXIST));
        ProjectResource res = BeanCopierUtil.copyProperties(resource, ProjectResource.class);
        projectResourceService.add(res);
    }

    @Override
    public void unbindResource(Integer id) {
        projectResourceService.deleteById(id);
    }

}
