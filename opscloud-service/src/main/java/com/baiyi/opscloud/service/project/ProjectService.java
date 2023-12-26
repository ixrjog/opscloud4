package com.baiyi.opscloud.service.project;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Project;
import com.baiyi.opscloud.domain.param.project.ProjectParam;

/**
 * @Author 修远
 * @Date 2023/5/12 5:30 PM
 * @Since 1.0
 */
public interface ProjectService {

    Project getById(Integer id);

    Project getByKey(String projectKey);

    Project getByName(String name);

    void add(Project project);

    void update(Project project);

    void updateByPrimaryKeySelective(Project project);

    void deleteById(Integer id);

    DataTable<Project> queryPageByParam(ProjectParam.ProjectPageQuery pageQuery);

    DataTable<Project> queryResPageByParam(ProjectParam.ResProjectPageQuery pageQuery);

}