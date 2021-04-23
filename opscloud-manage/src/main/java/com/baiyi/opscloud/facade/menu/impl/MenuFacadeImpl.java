package com.baiyi.opscloud.facade.menu.impl;

import com.baiyi.opscloud.builder.menu.RoleMenuBuilder;
import com.baiyi.opscloud.common.util.SessionUtils;
import com.baiyi.opscloud.decorator.menu.MenuDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcMenu;
import com.baiyi.opscloud.domain.generator.opscloud.OcRoleMenu;
import com.baiyi.opscloud.domain.generator.opscloud.OcSubmenu;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.menu.MenuParam;
import com.baiyi.opscloud.domain.vo.auth.menu.MenuVO;
import com.baiyi.opscloud.domain.vo.tree.TreeVO;
import com.baiyi.opscloud.facade.menu.MenuFacade;
import com.baiyi.opscloud.service.menu.OcMenuService;
import com.baiyi.opscloud.service.menu.OcRoleMenuService;
import com.baiyi.opscloud.service.menu.OcSubmenuService;
import com.baiyi.opscloud.service.user.OcUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/29 5:06 下午
 * @Since 1.0
 */

@Slf4j
@Component("MenuFacade")
public class MenuFacadeImpl implements MenuFacade {

    @Resource
    private OcMenuService ocMenuService;

    @Resource
    private OcSubmenuService ocSubmenuService;

    @Resource
    private MenuDecorator menuDecorator;

    @Resource
    private OcRoleMenuService ocRoleMenuService;

    @Resource
    private OcUserService ocUserService;

    @Override
    public BusinessWrapper<Boolean> saveMenuList(MenuParam.MenuSave param) {
        List<OcMenu> ocMenuList = param.getMenuList();
        if (!validMenuList(ocMenuList))
            return new BusinessWrapper<>(ErrorEnum.MENU_CONTENT_EMPTY);
        ocMenuList.forEach(ocMenu -> {
            ocMenu.setMenuOrder(ocMenuList.indexOf(ocMenu));
            if (ocMenu.getId() == null)
                ocMenuService.addOcMenu(ocMenu);
            else
                ocMenuService.updateOcMenu(ocMenu);
        });
        clearUserMenu();
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> delMenu(int id) {
        ocMenuService.delOcMenu(id);
        clearUserMenu();
        return BusinessWrapper.SUCCESS;
    }

    private Boolean validMenuList(List<OcMenu> menuList) {
        return menuList.stream().allMatch(x ->
                Strings.isNotBlank(x.getMenuIcon())
                        && Strings.isNotBlank(x.getMenuTitle())
        );
    }

    private Boolean validSubmenuList(List<OcSubmenu> submenuList) {
        return submenuList.stream().allMatch(x ->
                Strings.isNotBlank(x.getSubmenuTitle())
                        && Strings.isNotBlank(x.getSubmenuIcon())
                        && Strings.isNotBlank(x.getSubmenuPath())
        );
    }

    @Override
    public BusinessWrapper<Boolean> saveSubmenuList(MenuParam.SubmenuSave param) {
        List<OcSubmenu> ocSubmenuList = param.getSubmenuList();
        if (!validSubmenuList(ocSubmenuList))
            return new BusinessWrapper<>(ErrorEnum.SUBMENU_CONTENT_EMPTY);
        ocSubmenuList.forEach(ocSubmenu -> {
            ocSubmenu.setSubmenuOrder(ocSubmenuList.indexOf(ocSubmenu));
            if (ocSubmenu.getId() == null)
                ocSubmenuService.addOcSubmenu(ocSubmenu);
            else
                ocSubmenuService.updateOcSubmenu(ocSubmenu);
        });
        clearUserMenu();
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> delSubmenu(int id) {
        ocSubmenuService.delOcSubmenu(id);
        clearUserMenu();
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<List<OcMenu>> queryMenuAll() {
        List<OcMenu> ocMenuList = ocMenuService.queryOcMenuAll();
        return new BusinessWrapper<>(ocMenuList);
    }

    @Override
    public BusinessWrapper<List<OcSubmenu>> querySubmenuByMenuId(Integer menuId) {
        List<OcSubmenu> ocSubmenuList = ocSubmenuService.queryOcSubmenuByMenuId(menuId);
        return new BusinessWrapper<>(ocSubmenuList);
    }

    @Override
    public BusinessWrapper<List<TreeVO.Tree>> queryMenuListTree() {
        List<TreeVO.Tree> tree = menuDecorator.decoratorTree();
        return new BusinessWrapper<>(tree);
    }

    @Override
    public BusinessWrapper<List<MenuVO>> queryMyMenu() {
        List<MenuVO> menu = menuDecorator.rendererMenuList(SessionUtils.getUsername());
        return new BusinessWrapper<>(menu);
    }

    @Override
    public BusinessWrapper<Boolean> saveRoleMenu(MenuParam.RoleMenuSave param) {
        ocRoleMenuService.delOcRoleMenuByRoleId(param.getRoleId());
        List<OcRoleMenu> ocRoleMenuList = RoleMenuBuilder.buildList(param);
        try {
            ocRoleMenuService.addOcRoleMenuList(ocRoleMenuList);
            clearUserMenu();
            return BusinessWrapper.SUCCESS;
        } catch (Exception e) {
            log.error("保存角色菜单失败", e);
            return new BusinessWrapper<>(ErrorEnum.ROLE_MENU_SAVE_FAIL);
        }
    }

    @Override
    public BusinessWrapper<List<OcRoleMenu>> queryRoleMenu(Integer roleId) {
        List<OcRoleMenu> roleMenuList = ocRoleMenuService.queryOcRoleMenuByRoleId(roleId);
        return new BusinessWrapper<>(roleMenuList);
    }

    @Override
    public BusinessWrapper<String> queryRoleMenuTemp(Integer roleId) {
        List<OcRoleMenu> roleMenuList = ocRoleMenuService.queryOcRoleMenuByRoleId(roleId);
        return new BusinessWrapper<>(menuDecorator.renderMenuTemplate(roleMenuList));
    }

    private void clearUserMenu() {
        List<OcUser> ocUserList = ocUserService.queryOcUserAll();
        ocUserList.forEach(ocUser -> menuDecorator.evictMenuList(ocUser.getUsername()));
    }

    @Override
    public void removeUserMenuMap(String username) {
        menuDecorator.evictMenuList(username);
    }

}
