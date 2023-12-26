package com.baiyi.opscloud.service.auth;

import com.baiyi.opscloud.domain.generator.opscloud.AuthRoleMenu;

import java.util.List;

/**
 * @Author 修远
 * @Date 2021/6/2 6:01 下午
 * @Since 1.0
 */
public interface AuthRoleMenuService {

    void addList(List<AuthRoleMenu> authRoleMenuList);

    void deleteByRoleId(Integer roleId);

    List<AuthRoleMenu> queryByRoleId(Integer roleId);

    List<AuthRoleMenu> queryByRoleIds(List<Integer> roleIdList);

}