package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.domain.generator.opscloud.AuthRoleMenu;
import com.baiyi.opscloud.mapper.opscloud.AuthRoleMenuMapper;
import com.baiyi.opscloud.service.auth.AuthRoleMenuService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/2 6:01 下午
 * @Since 1.0
 */

@Service
public class AuthRoleMenuServiceImpl implements AuthRoleMenuService {

    @Resource
    private AuthRoleMenuMapper authRoleMenuMapper;

    @Override
    public void addList(List<AuthRoleMenu> authRoleMenuList) {
        authRoleMenuMapper.insertList(authRoleMenuList);
    }

    @Override
    public void delByRoleId(Integer roleId) {
        Example example = new Example(AuthRoleMenu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleId);
        authRoleMenuMapper.deleteByExample(example);
    }

    @Override
    public List<AuthRoleMenu> listByRoleId(Integer roleId) {
        Example example = new Example(AuthRoleMenu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleId);
        return authRoleMenuMapper.selectByExample(example);
    }

    @Override
    public List<AuthRoleMenu> listByRoleIdList(List<Integer> roleIdList) {
        Example example = new Example(AuthRoleMenu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("roleId", roleIdList);
        return authRoleMenuMapper.selectByExample(example);
    }
}
