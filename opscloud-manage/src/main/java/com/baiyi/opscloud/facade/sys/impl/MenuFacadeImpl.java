package com.baiyi.opscloud.facade.sys.impl;

import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.AuthRoleMenu;
import com.baiyi.opscloud.domain.generator.opscloud.Menu;
import com.baiyi.opscloud.domain.generator.opscloud.MenuChild;
import com.baiyi.opscloud.domain.param.sys.MenuParam;
import com.baiyi.opscloud.domain.vo.common.TreeVO;
import com.baiyi.opscloud.domain.vo.sys.MenuVO;
import com.baiyi.opscloud.facade.sys.MenuFacade;
import com.baiyi.opscloud.packer.sys.MenuPacker;
import com.baiyi.opscloud.service.auth.AuthRoleMenuService;
import com.baiyi.opscloud.service.sys.MenuChildService;
import com.baiyi.opscloud.service.sys.MenuService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 修远
 * @Date 2021/6/2 10:21 上午
 * @Since 1.0
 */

@Service
@RequiredArgsConstructor
public class MenuFacadeImpl implements MenuFacade {

    private final MenuService menuService;

    private final MenuChildService menuChildService;

    private final MenuPacker menuPacker;

    private final AuthRoleMenuService authRoleMenuService;

    @Override
    public void saveMenu(MenuParam.MenuSave param) {
        List<Menu> menuList = menuPacker.toDOList(param.getMenuList());
        if (!validMenuList(menuList)) {
            throw new OCException(ErrorEnum.MENU_CONTENT_EMPTY);
        }
        menuList.forEach(menu -> {
            menu.setSeq(menuList.indexOf(menu));
            if (menu.getId() == null) {
                menuService.add(menu);
            } else {
                menuService.update(menu);
            }
        });
    }

    private Boolean validMenuList(List<Menu> menuList) {
        return menuList.stream().allMatch(x -> StringUtils.isNotBlank(x.getIcon()) && StringUtils.isNotBlank(x.getTitle()));
    }

    @Override
    public void saveMenuChild(MenuParam.MenuChildSave param) {
        List<MenuChild> menuChildList = menuPacker.toChildDOList(param.getMenuChildList());
        if (!validMenuChildList(menuChildList)) {
            throw new OCException(ErrorEnum.MENU_CHILD_CONTENT_EMPTY);
        }
        menuChildList.forEach(menuChild -> {
            menuChild.setSeq(menuChildList.indexOf(menuChild));
            if (menuChild.getId() == null) {
                menuChildService.add(menuChild);
            } else {
                menuChildService.update(menuChild);
            }
        });
    }

    private Boolean validMenuChildList(List<MenuChild> menuChildList) {
        return menuChildList.stream().allMatch(x -> StringUtils.isNotBlank(x.getTitle()) && StringUtils.isNotBlank(x.getIcon()) && StringUtils.isNotBlank(x.getPath()));
    }

    @Override
    public List<MenuVO.Menu> queryMenu() {
        List<Menu> menuList = menuService.queryAllBySeq();
        return menuPacker.toVOList(menuList);
    }

    @Override
    public List<MenuVO.Child> queryMenuChild(Integer id) {
        List<MenuChild> menuChildList = menuChildService.listByMenuId(id);
        return menuPacker.toChildVOList(menuChildList);
    }

    @Override
    public void delMenuById(Integer id) {
        List<MenuChild> menuChildList = menuChildService.listByMenuId(id);
        if (!CollectionUtils.isEmpty(menuChildList)) {
            throw new OCException(ErrorEnum.MENU_CHILD_IS_NOT_EMPTY);
        }
        menuService.del(id);
    }

    @Override
    public void delMenuChildById(Integer id) {
        menuChildService.del(id);
    }

    @Override
    public List<TreeVO.Tree> queryMenuTree() {
        return menuPacker.wrapTree();
    }

    @Override
    @Transactional(rollbackFor = {OCException.class, Exception.class})
    public void saveAuthRoleMenu(MenuParam.AuthRoleMenuSave param) {
        authRoleMenuService.deleteByRoleId(param.getRoleId());
        List<AuthRoleMenu> authRoleMenuList = param.getMenuChildIdList().stream().map(menuChildId -> {
            AuthRoleMenu authRoleMenu = new AuthRoleMenu();
            authRoleMenu.setRoleId(param.getRoleId());
            authRoleMenu.setMenuChildId(menuChildId);
            return authRoleMenu;
        }).collect(Collectors.toList());
        try {
            authRoleMenuService.addList(authRoleMenuList);
        } catch (Exception e) {
            throw new OCException(ErrorEnum.ROLE_MENU_SAVE_FAIL);
        }
    }

    @Override
    public List<AuthRoleMenu> queryAuthRoleMenu(Integer roleId) {
        return authRoleMenuService.queryByRoleId(roleId);
    }

    @Override
    public List<MenuVO.Menu> queryAuthRoleMenuDetail(Integer roleId) {
        return menuPacker.toVOList(roleId);
    }

    @Override
    public List<MenuVO.Menu> queryMyMenu() {
        return menuPacker.toVOList(SessionHolder.getUsername());
    }

}