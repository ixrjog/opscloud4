package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.domain.generator.opscloud.AuthRoleMenu;
import com.baiyi.opscloud.mapper.AuthRoleMenuMapper;
import com.baiyi.opscloud.service.auth.AuthRoleMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 修远
 * @Date 2021/6/2 6:01 下午
 * @Since 1.0
 */

@Service
@RequiredArgsConstructor
public class AuthRoleMenuServiceImpl implements AuthRoleMenuService {

    private final AuthRoleMenuMapper authRoleMenuMapper;

    @Override
    public void addList(List<AuthRoleMenu> authRoleMenuList) {
        authRoleMenuMapper.insertList(authRoleMenuList);
    }

    @Override
    public void deleteByRoleId(Integer roleId) {
        Example example = new Example(AuthRoleMenu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleId);
        authRoleMenuMapper.deleteByExample(example);
    }

    @Override
    public List<AuthRoleMenu> queryByRoleId(Integer roleId) {
        Example example = new Example(AuthRoleMenu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleId);
        return authRoleMenuMapper.selectByExample(example);
    }

    @Override
    public List<AuthRoleMenu> queryByRoleIds(List<Integer> roleIds) {
        Example example = new Example(AuthRoleMenu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("roleId", roleIds);
        return authRoleMenuMapper.selectByExample(example);
    }

}