package com.baiyi.caesar.service.sys.impl;

import com.baiyi.caesar.domain.generator.caesar.Menu;
import com.baiyi.caesar.mapper.caesar.MenuMapper;
import com.baiyi.caesar.service.sys.MenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/1 5:09 下午
 * @Version 1.0
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    public void add(Menu menu) {
        menuMapper.insert(menu);
    }

    @Override
    public void update(Menu menu) {
        menuMapper.updateByPrimaryKey(menu);
    }

    @Override
    public void del(Integer id) {
        menuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Menu getById(Integer id) {
        return menuMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Menu> listAll() {
        return menuMapper.selectAll();
    }
}
