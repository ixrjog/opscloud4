package com.baiyi.opscloud.packer.sys;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.AuthRoleMenu;
import com.baiyi.opscloud.domain.generator.opscloud.AuthUserRole;
import com.baiyi.opscloud.domain.generator.opscloud.Menu;
import com.baiyi.opscloud.domain.generator.opscloud.MenuChild;
import com.baiyi.opscloud.domain.vo.common.TreeVO;
import com.baiyi.opscloud.domain.vo.sys.MenuVO;
import com.baiyi.opscloud.service.auth.AuthRoleMenuService;
import com.baiyi.opscloud.service.auth.AuthUserRoleService;
import com.baiyi.opscloud.service.sys.MenuChildService;
import com.baiyi.opscloud.service.sys.MenuService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author 修远
 * @Date 2021/6/2 10:53 上午
 * @Since 1.0
 */

@Component
@RequiredArgsConstructor
public class MenuPacker {

    private final MenuService menuService;

    private final MenuChildService menuChildService;

    private final AuthRoleMenuService authRoleMenuService;

    private final AuthUserRoleService authUserRoleService;

    public List<Menu> toDOList(List<MenuVO.Menu> menuList) {
        return BeanCopierUtil.copyListProperties(menuList, Menu.class);
    }

    public List<MenuChild> toChildDOList(List<MenuVO.Child> menuChildren) {
        return BeanCopierUtil.copyListProperties(menuChildren, MenuChild.class);
    }

    public List<MenuVO.Menu> toVOList(List<Menu> menuList) {
        return BeanCopierUtil.copyListProperties(menuList, MenuVO.Menu.class);
    }

    public List<MenuVO.Child> toChildVOList(List<MenuChild> menuChildList) {
        return BeanCopierUtil.copyListProperties(menuChildList, MenuVO.Child.class);
    }

    public List<TreeVO.Tree> wrapTree() {
        List<Menu> menuList = menuService.queryAllBySeq();
        List<TreeVO.Tree> treeList = Lists.newArrayListWithCapacity(menuList.size());
        menuList.forEach(ocMenu -> treeList.add(buildTree(ocMenu)));
        return treeList;
    }

    private TreeVO.Tree buildTree(Menu menu) {
        List<MenuChild> menuChildren = menuChildService.listByMenuId(menu.getId());
        List<TreeVO.Tree> treeList = Lists.newArrayListWithCapacity(menuChildren.size());
        menuChildren.forEach(menuChild -> treeList.add(buildTree(menuChild)));
        return TreeVO.Tree.builder()
                .label(menu.getTitle())
                .value(menu.getId() * -1)
                .children(treeList)
                .build();
    }

    private TreeVO.Tree buildTree(MenuChild menuChild) {
        return TreeVO.Tree.builder()
                .label(menuChild.getTitle())
                .value(menuChild.getId())
                .build();
    }

    public List<MenuVO.Menu> toVOList(Integer roleId) {
        List<AuthRoleMenu> authRoleMenuList = authRoleMenuService.queryByRoleId(roleId);
        if (CollectionUtils.isEmpty(authRoleMenuList)) {
            return Collections.emptyList();
        }
        List<MenuChild> menuChildren = querySubMenu(authRoleMenuList);
        return wrapVOList(menuChildren);
    }

    private List<MenuVO.Menu> wrapVOList(List<MenuChild> menuChildren) {
        Map<Integer, List<MenuChild>> map = menuChildren.stream()
                .collect(Collectors.groupingBy(MenuChild::getMenuId));
        List<MenuVO.Menu> menuList = Lists.newArrayListWithCapacity(map.size());
        map.forEach((k, y) -> {
            List<MenuVO.Child> sort = toChildVOList(y.stream()
                    .sorted(Comparator.comparing(MenuChild::getSeq))
                    .collect(Collectors.toList()));
            Menu menu = menuService.getById(k);
            MenuVO.Menu menuVO = MenuVO.Menu.builder()
                    .id(menu.getId())
                    .title(menu.getTitle())
                    .i18nEn(menu.getI18nEn())
                    .icon(menu.getIcon())
                    .seq(menu.getSeq())
                    .children(sort)
                    .build();
            menuList.add(menuVO);
        });
        return menuList.stream()
                .sorted(Comparator.comparing(MenuVO.Menu::getSeq))
                .collect(Collectors.toList());
    }

    private List<MenuChild> querySubMenu(List<AuthRoleMenu> authRoleMenuList) {
        List<Integer> idList = authRoleMenuList.stream().map(AuthRoleMenu::getMenuChildId).collect(Collectors.toList());
        return menuChildService.listByIdList(idList);
    }

    public List<MenuVO.Menu> toVOList(String username) {
        if (StringUtils.isEmpty(username)) {
            return Collections.emptyList();
        }
        List<AuthUserRole> authUserRoleList = authUserRoleService.queryByUsername(username);
        if (CollectionUtils.isEmpty(authUserRoleList)) {
            return Collections.emptyList();
        }
        List<AuthRoleMenu> authRoleMenuList = authRoleMenuService.queryByRoleIds(
                authUserRoleList.stream().map(AuthUserRole::getRoleId).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(authRoleMenuList)) {
            return Collections.emptyList();
        }
        List<MenuChild> menuChildren = querySubMenu(authRoleMenuList);
        return wrapVOList(menuChildren);
    }

}

