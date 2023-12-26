package com.baiyi.opscloud.service.project;

import com.baiyi.opscloud.domain.base.BaseProjectResource;
import com.baiyi.opscloud.domain.generator.opscloud.ProjectResource;

import java.util.List;

/**
 * @Author 修远
 * @Date 2023/5/12 5:31 PM
 * @Since 1.0
 */
public interface ProjectResourceService {

    ProjectResource getById(Integer id);

    void add(ProjectResource projectResource);

    void update(ProjectResource projectResource);

    void deleteById(Integer id);

    List<ProjectResource> listByProjectId(Integer projectId);

    ProjectResource getByUniqueKey(Integer projectId, Integer businessType, Integer businessId);

    default ProjectResource getByProjectResource(BaseProjectResource.IProjectResource resource) {
        return getByUniqueKey(resource.getProjectId(), resource.getBusinessType(), resource.getBusinessId());
    }

    List<ProjectResource> queryByBusiness(Integer businessType, Integer businessId);

}