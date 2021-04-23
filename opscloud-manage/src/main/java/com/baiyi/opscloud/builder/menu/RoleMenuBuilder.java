package com.baiyi.opscloud.builder.menu;

import com.baiyi.opscloud.domain.generator.opscloud.OcRoleMenu;
import com.baiyi.opscloud.domain.param.menu.MenuParam;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/2/2 4:34 下午
 * @Since 1.0
 */
public class RoleMenuBuilder {

    private static OcRoleMenu build(Integer roleId, Integer submenuId) {
        OcRoleMenu ocRoleMenu = new OcRoleMenu();
        ocRoleMenu.setRoleId(roleId);
        ocRoleMenu.setSubmenuId(submenuId);
        return ocRoleMenu;
    }

    public static List<OcRoleMenu> buildList(MenuParam.RoleMenuSave param) {
        List<OcRoleMenu> ocRoleMenuList = Lists.newArrayListWithCapacity(param.getSubmenuIdList().size());
        param.getSubmenuIdList().forEach(x -> ocRoleMenuList.add(build(param.getRoleId(), x)));
        return ocRoleMenuList;
    }
}
