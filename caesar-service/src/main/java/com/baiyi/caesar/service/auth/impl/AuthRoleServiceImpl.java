package com.baiyi.caesar.service.auth.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.AuthRole;
import com.baiyi.caesar.domain.param.auth.AuthRoleParam;
import com.baiyi.caesar.mapper.caesar.AuthRoleMapper;
import com.baiyi.caesar.service.auth.AuthRoleService;
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
 * @Date 2021/5/12 9:27 上午
 * @Version 1.0
 */
@Service
public class AuthRoleServiceImpl implements AuthRoleService {

    @Resource
    private AuthRoleMapper authRoleMapper;

    @Override
    public AuthRole getByRoleName(String roleName){
        Example example = new Example(AuthRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleName", roleName);
        return authRoleMapper.selectOneByExample(example);
    }

    @Override
    public AuthRole getById(int id) {
        return authRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    public DataTable<AuthRole> queryPageByParam(AuthRoleParam.AuthRolePageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(AuthRole.class);
        if (StringUtils.isNotBlank(pageQuery.getRoleName())) {
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("roleName", SQLUtil.toLike(pageQuery.getRoleName()));
        }
        example.setOrderByClause("create_time");
        List<AuthRole> data = authRoleMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public void add(AuthRole authRole) {
        authRoleMapper.insert(authRole);
    }

    @Override
    public void update(AuthRole authRole) {
        authRoleMapper.updateByPrimaryKey(authRole);
    }

    @Override
    public void deleteById(int id) {
        authRoleMapper.deleteByPrimaryKey(id);
    }
}
