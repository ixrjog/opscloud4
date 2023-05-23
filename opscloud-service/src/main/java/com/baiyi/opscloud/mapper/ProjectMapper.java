package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.Project;
import com.baiyi.opscloud.domain.param.project.ProjectParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ProjectMapper extends Mapper<Project> {


    /**
     *  模式
     * @param pageQuery
     * @return
     */
    List<Project> queryProjectByParam(ProjectParam.ProjectPageQuery pageQuery);

    List<Project> queryResProjectByParam(ProjectParam.ResProjectPageQuery pageQuery);
}