package com.baiyi.caesar.service.sys;

import com.baiyi.caesar.domain.generator.caesar.Menu;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/1 5:08 下午
 * @Version 1.0
 */
public interface MenuService {

    void add(Menu menu);

    void update(Menu menu);

    void del(Integer id);

    Menu getById(Integer id);

    List<Menu> listAll();
}
