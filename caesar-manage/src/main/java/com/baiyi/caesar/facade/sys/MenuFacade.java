package com.baiyi.caesar.facade.sys;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.param.sys.MenuParam;
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

    BusinessWrapper<List<MenuVO.Menu>> queryMenu();

    BusinessWrapper<List<MenuVO.MenuChild>> queryMenuChild(Integer id);

    void delMenu(Integer id);

    void delMenuChild(Integer id);
}
