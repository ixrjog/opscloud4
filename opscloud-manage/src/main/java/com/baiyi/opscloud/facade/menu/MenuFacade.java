package com.baiyi.opscloud.facade.menu;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcMenu;
import com.baiyi.opscloud.domain.generator.opscloud.OcRoleMenu;
import com.baiyi.opscloud.domain.generator.opscloud.OcSubmenu;
import com.baiyi.opscloud.domain.param.menu.MenuParam;
import com.baiyi.opscloud.domain.vo.auth.menu.MenuVO;
import com.baiyi.opscloud.domain.vo.tree.TreeVO;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/29 5:06 下午
 * @Since 1.0
 */
public interface MenuFacade {

    BusinessWrapper<Boolean> saveMenuList(MenuParam.MenuSave param);

    BusinessWrapper<Boolean> delMenu(int id);

    BusinessWrapper<Boolean> saveSubmenuList(MenuParam.SubmenuSave param);

    BusinessWrapper<Boolean> delSubmenu(int id);

    BusinessWrapper<List<OcMenu>> queryMenuAll();

    BusinessWrapper<List<OcSubmenu>> querySubmenuByMenuId(Integer menuId);

    BusinessWrapper<List<TreeVO.Tree>> queryMenuListTree();

    BusinessWrapper<List<MenuVO>> queryMyMenu();

    BusinessWrapper<Boolean> saveRoleMenu(MenuParam.RoleMenuSave param);

    BusinessWrapper<List<OcRoleMenu>> queryRoleMenu(Integer roleId);

    BusinessWrapper<String> queryRoleMenuTemp(Integer roleId);

    void removeUserMenuMap(String username);
}
