package com.baiyi.opscloud.facade.project;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.project.ProjectResourceParam;
import com.baiyi.opscloud.domain.vo.project.ProjectResourceVO;

/**
 * @Author baiyi
 * @Date 2023/5/17 16:42
 * @Version 1.0
 */
public interface IProjectResource {


    DataTable<ProjectResourceVO.Resource> previewProjectResourcePage(ProjectResourceParam.ResourcePageQuery pageQuery);

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
