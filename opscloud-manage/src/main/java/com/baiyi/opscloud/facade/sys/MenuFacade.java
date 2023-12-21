package com.baiyi.opscloud.facade.sys;

import com.baiyi.opscloud.domain.generator.opscloud.AuthRoleMenu;
import com.baiyi.opscloud.domain.param.sys.MenuParam;
import com.baiyi.opscloud.domain.vo.common.TreeVO;
import com.baiyi.opscloud.domain.vo.sys.MenuVO;

import java.util.List;

/**
 * @Author 修远
 * @Date 2021/6/2 10:21 上午
 * @Since 1.0
 */
public interface MenuFacade {

    void saveMenu(MenuParam.MenuSave param);

    void saveMenuChild(MenuParam.MenuChildSave param) ;

    List<MenuVO.Menu> queryMenu();

    List<MenuVO.Child> queryMenuChild(Integer id);

    void delMenuById(Integer id);

    void delMenuChildById(Integer id);

    List<TreeVO.Tree> queryMenuTree();

    void saveAuthRoleMenu(MenuParam.AuthRoleMenuSave param);

    List<AuthRoleMenu> queryAuthRoleMenu(Integer roleId);

    List<MenuVO.Menu> queryAuthRoleMenuDetail(Integer roleId);

    List<MenuVO.Menu> queryMyMenu();

}