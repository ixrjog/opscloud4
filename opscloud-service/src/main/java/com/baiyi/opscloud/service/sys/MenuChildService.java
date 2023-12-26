package com.baiyi.opscloud.service.sys;

import com.baiyi.opscloud.domain.generator.opscloud.MenuChild;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/1 5:10 下午
 * @Version 1.0
 */
public interface MenuChildService {

    void add(MenuChild menuChild);

    void update(MenuChild menuChild);

    void del(Integer id);

    MenuChild getById(Integer id);

    List<MenuChild> listByMenuId(Integer menuId);

    List<MenuChild> listByIdList(List<Integer> idList);

}