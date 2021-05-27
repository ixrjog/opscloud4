package com.baiyi.caesar.service.auth.impl;

import com.baiyi.caesar.domain.generator.caesar.AuthResource;
import com.baiyi.caesar.domain.generator.caesar.AuthRole;
import com.baiyi.caesar.domain.generator.caesar.AuthRoleResource;
import com.baiyi.caesar.mapper.caesar.AuthRoleMapper;
import com.baiyi.caesar.mapper.caesar.AuthRoleResourceMapper;
import com.baiyi.caesar.service.auth.AuthRoleResourceService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/12 4:13 下午
 * @Version 1.0
 */
@Service
public class AuthRoleResourceServiceImpl implements AuthRoleResourceService {

    @Resource
    private AuthRoleResourceMapper authRoleResourceMapper;

    @Override
    public Integer countByRoleId(Integer roleId){
        Example example = new Example(AuthRoleResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleId);
        return authRoleResourceMapper.selectCountByExample(example);
    }

    @Override
    public Integer countByResourceId(Integer resourceId){
        Example example = new Example(AuthRoleResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("resourceId", resourceId);
        return authRoleResourceMapper.selectCountByExample(example);
    }

    @Override
    public void add(AuthRoleResource authRoleResource) {
        authRoleResourceMapper.insert(authRoleResource);
    }

    @Override
    public void deleteById(int id) {
        authRoleResourceMapper.deleteByPrimaryKey(id);
    }
}
