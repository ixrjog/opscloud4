package com.baiyi.caesar.service.auth;

import com.baiyi.caesar.domain.generator.caesar.AuthRoleMenu;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/2 6:01 下午
 * @Since 1.0
 */
public interface AuthRoleMenuService {

    void addList(List<AuthRoleMenu> authRoleMenuList);

    void delByRoleId(Integer roleId);

    List<AuthRoleMenu> listByRoleId(Integer roleId);

    List<AuthRoleMenu> listByRoleIdList(List<Integer> roleIdList);
}
