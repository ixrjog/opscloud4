package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.domain.generator.opscloud.AuthRoleResource;
import com.baiyi.opscloud.mapper.AuthRoleResourceMapper;
import com.baiyi.opscloud.service.auth.AuthRoleResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/12 4:13 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthRoleResourceServiceImpl implements AuthRoleResourceService {

    private final AuthRoleResourceMapper authRoleResourceMapper;

    @Override
    public Integer countByRoleId(Integer roleId) {
        Example example = new Example(AuthRoleResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleId);
        return authRoleResourceMapper.selectCountByExample(example);
    }

    @Override
    public Integer countByResourceId(Integer resourceId) {
        Example example = new Example(AuthRoleResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("resourceId", resourceId);
        return authRoleResourceMapper.selectCountByExample(example);
    }

    @Override
    public List<AuthRoleResource> queryByRoleId(Integer roleId) {
        Example example = new Example(AuthRoleResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleId);
        return authRoleResourceMapper.selectByExample(example);
    }

    @Override
    public AuthRoleResource getByUniqueKey(AuthRoleResource authRoleResource) {
        Example example = new Example(AuthRoleResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", authRoleResource.getRoleId())
                .andEqualTo("resourceId", authRoleResource.getResourceId());
        return authRoleResourceMapper.selectOneByExample(example);
    }

    @Override
    public void add(AuthRoleResource authRoleResource) {
        authRoleResourceMapper.insert(authRoleResource);
    }

    @Override
    public void deleteById(int id) {
        authRoleResourceMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteByResourceId(Integer resourceId) {
        Example example = new Example(AuthRoleResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("resourceId", resourceId);
        authRoleResourceMapper.deleteByExample(example);
    }

}