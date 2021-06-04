package com.baiyi.caesar.facade.sys;

import com.baiyi.caesar.domain.generator.caesar.AuthRoleMenu;
import com.baiyi.caesar.domain.param.sys.MenuParam;
import com.baiyi.caesar.domain.vo.common.TreeVO;
import com.baiyi.caesar.domain.vo.sys.MenuVO;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/2 10:21 上午
 * @Since 1.0
 */
public interface MenuFacade {

    void saveMenu(MenuParam.MenuSave param);

    void saveMenuChild(MenuParam.MenuChildSave param) ;

    List<MenuVO.Menu> queryMenu();

    List<MenuVO.MenuChild> queryMenuChild(Integer id);

    void delMenuById(Integer id);

    void delMenuChildById(Integer id);

    List<TreeVO.Tree> queryMenuTree();

    void saveAuthRoleMenu(MenuParam.AuthRoleMenuSave param);

    List<AuthRoleMenu> queryAuthRoleMenu(Integer roleId);

    List<MenuVO.Menu> queryAuthRoleMenuDetail(Integer roleId);

    List<MenuVO.Menu> queryMyMenu();
}
