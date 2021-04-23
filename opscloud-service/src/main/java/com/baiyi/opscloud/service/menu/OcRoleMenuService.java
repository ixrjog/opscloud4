package com.baiyi.opscloud.service.menu;

import com.baiyi.opscloud.domain.generator.opscloud.OcRoleMenu;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/2/2 3:28 下午
 * @Since 1.0
 */
public interface OcRoleMenuService {

    void addOcRoleMenuList(List<OcRoleMenu> ocRoleMenuList);

    void delOcRoleMenuByRoleId(Integer roleId);

    List<OcRoleMenu> queryOcRoleMenuByRoleIdList(List<Integer> roleIdList);

    List<OcRoleMenu> queryOcRoleMenuByRoleId(Integer roleId);
}
