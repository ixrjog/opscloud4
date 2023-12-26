package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.AuthRole;
import com.baiyi.opscloud.domain.param.auth.AuthRoleParam;
import com.baiyi.opscloud.mapper.AuthRoleMapper;
import com.baiyi.opscloud.service.auth.AuthRoleService;
import com.baiyi.opscloud.util.SQLUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/12 9:27 上午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthRoleServiceImpl implements AuthRoleService {

    private final AuthRoleMapper authRoleMapper;

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
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(AuthRole.class);
        if (StringUtils.isNotBlank(pageQuery.getRoleName())) {
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("roleName", SQLUtil.toLike(pageQuery.getRoleName()));
        }
        example.setOrderByClause("access_level desc, create_time");
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

    @Override
    public int getRoleAccessLevelByUsername(String username) {
        return authRoleMapper.getRoleAccessLevelByUsername(username);
    }

}