package com.baiyi.opscloud.facade.project;

import com.baiyi.opscloud.domain.param.project.ProjectResourceParam;

/**
 * @Author baiyi
 * @Date 2023/5/17 16:42
 * @Version 1.0
 */
public interface IProjectResource {

    /**
     * 绑定资源
     *
     * @param resource
     */
    void bindResource(ProjectResourceParam.Resource resource);

    /**
     * 解绑资源
     *
     * @param id
     */
    void unbindResource(Integer id);

}
