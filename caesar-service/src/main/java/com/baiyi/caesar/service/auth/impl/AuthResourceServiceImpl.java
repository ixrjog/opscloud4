package com.baiyi.caesar.service.auth.impl;

import com.baiyi.caesar.common.util.IdUtil;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.AuthResource;
import com.baiyi.caesar.domain.param.auth.AuthResourceParam;
import com.baiyi.caesar.mapper.caesar.AuthResourceMapper;
import com.baiyi.caesar.service.auth.AuthResourceService;
import com.baiyi.caesar.util.SQLUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/11 11:19 上午
 * @Version 1.0
 */
@Service
public class AuthResourceServiceImpl implements AuthResourceService {

    @Resource
    private AuthResourceMapper authResourceMapper;

    @Override
    public DataTable<AuthResource> queryRoleBindResourcePageByParam(AuthResourceParam.RoleBindResourcePageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<AuthResource> data = pageQuery.getBind() ? authResourceMapper.queryRoleBindResourceByParam(pageQuery)
                : authResourceMapper.queryRoleUnbindResourceByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public DataTable<AuthResource> queryPageByParam(AuthResourceParam.AuthResourcePageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(AuthResource.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(pageQuery.getResourceName())) {
            criteria.andLike("resourceName", SQLUtil.toLike(pageQuery.getResourceName()));
        }
        if (!IdUtil.isEmpty(pageQuery.getGroupId())) {
            criteria.andEqualTo("groupId", pageQuery.getGroupId());
        }
        if (pageQuery.getNeedAuth() != null) {
            criteria.andEqualTo("needAuth", pageQuery.getNeedAuth());
        }
        example.setOrderByClause("create_time");
        List<AuthResource> data = authResourceMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public AuthResource queryByName(String resourceName) {
        Example example = new Example(AuthResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("resourceName", resourceName);
        return authResourceMapper.selectOneByExample(example);
    }

    @Override
    public Integer countByGroupId(Integer groupId){
        Example example = new Example(AuthResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupId", groupId);
        return authResourceMapper.selectCountByExample(example);
    }

    @Override
    public void update(AuthResource authResource) {
        authResourceMapper.updateByPrimaryKey(authResource);
    }

    @Override
    public AuthResource queryById(int id) {
        return authResourceMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(AuthResource authResource) {
        authResourceMapper.insert(authResource);
    }

    @Override
    public void deleteById(int id) {
        authResourceMapper.deleteByPrimaryKey(id);
    }
}
