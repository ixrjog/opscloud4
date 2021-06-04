package com.baiyi.caesar.facade.sys.impl;

import com.baiyi.caesar.common.exception.common.CommonRuntimeException;
import com.baiyi.caesar.common.util.SessionUtil;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.AuthRoleMenu;
import com.baiyi.caesar.domain.generator.caesar.Menu;
import com.baiyi.caesar.domain.generator.caesar.MenuChild;
import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.domain.param.sys.MenuParam;
import com.baiyi.caesar.domain.vo.common.TreeVO;
import com.baiyi.caesar.domain.vo.sys.MenuVO;
import com.baiyi.caesar.facade.sys.MenuFacade;
import com.baiyi.caesar.packer.sys.MenuPacker;
import com.baiyi.caesar.service.auth.AuthRoleMenuService;
import com.baiyi.caesar.service.auth.AuthUserRoleService;
import com.baiyi.caesar.service.sys.MenuChildService;
import com.baiyi.caesar.service.sys.MenuService;
import com.baiyi.caesar.service.user.UserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/2 10:21 上午
 * @Since 1.0
 */

@Service
public class MenuFacadeImpl implements MenuFacade {

    @Resource
    private MenuService menuService;

    @Resource
    private MenuChildService menuChildService;

    @Resource
    private MenuPacker menuPacker;

    @Resource
    private AuthRoleMenuService authRoleMenuService;

    @Resource
    private AuthUserRoleService authUserRoleService;

    @Resource
    private UserService userService;

    @Override
    public void saveMenu(MenuParam.MenuSave param) {
        List<Menu> menuList = menuPacker.toDOList(param.getMenuList());
        if (!validMenuList(menuList))
            throw new CommonRuntimeException(ErrorEnum.MENU_CONTENT_EMPTY);
        menuList.forEach(menu -> {
            menu.setSeq(menuList.indexOf(menu));
            if (menu.getId() == null)
                menuService.add(menu);
            else
                menuService.update(menu);
        });
        clearUserMenu();
    }

    private Boolean validMenuList(List<Menu> menuList) {
        return menuList.stream().allMatch(x ->
                Strings.isNotBlank(x.getIcon())
                        && Strings.isNotBlank(x.getTitle())
        );
    }

    @Override
    public void saveMenuChild(MenuParam.MenuChildSave param) {
        List<MenuChild> menuChildList = menuPacker.toChildDOList(param.getMenuChildList());
        if (!validMenuChildList(menuChildList))
            throw new CommonRuntimeException(ErrorEnum.MENU_CHILD_CONTENT_EMPTY);
        menuChildList.forEach(menuChild -> {
            menuChild.setSeq(menuChildList.indexOf(menuChild));
            if (menuChild.getId() == null)
                menuChildService.add(menuChild);
            else
                menuChildService.update(menuChild);
        });
        clearUserMenu();
    }

    private Boolean validMenuChildList(List<MenuChild> menuChildList) {
        return menuChildList.stream().allMatch(x ->
                Strings.isNotBlank(x.getTitle())
                        && Strings.isNotBlank(x.getIcon())
                        && Strings.isNotBlank(x.getPath())
        );
    }

    @Override
    public List<MenuVO.Menu> queryMenu() {
        List<Menu> menuList = menuService.queryAllBySeq();
        return menuPacker.toVOList(menuList);
    }

    @Override
    public List<MenuVO.MenuChild> queryMenuChild(Integer id) {
        List<MenuChild> menuChildList = menuChildService.listByMenuId(id);
        return menuPacker.toChildVOList(menuChildList);
    }

    @Override
    public void delMenuById(Integer id) {
        List<MenuChild> menuChildList = menuChildService.listByMenuId(id);
        if (!CollectionUtils.isEmpty(menuChildList))
            throw new CommonRuntimeException(ErrorEnum.MENU_CHILD_IS_NOT_EMPTY);
        menuService.del(id);
        clearUserMenu();
    }

    @Override
    public void delMenuChildById(Integer id) {
        menuChildService.del(id);
        clearUserMenu();
    }

    @Override
    public List<TreeVO.Tree> queryMenuTree() {
        return menuPacker.wrapTree();
    }

    @Override
    @Transactional(rollbackFor = {CommonRuntimeException.class, Exception.class})
    public void saveAuthRoleMenu(MenuParam.AuthRoleMenuSave param) {
        authRoleMenuService.delByRoleId(param.getRoleId());
        List<AuthRoleMenu> authRoleMenuList = param.getMenuChildIdList().stream().map(menuChildId -> {
            AuthRoleMenu authRoleMenu = new AuthRoleMenu();
            authRoleMenu.setRoleId(param.getRoleId());
            authRoleMenu.setMenuChildId(menuChildId);
            return authRoleMenu;
        }).collect(Collectors.toList());
        try {
            authRoleMenuService.addList(authRoleMenuList);
            clearUserMenu();
        } catch (Exception e) {
            throw new CommonRuntimeException(ErrorEnum.ROLE_MENU_SAVE_FAIL);
        }
    }

    @Override
    public List<AuthRoleMenu> queryAuthRoleMenu(Integer roleId) {
        return authRoleMenuService.listByRoleId(roleId);
    }

    @Override
    public List<MenuVO.Menu> queryAuthRoleMenuDetail(Integer roleId) {
        return menuPacker.toVOList(roleId);
    }

    @Override
    public List<MenuVO.Menu> queryMyMenu() {
        return menuPacker.toVOList(SessionUtil.getUsername());
    }

    private void clearUserMenu() {
        List<User> userList = userService.listActive();
        userList.forEach(ocUser -> menuPacker.evictMenuVOList(ocUser.getUsername()));
    }
}
