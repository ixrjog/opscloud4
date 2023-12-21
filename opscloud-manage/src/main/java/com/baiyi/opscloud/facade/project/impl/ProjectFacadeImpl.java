package com.baiyi.opscloud.facade.project.impl;

import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.FunctionUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Project;
import com.baiyi.opscloud.domain.generator.opscloud.ProjectResource;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.param.SimpleRelation;
import com.baiyi.opscloud.domain.param.project.ProjectParam;
import com.baiyi.opscloud.domain.param.project.ProjectResourceParam;
import com.baiyi.opscloud.domain.vo.project.ProjectResourceVO;
import com.baiyi.opscloud.domain.vo.project.ProjectVO;
import com.baiyi.opscloud.facade.project.ProjectFacade;
import com.baiyi.opscloud.factory.resource.IProjectResQuery;
import com.baiyi.opscloud.factory.resource.ProjectResQueryFactory;
import com.baiyi.opscloud.packer.project.ProjectPacker;
import com.baiyi.opscloud.service.project.ProjectResourceService;
import com.baiyi.opscloud.service.project.ProjectService;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.baiyi.opscloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

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

    private final ProjectPacker projectPacker;

    private final UserPermissionService userPermissionService;

    private final UserService userService;

    @Override
    public DataTable<ProjectVO.Project> queryProjectPage(ProjectParam.ProjectPageQuery pageQuery) {
        DataTable<Project> table = projectService.queryPageByParam(pageQuery);
        List<ProjectVO.Project> data = BeanCopierUtil.copyListProperties(table.getData(), ProjectVO.Project.class)
                .stream()
                .peek(e -> projectPacker.wrap(e, pageQuery, SimpleRelation.RELATION))
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public DataTable<ProjectVO.Project> queryResProjectPage(ProjectParam.ResProjectPageQuery pageQuery) {
        DataTable<Project> table = projectService.queryResPageByParam(pageQuery);
        List<ProjectVO.Project> data = BeanCopierUtil.copyListProperties(table.getData(), ProjectVO.Project.class)
                .stream()
                .peek(e -> projectPacker.wrap(e, pageQuery, SimpleRelation.NOT_RELATION))
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public ProjectVO.Project getProjectById(Integer id) {
        Project project = projectService.getById(id);
        FunctionUtil.isNull(project)
                .throwBaseException(new OCException(ErrorEnum.APPLICATION_NOT_EXIST));
        ProjectVO.Project vo = BeanCopierUtil.copyProperties(project, ProjectVO.Project.class);
        projectPacker.wrap(vo, SimpleExtend.EXTEND, SimpleRelation.RELATION);
        return vo;
    }

    @Override
    public void addProject(ProjectParam.AddProject project) {
        Project newProject = BeanCopierUtil.copyProperties(project, Project.class);
        newProject.setProjectKey(IdUtil.buildUUID());
        projectService.add(newProject);
        addProjectUserPermission(newProject);
    }

    private void addProjectUserPermission(Project project) {
        User user = userService.getByUsername(SessionHolder.getUsername());
        UserPermission userPermission = UserPermission.builder()
                .userId(user.getId())
                .businessId(project.getId())
                .businessType(BusinessTypeEnum.PROJECT.getType())
                .permissionRole("Admin")
                .build();
        userPermissionService.add(userPermission);
    }

    @Override
    public void updateProject(ProjectParam.UpdateProject project) {
        projectService.updateByPrimaryKeySelective(BeanCopierUtil.copyProperties(project, Project.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProjectAndUnbindAllResource(Integer projectId) {
        // 解除所有资源
        projectResourceService.listByProjectId(projectId)
                .forEach(resource -> this.unbindResource(resource.getId()));
        // 再删除项目
        projectService.deleteById(projectId);
    }

    @Override
    public void deleteProject(Integer projectId) {
        FunctionUtil.isTure(!CollectionUtils.isEmpty(projectResourceService.listByProjectId(projectId)))
                .throwBaseException(new OCException(ErrorEnum.PROJECT_RES_IS_NOT_EMPTY));
        projectService.deleteById(projectId);
    }

    @Override
    public DataTable<ProjectResourceVO.Resource> previewProjectResourcePage(ProjectResourceParam.ResourcePageQuery pageQuery) {
        IProjectResQuery resQuery = ProjectResQueryFactory.getProjectResQuery(pageQuery.getProjectResType(), pageQuery.getBusinessType());
        FunctionUtil.isNull(resQuery)
                .throwBaseException(new OCException("无法预览应用资源，未找到对应的方法！"));
        return resQuery.queryResourcePage(pageQuery);
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