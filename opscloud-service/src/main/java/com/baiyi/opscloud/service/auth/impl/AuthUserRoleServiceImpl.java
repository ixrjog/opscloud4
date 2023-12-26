package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.domain.generator.opscloud.AuthUserRole;
import com.baiyi.opscloud.mapper.AuthUserRoleMapper;
import com.baiyi.opscloud.service.auth.AuthUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/14 11:50 上午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthUserRoleServiceImpl implements AuthUserRoleService {

    private final AuthUserRoleMapper authUserRoleMapper;

    @Override
    public void add(AuthUserRole authUserRole) {
        authUserRoleMapper.insert(authUserRole);
    }

    @Override
    public void deleteById(Integer id) {
        authUserRoleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<AuthUserRole> queryByUsername(String username) {
        Example example = new Example(AuthUserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        return authUserRoleMapper.selectByExample(example);
    }

    @Override
    public AuthUserRole queryByUniqueKey(AuthUserRole authUserRole) {
        Example example = new Example(AuthUserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", authUserRole.getUsername())
                .andEqualTo("roleId", authUserRole.getRoleId());
        return authUserRoleMapper.selectOneByExample(example);
    }

}