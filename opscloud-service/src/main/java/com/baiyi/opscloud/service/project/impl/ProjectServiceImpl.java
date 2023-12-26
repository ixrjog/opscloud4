package com.baiyi.opscloud.service.project.impl;

import com.baiyi.opscloud.common.annotation.ServiceExceptionCatch;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Project;
import com.baiyi.opscloud.domain.param.project.ProjectParam;
import com.baiyi.opscloud.mapper.ProjectMapper;
import com.baiyi.opscloud.service.project.ProjectService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 修远
 * @Date 2023/5/12 5:30 PM
 * @Since 1.0
 */

@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "rawtypes", "resource"})
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;

    @Override
    public Project getById(Integer id) {
        return projectMapper.selectByPrimaryKey(id);
    }

    @Override
    public Project getByKey(String projectKey) {
        Example example = new Example(Project.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectKey", projectKey);
        return projectMapper.selectOneByExample(example);
    }

    @Override
    public Project getByName(String name) {
        Example example = new Example(Project.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        return projectMapper.selectOneByExample(example);
    }

    @Override
    @ServiceExceptionCatch(message = "新增项目错误: 请确认项目名是否冲突!")
    public void add(Project project) {
        projectMapper.insert(project);
    }

    @Override
    @ServiceExceptionCatch(message = "更新项目错误: 请确认项目名是否冲突!")
    public void update(Project project) {
        projectMapper.updateByPrimaryKey(project);
    }

    @Override
    public void updateByPrimaryKeySelective(Project project) {
        projectMapper.updateByPrimaryKeySelective(project);
    }

    @Override
    public void deleteById(Integer id) {
        projectMapper.deleteByPrimaryKey(id);
    }

    @Override
    public DataTable<Project> queryPageByParam(ProjectParam.ProjectPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<Project> data = projectMapper.queryProjectByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public DataTable<Project> queryResPageByParam(ProjectParam.ResProjectPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<Project> data = projectMapper.queryResProjectByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

}