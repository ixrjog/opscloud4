package com.baiyi.opscloud.decorator.menu;

import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.common.util.BeetlUtils;
import com.baiyi.opscloud.common.util.ObjectUtils;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.menu.MenuParam;
import com.baiyi.opscloud.domain.vo.auth.menu.MenuChildrenVO;
import com.baiyi.opscloud.domain.vo.auth.menu.MenuVO;
import com.baiyi.opscloud.domain.vo.tree.TreeVO;
import com.baiyi.opscloud.service.auth.OcAuthUserRoleService;
import com.baiyi.opscloud.service.file.OcFileTemplateService;
import com.baiyi.opscloud.service.menu.OcMenuService;
import com.baiyi.opscloud.service.menu.OcRoleMenuService;
import com.baiyi.opscloud.service.menu.OcSubmenuService;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/2/2 2:18 下午
 * @Since 1.0
 */

@Component("MenuDecorator")
public class MenuDecorator {

    private static final String FILE_TEMPLATE_OC_MENU = "OC_MENU";

    @Resource
    private OcMenuService ocMenuService;

    @Resource
    private OcSubmenuService ocSubmenuService;

    @Resource
    private OcFileTemplateService ocFileTemplateService;

    @Resource
    private OcAuthUserRoleService ocAuthUserRoleService;

    @Resource
    private OcRoleMenuService ocRoleMenuService;

    public List<TreeVO.Tree> decoratorTree() {
        List<OcMenu> menuList = ocMenuService.queryOcMenuAll();
        List<TreeVO.Tree> treeList = Lists.newArrayListWithCapacity(menuList.size());
        menuList.forEach(ocMenu -> treeList.add(buildTree(ocMenu)));
        return treeList;
    }

    private TreeVO.Tree buildTree(OcMenu ocMenu) {
        List<OcSubmenu> submenuList = ocSubmenuService.queryOcSubmenuByMenuId(ocMenu.getId());
        List<TreeVO.Tree> treeList = Lists.newArrayListWithCapacity(submenuList.size());
        submenuList.forEach(ocSubmenu -> treeList.add(buildTree(ocSubmenu)));
        return TreeVO.Tree.builder()
                .label(ocMenu.getMenuTitle())
                .value(ocMenu.getId() * -1)
                .children(treeList)
                .build();
    }

    private TreeVO.Tree buildTree(OcSubmenu ocSubmenu) {
        return TreeVO.Tree.builder()
                .label(ocSubmenu.getSubmenuTitle())
                .value(ocSubmenu.getId())
                .build();
    }

    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_COMMON_BY_DAY, key = "'menuDecorator_username_' + #username")
    public List<MenuVO> rendererMenuList(String username) {
        List<OcAuthUserRole> userRoleList = ocAuthUserRoleService.queryOcAuthUserRolesByUsername(username);
        List<Integer> roleIdList = userRoleList.stream()
                .map(OcAuthUserRole::getRoleId)
                .collect(Collectors.toList());
        List<OcRoleMenu> roleMenuList = ocRoleMenuService.queryOcRoleMenuByRoleIdList(roleIdList);
        return rendererMenuList(roleMenuList);
    }

    @CacheEvict(cacheNames = CachingConfig.CACHE_NAME_COMMON_BY_DAY, key = "'menuDecorator_username_' + #username")
    public void evictMenuList(String username) {
    }

    private List<MenuVO> rendererMenuList(List<OcRoleMenu> roleMenuList) {
        List<OcSubmenu> ocSubmenuList = querySubmenu(roleMenuList);
        List<MenuParam.menu> menuList = decoratorTemp(ocSubmenuList);
        return decoratorUserMenuList(menuList);
    }

    private List<MenuParam.menu> decoratorTemp(List<OcSubmenu> ocSubmenuList) {
        Map<Integer, List<OcSubmenu>> map = ocSubmenuList.stream().collect(Collectors.groupingBy(OcSubmenu::getMenuId));
        List<MenuParam.menu> menuList = Lists.newArrayListWithCapacity(map.size());
        map.forEach((k, y) -> {
            List<OcSubmenu> sort = y.stream()
                    .sorted(Comparator.comparing(OcSubmenu::getSubmenuOrder))
                    .collect(Collectors.toList());
            OcMenu ocMenu = ocMenuService.queryOcMenu(k);
            MenuParam.menu menu = MenuParam.menu.builder()
                    .menuTitle(ocMenu.getMenuTitle())
                    .menuIcon(ocMenu.getMenuIcon())
                    .menuOrder(ocMenu.getMenuOrder())
                    .submenuList(sort)
                    .build();
            menuList.add(menu);
        });
        return menuList.stream()
                .sorted(Comparator.comparing(MenuParam.menu::getMenuOrder))
                .collect(Collectors.toList());
    }

    private List<MenuVO> decoratorUserMenuList(List<MenuParam.menu> menuList) {
        List<MenuVO> menuVOList = Lists.newArrayListWithCapacity(menuList.size());
        menuList.forEach(menu -> menuVOList.add(decoratorUserMenu(menu)));
        return menuVOList;
    }

    private MenuVO decoratorUserMenu(MenuParam.menu menu) {
        return MenuVO.builder()
                .icon(menu.getMenuIcon())
                .title(menu.getMenuTitle())
                .children(decoratorMenuChildrenList(menu.getSubmenuList()))
                .build();
    }

    private MenuChildrenVO decoratorMenuChildren(OcSubmenu ocSubmenu) {
        MenuChildrenVO menuChildren = MenuChildrenVO.builder()
                .path(ocSubmenu.getSubmenuPath())
                .title(ocSubmenu.getSubmenuTitle())
                .build();
        if (ocSubmenu.getIsSvg()) {
            menuChildren.setIconSvg(ocSubmenu.getSubmenuIcon());
        } else {
            menuChildren.setIcon(ocSubmenu.getSubmenuIcon());
        }
        return menuChildren;
    }

    private List<MenuChildrenVO> decoratorMenuChildrenList(List<OcSubmenu> ocSubmenuList) {
        List<MenuChildrenVO> childrenVOList = Lists.newArrayListWithCapacity(ocSubmenuList.size());
        ocSubmenuList.forEach(ocSubmenu -> childrenVOList.add(decoratorMenuChildren(ocSubmenu)));
        return childrenVOList;
    }

    public String renderMenuTemplate(List<OcRoleMenu> roleMenuList) {
        List<OcSubmenu> ocSubmenuList = querySubmenu(roleMenuList);
        Map<String, Object> map = ObjectUtils.objectToMap(MenuParam.TempBuild.builder().menuList(decoratorTemp(ocSubmenuList)).build());
        OcFileTemplate template = ocFileTemplateService.queryOcFileTemplateByUniqueKey(FILE_TEMPLATE_OC_MENU, 0);
        try {
            return BeetlUtils.renderTemplate(template.getContent(), map);
        } catch (IOException e) {
            return Strings.EMPTY;
        }
    }

    private List<OcSubmenu> querySubmenu(List<OcRoleMenu> roleMenuList) {
        List<Integer> submenuIdList = roleMenuList.stream().map(OcRoleMenu::getSubmenuId).collect(Collectors.toList());
        return ocSubmenuService.queryOcSubmenuByIdList(submenuIdList);
    }
}
