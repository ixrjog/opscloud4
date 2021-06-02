package com.baiyi.caesar.facade.sys.impl;

import com.baiyi.caesar.common.exception.common.CommonRuntimeException;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.Menu;
import com.baiyi.caesar.domain.generator.caesar.MenuChild;
import com.baiyi.caesar.domain.param.sys.MenuParam;
import com.baiyi.caesar.facade.sys.MenuFacade;
import com.baiyi.caesar.packer.sys.MenuPacker;
import com.baiyi.caesar.service.sys.MenuChildService;
import com.baiyi.caesar.service.sys.MenuService;
import com.baiyi.caesar.domain.vo.sys.MenuVO;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

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
        // todo清除缓存
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
        // todo清除缓存
    }

    private Boolean validMenuChildList(List<MenuChild> menuChildList) {
        return menuChildList.stream().allMatch(x ->
                Strings.isNotBlank(x.getTitle())
                        && Strings.isNotBlank(x.getIcon())
                        && Strings.isNotBlank(x.getPath())
        );
    }

    @Override
    public BusinessWrapper<List<MenuVO.Menu>> queryMenu() {
        List<Menu> menuList = menuService.listAll();
        List<MenuVO.Menu> menuVOList = menuPacker.toVOList(menuList);
        return new BusinessWrapper<>(menuVOList);
    }

    @Override
    public BusinessWrapper<List<MenuVO.MenuChild>> queryMenuChild(Integer id) {
        List<MenuChild> menuChildList = menuChildService.listByMenuId(id);
        List<MenuVO.MenuChild> menuChildVOList = menuPacker.toChildVOList(menuChildList);
        return new BusinessWrapper<>(menuChildVOList);
    }

    @Override
    public void delMenu(Integer id) {
        List<MenuChild> menuChildList = menuChildService.listByMenuId(id);
        if (!CollectionUtils.isEmpty(menuChildList))
            throw new CommonRuntimeException(ErrorEnum.MENU_CHILD_IS_NOT_EMPTY);
        menuService.del(id);
        // todo清除缓存
    }

    @Override
    public void delMenuChild(Integer id) {
        menuChildService.del(id);
        // todo清除缓存
    }
}
