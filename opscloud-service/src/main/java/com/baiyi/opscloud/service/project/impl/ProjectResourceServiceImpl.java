package com.baiyi.opscloud.service.project.impl;

import com.baiyi.opscloud.domain.generator.opscloud.ProjectResource;
import com.baiyi.opscloud.mapper.ProjectResourceMapper;
import com.baiyi.opscloud.service.project.ProjectResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 修远
 * @Date 2023/5/12 5:31 PM
 * @Since 1.0
 */

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@RequiredArgsConstructor
public class ProjectResourceServiceImpl implements ProjectResourceService {

    private final ProjectResourceMapper projectResourceMapper;

    @Override
    public ProjectResource getById(Integer id) {
        return projectResourceMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(ProjectResource projectResource) {
        projectResourceMapper.insert(projectResource);
    }

    @Override
    public void update(ProjectResource projectResource) {
        projectResourceMapper.updateByPrimaryKey(projectResource);
    }

    @Override
    public void deleteById(Integer id) {
        projectResourceMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<ProjectResource> listByProjectId(Integer projectId) {
        Example example = new Example(ProjectResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId);
        return projectResourceMapper.selectByExample(example);
    }

    @Override
    public ProjectResource getByUniqueKey(Integer projectId, Integer businessType, Integer businessId) {
        Example example = new Example(ProjectResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId)
                .andEqualTo("businessType", businessType)
                .andEqualTo("businessId", businessId);
        return projectResourceMapper.selectOneByExample(example);
    }

    @Override
    public List<ProjectResource> queryByBusiness(Integer businessType, Integer businessId){
        Example example = new Example(ProjectResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", businessType)
                .andEqualTo("businessId", businessId);
        return projectResourceMapper.selectByExample(example);
    }

}